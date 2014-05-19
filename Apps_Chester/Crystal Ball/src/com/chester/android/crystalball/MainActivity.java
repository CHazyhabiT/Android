package com.chester.android.crystalball;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.chester.android.crystalball.R;
import com.chester.android.crystalball.ShakeDetector.OnShakeListener;

public class MainActivity extends Activity {

	String[] answers = getResources().getStringArray(R.array.crystalball_answers);
	private CrystalBall mCrystalBall = new CrystalBall(answers);
	// UI components
	private TextView mAnswerLabel;
	private ImageView mCrystalBallImage;
	// sensor for shake
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private ShakeDetector mShakeDetector;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Declare our View variables and assign them the Views from the layout file
        mAnswerLabel = (TextView) findViewById(R.id.textView1);
        mCrystalBallImage = (ImageView) findViewById(R.id.imageView1);
        
        // get SensorManager to handle the sensors
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // get the accelerometer sensor
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // initialize a listener
        mShakeDetector = new ShakeDetector(new OnShakeListener() {
        	@Override
			public void onShake() {
				getNewAnswer();
			}
		});
        
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	mSensorManager.registerListener(mShakeDetector, mAccelerometer, 
    			SensorManager.SENSOR_DELAY_UI);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	mSensorManager.unregisterListener(mShakeDetector);
    }
    
    private void animateCrystalBall() {
    	
    	mCrystalBallImage.setImageResource(R.drawable.ball_animation);
    	AnimationDrawable ballAnimation = (AnimationDrawable) mCrystalBallImage.getDrawable();
    	// once the animation been started, system will regard it in running state
    	// so before start it, we need to stop it first
    	if(ballAnimation.isRunning()) {
    		ballAnimation.stop();
    	}
    	ballAnimation.start();
    	
//    	Animation translateAnim = new TranslateAnimation(0,320, 0, 0);
//    	translateAnim.setDuration(2000);
//    	translateAnim.setRepeatCount(Animation.INFINITE);
//    	translateAnim.setRepeatMode(Animation.REVERSE);
//    	mCrystalBallImage.startAnimation(translateAnim);
    	
//    	Animation scaleAnim = new ScaleAnimation(0, 30, 0 ,50);
//    	scaleAnim.setDuration(2000);
//    	mCrystalBallImage.startAnimation(scaleAnim);
    	
    	
    }
 
    // let the text has a fade-in effects
    private void animateAnswer() {
    	AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1);
    	fadeInAnimation.setDuration(1500);
    	fadeInAnimation.setFillAfter(true);
    	mAnswerLabel.setAnimation(fadeInAnimation);
    }
    
    // sound effects
    private void playSound() {
    	MediaPlayer player = MediaPlayer.create(this,  R.raw.crystal_ball);
    	player.start();
    	player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	private void getNewAnswer() {
		String answer = mCrystalBall.getAnAnswer();
		
		// Update the label with our dynamic answer
		mAnswerLabel.setText(answer);
		animateCrystalBall();
		animateAnswer();
		playSound();
	}
}
