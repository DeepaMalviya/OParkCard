package attender.oparkCard.vehiclelist.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import attender.oparkCard.R;
import attender.oparkCard.vehiclelist.fragment.ThreeWheelerCheckInFragment;
import attender.oparkCard.vehiclelist.model.VehicleModelDetails;

/**
 * Created by Daffodil on 7/6/2018.
 */

public class ThreeWheelerCheckInAdapter extends RecyclerView.Adapter<ThreeWheelerCheckInAdapter.ViewHolder> {

    private Context context;
    ThreeWheelerCheckInFragment fourWheelerCheckInFragment;
    private ArrayList<VehicleModelDetails> vehicleModelList = new ArrayList<>();

    public ThreeWheelerCheckInAdapter(ThreeWheelerCheckInFragment fourWheelerCheckInFragment, ArrayList<VehicleModelDetails> vehicleModelList) {
        this.fourWheelerCheckInFragment = fourWheelerCheckInFragment;
        this.vehicleModelList = vehicleModelList;
        context = fourWheelerCheckInFragment.getContext();
    }


    @Override
    public ThreeWheelerCheckInAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_view_mange_vehicle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(vehicleModelList.get(position));

    }

    @Override
    public int getItemCount() {
          return vehicleModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewVehicleName;
        private TextView mobileNumber, vehicleType;
        private TextView vehicleNoOut;
        private TextView checkdate, chkOut;


        public ViewHolder(View itemView) {
            super(itemView);
            textViewVehicleName = (TextView) itemView.findViewById(R.id.textViewVehicleName);
            mobileNumber = (TextView) itemView.findViewById(R.id.mobileNumber);
            vehicleNoOut = (TextView) itemView.findViewById(R.id.vehicleNo);
            checkdate = (TextView) itemView.findViewById(R.id.checkdate);
            chkOut = (TextView) itemView.findViewById(R.id.chkOut);
            vehicleType = (TextView) itemView.findViewById(R.id.vehicleType);
        }

        @SuppressLint("ResourceAsColor")
        public void bind(VehicleModelDetails vehicleModelDetails) {

            //   textViewVehicleName.setText("Parking Name :"+vehicleModelDetails.getParkingName());
            String m = vehicleModelDetails.getMobileNo();
            /*if ( m.equals("")) {
                mobileNumber.setVisibility(View.GONE);
            } else {
                mobileNumber.setText("Mobile Number :  " + vehicleModelDetails.getMobileNo());
                mobileNumber.setTextColor(R.color.colorPrimary);
            }*/


            vehicleNoOut.setText("VehicleNo :  " + vehicleModelDetails.getVehicleNo());
            checkdate.setText("Check In Date :  " + vehicleModelDetails.getCheckInDateTime());
            chkOut.setText("Check Out :  " + vehicleModelDetails.getCheckOutDateTime());
            vehicleType.setText("Parking Type :  " + vehicleModelDetails.getParkingType());


        }
    }
}
