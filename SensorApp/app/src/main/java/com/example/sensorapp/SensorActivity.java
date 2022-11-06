package com.example.sensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends AppCompatActivity {

    private String KEY_SUBTITLE_VISABILITY = "SensorApp.subtitle_visable";
    private boolean subtitleVisable = false;

    private SensorManager sensorManager;
    private List<Sensor> sensorList;

    private RecyclerView recyclerView;
    private SensorActivity.SensorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            subtitleVisable = savedInstanceState.getBoolean(KEY_SUBTITLE_VISABILITY);
        }
        setContentView(R.layout.sensor_activity);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        //recycler
        recyclerView = findViewById(R.id.sensor_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(adapter == null){
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        }
        else{
            adapter.notifyDataSetChanged();
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SUBTITLE_VISABILITY, subtitleVisable);
    }

    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Sensor sensor;
        private TextView nameTextView;
        private ImageView iconImage;
        public SensorHolder(LayoutInflater inflater, ViewGroup parent ){
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));

            nameTextView= itemView.findViewById(R.id.sensor_item_name);
            iconImage = itemView.findViewById(R.id.sensor_icon_image);
        }

        public void bind(Sensor sensor){
            this.sensor = sensor;
            nameTextView.setText(sensor.getName());
            iconImage.setImageResource(R.drawable.ic_sensor);

            if (Sensor.TYPE_LIGHT == sensor.getType() || Sensor.TYPE_GRAVITY == sensor.getType()) {
                nameTextView.setPaintFlags(nameTextView.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG | Paint.UNDERLINE_TEXT_FLAG);
                itemView.setOnClickListener(this);
            }
            else if (sensor.getName().toUpperCase().contains("MAGNET") == true){
                itemView.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent;
            if(sensor.getName().toUpperCase().contains("MAGNET") == true){
                intent = new Intent(SensorActivity.this, LocationActivity.class);
            }
            else {
                intent = new Intent(SensorActivity.this, SensorsDetailsActivity.class);
            }

            //intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }

    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorActivity.SensorHolder>{
        private List<Sensor> sensors;
        private SensorAdapter(List<Sensor> sensors){
            this.sensors = sensors;
        }
        @NonNull
        @Override
        public SensorActivity.SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ){
            LayoutInflater layoutInflater = LayoutInflater.from(SensorActivity.this);
            return new SensorActivity.SensorHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(@NonNull SensorActivity.SensorHolder holder, int position){
            Sensor sensor = sensors.get(position);
            holder.bind(sensor);
        }
        @Override
        public int getItemCount(){
            return sensors.size();
        }
    }

    ////////////////////////MENU do////////////////////////////

    //create
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sensor_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (subtitleVisable){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
        return true;
    }

    public void updateSubtitle(){

        int sensorsCount = adapter.getItemCount();
        String subtitle = getString(R.string.sensors_count, sensorsCount);
        if(!subtitleVisable){
            subtitle=null;
        }
        AppCompatActivity appCompatActivity = this;
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }
    //reakcje na opcje
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.show_subtitle:
                subtitleVisable=!subtitleVisable;
                this.invalidateOptionsMenu(); //rekonstukcja menu
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /////////////


}