package com.hutechlab.lab7extra;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hutechlab.adapter.TaskAdapter;
import com.hutechlab.model.Task;

import static android.graphics.Color.rgb;


//import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Button btnDelete, btnSave, btnDate, btnTime, btnComplete;
    ListView lvEmp;
    TabHost host;
    EditText edtTaskName, edtTaskInfo;


    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Task> taskArrayList;


    RadioGroup radgEmp;

    TaskAdapter taskAdapter;

    TextView txtDate, txtTime;
    int checkfirebase = 0;
    String ID;

    //private TextView dateTimeDisplay;
     Calendar calendar;
     SimpleDateFormat dateFormat;
     String date;
     //calendar = Calendar.getInstance();

    Date currentTime = Calendar.getInstance().getTime();


    String selectedDate, selectedTime;

    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadControls();
        addEvents();
    }

    private void addEvents() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Xóa các mục đã chọn?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Vâng",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                int count= 0;
                                Log.d("BBB","ID : " + ID);
                                mDatabase.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                                            Boolean delete = ds.getValue(Task.class).isCheckedDelete();
                                            Log.d("BBB", delete.toString());
                                            if(delete.equals(true)){
                                                ds.getRef().removeValue();
                                                update();
                                                Log.d("BBB", "1");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(MainActivity.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        });

                builder1.setNegativeButton(
                        "Thôi",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                return;
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Hoàn thành các mục đã chọn?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Vâng",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                int count= 0;
                                mDatabase.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                                            Boolean complete = ds.getValue(Task.class).isCheckedComplete();
                                            if(complete.equals(true)){
//                                                ds.getRef().removeValue();
//                                                update();
                                                mDatabase.child("data").child(ds.getKey()).child("stamp").setValue(true);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(MainActivity.this, "Đã hoàn thành ("+count+") mục!", Toast.LENGTH_SHORT).show();
                            }
                        });

                builder.setNegativeButton(
                        "Thôi",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                return;
                            }
                        });

                AlertDialog alert11 = builder.create();
                alert11.show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task= new Task();
                task.setTaskName(edtTaskName.getText().toString());
                task.setTaskInfo(edtTaskInfo.getText().toString());
                if(radgEmp.getCheckedRadioButtonId() == R.id.radMe){
                    task.setTaskEmp("m");
                }
                else{
                    task.setTaskEmp("b");
                }
                task.setTaskDate(selectedDate);
                task.setTaskTime(selectedTime);
                task.setCheckedDelete(false);
                task.setCheckedComplete(false);

//                taskAdapter.add(task);
//                taskAdapter.notifyDataSetChanged();
                mDatabase.child("data").push().setValue(task);
                Toast.makeText(MainActivity.this, "Đã thêm 1 công việc mới", Toast.LENGTH_SHORT).show();
                checkfirebase = 1;
                host.setCurrentTab(1);
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateClicked();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeClicked();
            }
        });
    }

    private void TimeClicked() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if(mHour>hourOfDay||mMinute>minute)
                        {
                            txtTime.setText(hourOfDay + ":" + minute +" thời  phải lớn hơn hiện tại");
                            txtTime.setTextColor(rgb(244, 66, 66));
                            //txtTime.setError("Thời gian phải lớn hơn thời gian hiện tại");
                            btnSave.setEnabled(false);
                        }
                        else {
                            btnSave.setEnabled(true);
                            txtTime.setText(hourOfDay + ":" + minute);
                            txtTime.setTextColor(rgb(65, 169, 244));
                            selectedTime= hourOfDay + ":" + minute;

                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    private void DateClicked() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

//                        calendar = Calendar.getInstance();
//                        dateFormat = new SimpleDateFormat("dd");
//                        date = dateFormat.format(calendar.getTime());
//
//
                        if(mMonth>monthOfYear || mYear>year || mDay>dayOfMonth)
                        {
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " thời  phải lớn hơn hiện tại");
                            txtDate.setTextColor(rgb(244, 66, 66));
                            //txtDate.setError("ngày phải lớn hơn thời điểm hiện tại");
                            btnSave.setEnabled(false);
                        }
                        else {
                            btnSave.setEnabled(true);
                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            txtDate.setTextColor(rgb(65, 169, 244));
                            selectedDate= dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        }


                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void update(){

        mDatabase.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskArrayList.removeAll(taskArrayList);
                taskAdapter= new TaskAdapter(MainActivity.this, R.layout.listitem, taskArrayList);
                lvEmp.setAdapter(taskAdapter);
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String taskName = ds.getValue(Task.class).getTaskName();
                    String taskInfo = ds.getValue(Task.class).getTaskInfo();
                    String taskEmp = ds.getValue(Task.class).getTaskEmp();
                    String taskDate = ds.getValue(Task.class).getTaskDate();
                    String taskTime = ds.getValue(Task.class).getTaskTime();
                    boolean isCheckedComplete = ds.getValue(Task.class).isCheckedComplete();
                    boolean isCheckedDelete = ds.getValue(Task.class).isCheckedDelete();
                    Task task = new Task(taskName, taskInfo, taskEmp, taskDate, taskTime, isCheckedDelete, isCheckedComplete);
                    taskArrayList.add(task);
                    task.setTaskID(ds.getKey());
                    ID = ds.getKey();
                }
                Log.d("BBB", taskArrayList.toString());

                taskAdapter= new TaskAdapter(MainActivity.this, R.layout.listitem, taskArrayList);
                lvEmp.setAdapter(taskAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadControls() {
        btnDelete= findViewById(R.id.btnDelete);
        btnComplete = findViewById(R.id.btnComplete);
        btnSave= findViewById(R.id.btnSave);
        btnDate= findViewById(R.id.btnDate);
        btnTime= findViewById(R.id.btnTime);

        txtDate= findViewById(R.id.txtDate);
        txtTime= findViewById(R.id.txtTime);

        host = findViewById(R.id.tabHost);
        host.setup();
        TabHost.TabSpec spec = host.newTabSpec("Tab One").setIndicator("Tạo công việc");
        spec.setContent(R.id.tabEdit);
        host.addTab(spec);
        //Tab 2
        spec = host.newTabSpec("Tab Two").setIndicator("Danh sách công việc");
        spec.setContent(R.id.tabList);
        host.addTab(spec);

        edtTaskName= findViewById(R.id.edtTaskName);
        edtTaskInfo= findViewById(R.id.edtTaskInfo);

        edtTaskInfo.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( edtTaskInfo.getText().toString().length() == 0 ){
                    btnSave.setEnabled(false);
                    edtTaskInfo.setError( "Nội dung mô  không được rỗng " );
                }
                if(!s.equals("") ) {
                    //btnSave.setEnabled(true);
                }
            }



            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });


        edtTaskName.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( edtTaskName.getText().toString().length() == 0 ) {
                    edtTaskName.setError("Nội dung tên không được rong ");
                    btnSave.setEnabled(false);
                }
                if(!s.equals("") ) {
                   // btnSave.setEnabled(true);

                }
            }



            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
        radgEmp= findViewById(R.id.radgEmp);
        lvEmp= findViewById(R.id.lvEmp);

        taskArrayList = new ArrayList<>();

        mDatabase.child("data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String taskName = ds.getValue(Task.class).getTaskName();
                    String taskInfo = ds.getValue(Task.class).getTaskInfo();
                    String taskEmp = ds.getValue(Task.class).getTaskEmp();
                    String taskDate = ds.getValue(Task.class).getTaskDate();
                    String taskTime = ds.getValue(Task.class).getTaskTime();
                    boolean isCheckedComplete = ds.getValue(Task.class).isCheckedComplete();
                    boolean isCheckedDelete = ds.getValue(Task.class).isCheckedDelete();
                    Task task = new Task(taskName, taskInfo, taskEmp, taskDate, taskTime, isCheckedDelete, isCheckedComplete);
                    taskArrayList.add(task);
                    task.setTaskID(ds.getKey());
                    ID = ds.getKey();
                }
                Log.d("BBB", taskArrayList.toString());

                taskAdapter= new TaskAdapter(MainActivity.this, R.layout.listitem, taskArrayList);
                lvEmp.setAdapter(taskAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mDatabase.child("data").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(checkfirebase == 1){
                    taskArrayList.removeAll(taskArrayList);
                    taskAdapter= new TaskAdapter(MainActivity.this, R.layout.listitem, taskArrayList);
                    lvEmp.setAdapter(taskAdapter);
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String taskName = ds.getValue(Task.class).getTaskName();
                        String taskInfo = ds.getValue(Task.class).getTaskInfo();
                        String taskEmp = ds.getValue(Task.class).getTaskEmp();
                        String taskDate = ds.getValue(Task.class).getTaskDate();
                        String taskTime = ds.getValue(Task.class).getTaskTime();
                        boolean isCheckedComplete = ds.getValue(Task.class).isCheckedComplete();
                        boolean isCheckedDelete = ds.getValue(Task.class).isCheckedDelete();

                        Task task = new Task(taskName, taskInfo, taskEmp, taskDate, taskTime, isCheckedDelete, isCheckedComplete);
                        taskArrayList.add(task);
                        task.setTaskID(ds.getKey());
                    }
                    Log.d("BBB", taskArrayList.toString());

                    taskAdapter= new TaskAdapter(MainActivity.this, R.layout.listitem, taskArrayList);
                    lvEmp.setAdapter(taskAdapter);
                    checkfirebase = 0;
                }else{

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
