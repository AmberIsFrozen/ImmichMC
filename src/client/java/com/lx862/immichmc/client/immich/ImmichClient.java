package com.lx862.immichmc.client.immich;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lx862.immichmc.client.ImmichMC;
import com.lx862.immichmc.client.immich.dtos.Album;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class ImmichClient {
    private final String apiUrl;
    private final String apiKey;
    private final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private final Gson gson = new Gson();

    public ImmichClient(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    /** Combine the result of both {@link ImmichClient#getServerConfig()} and {@link ImmichClient#getServerVersion()} */
    public CompletableFuture<InstanceInfo> getCombinedInstanceInfo() {
        return getServerVersion().thenCombine(getServerConfig(), (version, config) -> new InstanceInfo(version, config));
    }

    public CompletableFuture<ValidateTokenResponse> validateAccessToken() {
        CompletableFuture<ValidateTokenResponse> completableFuture = new CompletableFuture<>();
        Request request = newAuthorizedRequest().url(apiUrl + ImmichAPIRoutes.VALIDATE_ACCESS_TOKEN).post(RequestBody.EMPTY).build();
        httpClient.newCall(request).enqueue(new HttpResponseCallback<>(completableFuture, (response) -> convertToClass(response, ValidateTokenResponse.class)));
        return completableFuture;
    }

    public CompletableFuture<AssetUploadResponse> upload(File file) {
        CompletableFuture<AssetUploadResponse> completableFuture = new CompletableFuture<>();

        long fileCreatedAt;
        long fileModifiedAt = file.lastModified();
        try {
            fileCreatedAt = Files.readAttributes(file.toPath(), BasicFileAttributes.class).creationTime().toMillis();
        } catch (IOException e) {
            completableFuture.completeExceptionally(e);
            return completableFuture;
        }

        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("assetData", file.getName(), RequestBody.create(file, MediaType.parse("image/png")))
                .addFormDataPart("deviceAssetId", file.getName() + "-" + fileModifiedAt)
                .addFormDataPart("deviceId", ImmichMC.BRAND)
                .addFormDataPart("fileCreatedAt", new Date(fileCreatedAt).toInstant().toString())
                .addFormDataPart("fileModifiedAt", new Date(fileModifiedAt).toInstant().toString())
                .addFormDataPart("isFavorite", "false")
                .build();

        Request request = newAuthorizedRequest()
                .header("Content-Type", "multipart/form-data")
                .header("Accept", "application/json")
                .post(formBody)
                .url(apiUrl + ImmichAPIRoutes.UPLOAD_ASSETS).build();

        httpClient.newCall(request).enqueue(new HttpResponseCallback<>(completableFuture, (response) -> convertToClass(response, AssetUploadResponse.class)));
        return completableFuture;
    }

    public CompletableFuture<AddAssetsToAlbumRespnse> addAssetsToAlbum(String albumId, String... assetIds) {
        CompletableFuture<AddAssetsToAlbumRespnse> completableFuture = new CompletableFuture<>();
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("ids", gson.toJsonTree(assetIds));

        Request request = newAuthorizedRequest().url(apiUrl + ImmichAPIRoutes.ADD_ASSETS_TO_ALBUM.replace("{ID}", albumId)).put(RequestBody.create(jsonObject.toString(), MediaType.get("application/json"))).build();
        httpClient.newCall(request).enqueue(new HttpResponseCallback<>(completableFuture, (response) -> new AddAssetsToAlbumRespnse(convertToClass(response, AddAssetsToAlbumRespnse.AssetResponse[].class))));
        return completableFuture;
    }

    public CompletableFuture<GetAllAlbumsResponse> getAllAlbums() {
        CompletableFuture<GetAllAlbumsResponse> completableFuture = new CompletableFuture<>();
        Request request = newAuthorizedRequest().url(apiUrl + ImmichAPIRoutes.GET_ALL_ALBUMS).get().build();
        httpClient.newCall(request).enqueue(new HttpResponseCallback<>(completableFuture, (response) -> new GetAllAlbumsResponse(convertToClass(response, Album[].class))));
        return completableFuture;
    }

    public CompletableFuture<ServerConfigResponse> getServerConfig() {
        CompletableFuture<ServerConfigResponse> completableFuture = new CompletableFuture<>();
        Request request = newRequest().url(apiUrl + ImmichAPIRoutes.GET_SERVER_CONFIG).get().build();
        httpClient.newCall(request).enqueue(new HttpResponseCallback<>(completableFuture, (response) -> convertToClass(response, ServerConfigResponse.class)));
        return completableFuture;
    }

    public CompletableFuture<ServerVersionResponse> getServerVersion() {
        CompletableFuture<ServerVersionResponse> completableFuture = new CompletableFuture<>();
        Request request = newRequest().url(apiUrl + ImmichAPIRoutes.GET_SERVER_VERSION).get().build();
        httpClient.newCall(request).enqueue(new HttpResponseCallback<>(completableFuture, (response) -> convertToClass(response, ServerVersionResponse.class)));
        return completableFuture;
    }

    /**
     * Convert an OkHttp response to the specified class
     */
    private <T> T convertToClass(Response response, Class<T> clazz) throws IOException, ImmichException {
        if(response.isSuccessful()) {
            return gson.fromJson(response.body().string(), clazz);
        } else {
            throw new ImmichException(gson.fromJson(response.body().string(), ErrorResponse.class));
        }
    }

    /**
     * Create a new OkHttp request with the API key header
     */
    private Request.Builder newAuthorizedRequest() {
        return newRequest().header("x-api-key", apiKey);
    }

    /**
     * Create a new OkHttp request. For authenticated endpoint, please use {@link ImmichClient#newAuthorizedRequest()}.
     */
    private Request.Builder newRequest() {
        return new Request.Builder();
    }

    public record AddAssetsToAlbumRespnse(AssetResponse[] assetResponses) {
        public record AssetResponse(String id, String error, boolean success) {}
    }

    public record GetAllAlbumsResponse(Album[] albums) {}

    public record AssetUploadResponse(String id, String status) {};

    public record ValidateTokenResponse(boolean authStatus) {}

    public record ErrorResponse(String message, String error, int statusCode, String correlationId) {}

    public record ServerVersionResponse(int major, int minor, int patch) {
        @Override
        public String toString() {
            return major + "." + minor + "." + patch;
        }
    }

    public record ServerConfigResponse(String loginPageMessage, int trashDays, int userDeleteDelay, String oauthButtonText, boolean isInitialized, boolean isOnboarded, String externalDomain, boolean publicUsers) { }

    public record InstanceInfo(ServerVersionResponse version, ServerConfigResponse config) {}

    public static class ImmichException extends Exception {
        private final ErrorResponse errorResponse;

        public ImmichException(ErrorResponse errorResponse) {
            super(String.format("Got status code %d: %s (%s).\nCorrelation ID: %s", errorResponse.statusCode(), errorResponse.error(), errorResponse.message(), errorResponse.correlationId()));
            this.errorResponse = errorResponse;
        }

        public ErrorResponse getErrorResponse() {
            return errorResponse;
        }
    }
}
