package com.github.luislorenzom.naudroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.github.luislorenzom.naudroid.config.NaudroidPreferences;
import com.github.luislorenzom.naudroid.config.dao.PreferencesDao;

import java.util.List;

public class ServersList extends ActionBarActivity {

    private ListView list;
    private String[] servers;
    private PreferencesDao preferencesDao;
    private String ipRegExp = "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers_list);

        preferencesDao = new PreferencesDao(this);
        List<String> serversList = preferencesDao.getPreference().getServerPreferences();

        servers = serversList.toArray(new String[serversList.size()]);

        list = (ListView)findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, servers);
        list.setAdapter(adapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final int pos = position;

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        ServersList.this);
                alertDialogBuilder
                        .setTitle("Delete the server")
                        .setMessage("Do you want to delete this server?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Logic to delete the ip
                                String removeItem = preferencesDao.getPreference().getServerPreferences().get(pos);
                                List<String> newServers = preferencesDao.getPreference().getServerPreferences();
                                newServers.remove(removeItem);
                                preferencesDao.updatePreferences(new NaudroidPreferences(null, null, newServers));

                                Toast.makeText(getApplicationContext(), "server deleted", Toast.LENGTH_SHORT).show();
                                // Refesh de la activity
                                finish();
                                startActivity(getIntent());
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

                return false;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.add) {
            //----- fragment to save new server ip
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add new server Ip");

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String ip = input.getText().toString();
                    // check if the ip which receive is correct
                    if (!(ip.matches(ipRegExp))) {
                        Toast.makeText(getApplicationContext(), "server ip wrong :-(", Toast.LENGTH_SHORT).show();
                    } else {
                        //---
                        // Save the ip
                        List<String> newServers = preferencesDao.getPreference().getServerPreferences();
                        newServers.add(ip);
                        preferencesDao.updatePreferences(new NaudroidPreferences(null, null, newServers));

                        Toast.makeText(getApplicationContext(), "server added", Toast.LENGTH_SHORT).show();
                        // Refresh the listview
                        finish();
                        startActivity(getIntent());
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            //-----
        }

        return super.onOptionsItemSelected(item);
    }
}
