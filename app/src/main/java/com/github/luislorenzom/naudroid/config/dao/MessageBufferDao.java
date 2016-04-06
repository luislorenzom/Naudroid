package com.github.luislorenzom.naudroid.config.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.github.luislorenzom.naudroid.config.dao.model.QuotesDataSource;

/**
 * Created by Luis on 05/04/2016.
 */
public class MessageBufferDao {

    private QuotesDataSource dataSource;
    private SQLiteDatabase database;

    public MessageBufferDao(Context context) {
        dataSource = new QuotesDataSource(context);
        database = dataSource.database;
    }

    public MessageBuffered getElementFromBuffer() {
        MessageBuffered msg = null;
        Cursor c = database.rawQuery("select * from " + QuotesDataSource.MESSAGE_BUFFER_NAME
                + " ORDER BY " + QuotesDataSource.ColumnMessageBuffer.MESSAGE_ID + " ASC LIMIT 1", null);

        while(c.moveToNext()) {
            String id = c.getString(0);
            String serverIp = c.getString(1);
            String hash = c.getString(2);

            msg = new MessageBuffered(id, serverIp, hash);
        }

        deleteMessageIntoBuffer(msg.getId());

        return msg;
    }

    public void setMessageIntoBuffer(MessageBuffered msg) {
        // Nuestro contenedor de valores
        ContentValues values = new ContentValues();

        // Añadimos los campos
        values.put(QuotesDataSource.ColumnMessageBuffer.HASH, msg.getHash());
        values.put(QuotesDataSource.ColumnMessageBuffer.SERVER_IP, msg.getServerIp());

        // Insertamos en la base de datos
        database.insert(QuotesDataSource.MESSAGE_BUFFER_NAME, null, values);
    }

    public boolean anyMessage() {
        long cnt = DatabaseUtils.queryNumEntries(database, QuotesDataSource.MESSAGE_BUFFER_NAME);
        if (cnt > 0) {
            return true;
        } else {
            return false;
        }
    }

    /*********************/
    /* PRIVATE FUNCTIONS */
    /*********************/

    private void deleteMessageIntoBuffer(String messageId) {
        String selection = QuotesDataSource.ColumnMessageBuffer.MESSAGE_ID + " = ?";
        String[] selectionArgs = {messageId};

        database.delete(QuotesDataSource.MESSAGE_BUFFER_NAME, selection, selectionArgs);
    }

}
