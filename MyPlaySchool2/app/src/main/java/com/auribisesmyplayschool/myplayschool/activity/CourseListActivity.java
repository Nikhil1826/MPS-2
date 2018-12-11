package com.auribisesmyplayschool.myplayschool.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.ViewCourseAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BranCourBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class CourseListActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<BranCourBean> courseBeanArrayList;
    ViewCourseAdapter viewCourseAdapter;
    BranCourBean courseBean;
    int pos,reqCode;
    SharedPreferences preferences;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
       // getSupportActionBar().hide();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Class");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initview();
    }
    void initview(){
        searchView = new SearchView(this);
        preferences=getSharedPreferences(AttUtil.shpREG, MODE_PRIVATE);
        listView = (ListView)findViewById(R.id.courseListView);
        //requestQueue = Volley.newRequestQueue(this);
        if(AttUtil.isNetworkConnected(this)){
            retrievefromdb();
        }else{
            //courseNetConnect();
        }
    }

    void retrievefromdb(){
        courseBeanArrayList = (ArrayList<BranCourBean>)getIntent().getSerializableExtra(AttUtil.KEY_COURSE_ARRAYLIST);
        getSupportActionBar().setTitle("View Class("+courseBeanArrayList.size()+")");
        viewCourseAdapter = new ViewCourseAdapter(CourseListActivity.this, R.layout.listcourse,courseBeanArrayList);
        listView.setAdapter(viewCourseAdapter);
        listView.setOnItemClickListener(onItemClickListener);

//
//        AttUtil.progressDialog(CourseListActivity.this);
//        AttUtil.pd(1);
//        JSONObject jsonObject = new JSONObject();
//        try{
//            jsonObject.put(AttUtil.branchId,preferences.getInt(AttUtil.shpBranchId,0));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        Log.i("courseList",jsonObject.toString());
//        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, AttUtil.retrieve_course_url, jsonObject, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                try{
//                    Log.i("courseList",jsonObject.toString());
//                    if(jsonObject.getInt(AttUtil.TAG_SUCCESS)==1){
//                        AttUtil.pd(0);
//                        JSONArray jsonArray = jsonObject.getJSONArray(AttUtil.TAG_COURSES);
//                        Type listType = new TypeToken<List<CourseBean>>() {
//                        }.getType();
//                        courseBeanArrayList = new Gson().fromJson(jsonArray.toString(), listType);
//                        getSupportActionBar().setTitle("View Courses("+courseBeanArrayList.size()+")");
//                        viewCourseAdapter = new ViewCourseAdapter(CourseListActivity.this, R.layout.listcourse,courseBeanArrayList);
//                        listView.setAdapter(viewCourseAdapter);
//                        listView.setOnItemClickListener(onItemClickListener);
//                    }else if (jsonObject.getInt(AttUtil.TAG_SUCCESS) == 2) {
//                        AttUtil.pd(0);
//                        Toast.makeText(CourseListActivity.this, "Branch is Deactivated", Toast.LENGTH_LONG).show();
//                        finish();
//                    }else{
//                        AttUtil.pd(0);
//                        Toast.makeText(CourseListActivity.this,"No Course Found", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                }catch(Exception e){
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                AttUtil.pd(0);
//                Toast.makeText(CourseListActivity.this, AttUtil.volleyError, Toast.LENGTH_SHORT).show();
//            }
//        });
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(60 * 8 * 1000, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        requestQueue.add(jsonObjectRequest);
    }
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            pos=position;
            courseBean = courseBeanArrayList.get(position);
            //showoption();
            //viewcoursedetial();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem searchitem=menu.findItem(R.id.Search_list);
        searchitem.setActionView(searchView);
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(onQueryTextListener);
        searchView.setQueryHint("Search");
        return super.onCreateOptionsMenu(menu);
    }

    SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            viewCourseAdapter.filter(newText.toString());
            viewCourseAdapter.notifyDataSetChanged();
            return false;
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
