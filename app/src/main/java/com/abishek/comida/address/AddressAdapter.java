package com.abishek.comida.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abishek.comida.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<AddressModel> addressList;
    private Context context;

    public AddressAdapter(List<AddressModel> addressList, Context context) {
        this.addressList = addressList;
        this.context = context;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_list_design,parent,false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {

        holder.addressTypeView.setText(addressList.get(position).getAddressType());
        holder.addressView.setText(addressList.get(position).getAddress());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder{

        private TextView addressTypeView;
        private TextView addressView;
        private TextView btnEdit,btnDelete;

        public AddressViewHolder(@NonNull View v) {
            super(v);

            addressTypeView = v.findViewById(R.id.address_type);
            addressView = v.findViewById(R.id.address);
            btnEdit = v.findViewById(R.id.edit);
            btnDelete = v.findViewById(R.id.delete);
        }
    }
}
