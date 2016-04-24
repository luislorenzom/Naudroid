package com.github.luislorenzom.naudroid.client;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.github.luislorenzom.naudroid.util.Constants;
import com.github.luislorenzom.naudroid.util.RSAManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Luis on 27/03/2016.
 */
public class FileUtilities {

    private RSAManager manager;

    public FileUtilities(Context context) {
        manager = new RSAManager(context);
    }

    public void fileSplit (String filePath) throws IOException {
        // Open the file
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));

        // Get the file length
        File file = new File(filePath);
        long fileSize = file.length();

        long chunkSize = generateChunkSize(fileSize);

        // Loop for each full chunk
        int subfile;
        for (subfile = 0; subfile < fileSize/chunkSize; subfile++) {
            // Open the output file
            BufferedOutputStream out = new BufferedOutputStream(new
                    FileOutputStream(filePath + "." + subfile));

            // Write the right amount of bytes
            for (int currentByte = 0; currentByte < chunkSize; currentByte++) {
                // Load one byte from the input file and write it to the output file
                out.write(in.read());
            }
            // Close the file
            out.close();
        }

        // Loop for the last chunk (which may be smaller than the chunk size)
        if (fileSize != chunkSize * (subfile - 1)) {
            // Open the output file
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(filePath + "." + subfile));

            // Write the rest of the file
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            // Close the file
            out.close();
        }
        // Close the file
        in.close();
    }


    public void fileJoin (String filePath) throws IOException {
        int parts = getNumberParts(filePath);

        // Now, assume that the files are correctly numbered in order
        BufferedOutputStream out = new BufferedOutputStream(new
                FileOutputStream(filePath));
        for (int part = 0; part < parts; part++) {
            BufferedInputStream in = new BufferedInputStream(new
                    FileInputStream(filePath + "." + part));
            int b;
            while ( (b = in.read()) != -1 ) {
                out.write(b);
            }
            in.close();
        }
        out.close();
    }


    public KeyContainer encryptFile (String filePath, Constants.ENCRYPT_ALG algorithm,
                                     PublicKey publicKey) throws Exception {
        switch (algorithm) {
            case RSA:
                // Encrypt with RSA-4096
                String RSAkey = encryptWithRSA(filePath, publicKey);
                return new KeyContainer(RSAkey, Constants.ENCRYPT_ALG.RSA);
            case AES:
                // Encrypt with AES-256
                String AESkey = secretKeyToString(encryptWithAES(filePath));
                return new KeyContainer(AESkey, Constants.ENCRYPT_ALG.AES);
        }
        return null;
    }


    public void decrypt (String key, String filePath, Constants.ENCRYPT_ALG algorithm)
            throws Exception{

        switch (algorithm) {
            case RSA:
                // Decrypt with RSA
                decryptWithRSA(filePath, key);
                break;

            case AES:
                // Decrypt with AES
                decryptWithAES(filePath, key);
                break;
        }
    }


    /*********************/
    /* PRIVATE FUNCTIONS */
    /*********************/

    private long generateChunkSize(long fileSize) {

		/* less than 50mb implies split in three parts */
        if (fileSize <= 52428800) {
            return (fileSize / 2) - 100;
        }

		/* less than 250mb implies split in five parts */
        if (fileSize <= 262144000) {
            return (fileSize / 4) - 100;
        }

		/* less than 1gb implies split in nine parts */
        if (fileSize <= 1073741824) {
            return (fileSize / 8) - 100;
        }

		/* Default */
        return (fileSize / 16) - 100;
    }


    private int getNumberParts(String baseName) {
        // List all files in the same directory
        File directory = new File(baseName).getAbsoluteFile().getParentFile();
        final String justFileName = new File(baseName).getName();
        String[] matchingFiles = directory.list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.startsWith(justFileName) &&
                        name.substring(justFileName.length()).matches("^\\.\\d+$");
            }

        });
        return matchingFiles.length;
    }


    private SecretKey encryptWithAES(String fileName) throws Exception {
        // Generate Key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        SecretKey key = keyGen.generateKey();

        // Generate cipher for AES
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, key);

        try(FileOutputStream fos = new FileOutputStream(fileName+".aes256")) {
            //creating file input stream to read contents for encryption
            try (FileInputStream fis = new FileInputStream(fileName)) {
                //creating cipher output stream to write encrypted contents
                try (CipherOutputStream cos = new CipherOutputStream(fos, aesCipher)) {
                    int read;
                    byte buf[] = new byte[4096];
                    //reading from file
                    while((read = fis.read(buf)) != -1)
                        //encrypting and writing to file
                        cos.write(buf, 0, read);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return key;
    }


    private String encryptWithRSA(String filePath, PublicKey key) {
        String encryptedKey = null;
        try {
            SecretKey secretKey = encryptWithAES(filePath);
            String AESkey = secretKeyToString(secretKey);
            encryptedKey = manager.encrypt(AESkey, key);
        } catch (Exception e) {
            Log.e("errorTag", "Can't encrypt the file");
        }
        return encryptedKey;
    }


    private void decryptWithAES(String filePath, String stringKey) throws Exception {
        SecretKey key = stringToSecretKey(stringKey);

        //creating file input stream to read from file
        try(FileInputStream fis = new FileInputStream(filePath)) {
            //getting cipher for AES
            Cipher aesCipher = Cipher.getInstance("AES");
            //initializing cipher for decryption
            aesCipher.init(Cipher.DECRYPT_MODE, key);

            //with key creating file output stream to write back original contents
            String absolutePath = new File(filePath).getParent();
            String decodeName = new File(filePath).getName();
            String fileName = absolutePath + "/" + decodeName;
            try(FileOutputStream fos = new FileOutputStream(fileName.substring(0
                    , fileName.length()-7))) {
                //creating cipher input stream to read encrypted contents
                try(CipherInputStream cis = new CipherInputStream(fis, aesCipher)) {
                    int read;
                    byte buf[] = new byte[4096];
                    //reading from file
                    while((read = cis.read(buf)) != -1)
                        //decrypting and writing to file
                        fos.write(buf, 0, read);
                }
            }
        }
    }


    private void decryptWithRSA(String filepath, String encryptedStringKey) throws Exception {
        String plainAESKey = manager.decrypt(encryptedStringKey, manager.getPrivateKey());
        decryptWithAES(filepath, plainAESKey);
    }


    private String secretKeyToString (SecretKey secretKey) {
        return Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
    }


    private SecretKey stringToSecretKey (String stringKey) {
        return new SecretKeySpec(Base64.decode(stringKey, Base64.DEFAULT), "AES");
    }
}
