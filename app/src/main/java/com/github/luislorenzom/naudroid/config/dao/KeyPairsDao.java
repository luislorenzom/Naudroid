package com.github.luislorenzom.naudroid.config.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.luislorenzom.naudroid.config.dao.model.QuotesDataSource;

/**
 * Created by Luis on 29/03/2016.
 */
public class KeyPairsDao {

    private QuotesDataSource dataSource;
    private SQLiteDatabase database;

    public KeyPairsDao(Context context) {
        dataSource = new QuotesDataSource(context);
        this.database = dataSource.database;
    }

    public KeyPairTable getKeyPair() {
        KeyPairTable keyPairTable = null;
        Cursor c = database.rawQuery("select * from " + QuotesDataSource.RSA_KEYS_TABLE_NAME, null);

        while(c.moveToNext()) {
            String publicKey = c.getString(1);
            String privateKey = c.getString(2);

            keyPairTable = new KeyPairTable(privateKey, publicKey);
        }

        return keyPairTable;
    }

    public void initKeyPair(String publicKey, String privateKey) {
        // Nuestro contenedor de valores
        ContentValues values = new ContentValues();

        // Añadimos los campos
        values.put(QuotesDataSource.ColumnRSAKeys.PUBLIC_KEY, publicKey);
        values.put(QuotesDataSource.ColumnRSAKeys.PRIVATE_KEY, privateKey);

        // Insertamos en la base de datos
        database.insert(QuotesDataSource.RSA_KEYS_TABLE_NAME, null, values);
    }
}
