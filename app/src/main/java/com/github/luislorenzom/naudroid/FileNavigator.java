package com.github.luislorenzom.naudroid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.luislorenzom.naudroid.config.NaudroidPreferences;
import com.github.luislorenzom.naudroid.config.dao.PreferencesDao;
import com.github.luislorenzom.naudroid.connection.ClientConnection;
import com.github.luislorenzom.naudroid.connection.NautilusKey;
import com.github.luislorenzom.naudroid.connection.NautilusKeyHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileNavigator extends ActionBarActivity {

    private ListView list;
    private String[] folders;
    private int originKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_navigator);

        // Get the origin parameter from the previous activity
        final int origin = (int) getIntent().getExtras().getSerializable("origin");
        originKey = origin;

        // Set title
        if (origin == 0) {
            setTitle("Selecting the files path");
        }

        if (origin == 1) {
            setTitle("Selecting the keys path");
        }

        if (origin == 2) {
            setTitle("Select file to save");
        }

        if (origin == 3) {
            setTitle("Select key to retrieval");
        }

        // Invoke the preferencesDao for save the new path
        final PreferencesDao preferencesDao = new PreferencesDao(this);

        // Get the filePath parameter from the previous activity
        final String filePath = (String) getIntent().getExtras().getSerializable("filePath");

        // Set the text path in the textView
        final TextView texViewPath = (TextView)findViewById(R.id.textView);
        texViewPath.setText(filePath);

        // Get all the writeables folders
        final File folder = new File(filePath);
        File[] files = folder.listFiles();
        List<String> foldersList = new ArrayList<>();

        // Check if we are in the main folder to add the "go to parent folder" option
        if (!(filePath.equals("/"))) {
            foldersList.add("go to parent folder");
        }

        // For save some folder
        if ((origin == 0) || (origin == 1)) {
            for (File file : files) {
                if (file.canWrite() && file.isDirectory()) {
                    foldersList.add(file.getName());
                }
            }
        }

        // For save some file
        if ((origin == 2) || (origin == 3)) {
            for (File file : files) {
                if (file.canWrite()) {
                    foldersList.add(file.getName());
                }
            }
        }

        folders = foldersList.toArray(new String[foldersList.size()]);

        list = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, folders);
        list.setAdapter(adapter);

        //Set the listener
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((position == 0) && (folders[position].equals("go to parent folder"))) {
                    // Go to parent folder
                    File tmpFile = new File(filePath);
                    String newPath = tmpFile.getParent();
                    Intent intent =  new Intent(FileNavigator.this, FileNavigator.class);
                    intent.putExtra("filePath", newPath);
                    intent.putExtra("origin", origin);
                    finish();
                    startActivity(intent);
                } else {
                    // Go to child folder
                    File newFile = new File(filePath + "/" + folders[position]);
                    // Check if the file is directory or not
                    if (!(newFile.isDirectory())) {
                        texViewPath.setText(newFile.getAbsolutePath());
                    } else {
                        String newPath = newFile.getAbsolutePath();
                        Intent intent = new Intent(FileNavigator.this, FileNavigator.class);
                        intent.putExtra("filePath", newPath);
                        intent.putExtra("origin", origin);
                        finish();
                        startActivity(intent);
                    }
                }
            }
        });

        // Set the button3 listener
        final Button button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NaudroidPreferences preferences = null;
                if (origin < 2) {
                    if (origin == 0) {
                        preferences = new NaudroidPreferences(filePath, null, null);
                    }

                    if (origin == 1) {
                        preferences = new NaudroidPreferences(null, filePath, null);
                    }

                    preferencesDao.updatePreferences(preferences);
                    // now come back to settings activity
                    Intent intent = new Intent(FileNavigator.this, Settings.class);
                    finish();
                    startActivity(intent);
                } else {
                    if (origin == 2) {
                        // If select a file
                        if (!(new File((String) texViewPath.getText()).isDirectory())) {
                            Intent intent = new Intent(FileNavigator.this, PrepareFileToSend.class);
                            intent.putExtra("filePath", texViewPath.getText());
                            finish();
                            startActivity(intent);
                        } else {
                            // If select a directory
                            Toast.makeText(getApplicationContext(), "Can't upload a folder, select some file", Toast.LENGTH_SHORT).show();
                        }
                    }

                    if (origin == 3) {
                        // Check if is a xml file
                        String fileName = new File((String) texViewPath.getText()).getName();
                        int fileNameLength = fileName.length();

                        if (fileName.substring(fileNameLength - 4, fileNameLength).equals(".xml")) {
                            // Check if the file is a correct key
                            NautilusKeyHandler nautilusKeyHandler = new NautilusKeyHandler(FileNavigator.this);
                            List<NautilusKey> keys = nautilusKeyHandler.getKey((String) texViewPath.getText());

                            if (keys.size() > 0) {
                                // Launch loading screen
                                final ProgressDialog progress = ProgressDialog.show(FileNavigator.this, "Retrieving file",
                                        "Conneting with the servers, wait a minute", true);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ClientConnection clientConnection =  new ClientConnection(FileNavigator.this);
                                        clientConnection.getFileFromKey((String) texViewPath.getText());
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                progress.dismiss();
                                            }
                                        });
                                    }
                                });

                            } else {
                                Toast.makeText(getApplicationContext(), "Can't get the keys from this file", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Can't get the keys from this file format", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // If back button is preassure then come back to settings
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (originKey <= 1)) {
            Intent intent = new Intent(FileNavigator.this, Settings.class);
            finish();
            startActivity(intent);
        } else {
            Intent intent = new Intent(FileNavigator.this, MainActivity.class);
            finish();
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
