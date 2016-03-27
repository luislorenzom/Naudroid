package com.github.luislorenzom.naudroid.config.dao;

/**
 * Created by Luis on 26/03/2016.
 */
public class ServerTable {

    private String serverId;
    private String serverIp;

    public ServerTable(String serverId, String serverIp) {
        this.serverId = serverId;
        this.serverIp = serverIp;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
