package com.example.trung.smartair;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.EventLogTags;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.numetriclabz.numandroidcharts.AreaStackChart;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTempValue,mIndoorTempValue,mOutdoorTempValue,mIndex,mTempPercentUp,mTempPercentDown,mPercentUp,mPercentDown;
    private TextView mInPercentUp,mInPercentDown,mPercentInUp,mPercentInDown;
    private TextView mOutPercentUp,mOutPercentDown,mPercentOutUp,mPercentOutDown;
    private ImageButton mUp,mDown;
    private ImageView mTempDown,mTempUp,mInUp,mInDown,mOutUp,mOutDown;
    private RecyclerView mListValue;
    private DatabaseReference mDatabase,getIndex,getIndoor,getOutdoor,getRemote;
    String tempUp,tempDown,x;
    int iTemp=0,iIn=0,iOut=0,hTemp=0,hIn=0,hOut=0,jIn=0,kIn=12,jOut=0,kOut=12;
    float result;
    ArrayList<String> list;
    String listTemp[] = new String[10000];
    String listIn[] = new String[10000];
    String listOut[] = new String[10000];
    int inChange[] = new int[10000];
    int outChange[] = new int[10000];
    BarChart barChartIn,barChartOut;
    AreaStackChart areaStackChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference().getRef();
        mTempValue = (TextView) findViewById(R.id.temp_value);
        mIndoorTempValue = (TextView) findViewById(R.id.indoor_temp_value);
        mOutdoorTempValue = (TextView) findViewById(R.id.outdoor_temp_value);
        mIndex = (TextView) findViewById(R.id.index_value);
        mDown = (ImageButton) findViewById(R.id.but_down);
        mUp = (ImageButton) findViewById(R.id.but_up);
        barChartIn = (BarChart) findViewById(R.id.InChart);
        barChartOut= (BarChart) findViewById(R.id.OutChart);
        areaStackChart = (AreaStackChart) findViewById(R.id.areachart);
      ////////////////---------air condition----------------

        mTempPercentUp = (TextView) findViewById(R.id.temp_percent_up);
        mTempPercentDown = (TextView) findViewById(R.id.temp_percent_down);
        mPercentDown = (TextView) findViewById(R.id.percent_down);
        mPercentUp = (TextView) findViewById(R.id.percent_up);
        mTempDown = (ImageView) findViewById(R.id.imageTempDown);
        mTempUp = (ImageView) findViewById(R.id.imageTempUp);

        /////////////-----------indoor-------------------

        mInPercentUp = (TextView) findViewById(R.id.in_percent_up);
        mInPercentDown = (TextView) findViewById(R.id.in_percent_down);
        mPercentInUp = (TextView) findViewById(R.id.syl_in_up);
        mPercentInDown = (TextView) findViewById(R.id.syl_in_down);
        mInDown = (ImageView) findViewById(R.id.imageInDown);
        mInUp = (ImageView) findViewById(R.id.imageInUp);


        /////////------------outdoor---------------------------

        mOutPercentUp = (TextView) findViewById(R.id.out_percent_up);
        mOutPercentDown = (TextView) findViewById(R.id.out_percent_down);
        mPercentOutUp = (TextView) findViewById(R.id.syl_out_up);
        mPercentOutDown = (TextView) findViewById(R.id.syl_out_down);
        mOutDown = (ImageView) findViewById(R.id.imageOutDown);
        mOutUp = (ImageView) findViewById(R.id.imageOutUp);

        //mTempDown.setImageDrawable(null);
        mTempDown.setVisibility(View.INVISIBLE);
        mTempUp.setVisibility(View.INVISIBLE);
        mInDown.setVisibility(View.INVISIBLE);
        mInUp.setVisibility(View.INVISIBLE);
        mOutDown.setVisibility(View.INVISIBLE);
        mOutUp.setVisibility(View.INVISIBLE);
        //-------------------------------------------------------
        barChartIn.setNoDataText("");
        barChartOut.setNoDataText("");


        getIndex = FirebaseDatabase.getInstance().getReference().child("index");
        getIndoor = FirebaseDatabase.getInstance().getReference().child("indoor");
        getOutdoor = FirebaseDatabase.getInstance().getReference().child("outdoor");
        getRemote = FirebaseDatabase.getInstance().getReference().child("remote");

        getIndex.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String index = dataSnapshot.getValue().toString();
                    mIndex.setText(index);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getIndoor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String index = dataSnapshot.getValue().toString();
                    listIn[iIn]=index;
                    inChange[iIn] = Integer.parseInt(index);
                    InChar();


                    mIndoorTempValue.setText(index);

                    if(iIn>=1){
                        percentIn();
                       // InChar();

                    }
                    iIn++;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getOutdoor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String index = dataSnapshot.getValue().toString();
                listOut[iOut]=index;
                mOutdoorTempValue.setText(index);
                outChange[iOut]= Integer.parseInt(index);
                OutChar();
                if(iOut>=1){
                    percentOut(listOut);
                   // OutChar();
                }
                iOut++;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        getRemote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String index = dataSnapshot.getValue().toString();
                listTemp[iTemp] = index;


                tempUp = index;
                tempDown = index;
                mTempValue.setText(index);
                if(iTemp>=1){
                    percentTemp(listTemp);
                }
                iTemp++;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cong(tempUp);
            }
        });

        mDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tru(tempDown);
            }
        });
       // example();
       // example1();
//        InChar();
//        OutChar();

        //-----------------------------------------------------

        //-----------------------------------------------------


    }

    private void cong(String index){

        int foo = Integer.parseInt(index);
        foo  = foo+1;
        String str = ""+foo;
        mTempValue.setText(str);
        tempUp = str;
        tempDown = str;

    }

    private void tru(String index){
        int foo = Integer.parseInt(index);
        foo  = foo-1;
        String str = ""+foo;
        mTempValue.setText(str);
        tempDown = str;
        tempUp = str;
    }

    private void percentTemp(String arr[]){
        float foo1 = Float.parseFloat(arr[hTemp]);
        float foo2 = Float.parseFloat(arr[hTemp+1]);
        if(foo1 < foo2){
            result = ((foo2 - foo1)/foo1)*100;
            result = Math.round(result*100)/100;
            String str = ""+result;
            mTempPercentDown.setText("");
            mPercentDown.setText("");
            mTempDown.setVisibility(View.INVISIBLE);
            mTempUp.setVisibility(View.VISIBLE);
            mTempPercentUp.setText(str);
            mPercentUp.setText("%");
        }
        else if (foo1 > foo2){
            result = ((foo1-foo2)/foo1)*100;
            result = Math.round(result*100)/100;
            String str = ""+result;
            mTempPercentUp.setText("");
            mPercentUp.setText("");
            mTempUp.setVisibility(View.INVISIBLE);
            mTempDown.setVisibility(View.VISIBLE);
            mTempPercentDown.setText(str);
            mPercentDown.setText("%");

        }
        else { result = 0; }
        hTemp++;
    }

    private void percentIn(){

        float foo1 = Float.parseFloat(listIn[hIn]);
        float foo2 = Float.parseFloat(listIn[hIn+1]);
        if(foo1 < foo2){
            result = ((foo2 - foo1)/foo1)*100;
            result = Math.round(result*100)/100;
            String str = ""+result;
            mInPercentDown.setText("");
            mPercentInDown.setText("");
            mInPercentUp.setText(str);
            mPercentInUp.setText("%");
            mInDown.setVisibility(View.INVISIBLE);
            mInUp.setVisibility(View.VISIBLE);
        }
        else if (foo1 > foo2){
            result = ((foo1-foo2)/foo1)*100;
            result = Math.round(result*100)/100;
            String str = ""+result;
            mInPercentUp.setText("");
            mPercentInUp.setText("");
            mInPercentDown.setText(str);
            mPercentInDown.setText("%");
            mInDown.setVisibility(View.VISIBLE);
            mInUp.setVisibility(View.INVISIBLE);
        }
        else { result = 0; }


        hIn++;
    }

    private void percentOut(String arr[]){

        float foo1 = Float.parseFloat(arr[hOut]);
        float foo2 = Float.parseFloat(arr[hOut+1]);
        if(foo1 < foo2){
            result = ((foo2 - foo1)/foo1)*100;
            result = Math.round(result*100)/100;
            String str = ""+result;
            mOutPercentDown.setText("");
            mPercentOutDown.setText("");
            mOutPercentUp.setText(str);
            mPercentOutUp.setText("%");
            mOutDown.setVisibility(View.INVISIBLE);
            mOutUp.setVisibility(View.VISIBLE);
        }
        else if (foo1 > foo2){
            result = ((foo1-foo2)/foo1)*100;
            result = Math.round(result*100)/100;
            String str = ""+result;
            mOutPercentDown.setText(str);
            mPercentOutDown.setText("%");
            mOutPercentUp.setText("");
            mPercentOutUp.setText("");
            mOutDown.setVisibility(View.VISIBLE);
            mOutUp.setVisibility(View.INVISIBLE);
        }
        else { result = 0; }


        hOut++;
    }

    private void OutChar(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        if(iOut == kOut){
            jOut++;
            kOut++;
        }
        for(int i=jOut;i<=iOut;i++){
            barEntries.add(new BarEntry(outChange[i],i-jOut));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries,"");
        barDataSet.setColor(Color.LTGRAY);
        ArrayList<String> theDate = new ArrayList<>();
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");

        BarData theData = new BarData(theDate,barDataSet);
        theData.setDrawValues(false);
        XAxis xAxis = barChartOut.getXAxis();
        xAxis.setEnabled(false);
        barChartOut.getAxisLeft().setEnabled(false);
        barChartOut.getAxisRight().setEnabled(false);
        barChartOut.setData(theData);
        barChartOut.setDescription("");
        barChartOut.getLegend().setEnabled(false);

    }

    private void InChar(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        if(iIn == kIn){
            jIn++;
            kIn++;
        }
        for(int i=jIn;i<=iIn;i++){
            barEntries.add(new BarEntry(inChange[i],i-jIn));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries,"");
        ArrayList<String> theDate = new ArrayList<>();
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");

        BarData theData = new BarData(theDate,barDataSet);
        theData.setDrawValues(false);
        XAxis xAxis = barChartIn.getXAxis();
        xAxis.setEnabled(false);
        barChartIn.getAxisLeft().setEnabled(false);
        barChartIn.getAxisRight().setEnabled(false);
        barChartIn.setData(theData);
        barChartIn.setDescription("");
        barChartIn.getLegend().setEnabled(false);




    }

    private void example(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(4,0));
        barEntries.add(new BarEntry(1,1));
        barEntries.add(new BarEntry(6,2));
        barEntries.add(new BarEntry(7,3));
        barEntries.add(new BarEntry(8,4));
        barEntries.add(new BarEntry(4,5));
        barEntries.add(new BarEntry(1,6));
        barEntries.add(new BarEntry(6,7));
        barEntries.add(new BarEntry(7,8));
        barEntries.add(new BarEntry(8,9));
        barEntries.add(new BarEntry(6,10));
        barEntries.add(new BarEntry(7,11));
        barEntries.add(new BarEntry(8,12));
        BarDataSet barDataSet = new BarDataSet(barEntries,"");

        ArrayList<String> theDate = new ArrayList<>();
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");

        BarData theData = new BarData(theDate,barDataSet);
        theData.setDrawValues(false);
        XAxis xAxis = barChartOut.getXAxis();
        xAxis.setEnabled(false);
        barChartOut.getAxisLeft().setEnabled(false);
        barChartOut.getAxisRight().setEnabled(false);
        barChartOut.setData(theData);
        barChartOut.setDescription("");
        barChartOut.getLegend().setEnabled(false);
    }

    private void example1(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(4,0));
        barEntries.add(new BarEntry(1,1));
        barEntries.add(new BarEntry(6,2));
        barEntries.add(new BarEntry(7,3));
        barEntries.add(new BarEntry(8,4));
        barEntries.add(new BarEntry(4,5));
        barEntries.add(new BarEntry(1,6));
        barEntries.add(new BarEntry(6,7));
        barEntries.add(new BarEntry(7,8));
        barEntries.add(new BarEntry(8,9));
        barEntries.add(new BarEntry(6,10));
        barEntries.add(new BarEntry(7,11));
        barEntries.add(new BarEntry(8,12));
        BarDataSet barDataSet = new BarDataSet(barEntries,"");
        barDataSet.setColor(Color.LTGRAY);
        ArrayList<String> theDate = new ArrayList<>();
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");
        theDate.add("");

        BarData theData = new BarData(theDate,barDataSet);
        theData.setDrawValues(false);
        XAxis xAxis = barChartIn.getXAxis();
        xAxis.setEnabled(false);
        barChartIn.getAxisLeft().setEnabled(false);
        barChartIn.getAxisRight().setEnabled(false);
        barChartIn.setData(theData);
        barChartIn.setDescription("");
        barChartIn.getLegend().setEnabled(false);
    }



}

