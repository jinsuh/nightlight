package com.suhongjin.nightlight;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.suhongjin.nightlight.communication.NightlightClient;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  private final Callback<Boolean> flipPowerResponse =
      new Callback<Boolean>() {
        @Override
        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
          Log.d(TAG, "Flip power: " + response.body());
          nightlightState.setNightlightPower(response.body());
          updateButton();
        }

        @Override
        public void onFailure(Call<Boolean> call, Throwable t) {
          Toast.makeText(
              MainActivity.this,
              R.string.server_connection_error,
              Toast.LENGTH_LONG).show();
        }
      };
  private final Callback<Boolean> isPowerOnResponse =
      new Callback<Boolean>() {
        @Override
        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
          Log.d(TAG, "Is power on: " + response.body());
          nightlightState.setNightlightPower(response.body());
          updateButton();
        }

        @Override
        public void onFailure(Call<Boolean> call, Throwable t) {
          //TODO: Should there be a reconnect dialog instead?
          Toast.makeText(
              MainActivity.this,
              R.string.server_connection_error,
              Toast.LENGTH_LONG).show();
        }
      };

  @Inject
  NightlightClient nightlightClient;

  private Button powerButton;
  private NightlightState nightlightState;
  private ActionBarDrawerToggle drawerToggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    NightlightApplication app =((NightlightApplication) getApplication());
    app.getAppComponent().inject(this);

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    nightlightState = NightlightState.getInstance();

    powerButton = findViewById(R.id.power_button);
    powerButton.setOnClickListener(
        (view) -> nightlightClient.sendPowerStateFlip(flipPowerResponse));
    Button manualButton = findViewById(R.id.manual_button);
    manualButton.setOnClickListener((view) -> {
      ColorPickerDialogFragment dialogFragment = new ColorPickerDialogFragment();
      dialogFragment.show(getSupportFragmentManager(), "manual");
    });
    Button autoButton = findViewById(R.id.auto_button);
    autoButton.setOnClickListener((view) -> showAutoDialog());

    DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
    drawerToggle =
        new ActionBarDrawerToggle(
            this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {  
          public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            invalidateOptionsMenu();
          }
        };
    drawerLayout.addDrawerListener(drawerToggle);
    if (getSupportActionBar() != null) {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeButtonEnabled(true);
    }
    requestNightlightState();
  }

  private void requestNightlightState() {
    nightlightClient.getNightlightPowerState(isPowerOnResponse);
  }

  /* Update the drawable of the button based on the NightlightState. */
  private void updateButton() {
    if (nightlightState.isNightlightOn()) {
      powerButton.setBackgroundResource(R.drawable.power_button_lit);
    } else {
      powerButton.setBackgroundResource(R.drawable.power_button);
    }
  }

  private void showAutoDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle("Select a choice.");
    builder.setItems(R.array.auto_choices, (dialogInterface, position) -> {
      Log.d(TAG, "Pressed: " + position);
    });
    builder.create().show();
  }
  
  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    drawerToggle.syncState();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Pass the event to ActionBarDrawerToggle, if it returns
    // true, then it has handled the app icon touch event
    return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
  }

  public void displayChangeIpDialog(MenuItem item) {
    Log.d(TAG, "Display change ip dialog.");
    LayoutInflater inflater = this.getLayoutInflater();
    View dialogView = inflater.inflate(R.layout.server_change, null);
    AlertDialog.Builder builder =
        new AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
              EditText newUrlEditText = dialogView.findViewById(R.id.new_ip);
              String newUrl = newUrlEditText.getText().toString();
              if (URLUtil.isValidUrl(newUrl)) {
                nightlightClient.updateUrl(newUrl);
              } else {
                Toast.makeText(
                    this, "Not a valid url", Toast.LENGTH_SHORT).show();
              }
            })
            .setNegativeButton(
                R.string.cancel,
                (dialogInterface, i) -> dialogInterface.dismiss());
    builder.create().show();
  } 
}
