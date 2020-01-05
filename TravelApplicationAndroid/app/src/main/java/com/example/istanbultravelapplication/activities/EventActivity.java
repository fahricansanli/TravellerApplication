package com.example.istanbultravelapplication.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.istanbultravelapplication.R;
import com.example.istanbultravelapplication.adapters.LeisureActivityAdapter;
import com.example.istanbultravelapplication.models.BaseThread;
import com.example.istanbultravelapplication.models.LeisureTimeActivity;
import com.example.istanbultravelapplication.models.Message;
import com.example.istanbultravelapplication.models.Type;
import com.example.istanbultravelapplication.util.JsonHelper;

import java.io.IOException;
import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {
    private SearchView searchView;
    private ListView listView;
    public static ArrayList<LeisureTimeActivity> listOfActivities;
    public static ArrayList<LeisureTimeActivity> searchedListOfActivities = new ArrayList<LeisureTimeActivity>();
    private LeisureActivityAdapter adapter;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_event);
        this.searchView = (SearchView) findViewById(R.id.searchView2);
        this.listView = (ListView) findViewById(R.id.eventList);
        sharedPref = getSharedPreferences("traveller", Context.MODE_PRIVATE);

        this.adapter = new LeisureActivityAdapter(this,EventActivity.searchedListOfActivities);
        this.listView.setAdapter(this.adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                final LeisureTimeActivity selectedActivity = (LeisureTimeActivity) listView.getItemAtPosition(position);
                final String selectedActivityName = (String) selectedActivity.getName();
                AlertDialog.Builder builder = new AlertDialog.Builder(EventActivity.this);
                builder.setTitle("To continue to the next step")
                        .setMessage("Do you want to continue with " + selectedActivityName + " ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent i = new Intent(getApplicationContext(),PlaceListActivity.class);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("activityName",selectedActivityName.toLowerCase());
                                editor.apply();
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog  = builder.create();
                dialog.show();
            }
        });

        this.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() == 0){
                    searchedListOfActivities.clear();
                    searchedListOfActivities.addAll(listOfActivities);
                    adapter.notifyDataSetChanged();
                    return true;
                }
                else{
                    ArrayList<LeisureTimeActivity> newList = new ArrayList<LeisureTimeActivity>();
                    for(LeisureTimeActivity cur:listOfActivities){
                        if(cur.getName().toLowerCase().startsWith(newText.toLowerCase())){
                            newList.add(cur);
                        }
                    }
                    searchedListOfActivities.clear();
                    searchedListOfActivities.addAll(newList);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            }
        });

        EventThread thread = new EventThread();
        thread.start();
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, DistrictActivity.class);
        startActivity(i);
        finish();
    }

    public void setListActivityAdapter() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    class EventThread extends BaseThread {
        @Override
        public void run() {
            super.run();
            Message messageForServer = new Message(Type.GET_LEISURE_ACTIVITY,"","","","");
            sendMessageToServer(messageForServer);
            try {
                String responseInJsonFormat = inputFromClient.readLine();
                Message responseMessage = JsonHelper.convertToObject(responseInJsonFormat,Message.class);
                EventActivity.listOfActivities = JsonHelper.getDataAsArrayList(responseMessage.getJsonData(), LeisureTimeActivity.class);
                EventActivity.searchedListOfActivities.clear();
                EventActivity.searchedListOfActivities.addAll(EventActivity.listOfActivities);
                setListActivityAdapter();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
