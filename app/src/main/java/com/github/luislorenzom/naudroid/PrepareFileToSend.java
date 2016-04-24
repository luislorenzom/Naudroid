package com.github.luislorenzom.naudroid;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;

import com.github.luislorenzom.naudroid.config.dao.KeyPairsDao;
import com.github.luislorenzom.naudroid.connection.ClientConnection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PrepareFileToSend extends ActionBarActivity {

    private ListView list;
    private String[] parameters;
    private ArrayAdapter<String> globalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prepare_file_to_send);

        final String filePath = (String) getIntent().getExtras().getSerializable("filePath");

        final List<String> parametersList = new ArrayList<>();
        parametersList.add(filePath);
        parametersList.add("Download Limit: ");
        parametersList.add("Date Limit: ");
        parametersList.add("Date Release: ");
        parametersList.add("Use Public Key: No");

        parameters = parametersList.toArray(new String[parametersList.size()]);

        list = (ListView)findViewById(R.id.listView2);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, parameters);
        globalAdapter = adapter;
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
                                if (which == 0) {
                                    parameters[1] = "Download Limit: ";
                                    adapter.notifyDataSetChanged();
                                } else {
                                    parameters[1] = "Download Limit: " + limits[which];
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });

                        builder.show();
                        break;

                    case 2:
                        // Show datepicker for date limit
                        AlertDialog.Builder builderDateLimit = new AlertDialog.Builder(PrepareFileToSend.this);
                        final DatePicker picker = new DatePicker(PrepareFileToSend.this);
                        picker.setCalendarViewShown(false);

                        builderDateLimit.setView(picker);
                        builderDateLimit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                parameters[2] = "Date Limit: ";
                                adapter.notifyDataSetChanged();
                            }
                        });

                        builderDateLimit.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String date = picker.getDayOfMonth() + "/" + picker.getMonth() + "/" + picker.getYear();
                                parameters[2] = "Date Limit: " + date;
                                adapter.notifyDataSetChanged();
                            }
                        });

                        builderDateLimit.show();
                        break;

                    case 3:
                        // Show datepicker for date release
                        AlertDialog.Builder builderDateRelease = new AlertDialog.Builder(PrepareFileToSend.this);
                        final DatePicker pickerTwo = new DatePicker(PrepareFileToSend.this);
                        pickerTwo.setCalendarViewShown(false);

                        builderDateRelease.setView(pickerTwo);
                        builderDateRelease.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                parameters[3] = "Date Release: ";
                                adapter.notifyDataSetChanged();
                            }
                        });

                        builderDateRelease.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String date = pickerTwo.getDayOfMonth() + "/" + pickerTwo.getMonth() + "/" + pickerTwo.getYear();
                                parameters[3] = "Date Release: " + date;
                                adapter.notifyDataSetChanged();
                            }
                        });

                        builderDateRelease.show();
                        break;

                    case 4:
                        // Show dialog for select public key
                        AlertDialog.Builder builderPublicKey = new AlertDialog.Builder(PrepareFileToSend.this);
                        builderPublicKey.setTitle("Use public key");
                        builderPublicKey.setMessage("Do you want to use your public key?");

                        builderPublicKey.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                parameters[4] = "Use Public Key: Yes";
                                adapter.notifyDataSetChanged();
                            }
                        });

                        builderPublicKey.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                parameters[4] = "Use Public Key: No";
                                dialog.cancel();
                                adapter.notifyDataSetChanged();
                            }
                        });

                        builderPublicKey.show();
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.add).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.save) {
            // File path
            final String filePath = parameters[0];
            final int downloadLimit;
            final Calendar dateLimit;
            final Calendar dateRelease;
            final String publicKey;

            // Download Limit
            String downloadLimitString = parameters[1].split(":")[1];
            if (downloadLimitString.equals(" ")) {
                downloadLimit = -1;
            } else {
                downloadLimit = Integer.parseInt(downloadLimitString.substring(1));
            }

            // Date limit
            String dateLimitString = parameters[2].split(":")[1];
            if (dateLimitString.equals(" ")) {
                dateLimit = null;
            } else {
                dateLimit = stringToCalendar(dateLimitString.substring(1));
            }

            // Date release
            String dateReleaseString = parameters[3].split(":")[1];
            if (dateReleaseString.equals(" ")) {
                dateRelease = null;
            } else {
                dateRelease = stringToCalendar(dateReleaseString);
            }

            // Public Key
            boolean usePublicKey = Boolean.parseBoolean(parameters[4].split(":")[1]);
            if (usePublicKey) {
                KeyPairsDao keyPairsDao =  new KeyPairsDao(this);
                // en vez de recibir el string mandar a la subrutina el objeto PublicKey
                publicKey = keyPairsDao.getKeyPair().getPublicKey();
            } else {
                publicKey = null;
            }

            final ProgressDialog progress = ProgressDialog.show(PrepareFileToSend.this, "Retrieving file",
                    "Conneting with the servers, wait a minute", true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    // Send the petition to clientConnection
                    ClientConnection clientConnection =  new ClientConnection(PrepareFileToSend.this);
                    clientConnection.saveFileInNetwork(filePath, downloadLimit, dateLimit, dateRelease, publicKey);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.dismiss();
                        }
                    });
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private Calendar stringToCalendar(String calendarString) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            cal.setTime(sdf.parse(calendarString));
            return cal;
        } catch (Exception e) {
            return null;
        }
    }
}
