package com.suhongjin.nightlight;

/** Keeps track of the state of the nightlight. */
public final class NightlightState {

  private int redValue, greenValue, blueValue;
  private boolean isOn;

  private static NightlightState instance;

  public static synchronized NightlightState getInstance() {
    if (instance == null) {
        instance = new NightlightState();
    }
    return instance;
  }

  private NightlightState() {
    redValue = greenValue = blueValue = 0;
    isOn = false;
  }

  /*
   * Updates the proper color value based on the idTag.
   *
   * @param value The new value to set the color to.
   * @param idTag The corresponding String tag to determine which color to update.
   * @throws AssertionError If an idTag does not correspond to a color tag.
   */
  public void updateColorValue(int value, String idTag) {
    switch (idTag) {
      case Utils.RED_TAG:
        redValue = value;
        break;
      case Utils.GREEN_TAG:
        greenValue = value;
        break;
      case Utils.BLUE_TAG:
        blueValue = value;
        break;
      default:
        throw new AssertionError("Received unexpected tag in seekbar.");
    }
  }

  public void setNightlightPower(boolean isOn) {
    this.isOn = isOn;
  }

  public int getRedValue() {
    return redValue;
  }

  public int getGreenValue() {
    return greenValue;
  }

  public int getBlueValue() {
    return blueValue;
  }

  public boolean isNightlightOn() {
    return isOn;
  }
}
