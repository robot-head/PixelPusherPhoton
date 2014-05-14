package com.heroicrobot.pixelpusherphoton;

import java.util.Observable;
import java.util.Observer;

import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.dropbit.registry.DeviceRegistry;

import android.app.Application;
import android.util.Log;

public class PixelPusherPhotonApplication extends Application {

	public static final String TAG = "PixelPusherPhoton";

	class pusherObserver implements Observer {

		@Override
		public void update(Observable registry, Object updatedDevice) {
			if (updatedDevice != null) {
				PixelPusher device = (PixelPusher) updatedDevice;
				Log.i(TAG, "Updated pusher: " + device.toString());
			}
		}

	}

	DeviceRegistry registry;
	pusherObserver observer;

	@Override
	public void onCreate() {
		super.onCreate();
		registry = new DeviceRegistry();
		observer = new pusherObserver();
		registry.addObserver(observer);
		registry.setAntiLog(true);
	}

}
