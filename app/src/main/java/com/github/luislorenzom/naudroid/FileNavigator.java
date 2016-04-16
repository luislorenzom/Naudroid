package com.github.luislorenzom.naudroid;

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

import com.github.luislorenzom.naudroid.config.NaudroidPreferences;
import com.github.luislorenzom.naudroid.config.dao.PreferencesDao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileNavigator extends ActionBarActivity {

    private ListView list;
    private String[] folders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_navigator);

        // Get the origin parameter from the previous activity
        final int origin = (int) getIntent().getExtras().getSerializable("origin");

        // Set title
        if (origin == 0) {
            setTitle("selecting the files path");
        }

        if (origin == 1) {
            setTitle("Selecting the keys path");
        }

        // Invoke the preferencesDao for save the new path
        final PreferencesDao preferencesDao = new PreferencesDao(this);

        // Get the filePath parameter from the previous activity
        final String filePath = (String) getIntent().getExtras().getSerializable("filePath");

        // Set the text path in the textView
        TextView texViewPath = (TextView)findViewById(R.id.textView);
        texViewPath.setText(filePath);

        // Get all the writeables folders
        final File folder = new File(filePath);
        File[] files = folder.listFiles();
        List<String> foldersList = new ArrayList<>();
        for (File file : files) {
            if (file.canWrite() && file.isDirectory()) {
                foldersList.add(file.getName());
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
                File newFile = new File(filePath + "/" +folders[position]);
                String newPath = newFile.getAbsolutePath();
                Intent intent =  new Intent(FileNavigator.this, FileNavigator.class);
                intent.putExtra("filePath", newPath);
                intent.putExtra("origin", origin);
                finish();
                startActivity(intent);
            }
        });

        // Set the button3 listener
        final Button button = (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NaudroidPreferences preferences = null;

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
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // If back button is preassure then come back to settings
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(FileNavigator.this, Settings.class);
            finish();
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
