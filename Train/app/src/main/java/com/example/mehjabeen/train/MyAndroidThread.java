package com.example.mehjabeen.train;

import android.app.Activity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

class MyAndroidThread extends MapsActivity implements Runnable
{
    Activity activity;
    static double x,y; //this x & y keeps the value of onSetLongClickListener latitude and longitude
    static int flag;
    public MyAndroidThread(Activity activity)
    {
        this.activity = activity;
    }
    @Override
    public void run()
    {

        //perform heavy task here and finally update the UI with result this way -
        activity.runOnUiThread(new Runnable()
        {

            @Override
            public void run()
            {
                        Line ob=new Line();
                        MapsActivity.total_distance =0;

                        ob.calculate_distance(x, y);

                        String snippet = String.valueOf(x+","+ y) ;

                        /*mMap.addMarker(new MarkerOptions()
                                 .position(new LatLng(x,y))
                                .title("Dropped Pin")
                                .snippet(snippet));*/


            }


        });
    }
}
