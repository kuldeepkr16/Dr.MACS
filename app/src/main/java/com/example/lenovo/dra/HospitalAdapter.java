package com.example.lenovo.dra;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.ViewHolder> {
    public List<PlacesDetails> usersList;
    public HospitalAdapter(List<PlacesDetails> usersList){
        this.usersList=usersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.myhospital_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameofPlace.setText(usersList.get(position).getNameOfPlace());
        holder.Address.setText(usersList.get(position).getAddressOfPlace());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TextView nameofPlace;
        public TextView Address;



        public ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            nameofPlace=(TextView) mView.findViewById(R.id.myHospital);
            Address=(TextView) mView.findViewById(R.id.myplacee);
        }
    }
}
