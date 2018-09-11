package com.patient_doctor.heartbeat_analysis;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    RingView mringview;

    public void btnRetryClick(View view){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void btnGraphClick(View view){
        Intent intent=new Intent(this,graphActivity.class);
        startActivity(intent);
    }

    private static final String TAG = "HeartRate";
    private static final AtomicBoolean processing = new AtomicBoolean(false);

    private static SurfaceView preview = null;
    private static SurfaceHolder previewHolder = null;
    private static Camera camera = null;
    private static View image = null;
    private static TextView text = null;

    private static PowerManager.WakeLock wakeLock = null;

    public static enum TYPE {
        GREEN, RED
    };
    private static TYPE currentType = TYPE.GREEN;
    public static TYPE getCurrent() {
        return currentType;
    }

    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;

    private static final int AVERAGE_ARRAY_SIZE=4;
    private static final int[] AVERAGE_ARRAY=new int[AVERAGE_ARRAY_SIZE];

    private static int averageIndex=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preview=(SurfaceView)findViewById(R.id.preview);
        previewHolder=preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        image=findViewById(R.id.image);
        text=(TextView)findViewById(R.id.text);

        PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
        wakeLock=pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

     //   Button retryBtn=(Button)findViewById(R.id.action_retry);
     //   retryBtn.setOnClickListener(new View.OnClickListener(){
     //      @Override

     //      public void onClick(View view){
     //          text.setText(R.string.default_text);
     //          startCam();
     //          mringview.startAnim();
     //       }
     //    });

        Button saveBtn=(Button)findViewById(R.id.action_save);

        saveBtn.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View view){


               }
            });
    }

    @Override
    public void onResume(){
        super.onResume();


        if (isCameraCanUse()) {
            Log.i(TAG, "相机");
        } else {

            getAppDetailSettingIntent();
            return;
        }
        wakeLock.acquire();
        camera = Camera.open();
        Toast.makeText(getApplicationContext(),"START MEASURING...",Toast.LENGTH_LONG).show();
        startTime=System.currentTimeMillis();
    }

    @Override
    public void onPause(){
        super.onPause();

        wakeLock.release();
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera=null;
    }

    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration){
        super.onConfigurationChanged(configuration);
    }

    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open(0);
            mCamera.setDisplayOrientation(90);
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
        }
        return canUse;
    }
    private void getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getApplicationContext().getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getApplicationContext().getPackageName());
        }
        startActivity(localIntent);
    }

    private static Camera.PreviewCallback previewCallback=new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = camera.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();

            if (!processing.compareAndSet(false, true)) return;

            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            // Log.i(TAG, "imgAvg="+imgAvg);
            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int averageArray:AVERAGE_ARRAY) {
                if (averageArray > 0) {
                    averageArrayAvg += averageArray;
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    // Log.d(TAG, "BEAT!! beats="+beats);
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == AVERAGE_ARRAY_SIZE) averageIndex = 0;
            AVERAGE_ARRAY[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
                image.postInvalidate();
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;

            if (totalTimeInSecs >= 10) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                // Log.d(TAG,
                // "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;

                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                //heartrate
                text.setText(String.valueOf(beatsAvg));
                // Log.v(TAG, "beatsAvg="+beatsAvg);
                startTime = System.currentTimeMillis();
                beats = 0;
                stopCam();
            }
            processing.set(false);
        }
    };

    public static void stopCam(){
        camera.stopPreview();
    }

    public static void startCam(){
        camera.startPreview();
        startTime=System.currentTimeMillis();
    }

    private static SurfaceHolder.Callback surfaceCallback=new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(previewCallback);
            }catch (Throwable t){
                Log.e(TAG,"Exception in setPreviewDisplay()",t);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Camera.Parameters parameters=camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            Camera.Size size=getSmallestPreviewSize(width,height,parameters);
            if(size!=null){
                parameters.setPreviewSize(size.width,size.height);
                Log.d(TAG,"Using width="+size.width+"height="+size.height);
            }
            camera.setParameters(parameters);
            camera.startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };

    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters ){
        Camera.Size result = null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;

                    if (newArea < resultArea) result = size;
                }
            }
        }
        return result;
    }

    }