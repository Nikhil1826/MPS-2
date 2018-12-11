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
import com.auribisesmyplayschool.myplayschool.activity.LoginActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.activity.RouteBusesActivity;
import com.auribisesmyplayschool.myplayschool.adminApp.adapter.RoutesAdapter;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.RoutesBean;
import com.auribisesmyplayschool.myplayschool.adminApp.classes.AdminUtil;
import com.auribisesmyplayschool.myplayschool.classes.AttUtil;
import com.auribisesmyplayschool.myplayschool.classes.MyResponseConnectivity;
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


public class RoutesFragment extends Fragment
{

    static RoutesFragment fragment = new RoutesFragment();
    static Context context;

    RecyclerView rvRoutes;
    View View , em_view;

    RoutesAdapter routesAdapter;
    RoutesBean posRouteBean;

    private int responseSignal=0 ,reqCode = 0;
    int pos_activate=0;
    int pos;
    FirebaseFirestore db;
    int routeId;
    ArrayList<Integer> listId;

    public RoutesFragment()
    {
        // Required empty public constructor
    }

    public static RoutesFragment newInstance(Context context1)
    {
        context = context1;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View = inflater.inflate(R.layout.admin_fragment_routes, container, false);
        initViews();


//        if(RouteBusesActivity.routesBeansArrayList.isEmpty())
//            empty_routes();


        rvRoutes.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
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

        return View;
    }

    void initViews()
    {
//        FloatingActionMenu menu =(FloatingActionMenu)View.findViewById(R.id.fab_route);
//        if(menu.isOpened())
//        {
//            menu.close(true);
//        }
        listId = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        em_view = View.findViewById(R.id.em_routes);
        rvRoutes = View.findViewById(R.id.lvFragmentRoutes);

        // init recycler view
        rvRoutes.setLayoutManager(new LinearLayoutManager(rvRoutes.getContext()));
        rvRoutes.setItemAnimator(new DefaultItemAnimator());

    }

    void empty_routes()
    {
        em_view.setVisibility(View.VISIBLE);
        rvRoutes.setVisibility(View.GONE);
    }

    public void init_list()
    {

        Log.i("show","init list");
        routesAdapter = new RoutesAdapter(RouteBusesActivity.routesBeansArrayList,context);

        rvRoutes.setAdapter(routesAdapter);

        for (RoutesBean i : RouteBusesActivity.routesBeansArrayList)
        {
            Log.i("show",i.toString());
        }
    }

    public void showOptions()
    {
        String text;

        posRouteBean = RouteBusesActivity.routesBeansArrayList.get(pos);

        if(posRouteBean.getActivate()==0)
            text="Activate";
        else
            text="Deactivate";

        String[] options = {"View","Update",text};

        //        for (String i : options) {
//
//            Log.i("show", i.toString());
//        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setItems(options, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                switch (which)
                {
                    case 0:
                    showRouteDialog();
                    break;
                    case 1:
                        updateMode = true;
                        showInsertDialogRoute();
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
        builder.create().show();



    }


   void showRouteDialog()
    {
        final Dialog dialogProduct;
        dialogProduct = new Dialog(getActivity());
        dialogProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogProduct.setContentView(R.layout.admin_dialog_show_routes_info);
        dialogProduct.show();

        TextView txtProductCode = (TextView)dialogProduct.findViewById(R.id.productCode);
        TextView txtProductCategory= (TextView)dialogProduct.findViewById(R.id.productCategory);
        TextView txtProductDesc = (TextView)dialogProduct.findViewById(R.id.productDesc);
        TextView txtClose = (TextView)dialogProduct.findViewById(R.id.txtClose);

        txtProductCode.setText(String.valueOf(posRouteBean.getRouteId()));
        txtProductCategory.setText(posRouteBean.getRouteName());
        txtProductDesc.setText(posRouteBean.getRouteDesc());

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
        if(posRouteBean.getActivate()==0){
            posRouteBean.setActivate(1);
            db.collection(Constants.routes_collection).document(String.valueOf(posRouteBean.getRouteId()))
                    .update("activate",posRouteBean.getActivate()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AttUtil.pd(0);
                     Toast.makeText(context,"Status Changed",Toast.LENGTH_SHORT).show();
                     routesAdapter.notifyDataSetChanged();
                }
            });
        }else{
            posRouteBean.setActivate(0);
            db.collection(Constants.routes_collection).document(String.valueOf(posRouteBean.getRouteId()))
                    .update("activate",posRouteBean.getActivate()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    AttUtil.pd(0);
                    Toast.makeText(context,"Status Changed",Toast.LENGTH_SHORT).show();
                    routesAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    void activateNetConnect()
    {

    }


    Dialog dialogProduct;
    public static boolean updateMode = false;
    EditText edRouteName;
    EditText edRouteDesc;
    RoutesBean routesBean;

  public  void showInsertDialogRoute()
    {
        try {
            Log.i("test","1");
            dialogProduct = new Dialog(getActivity());
            Log.i("test","2");
            dialogProduct.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialogProduct.setContentView(R.layout.admin_dialog_insert_route);
            dialogProduct.show();

            edRouteName= (EditText)dialogProduct.findViewById(R.id.ed_route_name);
            edRouteDesc = (EditText)dialogProduct.findViewById(R.id.ed_route_desc);
            Button btnUpdate = (Button)dialogProduct.findViewById(R.id.btn_insert_route);

            if(!updateMode){
                routesBean = new RoutesBean();
            }


            if(updateMode)
            {
                btnUpdate.setText("Update");
                edRouteName.setText(posRouteBean.getRouteName());
                edRouteDesc.setText(posRouteBean.getRouteDesc());
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
                            posRouteBean.setRouteName(edRouteName.getText().toString());
                            posRouteBean.setRouteDesc(edRouteDesc.getText().toString());
                            //posRouteBean.setActivate(posRouteBean.getActivate());
                            insertUpdateRoute();
                        }else {
                            routesBean.setRouteName(edRouteName.getText().toString());
                            routesBean.setRouteDesc(edRouteDesc.getText().toString());
                            routesBean.setBranchId(RouteBusesActivity.branch_id);
                            routesBean.setActivate(1);

                            if (RouteBusesActivity.routesBeansArrayList.size()!=0){
                                getLastRouteId();
                            }else {
                                routesBean.setRouteId(1);
                                insertUpdateRoute();
                            }

                        }
                    }else {
                        AttUtil.pd(0);
                    }

                }
            });
        }catch (Exception e){
            Log.i("test",e.toString());
        }

    }

     void getLastRouteId() {
      db.collection(Constants.routes_collection).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
          @Override
          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
               for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()){
                   if(queryDocumentSnapshots.size()>0){
                       int chkId = doc.getLong("routeId").intValue();
                       listId.add(chkId);
                   }else{
                       routeId = 0;
                   }
               }

               if(listId.size()>0){
                   routeId = Collections.max(listId);
               }else{
                   routeId = 0;
               }
               routesBean.setRouteId(routeId+1);
               insertUpdateRoute();
          }
      });
    }


    void insertUpdateRoute()
    {
        if(updateMode){
            db.collection(Constants.routes_collection).document(String.valueOf(posRouteBean.getRouteId())).set(posRouteBean)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            updateMode = false;
                            AttUtil.pd(0);
                            dialogProduct.dismiss();
                            Toast.makeText(context,"Updated",Toast.LENGTH_SHORT).show();
                            routesAdapter.notifyDataSetChanged();
                        }
                    });
        }else {
            db.collection(Constants.routes_collection).document(String.valueOf(routesBean.getRouteId())).set(routesBean)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            AttUtil.pd(0);
                            dialogProduct.dismiss();
                            Toast.makeText(context,"New route "+routesBean.getRouteName()+" added.",Toast.LENGTH_SHORT).show();
                            listId.clear();
                            RouteBusesActivity.routesBeansArrayList.add(routesBean);
                            routesAdapter.notifyDataSetChanged();
                        }
                    });
        }

    }


    boolean validation()
    {
        boolean flag = true;

        if(edRouteName.getText().toString().isEmpty())
        {   flag=false;
            edRouteName.setError("Enter Route Name");

        }
        if(edRouteDesc.getText().toString().isEmpty())
        {
            flag=false;
            edRouteDesc.setError("Enter Route Description");

        }

        return flag;
    }

}
