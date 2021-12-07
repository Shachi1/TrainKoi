package com.example.trainkoi;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
     static GoogleMap mMap;

     static TextView txt1,txt2;

     static int flag;
    static  int source_index,dest_index;
    static double total_distance=0;
    Marker TrainMarker;
    static ArrayList<LatLng> list;
    static Map<String, Integer> mp;
    DatabaseReference TrainLocationUpdate;

    //long count=0;
    //Double[][] station_distances = new Double[10][10];    //sstoring distances among stations


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        //textview adding
        txt1= (TextView)findViewById(R.id.tex);  //time
        txt2= (TextView)findViewById(R.id.tex2);  //distance


       TrainLocationUpdate= FirebaseDatabase.getInstance()
                             .getReference("Train_Live_Location_Update")
                             .child("Dhaka-Narayanganj");

        flag=(int)getIntent().getIntExtra("flag",0);
        list = (ArrayList<LatLng>) getIntent().getSerializableExtra("mylist");
       if(flag==1)
       {
           source_index = (int) getIntent().getIntExtra("source", 0);
           System.out.println("mapsActivity--> source: " + source_index + "destination: " + dest_index );
       }
       else if(flag ==2){



        }
        else
       {
           source_index = (int) getIntent().getIntExtra("source", 0);
           dest_index = (int) getIntent().getIntExtra("destination", 0);

           System.out.println("mapsActivity--> source: " + source_index + "destination: " + dest_index );
       }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }



    }

    void TrainMarkerUpdate(double x, double y)
    {
        System.out.println("updated1");
        if(TrainMarker!=null)
        {
            //it removes the previous marker and set a new one
            TrainMarker.remove();
            TrainMarker=null;
        }
        TrainMarker=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(x,y))
                .title("Express")       //train name
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

    }
    void TrainMarkerUpdate(double x, double y,double total_dist,double velocity)
    {
        System.out.println("updated2");


        total_dist = Math.round(total_dist*100)/100.0d;     //rounding up km
        velocity = Math.round(velocity*100)/100.0d;         //rounding up m/s

        double total_dist_in_meter=total_dist*1000;         // meter

        double time=total_dist_in_meter/velocity;        //seconds



        GregorianCalendar cal = new GregorianCalendar(0,0,0,0,0, (int) time);
        Date dNow = cal.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat ft = new SimpleDateFormat("HH 'hours' mm 'minutes' ");

        System.out.println("Your time: " + ft.format(dNow)+"  "+total_dist+" kilo meter  "+velocity+" m/s");

        String TIME=ft.format(dNow);

        SetTextView(TIME,String.valueOf(total_dist),String.valueOf(velocity));
        if(TrainMarker!=null)
        {
            //it removes the previous marker and set a new one
            TrainMarker.remove();
            TrainMarker=null;
        }
        TrainMarker=mMap.addMarker(new MarkerOptions()
                .position(new LatLng(x,y))
                .title("Express")
                .snippet(String.valueOf(TIME))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

    }
    Result RouteMaker(int TrainPoint,ArrayList<LatLng>list){
        double x1,x2, y1, y2;

        ArrayList<LatLng> temp_list=new ArrayList<LatLng>() ;

        if(MapsActivity.flag==3) {
            for (int i = TrainPoint, j = i + 1; j < dest_index; i++, j++) {

                LatLng cord = list.get(i);
                LatLng cord1 = list.get(j);

                x1 = cord.latitude;
                y1 = cord.longitude;
                x2 = cord1.latitude;
                y2 = cord1.longitude;
                double dist = Haversine.distance(x1, y1, x2, y2);
                total_distance += dist;
                temp_list.add(new LatLng(x1, y1));

            }
        }
        else
        {
            for (int i = TrainPoint, j = i - 1; j >= source_index; i--, j--) {

                LatLng cord = list.get(i);
                LatLng cord1 = list.get(j);

                x1 = cord.latitude;
                y1 = cord.longitude;
                x2 = cord1.latitude;
                y2 = cord1.longitude;
                double dist = Haversine.distance(x1, y1, x2, y2);
                total_distance += dist;
                temp_list.add(new LatLng(x1, y1));

            }
        }
        System.out.println("list size: "+temp_list.size() +" source: "+source_index+"destination: "+dest_index);



        Result res=new Result();
        res.temp_list=temp_list;
        res.total_distance=total_distance;

        return res;

    }
    private void TrainLiveLocationReader()
    {
        TrainLocationUpdate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TrainLocationUpdate tlu =dataSnapshot.getValue(TrainLocationUpdate.class);
               if(tlu!=null) {
                   double latitude = tlu.getLatitude();
                   double longitude = tlu.getLongitude();
                   double velocity = tlu.getVelocity();         //velocity in m/s
                    Line.velocity=velocity;

                  // System.out.println(latitude + "," + longitude + "," + velocity+"m/s");

                   if(flag==1)MyAndroidThread.flag=1;
                   if(flag==2)MyAndroidThread.flag=2;
                   if(flag==3)MyAndroidThread.flag=3;

                   //start
                   MyAndroidThread.x = latitude;
                   MyAndroidThread.y = longitude;

                   MyAndroidThread myTask = new MyAndroidThread(MapsActivity.this);
                   Thread t1 = new Thread(myTask, "MyThread");
                   t1.start();
                   //end


               }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
/*
    private void TrainSingleLocationReader()
    {
        TrainLocationUpdate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TrainLocationUpdate tlu =dataSnapshot.getValue(TrainLocationUpdate.class);
                if(tlu!=null) {
                    double latitude = tlu.getLatitude();
                    double longitude = tlu.getLongitude();
                    double velocity = tlu.getVelocity();         //velocity in km/h
                    Line.velocity=velocity;

                    System.out.println("single reader-->"+latitude + "," + longitude + "," + velocity+"m/s");

                    if(flag==1)
                    {
                        //start
                        MyAndroidThread.x = latitude;
                        MyAndroidThread.y = longitude;
                        MyAndroidThread.flag=1;

                        MyAndroidThread myTask = new MyAndroidThread(MapsActivity.this);
                        Thread t1 = new Thread(myTask, "Distance Measure Thread");
                        t1.start();
                        //end
                    }
                    if(flag==3) {
                        //start
                        MyAndroidThread.x = latitude;
                        MyAndroidThread.y = longitude;
                        MyAndroidThread.flag=3;

                        MyAndroidThread myTask = new MyAndroidThread(MapsActivity.this);
                        Thread t1 = new Thread(myTask, "Distance Measure Thread");
                        t1.start();
                        //end
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
*/

    //add the marker for rarilway stations
    void RailwayStationLocation()
    {
        LatLng Kamlapur=new LatLng(23.7301531,90.4282789);
        LatLng Gandaria=new LatLng(23.7008024,90.4292834);//80
        LatLng Pagla=new LatLng(23.6657447,90.4599602);//300
        LatLng Fatullah=new LatLng(23.6503499,90.4744381);//420
        LatLng Narayangonj=new LatLng(23.6182024,90.5055219);//623


        mMap.addMarker(new MarkerOptions().position(Kamlapur).title("Kamlapur Station").snippet("Dhaka").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions().position(Gandaria).title("Gandaria Station").snippet("Dhaka").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions().position(Pagla).title("Pagla Station").snippet("Dhaka").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions().position(Fatullah).title("Fatullah Station").snippet("Dhaka").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.addMarker(new MarkerOptions().position(Narayangonj).title("Narayangonj Station").snippet("Dhaka").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Kamlapur,13));

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //add the marker for rarilway stations
        RailwayStationLocation();

        Line.cordinates =list;
        System.out.println("list size(mapsactivity)"+list.size());
        //ck==1 for black railway track drawing in the google map
        Line.DrawCurveLine(list,1);

        //listens for a change in train location
        TrainLiveLocationReader();

    }
    void SetTextView(String time,String distance,String velocity)
    {

        txt1.append(time);
        txt2.append(distance);
        txt2.append(" km");
       // txt3.append(velocity);
    }
}










/*
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick( LatLng latLng) {

                MyAndroidThread.x=latLng.latitude;
                MyAndroidThread.y=latLng.longitude;

                MyAndroidThread myTask = new MyAndroidThread(MapsActivity.this);
                Thread t1 = new Thread(myTask, "Distance Measure Thread");
                t1.start();
            }
        });
*/
