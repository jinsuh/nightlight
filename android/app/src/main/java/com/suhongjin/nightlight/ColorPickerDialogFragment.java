package com.suhongjin.nightlight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Display a DialogFragment to manually choose a color for the nightlight.
 */

public class ColorPickerDialogFragment extends DialogFragment implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = ColorPickerDialogFragment.class.getSimpleName();

    private SeekBar redSeekbar, greenSeekbar, blueSeekbar;
    private View colorView;
    private NightlightState nightlightState;
    private NightlightClient nightlightClient;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View layoutView = inflater.inflate(R.layout.dialog_colorpicker, null);

        builder.setView(layoutView)
                .setPositiveButton(
                        R.string.done, (dialogInterface, i) -> {
                            // Update colors one more time, send color request and then exit dialog.
                            updateColors();
                            try {
                                nightlightClient.sendColorChangeRequest(
                                        nightlightState.getRedValue(),
                                        nightlightState.getGreenValue(),
                                        nightlightState.getBlueValue(),
                                        new NightlightSetColorCallback());
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                            ColorPickerDialogFragment.this.getDialog().dismiss();
                });


        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        nightlightState = NightlightState.getInstance();
        nightlightClient = NightlightClient.getInstance(getActivity());
        redSeekbar = getDialog().findViewById(R.id.red_seekbar);
        redSeekbar.setTag(NightlightState.RED_TAG);
        greenSeekbar = getDialog().findViewById(R.id.green_seekbar);
        greenSeekbar.setTag(NightlightState.GREEN_TAG);
        blueSeekbar = getDialog().findViewById(R.id.blue_seekbar);
        blueSeekbar.setTag(NightlightState.BLUE_TAG);

        redSeekbar.setOnSeekBarChangeListener(this);
        greenSeekbar.setOnSeekBarChangeListener(this);
        blueSeekbar.setOnSeekBarChangeListener(this);

        colorView = getDialog().findViewById(R.id.color_preview);
        initColor();
    }

    private void initColor() {
        nightlightClient.getNightlightColor(
                new NightlightGetColorCallback());

    }

    private void updateColors() {
        colorView.setBackgroundColor(Color.rgb(
                nightlightState.getRedValue(),
                nightlightState.getGreenValue(),
                nightlightState.getBlueValue()));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String idTag = seekBar.getTag().toString();
        nightlightState.updateColorValue(progress, idTag);
        Log.d(TAG, "updated: " + idTag + ", " + progress);
        updateColors();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        String idTag = seekBar.getTag().toString();
        int progress = seekBar.getProgress();
        nightlightState.updateColorValue(progress, idTag);
        Log.d(TAG, "updated: " + idTag + ", " + progress);
        updateColors();
    }

    /* Callback when the update color request to NightlightClient is done. */
    public class NightlightSetColorCallback implements Callback {

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.e(TAG, "Failure on callback: " + e.toString());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            Log.d(TAG, "Success on color callback.");
        }
    }

    /* Callback when the update color request to NightlightClient is done. */
    public class NightlightGetColorCallback implements Callback {

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            Log.e(TAG, "Failure on callback: " + e.toString());
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            try {
                JSONObject json = new JSONObject(response.body().string());
                redSeekbar.setProgress(json.getInt("red"));
                greenSeekbar.setProgress(json.getInt("green"));
                blueSeekbar.setProgress(json.getInt("blue"));
                Log.d(TAG, "Success on color callback.");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
