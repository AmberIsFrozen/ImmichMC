package com.lx862.immichmc.client.immich.dtos;

public record User(UserAvatarColor avatarColor, String email, String id, String name, String profileChangedAt, String profileImagePath) {
    public enum UserAvatarColor {
        primary,
        pink,
        red,
        yellow,
        blue,
        green,
        purple,
        orange,
        gray,
        amber
    }
}
