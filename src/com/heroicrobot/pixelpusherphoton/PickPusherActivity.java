package com.heroicrobot.pixelpusherphoton;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.heroicrobot.dropbit.devices.pixelpusher.PixelPusher;
import com.heroicrobot.pixelpusherphoton.RegistryService.LocalBinder;

public class PickPusherActivity extends Activity {

	ScheduledExecutorService scheduledExecutorService;
	private RegistryService myService;
	private boolean isBound;
	private ListView listView;
	private ArrayAdapter<PixelPusher> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_pusher);
		scheduledExecutorService = Executors.newScheduledThreadPool(5);
		Intent intent = new Intent(this, RegistryService.class);
		bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
		listView = (ListView) findViewById(R.id.pusherListView);
		adapter = new ArrayAdapter<PixelPusher>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				new ArrayList<PixelPusher>()) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				PixelPusher pusher = this.getItem(position);
				if (pusher.getControllerOrdinal() == 0) {
					TextView view = new TextView(parent.getContext());
					view.setText(pusher.getMacAddress());
					return view;
				}
				TextView view = new TextView(parent.getContext());
				view.setText("Group " + pusher.getGroupOrdinal()
						+ " Controller " + pusher.getControllerOrdinal());
				return view;
			}

		};
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}

		});
	}

	private ServiceConnection myConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			LocalBinder binder = (LocalBinder) service;
			myService = binder.getService();
			isBound = true;
		}

		public void onServiceDisconnected(ComponentName arg0) {
			isBound = false;
		}

	};

	public void scanButtonClicked(View view) {
		final Button scanButton = (Button) view;
		scanButton.setPressed(true);
		scanButton.setEnabled(false);
		final ProgressBar scanSpinner = (ProgressBar) findViewById(R.id.scanSpinner);
		scanSpinner.setVisibility(View.VISIBLE);
		scheduledExecutorService.schedule(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						scanSpinner.setVisibility(View.GONE);
						scanButton.setPressed(false);
						scanButton.setEnabled(true);
						adapter.clear();
						for (PixelPusher pusher : myService.getRegistry()
								.getPushers()) {
							adapter.add(pusher);
						}
					}
				});

				return null;
			}
		}, 5, TimeUnit.SECONDS);

	}
}
