package com.ej.demop;

import android.Manifest;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ejplayer.myplaysdk.common.EILPlayerManager;
public class MainActivity extends AppCompatActivity implements EILPlayerManager.PlayerStateListener{

    private static final String TAG = "appplayer";
    private EILPlayerManager player;
    boolean mRecordingEnabled;
    boolean mIspaused;
    private Button mBtnrecord;
    private Button mBtnplay;
    private Button mBtnpause;
    private Button mBtnflip;
    private Button mBtnrot;
    private int mReconect;
    private boolean bFullScreen;
    private TextView mVersioninfo;
    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Intent intent=new Intent(this,PlayActivity.class);  //方法1

        // startActivity(intent);
        View layout_outer = this.findViewById(R.id.video_view);
        mVersioninfo=(TextView) findViewById(R.id.versionView);
        initPlayer(layout_outer);
       // layout_outer.setRotation(90);
        mRecordingEnabled=false;
        mIspaused=false;
        if (PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.INTERNET},
                    0);
        }
        if (PermissionChecker.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
        powerManager = (PowerManager)this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
     //   this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
        if(player.isPlaying()||mIspaused==true) {
            mBtnplay.setEnabled(false);
            player.stop();
            EditText et = (EditText) findViewById(R.id.editText);
            String s = et.getText().toString();
            player.play(s);
            mIspaused=false;
            updateControls();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        mBtnpause.setEnabled(false);
        mBtnflip.setEnabled(false);
        mBtnrot.setEnabled(false);
    }
    @Override
    protected void onPause(){
        super.onPause();
        wakeLock.release();
     //   player.pause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mBtnpause.setEnabled(false);
        mBtnflip.setEnabled(false);
        mBtnrot.setEnabled(false);
    //    player.stop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        return player.onTouch(null,event);
        //return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
          //  player.setFullScreenOnly(true);
          //  player.playInFullScreen(true);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
          //  player.setFullScreenOnly(false);
        //   player.playInFullScreen(false);

        }
    }
    private void initPlayer(View view) {
        player = new EILPlayerManager(this,view);
        Log.i(TAG,"initPlayer start\n");
     //   player.setFullScreenOnly(true);
        player.setScaleType(EILPlayerManager.SCALETYPE_FITXY);
     //   player.playInFullScreen(true);

        player.setPlayerStateListener(this);
        //player.play("http://zv.3gv.ifeng.com/live/zhongwen800k.m3u8");
       // player.play("http://videoplay.ejucloud.com/newcode-88b562--20161124101351.mp4");

        //player.play("/sdcard/2slice.mp4");
        mBtnplay = (Button) findViewById(R.id.button_play);
        mBtnpause = (Button) findViewById(R.id.button_pause);
        mBtnflip = (Button) findViewById(R.id.button_flip);
        mBtnrot = (Button) findViewById(R.id.button_rot);
        mBtnpause.setEnabled(false);
        mBtnflip.setEnabled(false);
        mBtnrot.setEnabled(false);

        bFullScreen =false;
        mVersioninfo.setText(String.format("Version: %s", player.GetVersion()));
        Log.i(TAG,"initPlayer\n");
    }
    private void updateControls() {
        Button toggleRelease = (Button) findViewById(R.id.button_pause);
        int id = mIspaused ?
                R.string.togglePlayOff : R.string.togglePlayOn;
        toggleRelease.setText(id);
        //CheckBox cb = (CheckBox) findViewById(R.id.rebindHack_checkbox);
        //cb.setChecked(TextureRender.sWorkAroundContextProblem);
    }
    public void clickTogglePlay(@SuppressWarnings("unused") View unused) {
      //  Button toggleRelease = (Button) findViewById(R.id.button_record);

        mBtnpause.setEnabled(false);
        mBtnplay.setEnabled(false);
        mBtnflip.setEnabled(false);
        mBtnrot.setEnabled(false);
        player.live(true);
        if(mRecordingEnabled) {
            player.stop();
            mRecordingEnabled=!mRecordingEnabled;
//            toggleRelease.setClickable(false);
//            toggleRelease.setEnabled(false);
        }
        if(!mRecordingEnabled)
        {
        //    player.setFullScreenOnly(true);
        //    player.setScaleType(EILPlayerManager.SCALETYPE_FITXY);
        //    player.playInFullScreen(true);

            mReconect=1;
            player.setPlayerStateListener(this);

            EditText et = (EditText)findViewById(R.id.editText);
            String s = et.getText().toString();
            player.play(s);
            mRecordingEnabled=!mRecordingEnabled;
//            toggleRelease.setClickable(true);
//            toggleRelease.setEnabled(true);
        }


          updateControls();
    }
    public void clickTogglePause(@SuppressWarnings("unused") View unused) {

        if(mIspaused)
            player.onResume();
        else
            player.pause();
        mIspaused=!mIspaused;
        updateControls();
    }
    public void clickToggleRecord(@SuppressWarnings("unused") View unused) {

//        player.pause();
//        player.record();
//        player.onResume();
    }
    public void clickToggleFlip(@SuppressWarnings("unused") View unused) {

       player.toggleflip();
    }
    public void clickToggleRotate(@SuppressWarnings("unused") View unused) {

       player.togglerotation();
//        bFullScreen=bFullScreen? false:true;
//        player.setFullScreenOnly(bFullScreen);
//        player.playInFullScreen(bFullScreen);
    }
    private void showMessage(String message)
    {
        Toast toast = Toast.makeText(MainActivity.this,
                message,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onComplete() {
        Log.i(TAG,"complete\n");
        new AlertDialog.Builder(this)
                .setTitle("complete")
                .setMessage("complete")
                .setPositiveButton("Cancel", null)
                .show();
        mBtnplay.setEnabled(true);
        mBtnpause.setEnabled(false);
        mBtnflip.setEnabled(false);
        mBtnrot.setEnabled(false);

    }

    @Override
    public void onError() {
        Log.i(TAG,"error,mReconect ="+mReconect);

        mReconect++;
        if(mReconect<2) {
            showMessage("reconnect");
            mBtnplay.setEnabled(false);
            mBtnpause.setEnabled(false);
            mBtnflip.setEnabled(false);
            mBtnrot.setEnabled(false);
            EditText et = (EditText) findViewById(R.id.editText);
            String s = et.getText().toString();
            player.play(s);
        }
        else
        {
            new AlertDialog.Builder(this)
                    .setTitle("ERROR")
                    .setMessage("Could't Connect To URL, Please try again later")
                    .setPositiveButton("Cancel", null)
                    .show();
            mBtnplay.setEnabled(true);
            mBtnpause.setEnabled(false);
            mBtnflip.setEnabled(false);
            mBtnrot.setEnabled(false);

        }
    }

    @Override
    public void onLoading() {
        Log.i(TAG,"onLoading\n");

    }

    @Override
    public void onPlay() {

        //  player.pause();
        mBtnpause.setEnabled(true);
        mBtnplay.setEnabled(true);
        mBtnflip.setEnabled(true);
        mBtnrot.setEnabled(true);
        mReconect=0;
        Log.i(TAG,"onPlay\n");
    }
}

