package com.example.trainkoi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import static com.example.trainkoi.MapsActivity.mp;

public class Page3 extends AppCompatActivity {


    DatabaseReference data1;
     ArrayList<LatLng> list;

    Coordinates co[];         //storing every coordinates as node with left and right station
     String From_Station;
     String To_Station;

     Button arrivingButton, reachingButton, locButton;
    static int source_index,dest_index;

    void init_HASH_MAP()
    {
        mp.put("kamlapur",0);
        mp.put("gandaria",80);
        mp.put("pagla",300);
        mp.put("fatullah",420);
        mp.put("narayanganj",621);
    }
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //connecting with firebase/database named Coordinates
        data1=FirebaseDatabase.getInstance().getReference("KAMLAPUR-NARAYANGONJ");

        From_Station = getIntent().getStringExtra("from");
        To_Station = getIntent().getStringExtra("to");
        setContentView(R.layout.activity_page3);

        co=new Coordinates[1000];
        mp=new HashMap<>();
        init_HASH_MAP();
        list=new ArrayList<LatLng>() ;
        //this function read all the Coordinates from the firebase database
        FirebaseReader();

        System.out.println("From: "+From_Station+"  To: "+To_Station);

        //getting index of from station and destination station in arraylist
        source_index=mp.get(From_Station);
        dest_index= mp.get(To_Station);


        arrivingButton = findViewById(R.id.arrivingTimeButton);
        locButton = findViewById(R.id.locationButton);
        reachingButton = findViewById(R.id.reachingTimeButton);
        arrivingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Page3.this,MapsActivity.class);
                intent.putExtra("source",source_index);
                //intent.putExtra("destination",dest_index);
                intent.putExtra("mylist",list);
                intent.putExtra("flag",1);
                 startActivity(intent);
            }
        });
        locButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page3.this,MapsActivity.class);
                intent.putExtra("mylist",list);
                intent.putExtra("flag",2);
                startActivity(intent);
            }
        });
        reachingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page3.this,MapsActivity.class);
                intent.putExtra("source",source_index);
                intent.putExtra("destination",dest_index);

                intent.putExtra("mylist",list);
                intent.putExtra("flag",3);
                 startActivity(intent);
            }
        });
    }

    private void FirebaseReader()
    {

        //addListenerForSingleValueEvent used for directly reading firebase data without any change
        data1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                 int i=0;
                for(DataSnapshot coordinate : dataSnapshot.getChildren())
                {
                    String str=String.valueOf(coordinate.getValue());

                    String str1[] = str.split(",");
                    double x = Double.parseDouble(str1[0]);
                    double y = Double.parseDouble(str1[1]);
                    String left_up_station="";
                    String right_down_station="";
                    try {
                        left_up_station = str1[2];
                        right_down_station = str1[3];
                        //System.out.println(i+"-->"+x+" "+y+" "+" "+left_up_station+" "+right_down_station);
                    }catch (ArrayIndexOutOfBoundsException e)
                    {
                        System.out.println(e);
                    }
                    list.add(new LatLng(x,y));


                    co[i]=new Coordinates(x,y,left_up_station,right_down_station);
                    i++;
                    //FirebaseWriter(x,y);

                }
                //System.out.println("list size: "+list.size());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(),"Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
