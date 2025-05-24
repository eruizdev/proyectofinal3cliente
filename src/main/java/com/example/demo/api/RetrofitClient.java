package com.example.demo.api;

import com.google.gson.*;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.lang.reflect.Type;
import java.time.LocalDate;

public class RetrofitClient {

    private static final String BASE_URL = "http://localhost:8080/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            // 1) Interceptor para agregar Authorization
            OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor())
                .build();

            // 2) Gson personalizado para LocalDate
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDate.parse(json.getAsString());
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                    @Override
                    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(src.toString());
                    }
                })
                .create();

            // 3) Construye Retrofit con OkHttpClient y converters
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}