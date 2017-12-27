package com.suhongjin.nightlight;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SeekBar redSeekbar, greenSeekbar, blueSeekbar;
    private Button powerButton;
    private NightlightClient nightlightClient;
    private NightlightState nightlightState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nightlightClient = NightlightClient.getInstance();
        nightlightState = NightlightState.getInstance();

        redSeekbar = findViewById(R.id.red_seekbar);
        redSeekbar.setTag(NightlightState.RED_TAG);
        greenSeekbar = findViewById(R.id.green_seekbar);
        greenSeekbar.setTag(NightlightState.GREEN_TAG);
        blueSeekbar = findViewById(R.id.blue_seekbar);
        blueSeekbar.setTag(NightlightState.BLUE_TAG);

        redSeekbar.setOnSeekBarChangeListener(this);
        greenSeekbar.setOnSeekBarChangeListener(this);
        blueSeekbar.setOnSeekBarChangeListener(this);

        powerButton = findViewById(R.id.power_button);
        powerButton.setOnClickListener(this);

        requestNightlightState();
    }

    private void requestNightlightState() {
        nightlightClient.getNightlightPowerState(
                new NightlightIsPowerOnCallback());
    }

    @Override
    /* Update NightlightState color. */
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String idTag = seekBar.getTag().toString();
        nightlightState.updateColorValue(progress, idTag);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    /* Sends a request via nightlightClient to change the nightlight color. */
    public void onStopTrackingTouch(SeekBar seekBar) {
        try {
            nightlightClient.sendColorChangeRequest(
                    nightlightState.getRedValue(),
                    nightlightState.getGreenValue(),
                    nightlightState.getBlueValue(),
                    new NightlightColorCallback());
        } catch (IOException | JSONException e) {
            Log.d(TAG, "Exception: " + e.toString());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.power_button) {
            nightlightClient.sendPowerFlipRequest(new NightlightFlipPowerCallback());
        }
    }

    /* Update the drawable of the button based on the NightlightState. */
    private void updateButton() {
        if (nightlightState.isNightlightOn()) {
            powerButton.setBackgroundResource(R.drawable.power_button_lit);
        } else {
            powerButton.setBackgroundResource(R.drawable.power_button);
        }
    }

    /* Callback when the update color request to NightlightClient is done. */
    public class NightlightColorCallback implements Callback {

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.e(TAG, "Failure on callback: " + e.toString());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            Log.d(TAG, "Success on updating nightlight.");
        }
    }

    /* Callback when the is power on request to NightlightClient is done. */
    public class NightlightIsPowerOnCallback implements Callback {

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.e(TAG, "Failure on isPowerOn callback: " + e.toString());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            boolean state = Boolean.parseBoolean(response.body().string());
            Log.d(TAG, "Is power on: " + state);
            nightlightState.setNightlightPower(state);
            updateButton();
        }
    }

    /* Callback when the flip power request to NightlightClient is done. */
    public class NightlightFlipPowerCallback implements Callback {

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Toast.makeText(
                    MainActivity.this,
                    "Could not flip power.",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        /* Set state and update the drawable on the button. */
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            boolean isOn = Boolean.parseBoolean(response.body().string());
            Log.d(TAG, "Flip power: " + isOn);
            nightlightState.setNightlightPower(isOn);
            updateButton();
        }
    }
}
