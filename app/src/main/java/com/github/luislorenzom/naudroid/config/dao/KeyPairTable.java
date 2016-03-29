package com.github.luislorenzom.naudroid.config.dao;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Created by Luis on 29/03/2016.
 */
public class KeyPairTable {

    private String privateKey;
    private String publicKey;

    public KeyPairTable(String privateKey, String publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
