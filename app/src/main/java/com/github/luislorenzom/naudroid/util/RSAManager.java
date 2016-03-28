package com.github.luislorenzom.naudroid.util;

import android.util.Base64;
import android.util.Log;

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

    public KeyPair generateKeys () {
        KeyPair pair = null;
        try {
            // Generate Keys
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(4096);
            KeyPair key = keyGen.generateKeyPair();

            // Save the public key
            //String stringPublicKey = Base64Utils.encodeToString(key.getPublic().getEncoded());
            String stringPublicKey = Base64.encodeToString(key.getPublic().getEncoded(), Base64.DEFAULT);
            PrintWriter out = new PrintWriter("public.key");
            out.println(stringPublicKey);
            out.close();

            // Save the private key
            //String stringPrivateKey = Base64Utils.encodeToString(key.getPrivate().getEncoded());
            String stringPrivateKey = Base64.encodeToString(key.getPrivate().getEncoded(), Base64.DEFAULT);
            out = new PrintWriter("private.key");
            out.println(stringPrivateKey);
            out.close();
            pair = key;

        } catch (Exception e) {
            Log.e("errorTag", "Has been happened one error in the key pair generation");
        }
        return pair;
    }


    public PublicKey getPublicKey () {
        try {
            return getPublicKeyFromFile("public.key");
        } catch (Exception e) {
            Log.e("errorTag", "Can't recovery the private key");
            return null;
        }
    }


    public PrivateKey getPrivateKey () {
        PrivateKey key = null;
        try {
            // Open the file and read the bytes
            File privateKeyFile = new File("private.key");
            FileInputStream fis = new FileInputStream(privateKeyFile);
            DataInputStream dis = new DataInputStream(fis);
            byte[] keyBytes = new byte[(int) privateKeyFile.length()];
            dis.readFully(keyBytes);
            dis.close();

            // Decode the bytes
            String tmp = new String(keyBytes);
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
        return ((new File("private.key").exists()) && (new File("public.key").exists()));
    }


    public PublicKey getPublicKeyFromFile(String keyPath) throws Exception {
        PublicKey key = null;
        // Open the file and read the bytes
        File publicKeyFile = new File(keyPath);
        FileInputStream fis = new FileInputStream(publicKeyFile);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) publicKeyFile.length()];
        dis.readFully(keyBytes);
        dis.close();

        // Decode the bytes
        String tmp = new String(keyBytes);
        //byte[] tmpDecoded = Base64Utils.decodeFromString(tmp);
        byte[] tmpDecoded = Base64.decode(tmp, Base64.DEFAULT);

        // Transform the bytes into PublicKey
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(tmpDecoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        key = kf.generatePublic(keySpec);

        return key;
    }
}
