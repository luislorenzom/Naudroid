package com.github.luislorenzom.naudroid.connection;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Luis on 30/03/2016.
 */
public class NautilusMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    private int type;
    private String hash;
    private byte[] content;
    private int downloadLimit;
    private Calendar dateLimit;
    private Calendar releaseDate;
    private boolean synchronize;


    public NautilusMessage(int type, String hash) {
        this.type = type;
        this.hash = hash;
    }


    public NautilusMessage(int type, String hash, byte[] content, int downloadLimit,
                           Calendar dateLimit, Calendar releaseDate) {
        this.type = type;
        this.hash = hash;
        this.content = content;
        this.downloadLimit = downloadLimit;
        this.dateLimit = dateLimit;
        this.releaseDate = releaseDate;
    }


    public NautilusMessage(byte[] content, boolean synchronize) {
        this.content = content;
        this.synchronize = synchronize;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getDownloadLimit() {
        return downloadLimit;
    }

    public void setDownloadLimit(int downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    public Calendar getDateLimit() {
        return dateLimit;
    }

    public void setDateLimit(Calendar dateLimit) {
        this.dateLimit = dateLimit;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isSynchronize() {
        return synchronize;
    }

    public void setSynchronize(boolean synchronize) {
        this.synchronize = synchronize;
    }
}
