package com.github.luislorenzom.naudroid.connection;

import android.content.Context;

import com.github.luislorenzom.naudroid.client.FileUtilities;
import com.github.luislorenzom.naudroid.client.KeyContainer;
import com.github.luislorenzom.naudroid.config.dao.PreferencesDao;
import com.github.luislorenzom.naudroid.util.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Luis on 31/03/2016.
 */
public class ConnectionUtilities {

    private Context context;
    private PreferencesDao preferencesDao;
    private FileUtilities fileUtilities;
    private NautilusKeyHandler keysHandler;

    public ConnectionUtilities(Context context) {
        this.context = context;
        preferencesDao =  new PreferencesDao(context);
        fileUtilities = new FileUtilities(context);
        keysHandler =  new NautilusKeyHandler(context);
    }


    public List<NautilusMessage> prepareFileToSend(String filePath, int downloadLimit,
                                        Calendar dateLimit, Calendar releaseDate, PublicKey pKey) {

        String keyName = new File(filePath).getName();
        List<NautilusMessage> msgs = new ArrayList<NautilusMessage>();
        try {
            String pathForList = preferencesDao.getPreference().getSaveKeysPath();
            /* Split the file */
            fileUtilities.fileSplit(filePath);
            List<File> splitFiles = new ArrayList<File>();

            if (new File(filePath).getParent() != null) {
                pathForList = new File(filePath).getParent();
            }

            File[] files = new File(pathForList).listFiles();
            /* Get the file's chunks */
            for (File fileEntry : files) {
                if (fileEntry.getName().contains(getNameAboutPath(filePath))) {
                    if ((fileEntry.getName()).substring(fileEntry.getName().length() - 1).matches("[0-9]+")) {
                        if (!(fileEntry.getPath().equals(filePath))) {
                            splitFiles.add(fileEntry);
                        }
                    }
                }
            }

            /* Prepare the list that before we turned to a XML key */
            List<NautilusKey> keysList = new ArrayList<NautilusKey>();
            /* Now encrypt the files and generate a key */
            for (File fileEntry : splitFiles) {
                // Initialize the key container
                KeyContainer key = null;

                // Encrypt
                if (pKey == null) {
                    // if public key doesn't exist then encrpyt with AES
                    key = fileUtilities.encryptFile(fileEntry.getPath(), Constants.ENCRYPT_ALG.AES, null);
                } else {
                    // if the public key exists the encrypt with RSA
                    key = fileUtilities.encryptFile(fileEntry.getPath(), Constants.ENCRYPT_ALG.RSA, pKey);
                }

                String EncryptfileName = fileEntry.getName()+".aes256";

                // Delete the plain file
                fileEntry.delete();

                File encryptFile = new File(fileEntry.getPath()+".aes256");
                // Generate the file hash
                String hash = getHashFromFile(encryptFile, "SHA-256");

                // We generate a key and adding to list, after when send the file will save the host
                NautilusKey nKey = new NautilusKey(EncryptfileName, key.getKey(), key.getEncrypt_alg(), hash, null, null);
                keysList.add(nKey);

                // Generate a message
                NautilusMessage msg = new NautilusMessage(1, hash, readContentIntoByteArray(encryptFile),
                        downloadLimit, dateLimit, releaseDate);

                // Add the new message to the messageList
                msgs.add(msg);
            }

            // Write the key into xml
            // TODO asegurarse que existe la carpeta key en la sd
            keysHandler.generateKeys(keysList, preferencesDao.getPreference().getSaveKeysPath()+"/"+keyName+"_key.xml");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msgs;
    }

    public List<String> getHostAndBackupFromConfig() {
        List<String> serverPreferences = preferencesDao.getPreference().getServerPreferences();
        long seed = System.nanoTime();

        Collections.shuffle(serverPreferences, new Random(seed));
        return serverPreferences;
    }

    public void restoreFile(List<File> files, List<NautilusKey> keys) {
        try {
            String basePath = files.get(0).getParent();
            int index = 0;
            List<File> deleteFiles = new ArrayList<File>();
            // Decrypt
            for (File file : files) {
                if (keys.get(index).getEncryptAlg() == Constants.ENCRYPT_ALG.AES) {
                    fileUtilities.decrypt(keys.get(index).getKey(), file.getPath(), Constants.ENCRYPT_ALG.AES);
                } else {
                    fileUtilities.decrypt(keys.get(index).getKey(), file.getPath(), Constants.ENCRYPT_ALG.RSA);
                }

                index++;

                // delete encrypt file
                file.delete();

                int lenghtDeleteFiles = file.getName().length() - 7;
                deleteFiles.add(new File(basePath+"/"+file.getName().substring(0, lenghtDeleteFiles)));
            }

            // Get the baseName for make the join operation
            String[] baseNameArray = keys.get(0).getFileName().split("\\.");
            String baseName = "";
            index = 0;
            for (String baseNameFrag : baseNameArray) {
                if (index == (baseNameArray.length - 2)) {
                    break;
                }

                if (index == (baseNameArray.length - 3)) {
                    baseName += baseNameFrag;
                } else {
                    baseName += baseNameFrag + ".";
                }

                index++;
            }

            // Join
            fileUtilities.fileJoin(basePath+"/"+baseName);

            for (File fileToDelete : deleteFiles) {
                fileToDelete.delete();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*********************/
    /* PRIVATE FUNCTIONS */
    /*********************/

    private String getNameAboutPath(String path) {
        String[] tmp = path.split("/");
        return tmp[tmp.length - 1];
    }

    private byte[] readContentIntoByteArray(File file) {
        FileInputStream fileInputStream = null;
        byte[] bFile = new byte[(int) file.length()];
        try {
            //convert file into array of bytes
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bFile;
    }

    private String getHashFromFile(File file, String algorithm) {
        return null;
    }

}
