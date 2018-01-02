package com.suhongjin.nightlight;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Client that sends request to the server.
 */

public class NightlightClient {

    private static NightlightClient instance;

    private static final String TAG = NightlightClient.class.getSimpleName();
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    private Context context;
    private String url;

    public static synchronized NightlightClient getInstance(Context context) {
        if (instance == null) {
            instance = new NightlightClient(context);
        }
        return instance;
    }

    private NightlightClient(Context context) {
        client = new OkHttpClient();
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
        url = sharedPreferences.getString(
                context.getString(R.string.ip_key), Utils.INITIAL_SERVER_URL);

    }

    public void updateUrl(String newUrl) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.preferences_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.ip_key), newUrl);
        editor.commit();
        url = newUrl;
    }

    public void sendColorChangeRequest(
            final int r,
            final int g,
            final int b,
            final Callback callback) throws IOException, JSONException {
        Log.d(TAG, "Calling send color change request.");

        RequestBody body = RequestBody.create(JSON, createColorChangeParams(r, g, b));
        Request request = new Request.Builder().url(url + Utils.UPDATE_COLOR_HANDLER)
                .post(body).build();
        client.newCall(request).enqueue(callback);
    }

    @VisibleForTesting
    String createColorChangeParams(
            final int r,
            final int g,
            final int b) throws JSONException {
        JSONObject json = new JSONObject();
        json.put(Utils.POST_RED_PARAM, r);
        json.put(Utils.POST_GREEN_PARAM, g);
        json.put(Utils.POST_BLUE_PARAM, b);

        return json.toString();
    }

    public void getNightlightColor(Callback callback) {
        Log.d(TAG, "Requesting nightlight color.");

        Request request = new Request.Builder().url(url + Utils.GET_COLOR_HANDLER)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void getNightlightPowerState(Callback callback) {
        Log.d(TAG, "Requesting nightlight power state.");

        Request request = new Request.Builder().url(url + Utils.IS_POWER_ON_HANDLER)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void sendPowerFlipRequest(Callback callback) {
        RequestBody body = RequestBody.create(null, new byte[0]);
        Request request = new Request.Builder().url(url + Utils.FLIP_POWER_HANDLER)
                .post(body).build();
        client.newCall(request).enqueue(callback);
    }

    public String getUrl() {
        return url;
    }
}
