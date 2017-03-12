package com.mantas.visualrecognition.Enums;


public enum Actions {
    START_SCANNING("start"), STOP_SCANNING("stop"), EXIT("exit"), SETTINGS("settings"), BACK("back"), START_CAMERA("camera");

    private String action;

    Actions(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public static Actions getActionFromValue(String value) {
        for (Actions a: Actions.values()) {
            if (a.getAction().equals(value)) {
                return a;
            }
        }
        return null;
    }

}
