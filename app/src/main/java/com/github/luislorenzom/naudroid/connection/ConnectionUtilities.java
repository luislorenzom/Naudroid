package com.github.luislorenzom.naudroid.connection;

import java.io.File;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Luis on 31/03/2016.
 */
public class ConnectionUtilities {

    public List<NautilusMessage> prepareFileToSend(String filePath, int downloadLimit,
                                                   Calendar dateLimit, Calendar releaseDate,
                                                   PublicKey pKey) {

        return null;
    }

    public List<String> getHostAndBackupFromConfig() {

        return null;
    }

    public void restoreFile(List<File> files, List<NautilusKey> keys) {

    }


    /*********************/
    /* PRIVATE FUNCTIONS */
    /*********************/

    private String getHashFromFile(File file, String algorithm) {
        return null;
    }

}
