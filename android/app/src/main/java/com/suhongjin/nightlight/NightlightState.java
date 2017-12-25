package com.suhongjin.nightlight;

/**
 * Keeps track of the state of the nightlight.
 */

public class NightlightState {

    public static final String RED_TAG = "red tag";
    public static final String GREEN_TAG = "green tag";
    public static final String BLUE_TAG = "blue tag";

    private int redValue, greenValue, blueValue;

    private static NightlightState instance;

    public static synchronized NightlightState getInstance() {
        if (instance == null) {
            instance = new NightlightState();
        }
        return instance;
    }

    private NightlightState() {
        redValue = greenValue = blueValue = 0;
    }

    /*
     * Updates the proper color value based on the idTag.
     * @param value The new value to set the color to.
     * @param idTag The corresponding String tag to determine which color to update.
     * @throws AssertionError If an idTag does not correspond to a color tag.
     */
    public void updateValue(int value, String idTag) {
        switch (idTag) {
            case RED_TAG:
                redValue = value;
                break;
            case GREEN_TAG:
                greenValue = value;
                break;
            case BLUE_TAG:
                blueValue = value;
                break;
            default:
                throw new AssertionError("Received unexpected tag in seekbar.");
        }
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
}
