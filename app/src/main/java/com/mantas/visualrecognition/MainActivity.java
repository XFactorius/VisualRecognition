package com.mantas.visualrecognition;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.mantas.visualrecognition.Enums.Actions;
import com.mantas.visualrecognition.Enums.Commands;
import com.mantas.visualrecognition.Services.VoiceRecognition;
import com.mantas.visualrecognition.Util.ActionUtils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends FragmentActivity {

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            doActionBasedOnMatch(matches);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            turnActiveCameraActivity();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        if (ActionUtils.containsTouch(action)) {
            enableVoiceRecognition();
        }
        return super.onTouchEvent(event);
    }

     void doActionBasedOnMatch(List<String> matches) {
        Actions actions = VoiceRecognition.getAction(matches);
        switch (actions != null ? actions : null) {
            case START_CAMERA:
                turnActiveCameraActivity();
                break;
            case EXIT:
                exitToMain();
                break;
            case SETTINGS:
                turnSettingsActivity();
                break;
            default:
        }
    }

    private void turnSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void exitToMain() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    private void turnActiveCameraActivity() {
        Intent intent = new Intent(this, ActiveCameraActivity.class);
        startActivity(intent);
    }

    private void enableVoiceRecognition() {
        Intent voiceIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        voiceIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say a command");
        startActivityForResult(voiceIntent, VoiceRecognition.VOICE_RECOGNITION_REQUEST_CODE);
    }


}
