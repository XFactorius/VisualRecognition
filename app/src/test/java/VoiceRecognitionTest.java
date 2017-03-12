import com.mantas.visualrecognition.Enums.Actions;
import com.mantas.visualrecognition.Enums.Commands;
import com.mantas.visualrecognition.Services.VoiceRecognition;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class VoiceRecognitionTest {
    @Test
    public void testContainsVoiceCommandCamera() throws Exception {
        List<String> list = Arrays.asList("camera");
        boolean result = VoiceRecognition.containsVoiceCommand(list, Commands.CAMERA);

        assertTrue(result);
    }
    @Test
    public void testContainsVoiceCommandExit() throws Exception {
        List<String> list = Arrays.asList("exit");
        boolean result = VoiceRecognition.containsVoiceCommand(list, Commands.EXIT);

        assertTrue(result);
    }

    @Test
    public void testContainsVoiceCommandSettings() throws Exception {
        List<String> list = Arrays.asList("settings");
        boolean result = VoiceRecognition.containsVoiceCommand(list, Commands.SETTINGS);

        assertTrue(result);
    }

    @Test
    public void testContainsVoiceCommandCameraAndExpectExit() throws Exception {
        List<String> list = Arrays.asList("camera");
        boolean result = VoiceRecognition.containsVoiceCommand(list, Commands.EXIT);
        assertFalse(result);
    }

    @Test
    public void testGetActionWithCamera() {
        List<String> matches = Arrays.asList("camera", "kamera", "whatever");
        Actions actions  = VoiceRecognition.getAction(matches);

        assertEquals(Actions.START_CAMERA, actions);
    }

    @Test
    public void testGetActionWithNohing() {
        List<String> matches = Arrays.asList("nothing", "atAll", "lol");
        Actions actions  = VoiceRecognition.getAction(matches);

        assertEquals(null, actions);

    }

}
