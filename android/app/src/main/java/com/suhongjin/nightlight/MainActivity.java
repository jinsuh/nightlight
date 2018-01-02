package com.suhongjin.nightlight;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.webkit.URLUtil.isValidUrl;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button powerButton;
    private Button manualButton;
    private NightlightClient nightlightClient;
    private NightlightState nightlightState;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nightlightClient = NightlightClient.getInstance(this);
        nightlightState = NightlightState.getInstance();

        powerButton = findViewById(R.id.power_button);
        powerButton.setOnClickListener(this);
        manualButton = findViewById(R.id.manual_button);
        manualButton.setOnClickListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        requestNightlightState();
    }

    private void requestNightlightState() {
        nightlightClient.getNightlightPowerState(
                new NightlightIsPowerOnCallback());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.power_button) {
            nightlightClient.sendPowerFlipRequest(new NightlightFlipPowerCallback());
        } else if (view.getId() == R.id.manual_button) {
            ColorPickerDialogFragment dialogFragment = new ColorPickerDialogFragment();
            dialogFragment.show(getSupportFragmentManager(), "manual");
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

    /* Callback when the is power on request to NightlightClient is done. */
    public class NightlightIsPowerOnCallback implements Callback {

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            //TODO: Should there be a reconnect dialog instead?
            Log.e(TAG, "Failure on isPowerOn callback: " + e.toString());
            runOnUiThread(() -> {
                Toast.makeText(
                        MainActivity.this,
                        R.string.server_connection_error,
                        Toast.LENGTH_LONG).show();
            });
        }

        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            boolean state = Boolean.parseBoolean(response.body().string());
            Log.d(TAG, "Is power on: " + state);
            nightlightState.setNightlightPower(state);
            updateButton();
        }
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
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayChangeIpDialog(MenuItem item) {
        Log.d(TAG, "Display change ip dialog.");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.server_change, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    EditText newUrlEditText = dialogView.findViewById(R.id.new_ip);
                    String newUrl = newUrlEditText.getText().toString();
                    if (isValidUrl(newUrl)) {
                        nightlightClient.updateUrl(newUrl);
                    } else {
                        Toast.makeText(
                                this, "Not a valid url", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) ->
                        dialogInterface.dismiss());
        builder.create().show();
    }

    /* Callback when the flip power request to NightlightClient is done. */
    public class NightlightFlipPowerCallback implements Callback {

        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            runOnUiThread(() -> {
                Toast.makeText(
                        MainActivity.this,
                        R.string.server_connection_error,
                        Toast.LENGTH_LONG).show();
            });
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
