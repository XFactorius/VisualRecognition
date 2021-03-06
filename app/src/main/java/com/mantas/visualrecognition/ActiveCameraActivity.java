package com.mantas.visualrecognition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.mantas.visualrecognition.Dialogs.SettingsDialog;
import com.mantas.visualrecognition.Enums.Actions;
import com.mantas.visualrecognition.Enums.ScanningMethod;
import com.mantas.visualrecognition.Services.VoiceRecognition;
import com.mantas.visualrecognition.Settings.MainSettings;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ActiveCameraActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OCVSample::Activity";
    Mat mRgba;
    Mat mRgbaF;
    Mat mRgbaT;
    Mat mIntermediateMat;
    private CameraBridgeViewBase mOpenCvCameraView;
    boolean resourcesLoaded = false;

    private CascadeClassifier cascadeClassifier = null;
    private CascadeClassifier eyesClassifier = null;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_active_camera);

        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.show_camera_activity_java_surface_view);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mIntermediateMat = new Mat(height, width, CvType.CV_8UC4);
        mRgbaF = new Mat(height, width, CvType.CV_8UC4);
        mRgbaT = new Mat(width, width, CvType.CV_8UC4);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

            mRgba = inputFrame.gray();
            mRgbaF = inputFrame.rgba();

        if (MainSettings.scanning) {

            if (MainSettings.method == ScanningMethod.HAAR) {

                if (!resourcesLoaded) {

                    InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
                    InputStream is2 = getResources().openRawResource(R.raw.frontal_eyes);

                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);

                    File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_default.xml");
                    File eyesCascadeFile = new File(cascadeDir, "frontal_eyes.xml");

                    loadResources(is, mCascadeFile);
                    loadResources(is2, eyesCascadeFile);

                    cascadeClassifier = new CascadeClassifier(mCascadeFile.getAbsolutePath());

                    cascadeClassifier.load(mCascadeFile.getAbsolutePath());

                    eyesClassifier = new CascadeClassifier(eyesCascadeFile.getAbsolutePath());

                    eyesClassifier.load(eyesCascadeFile.getAbsolutePath());

                    resourcesLoaded = true;
                }

                if (!cascadeClassifier.empty() && !eyesClassifier.empty()) {

                    MatOfRect objects = new MatOfRect();

                    MatOfRect eyesObjects = new MatOfRect();

                    cascadeClassifier.detectMultiScale(mRgba, objects, 1.04, 4, 0, new Size(400, 400), new Size(1000, 1000));

                    Rect[] dataArray = objects.toArray();

                    for (Rect rect : dataArray) {
                        Imgproc.rectangle(mRgbaF, rect.tl(), rect.br(), new Scalar(0, 255, 0, 255), 3);

                        eyesClassifier.detectMultiScale(mRgba, eyesObjects, 1.04, 4, 0, new Size(100, 100), new Size(1000, 1000));
                        Rect[] eyesDataArray = eyesObjects.toArray();

                        for (Rect eye : eyesDataArray) {
                            Imgproc.rectangle(mRgbaF, eye.tl(), eye.br(), new Scalar(255, 0, 0, 255), 3);
                            vibrate();
                        }

                    }
                }
            } else {

            }
        }

        return mRgbaF;
    }

    private void loadResources(InputStream is, File mCascadeFile) {
        try {
            FileOutputStream os = new FileOutputStream(mCascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

       /* final int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            enableVoiceRecognition();
        } */

        SettingsDialog dialog = new SettingsDialog();
        dialog.show(getFragmentManager(), "Test");

        return super.onTouchEvent(event);
    }

    private void enableVoiceRecognition() {
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a command");
        startActivityForResult(voiceIntent, VoiceRecognition.VOICE_RECOGNITION_REQUEST_CODE_FROM_CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VoiceRecognition.VOICE_RECOGNITION_REQUEST_CODE_FROM_CAMERA && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            doActionBasedOnMatch(matches);
        }
    }

    void doActionBasedOnMatch(ArrayList<String> matches) {
        Actions actions = VoiceRecognition.getAction(matches);
        switch (actions != null ? actions : null) {
            case START_SCANNING:
                turnScanningOn();
                break;
            case EXIT:
                exitToMain();
                break;
            case STOP_SCANNING:
                turnScanningOff();
                break;
            case BACK:
                back();
                break;
            default:
        }
    }

    void turnScanningOff() {
        MainSettings.scanning = false;
    }

    void turnScanningOn() {
        MainSettings.scanning = true;
    }

    private void back() {
        finish();
    }

    private void exitToMain() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
}
