package com.example.mehjabeen.train;

import android.content.Intent;
import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;


class Line extends MapsActivity{
   static ArrayList<LatLng> cordinates;
   static Polyline line;

   static double velocity;

   void calculate_distance(double x0,double y0)
   {


       double Min_dist=1000000000;
       int index=0;


       for(int i=0;i<cordinates.size();i++)
       {
           LatLng cord=cordinates.get(i);

           double x=cord.latitude;
           double y=cord.longitude;

           //(x0,y0) is the real current location of the train
           double dist=Haversine.distance(x,y,x0,y0);
           if(dist<Min_dist)
           {
               Min_dist=dist;
               index=i;
           }
       }
       System.out.println("FLAG="+ MapsActivity.flag);
       MapsActivity MAP = new MapsActivity();
       /*ck==2 for blue line on the map which is for distance measuring
        * the index coordinates refer the location of the train  */
       if(MapsActivity.flag==1 || MapsActivity.flag==3) {

           Result RS = MAP.RouteMaker(index, cordinates);
           double total_distance = RS.get_TotalDistance();
           ArrayList<LatLng> RouteList = RS.get_RouteList();

           System.out.println("Route list size: " + RouteList.size());
           DrawCurveLine(RouteList, 2);
       }

       System.out.println("index:  "+index);


       //below codes are used for setting train marker at the assumed index of coordinates
       LatLng cord1=cordinates.get(index);
       double x=cord1.latitude;
       double y=cord1.longitude;
       CameraUpdate location = CameraUpdateFactory.newLatLngZoom(new LatLng(x,y), 15);
       mMap.animateCamera(location);
       if(MapsActivity.flag==2)MAP.TrainMarkerUpdate(x,y);        //it will only update the train Marker
       else MAP.TrainMarkerUpdate(x,y,total_distance,velocity);    //it will update the marker with distance,velocity  and marker


   }

    static void DrawCurveLine(ArrayList<LatLng> list, int ck)
   {

       // LatLng lt=new LatLng(x1,y1);
       // LatLng lt1=new LatLng(x2,y2);
       PolylineOptions pop=new PolylineOptions();
       //System.out.println("map: "+mMap);
       //
       if(ck==1){
           pop.addAll(list).width(8).color(Color.BLACK).geodesic(true);

           mMap.addPolyline(pop);

           LatLngBounds.Builder builder = new LatLngBounds.Builder();
           for (LatLng latLng : list) {
               builder.include(latLng);
           }

           final LatLngBounds bounds = builder.build();

           //BOUND_PADDING is an int to specify padding of bound.. try 100.
           CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,100);
           mMap.animateCamera(cu);
       }
       else {

           pop.addAll(list).width(20).color(Color.BLUE).geodesic(true);
           // mMap.clear();
           line= mMap.addPolyline(pop);

           LatLngBounds.Builder builder = new LatLngBounds.Builder();
          // System.out.println(list.size());

           for (LatLng latLng : list) {
               builder.include(latLng);
           }

            try{
                LatLngBounds bounds = builder.build();
                //BOUND_PADDING is an int to specify padding of bound.. try 100.
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,100);
                mMap.animateCamera(cu);
            }catch (IllegalStateException e)
            {
                System.out.println(e);
            }


       }
   }
}
