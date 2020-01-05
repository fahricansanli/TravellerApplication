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
import com.example.istanbultravelapplication.adapters.DistrictAdapter;
import com.example.istanbultravelapplication.models.BaseThread;
import com.example.istanbultravelapplication.models.District;
import com.example.istanbultravelapplication.models.Message;
import com.example.istanbultravelapplication.models.Type;
import com.example.istanbultravelapplication.util.JsonHelper;

import java.io.IOException;
import java.util.ArrayList;

public class DistrictActivity extends AppCompatActivity {
    private SearchView searchView;
    private ListView listView;
    private static ArrayList<District> listOfDistricts;
    private static ArrayList<District> searchedListOfDistricts = new ArrayList<District>();
    private DistrictAdapter adapter;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_district);
        this.searchView = (SearchView) findViewById(R.id.searchView);
        this.listView = (ListView) findViewById(R.id.districtList);
        this.adapter = new DistrictAdapter(this,DistrictActivity.searchedListOfDistricts);
        this.listView.setAdapter(this.adapter);
        sharedPref = getSharedPreferences("traveller", Context.MODE_PRIVATE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                final String selectedDistrict =  (String)((District)listView.getItemAtPosition(position)).getDistrictName();
                AlertDialog.Builder builder = new AlertDialog.Builder(DistrictActivity.this);
                builder.setTitle("To continue to the next step")
                        .setMessage("Do you want to continue with " + selectedDistrict + " ?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(getApplicationContext(), EventActivity.class);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("districtName",selectedDistrict.toLowerCase());
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
                    searchedListOfDistricts.clear();
                    searchedListOfDistricts.addAll(listOfDistricts);
                    adapter.notifyDataSetChanged();
                    return true;
                }
                else{
                    ArrayList<District> newList = new ArrayList<District>();
                    for(District cur:listOfDistricts){
                        if(cur.getDistrictName().toLowerCase().startsWith(newText.toLowerCase())){
                            newList.add(cur);
                        }
                    }
                    searchedListOfDistricts.clear();
                    searchedListOfDistricts.addAll(newList);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            }
        });

        DistrictActivity.DistricThread thread = new DistrictActivity.DistricThread();
        thread.start();

    }
    public void setDistrictAdapter(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    class DistricThread extends BaseThread {
        @Override
        public void run() {
            super.run();
            Message messageForServer = new Message(Type.GET_DISTRICT,"","","","");
            sendMessageToServer(messageForServer);
            try {
                String responseInJsonFormat = inputFromClient.readLine();
                Message responseMessage = JsonHelper.convertToObject(responseInJsonFormat,Message.class);
                DistrictActivity.listOfDistricts = JsonHelper.getDataAsArrayList(responseMessage.getJsonData(),District.class);
                DistrictActivity.searchedListOfDistricts.clear();
                DistrictActivity.searchedListOfDistricts.addAll(DistrictActivity.listOfDistricts);
                setDistrictAdapter();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}
