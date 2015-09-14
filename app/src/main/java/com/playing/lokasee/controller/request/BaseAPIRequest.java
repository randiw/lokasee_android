package com.playing.lokasee.controller.request;

import com.android.volley.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.playing.lokasee.tools.BaseRequest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by randi on 9/11/15.
 */
public abstract class BaseAPIRequest<T> extends BaseRequest<T> {

    private static final String TAG = BaseAPIRequest.class.getSimpleName();

    public BaseAPIRequest(int method, String url, Map<String, String> headers, Type type, Response.Listener<T> successListener, Response.ErrorListener listener) {
        super(method, url, headers, type, successListener, listener);
    }

    @Override
    protected GsonBuilder gsonHandler(GsonBuilder builder) {
        builder.registerTypeAdapterFactory(new ResponseTypeAdapterFactory());
        return builder;
    }

    private class ResponseTypeAdapterFactory implements TypeAdapterFactory {
        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
            final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
            final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

            return new TypeAdapter<T>() {
                @Override
                public void write(JsonWriter out, T value) throws IOException {
                    delegate.write(out, value);
                }

                @Override
                public T read(JsonReader in) throws IOException {

                    JsonElement jsonElement = elementAdapter.read(in);
                    if (jsonElement.isJsonObject()) {
                        JsonObject jsonObject = jsonElement.getAsJsonObject();
                        if (jsonObject.has("data")) {
                            jsonElement = jsonObject.get("data");
                        }
                    }

                    return delegate.fromJsonTree(jsonElement);
                }
            }.nullSafe();
        }
    }
}