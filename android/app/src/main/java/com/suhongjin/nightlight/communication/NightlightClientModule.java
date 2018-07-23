package com.suhongjin.nightlight.communication;

import android.app.Application;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class NightlightClientModule {

  @Provides
  @Singleton
  NightlightClient providesNightlightClient(Application application) {
    return new NightlightClientImpl(application);
  }
}
