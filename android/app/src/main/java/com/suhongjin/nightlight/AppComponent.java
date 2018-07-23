package com.suhongjin.nightlight;

import com.suhongjin.nightlight.communication.NightlightClientImpl;
import com.suhongjin.nightlight.communication.NightlightClientModule;
import javax.inject.Singleton;
import dagger.Component;

/** Application bound modules */
@Component(modules={AppModule.class, NetModule.class, NightlightClientModule.class})
@Singleton
public interface AppComponent {
  void inject(MainActivity activity);
  void inject(NightlightClientImpl nightlightClient);
}
