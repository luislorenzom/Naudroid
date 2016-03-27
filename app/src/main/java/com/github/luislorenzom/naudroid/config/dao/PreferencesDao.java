package com.github.luislorenzom.naudroid.config.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.luislorenzom.naudroid.config.NaudroidPreferences;
import com.github.luislorenzom.naudroid.config.dao.model.QuotesDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis on 26/03/2016.
 */
public class PreferencesDao {

    private QuotesDataSource dataSource;
    private SQLiteDatabase database;

    public PreferencesDao(Context context) {
        dataSource = new QuotesDataSource(context);
        database = dataSource.database;
    }

    public void updatePreferences(NaudroidPreferences preferences) {

        // Actualizamos los path de los directorios
        if ((preferences.getSaveFilesPath() != null) && (preferences.getSaveKeysPath() != null)) {
            updatePreferencesTable(preferences.getSaveFilesPath(), preferences.getSaveKeysPath());
        }

        // Actualizamos la lista de servidores
        if (preferences.getServerPreferences() != null) {
            updateServers(preferences.getServerPreferences());
        }
    }

    public NaudroidPreferences getPreference() {
        List<ServerTable> serversTable = getAllServers();
        List<String> servers = new ArrayList<>();

        for (ServerTable serverTable : serversTable) {
            servers.add(serverTable.getServerIp());
        }

        PreferenceTable preferencesTable = getPreferences();
        String filesPath = preferencesTable.getSaveFilesPath();
        String keysPath = preferencesTable.getSaveKeysPath();
        return new NaudroidPreferences(filesPath, keysPath, servers);
    }

    /*********************/
    /* Private functions */
    /*********************/

    /*-------- OPERACIONES SERVER_PREFERENCES ------------*/
    private void updatePreferencesTable(String filesPath, String keysPath) {
        boolean update = false;
        // Recuperamos los parametros que tenemos en la base de datos
        PreferenceTable preferenceTable = getPreferences();

        //Nuestro contenedor de valores
        ContentValues values = new ContentValues();

        if ((preferenceTable.getSaveFilesPath() != filesPath) && (filesPath != null)) {
            values.put("save_files_path", filesPath);
            update = true;
        }

        if ((preferenceTable.getSaveKeysPath() != keysPath) && (keysPath != null)) {
            values.put("save_keys_path", keysPath);
            update = true;
        }

        // Clausula where
        if (update) {
            String selection = QuotesDataSource.ColumnPreferences.PREFERENCE_ID + " = ?";
            String[] selectionArgs = {preferenceTable.getId()};
            database.update("Preferences", values, selection, selectionArgs);
        }
    }

    private PreferenceTable getPreferences() {
        PreferenceTable preferenceTable = null;
        Cursor c = database.rawQuery("select * from Preferences", null);

        while(c.moveToNext()) {
            String preferecenId = c.getString(0);
            String saveFilesPath = c.getString(1);
            String saveKeysPath = c.getString(2);

            preferenceTable = new PreferenceTable(preferecenId, saveFilesPath, saveKeysPath);
        }
        return preferenceTable;
    }

    /**********************************************************/

    /*-------- OPERACIONES SERVER_PREFERENCES ------------*/

    private void updateServers(List<String> servers) {
        List<ServerTable> oldServers = getAllServers();
        List<ServerTable> deleteServers = new ArrayList<>();

        /* determinamos que ips hay que borrar y cuales guardar */
        for (ServerTable server : oldServers) {
            if (servers.contains(server.getServerIp())) {
                // quitamos la ip de la lista de nuevos para no tener que añadirla otra vez
                servers.remove(server.getServerIp());
            } else {
                // añadimos el servidor para eliminarlo
                deleteServers.add(server);
            }
        }

        /* eliminamos las ips viejas */
        for (ServerTable server : deleteServers) {
            deleteServer(server.getServerId());
        }

        /* añadimos las nuevas ips */
        if (servers != null) {
            for (String newServer : servers) {
                saveServer(newServer);
            }
        }
    }

    private List<ServerTable> getAllServers() {
        List<ServerTable> servers = new ArrayList<ServerTable>();

        Cursor c = database.rawQuery("select * from ServerPreferences", null);

        while(c.moveToNext()) {
            String serverId = c.getString(0);
            String serverIp = c.getString(1);
            servers.add(new ServerTable(serverId, serverIp));
        }
        return servers;
    }

    private void deleteServer(String serverId) {
        String selection = QuotesDataSource.ColumnServerPreferences.SERVER_ID + " = ?";
        String[] selectionArgs = {serverId};

        database.delete("ServerPreferences", selection, selectionArgs);
    }

    private void saveServer(String serverIp) {
        // Nuestro contenedor de valores
        ContentValues values = new ContentValues();

        // Añadimos el campo
        values.put(QuotesDataSource.ColumnServerPreferences.SERVER_IP, serverIp);

        // Insertamos en la base de datos
        database.insert("ServerPreferences", null, values);
    }

    /**********************************************************/
}
