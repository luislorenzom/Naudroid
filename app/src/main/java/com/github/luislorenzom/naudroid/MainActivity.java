package com.github.luislorenzom.naudroid;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.github.luislorenzom.naudroid.client.FileUtilities;
import com.github.luislorenzom.naudroid.client.KeyContainer;
import com.github.luislorenzom.naudroid.config.NaudroidPreferences;
import com.github.luislorenzom.naudroid.config.dao.KeyPairsDao;
import com.github.luislorenzom.naudroid.config.dao.PreferencesDao;
import com.github.luislorenzom.naudroid.config.dao.model.QuotesDataSource;
import com.github.luislorenzom.naudroid.config.dao.model.QuotesReaderDbHelper;
import com.github.luislorenzom.naudroid.connection.ConnectionUtilities;
import com.github.luislorenzom.naudroid.connection.NautilusKey;
import com.github.luislorenzom.naudroid.connection.NautilusKeyHandler;
import com.github.luislorenzom.naudroid.util.Constants;
import com.github.luislorenzom.naudroid.util.RSAManager;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            //Crear nuevo objeto QuotesDataSource
            //QuotesDataSource dataSource = new QuotesDataSource(this);

            if (false) {
                ConnectionUtilities connectionUtilities = new ConnectionUtilities(this);
                connectionUtilities.prepareFileToSend("/storage/emulated/0/Download/photo.jpg", 0, null, null, null);
            } else {

                ConnectionUtilities connectionUtilities = new ConnectionUtilities(this);
                List<File> files = new ArrayList<>();

                files.add(new File("/storage/emulated/0/Download/photo.jpg.0.aes256"));
                files.add(new File("/storage/emulated/0/Download/photo.jpg.1.aes256"));
                files.add(new File("/storage/emulated/0/Download/photo.jpg.2.aes256"));

                NautilusKeyHandler handler = new NautilusKeyHandler(this);
                List<NautilusKey> keys = handler.getKey("/storage/emulated/0/keys/photo.jpg_key.xml");

                connectionUtilities.restoreFile(files, keys);
            }

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
