package com.suhongjin.nightlight;

import android.app.Application;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/** Provides application context. */
@Module
class AppModule {

  Application applicaton;

  AppModule(Application application) {
    this.applicaton = application;
  }

  @Provides
  @Singleton
  Application providesApplication() {
    return applicaton;
  }
}
