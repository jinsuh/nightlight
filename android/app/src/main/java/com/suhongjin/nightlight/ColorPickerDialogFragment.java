package com.suhongjin.nightlight;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.suhongjin.nightlight.communication.ColorBody;
import com.suhongjin.nightlight.communication.NightlightClient;
import com.suhongjin.nightlight.communication.NightlightClientImpl;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/** Display a {@link DialogFragment} to manually choose a color for the nightlight. */
public final class ColorPickerDialogFragment extends DialogFragment implements 
    SeekBar.OnSeekBarChangeListener {

  private static final String TAG = ColorPickerDialogFragment.class.getSimpleName();

  private final Callback<ResponseBody> setColorResponse =
      new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
          Log.d(TAG, "Success on color callback.");
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
          Log.e(TAG, "Error in response: " + t);
        }
      };
  private final Callback<ColorBody> getColorResponse =
      new Callback<ColorBody>() {
        @Override
        public void onResponse(Call<ColorBody> call, Response<ColorBody> response) {
          ColorBody colorResponse = response.body();
          redSeekbar.setProgress(colorResponse.getRed());
          greenSeekbar.setProgress(colorResponse.getGreen());
          blueSeekbar.setProgress(colorResponse.getBlue());
        }

        @Override
        public void onFailure(Call<ColorBody> call, Throwable t) {
          Log.e(TAG, "Error in response: " + t);
        }
      };

  private SeekBar redSeekbar, greenSeekbar, blueSeekbar;
  private View colorView;
  private NightlightState nightlightState;
  private NightlightClient nightlightClient;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    LayoutInflater inflater = getActivity().getLayoutInflater();
    View layoutView = inflater.inflate(R.layout.dialog_colorpicker, null);

    return new AlertDialog.Builder(getActivity())
        .setView(layoutView)
        .setPositiveButton(R.string.done, (dialogInterface, i) -> {
          // Update colors one more time, send color request and then exit dialog.
          updateColors();
          nightlightClient.sendColorChangeRequest(
              nightlightState.getRedValue(),
              nightlightState.getGreenValue(),
              nightlightState.getBlueValue(),
              setColorResponse);
          ColorPickerDialogFragment.this.getDialog().dismiss();
        })
        .create();
  }

  @Override
  public void onStart() {
    super.onStart();

    nightlightState = NightlightState.getInstance();
    nightlightClient = new NightlightClientImpl(getActivity());
    redSeekbar = getDialog().findViewById(R.id.red_seekbar);
    redSeekbar.setTag(Utils.RED_TAG);
    greenSeekbar = getDialog().findViewById(R.id.green_seekbar);
    greenSeekbar.setTag(Utils.GREEN_TAG);
    blueSeekbar = getDialog().findViewById(R.id.blue_seekbar);
    blueSeekbar.setTag(Utils.BLUE_TAG);

    redSeekbar.setOnSeekBarChangeListener(this);
    greenSeekbar.setOnSeekBarChangeListener(this);
    blueSeekbar.setOnSeekBarChangeListener(this);

    colorView = getDialog().findViewById(R.id.color_preview);
    initColor();
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

  private void initColor() {
    nightlightClient.getNightlightColor(getColorResponse);
  }

  private void updateColors() {
    colorView.post(() ->
        colorView.setBackgroundColor(
            Color.rgb(
                nightlightState.getRedValue(),
                nightlightState.getGreenValue(),
                nightlightState.getBlueValue())));
  }
}
