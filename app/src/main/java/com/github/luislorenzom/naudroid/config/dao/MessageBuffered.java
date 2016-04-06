package com.github.luislorenzom.naudroid.config.dao;

/**
 * Created by Luis on 06/04/2016.
 */
public class MessageBuffered {

    private String id;
    private String serverIp;
    private String hash;

    public MessageBuffered(String id, String serverIp, String hash) {
        this.id = id;
        this.serverIp = serverIp;
        this.hash = hash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
