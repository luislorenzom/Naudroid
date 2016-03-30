package com.github.luislorenzom.naudroid.connection;


import android.content.Context;
import android.util.Xml;

import com.github.luislorenzom.naudroid.util.Constants;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis on 30/03/2016.
 */
public class NautilusKeyHandler {

    private Context context;

    public NautilusKeyHandler(Context context) {
        this.context = context;
    }

    public List<NautilusKey> getKey(String keyPath) {
        List<NautilusKey> keysList = new ArrayList<NautilusKey>();
        try {
            FileInputStream fis = context.openFileInput(keyPath);
            InputStreamReader isr = new InputStreamReader(fis);
            char[] inputBuffer = new char[fis.available()];
            isr.read(inputBuffer);
            String data = new String(inputBuffer);
            isr.close();
            fis.close();

            XmlPullParserFactory factory = null;
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = null;
            xpp = factory.newPullParser();
            xpp.setInput(new StringReader(data));
            int eventType = 0;
            eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT){
                String tag = null;

                String fileName = null;
                String key = null;
                Constants.ENCRYPT_ALG encryptAlg = null;
                String hash = null;
                String host = null;
                String hostBackup = null;

                if (eventType == XmlPullParser.START_TAG) {
                    tag = xpp.getName();
                }

                if (eventType == XmlPullParser.TEXT) {
                    if (tag == "fileName") {
                        fileName = xpp.getText();
                    }

                    if (tag == "AESKey") {
                        key = xpp.getText();
                        encryptAlg = Constants.ENCRYPT_ALG.AES;
                    }

                    if (tag == "RSAKey") {
                        key = xpp.getText();
                        encryptAlg = Constants.ENCRYPT_ALG.RSA;
                    }

                    if (tag == "hash") {
                        hash = xpp.getText();
                    }

                    if (tag == "host") {
                        host = xpp.getText();
                    }

                    if (tag == "hostBackup") {
                        hostBackup = xpp.getText();
                    }

                    NautilusKey nKey = new NautilusKey(fileName, key, encryptAlg, hash, host, hostBackup);
                    keysList.add(nKey);
                }

                eventType = xpp.next();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return keysList;
    }

    public void generateKeys(List<NautilusKey> keysList, String keyPath) {
        try {
            //FileOutputStream fos = new  FileOutputStream(keyPath);
            FileOutputStream fileos= context.openFileOutput(keyPath, Context.MODE_PRIVATE);
            XmlSerializer xmlSerializer = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8", true);

            xmlSerializer.startTag(null, "nautilusKey");
            xmlSerializer.startTag(null, "keys");

            for (NautilusKey key : keysList) {

                xmlSerializer.startTag(null, "key");

                if (key.getFileName() != null) {
                    xmlSerializer.startTag(null, "fileName");
                    xmlSerializer.text(key.getFileName());
                    xmlSerializer.endTag(null, "fileName");
                }

                if ((key.getKey() != null) && (key.getEncryptAlg() == Constants.ENCRYPT_ALG.AES)) {
                    xmlSerializer.startTag(null, "AESKey");
                    xmlSerializer.text(key.getKey());
                    xmlSerializer.endTag(null, "AESKey");
                }

                if ((key.getKey() != null) && (key.getEncryptAlg() == Constants.ENCRYPT_ALG.RSA)) {
                    xmlSerializer.startTag(null, "RSAKey");
                    xmlSerializer.text(key.getKey());
                    xmlSerializer.endTag(null, "RSAKey");
                }

                if (key.getHash() != null) {
                    xmlSerializer.startTag(null, "hash");
                    xmlSerializer.text(key.getHash());
                    xmlSerializer.endTag(null, "hash");
                }

                if (key.getHost() != null) {
                    xmlSerializer.startTag(null, "host");
                    xmlSerializer.text(key.getHost());
                    xmlSerializer.endTag(null, "host");
                }

                if (key.getHostBackup() != null) {
                    xmlSerializer.startTag(null, "hostBackup");
                    xmlSerializer.text(key.getHostBackup());
                    xmlSerializer.endTag(null, "hostBackup");
                }

                xmlSerializer.endTag(null, "key");
            }

            xmlSerializer.endTag(null, "keys");
            xmlSerializer.endTag(null, "nautilusKey");

            xmlSerializer.endDocument();
            xmlSerializer.flush();
            String dataWrite = writer.toString();
            fileos.write(dataWrite.getBytes());
            fileos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
