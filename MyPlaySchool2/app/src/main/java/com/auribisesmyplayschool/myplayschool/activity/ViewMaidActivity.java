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

public class ViewMaidActivity extends AppCompatActivity {
    SharedPreferences pref;
    int branchId;


    ArrayList<UserBean> maidArrayList;

    RecyclerView rvMaids;
    TvViewUserItemAdapter maidListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_maid);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Maid");

        initViews();
        populateMaidArray();
    }

    void initViews()
    {
        rvMaids =(RecyclerView)findViewById(R.id.rv_view_maids);
        rvMaids.setHasFixedSize(true);
        rvMaids.setLayoutManager(new LinearLayoutManager(this));
        rvMaids.setItemAnimator(new DefaultItemAnimator());

    }

    void populateMaidArray()
    {
        maidArrayList = TransportActivity.init_maid_list(this);
        if(maidArrayList.size()>0)
        {
            maidListAdapter = new TvViewUserItemAdapter(this,maidArrayList);
            rvMaids.setAdapter(maidListAdapter);
        }
        else
            Toast.makeText(this,"No Maids For Branch",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

                        maidListAdapter.filter(newText);
                        return false;
                    }
                }
        );
        return true;
    }
}
