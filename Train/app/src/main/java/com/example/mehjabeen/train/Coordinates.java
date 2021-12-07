package com.example.mehjabeen.train;

public class Coordinates {
    double x;
    double y;
    String left_up_station;
    String right_down_station;

    String str;
    public Coordinates(double x, double y, String left_up_station, String right_down_station)
    {
        this.x=x;
        this.y=y;
        this.left_up_station=left_up_station;
        this.right_down_station=right_down_station;

    }


    @Override
    public String toString() {
        return x+","+y+","+left_up_station+","+right_down_station;
    }
}
