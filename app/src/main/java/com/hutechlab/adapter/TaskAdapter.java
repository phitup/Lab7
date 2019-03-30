package com.hutechlab.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hutechlab.lab7extra.R;
import com.hutechlab.model.Task;

import java.util.List;

public class TaskAdapter extends BaseAdapter {
    Activity context;
    int resource;
    CheckBox chkComplete;
    Boolean check;
    TextView txtTaskName;
    private List<Task> taskList;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    public TaskAdapter(Activity context, int resource, List<Task> taskList) {
        this.context = context;
        this.resource = resource;
        this.taskList = taskList;
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position,View convertView, ViewGroup parent) {
        LayoutInflater inflatter = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View custom= inflatter.inflate(this.resource, null);

        ImageView imgEmp= custom.findViewById(R.id.imgEmp);
        txtTaskName= custom.findViewById(R.id.txtTaskName);
        TextView txtTaskInfo= custom.findViewById(R.id.txtTaskInfo);
        TextView txtTaskTime= custom.findViewById(R.id.txtTaskTime);
        TextView txtID = custom.findViewById(R.id.txtID);
        final CheckBox chkDel= custom.findViewById(R.id.chkDel);
        chkComplete = custom.findViewById(R.id.chkComplete);
        final Task task = taskList.get(position);



        if(task.getTaskEmp().equals("m")){
            imgEmp.setImageResource(R.drawable.doge);
        }
        else{
            imgEmp.setImageResource(R.drawable.husky);
        }
        txtTaskName.setText(task.getTaskName());
        txtTaskInfo.setText(task.getTaskInfo());
        txtTaskTime.setText("Ngày: "+task.getTaskDate());
        txtTaskTime.setText(task.getTaskTime());

        chkDel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chkDel.isChecked()){
                    task.setCheckedDelete(true);
                    mDatabase.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                if(task.getTaskID() == ds.getKey()){
                                    mDatabase.child("data").child(ds.getKey()).child("checkedDelete").setValue(true);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    mDatabase.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                if(task.getTaskID() == ds.getKey()){
                                    mDatabase.child("data").child(ds.getKey()).child("checkedDelete").setValue(false);


                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        chkComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(chkComplete.isChecked()){
                    mDatabase.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                if(task.getTaskID() == ds.getKey()){
                                    mDatabase.child("data").child(ds.getKey()).child("checkedComplete").setValue(true);

                                    mDatabase.child("data").child(ds.getKey()).child("stamp").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Log.d("BBB", "value : " + dataSnapshot.getValue());
                                            if(dataSnapshot.getValue().equals(true)){
                                                chkComplete.setEnabled(false);
                                                Log.d("BBB","ahihi");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    mDatabase.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                if(task.getTaskID() == ds.getKey()){
                                    mDatabase.child("data").child(ds.getKey()).child("checkedComplete").setValue(false);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        return custom;
    }


}
