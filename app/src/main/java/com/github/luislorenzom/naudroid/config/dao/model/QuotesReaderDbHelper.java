package com.github.luislorenzom.naudroid.config.dao.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Luis on 26/03/2016.
 */
public class QuotesReaderDbHelper extends SQLiteOpenHelper {

    private static final String  DATABASE_NAME = "NaudroidDataBase.db";
    private static final int DATABASE_VERSION = 1;

    public QuotesReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creamos las tablas
        db.execSQL(QuotesDataSource.CREATE_PREFERENCES);
        db.execSQL(QuotesDataSource.CREATE_SERVER_PREFERENCES);
        db.execSQL(QuotesDataSource.CREATE_KEYPAIR_PREFERENCES);

        // Insertamos los registro iniciales
        db.execSQL(QuotesDataSource.INSERT_TYPES_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Añadimos los cambios que se realizaran en el esquema
    }
}
