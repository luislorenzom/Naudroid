package com.github.luislorenzom.naudroid.connection;

import com.github.luislorenzom.naudroid.util.Constants;

/**
 * Created by Luis on 30/03/2016.
 */
public class NautilusKey {

    private String fileName;
    private String key;
    private Constants.ENCRYPT_ALG encryptAlg;
    private String hash;
    private String host;
    private String hostBackup;

    public NautilusKey(String fileName, String key, Constants.ENCRYPT_ALG encryptAlg,
                              String hash, String host, String hostBackup) {
        this.fileName = fileName;
        this.key = key;
        this.encryptAlg = encryptAlg;
        this.hash = hash;
        this.host = host;
        this.hostBackup = hostBackup;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Constants.ENCRYPT_ALG getEncryptAlg() {
        return encryptAlg;
    }

    public void setEncryptAlg(Constants.ENCRYPT_ALG encryptAlg) {
        this.encryptAlg = encryptAlg;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHostBackup() {
        return hostBackup;
    }

    public void setHostBackup(String hostBackup) {
        this.hostBackup = hostBackup;
    }
}
