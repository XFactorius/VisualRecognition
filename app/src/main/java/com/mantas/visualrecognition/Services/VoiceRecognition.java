package com.mantas.visualrecognition.Services;

import android.app.Activity;

import com.mantas.visualrecognition.Enums.Actions;
import com.mantas.visualrecognition.Enums.Commands;

import java.util.List;

public class VoiceRecognition extends Activity {

    public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public static final int VOICE_RECOGNITION_REQUEST_CODE_FROM_CAMERA = 12345;

    public static boolean containsVoiceCommand(List<String> matches, Commands command) {
        return matches.contains(command.getCommand());
    }

    public static Actions getAction(List<String> matches) {
        for (String match : matches) {
            Actions action = Actions.getActionFromValue(match);
            if (action != null) {
                return action;
            }
        }
        return null;
    }
}
