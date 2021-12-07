package com.example.mehjabeen.train;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

class Result {
   ArrayList<LatLng> temp_list;
   double total_distance;

   double get_TotalDistance()
   {
       return total_distance;
   }
   ArrayList<LatLng> get_RouteList()
   {
       return temp_list;
   }
}
