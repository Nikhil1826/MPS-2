package com.auribisesmyplayschool.myplayschool.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adapter.Spn_Adapter_Bus;
import com.auribisesmyplayschool.myplayschool.adapter.Spn_Adapter_Route;
import com.auribisesmyplayschool.myplayschool.adapter.Spn_Adapter_User;
import com.auribisesmyplayschool.myplayschool.adapter.TransportAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BusesBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.RoutesBean;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.UserBean;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.auribisesmyplayschool.myplayschool.classes.RecyclerItemClickListener;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

import static java.security.AccessController.getContext;

public class TransportActivity extends AppCompatActivity {

    ArrayList<UserBean> driverMaidArrayList;
    ArrayList<RoutesBean> routesBeanArrayList;
    ArrayList<BusesBean> busesBeanArrayList;
    UserBean userBean;
    RoutesBean routesBean;
    BusesBean busesBean;
    ArrayList<UserBean> userBeanArrayList;

    public static HashMap<Integer, UserBean> userBeanHashMap;
    public static HashMap<Integer, RoutesBean> routesBeanHashMap;
    public static HashMap<Integer, BusesBean> busesBeanHashMap;


    private int reqCode = 0;
    private SharedPreferences prefs;
    private int responseSignal = 0;

    boolean isReassignRoute = false;
    FirebaseFirestore db;
    int branchId;
    SpotsDialog progressDialog;
    int busId = 0;
    int maidId = 0;
    int routeId = 0;
    int driverId = 0;
    int spinner_selected_bus = 0;


    String[] transport_main_list = {"Drivers", "Maid", "Routes", "Buses"};
    int[] transport_main_images = {R.drawable.ic_driver, R.drawable.ic_maids, R.drawable.ic_route1, R.drawable.ic_school_bus};
    int[] color = {
            R.color.transport_driver,
            R.color.transport_maid,
            R.color.transport_route,
            R.color.transport_bus
    };

    RecyclerView rvTransport;
    TransportAdapter transportAdapter;

    ArrayList<RoutesBean> spinnerRoutesBeanArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        branchId = getIntent().getIntExtra("branchId", 0);
        initView();
        prefs = getSharedPreferences(AttUtil.shpREG, Context.MODE_PRIVATE);
        if (AttUtil.isNetworkConnected(this)){
            AttUtil.progressDialog(this);
            AttUtil.pd(1);
            retrieveData();
        }

        //else
            //retrieveNetConnect();
            rvTransport.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            switch (position) {
                                case 0:
                                    startActivity(new Intent(TransportActivity.this, ViewDriversActivity.class));
                                    break;

                                case 1:
                                    startActivity(new Intent(TransportActivity.this, ViewMaidActivity.class));
                                    break;


                                case 2:
                                    startActivity(new Intent(TransportActivity.this, ViewRoutesActivity.class));
                                    break;


                                case 3:
                                   startActivity(new Intent(TransportActivity.this, ViewBusesActivity.class));
                                    break;
                            }
                        }
                    }));

    }

    void retrieveData() {
        db.collection(Constants.usersCollection).whereEqualTo("branchId", branchId)
                .whereEqualTo("userType", 4).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            userBean = doc.getDocument().toObject(UserBean.class);
                            driverMaidArrayList.add(userBean);
                        }
                        fetchMaidList();
                    }
                });

       /*for(int i=0;i<userBeanArrayList.size();i++){
           if(userBeanArrayList.get(i).getUserType() == 4 || userBeanArrayList.get(i).getUserType() == 5 ){
               driverMaidArrayList.add(userBeanArrayList.get(i));
           }
       }*/
       // fetchRouteList();
    }

     void fetchMaidList() {
         db.collection(Constants.usersCollection).whereEqualTo("branchId", branchId)
                 .whereEqualTo("userType", 5).get()
                 .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                     @Override
                     public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                         for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                             userBean = doc.getDocument().toObject(UserBean.class);
                             driverMaidArrayList.add(userBean);
                         }
                         fetchRouteList();
                     }
                 });
    }

    void fetchRouteList() {
        db.collection(Constants.routes_collection).whereEqualTo("branchId", branchId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            routesBean = doc.getDocument().toObject(RoutesBean.class);
                            routesBeanArrayList.add(routesBean);
                        }
                        fetchBusesList();
                    }
                });
    }

    void fetchBusesList() {
        db.collection(Constants.buses_collection).whereEqualTo("branchId", branchId).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                            busesBean = doc.getDocument().toObject(BusesBean.class);
                            busesBeanArrayList.add(busesBean);
                        }
                        initHashMaps();
                    }
                });
    }

    void initHashMaps() {
        routesBeanHashMap = new HashMap<Integer, RoutesBean>();
        userBeanHashMap = new HashMap<Integer, UserBean>();
        busesBeanHashMap = new HashMap<Integer, BusesBean>();

        for (RoutesBean r : routesBeanArrayList) {
            routesBeanHashMap.put(r.getRouteId(), r);
//            Log.i("show",r.toString());
        }

        for (BusesBean b : busesBeanArrayList) {
            busesBeanHashMap.put(b.getBusId(), b);
//            Log.i("show",b.toString());
        }

        for (UserBean u : driverMaidArrayList) {
            userBeanHashMap.put(u.getUserId(), u);
//            Log.i("show",u.toString());
        }
        AttUtil.pd(0);
    }

    static ArrayList<UserBean> init_maid_list(Context context) {
        ArrayList<UserBean> maidArrayList = new ArrayList<>();
        if (userBeanHashMap.size() > 0) {
            for (HashMap.Entry<Integer, UserBean> u : TransportActivity.userBeanHashMap.entrySet()) {
                if (u.getValue().getUserType() == 5)
                    maidArrayList.add(u.getValue());
            }
        } else
            Toast.makeText(context, "No Maid For Branch", Toast.LENGTH_LONG).show();
        return maidArrayList;
    }

    static ArrayList<UserBean> init_driver_list(Context context) {
        ArrayList<UserBean> driverArrayList = new ArrayList<>();
        if (userBeanHashMap.size() > 0) {
            for (HashMap.Entry<Integer, UserBean> u : TransportActivity.userBeanHashMap.entrySet()) {
                if (u.getValue().getUserType() == 4)
                    driverArrayList.add(u.getValue());
            }
        } else
            Toast.makeText(context, "No Driver For Branch", Toast.LENGTH_LONG).show();
        return driverArrayList;
    }




    void initView() {
        db = FirebaseFirestore.getInstance();
        userBean = new UserBean();
        userBeanArrayList = (ArrayList<UserBean>) getIntent().getSerializableExtra("userList");
        routesBean = new RoutesBean();
        busesBean = new BusesBean();
        driverMaidArrayList = new ArrayList<>();
        routesBeanArrayList = new ArrayList<>();
        busesBeanArrayList = new ArrayList<>();
        FloatingActionMenu menu = (FloatingActionMenu) findViewById(R.id.fab_transport);
        if (menu.isOpened()) {
            menu.close(true);
        }

        rvTransport = (RecyclerView) findViewById(R.id.transport_rv);
        rvTransport.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvTransport.setLayoutManager(gridLayoutManager);
        rvTransport.setItemAnimator(new DefaultItemAnimator());

        transportAdapter = new TransportAdapter(TransportActivity.this, transport_main_list, transport_main_images, color);
        rvTransport.setAdapter(transportAdapter);
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.fab_assign_buses:
                show_dialog_assing_bus();
                break;

            case R.id.fab_assign_routes:
                show_dialog_assing_routes();
                break;
        }

    }

    Spinner spinner_bus, spinner_route, spinner_maid, spinner_driver;
    Button btn_assing;

    Spn_Adapter_User maidAdapter, driverAdapter;
    Spn_Adapter_Bus busAdapter;

    Dialog dialogProduct;

    void show_dialog_assing_bus() {
        dialogProduct = new Dialog(this);
        dialogProduct.setContentView(R.layout.activity_assign__driver__maid);
        dialogProduct.setTitle("Link drivers, maids to route");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogProduct.getWindow();
        lp.copyFrom(window.getAttributes());

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialogProduct.show();

        spinner_bus = dialogProduct.findViewById(R.id.spinner_bus);
        busAdapter = new Spn_Adapter_Bus(dialogProduct.getContext(), R.layout.spinner_bus, busesBeanArrayList);
        spinner_bus.setAdapter(busAdapter);

        spinner_driver = dialogProduct.findViewById(R.id.spinner_driver);
        driverAdapter = new Spn_Adapter_User(dialogProduct.getContext(), R.layout.spinner_maid_driver, init_driver_list(this));
        spinner_driver.setAdapter(driverAdapter);

        spinner_maid = dialogProduct.findViewById(R.id.spinner_maid);
        maidAdapter = new Spn_Adapter_User(dialogProduct.getContext(), R.layout.spinner_maid_driver, init_maid_list(this));
        spinner_maid.setAdapter(maidAdapter);

        btn_assing = dialogProduct.findViewById(R.id.btn_assign_bus);

        btn_assing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialogProduct.dismiss();
                if(busesBeanArrayList.size()>0){
                    if (AttUtil.isNetworkConnected(TransportActivity.this))
                        insertBusDriver();
                    /*else
                        retrieveNetConnect();*/
                }else
                    Toast.makeText(TransportActivity.this,"No bus to link driver and maid with.",Toast.LENGTH_SHORT);
            }
        });

        spinner_bus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinner_selected_bus = i;
                busId = busesBeanArrayList.get(i).getBusId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_maid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                maidId = init_maid_list(TransportActivity.this).get(i).getUserId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_driver.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                driverId = init_driver_list(TransportActivity.this).get(i).getUserId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

     void insertBusDriver() {
    }

    void show_dialog_assing_routes() {
        dialogProduct = new Dialog(this);
        dialogProduct.setContentView(R.layout.activity_assign__routes__buses);
        dialogProduct.setTitle("Link route to bus");

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogProduct.getWindow();
        lp.copyFrom(window.getAttributes());

        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        dialogProduct.show();

        spinner_bus = dialogProduct.findViewById(R.id.spinner_bus);
        busAdapter = new Spn_Adapter_Bus(dialogProduct.getContext(), R.layout.spinner_bus,
                busesBeanArrayList);
        spinner_bus.setAdapter(busAdapter);

        spinnerRoutesBeanArrayList = new ArrayList<>(TransportActivity.routesBeanHashMap.values());
        spinner_route = dialogProduct.findViewById(R.id.spinner_route);
        Spn_Adapter_Route adapterRoute = new Spn_Adapter_Route(dialogProduct.getContext(), R.layout.spinner_routes,
                spinnerRoutesBeanArrayList);
        spinner_route.setAdapter(adapterRoute);

        btn_assing = dialogProduct.findViewById(R.id.btn_assign_route);

        btn_assing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                dialogProduct.dismiss();
                if(spinnerRoutesBeanArrayList.size()>0||busesBeanArrayList.size()>0){
                    if (AttUtil.isNetworkConnected(TransportActivity.this))
                        insertRouteBus();
                    /*else
                        retrieveNetConnect();*/
                }else{
                    Toast.makeText(TransportActivity.this,"Invalid data",Toast.LENGTH_SHORT);
                }
            }
        });


        spinner_bus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                busId = busesBeanArrayList.get(i).getBusId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                routeId = routesBeanArrayList.get(i).getRouteId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

     void insertRouteBus() {

    }

}
