package com.example.istanbultravelapplication.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.istanbultravelapplication.R;
import com.example.istanbultravelapplication.adapters.CommentAdapter;
import com.example.istanbultravelapplication.models.BaseThread;
import com.example.istanbultravelapplication.models.Comment;
import com.example.istanbultravelapplication.models.District;
import com.example.istanbultravelapplication.models.LeisureTimeActivity;
import com.example.istanbultravelapplication.models.Message;
import com.example.istanbultravelapplication.models.Place;
import com.example.istanbultravelapplication.models.Type;
import com.example.istanbultravelapplication.util.JsonHelper;

import java.io.IOException;
import java.util.ArrayList;

public class PlaceDetailActivity extends AppCompatActivity {
    private ListView listView;
    private TextView placeName;
    private TextView placeDescription;
    public static ArrayList<Comment> listOfComments = new ArrayList<>();
    public CommentAdapter adapter;
    private Button addComment;
    SharedPreferences sharedPref;
    private EditText commentText;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 3*1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_place_detail);
        this.placeName = (TextView) findViewById(R.id.placeName);
        this.placeDescription = (TextView) findViewById(R.id.placeDescription);
        this.listView = (ListView) findViewById(R.id.commentList);
        this.addComment = (Button) findViewById(R.id.addCommentButton);
        sharedPref = getSharedPreferences("traveller", Context.MODE_PRIVATE);

        placeName.setText(sharedPref.getString("placeName", ""));
        placeDescription.setText("Brief Description: " + sharedPref.getString("placeDescription", ""));

        this.adapter = new CommentAdapter(this, PlaceDetailActivity.listOfComments);
        this.listView.setAdapter(this.adapter);

        this.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCommentDialog();
            }
        });
        PlaceDetailActivity.PlaceDetailThread thread = new PlaceDetailActivity.PlaceDetailThread();
        thread.start();

    }


    @Override
    protected void onResume() {

        handler.postDelayed( runnable = new Runnable() {
            public void run() {

                PlaceDetailActivity.PlaceDetailThread thread = new PlaceDetailActivity.PlaceDetailThread();
                thread.start();

                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }


    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    public void showAddCommentDialog(){
        final String activityName = sharedPref.getString("activityName","");
        final String districtName = sharedPref.getString("districtName","");
        final String placeName = sharedPref.getString("placeName","");
        final String placeDescription = sharedPref.getString("placeDescription","");
        final int ID = Integer.parseInt(sharedPref.getString("ID",""));
        final String username = sharedPref.getString("username","");

        AlertDialog.Builder builder = new AlertDialog.Builder(PlaceDetailActivity.this);

        LayoutInflater inflater = PlaceDetailActivity.this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_comment,null);
        commentText = (EditText) view.findViewById(R.id.commentText);
        builder.setView(view)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String newComment = commentText.getText().toString();
                        if(newComment.length()==0){
                            return;
                        }
                        PlaceDetailActivity.AddCommentThread thread = new PlaceDetailActivity.AddCommentThread(activityName,districtName,placeName,placeDescription,ID,username,newComment);
                        thread.start();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog  = builder.create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, PlaceListActivity.class);
        startActivity(i);
        finish();
    }


    public void changeCommentList() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    class PlaceDetailThread extends BaseThread {
        @Override
        public void run() {
            super.run();
            District district = new District(sharedPref.getString("districtName", ""), "");
            LeisureTimeActivity selectedActivity = new LeisureTimeActivity(sharedPref.getString("activityName", ""));
            Place selectedPlace = new Place(sharedPref.getString("placeName", ""), sharedPref.getString("placeDescription", ""), Integer.parseInt(sharedPref.getString("ID", "")));
            Message messageForServer = new Message(Type.PLACE_DETAIL, JsonHelper.convertToJSON(district), JsonHelper.convertToJSON(selectedActivity), JsonHelper.convertToJSON(selectedPlace), "");
            sendMessageToServer(messageForServer);
            try {
                String responseInJsonFormat = inputFromClient.readLine();
                Message responseMessage = JsonHelper.convertToObject(responseInJsonFormat, Message.class);
                final ArrayList<Comment> newComments = JsonHelper.getDataAsArrayList(responseMessage.getJsonData(), Comment.class);
                PlaceDetailActivity.listOfComments.clear();
                PlaceDetailActivity.listOfComments.addAll(newComments);
                changeCommentList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class AddCommentThread extends BaseThread {
        String activityName;
        String districtName;
        String placeName;
        String placeDescription;
        int ID;
        String username;
        String newComment;
        public AddCommentThread(String activityName, String districtName, String placeName, String placeDescription, int ID, String username, String newComment){
            this.activityName = activityName;
            this.districtName = districtName;
            this.placeName = placeName;
            this.placeDescription = placeDescription;
            this.ID = ID;
            this.username = username;
            this.newComment = newComment;
        }
        @Override
        public void run() {
            super.run();
            District district = new District(districtName,"");
            LeisureTimeActivity activity = new LeisureTimeActivity(activityName);
            Place place = new Place(placeName,placeDescription,ID);
            Comment comment = new Comment(newComment,username);
            Message messageForServer = new Message(Type.UPDATE_COMMENT_LIST,JsonHelper.convertToJSON(district), JsonHelper.convertToJSON(activity),JsonHelper.convertToJSON(place),JsonHelper.convertToJSON(comment));
            sendMessageToServer(messageForServer);
            try {
                String responseInJsonFormat = inputFromClient.readLine();
                Message responseMessage = JsonHelper.convertToObject(responseInJsonFormat,Message.class);
                final ArrayList<Comment> newList = JsonHelper.getDataAsArrayList(responseMessage.getJsonData(),Comment.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PlaceDetailActivity.listOfComments.clear();
                        PlaceDetailActivity.listOfComments.addAll(newList);
                        changeCommentList();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

