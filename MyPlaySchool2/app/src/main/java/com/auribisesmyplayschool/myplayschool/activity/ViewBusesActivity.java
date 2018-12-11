package com.auribisesmyplayschool.myplayschool.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.TvViewBusItemAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BusesBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.RoutesBean;
import com.auribisesmyplayschool.myplayschool.classes.RecyclerItemClickListener;

import java.util.ArrayList;

import static android.view.View.GONE;

public class ViewBusesActivity extends AppCompatActivity {

    SharedPreferences pref;
    int branchId;

    RecyclerView rvBuses;
    TvViewBusItemAdapter busesAdapter;
    View view;

    int pos;

    boolean isMaid=false , isRouteReassign=false;
    private int reqCode = 0;
    private int responseSignal=0;

    int maidId =0;
    int driverId = 0;
    int routeId=0;

    ArrayList<BusesBean> busesBeanArrayList;
    ArrayList<RoutesBean>routesBeanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_fragment_buses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Buses");

        initViews();

        rvBuses.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        pos =position;
                        Log.i("show","position "+pos);
                        showOptions();
                    }
                }));
    }

    void initViews() {
        view = findViewById(R.id.em_bus);

        rvBuses =(RecyclerView)findViewById(R.id.lvFragmentBuses);
        rvBuses.setHasFixedSize(true);
        rvBuses.setLayoutManager(new LinearLayoutManager(this));
        rvBuses.setItemAnimator(new DefaultItemAnimator());

        busesBeanArrayList = new ArrayList<>(TransportActivity.busesBeanHashMap.values());

        if(busesBeanArrayList.size()<0) {
            rvBuses.setVisibility(GONE);
            view.setVisibility(View.VISIBLE);
        }
        else {
            busesAdapter = new TvViewBusItemAdapter(busesBeanArrayList, this);
            rvBuses.setAdapter(busesAdapter);
        }
    }

    void showOptions() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        String[] options = {"Update Driver", "Update Maid","Re-assign Route"};
        build.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //showassignDriverSpn();
                        break;
                    case 1:
                       // showassignMaidSpn();
                        break;
                    case 2:
                      //  showReassignRoutesSpinner();
                        break;
                }
            }});
        build.create().show();
    }
}
