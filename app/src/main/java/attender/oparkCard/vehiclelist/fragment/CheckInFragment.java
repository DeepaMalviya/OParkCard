package attender.oparkCard.vehiclelist.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
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
import attender.oparkCard.base.RecyclerTouchListener;
import attender.oparkCard.subscription.model.CustomRequest;
import attender.oparkCard.vehiclelist.activity.WebViewPrint;
import attender.oparkCard.vehiclelist.adapter.CarInAdapter;

import attender.oparkCard.base.DividerItemDecoration;
import attender.oparkCard.vehiclelist.model.VehicleModelDetails;


public class CheckInFragment extends Fragment {
    //
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "CheckInFragment";

    private CarInAdapter adapter;
    private RecyclerView recyclerViewVehicle;
    private ArrayList<VehicleModelDetails> vehicleModelList = new ArrayList<>();
    protected ProgressDialog progressDialog;
    SharedPreferences sharedpref;
    String agentId, userRole, userName, userContactNo, vendorId, vendorName, parkingName, parkingType, parkingId,
            parkingVehicle, parkingTypeTwo, vn, parkingType1234, aName, parkingType12, pType2, pType4;
    Fragment fragment = null;
    String[] pType = new String[3];


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CheckInFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CheckInFragment newInstance(String param1, String param2) {
        CheckInFragment fragment = new CheckInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_in, container, false);

        findVievId(view);

        return view;


    }

    @Override
    public void onResume() {
        super.onResume();
        if (AppConstants.isInternetAvailable(getActivity())) {

          /*  if (parkingType1234.equals("2Wheeler") && parkingType12.equals("4Wheeler")) {
                getDetails(parkingId, parkingTypeTwo, "checkInList");
            }*/

            if (pType.length == 2) {
                getDetails(parkingId, "2Wheeler", "checkInList");

            } else {
                getDetails(parkingId, "2Wheeler", "checkInList");

            }
            // getDetails(parkingId, parkingType1234, "checkInList");

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
        parkingType = sharedpref.getString("getparkingType", "");
        parkingId = sharedpref.getString("parkingId", "");
        parkingType12 = sharedpref.getString("VehicleType2", "");

        //      parkingVehicle = sharedpref.getString("parkingVehicle", "");
//        parkingType1234 = sharedpref.getString("VehicleType1", "");

//        parkingTypeTwo = sharedpref.getString("towWheeler", "");
//        parkingTypeFour = sharedpref.getString("fourWheeler", "");
//        String pType[] = parkingType.toString().split(",");
//        pType2 = pType[0];
//        pType4 = pType[1];
        pType = parkingType.toString().split(",");

        if (pType.length == 1) {
            parkingType1234 = sharedpref.getString("VehicleType1", "");
        }


        if (pType.length == 2) {
            pType2 = pType[0];
            pType4 = pType[1];
        }

        final String mode = "checkIn";
        //7049273490

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

                          getReceiptWeb(parkingId, "2Wheeler", mode, vehicleModelList.get(position).getTransactionId(),agentId);

                        } else {

                            getReceiptWeb(parkingId, "2Wheeler", mode, vehicleModelList.get(position).getTransactionId(),agentId);

                        }

                        //  getReceiptWeb(parkingId, parkingType1234, mode, vehicleModelList.get(position).getTransactionId());
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
        /*http://staggingapi.opark.in/index.php/v1/parking/vehicle_list?parkingId=1&parkingType=2Wheeler&mode=checkOutList*/

        String urlData = AppConstants.BASEURL + "parking/vehicle_list?parkingId=" + parkingId + "&parkingType=" + parkingType + "&mode=checkInList";
        Log.e("TAG", "getDetails: "+urlData);
        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {


                try {
                    System.out.println("JSON RETURN " + json.toString());

                    vehicleModelList.clear();
                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");


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

                            aName = vehicleModelDetails.getAgentName();
                            vn = vehicleModelDetails.getVehicleNo();

                            vehicleModelList.add(vehicleModelDetails);


                        }


                        adapter = new CarInAdapter(CheckInFragment.this, vehicleModelList);
                        recyclerViewVehicle.setAdapter(adapter);


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                    pDialog.dismiss();

                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Unexpected Error...", Toast.LENGTH_SHORT).show();
                    sendError(e.toString(), "parking/vehicle_list?parkingId=");
                    pDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Technical Error...", Toast.LENGTH_SHORT).show();
                    sendError(e.toString(), "parking/vehicle_list?parkingId=");
                    pDialog.dismiss();
                }
            }
        },

                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        sendError(error.toString(), "parking/vehicle_list?parkingId=");
                        Log.d("Response error", error + "");
                        Toast.makeText(getActivity(), "Internet Connection Required", Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                    }
                });

        request.setRetryPolicy(new DefaultRetryPolicy(50000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().getRequestQueue().add(request);
        //  pDialog.dismiss();

    }
//    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void getReceiptWeb(String parkingId, final String parkingType, String mode, String transactionId, String agentId) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(true);
        pDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pDialog.show();
        pDialog.setContentView(R.layout.custom_progress_bar);

        String urlData = AppConstants.BASEURL + "parking/receipt?parkingId=" + parkingId + "&parkingType=" + parkingType + "&mode=" + mode + "&transactionId=" + transactionId + "&agentId=" + agentId;
        Log.e(TAG, "getReceiptWeb: urlData  "+urlData );
        JsonObjectRequest request = new JsonObjectRequest(urlData, null, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject json) {

                vehicleModelList.clear();
                try {
                    System.out.println("JSON " + "ETURN " + json.toString());

                    Log.e(TAG, "onResponse: "+ json.toString() );
                    String data = String.valueOf(json.get("data"));
                    String message = String.valueOf(json.get("message"));
                    int status = json.getInt("status");

                    if (status == 0) {

                        final JSONObject arrayObj = new JSONObject(data);


                        SharedPreferences storeAllValues = getActivity().getSharedPreferences("opark", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storeAllValues.edit();
                        editor.putString("parkingPrint", "checkInModel");
                        editor.apply();
                        editor.commit();

                        String transactionId = arrayObj.getString("transactionId");
                        String receiptHeading = arrayObj.getString("receiptHeading");
                        String parkingAddress = arrayObj.getString("parkingAddress");
                        String userContactNo = arrayObj.getString("userContactNo");
                        String checkInDate = arrayObj.getString("checkInDate");
                        String agentId = arrayObj.getString("agentId");
                        String availableSlots = arrayObj.getString("availableSlots");
                        String parkingId = arrayObj.getString("parkingId");
                        String vehicleNo1 = arrayObj.getString("vehicleNo");
                        String parkingRate = arrayObj.getString("parkingRate");
                        String additionalParkingRate = arrayObj.getString("additionalParkingRate");
                        String mode = arrayObj.getString("mode");
                        String receiptStaticText = arrayObj.getString("receiptStaticText");
                        String receiptEmail = arrayObj.getString("receiptEmail");
                        String receiptMobile = arrayObj.getString("receiptMobile");
                        String receiptWebsite = arrayObj.getString("receiptWebsite");
                        String receipt = arrayObj.getString("receipt");
                        String barcode = arrayObj.getString("barcode");
                        String responseType = arrayObj.getString("responseType");
                        String parkingType = arrayObj.getString("parkingType");
                        String companyWebsite = arrayObj.getString("companyWebsite");
                        String poweredBy = arrayObj.getString("poweredBy");
                        String receiptType = arrayObj.getString("receiptType");
                        String receiptNo = arrayObj.getString("receiptNo");
                        String qrCode = arrayObj.getString("qrCode");
                        String agentName = arrayObj.getString("agentName");
                        String lastLine = arrayObj.getString("lastLine");
                        String reprintText = arrayObj.getString("reprintText");
                        // String printReceipt = arrayObj.getString("printReceipt");

                        Intent intentWeb = new Intent(getActivity(), WebViewPrint.class);

                        intentWeb.putExtra("transactionId", transactionId);
                        intentWeb.putExtra("parkingId", parkingId);
                        intentWeb.putExtra("parkingType", "2Wheeler");
                        intentWeb.putExtra("mode", mode);
                        intentWeb.putExtra("receipt", receipt);
                        intentWeb.putExtra("receiptHeading", receiptHeading);
                        intentWeb.putExtra("parkingAddress", parkingAddress);
                        intentWeb.putExtra("userContactNo", userContactNo);
                        intentWeb.putExtra("checkInDate", checkInDate);
                        intentWeb.putExtra("agentId", agentId);
                        intentWeb.putExtra("availableSlots", availableSlots);
                        intentWeb.putExtra("vn", vehicleNo1);
                        intentWeb.putExtra("parkingRate", parkingRate);
                        intentWeb.putExtra("additionalParkingRate", additionalParkingRate);
                        intentWeb.putExtra("receiptStaticText", receiptStaticText);
                        intentWeb.putExtra("receiptEmail", receiptEmail);
                        intentWeb.putExtra("receiptMobile", receiptMobile);
                        intentWeb.putExtra("receiptWebsite", receiptWebsite);
                        intentWeb.putExtra("barcode", barcode);
                        intentWeb.putExtra("responseType", responseType);
                        intentWeb.putExtra("companyWebsite", companyWebsite);
                        intentWeb.putExtra("poweredBy", poweredBy);
                        intentWeb.putExtra("receiptType", receiptType);
                        intentWeb.putExtra("receiptNo", receiptNo);
                        intentWeb.putExtra("qrCode", qrCode);
                        intentWeb.putExtra("aName", agentName);
                        intentWeb.putExtra("lastLine", lastLine);
                        intentWeb.putExtra("reprintText", reprintText);
                        //  intentWeb.putExtra("printReceipt", printReceipt);

                        getActivity().startActivity(intentWeb);


                        pDialog.dismiss();


                    } else {
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
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

                new com.android.volley.Response.ErrorListener() {
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
                    Log.e("RESPONSE ERROR", volleyError.toString());
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

//    private boolean _hasLoadedOnce= false; // your boolean field
//
//    @Override
//    public void setUserVisibleHint(boolean isFragmentVisible_) {
//        super.setUserVisibleHint(true);
//
//
//        if (this.isVisible()) {
//            // we check that the fragment is becoming visible
//            if (isFragmentVisible_ && !_hasLoadedOnce) {
//              //  new NetCheck().execute();
//                _hasLoadedOnce = true;
//            }
//        }
//    }

}
