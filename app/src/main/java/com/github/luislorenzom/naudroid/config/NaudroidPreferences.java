package com.github.luislorenzom.naudroid.config;

import java.util.List;

/**
 * Created by Luis on 26/03/2016.
 */
public class NaudroidPreferences {

    private String saveFilesPath;
    private String saveKeysPath;
    private List<String> serverPreferences;

    public NaudroidPreferences(String saveFilesPath, String saveKeysPath, List<String> serverPreferences) {
        this.saveFilesPath = saveFilesPath;
        this.saveKeysPath = saveKeysPath;
        this.serverPreferences = serverPreferences;
    }

    public String getSaveFilesPath() {
        return saveFilesPath;
    }

    public void setSaveFilesPath(String saveFilesPath) {
        this.saveFilesPath = saveFilesPath;
    }

    public String getSaveKeysPath() {
        return saveKeysPath;
    }

    public void setSaveKeysPath(String saveKeysPath) {
        this.saveKeysPath = saveKeysPath;
    }

    public List<String> getServerPreferences() {
        return serverPreferences;
    }

    public void setServerPreferences(List<String> serverPreferences) {
        this.serverPreferences = serverPreferences;
    }
}
