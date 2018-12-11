package com.auribisesmyplayschool.myplayschool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.auribisesmyplayschool.myplayschool.R;
import com.auribisesmyplayschool.myplayschool.adminApp.bean.RoutesBean;
import com.auribisesmyplayschool.myplayschool.bean.FeeCostBean;
import com.auribisesmyplayschool.myplayschool.classes.PayFeeInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Amandeep on 26-Jul-17.
 */

public class FeeAdapterThree extends RecyclerView.Adapter<FeeAdapterThree.CustomViewHolder> {
    private ArrayList<FeeCostBean> feeCostBeanArrayList;
    private Context context;
    private int signal = 0, transportationSignal = 0;
    PayFeeInterface PayFeeInterface;
    View myview;
    SimpleDateFormat formatter;
    boolean isTransportation = false;
    ArrayList<RoutesBean> routesBeanArrayList;

    public FeeAdapterThree(@NonNull Context context, @NonNull ArrayList<FeeCostBean> objects, PayFeeInterface PayFeeInterface) {
        this.context = context;
        feeCostBeanArrayList = objects;
        this.PayFeeInterface = PayFeeInterface;
    }

    public FeeAdapterThree(@NonNull Context context, @NonNull ArrayList<FeeCostBean> objects, int i, PayFeeInterface PayFeeInterface) {
        this.context = context;
        feeCostBeanArrayList = objects;
        this.PayFeeInterface = PayFeeInterface;
        signal = i;
        formatter = new SimpleDateFormat("yyyy-MM-dd");
    }

    public FeeAdapterThree(Context context, ArrayList<FeeCostBean> transactionFeeHeadList, int i, PayFeeInterface PayFeeInterface, boolean b, ArrayList<RoutesBean> routesBeanArrayList) {
        this.context = context;
        feeCostBeanArrayList = transactionFeeHeadList;
        this.PayFeeInterface = PayFeeInterface;
        isTransportation = b;
        signal = i;
        this.routesBeanArrayList = routesBeanArrayList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isTransportation) {
            myview = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_transport_listitem, parent, false);
        } else {
            myview = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_fee_type_list, parent, false);
        }
        return new CustomViewHolder(myview);
    }

    @SuppressLint({"ResourceAsColor", "WrongConstant"})
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int position) {
        if (signal == 1 && isTransportation) {
            if (routesBeanArrayList.size() == 0) {
                routesBeanArrayList.clear();
                routesBeanArrayList.add(new RoutesBean("No Route", 0, "No description", 0, 0));
            }

            holder.editTextCredit.setVisibility(View.GONE);
            holder.tvHeadName.setText(feeCostBeanArrayList.get(position).getHeadName());
            Spn_Adapter_Route adapterRoute = new Spn_Adapter_Route(context, R.layout.spinner_routes,
                    routesBeanArrayList);
            holder.spnRouteName.setAdapter(adapterRoute);
            if (!feeCostBeanArrayList.get(position).isChecked()) {
                holder.editTextSelling.setText(feeCostBeanArrayList.get(position).getCost() + "");
                holder.editTextSelling.setEnabled(false);
                holder.cbFeeEntity.setChecked(false);
            } else {
                holder.editTextSelling.setText(feeCostBeanArrayList.get(position).getSellingAmount() + "");
                holder.editTextSelling.setEnabled(true);
                holder.cbFeeEntity.setChecked(true);
            }

            holder.editTextSelling.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (holder.editTextSelling.getText().toString().trim().equals("0") ||
                            holder.editTextSelling.getText().toString().trim().length() == 0) {
                        holder.editTextSelling.setError("Invalid");
                        feeCostBeanArrayList.get(position).setSellingAmount(0);
                    } else {
                        feeCostBeanArrayList.get(position).setSellingAmount(Integer.parseInt(holder.editTextSelling.getText().toString().trim()));
                    }

                    PayFeeInterface.setSelectedAmount();
                }
            });


            holder.cbFeeEntity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (routesBeanArrayList.size() > 0) {
                        if (b) {
                            feeCostBeanArrayList.get(position).setChecked(true);
                            holder.editTextSelling.setEnabled(true);
                            if (holder.editTextSelling.getText().toString().trim().length() == 0) {
                                feeCostBeanArrayList.get(position).setDiscount(0);
                                holder.editTextSelling.setText(feeCostBeanArrayList.get(position).getCost() + "");
                                feeCostBeanArrayList.get(position).setSellingAmount(feeCostBeanArrayList.get(position).getCost());
                            } else {
                                feeCostBeanArrayList.get(position).setDiscount(0);
                                feeCostBeanArrayList.get(position).setSellingAmount(Integer.parseInt(holder.editTextSelling.getText().toString().trim()));
                            }
                            PayFeeInterface.feeHeadCheck(1);

                        } else {
                            feeCostBeanArrayList.get(position).setChecked(false);
                            feeCostBeanArrayList.get(position).setDiscount(0);
                            feeCostBeanArrayList.get(position).setSellingAmount(feeCostBeanArrayList.get(position).getCost());
                            holder.editTextSelling.setEnabled(false);
                            holder.editTextSelling.setText(0 + "");
                            holder.editTextSelling.setError(null);
                            PayFeeInterface.feeHeadCheck(0);
                        }
                        PayFeeInterface.setSelectedAmount();
                    } else {
                        Toast.makeText(context, "No route to link student with. Define route first to use this service.", Toast.LENGTH_SHORT);
                    }
                }
            });
            holder.spnRouteName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int p, long id) {
                    feeCostBeanArrayList.get(position).setRouteId(routesBeanArrayList.get(p).getRouteId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else if (signal == 2 && isTransportation) {
            holder.editTextCredit.setVisibility(View.GONE);
            if (routesBeanArrayList.size() == 0) {
                routesBeanArrayList.clear();
                routesBeanArrayList.add(new RoutesBean("No Route", 0, "No description", 0, 0));
            }
            Spn_Adapter_Route adapterRoute = new Spn_Adapter_Route(context, R.layout.spinner_routes,
                    routesBeanArrayList);
            holder.spnRouteName.setAdapter(adapterRoute);
            holder.spnRouteName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int p, long id) {
                    feeCostBeanArrayList.get(position).setRouteId(routesBeanArrayList.get(p).getRouteId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            for (int i=0;i<routesBeanArrayList.size();i++){
                if(feeCostBeanArrayList.get(position).getRouteId() == routesBeanArrayList.get(i).getRouteId())
                    holder.spnRouteName.setSelection(i);
            }

            if (feeCostBeanArrayList.get(position).isChecked()) {
                holder.tvHeadName.setText(feeCostBeanArrayList.get(position).getHeadName());
                holder.editTextSelling.setText(feeCostBeanArrayList.get(position).getSellingCost() + "");
                holder.cbFeeEntity.setChecked(true);
                holder.editTextCredit.setText(0 + "");
                holder.editTextCredit.setEnabled(false);
            } else {
                holder.tvHeadName.setText(feeCostBeanArrayList.get(position).getHeadName());
                holder.editTextSelling.setText(feeCostBeanArrayList.get(position).getSellingCost() + "");
                holder.cbFeeEntity.setChecked(false);
                feeCostBeanArrayList.get(position).setChecked(false);
            }
            PayFeeInterface.setSelectedAmount();

            holder.cbFeeEntity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (routesBeanArrayList.size() > 0) {
                        if (b) {
                            feeCostBeanArrayList.get(position).setChecked(true);
                            holder.editTextSelling.setEnabled(true);
                            if (holder.editTextSelling.getText().toString().trim().length() == 0) {
                                holder.editTextSelling.setText("0");
//                                feeCostBeanArrayList.get(position).setSellingAmount(feeCostBeanArrayList.get(position).getSell());
                            } else {
                                feeCostBeanArrayList.get(position).setSellingCost(Integer.parseInt(holder.editTextSelling.getText().toString().trim()));
                            }

                        } else {
                            feeCostBeanArrayList.get(position).setChecked(false);
                            holder.editTextSelling.setEnabled(false);
                            holder.editTextSelling.setText(feeCostBeanArrayList.get(position).getSellingCost() + "");
                            holder.editTextSelling.setError(null);
                        }
                        PayFeeInterface.setSelectedAmount();
                    } else {
                        Toast.makeText(context, "No route to link student with. Define route first to use this service.", Toast.LENGTH_SHORT);
                    }
                }
            });

            holder.editTextSelling.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (holder.editTextSelling.getText().toString().trim().equals("0") ||
                            holder.editTextSelling.getText().toString().trim().length() == 0) {
                        holder.editTextSelling.setError("Invalid");
                        feeCostBeanArrayList.get(position).setSellingCost(0);
                    } else {
                        holder.editTextSelling.setError(null);
                        feeCostBeanArrayList.get(position).setSellingCost(Integer.parseInt(holder.editTextSelling.getText().toString().trim()));
                    }

                    PayFeeInterface.setSelectedAmount();
                }
            });


        }else if (signal == 2) {
            holder.editTextCredit.setVisibility(View.GONE);

            if (feeCostBeanArrayList.get(position).isChecked()) {
                holder.tvHeadName.setText(feeCostBeanArrayList.get(position).getHeadName());
                holder.textViewPrice.setText(feeCostBeanArrayList.get(position).getCost() + "");

                holder.editTextDiscount.setEnabled(true);
                holder.editTextDiscount.setText((feeCostBeanArrayList.get(position).getCost() -
                        feeCostBeanArrayList.get(position).getSellingCost()) + "");

                holder.textViewNetPrice.setText(feeCostBeanArrayList.get(position).getSellingCost() + "");
                holder.cbFeeEntity.setChecked(true);
                holder.editTextCredit.setText(0 + "");
                holder.editTextCredit.setEnabled(false);
            } else {
                holder.tvHeadName.setText(feeCostBeanArrayList.get(position).getHeadName());
                holder.textViewPrice.setText(feeCostBeanArrayList.get(position).getCost() + "");
                holder.editTextDiscount.setEnabled(false);
                holder.editTextDiscount.setText("0");
                holder.textViewNetPrice.setText(feeCostBeanArrayList.get(position).getCost() + "");
                holder.cbFeeEntity.setChecked(false);
                feeCostBeanArrayList.get(position).setChecked(false);
            }
            PayFeeInterface.setSelectedAmount();

            holder.cbFeeEntity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        feeCostBeanArrayList.get(position).setChecked(true);
                        holder.editTextDiscount.setEnabled(true);
                        //
                        if (holder.editTextDiscount.getText().toString().equals("0")) {
                            feeCostBeanArrayList.get(position).setDiscount(0);
                            feeCostBeanArrayList.get(position).setSellingCost(feeCostBeanArrayList.get(position).getCost());
                        } else {
                            holder.textViewNetPrice.setText("" + (feeCostBeanArrayList.get(position).getCost() -
                                    Integer.parseInt(holder.editTextDiscount.getText().toString().trim())));
                            feeCostBeanArrayList.get(position).setDiscount(Integer.parseInt(holder.editTextDiscount.getText().toString().trim()));
                            feeCostBeanArrayList.get(position).setSellingCost(Integer.parseInt(holder.textViewNetPrice.getText().toString().trim()));
                        }
                        PayFeeInterface.setSelectedAmount();

                    } else {
                        feeCostBeanArrayList.get(position).setChecked(false);
                        feeCostBeanArrayList.get(position).setDiscount(0);
                        feeCostBeanArrayList.get(position).setSellingCost(feeCostBeanArrayList.get(position).getCost());
                        feeCostBeanArrayList.get(position).setChecked(false);
                        holder.editTextDiscount.setEnabled(false);
                        holder.editTextDiscount.setText(0 + "");
                        PayFeeInterface.feeHeadCheck(0);
                    }
                    PayFeeInterface.setSelectedAmount();
                }
            });
            holder.editTextDiscount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (feeCostBeanArrayList.get(position).getCost() > 0 && holder.editTextDiscount.getText().toString().trim().length() > 0) {
                        if (Integer.parseInt(holder.editTextDiscount.getText().toString().trim()) > feeCostBeanArrayList.get(position).getCost()) {
                            holder.editTextDiscount.setError("Invalid");
                        } else {
                            holder.textViewNetPrice.setText(
                                    feeCostBeanArrayList.get(position).getCost() -
                                            Integer.parseInt(holder.editTextDiscount.getText().toString().trim()) + "");
                            feeCostBeanArrayList.get(position).setDiscount(Integer.parseInt(holder.editTextDiscount.getText().toString().trim()));
                            feeCostBeanArrayList.get(position).setSellingCost(Integer.parseInt(holder.textViewNetPrice.getText().toString().trim()));
                        }
                    } else {
                        holder.textViewNetPrice.setText(feeCostBeanArrayList.get(position).getCost() + "");
                        holder.editTextDiscount.setText("0");
                        feeCostBeanArrayList.get(position).setDiscount(Integer.parseInt(holder.editTextDiscount.getText().toString().trim()));
                        feeCostBeanArrayList.get(position).setSellingCost(Integer.parseInt(holder.textViewNetPrice.getText().toString().trim()));
                    }
                    PayFeeInterface.setSelectedAmount();
                }
            });


        } else if (signal == 3) {
            holder.tvHeadName.setText(feeCostBeanArrayList.get(position).getHeadName());

            if (feeCostBeanArrayList.get(position).getFeeType() == 1) {
                try {
                    if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                        holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                        holder.tvHeadNameDates.setText(feeCostBeanArrayList.get(position).getStartingDate());
                    }
                } catch (Exception e) {
                    holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                    holder.tvHeadNameDates.setText("Never Paid");
                }
            } else if (feeCostBeanArrayList.get(position).getFeeType() == 2) {
                try {
                    if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                        holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                        holder.tvHeadNameDates.setText(feeCostBeanArrayList.get(position).getStartingDate() + " to " +
                                feeCostBeanArrayList.get(position).getEndingDate());
                    }
                } catch (Exception e) {
                    holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                    holder.tvHeadNameDates.setText("Never Paid");
                }
            } else if (feeCostBeanArrayList.get(position).getFeeType() == 3) {
                try {
                    if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                        holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                        holder.tvHeadNameDates.setText(feeCostBeanArrayList.get(position).getStartingDate() + " to " +
                                feeCostBeanArrayList.get(position).getEndingDate());
                    }

                } catch (Exception e) {
                    holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                    holder.tvHeadNameDates.setText("Never Paid");
                }
            } else if (feeCostBeanArrayList.get(position).getFeeType() == 4) {
                try {
                    if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                        holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                        holder.tvHeadNameDates.setText(feeCostBeanArrayList.get(position).getStartingDate() + " to " +
                                feeCostBeanArrayList.get(position).getEndingDate());
                    }

                } catch (Exception e) {
                    holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                    holder.tvHeadNameDates.setText("Never Paid");
                }
            }
            feeCostBeanArrayList.get(position).setSellingCost(feeCostBeanArrayList.get(position).getSellingAmount());
            holder.textViewNetPrice.setText(feeCostBeanArrayList.get(position).getSellingCost() + "");
            holder.editTextCredit.setVisibility(View.GONE);
            holder.editTextDiscount.setVisibility(View.GONE);
            holder.textViewPrice.setVisibility(View.GONE);
            if (feeCostBeanArrayList.get(position).isChecked()) {
                holder.cbFeeEntity.setChecked(true);
            } else {
                holder.cbFeeEntity.setChecked(false);
            }
            holder.cbFeeEntity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (feeCostBeanArrayList.get(position).getFeeType() == 1) {
                        try {
                            if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                                holder.cbFeeEntity.setChecked(false);
                            }
                        } catch (Exception e) {
                            if (b) {
                                feeCostBeanArrayList.get(position).setChecked(true);
                            } else {
                                feeCostBeanArrayList.get(position).setChecked(false);
                            }
                            PayFeeInterface.setSelectedAmount();
                        }
                    } else if (feeCostBeanArrayList.get(position).getFeeType() == 2) {
                        try {
                            if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                                Date endingDate = null, currentDate = null;
                                try {
                                    endingDate = formatter.parse(feeCostBeanArrayList.get(position).getEndingDate());
                                    String StringCurrentDate = formatter.format(new Date());
                                    currentDate = formatter.parse(StringCurrentDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
//                                if (currentDate.compareTo(endingDate) > 0) {
                                if (b) {
                                    feeCostBeanArrayList.get(position).setChecked(true);
                                } else {
                                    feeCostBeanArrayList.get(position).setChecked(false);
                                }
                                PayFeeInterface.setSelectedAmount();
//                                }else{
//                                    holder.cbFeeEntity.setChecked(false);
//                                }
                            }
                        } catch (Exception e) {
                            if (b) {
                                feeCostBeanArrayList.get(position).setChecked(true);
                            } else {
                                feeCostBeanArrayList.get(position).setChecked(false);
                            }
                            PayFeeInterface.setSelectedAmount();
                        }
                    } else if (feeCostBeanArrayList.get(position).getFeeType() == 3) {
                        try {
                            if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                                Date endingDate = null, currentDate = null;
                                try {
                                    endingDate = formatter.parse(feeCostBeanArrayList.get(position).getEndingDate());
                                    String StringCurrentDate = formatter.format(new Date());
                                    currentDate = formatter.parse(StringCurrentDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //if (currentDate.compareTo(endingDate) > 0) {
                                if (b) {
                                    feeCostBeanArrayList.get(position).setChecked(true);
                                } else {
                                    feeCostBeanArrayList.get(position).setChecked(false);
                                }
                                PayFeeInterface.setSelectedAmount();
//                                }else{
//                                    holder.cbFeeEntity.setChecked(false);
//                                }
                            }
                        } catch (Exception e) {
                            if (b) {
                                feeCostBeanArrayList.get(position).setChecked(true);
                            } else {
                                feeCostBeanArrayList.get(position).setChecked(false);
                            }
                            PayFeeInterface.setSelectedAmount();
                        }
                    }else if (feeCostBeanArrayList.get(position).getFeeType() == 4) {
                        try {
                            if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                                Date endingDate = null, currentDate = null;
                                try {
                                    endingDate = formatter.parse(feeCostBeanArrayList.get(position).getEndingDate());
                                    String StringCurrentDate = formatter.format(new Date());
                                    currentDate = formatter.parse(StringCurrentDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //if (currentDate.compareTo(endingDate) > 0) {
                                if (b) {
                                    feeCostBeanArrayList.get(position).setChecked(true);
                                } else {
                                    feeCostBeanArrayList.get(position).setChecked(false);
                                }
                                PayFeeInterface.setSelectedAmount();
//                                }else{
//                                    holder.cbFeeEntity.setChecked(false);
//                                }
                            }
                        } catch (Exception e) {
                            if (b) {
                                feeCostBeanArrayList.get(position).setChecked(true);
                            } else {
                                feeCostBeanArrayList.get(position).setChecked(false);
                            }
                            PayFeeInterface.setSelectedAmount();
                        }
                    }

                }
            });


        } else if (signal == 4) {
            holder.tvHeadName.setText(feeCostBeanArrayList.get(position).getHeadName());
            if (feeCostBeanArrayList.get(position).getFeeType() == 1) {
                try {
                    if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                        holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                        holder.tvHeadNameDates.setText(feeCostBeanArrayList.get(position).getStartingDate());
                    }
                } catch (Exception e) {
                    holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                    holder.tvHeadNameDates.setText("Never Paid");
                }
            } else if (feeCostBeanArrayList.get(position).getFeeType() == 2) {
                try {
                    if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                        holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                        holder.tvHeadNameDates.setText(feeCostBeanArrayList.get(position).getStartingDate() + " to " +
                                feeCostBeanArrayList.get(position).getEndingDate());

                    }
                } catch (Exception e) {
                    holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                    holder.tvHeadNameDates.setText("Never Paid");
                }
            } else if (feeCostBeanArrayList.get(position).getFeeType() == 3) {
                try {
                    if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                        holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                        holder.tvHeadNameDates.setText(feeCostBeanArrayList.get(position).getStartingDate() + " to " +
                                feeCostBeanArrayList.get(position).getEndingDate());
                        /*feeCostBeanArrayList.get(position).setSellingCost(feeCostBeanArrayList.get(position).getNoOfMonths()*
                        feeCostBeanArrayList.get(position).getSellingCost());*/
                    }

                } catch (Exception e) {
                    holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                    holder.tvHeadNameDates.setText("Never Paid");
                }
            }else if (feeCostBeanArrayList.get(position).getFeeType() == 4) {
                try {
                    if (feeCostBeanArrayList.get(position).getStartingDate().length() != 0) {
                        holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                        holder.tvHeadNameDates.setText(feeCostBeanArrayList.get(position).getStartingDate() + " to " +
                                feeCostBeanArrayList.get(position).getEndingDate());
                       /* feeCostBeanArrayList.get(position).setSellingCost(feeCostBeanArrayList.get(position).getNoOfMonths()*
                                feeCostBeanArrayList.get(position).getSellingCost());*/
                    }

                } catch (Exception e) {
                    holder.tvHeadNameDates.setVisibility(View.VISIBLE);
                    holder.tvHeadNameDates.setText("Never Paid");
                }
            }



            /*holder.textViewNetPrice.setText(feeCostBeanArrayList.get(position).getSellingCost() + "\n"
                    + (feeCostBeanArrayList.get(position).getCreditAmount() + feeCostBeanArrayList.get(position).getCreditAmountTemp()) + "\n"
                    + (feeCostBeanArrayList.get(position).getSellingCost() - feeCostBeanArrayList.get(position).getCreditAmount()));*/

            holder.textViewNetPrice.setText(feeCostBeanArrayList.get(position).getTotalSellingCost() + "\n"
                    + (feeCostBeanArrayList.get(position).getCreditAmount() + feeCostBeanArrayList.get(position).getCreditAmountTemp()) + "\n"
                    + (feeCostBeanArrayList.get(position).getTotalSellingCost() - feeCostBeanArrayList.get(position).getCreditAmount()));
            holder.cbFeeEntity.setVisibility(View.GONE);
            holder.editTextDiscount.setVisibility(View.GONE);
            holder.textViewPrice.setVisibility(View.GONE);

            if(feeCostBeanArrayList.get(position).getTotalSellingCost() == feeCostBeanArrayList.get(position).getCreditAmount()){
                //holder.editTextCredit.setVisibility(View.GONE);
                holder.editTextCredit.setText("PAID");
                holder.editTextCredit.setBackgroundResource(R.drawable.paid);
                holder.editTextCredit.setEnabled(false);
                holder.editTextCredit.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                holder.editTextCredit.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD_ITALIC));
                holder.editTextCredit.setTextColor(Color.parseColor("#ffffff"));
            }else{
                holder.editTextCredit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (feeCostBeanArrayList.get(position).getTotalSellingCost() > 0 && holder.editTextCredit.getText().toString().trim().trim().length() > 0) {
                            if (Integer.parseInt(holder.editTextCredit.getText().toString().trim()) > feeCostBeanArrayList.get(position).getTotalSellingCost()) {
                                holder.editTextCredit.setError("Invalid");
                                feeCostBeanArrayList.get(position).setCreditAmountTemp(0);
                            } else {
                                feeCostBeanArrayList.get(position).setCreditAmountTemp(Integer.parseInt(holder.editTextCredit.getText().toString().trim()));
                                holder.textViewNetPrice.setText(feeCostBeanArrayList.get(position).getTotalSellingCost()
                                        + "\n" + (feeCostBeanArrayList.get(position).getCreditAmountTemp() + feeCostBeanArrayList.get(position).getCreditAmount())
                                        + "\n" + (feeCostBeanArrayList.get(position).getTotalSellingCost() -
                                        (feeCostBeanArrayList.get(position).getCreditAmount() + feeCostBeanArrayList.get(position).getCreditAmountTemp())));

                            }
                        } else {
                            feeCostBeanArrayList.get(position).setCreditAmountTemp(0);
                            holder.textViewNetPrice.setText(feeCostBeanArrayList.get(position).getTotalSellingCost() + "\n"
                                    + (feeCostBeanArrayList.get(position).getCreditAmount() + feeCostBeanArrayList.get(position).getCreditAmountTemp()) + "\n"
                                    + (feeCostBeanArrayList.get(position).getTotalSellingCost() - feeCostBeanArrayList.get(position).getCreditAmount()));
                            holder.editTextDiscount.setText("0");
                        }
                        PayFeeInterface.setSelectedAmount();
                    }
                });
            }


        } else {
            holder.editTextCredit.setVisibility(View.GONE);
            holder.tvHeadName.setText(feeCostBeanArrayList.get(position).getHeadName());
            holder.textViewPrice.setText(feeCostBeanArrayList.get(position).getCost() + "");
            if (!feeCostBeanArrayList.get(position).isChecked()) {
                holder.editTextDiscount.setEnabled(false);
                holder.editTextDiscount.setText("0");
                holder.textViewNetPrice.setText(feeCostBeanArrayList.get(position).getCost() + "");
                holder.cbFeeEntity.setChecked(false);
            } else {
                holder.editTextDiscount.setEnabled(true);
                holder.editTextDiscount.setText(feeCostBeanArrayList.get(position).getDiscount() + "");
                holder.textViewNetPrice.setText(feeCostBeanArrayList.get(position).getSellingAmount() + "");
                holder.cbFeeEntity.setChecked(true);
            }

            holder.cbFeeEntity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        feeCostBeanArrayList.get(position).setChecked(true);
                        holder.editTextDiscount.setEnabled(true);
                        if (holder.editTextDiscount.getText().toString().trim().length() == 0) {
                            feeCostBeanArrayList.get(position).setDiscount(0);
                            feeCostBeanArrayList.get(position).setSellingAmount(feeCostBeanArrayList.get(position).getCost());
                        } else {
                            holder.textViewNetPrice.setText("" + (feeCostBeanArrayList.get(position).getCost() -
                                    Integer.parseInt(holder.editTextDiscount.getText().toString().trim())));
                            feeCostBeanArrayList.get(position).setDiscount(Integer.parseInt(holder.editTextDiscount.getText().toString().trim()));
                            feeCostBeanArrayList.get(position).setSellingAmount(Integer.parseInt(holder.textViewNetPrice.getText().toString().trim()));
                        }
                        PayFeeInterface.feeHeadCheck(1);

                    } else {
                        feeCostBeanArrayList.get(position).setChecked(false);
                        feeCostBeanArrayList.get(position).setDiscount(0);
                        feeCostBeanArrayList.get(position).setSellingAmount(feeCostBeanArrayList.get(position).getCost());
                        feeCostBeanArrayList.get(position).setChecked(false);
                        holder.editTextDiscount.setEnabled(false);
                        holder.editTextDiscount.setText(0 + "");
                        PayFeeInterface.feeHeadCheck(0);
                    }
                    PayFeeInterface.setSelectedAmount();
                }
            });
            holder.editTextDiscount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if ((feeCostBeanArrayList.get(position).getCost() > 0 || feeCostBeanArrayList.get(position).getCost() == 0)
                            && holder.editTextDiscount.getText().toString().trim().length() > 0) {
                        if (Integer.parseInt(holder.editTextDiscount.getText().toString().trim()) > feeCostBeanArrayList.get(position).getCost()) {
                            holder.editTextDiscount.setError("Invalid");
                        } else {
                            holder.textViewNetPrice.setText(
                                    feeCostBeanArrayList.get(position).getCost() -
                                            Integer.parseInt(holder.editTextDiscount.getText().toString().trim()) + "");
                            feeCostBeanArrayList.get(position).setDiscount(Integer.parseInt(holder.editTextDiscount.getText().toString().trim()));
                            feeCostBeanArrayList.get(position).setSellingAmount(Integer.parseInt(holder.textViewNetPrice.getText().toString().trim()));
                        }
                    } else {
                        holder.textViewNetPrice.setText(feeCostBeanArrayList.get(position).getCost() + "");
                        holder.editTextDiscount.setText("0");
                        feeCostBeanArrayList.get(position).setDiscount(Integer.parseInt(holder.editTextDiscount.getText().toString().trim()));
                        feeCostBeanArrayList.get(position).setSellingAmount(Integer.parseInt(holder.textViewNetPrice.getText().toString().trim()));
                    }
                    PayFeeInterface.setSelectedAmount();
                }
            });

        }

    }



    @Override
    public int getItemCount() {
        return feeCostBeanArrayList.size();
    }




    public class CustomViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbFeeEntity;
        TextView textViewPrice, textViewNetPrice, tvHeadName, tvHeadNameDates;
        EditText editTextDiscount, editTextCredit, editTextSelling;
        //transportation
        Spinner spnRouteName;


        public CustomViewHolder(View itemView) {
            super(itemView);
            if (isTransportation) {
                tvHeadName = myview.findViewById(R.id.tvHeadName);
                editTextDiscount = myview.findViewById(R.id.editTextDiscount);
                tvHeadNameDates = myview.findViewById(R.id.tvHeadNameDates);
                spnRouteName = myview.findViewById(R.id.spnRouteName);
                editTextSelling = myview.findViewById(R.id.editTextSelling);
                editTextCredit = myview.findViewById(R.id.editTextCredit);
                cbFeeEntity = myview.findViewById(R.id.cbFeeEntity);
            } else {
                cbFeeEntity = myview.findViewById(R.id.cbFeeEntity);
                textViewPrice = myview.findViewById(R.id.textViewPrice);
                tvHeadName = myview.findViewById(R.id.tvHeadName);
                tvHeadNameDates = myview.findViewById(R.id.tvHeadNameDates);
                textViewNetPrice = myview.findViewById(R.id.textViewNetPrice);
                editTextDiscount = myview.findViewById(R.id.editTextDiscount);
                editTextCredit = myview.findViewById(R.id.editTextCredit);
            }

        }


    }

}
