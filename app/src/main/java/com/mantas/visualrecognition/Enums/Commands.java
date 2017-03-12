package com.mantas.visualrecognition.Enums;

public enum Commands {
    CAMERA("camera"), EXIT("exit"), SETTINGS("settings"), BACK("back"), START("start"), STOP("stop");

    private String command;

    Commands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
