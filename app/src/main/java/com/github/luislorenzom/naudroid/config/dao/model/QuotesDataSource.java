package com.github.luislorenzom.naudroid.config.dao.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Luis on 26/03/2016.
 */
public class QuotesDataSource {

    // Nombre de las tablas
    public static final String PREFERENCES_TABLE_NAME = "Preferences";
    public static final String SERVER_PREFERENCES_TABLE_NAME = "ServerPreferences";
    public static final String RSA_KEYS_TABLE_NAME = "RSAKeys";
    public static final String MESSAGE_BUFFER_NAME = "BufferMessage";

    // Tipos de los campos
    public static final String STRING_TYPE = "text";
    public static final String INT_TYPE = "integer";

    // Atributos para la instanciacion de la base de datos
    private QuotesReaderDbHelper openHelper;
    public SQLiteDatabase database;

    public QuotesDataSource(Context context) {
        // Creamos una instancia hacia la base de datos
        openHelper = new QuotesReaderDbHelper(context);
        database = openHelper.getWritableDatabase();
    }

    /* ------------- Declaracion de las tablas -------------------- */

    // Campos de la tabla Preferences
    public static class ColumnPreferences {
        public static final String PREFERENCE_ID = BaseColumns._ID;
        public static final String SAVE_FILES_PATH = "save_files_path";
        public static final String SAVE_KEYS_PATH = "save_keys_path";
    }

    // Campos de la tabla ServerPreferences
    public static class ColumnServerPreferences {
        public static final String SERVER_ID = BaseColumns._ID;
        public static final String SERVER_IP = "server_ip";
    }

    // Campos de la tabla RSAKeys
    public static class ColumnRSAKeys {
        public static final String KEY_PAIR_ID = BaseColumns._ID;
        public static final String PUBLIC_KEY = "public_key";
        public static final String PRIVATE_KEY = "private_key";
    }

    // Campos de la tabla MessageBuffer
    public static class ColumnMessageBuffer {
        public static final String MESSAGE_ID = BaseColumns._ID;
        public static final String SERVER_IP = "server_ip";
        public static final String HASH = "hash";
    }

    /* ------------- Scripts de creacion -------------------- */

    public static final String CREATE_PREFERENCES =
            "CREATE TABLE " + PREFERENCES_TABLE_NAME + "(" +
                    ColumnPreferences.PREFERENCE_ID + " " + INT_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    ColumnPreferences.SAVE_FILES_PATH + " " + STRING_TYPE + " NOT NULL," +
                    ColumnPreferences.SAVE_KEYS_PATH + " " + STRING_TYPE + " NOT NULL)";

    public static final String CREATE_SERVER_PREFERENCES =
            "CREATE TABLE " + SERVER_PREFERENCES_TABLE_NAME + "(" +
                    ColumnServerPreferences.SERVER_ID + " " + INT_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    ColumnServerPreferences.SERVER_IP + " " + STRING_TYPE + " NOT NULL)";

    public static final String CREATE_KEYPAIR_PREFERENCES =
            "CREATE TABLE " + RSA_KEYS_TABLE_NAME + "(" +
                    ColumnRSAKeys.KEY_PAIR_ID + " " + INT_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    ColumnRSAKeys.PUBLIC_KEY + " " + STRING_TYPE + " NOT NULL," +
                    ColumnRSAKeys.PRIVATE_KEY + " " + STRING_TYPE + " NOT NULL)";

    public static final String CREATE_MESSAGE_BUFFER_TABLE =
            "CREATE TABLE " + MESSAGE_BUFFER_NAME + "(" +
                ColumnMessageBuffer.MESSAGE_ID + " " + INT_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                ColumnMessageBuffer.SERVER_IP + " " + STRING_TYPE + " NOT NULL," +
                ColumnMessageBuffer.HASH + " " + STRING_TYPE + " NOT NULL)";

    /* ------------- Scripts de inicializacion -------------------- */

    public static final String INSERT_TYPES_SCRIPT =
            "INSERT INTO " + PREFERENCES_TABLE_NAME + " VALUES(" +
                    "null," +
                    "\"/storage/naudroid/files/\"" + "," +
                    "\"/storage/naudroid/keys/\"" + ")";

    /* ------------------------------------------------------ */
}
