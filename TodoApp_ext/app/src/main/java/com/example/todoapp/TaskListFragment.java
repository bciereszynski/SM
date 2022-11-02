package com.example.todoapp;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.nio.BufferUnderflowException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TaskListFragment extends Fragment {
    public static final String KEY_EXTRA_TASK_ID = "pl.edu.pb.wi.ToDo.taskId";
    private RecyclerView recyclerView;
    private TaskAdapter adapter;

    private static final String KEY_SUBTITLE_VISABILITY = "subtitleVisable";

    private boolean subtitleVisable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        recyclerView = view.findViewById(R.id.task_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
    private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Task task;
        private TextView nameTextView;
        private TextView dateTextView;
        private ImageView iconImage;
        private CheckBox checkBox;
        public TaskHolder(LayoutInflater inflater, ViewGroup parent ){
            super(inflater.inflate(R.layout.list_item_task, parent, false));
            itemView.setOnClickListener(this);

            nameTextView= itemView.findViewById(R.id.task_item_name);
            dateTextView = itemView.findViewById(R.id.task_item_date);
            iconImage = itemView.findViewById(R.id.task_icon_image);
            checkBox = itemView.findViewById(R.id.task_done_checkBox);
        }
        public void cross(Task task){
            if(task.isDone()){
                nameTextView.setPaintFlags(nameTextView.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else{
                nameTextView.setPaintFlags(nameTextView.getPaintFlags() &~ Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }

        public void bind(Task task){
            this.task = task;
            nameTextView.setText(task.getName());
            DateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy");
            dateTextView.setText(dateFormat.format(task.getDate()));
            if(task.getCategory().equals(Category.HOME)){
                iconImage.setImageResource(R.drawable.ic_home);
            }
            else{
                iconImage.setImageResource(R.drawable.ic_college);
            }
            checkBox.setChecked(task.isDone());

            cross(task);

        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
            startActivity(intent);
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }
    }

    private class TaskAdapter extends RecyclerView.Adapter<TaskHolder>{
        private List<Task> tasks;
        private TaskAdapter(List<Task> tasks){
            this.tasks = tasks;
        }
        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TaskHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position){
            Task task = tasks.get(position);
            holder.bind(task);
            CheckBox checkBox = holder.getCheckBox();
            checkBox.setChecked(tasks.get(position).isDone());
            checkBox.setOnCheckedChangeListener( (compoundButton, isChcecked) ->{
                        tasks.get(holder.getBindingAdapterPosition()).setDone(isChcecked);
                        holder.cross(task);
                        updateSubtitle();
                    });
        }
        @Override
        public int getItemCount(){
            return tasks.size();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateView();
    }
    private void updateView(){
        TaskStorage taskStorage= TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();

        if(adapter == null){
            adapter= new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        }
        else{
            adapter.notifyDataSetChanged();
        }
        updateSubtitle();
    }

    public void updateSubtitle(){
        TaskStorage taskStorage= TaskStorage.getInstance();
        List<Task> tasks = taskStorage.getTasks();
        int todoTasksCount = 0;
        for(Task task : tasks){
            if(!task.isDone()){
                todoTasksCount++;
            }
        }
        String subtitle = getString(R.string.subtitle_format, todoTasksCount);
        if(!subtitleVisable){
            subtitle=null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }


    ////////////////////////MENU do////////////////////////////
    //eneable onCreateOptionsMenu
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        /////
        if(savedInstanceState != null){
            subtitleVisable = savedInstanceState.getBoolean(KEY_SUBTITLE_VISABILITY);
        }
    }
    //create
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (subtitleVisable){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }
    //reakcje na opcje
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.new_task:
                Task task = new Task();
                TaskStorage.getInstance().addTask(task);
                Intent intent= new Intent(getActivity(), MainActivity.class);
                intent.putExtra(TaskListFragment.KEY_EXTRA_TASK_ID, task.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                subtitleVisable=!subtitleVisable;
                getActivity().invalidateOptionsMenu(); //rekonstukcja menu
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /////////////
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_SUBTITLE_VISABILITY, subtitleVisable);
    }
}
