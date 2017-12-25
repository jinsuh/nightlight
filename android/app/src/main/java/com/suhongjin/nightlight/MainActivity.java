package com.suhongjin.nightlight;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SeekBar redSeekbar, greenSeekbar, blueSeekbar;
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
    }

    @Override
    /* Update NightlightState color. */
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            String idTag = seekBar.getTag().toString();
            nightlightState.updateValue(progress, idTag);
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    /* Sends a request via nightlightClient to change the nightlight color. */
    public void onStopTrackingTouch(SeekBar seekBar) {
        try {
            nightlightClient.sendColorChangeRequest(
                    nightlightState.getRedValue(),
                    nightlightState.getGreenValue(),
                    nightlightState.getBlueValue(),
                    new NightlightClientCallback());
        } catch (IOException | JSONException e) {
            Log.d(TAG, "Exception: " + e.toString());
        }
    }

    /* Callback when the request to NightlightClient is done. */
    public class NightlightClientCallback implements Callback {

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.e(TAG, "Failure on callback: " + e.toString());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            Log.d(TAG, "Success on updating nightlight.");
        }
    }
}
