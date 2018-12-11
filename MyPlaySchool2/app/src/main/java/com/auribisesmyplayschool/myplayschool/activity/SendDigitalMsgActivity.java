package com.auribisesmyplayschool.myplayschool.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;
import com.auribisesmyplayschool.myplayschool.bean.MessageBean;
import com.auribisesmyplayschool.myplayschool.bean.StudentBean;
import com.auribisesmyplayschool.myplayschool.bean.TeacherBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SendDigitalMsgActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {
    EditText editTextMessage;
    TextView textViewSelectedAud, textViewAud;
    Button submit;
    ProgressDialog pd;
    ListView listViewStu;
    SharedPreferences preferences;
    public static ArrayList<StudentBean> selectedStudents = new ArrayList<>();
    ArrayList<String> deviceIds = new ArrayList<>();
    String stuPhone = "";
    ArrayList<String> students = new ArrayList<>();
    //ArrayList<String> audience=new ArrayList<>();
    ArrayList<Integer> ids = new ArrayList<>();
    int request, batchId, type = 0, branchId, activitySignal = 0, userId;
    ArrayAdapter adapterList;
    FirebaseFirestore db;
    ArrayList<BatchBean> batchBeanArrayList;
    String batchName;
    int msgId;
    ArrayList<Integer> listId;
    MessageBean messageBean;
    String userName;
    int chkId,branCourId;
    String teacherBatchName;
    TeacherBean teacherBean;


    void initViews() {
        db = FirebaseFirestore.getInstance();
        messageBean = new MessageBean();
        listId = new ArrayList<>();
        preferences = getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
        branchId = preferences.getInt(AttUtil.shpBranchId, 0);

        if(preferences.getInt(AttUtil.shpLoginType,0) == 2){
            batchBeanArrayList = (ArrayList<BatchBean>) getIntent().getSerializableExtra(AttUtil.BATCH_LIST);
        }else{
            teacherBean = (TeacherBean) getIntent().getSerializableExtra(AttUtil.TEACHERBEAN);
        }

        textViewAud = (TextView) findViewById(R.id.textViewAud);
        textViewAud.setOnClickListener(this);
        listViewStu = (ListView) findViewById(R.id.listViewStu);
        adapterList = new ArrayAdapter(this, android.R.layout.simple_list_item_1, students);
        listViewStu.setAdapter(adapterList);
        selectedStudents.clear();
        pd = new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.setCancelable(false);
        editTextMessage = (EditText) findViewById(R.id.editTextMsg);
        textViewSelectedAud = (TextView) findViewById(R.id.selectedAud);
        submit = (Button) findViewById(R.id.btnSubmit);
        submit.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_digital_msg);
        Intent ircv = getIntent();
        if (ircv.hasExtra("activitySignal"))
            activitySignal = ircv.getIntExtra("activitySignal", 0);
        initViews();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSubmit) {
            if(validate()){
                AttUtil.progressDialog(this);
                AttUtil.pd(1);
                getLastMsgId();
            }
        } else if (v.getId() == R.id.textViewAud) {
            showAudience();
        }
    }

     void getLastMsgId() {
        db.collection(Constants.messages_collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    if(queryDocumentSnapshots.size()>0){
                        chkId = doc.getLong("id").intValue();
                        listId.add(chkId);
                    }else{
                        msgId = 0;
                    }
                }

                if(listId.size()>0){
                    msgId = Collections.max(listId);
                    msgId = msgId + 1;
                }else{
                    msgId = 1;
                }

                if(preferences.getInt(AttUtil.shpLoginType,0) == 2){
                    setDataToBean();
                }else{
                    fetchBatchId();
                }

            }
        });
    }

     void fetchBatchId() {
        db.collection(Constants.branchCollection).document(String.valueOf(teacherBean.getBranchId()))
                .collection(Constants.branch_course_collection).document(String.valueOf(branCourId))
                .collection(Constants.batch_section_collection).whereEqualTo("batch_title",teacherBatchName)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                //DocumentSnapshot doc = null;
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    batchId = doc.getLong("batchId").intValue();
                }
                setDataToBean();
            }
        });
    }

    void setDataToBean() {
        Date date = new Date();
        messageBean.setId(msgId);
        messageBean.setBatchId(batchId);

        if(preferences.getInt(AttUtil.shpLoginType,0) == 2){
            if(batchId == 0){
                messageBean.setBatch_title("");
            }else{
                messageBean.setBatch_title(batchName);
            }
            messageBean.setBranchId(branchId);

        }else{
            messageBean.setBatch_title(teacherBatchName);
            messageBean.setBranchId(teacherBean.getBranchId());
        }

        messageBean.setMessage(editTextMessage.getText().toString().trim());
        messageBean.setType(type);
         if (preferences.getInt(AttUtil.shpLoginType, 0) == 3)
             //userId = preferences.getInt(AttUtil.shpUserId, 0);
             userId = teacherBean.getUserId();
         else
             userId = 0;
         messageBean.setUserId(userId);

        if(messageBean.getUserId() == 0)
            messageBean.setUserName("");
        else
            messageBean.setUserName(userName);

         if (type == 3)
             messageBean.setAudience(ids);
         else{
             List<Integer> empty;
             empty = new ArrayList<>();
             messageBean.setAudience(empty);
         }
         messageBean.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
         messageBean.setTime(new SimpleDateFormat("HH:mm:ss").format(date));
         sendMessage();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


     void showAudience() {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         String options[] = null;
         if (preferences.getInt(AttUtil.shpLoginType, 0) == 3) {
             //Teacher login
             options = new String[2];
             options[0] = "Section";
             options[1] = "Student";
         } else if (preferences.getInt(AttUtil.shpLoginType, 0) == 2) {
             //Manager login
             options = new String[3];
             options[0] = "All";
             options[1] = "Section";
             options[2] = "Student";
         }

         builder.setItems(options, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int position) {
                 switch (position) {
                     case 0:
                         if (preferences.getInt(AttUtil.shpLoginType, 0) == 3) {
                             type = 2;
                             selectedStudents.clear();
                             findViewById(R.id.cardViewList).setVisibility(View.GONE);
                             showBatchesAlert(1);
                         } else if (preferences.getInt(AttUtil.shpLoginType, 0) == 2) {
                             type = 1;
                             selectedStudents.clear();
                             findViewById(R.id.cardViewList).setVisibility(View.GONE);
                             textViewSelectedAud.setText("All");
                         }
                         break;
                     case 1:
                         if (preferences.getInt(AttUtil.shpLoginType, 0) == 3) {
                             type = 3;
                             //showBatchesAlert(2);
                         } else if (preferences.getInt(AttUtil.shpLoginType, 0) == 2) {
                             type = 2;
                             selectedStudents.clear();
                             findViewById(R.id.cardViewList).setVisibility(View.GONE);
                             showBatchesAlert(1);
                         }
                         break;
                     case 2:
                         type = 3;
                         showBatchesAlert(2);
                         break;
                 }
             }
         });
         builder.create().show();
    }

    void sendMessage() {
        db.collection(Constants.messages_collection).document(String.valueOf(messageBean.getId()))
                .set(messageBean).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                AttUtil.pd(0);
                clearFields();
                listId.clear();
                Toast.makeText(SendDigitalMsgActivity.this, "Message Successfully Posted!", Toast.LENGTH_LONG).show();
            }
        });
    }

     void clearFields() {
         selectedStudents.clear();
         findViewById(R.id.cardViewList).setVisibility(View.GONE);
         editTextMessage.setText("");
         textViewSelectedAud.setText("");
         textViewAud.setText("---Select an Audience---");
    }

    void showBatchesAlert(final int req) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Section");
        final String arr[];
        if (preferences.getInt(AttUtil.shpLoginType, 0) == 3) {
           arr = new String[teacherBean.getBatchCount()];
            for (int i = 0; i < teacherBean.getBatchCount(); i++) {
                arr[i] = teacherBean.getBatchGroup().get(i);
            }
        } else if (preferences.getInt(AttUtil.shpLoginType, 0) == 2) {
            arr = new String[batchBeanArrayList.size()];
            for (int i = 0; i < batchBeanArrayList.size(); i++) {
                arr[i] = batchBeanArrayList.get(i).getBatch_title();
            }
        } else
            arr = null;
//        final CharSequence []arr=new CharSequence[AttUtil.batchBeanArrayListActive.size()];
//        for(int i=0;i<AttUtil.batchBeanArrayListActive.size();i++){
//            arr[i]=AttUtil.batchBeanArrayListActive.get(i).getBatch_title();
//        }
        builder.setItems(arr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if (preferences.getInt(AttUtil.shpLoginType, 0) == 3) {
                    //batchId = teacherBean.getBat;
                    branCourId = teacherBean.getBranCourId().get(position);
                    teacherBatchName = teacherBean.getBatchGroup().get(position);
                    userName = teacherBean.getUserName();
                } else if (preferences.getInt(AttUtil.shpLoginType, 0) == 2) {
                    batchId = batchBeanArrayList.get(position).getBatchId();
                    batchName = batchBeanArrayList.get(position).getBatch_title();
                }
                if (req == 1) {
                    textViewSelectedAud.setText(arr[position].toString());
                    dialogInterface.dismiss();
                } else {
                    Intent intent = new Intent(SendDigitalMsgActivity.this, SelectStudentActivity.class);
                    //intent.putExtra("batch_id", batchId);
                    intent.putExtra("batch_name", batchName);
                    startActivityForResult(intent, 101);
                    dialogInterface.dismiss();
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == 102) {
            stuPhone = "";
            findViewById(R.id.cardViewList).setVisibility(View.VISIBLE);
            for (int i = 0; i < selectedStudents.size(); i++) {
//                if(activitySignal==1)
//                    stuPhone = stuPhone+selectedStudents.get(i).getPhone()+",";
//                else
                deviceIds.add(selectedStudents.get(i).getDevice_id());
                students.add(selectedStudents.get(i).getStuName());
                ids.add(selectedStudents.get(i).getStudentId());
            }
            adapterList.notifyDataSetChanged();
        }
    }

    boolean validate() {
        boolean valid = true;
        if (editTextMessage.getText().toString().isEmpty()) {
            valid = false;
            editTextMessage.setError("Can't leave this field empty");
        }
        if (type == 0) {
            valid = false;
            Toast.makeText(SendDigitalMsgActivity.this, "Kindly Select Audience", Toast.LENGTH_SHORT).show();
        }
        return valid;
    }


}
