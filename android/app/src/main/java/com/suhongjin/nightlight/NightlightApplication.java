package com.suhongjin.nightlight;

import android.app.Application;

import com.suhongjin.nightlight.communication.NightlightClientModule;

/** Dagger instantiation. */
public class NightlightApplication extends Application {

  private AppComponent appComponent;

  @Override
  public void onCreate() {
    super.onCreate();

    appComponent =
        DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .netModule(new NetModule())
            .nightlightClientModule(new NightlightClientModule())
            .build();

  }

  public AppComponent getAppComponent() {
    return appComponent;
  }
}
