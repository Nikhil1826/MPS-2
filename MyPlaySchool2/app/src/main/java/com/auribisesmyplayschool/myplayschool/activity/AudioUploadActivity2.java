package com.auribisesmyplayschool.myplayschool.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.bean.BatchBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;

import java.util.ArrayList;

public class AudioUploadActivity2 extends AppCompatActivity implements View.OnClickListener {
    String name;
    int batchId,branchId,signal=0;
    SharedPreferences preferences;
    Spinner spinnerAudience;
    ArrayList<String> arrayListAud=new ArrayList<>();
    boolean teacherLogin;
    CardView cardOne,cardTwo,cardThree,tvChoosePdfCard,tvChooseAudioCard,tvChooseImageCard;
    TextView tvChoosePdf,tvChooseAudio,tvChooseImage,tvChooseNext,textViewName,textViewSelectedAud;
    EditText edtChooseFileName;
    ArrayList<BatchBean> batchBeanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_upload2);
        getSupportActionBar().setTitle("Upload Media");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        preferences=getSharedPreferences(AttUtil.shpREG,MODE_PRIVATE);
        branchId=preferences.getInt(AttUtil.shpBranchId,0);
        batchBeanArrayList = (ArrayList<BatchBean>) getIntent().getSerializableExtra(AttUtil.BATCH_LIST);
        textViewSelectedAud=(TextView)findViewById(R.id.selectedAud);
        textViewName=(TextView)findViewById(R.id.textViewFile);
        if(preferences.getInt(AttUtil.shpLoginType,0)==3)
            teacherLogin=true;
        else if(preferences.getInt(AttUtil.shpLoginType,0)==2)
            teacherLogin=false;
        arrayListAud.add("--Select Audience--");
        if(teacherLogin){
            arrayListAud.add("Section");
        }else{
            arrayListAud.add("All");
            arrayListAud.add("Section");
        }
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,arrayListAud);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAudience=(Spinner)findViewById(R.id.spinnerAudience);
        spinnerAudience.setAdapter(adapter);
        spinnerAudience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1:
                        if(teacherLogin){
                            //showBatchesAlert();
                        }else{
                            batchId=0;
                            textViewSelectedAud.setText("To: All");
                        }
                        showFileCardOptions();
                        break;
                    case 2:
                        showBatchesAlert();
                        showFileCardOptions();
                        break;
                    case 0:
                        cardTwo.setVisibility(View.INVISIBLE);
                        cardThree.setVisibility(View.INVISIBLE);
                        textViewSelectedAud.setText("");
                        edtChooseFileName.setText("");
                        signal=0;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    void showFileCardOptions(){
        cardTwo.setVisibility(View.VISIBLE);
        cardThree.setVisibility(View.INVISIBLE);
        edtChooseFileName.setText("");
        signal=0;
        changeCardOption();
    }

    void changeCardOption(){
        tvChoosePdfCard.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        tvChooseAudioCard.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        tvChooseImageCard.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        tvChoosePdf.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        tvChooseAudio.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        tvChooseImage.setTextColor(ContextCompat.getColor(this, android.R.color.black));
    }

    void showBatchesAlert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Select a Section");
        final String arr[];
        if(teacherLogin){
            //arr=new String[AttUtil.batchBeanArrayListTeacher.size()];
            arr =null;
            /*for(int i = 0; i< AttUtil.batchBeanArrayListTeacher.size(); i++){
                arr[i]= AttUtil.batchBeanArrayListTeacher.get(i).getBatch_title();
            }*/
        }else{
            arr=new String[batchBeanArrayList.size()];
            for(int i = 0; i< batchBeanArrayList.size(); i++){
                arr[i]= batchBeanArrayList.get(i).getBatch_title();
            }
        }
        builder.setItems(arr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int position) {
                if(teacherLogin){
                   // batchId= AttUtil.batchBeanArrayListTeacher.get(position).getBatchId();
                }
                else
                    batchId= batchBeanArrayList.get(position).getBatchId();
                textViewSelectedAud.setText("To: "+arr[position].toString());
                dialogInterface.dismiss();
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
    public void onClick(View view) {
        if(R.id.tvChoosePdf==view.getId()){
            changeCardOption();
            tvChoosePdfCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvChoosePdf.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            signal=1;
            cardThree.setVisibility(View.VISIBLE);
            edtChooseFileName.setText("");
        }else if(R.id.tvChooseAudio==view.getId()){
            changeCardOption();
            tvChooseAudioCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvChooseAudio.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            signal=2;
            cardThree.setVisibility(View.VISIBLE);
            edtChooseFileName.setText("");
        }else if(R.id.tvChooseImage==view.getId()){
            changeCardOption();
            tvChooseImageCard.setCardBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            tvChooseImage.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            signal=3;
            cardThree.setVisibility(View.VISIBLE);
            edtChooseFileName.setText("");
        }else if (R.id.tvChooseNext==view.getId()){
            /*if(edtChooseFileName.getText().toString().trim().length()>0){
                if(signal==1)
                    showPdfOptions();
                if(signal==2)
                    chooseFile();
                if(signal==3)
                    showCaptureOptions();
            }else
                Toast.makeText(this, "Invalid Name for file", Toast.LENGTH_SHORT).show();*/
        }

    }


}
