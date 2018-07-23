package com.suhongjin.nightlight.communication;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/** Rest API interfaces for server communication. */
interface RestService {

  /** Retrieve the nightlight color as a {@link ColorBody}. */
  @GET("/getrgb")
  Call<ColorBody> getNightlightColor();

  /** Retrieve whether the nightlight is on or off. */
  @GET("/power")
  Call<Boolean> getNightlightPowerState();

  /** Send request to change the color of the nightlight. */
  @POST("/setrgb")
  Call<ResponseBody> sendColorChangeRequest(@Body ColorBody colorBody);

  /** Send request to flip power of the nightlight on/off. */
  @POST("/flip")
  Call<Boolean> flipPowerSwitch();
  
}
