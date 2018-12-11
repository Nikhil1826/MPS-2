package com.auribisesmyplayschool.myplayschool.adminApp.fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.auribisesmyplayschool.myplayschool.Helper.Constants;
import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.activity.RouteBusesActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.adapter.BusesAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.BusesBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.auribisesmyplayschool.myplayschool.classes.RecyclerItemClickListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import dmax.dialog.SpotsDialog;

public class BusesFragment extends Fragment

{
    static BusesFragment fragment = new BusesFragment();
    static Context context;

    RecyclerView rvBuses;
    View view , em_view;

    BusesAdapter busesAdapter;
    BusesBean posBusBean;

    private int responseSignal=0 ,reqCode = 0;
    int pos ,pos_activate =1,busId;
    FirebaseFirestore db;
    ArrayList<Integer> listId;



    public BusesFragment()
    {
        // Required empty public constructor
    }

    public static BusesFragment newInstance(Context context1)
    {
        context = context1;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.admin_fragment_buses, container, false);
        initViews();

//        if(RouteBusesActivity.busesBeansArrayList.isEmpty())
//            empty_buses();


        rvBuses.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(View view, int position)
                    {
                        pos =position;
                        showOptions();

//                        Toast.makeText(context,"on click",Toast.LENGTH_LONG).show();

                    }
                })

        );



       return  view;
    }

    void initViews()
    {
//        FloatingActionMenu menu =(FloatingActionMenu)view.findViewById(R.id.fab_bus);
//        if(menu.isOpened()){
//            menu.close(true);
//        }
        listId = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        em_view = view.findViewById(R.id.em_bus);
        rvBuses = view.findViewById(R.id.lvFragmentBuses);

        // init recycler view
        rvBuses.setLayoutManager(new LinearLayoutManager(rvBuses.getContext()));
        rvBuses.setItemAnimator(new DefaultItemAnimator());

    }

   public void showOptions()
    {
        posBusBean = RouteBusesActivity.busesBeansArrayList.get(pos);

        AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
        String text;

        if(posBusBean.getActivate()==0)
            text="Activate";
        else
            text="Deactivate";

        String[] options = {"View","Update",text};

//        for (String i : options) {
//
//            Log.i("show", i.toString());
//        }


        build.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        showBusDialog();
                        break;

                    case 1:
                        updateMode = true;
                        showInsertDialogBus();
                        break;
                    case 2:
                        if (AdminUtil.isNetworkConnected(getActivity())){
                            AttUtil.progressDialog((Activity) getContext());
                            AttUtil.pd(1);
                            start_deactivate();
                        }
                        else
                            activateNetConnect();
                        break;
                }
            }
        });

        build.create().show();
    }


    void empty_buses()
    {
        em_view.setVisibility(View.VISIBLE);
        rvBuses.setVisibility(View.GONE);
    }

    public void init_list()
    {
        Log.i("show","init list");


        busesAdapter = new BusesAdapter(RouteBusesActivity.busesBeansArrayList,context);
        rvBuses.setAdapter(busesAdapter);

//        for (BusesBean i : RouteBusesActivity.busesBeansArrayList)
//        {
//            Log.i("show",i.toString());
//        }
    }


   public void showBusDialog()
    {
        final Dialog dialogProduct;
        dialogProduct = new Dialog(getActivity());
        dialogProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProduct.setContentView(R.layout.admin_dialog_show_buses_info);
        dialogProduct.show();


        TextView txtProductCode = (TextView)dialogProduct.findViewById(R.id.productCode);
        TextView txtProductCategory= (TextView)dialogProduct.findViewById(R.id.productCategory);
        TextView txtProductDesc = (TextView)dialogProduct.findViewById(R.id.productDesc);
        TextView txtClose = (TextView)dialogProduct.findViewById(R.id.txtClose);

        txtProductCode.setText(String.valueOf(posBusBean.getBusId()));
        txtProductCategory.setText(posBusBean.getBusNumber());
        txtProductDesc.setText(posBusBean.getBusDesc());

        txtClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                dialogProduct.dismiss();
            }
        });
    }

    void start_deactivate()
    {
        if(posBusBean.getActivate()==0){
            posBusBean.setActivate(1);
            db.collection(Constants.buses_collection).document(String.valueOf(posBusBean.getBusId()))
                    .update("activate",posBusBean.getActivate()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AttUtil.pd(0);
                    Toast.makeText(context,"Status Changed",Toast.LENGTH_SHORT).show();
                    busesAdapter.notifyDataSetChanged();
                }
            });
        }else{
            posBusBean.setActivate(0);
            db.collection(Constants.buses_collection).document(String.valueOf(posBusBean.getBusId()))
                    .update("activate",posBusBean.getActivate()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AttUtil.pd(0);
                    Toast.makeText(context,"Status Changed",Toast.LENGTH_SHORT).show();
                    busesAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    void activateNetConnect()
    {

    }


    Dialog dialogProduct;
    public static boolean updateMode = false;
    EditText edBusName;
    EditText edBusDesc;
    BusesBean busesBean;

  public  void showInsertDialogBus()
    {
        dialogProduct = new Dialog(getActivity());
        dialogProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogProduct.setContentView(R.layout.admin_dialog_insert_bus);
        dialogProduct.show();

        edBusName = dialogProduct.findViewById(R.id.ed_bus_number);
        edBusDesc = (EditText)dialogProduct.findViewById(R.id.ed_bus_description);
        Button btnUpdate = (Button)dialogProduct.findViewById(R.id.btn_insert_bus);


        if(!updateMode){
            busesBean = new BusesBean();
        }


        if(updateMode)
        {
            btnUpdate.setText("Update");
            edBusName.setText(posBusBean.getBusNumber());
            edBusDesc.setText(posBusBean.getBusDesc());
        }



        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AttUtil.progressDialog((Activity) getContext());
                AttUtil.pd(1);
                if(validation())
                {
                    if(updateMode){
                        posBusBean.setBusNumber(edBusName.getText().toString());
                        posBusBean.setBusDesc(edBusDesc.getText().toString());
                        insertUpdateBus();
                    }else {
                        busesBean.setBusNumber(edBusName.getText().toString());
                        busesBean.setBusDesc(edBusDesc.getText().toString());
                        busesBean.setBranchId(RouteBusesActivity.branch_id);
                        busesBean.setActivate(1);

                        if (RouteBusesActivity.busesBeansArrayList.size()!=0){
                            getLastBusId();
                        }else {
                            busesBean.setBusId(1);
                            insertUpdateBus();
                        }
                    }
                }else {
                    AttUtil.pd(0);
                }

            }
        });

    }

     void getLastBusId() {
         db.collection(Constants.buses_collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
             @Override
             public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                 for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                     if(queryDocumentSnapshots.size()>0){
                         int chkId = doc.getLong("busId").intValue();
                         listId.add(chkId);
                     }else{
                         busId = 0;
                     }
                 }

                 if(listId.size()>0){
                     busId = Collections.max(listId);
                 }else{
                     busId = 0;
                 }
                 busesBean.setBusId(busId+1);
                 insertUpdateBus();
             }
         });
    }

    void insertUpdateBus()
    {
        if(updateMode){
            db.collection(Constants.buses_collection).document(String.valueOf(posBusBean.getBusId())).set(posBusBean)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            updateMode = false;
                            AttUtil.pd(0);
                            dialogProduct.dismiss();
                            Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show();
                            busesAdapter.notifyDataSetChanged();
                        }
                    });
        }else {
            db.collection(Constants.buses_collection).document(String.valueOf(busesBean.getBusId())).set(busesBean)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            AttUtil.pd(0);
                            listId.clear();
                            dialogProduct.dismiss();
                            Toast.makeText(context,"New bus "+busesBean.getBusNumber()+" added.",Toast.LENGTH_SHORT).show();
                            RouteBusesActivity.busesBeansArrayList.add(busesBean);
                            busesAdapter.notifyDataSetChanged();
                        }
                    });
        }

    }


    boolean validation()
    {
        boolean flag = true;

        if(edBusName.getText().toString().isEmpty())
        {
            edBusName.setError("Enter Bus Number");
            flag=false;
        }
        if(edBusDesc.getText().toString().isEmpty())
        {
            edBusDesc.setError("Enter Bus Description");
            flag=false;
        }

        return flag;
    }




}
