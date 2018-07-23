package com.suhongjin.nightlight.communication;

import okhttp3.ResponseBody;
import retrofit2.Callback;

/** Interface to communicate with the nightlight. */
public interface NightlightClient {

  /** Update server url information. */
  void updateUrl(String newUrl);

  /** Change the color of nightlight. */
  void sendColorChangeRequest(int r, int g, int b, Callback<ResponseBody> response);

  /** Retrieve the color of the nightlight. */
  void getNightlightColor(Callback<ColorBody> response);

  /** Retrieve information on whether the nightlight is on/off. */
  void getNightlightPowerState(Callback<Boolean> response);

  /** Request nightlight to switch on/off. */
  void sendPowerStateFlip(Callback<Boolean> response);
}
