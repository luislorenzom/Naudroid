package com.github.luislorenzom.naudroid;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PrepareFileToSend extends ActionBarActivity {

    private ListView list;
    private String[] paremeters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_file_to_send);

        final String filePath = (String) getIntent().getExtras().getSerializable("filePath");
        List<String> parametersList = new ArrayList<>();
        parametersList.add(filePath);
        parametersList.add("Download Limit: ");
        parametersList.add("Date Limit: ");
        parametersList.add("Date Release: ");
        parametersList.add("Use Public Key? No");

        paremeters = parametersList.toArray(new String[parametersList.size()]);
        list = (ListView)findViewById(R.id.listView2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, paremeters);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        // Show dialog with download limit
                        AlertDialog.Builder builder = new AlertDialog.Builder(PrepareFileToSend.this);
                        builder.setTitle("Download Limit");
                        final String[] limits = {"Don't use","1","2","3","4","5"};

                        builder.setItems(limits, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                Toast.makeText(getApplicationContext(), limits[which], Toast.LENGTH_SHORT).show();
                            }
                        });

                        builder.show();
                        break;

                    case 2:
                        // Show datepicker for date limit
                        showDialog(0);
                        break;

                    case 3:
                        // Show datepicker for date release
                        showDialog(0);
                        break;

                    case 4:
                        // Show dialog for select public key
                        AlertDialog.Builder builderPublicKey = new AlertDialog.Builder(PrepareFileToSend.this);
                        builderPublicKey.setTitle("Use public key");
                        builderPublicKey.setMessage("Do you want to use your public key?");

                        builderPublicKey.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do something!
                            }
                        });

                        builderPublicKey.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do something!
                                dialog.cancel();
                            }
                        });

                        builderPublicKey.show();
                        break;
                }
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(PrepareFileToSend.this, myDateListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), dayOfMonth+"/"+monthOfYear+"/"+year, Toast.LENGTH_SHORT).show();
        }
    };
}
