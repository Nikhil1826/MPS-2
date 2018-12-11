package com.auribisesmyplayschool.myplayschool.activity;

import android.content.SharedPreferences;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.TvViewUserItemAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;

import java.util.ArrayList;

public class ViewDriversActivity extends AppCompatActivity {

    ArrayList<UserBean> driverArrayList;

    RecyclerView rvDriver;
    TvViewUserItemAdapter driverListAdapter;

    private int reqCode = 0;
    private SharedPreferences prefs;
    private int responseSignal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_drivers);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Drivers");

        initView();

        populateDriverArray();

    }

    void initView()
    {
        rvDriver = findViewById(R.id.rv_view_drivers);
        rvDriver.setHasFixedSize(true);
        rvDriver.setLayoutManager(new LinearLayoutManager(this));
        rvDriver.setItemAnimator(new DefaultItemAnimator());
    }

    void populateDriverArray()
    {
        driverArrayList = TransportActivity.init_driver_list(this);

        if(driverArrayList.size()>0)
        {
            driverListAdapter = new TvViewUserItemAdapter(this,driverArrayList);
            rvDriver.setAdapter(driverListAdapter);
        }

        else
            Toast.makeText(this,"No Driver For Branch",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        MenuItem search= menu.findItem( R.id.search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(search);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener(){
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        driverListAdapter.filter(newText);
                        return false;
                    }
                }
        );
        return true;
    }
}
