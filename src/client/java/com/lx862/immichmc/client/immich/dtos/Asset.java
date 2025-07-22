package com.lx862.immichmc.client.immich.dtos;

import com.google.gson.annotations.SerializedName;

public record Asset(String checksum, String deviceAssetId, String deviceId, String duplicateId, String duration, String fileCreatedAt, String fileModifiedAt, boolean hasMetadata, String id, boolean isArchived, boolean isFavorite, boolean isOffline, boolean isTrashed, String livePhotoVideoId, String localDateTime, String originalFileName, String originalMimeType, String originalPath, User owner, String ownerId, boolean resized, String thumbhash, String updatedAt, AssetVisibility visibility) {
    public record AssetExifInfo(String city, String country, String dateTimeOriginal, String description, double exifImageHeight, double exifImageWidth, String exposureTime, double fNumber, long fileSizeInByte, double focalLength, double iso, double latitude, String lensModel, double longitude, String make, String model, String modifyDate, String orientation, String projectionType, double rating, String state, String timeZone) {}

    public enum AssetVisibility {
        @SerializedName("archive")
        ARCHIVE,
        @SerializedName("timeline")
        TIMELINE,
        @SerializedName("hidden")
        HIDDEN,
        @SerializedName("locked")
        LOCKED
    }

    public enum AssetOrder {
        @SerializedName("asc")
        ASCENDING,
        @SerializedName("desc")
        DESCENDING
    }
}
