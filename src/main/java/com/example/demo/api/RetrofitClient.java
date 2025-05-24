package com.example.demo.api;

import com.google.gson.*;
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
            // 1) Definir un Gson personalizado que maneje LocalDate
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        // Convierte el string JSON a LocalDate
                        return LocalDate.parse(json.getAsString());
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                    @Override
                    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                        // Convierte el LocalDate a un string JSON
                        return new JsonPrimitive(src.toString());
                    }
                })
                .create();

            // 2) Crear la instancia de Retrofit con Scalars y Gson personalizados
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())    // para respuestas tipo String
                    .addConverterFactory(GsonConverterFactory.create(gson))   // para objetos JSON con LocalDate
                    .build();
        }
        return retrofit;
    }
}
