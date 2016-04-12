package com.github.luislorenzom.naudroid.util;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.github.luislorenzom.naudroid.config.dao.KeyPairTable;
import com.github.luislorenzom.naudroid.config.dao.KeyPairsDao;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * Created by Luis on 27/03/2016.
 */
public class RSAManager {

    private Context context;
    KeyPairsDao keyPairsDao;

    public RSAManager(Context context) {
        this.context = context;
        keyPairsDao = new KeyPairsDao(context);
    }

    public KeyPair generateKeys () {
        KeyPair pair = null;
        try {
            // Generate Keys
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(4096);
            KeyPair key = keyGen.generateKeyPair();

            // Save the pair
            String stringPublicKey = Base64.encodeToString(key.getPublic().getEncoded(), Base64.DEFAULT);
            String stringPrivateKey = Base64.encodeToString(key.getPrivate().getEncoded(), Base64.DEFAULT);
            keyPairsDao.initKeyPair(stringPublicKey, stringPrivateKey);

            pair = key;

        } catch (Exception e) {
            Log.e("errorTag", "Has been happened one error in the key pair generation");
        }
        return pair;
    }


    public PublicKey getPublicKey () {
        PublicKey key = null;
        try {
            // Get the public key string
            String tmp = keyPairsDao.getKeyPair().getPublicKey();

            byte[] tmpDecoded = Base64.decode(tmp, Base64.DEFAULT);

            // Transform the bytes into PublicKey
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(tmpDecoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            key = kf.generatePublic(keySpec);

        } catch (Exception e) {
            Log.e("errorTag", "Can't recovery the public key");
        }
        return key;
    }


    public PrivateKey getPrivateKey () {
        PrivateKey key = null;
        try {
            // Get the private key string
            String tmp = keyPairsDao.getKeyPair().getPrivateKey();
            //byte[] tmpDecoded = Base64Utils.decodeFromString(tmp);
            byte[] tmpDecoded = Base64.decode(tmp, Base64.DEFAULT);

            // Transform the bytes into PrivateKey
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(tmpDecoded);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            key = kf.generatePrivate(keySpec);


        } catch (Exception e) {
            Log.e("errorTag", "Can't recovery the private key");
        }
        return key;
    }


    public String encrypt (String plainString, PublicKey key) {
        String encryptMessage = null;
        try {
            // Initialized the cipher in encrypt mode
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            // Encrypt the message
            byte[] data = cipher.doFinal(plainString.getBytes());
            //encryptMessage = Base64Utils.encodeToString(data);
            encryptMessage = Base64.encodeToString(data, Base64.DEFAULT);

        } catch (Exception e) {
            Log.e("errorTag", "Can't encrypt the information");
        }
        return encryptMessage;
    }


    public String decrypt (String encryptString, PrivateKey key) throws Exception {
        String plainMessage = null;
        // Initialized the cipher in encrypt mode
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Decrypt the message
        //byte[] data = cipher.doFinal(Base64Utils.decode(encryptString.getBytes()));
        byte[]data = cipher.doFinal(Base64.decode(encryptString.getBytes(), Base64.DEFAULT));
        //plainMessage = new String (data);
        plainMessage = new String (data, "UTF-8");
        return plainMessage;
    }


    public boolean existsPair() {
        return (keyPairsDao.getKeyPair() != null);
    }
}
