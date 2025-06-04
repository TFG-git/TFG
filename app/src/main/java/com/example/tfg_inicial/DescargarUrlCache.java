package com.example.tfg_inicial;

import java.util.HashMap;
import java.util.Map;

public class DescargarUrlCache {
    private static final Map<String, String> cache = new HashMap<>();

    public static void getUrl(String storagePath, OnDownloadUrlReady callback) {
        if (cache.containsKey(storagePath)) {
            callback.onReady(cache.get(storagePath));
        } else {
            com.google.firebase.storage.FirebaseStorage.getInstance()
                    .getReference().child(storagePath)
                    .getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        cache.put(storagePath, uri.toString());
                        callback.onReady(uri.toString());
                    })
                    .addOnFailureListener(e -> {
                        callback.onReady(null);
                    });
        }
    }

    public interface OnDownloadUrlReady {
        void onReady(String url);
    }
}
