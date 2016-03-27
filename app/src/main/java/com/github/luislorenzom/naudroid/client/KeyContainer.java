package com.github.luislorenzom.naudroid.client;

import com.github.luislorenzom.naudroid.util.Constants;

/**
 * Created by Luis on 27/03/2016.
 */
public class KeyContainer {

    private String key;
    private Constants.ENCRYPT_ALG encrypt_alg;

    public KeyContainer(String key, Constants.ENCRYPT_ALG encrypt_alg) {
        this.key = key;
        this.encrypt_alg = encrypt_alg;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Constants.ENCRYPT_ALG getEncrypt_alg() {
        return encrypt_alg;
    }

    public void setEncrypt_alg(Constants.ENCRYPT_ALG encrypt_alg) {
        this.encrypt_alg = encrypt_alg;
    }
}
