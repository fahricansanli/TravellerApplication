package com.example.istanbultravelapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.istanbultravelapplication.R;
import com.example.istanbultravelapplication.adapters.PlaceAdapter;
import com.example.istanbultravelapplication.models.BaseThread;
import com.example.istanbultravelapplication.models.District;
import com.example.istanbultravelapplication.models.LeisureTimeActivity;
import com.example.istanbultravelapplication.models.Message;
import com.example.istanbultravelapplication.models.Place;
import com.example.istanbultravelapplication.models.Type;
import com.example.istanbultravelapplication.util.JsonHelper;

import java.io.IOException;
import java.util.ArrayList;

public class PlaceListActivity extends AppCompatActivity {
    private ListView listView;
    private static ArrayList<Place> listOfPlaces = new ArrayList<>();
    private PlaceAdapter adapter;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_place_list);
        this.listView = (ListView) findViewById(R.id.placeList);
        sharedPref = getSharedPreferences("traveller", Context.MODE_PRIVATE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Place selected = PlaceListActivity.listOfPlaces.get(position);
                Intent i = new Intent(getApplicationContext(),PlaceDetailActivity.class);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("placeName",selected.getName());
                editor.putString("placeDescription",selected.getDescription());
                editor.putString("ID",selected.getID()+"");
                editor.apply();
                startActivity(i);
                finish();
            }
        });

        PlaceListThread thread = new PlaceListThread();
        thread.start();

    }
    public void setPlaceAdapter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new PlaceAdapter(getApplicationContext(),PlaceListActivity.listOfPlaces);
                listView.setAdapter(adapter);
            }
        });
    }

    class PlaceListThread extends BaseThread {
        @Override
        public void run() {
            super.run();
            District district = new District(sharedPref.getString("districtName",""),"");
            LeisureTimeActivity activity = new LeisureTimeActivity(sharedPref.getString("activityName",""));
            Message messageForServer = new Message(Type.GET_PLACES, JsonHelper.convertToJSON(district),JsonHelper.convertToJSON(activity),"","");
            sendMessageToServer(messageForServer);
            try {
                String responseInJsonFormat = inputFromClient.readLine();
                Message responseMessage = JsonHelper.convertToObject(responseInJsonFormat,Message.class);
                PlaceListActivity.listOfPlaces = JsonHelper.getDataAsArrayList(responseMessage.getJsonData(),Place.class);
                setPlaceAdapter();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,EventActivity.class);
        startActivity(i);
        finish();
    }

}
