package com.heroicrobot.pixelpusherphoton;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class PickPusherActivity extends Activity {

	ScheduledExecutorService scheduledExecutorService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_pusher);
		scheduledExecutorService = Executors.newScheduledThreadPool(5);
	}

	public void scanButtonClicked(View view) {
		final Button scanButton = (Button) view;
		scanButton.setPressed(true);
		scanButton.setEnabled(false);
		final ProgressBar scanSpinner = (ProgressBar) findViewById(R.id.scanSpinner);
		scanSpinner.setVisibility(View.VISIBLE);
		ScheduledFuture<Void> cancelScanFuture = scheduledExecutorService
				.schedule(new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								scanSpinner.setVisibility(View.GONE);
								scanButton.setPressed(false);
								scanButton.setEnabled(true);
							}
						});

						return null;
					}
				}, 5, TimeUnit.SECONDS);
	}
}
