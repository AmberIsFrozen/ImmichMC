package com.lx862.immichmc.client.immich.dtos;

import com.google.gson.annotations.SerializedName;

public record Album(String albumName, String albumThumbnailAssetId, AlbumUser[] albumUsers, int assetCount, Asset[] assets, String createdAt, String description, String endDate, boolean hasSharedLink, String id, boolean isActivityEnabled, String lastModifiedAssetTimestamp, Asset.AssetOrder assetOrder, User owner, String ownerId, boolean shared, String startDate, String updatedAt) {
    public record AlbumUser(AlbumUserRole role, AlbumUser user) { }

    public enum AlbumUserRole {
        @SerializedName("editor")
        EDITOR,
        @SerializedName("viewer")
        VIEWER
    }
}
