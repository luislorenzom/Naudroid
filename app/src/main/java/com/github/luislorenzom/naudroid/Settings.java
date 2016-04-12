package com.github.luislorenzom.naudroid;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.luislorenzom.naudroid.config.dao.PreferencesDao;
import com.github.luislorenzom.naudroid.util.RSAManager;

public class Settings extends ActionBarActivity {

    private ListView list;
    private String[] settings = {"Files path", "Keys path", "Servers", "Copy your Public Key"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        PreferencesDao preferencesDao = new PreferencesDao(this);
        final RSAManager rsaManager = new RSAManager(this);

        // Link the listview with the xml
        list = (ListView)findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, settings);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

                switch (position) {
                    case 0:
                        Toast.makeText(getApplicationContext(), "Selected: " + position, Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Toast.makeText(getApplicationContext(), "Selected: " + position, Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        // Launch server activity
                        Intent intent = new Intent(Settings.this, ServersList.class);
                        startActivity(intent);
                        break;

                    case 3:
                        // Copy public key to clipboard
                        Toast.makeText(getApplicationContext(), "Your public key has been copy in you clipboard", Toast.LENGTH_SHORT).show();
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        String pkey = Base64.encodeToString(rsaManager.getPublicKey().getEncoded(), Base64.DEFAULT);
                        ClipData clip = ClipData.newPlainText("label", pkey);
                        clipboard.setPrimaryClip(clip);
                        break;
                }
            }
        });
    }

}
