package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.adapter.CategoryAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.CategoryBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

import dmax.dialog.SpotsDialog;

public class AddFeeCategoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listViewCategories;
    private FloatingActionButton fab;
    private int branchId,updateMode=0,request,position;
    private CategoryBean categoryBean;
    private ArrayList<CategoryBean> categoryBeanArrayList=new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    FirebaseFirestore db;
    int feeCategoryId;
    ArrayList<Integer> listId;

    private void initViews(){
        listId = new ArrayList<>();
        branchId=getIntent().getIntExtra("branchId",0);
        db = FirebaseFirestore.getInstance();
        categoryBean = new CategoryBean();
        listViewCategories=(ListView)findViewById(R.id.listViewCategories);
        listViewCategories.setOnItemClickListener(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMode=0;
               // getLastCategoryId();
                alertAddCategory("");

            }
        });
    }

     void getLastCategoryId(final EditText edtCategory, final Dialog dialog) {
        db.collection(Constants.fee_category_collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    if(queryDocumentSnapshots.size()>0){
                        int chkId = doc.getLong("feeCategoryId").intValue();
                        listId.add(chkId);
                    }else{
                        feeCategoryId = 0;
                    }
                }

                if(listId.size()>0){
                    feeCategoryId = Collections.max(listId);
                }else{
                    feeCategoryId = 0;
                }
                setDataToBean(edtCategory,dialog);
            }
        });
    }

    void alertAddCategory(String categoryName) {
         final AlertDialog.Builder builder=new AlertDialog.Builder(this);
         View view=getLayoutInflater().inflate(R.layout.dialog_add_category,null);
         final EditText edtCategory=(EditText)view.findViewById(R.id.editTextCategory);
         edtCategory.setText(categoryName);
         edtCategory.setHint("Enter Category Name");
         Button buttonAdd=(Button)view.findViewById(R.id.buttonAddCategory);
         builder.setView(view);
         if(updateMode==1){
             buttonAdd.setText("UPDATE");
             builder.setTitle("Update Category");
         }else{
             builder.setTitle("Add Category");
             buttonAdd.setText("ADD");
         }

         final Dialog dialog=builder.create();
         buttonAdd.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 AttUtil.progressDialog(AddFeeCategoryActivity.this);
                 AttUtil.pd(1);
                 if(updateMode==1){
                     categoryBean.setCategoryName(edtCategory.getText().toString().trim());
                     insertIntoDB(dialog);
                     //jsonObject.put("id", categoryBeanArrayList.get(AddFeeCategoryActivity.this.position).getFeeCategoryId());
                 }else{
                     getLastCategoryId(edtCategory,dialog);
                 }
             }
         });
         dialog.show();


    }

     void setDataToBean(EditText edtCategory, Dialog dialog) {
         categoryBean =new CategoryBean();
         categoryBean.setCategoryName(edtCategory.getText().toString().trim());
         categoryBean.setBranchId(branchId);
         categoryBean.setFeeCategoryId(feeCategoryId+1);
         insertIntoDB(dialog);
    }

     void insertIntoDB(final Dialog dialog) {
        if(updateMode==0){
            db.collection(Constants.fee_category_collection).document(String.valueOf(categoryBean.getFeeCategoryId()))
                    .set(categoryBean).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AttUtil.pd(0);
                    dialog.dismiss();
                    listId.clear();
                    Toast.makeText(AddFeeCategoryActivity.this,"New category "+categoryBean.getCategoryName()+" added.",Toast.LENGTH_SHORT).show();
                    categoryBeanArrayList.add(categoryBean);
                    categoryAdapter.notifyDataSetChanged();
                }
            });
        }else{
            db.collection(Constants.fee_category_collection).document(String.valueOf(categoryBean.getFeeCategoryId()))
                    .update("categoryName",categoryBean.getCategoryName()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AttUtil.pd(0);
                    dialog.dismiss();
                    updateMode = 0;
                    Toast.makeText(AddFeeCategoryActivity.this,"Updated",Toast.LENGTH_SHORT).show();
                    categoryAdapter.notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fee_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();
        AttUtil.progressDialog(this);
        AttUtil.pd(1);
        retrieveCategories();

    }

     void retrieveCategories() {
        db.collection(Constants.fee_category_collection).whereEqualTo("branchId",branchId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                    categoryBean = doc.toObject(CategoryBean.class);
                    categoryBeanArrayList.add(categoryBean);
                }
                categoryAdapter=new CategoryAdapter(AddFeeCategoryActivity.this,R.layout.adapter_categories,categoryBeanArrayList,1);
                listViewCategories.setAdapter(categoryAdapter);
                AttUtil.pd(0);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view,final int position, long id) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Update Category?");
        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateMode=1;
                AddFeeCategoryActivity.this.categoryBean = categoryBeanArrayList.get(position);
                AddFeeCategoryActivity.this.position=position;
                alertAddCategory(categoryBeanArrayList.get(position).getCategoryName());
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
