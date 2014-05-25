package com.heroicrobot.pixelpusherphoton;

import java.util.Observable;
import java.util.Observer;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.heroicrobot.dropbit.registry.DeviceRegistry;

public class RegistryService extends Service {

	class PixelPusherObserver implements Observer {

		@Override
		public void update(Observable registry, Object updatedDevice) {

		}

	}

	public DeviceRegistry registry;
	private PixelPusherObserver observer;
	final Messenger mMessenger = new Messenger(new IncomingHandler());

	@Override
	public IBinder onBind(Intent arg0) {
		return mMessenger.getBinder();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		registry.stopPushing();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		registry = new DeviceRegistry();
		observer = new PixelPusherObserver();
		registry.addObserver(observer);
		registry.setAntiLog(true);
	}
	
	class IncomingHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
		
	}

}
