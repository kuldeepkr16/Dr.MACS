package com.example.lenovo.dra;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovo.dra.POJO.BloodRequestPojo;

import java.util.List;



class BloodUsersAdapter extends RecyclerView.Adapter<BloodUsersAdapter.ViewHolder>{
private List<BloodRequestPojo> usersList;
    BloodUsersAdapter(List<BloodRequestPojo> usersList){
        this.usersList=usersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bloodrequest_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameText.setText(usersList.get(position).getFrom());
        holder.phonenumber.setText(usersList.get(position).getPhone_no());
        holder.Bloodtype.setText(usersList.get(position).getSelectedBloodType());
        holder.urgent.setText(usersList.get(position).getSelectedUrgency());
        holder.NOW.setText(usersList.get(position).getNOW());
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView nameText;
        TextView phonenumber;
        TextView Bloodtype;
        TextView urgent;
        TextView NOW;
        
        ViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
            nameText=(TextView) mView.findViewById(R.id.from);
            phonenumber=(TextView) mView.findViewById(R.id.phonenumber);
            Bloodtype=(TextView) mView.findViewById(R.id.BloodType);
            urgent=(TextView) mView.findViewById(R.id.Urgency);
            NOW = (TextView) mView.findViewById(R.id.txtNOW);
        }
    }
}