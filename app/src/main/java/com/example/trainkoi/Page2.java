package com.example.trainkoi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class Page2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button nextButton;
    String[] trainNames, fromStationNames, toStationNames;
    private AutoCompleteTextView selectTrain, selectFromStation, selectToStation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);

        trainNames=getResources().getStringArray(R.array.Train_names);
        fromStationNames=getResources().getStringArray(R.array.Station_names);
        toStationNames=getResources().getStringArray(R.array.Station_names);

        selectTrain = (AutoCompleteTextView) findViewById(R.id.select_train);
        selectFromStation = (AutoCompleteTextView) findViewById(R.id.fromId);
        selectToStation = (AutoCompleteTextView) findViewById(R.id.toId);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,trainNames);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fromStationNames);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,toStationNames);

        selectTrain.setThreshold(1);
        selectTrain.setAdapter(adapter);
        selectTrain.setTextColor(Color.WHITE);

        selectTrain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedTrain = selectTrain.getText().toString();
            }
        });

        selectFromStation.setThreshold(1);
        selectFromStation.setAdapter(adapter2);
        selectFromStation.setTextColor(Color.WHITE);

        selectFromStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {



            }
        });

        selectToStation.setThreshold(1);
        selectToStation.setAdapter(adapter3);
        selectToStation.setTextColor(Color.WHITE);

        selectToStation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                }
        });
        nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromStation = selectFromStation.getText().toString().toLowerCase();
                String toStation = selectToStation.getText().toString().toLowerCase();

                Intent intent = new Intent(Page2.this,Page3.class);
                intent.putExtra("from",fromStation );
                intent.putExtra("to",toStation );
                startActivity(intent);
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
