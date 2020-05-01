package com.example.bsport;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bsport.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

public class Activity extends AppCompatActivity {
    public EditText name,description,image,type,number_of_players;
    public int id;
    public String date;

    public Activity(){

    }
    public Activity(EditText name, EditText description,EditText type,EditText number_of_players ,int id) {
        this.name = name;
        this.date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        this.description = description;
        this.id = id;
        this.number_of_players = number_of_players;
        this.type = type;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newactiviry);
        /*Button MyActivity = (Button) findViewById(R.id.submit_activity);
        MyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = Paper.book().read(Prevalent.UserNameKey);
                date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());;
                id =2;
                type = (EditText)findViewById(R.id.game_type);
                description=(EditText)findViewById(R.id.activity_type);
                number_of_players = (EditText)findViewById(R.id.number_of_players);

                return;


            }
        });*/
    }
   /* public Activity(String name, String description, String image,String type,int number_of_players ,int id) {
        this.name = name;
        this.date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());;
        this.description = description;
        this.id =id;
        this.number_of_players=number_of_players;
        this.type = type;
    }*/

}
