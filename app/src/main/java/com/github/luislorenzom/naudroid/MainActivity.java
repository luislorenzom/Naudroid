package com.github.luislorenzom.naudroid;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.luislorenzom.naudroid.config.NaudroidPreferences;
import com.github.luislorenzom.naudroid.config.dao.PreferencesDao;
import com.github.luislorenzom.naudroid.config.dao.model.QuotesDataSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //Crear nuevo objeto QuotesDataSource
            QuotesDataSource dataSource = new QuotesDataSource(this);

            //boolean exist = new File("/data/data/com.github.luislorenzom.naudroid/databases/NaudroidDataBase.db").exists();
            //Log.e("exist", String.valueOf(exist));

            /*PreferencesDao preferecesDao = new PreferencesDao(this);

            List<String> servers = new ArrayList<>();
            servers.add("192.168.1.57");
            NaudroidPreferences newPrefereces = new NaudroidPreferences("/data/data/naudroidFiles1", "/data/data/naudroidKeys2", servers);

            preferecesDao.updatePreferences(newPrefereces);

            NaudroidPreferences prefereces = preferecesDao.getPreference();

            Log.e("preferences", prefereces.getSaveFilesPath());
            Log.e("preferences", prefereces.getSaveKeysPath());
            Log.e("preferences", String.valueOf(prefereces.getServerPreferences().size()));
            Log.e("preferences", prefereces.getServerPreferences().get(0));*/

        } catch (Exception e) {
           Log.e("errorTag",Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
