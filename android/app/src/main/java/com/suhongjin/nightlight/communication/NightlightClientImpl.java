package com.suhongjin.nightlight.communication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.suhongjin.nightlight.NightlightApplication;
import com.suhongjin.nightlight.R;
import com.suhongjin.nightlight.Utils;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/** Client that sends request to the server. */
public final class NightlightClientImpl implements NightlightClient {

  private static final String TAG = NightlightClient.class.getSimpleName();

  @Inject
  Retrofit.Builder retrofitBuilder;
  @Inject
  SharedPreferences sharedPreferences;

  private Retrofit retrofit;
  private RestService restService;
  private Context context;
  private String url;

  public NightlightClientImpl(Context context) {
    ((NightlightApplication) context.getApplicationContext()).getAppComponent().inject(this);
    this.context = context;
    url = sharedPreferences.getString(context.getString(R.string.ip_key), Utils.INITIAL_SERVER_URL);
    updateServices();
  }

  @Override
  public void updateUrl(String newUrl) {
    SharedPreferences.Editor editor = sharedPreferences.edit();
    editor.putString(context.getString(R.string.ip_key), newUrl);
    editor.apply();
    url = newUrl;
    updateServices();
  }

  @Override
  public void sendColorChangeRequest(int r, int g, int b, Callback<ResponseBody> response) {
    Call<ResponseBody> call = restService.sendColorChangeRequest(new ColorBody(r, g, b));
    call.enqueue(response);
  }

  @Override
  public void getNightlightColor(Callback<ColorBody> response) {
    Log.d(TAG, "Requesting nightlight color.");
    Call<ColorBody> call = restService.getNightlightColor();
    call.enqueue(response);
  } 

  @Override
  public void getNightlightPowerState(Callback<Boolean> response) {
    Log.d(TAG, "Requesting nightlight power state.");
    Call<Boolean> call = restService.getNightlightPowerState();
    call.enqueue(response);
  }

  @Override
  public void sendPowerStateFlip(Callback<Boolean> response) {
    Call<Boolean> call = restService.flipPowerSwitch();
    call.enqueue(response);
  }

  private void updateServices() {
    retrofit = retrofitBuilder.baseUrl(url).build();
    restService = retrofit.create(RestService.class);
  }
}
