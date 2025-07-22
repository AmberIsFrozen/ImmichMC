package com.lx862.immichmc.client.immich;

public class ImmichUtil {
    public static String rectifyApiUrl(String apiUrl) {
        if (apiUrl.endsWith("/")) return apiUrl.substring(0, apiUrl.length() - 1);
        return apiUrl;
    }
}
