package com.github.luislorenzom.naudroid.config.dao;

/**
 * Created by Luis on 26/03/2016.
 */
public class PreferenceTable {

    private String id;
    private String saveFilesPath;
    private String saveKeysPath;

    public PreferenceTable(String id, String saveFilesPath, String saveKeysPath) {
        this.id = id;
        this.saveFilesPath = saveFilesPath;
        this.saveKeysPath = saveKeysPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
