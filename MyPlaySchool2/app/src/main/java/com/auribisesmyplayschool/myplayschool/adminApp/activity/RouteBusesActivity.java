package com.auribisesmyplayschool.myplayschool.adminApp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.activity.LoginActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BusesBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.RoutesBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.auribisesmyplayschool.myplayschool.adminApp.fragment.BusesFragment;
import com.auribisesmyplayschool.myplayschool.adminApp.fragment.RoutesFragment;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class RouteBusesActivity extends AppCompatActivity {


    //ListView listView;
    public static ArrayList<BusesBean> busesBeansArrayList;
    public static ArrayList<RoutesBean> routesBeansArrayList;

    boolean isRoute=true;

//    private ArrayList<UserBean> userBeanArrayList;
//    private UserBean userBean;

    private int reqCode = 0;
    private SharedPreferences prefs;
    private int responseSignal=0;

    private RouteBusesActivity.SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager mViewPager;
    private BusesFragment busesFragment;
    private RoutesFragment routesFragment;
    public static int branch_id;

    RoutesBean routesBean;
    BusesBean busesBean;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_buses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        busesFragment = BusesFragment.newInstance(getApplicationContext());
        routesFragment = RoutesFragment.newInstance(getApplicationContext());

        Intent rcv = getIntent();
        branch_id = rcv.getIntExtra("branchId",0);

        prefs = getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);

        initViews();

        if (AdminUtil.isNetworkConnected(this)){
            AttUtil.progressDialog(this);
            AttUtil.pd(1);
            getRoutes();
        }

        /*else
            retrieveNetConnect();*/
    }

     void getRoutes() {
        db.collection(Constants.routes_collection).whereEqualTo("branchId",branch_id).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    routesBean = doc.getDocument().toObject(RoutesBean.class);
                    routesBeansArrayList.add(routesBean);
                }
                getBuses();
            }
        });
    }

     void getBuses() {
         db.collection(Constants.buses_collection).whereEqualTo("branchId",branch_id).get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                             busesBean = doc.getDocument().toObject(BusesBean.class);
                             busesBeansArrayList.add(busesBean);
                         }
                         if(routesBeansArrayList.size()>0)
                             routesFragment.init_list();
                         if(busesBeansArrayList.size()>0)
                             busesFragment.init_list();
                         AttUtil.pd(0);
                     }
                 });
    }

    public void initViews()
    {

        FloatingActionMenu menu =(FloatingActionMenu)findViewById(R.id.fab_bus_route);
        if(menu.isOpened())
        {
            menu.close(true);
        }

        db = FirebaseFirestore.getInstance();
        busesBeansArrayList = new ArrayList<>();

        routesBeansArrayList = new ArrayList<>();
        routesBean = new RoutesBean();
        busesBean = new BusesBean();

        sectionsPagerAdapter = new RouteBusesActivity .SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {   isRoute=true;
                    getSupportActionBar().setTitle("Routes("+routesBeansArrayList.size()+")");}
                if(position==1)
                {   isRoute=false;
                    getSupportActionBar().setTitle("Buses("+busesBeansArrayList.size()+")");}


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment f = null;

            switch (position)
            {
                case 0:
                    f = routesFragment;
                    break;
                case 1:
                    f = busesFragment;
                    break;


            }
            return f;
        }

        @Override
        public int getCount()
        {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Routes";

                case 1:
                    return "Buses";

            }
            return null;
        }

    }

    public void onClick(View view)
    {
        int id = view.getId();
        Log.i("test",""+id);
        if(id==R.id.fab1)
        {
            if(isRoute){
                RoutesFragment.updateMode = false;
                routesFragment.showInsertDialogRoute();
            }
            else{
                BusesFragment.updateMode = false;
                busesFragment.showInsertDialogBus();
            }

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }


}
