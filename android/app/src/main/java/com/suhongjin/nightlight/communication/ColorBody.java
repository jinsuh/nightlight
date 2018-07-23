package com.suhongjin.nightlight.communication;

import com.google.gson.annotations.SerializedName;
import com.suhongjin.nightlight.Utils;

/** Class for nightlight color  to be serialized for {@link RestService#sendColorChangeRequest}. */
public final class ColorBody {

  @SerializedName(Utils.POST_RED_PARAM)
  private int red;

  @SerializedName(Utils.POST_GREEN_PARAM)
  private int green;

  @SerializedName(Utils.POST_BLUE_PARAM)
  private int blue;

  public ColorBody(int r, int g, int b) {
    this.red = r;
    this.green = g;
    this.blue = b;
  }

  public int getRed() {
    return red;
  }

  public int getGreen() {
    return green;
  }

  public int getBlue() {
    return blue;
  }
}
