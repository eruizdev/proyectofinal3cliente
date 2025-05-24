package com.example.demo.api;

import com.example.demo.util.TokenStore;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Inyecta automáticamente el header Authorization en cada petición.
 */
public class AuthInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request req = chain.request();
        String token = TokenStore.getToken();
        if (token != null) {
            req = req.newBuilder()
                     .addHeader("Authorization", "Bearer " + token)
                     .build();
        }
        return chain.proceed(req);
    }
}