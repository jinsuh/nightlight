package com.suhongjin.nightlight;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Local unit test of NightlightState.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NightlightStateTest {

    private NightlightState nightlightState;

    @Before
    public void setUp() throws SecurityException,
            NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        // Reflection to reset NightlightState instance with each test.
        Field instance = NightlightState.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
        nightlightState = NightlightState.getInstance();
    }

    @Test
    /* Test default values. */
    public void testInitialState()  {
        assertEquals(nightlightState.getRedValue(), 0);
        assertEquals(nightlightState.getGreenValue(), 0);
        assertEquals(nightlightState.getBlueValue(), 0);
    }

    @Test(expected = AssertionError.class)
    /* Expect an AssertionError when an invalid tag is used. */
    public void testUpdateValueThrowsAssertionErrorWithInvalidTag() {
        nightlightState.updateColorValue(23, "invalid tag");
    }

    @Test
    /* Expect each value to be updated properly with the right tags. */
    public void testUpdateValueSetsCorrectVariable() {
        int expectedRedValue = 43;
        nightlightState.updateColorValue(expectedRedValue, NightlightState.RED_TAG);
        assertEquals(nightlightState.getRedValue(), expectedRedValue);
        assertEquals(nightlightState.getGreenValue(), 0);
        assertEquals(nightlightState.getBlueValue(), 0);

        int expectedGreenValue = 25;
        nightlightState.updateColorValue(expectedGreenValue, NightlightState.GREEN_TAG);
        assertEquals(nightlightState.getRedValue(), expectedRedValue);
        assertEquals(nightlightState.getGreenValue(), expectedGreenValue);
        assertEquals(nightlightState.getBlueValue(), 0);

        int expectedBlueValue = 1;
        nightlightState.updateColorValue(expectedBlueValue, NightlightState.BLUE_TAG);
        assertEquals(nightlightState.getRedValue(), expectedRedValue);
        assertEquals(nightlightState.getGreenValue(), expectedGreenValue);
        assertEquals(nightlightState.getBlueValue(), expectedBlueValue);
    }

    @Test
    public void testSetNightlightPower() {
        nightlightState.setNightlightPower(true);
        assertTrue(nightlightState.isNightlightOn());
        nightlightState.setNightlightPower(false);
        assertFalse(nightlightState.isNightlightOn());
    }
}