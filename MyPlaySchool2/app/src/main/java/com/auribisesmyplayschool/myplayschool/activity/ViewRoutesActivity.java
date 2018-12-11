package com.auribisesmyplayschool.myplayschool.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.TvRoutesViewAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BusesBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.RoutesBean;
import com.auribisesmyplayschool.myplayschool.classes.RecyclerItemClickListener;

import java.util.ArrayList;

import static android.view.View.GONE;

public class ViewRoutesActivity extends AppCompatActivity {
    RecyclerView rvRoutes;
    TvRoutesViewAdapter routesAdapter;
    ArrayList<BusesBean> busesBeans;
    ArrayList<RoutesBean> routesBeanArrayList;
    View view;
    int pos = 0;
    private int reqCode = 0;
    private int responseSignal = 0;
    String busIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_fragment_routes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Routes");
        busesBeans = new ArrayList<>();
        initViews();
        rvRoutes.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        pos = position;
                        showOptions();
                    }
                })
        );
    }

    void initViews() {
        view = findViewById(R.id.em_routes);
        rvRoutes = (RecyclerView) findViewById(R.id.lvFragmentRoutes);
        rvRoutes.setHasFixedSize(true);
        rvRoutes.setLayoutManager(new LinearLayoutManager(this));
        rvRoutes.setItemAnimator(new DefaultItemAnimator());
        routesBeanArrayList = new ArrayList<>(TransportActivity.routesBeanHashMap.values());
        if (routesBeanArrayList.size() < 0) {
            rvRoutes.setVisibility(GONE);
            view.setVisibility(View.VISIBLE);
        } else {
            routesAdapter = new TvRoutesViewAdapter(routesBeanArrayList, this);
            rvRoutes.setAdapter(routesAdapter);
        }
    }

    void showOptions() {
        AlertDialog.Builder build = new AlertDialog.Builder(this);
        String[] options = {"View Students","Remove Buses"};
        build.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        /*startActivity(new Intent(ViewRoutesActivity.this,StudentRouteListActivity.class)
                                .putExtra("routeId",routesBeanArrayList.get(pos).getRouteId())
                                .putExtra("routeName",routesBeanArrayList.get(pos).getRouteName()));*/
                        break;
                    case 1:
                        //showRemoveBuses();

                }
            }
        });
        build.create().show();
    }
}
