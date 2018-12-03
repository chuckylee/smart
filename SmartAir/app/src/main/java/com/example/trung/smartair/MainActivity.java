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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXIndexComparator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.numetriclabz.numandroidcharts.AreaChart;
import com.numetriclabz.numandroidcharts.AreaStackChart;
import com.numetriclabz.numandroidcharts.ChartData;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTempValue,mIndoorTempValue,mOutdoorTempValue,mIndex,mTempPercentUp,mTempPercentDown,mPercentUp,mPercentDown;
    private TextView mInPercentUp,mInPercentDown,mPercentInUp,mPercentInDown;
    private TextView mOutPercentUp,mOutPercentDown,mPercentOutUp,mPercentOutDown;
    private ImageButton mUp,mDown;
    private ImageView mUpBorder,mDownBorder;
    private ImageView mTempDown,mTempUp,mInUp,mInDown,mOutUp,mOutDown;
    private RecyclerView mListValue;
    private DatabaseReference mDatabase,getIndex,getIndoor,getOutdoor,getRemote;
    String tempUp,tempDown,x;
    int iTemp=0,iIn=0,iOut=0,hTemp=0,hIn=0,hOut=0,jIn=0,kIn=12,jOut=0,kOut=12,iIndex=0,hInChange=1,hOutChange=1,ibar=17,jbar=0,t=0;
    float result;
    ArrayList<String> list;
    String listTemp[] = new String[10000];
    String listIn[] = new String[10000];
    String listOut[] = new String[10000];
    String str[] = new String[10000];
    int inChange[] = new int[10000];
    int outChange[] = new int[10000];
    int inChange1[] = new int[10000];
    int outChange1[] = new int[10000];
    int indexChange[] = new int[10000];
    BarChart barChartIn,barChartOut,barChart;


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

        //------------------------chart--------------------------
        barChartIn = (BarChart) findViewById(R.id.InChart);
        barChartOut= (BarChart) findViewById(R.id.OutChart);
        barChart = (BarChart) findViewById(R.id.barChart);
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
        //-----------------------------------------------------------
        mTempDown.setVisibility(View.INVISIBLE);
        mTempUp.setVisibility(View.INVISIBLE);
        mInDown.setVisibility(View.INVISIBLE);
        mInUp.setVisibility(View.INVISIBLE);
        mOutDown.setVisibility(View.INVISIBLE);
        mOutUp.setVisibility(View.INVISIBLE);

        //-------------------------------------------------------
        barChartIn.setNoDataText("");
        barChartOut.setNoDataText("");
        barChart.setScaleEnabled(false);
        barChartIn.setScaleEnabled(false);
        barChartOut.setScaleEnabled(false);
        barChart.setNoDataText("");
        //--------------------------------------------------
        getIndex = FirebaseDatabase.getInstance().getReference().child("index");
        getIndoor = FirebaseDatabase.getInstance().getReference().child("indoor");
        getOutdoor = FirebaseDatabase.getInstance().getReference().child("outdoor");
        getRemote = FirebaseDatabase.getInstance().getReference().child("remote");
        //---------------------------------------------------------
        getIndoor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    String index = dataSnapshot.getValue().toString();
                    listIn[iIn]=index;
                    inChange[iIn] = Integer.parseInt(index);
                    inChange1[hInChange-1] = Integer.parseInt(index);
                    mIndoorTempValue.setText(index);
                    if(iIn>=1){
                        percentIn();
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
                outChange1[hOutChange-1]= Integer.parseInt(index);
                if(iOut>=1){
                    percentOut(listOut);
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
        getIndex.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String index = dataSnapshot.getValue().toString();
                indexChange[iIndex] = Integer.parseInt(index);
                mIndex.setText(index);
                OutChar();
                InChar();
                finalbar();

                barChart.notifyDataSetChanged(); // let the chart know it's data changed
                barChartIn.notifyDataSetChanged();
                barChartOut.notifyDataSetChanged();
                barChart.invalidate(); // refresh
                barChartOut.invalidate();
                barChartIn.invalidate();


                iIndex++;
                inChange1[hInChange] = inChange[iIn-1];
                outChange1[hOutChange] = outChange[iOut-1];
                hInChange++;
                hOutChange++;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //----------------------------------------------------------------
        mUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(tempUp);
            }
        });
        mDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sub(tempDown);
            }
        });
        //-------------------------------------------------------------
        example();
        example1();
        bar();

    }

    private void add(String index){

        int foo = Integer.parseInt(index);
        foo  = foo+1;
        String str = ""+foo;
        mTempValue.setText(str);
        tempUp = str;
        tempDown = str;


    }
    private void sub(String index){
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
        if(iIndex == kOut){
            jOut++;
            kOut++;
        }
        for(int i=jOut;i<=iIndex;i++){
            barEntries.add(new BarEntry(outChange1[i],i-jOut));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries,"");
        barDataSet.setColor(Color.CYAN);
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
        if(iIndex == kIn){
            jIn++;
            kIn++;
        }
        for(int i=jIn;i<=iIndex;i++){
            barEntries.add(new BarEntry(inChange1[i],i-jIn));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries,"");
        barDataSet.setColor(Color.BLUE);
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
        barDataSet.setColors(new int[]{Color.CYAN});
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
        ArrayList<BarEntry> barEntries1 = new ArrayList<>();
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

        barDataSet.setColor(Color.BLUE);

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
    private void bar(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(4,0));
        barEntries.add(new BarEntry(6,2));
        barEntries.add(new BarEntry(1,1));

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
        barEntries.add(new BarEntry(1,13));
        barEntries.add(new BarEntry(6,14));
        barEntries.add(new BarEntry(7,15));
        barEntries.add(new BarEntry(8,16));
        barEntries.add(new BarEntry(4,17));
        barEntries.add(new BarEntry(1,18));
        barEntries.add(new BarEntry(7,19));
        barEntries.add(new BarEntry(8,20));
        barEntries.add(new BarEntry(4,21));
        barEntries.add(new BarEntry(1,22));
        barEntries.add(new BarEntry(8,23));
        barEntries.add(new BarEntry(4,24));
        barEntries.add(new BarEntry(1,25));
        barEntries.add(new BarEntry(1,26));
        barEntries.add(new BarEntry(4,27));
        barEntries.add(new BarEntry(1,28));
        barEntries.add(new BarEntry(4,29));
        barEntries.add(new BarEntry(1,30));
        barEntries.add(new BarEntry(4,31));
        barEntries.add(new BarEntry(1,32));
        barEntries.add(new BarEntry(4,33));
        BarDataSet barDataSet = new BarDataSet(barEntries,"Indoor Temperature, Outdoor Temperature");
        barDataSet.setColors(new int[]{Color.BLUE,Color.CYAN});
        ArrayList<String> theDate = new ArrayList<>();
        theDate.add("1");
        theDate.add("2");
        theDate.add("3");
        theDate.add("4");
        theDate.add("5");
        theDate.add("6");
        theDate.add("7");
        theDate.add("8");
        theDate.add("9");
        theDate.add("10");
        theDate.add("11");
        theDate.add("12");
        theDate.add("13");
        theDate.add("14");
        theDate.add("15");
        theDate.add("16");
        theDate.add("17");
        theDate.add("18");
        theDate.add("19");
        theDate.add("20");
        theDate.add("21");
        theDate.add("22");
        theDate.add("23");
        theDate.add("24");
        theDate.add("25");
        theDate.add("26");
        theDate.add("27");
        theDate.add("28");
        theDate.add("29");
        theDate.add("30");
        theDate.add("31");
        theDate.add("32");
        theDate.add("33");
        theDate.add("34");

        BarData theData = new BarData(theDate,barDataSet);
        theData.setDrawValues(false);
        barChart.setData(theData);
        barChart.setDescription("");

    }
    private void finalbar(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        if(iIndex == ibar){
            ibar++;
            jbar++;
        }
//        for(int i=jbar;i<=iIndex;i++){
//            if((i-jbar)%2==0){
//            barEntries.add(new BarEntry(inChange1[i],i-jbar));}
//            else {
//                barEntries.add(new BarEntry(outChange1[i-1],i-jbar));}
//        }

        for(int i=jbar;i<=iIndex;i++){
            barEntries.add(new BarEntry(inChange1[i],(i-jbar)*2));
//        }
//
//        for(int i=jbar;i<=iIndex;i++){
            barEntries.add(new BarEntry(outChange1[i],(i-jbar)*2+1));
        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Indoor Temperature");
        barDataSet.setColors(new int[]{Color.BLUE,Color.CYAN});

        ArrayList<String> theDate = new ArrayList<>();
        theDate.add("1");
        theDate.add("");
        theDate.add("2");
        theDate.add("");
        theDate.add("3");
        theDate.add("");
        theDate.add("4");
        theDate.add("");
        theDate.add("5");
        theDate.add("");
        theDate.add("6");
        theDate.add("");
        theDate.add("7");
        theDate.add("");
        theDate.add("8");
        theDate.add("");
        theDate.add("9");
        theDate.add("");
        theDate.add("10");
        theDate.add("");
        theDate.add("11");
        theDate.add("");
        theDate.add("12");
        theDate.add("");
        theDate.add("13");
        theDate.add("");
        theDate.add("14");
        theDate.add("");
        theDate.add("15");
        theDate.add("");
        theDate.add("16");
        theDate.add("");
        theDate.add("17");
        theDate.add("");

//        for (int i = jbar;i<=10000;i++){
//            str[i-jbar] = ""+t;
//            t++;
//        }
//
//        theDate.add(str[0]);
//        theDate.add("");
//        theDate.add(str[1]);
//        theDate.add("");
//        theDate.add(str[2]);
//        theDate.add("");
//        theDate.add(str[3]);
//        theDate.add("");
//        theDate.add(str[4]);
//        theDate.add("");
//        theDate.add(str[5]);
//        theDate.add("");
//        theDate.add(str[6]);
//        theDate.add("");
//        theDate.add(str[7]);
//        theDate.add("");
//        theDate.add(str[8]);
//        theDate.add("");
//        theDate.add(str[9]);
//        theDate.add("");
//        theDate.add(str[10]);
//        theDate.add("");
//        theDate.add(str[11]);
//        theDate.add("");
//        theDate.add(str[12]);
//        theDate.add("");
//        theDate.add(str[13]);
//        theDate.add("");
//        theDate.add(str[14]);
//        theDate.add("");
//        theDate.add(str[15]);
//        theDate.add("");
//        theDate.add(str[16]);
//        theDate.add("");


        BarData theData = new BarData(theDate,barDataSet);

        theData.setDrawValues(false);

        barChart.setData(theData);

        barChart.setDescription("");


    }
}

