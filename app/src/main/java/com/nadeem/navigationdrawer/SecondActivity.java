package com.nadeem.navigationdrawer;

import android.animation.ObjectAnimator;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codetroopers.betterpickers.hmspicker.HmsPickerBuilder;
import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.codetroopers.betterpickers.timepicker.TimePickerBuilder;

import com.codetroopers.betterpickers.hmspicker.HmsPickerBuilder;
import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment;

public class SecondActivity extends BaseActivity implements HmsPickerDialogFragment.HmsPickerDialogHandlerV2 {
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;
	private ImageButton play, trash;
	private TextView texto;
	private ProgressBar barra;
	public static final long MILLIS_TO_MINUTES = 60000;
	public static final long MILLS_TO_HOURS = 3600000;
	private CountDownTimer myCountDownTimer;
	int flag = 0, flag1 = 0;
	public long res;
	private ToneGenerator tg;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); // load
		showpopup();


		texto = (TextView) findViewById(R.id.tv);
		trash = (ImageButton) findViewById(R.id.botontrash);
		barra = (ProgressBar) findViewById(R.id.circularProgressbar);
		play = (ImageButton) findViewById(R.id.imageButton);
		tg = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
		barra.setMax(30000);

		play.setOnClickListener(new View.OnClickListener()

		{
			@Override
			public void onClick(View v) {

				if (flag == 0) {
					play.setImageResource(R.drawable.resume);
					Log.d("DEBUG MARTIN ALBARRACIN", "begin timer");
					myCountDownTimer = new MyCountDownTimer((int) res, 1);
					myCountDownTimer.start();
					flag = 3;
				} else if (flag == 1) {
					Log.d("DEBUG MARTIN ALBARRACIN", "resume timer");
					play.setImageResource(R.drawable.resume);
					String acum = texto.getText().toString();
					int hou = Integer.parseInt(acum.substring(0, 2));
					int min = Integer.parseInt(acum.substring(3, 5));
					int sec = Integer.parseInt(acum.substring(6, 8));
					long resultado = ((min * MILLIS_TO_MINUTES) + (sec * 1000) + (hou * MILLS_TO_HOURS));

					myCountDownTimer = new MyCountDownTimer((int) resultado, 1);
					myCountDownTimer.start();
					flag = 3;
				} else {
					play.setImageResource(R.drawable.play);
					Log.d("DEBUG MARTIN ALBARRACIN", "stop timer");
					myCountDownTimer.cancel();
					flag = 1;
					flag1 = 1;
				}


			}
		});

		trash.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showpopup();

			}
		});




		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);// load icons from
		// strings.xml

		set(navMenuTitles, navMenuIcons);

	}

	public class MyCountDownTimer extends CountDownTimer {

		public MyCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onTick(long millisUntilFinished) {

			int seconds = (int) (millisUntilFinished / 1000) % 60;
			int minutes = (int) ((millisUntilFinished / (MILLIS_TO_MINUTES)) % 60);
			//int hours = (int) ((since / (MILLS_TO_HOURS)) % 24); //this resets to  0 after 24 hour!
			int hours = (int) ((millisUntilFinished / (MILLS_TO_HOURS))); //this does not reset to 0!

			texto.setText(String.format("%02d:%02d:%02d"
					, hours, minutes, seconds));
			barra.setProgress(((int) millisUntilFinished)-1000);
		}

		public void onCancel(long millisUntilFinished) {
			Log.d("DEBUG MARTIN ALBARRACIN", "time is canceled");
		}

		@Override
		public void onFinish() {

			play.setImageResource(R.drawable.play);
			texto.setText("End");
			barra.setProgress(0);
			flag = 0;
			tg.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200);
		}


	}

	@Override
	public void onDialogHmsSet(int reference, boolean isNegative, int hours, int minutes,
							   int seconds) {

		res = ((minutes * MILLIS_TO_MINUTES) + ((seconds)*1000) + (hours * MILLS_TO_HOURS));

		barra.setMax(((int) res)-1000);
		barra.setProgress(((int) res)-1000);
		Log.d("DEBUG MARTIN ALBARRACIN", String.valueOf(res));
		texto.setText(String.format("%02d:%02d:%02d"
				, hours, minutes, seconds));
		myCountDownTimer = new MyCountDownTimer((int) res, 1);


	}

	public void showpopup() {
		HmsPickerBuilder hpb = new HmsPickerBuilder()
				.setFragmentManager(getSupportFragmentManager())
				.setStyleResId(R.style.BetterPickersDialogFragment);
		hpb.show();


	}
}
