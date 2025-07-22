package com.lx862.immichmc.client.immich;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class HttpResponseCallback<T> implements Callback {
    private final CompletableFuture<T> consumer;
    private final ResponseConverter<T> responseConverter;

    public HttpResponseCallback(CompletableFuture<T> completableFuture, ResponseConverter<T> responseConverter) {
        if(completableFuture == null) throw new IllegalArgumentException("completableFuture cannot be null!");
        this.consumer = completableFuture;
        this.responseConverter = responseConverter;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        consumer.completeExceptionally(e);
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) {
        try {
            consumer.complete(responseConverter.convertResponse(response));
        } catch (Exception e) {
            consumer.completeExceptionally(e);
        }
    }

    @FunctionalInterface
    public interface ResponseConverter<R> {
        R convertResponse(Response response) throws Exception;
    }
}
