package com.suhongjin.nightlight;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * UI Test that runs with espresso.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityUITest {

    private MainActivity activity;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void setUp() {
        activity = activityTestRule.getActivity();
    }

    @Test
    public void testInitialScreen() {
        onView(withId(R.id.power_button)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.manual_button)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void testColorPickerDialog() {
        /* Initial dialog screen. */
        onView(withId(R.id.manual_button)).perform(click());
        onView(withId(R.id.red_seekbar)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.green_seekbar)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.blue_seekbar)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.color_preview)).check(
                matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        /* Changing a seekbar value updates NightlightState. */
        int expectedRedValue = 20;
        onView(withId(R.id.red_seekbar)).perform(setProgress(expectedRedValue));
        assertThat(NightlightState.getInstance().getRedValue(), is(expectedRedValue));
    }

    @Test(expected = NoMatchingViewException.class)
    public void testColorPickerDialogDismissal() {
        /* Dismisses dialog properly. */
        onView(withId(R.id.manual_button)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(R.id.red_seekbar)).check(matches(not(isDisplayed())));
    }

    /* Sets a SeekBar progress. */
    public static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }

            @Override
            public String getDescription() {
                return "Setting a progress bar";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SeekBar) view).setProgress(progress);
            }
        };
    }
}