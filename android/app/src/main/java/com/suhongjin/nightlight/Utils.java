package com.suhongjin.nightlight;

/** Holds constants for use. */
public final class Utils {

  // ID Tags
  public static final String RED_TAG = "red tag";
  public static final String GREEN_TAG = "green tag";
  public static final String BLUE_TAG = "blue tag";

  // Server params
  public static final String POST_RED_PARAM = "red";
  public static final String POST_GREEN_PARAM = "green";
  public static final String POST_BLUE_PARAM = "blue";

  public static final String INITIAL_SERVER_URL = 
      "http://127.0.0.1:8080";
  public static final String IS_POWER_ON_HANDLER = "/power";
  public static final String UPDATE_COLOR_HANDLER = "/setrgb";
  public static final String GET_COLOR_HANDLER = "/getrgb";
  public static final String FLIP_POWER_HANDLER = "/flip";

  private Utils() {}
}
