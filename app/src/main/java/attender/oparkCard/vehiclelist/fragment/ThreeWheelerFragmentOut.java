package attender.oparkCard.vehiclelist.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import attender.oparkCard.R;
import attender.oparkCard.base.AppConstants;
import attender.oparkCard.base.AppController;
import attender.oparkCard.base.ClickListener;
import attender.oparkCard.base.DividerItemDecoration;
import attender.oparkCard.base.RecyclerTouchListener;
import attender.oparkCard.subscription.model.CustomRequest;
import attender.oparkCard.vehiclelist.activity.WebViewPrint;
import attender.oparkCard.vehiclelist.adapter.ThreeFragmentOutAdapter;
import attender.oparkCard.vehiclelist.model.VehicleModelDetails;


public class ThreeWheelerFragmentOut extends Fragment {
    private ThreeFragmentOutAdapter adapter;
    private RecyclerView recyclerViewVehicle;
    private ArrayList<VehicleModelDetails> vehicleModelList = new ArrayList<>();
    protected ProgressDialog progressDialog;
    SharedPreferences sharedpref;
    String agentId, userRole, userName, userContactNo, vendorId, vendorName, parkingName, parkingType, parkingId,
            parkingVehicle, parkingType1234, parkingTypeTwo, pType2, pType4;
    String[] pType = new String[3];

    public ThreeWheelerFragmentOut() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_in, container, false);

        findVievId(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppConstants.isInternetAvailable(getActivity())) {
            if (pType.length == 2) {
                // getDetails(parkingId, pType2, "checkInList");
                getDetails(parkingId, "3Wheeler", "checkOutList");

            } else {
                getDetails(parkingId, "3Wheeler", "checkOutList");

            }
            // getDetails(parkingId, parkingType1234, "checkOutList");
        } else {
            Toast.makeText(getActivity(), "Internet Connection Required", Toast.LENGTH_LONG).show();
        }

    }

    public void findVievId(View view) {

        sharedpref = getActivity().getSharedPreferences("opark", Context.MODE_PRIVATE);
        agentId = sharedpref.getString("agentId", "");
        userRole = sharedpref.getString("userRole", "");
        userName = sharedpref.getString("userName", "");
        userContactNo = sharedpref.getString("userContactNo", "");
        vendorId = sharedpref.getString("vendorId", "");
        vendorName = sharedpref.getString("vendorName", "");
        parkingName = sharedpref.getString("parkingName", "");
        parkingType = sharedpref.getString("getParkingType", "");
        parkingId = sharedpref.getString("parkingId", "");
        parkingTypeTwo = sharedpref.getString("towWheeler", "");
        parkingVehicle = sharedpref.getString("parkingVehicle", "");
        parkingType1234 = sharedpref.getString("VehicleType1", "");

        final String mode = "checkOut";
        pType = parkingType.toString().split(",");

        if (pType.length == 1) {
            parkingType1234 = sharedpref.getString("VehicleType1", "");
        }


        if (pType.length == 2) {
            pType2 = pType[0];
            pType4 = pType[1];
        }

        recyclerViewVehicle = (RecyclerView) view.findViewById(R.id.recyclerViewReview);
        recyclerViewVehicle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewVehicle.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.horizontal_divider_gray)));
        recyclerViewVehicle.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerViewVehicle, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {

//                Toast.makeText(CarInActivity.this, vehicleModelList.get(position).getTransactionId(), Toast.LENGTH_SHORT).show();
                try {

                    if (AppConstants.isInternetAvailable(getActivity())) {
                        if (pType.length == 2) {
                            // getDetails(parkingId, pType2, "checkInList");
                          //  getDetails(parkingId, "3Wheeler", "checkOutList");
                         getReceiptWeb(parkingId, "3Wheeler", mode, vehicleModelList.get(position).getTransactionId(),agentId );

                        } else {
                            getReceiptWeb(parkingId, "3Wheeler", mode, vehicleModelList.get(position).getTransactionId(),agentId );

                        }
                        // getReceiptWeb(parkingId, parkingType1234, mode, vehicleModelList.get(position).getTransactionId());
                    } else {
                        Toast.makeText(getActivity(), "Internet Connection Required", Toast.LENGTH_LONG).show();
                    }


                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public void getDetails(String parkingId, String parkingType, String mode) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        /*http://staggingapi.opark.in/index.php/v1/parking/vehicle_list?parkingId=1&parkingType=4Wheeler&mode=checkInList*/

        String urlData = AppConstants.BASEURL + "parking/vehicle_list?parkingId=" + parkingId + "&parkingType=" + parkingType + "&mode=checkOutList";
        Log.e("TAG", "getDetails: "+urlData);
        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {

                vehicleModelList.clear();
                try {
                    System.out.println("JSON RETURN " + json.toString());


                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");
                    //  int detail = json.getInt("detail");

                    if (status == 0) {

                        final JSONArray arrayObj = new JSONArray(data);


                        //   Toast.makeText(DetailActivity.this, message, Toast.LENGTH_SHORT).show();

                        for (int i = 0; i < arrayObj.length(); i++) {

                            JSONObject jsonObject = arrayObj.getJSONObject(i);

                            String transactionId = jsonObject.getString("transactionId");
                            String parkingName = jsonObject.getString("parkingName");
                            String checkInDateTime = jsonObject.getString("checkInDateTime");
                            String checkOutDateTime = jsonObject.getString("checkOutDateTime");
                            String agentName = jsonObject.getString("agentName");
                            String parkingType = jsonObject.getString("parkingType");
                            String vehicleNo = jsonObject.getString("vehicleNo");

                            //   Toast.makeText(DetailActivity.this, transactionId +"/n"+ parkingName +"/n"+checkInDateTime +"/n"+vehicleNo+mobileNo, Toast.LENGTH_SHORT).show();

                            VehicleModelDetails vehicleModelDetails = new VehicleModelDetails();

                            vehicleModelDetails.setTransactionId(transactionId);
                            vehicleModelDetails.setParkingName(parkingName);
                            vehicleModelDetails.setCheckInDateTime(checkInDateTime);
                            vehicleModelDetails.setCheckOutDateTime(checkOutDateTime);
                            vehicleModelDetails.setAgentName(agentName);
                            vehicleModelDetails.setParkingType(parkingType);
                            vehicleModelDetails.setVehicleNo(vehicleNo);

                            vehicleModelList.add(vehicleModelDetails);
                        }
                        adapter = new ThreeFragmentOutAdapter(ThreeWheelerFragmentOut.this, vehicleModelList);
                        recyclerViewVehicle.setAdapter(adapter);


                        pDialog.dismiss();


                    } else {
                        //  Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/vehicle_list?parkingId=");
                    Toast.makeText(getActivity(), "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/vehicle_list?parkingId=");
                    Toast.makeText(getActivity(), "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response error", error + "");
                        sendError(error.toString(), "parking/vehicle_list?parkingId=");
                        Toast.makeText(getActivity(), "Internet Connection Required", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);
        pDialog.dismiss();

    }

    public void getReceiptWeb(String parkingId, final String parkingType, String mode, String transactionId,String agentId ) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        String urlData = AppConstants.BASEURL + "parking/receipt?parkingId=" + parkingId + "&parkingType=" + parkingType + "&mode=" + mode + "&transactionId=" + transactionId+"&agentId="+agentId  ;

        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {

                vehicleModelList.clear();
                try {
                    System.out.println("JSON " + "ETURN " + json.toString());


                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");

                    if (status == 0) {

                        final JSONObject arrayObj = new JSONObject(data);

                        String transactionId = arrayObj.getString("transactionId");
                        String receiptHeading = arrayObj.getString("receiptHeading");
                        String parkingAddress = arrayObj.getString("parkingAddress");
                        String vehicleNo1 = arrayObj.getString("vehicleNo");
                        String checkInDate = arrayObj.getString("checkInDate");
                        String checkOutDate = arrayObj.getString("checkOutDate");
                        String checkInTime = arrayObj.getString("checkInTime");
                        String checkOutTime = arrayObj.getString("checkOutTime");
                        String duration = arrayObj.getString("duration");
                        String durationUnit = arrayObj.getString("durationUnit");
                        String grandTotal = arrayObj.getString("grandTotal");
                        String currencySymbol = arrayObj.getString("currencySymbol");
                        String userContactNo = arrayObj.getString("userContactNo");
                        String agentId = arrayObj.getString("agentId");
                        String availableSlots = arrayObj.getString("availableSlots");
                        String parkingId = arrayObj.getString("parkingId");
                        String parkingRate = arrayObj.getString("parkingRate");
                        String tax = arrayObj.getString("tax");
                        String total = arrayObj.getString("total");
                        String barcode = arrayObj.getString("barcode");
                        String mode = arrayObj.getString("mode");
                        String responseType = arrayObj.getString("responseType");
                        String minimumAmount = arrayObj.getString("minimumAmount");
                        String parkingName = arrayObj.getString("parkingName");
                        String receipt = arrayObj.getString("receipt");
                        String companyWebsite = arrayObj.getString("companyWebsite");
                        String poweredBy = arrayObj.getString("poweredBy");
                        String receiptType = arrayObj.getString("receiptType");
                        String receiptNo = arrayObj.getString("receiptNo");
                        String parkingType = arrayObj.getString("parkingType");
                        String reprintText = arrayObj.getString("reprintText");


                        SharedPreferences storeAllValues = getActivity().getSharedPreferences("opark", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storeAllValues.edit();

                        editor.putString("parkingPrint", "checkOUTModel");
                        editor.apply();
                        editor.commit();
                        Intent intentWeb = new Intent(getActivity(), WebViewPrint.class);

                        intentWeb.putExtra("receiptHeading", receiptHeading);
                        intentWeb.putExtra("parkingAddress", parkingAddress);
                        intentWeb.putExtra("vehicleNo1", vehicleNo1);
                        intentWeb.putExtra("checkInDate", checkInDate);
                        intentWeb.putExtra("checkOutDate", checkOutDate);
                        intentWeb.putExtra("duration", duration);
                        intentWeb.putExtra("durationUnit", durationUnit);
                        intentWeb.putExtra("currencySymbol", currencySymbol);
                        intentWeb.putExtra("grandTotal", grandTotal);
                        intentWeb.putExtra("poweredBy", poweredBy);
                        intentWeb.putExtra("receiptNo", receiptNo);
                        intentWeb.putExtra("companyWebsite", companyWebsite);
                        intentWeb.putExtra("receipt", receipt);
                        intentWeb.putExtra("reprintText", reprintText);
                        getActivity().startActivity(intentWeb);


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/receipt?parkingId=");
                    Toast.makeText(getActivity(), "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendError(e.toString(), "parking/receipt?parkingId=");
                    Toast.makeText(getActivity(), "Technical Error...", Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }
        },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendError(error.toString(), "parking/receipt?parkingId=");
                        Log.d("Response error", error + "");
                        Toast.makeText(getActivity(), "Internet Connection Required", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);

    }

    private void sendError(String e, String apiName) {

        //https://opark.in/O_par_aPi/gwaliorStg/index.php/v3/error/add
        String url = AppConstants.BASEURL + AppConstants.APIERROR;
        Map<String, String> parameterData = new HashMap<>();
        parameterData.put(("error"), e.toString());
        parameterData.put(("apiName"), apiName);

        if (AppConstants.isInternetAvailable(getActivity())) {
            send(url, parameterData);
        } else {
            Toast.makeText(getActivity(), "Internet Connection Required", Toast.LENGTH_LONG).show();
        }
    }

    private void send(String url, Map<String, String> parameterData) {
        try {

            Response.Listener<JSONObject> reponseListener = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    processJsonObject(jsonObject);

                }
            };
            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    if (volleyError instanceof NetworkError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(getContext(),
                                "Oops. Network Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof ServerError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(getContext(),
                                "Oops. Server error!",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof AuthFailureError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(getContext(),
                                "Oops. AuthFailureError error!",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof ParseError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(getContext(),
                                "Oops. Parse Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof NoConnectionError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(getContext(),
                                "Oops. No Connection Error !",
                                Toast.LENGTH_LONG).show();
                    } else if (volleyError instanceof TimeoutError) {
                        sendError(volleyError.toString(), "parking/pass_add");
                        Toast.makeText(getContext(),
                                "Oops. Timeout Error!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            };
            CustomRequest jsObjRequest = new CustomRequest(Request.Method.POST, url, parameterData, reponseListener, errorListener);
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(jsObjRequest);
        } catch (Exception e) {
            Log.e("RESPONSE ERROR", e.toString());
            VolleyLog.d("RESPONSE ERROR", e.toString());
        }
    }

    private void processJsonObject(JSONObject response) {
        if (response != null) {
            Log.d("Response", response + "");

            try {
                String data = String.valueOf(response.get("data"));
                String message = String.valueOf(response.get("message"));
                int status = response.getInt("status");
                // String responce = json.getJSONArray("RESPONSE");

            } catch (NullPointerException e) {
                Toast.makeText(getActivity(), "Unexpected Error...", Toast.LENGTH_SHORT).show();
                // Toast.makeText(RegisterUserActivity.this, "Nothing ", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                // pDialog.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Technical Error...", Toast.LENGTH_SHORT).show();
                // pDialog.dismiss();
            }
        }
    }
}
