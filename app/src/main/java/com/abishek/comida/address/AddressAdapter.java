package com.abishek.comida.address;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abishek.comida.R;
import com.abishek.comida.cart.Checkout;
import com.abishek.comida.commonFiles.LoginSessionManager;
import com.abishek.comida.commonFiles.MySingleton;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_ADDRESS_ADD;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.BASE_ADDRESS_DELETE;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.NO_OF_RETRY;
import static com.abishek.comida.commonFiles.CommonVariablesAndFunctions.RETRY_SECONDS;
import static com.abishek.comida.commonFiles.LoginSessionManager.ACCESS_TOKEN;
import static com.abishek.comida.commonFiles.LoginSessionManager.TOKEN_TYPE;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private String TAG = "AddressAdapter";

    private List<AddressModel> addressList;
    private Context context;
    private int from;

    public AddressAdapter(List<AddressModel> addressList, Context context,int from) {
        this.addressList = addressList;
        this.context = context;
        this.from = from;
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

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnDelete.setEnabled(false);
                      deleteAddressFromDatabase(addressList.get(position).getAddressId(),position);


            }
        });

        if(addressList.get(position).getAddressType().toLowerCase().equals("home"))
        {
            holder.addressTypeFace.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_home_black));
        }
        else if(addressList.get(position).getAddressType().toLowerCase().equals("office"))
        {
            holder.addressTypeFace.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_office));
        }
        else
        {
            holder.addressTypeFace.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_other));
        }

        if(from==2)
        {
            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, Checkout.class).
                            putExtra("address_id",addressList.get(position).getAddressId())
                            .putExtra("address",addressList.get(position).getAddress())
                            .putExtra("address_name",addressList.get(position).getAddressType()));
                    Log.e(TAG,".....from2: "+addressList.get(position).getAddressId()+": ....");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder{

        private TextView addressTypeView;
        private TextView addressView;
        private TextView btnEdit,btnDelete;
        private ImageView addressTypeFace;
        private LinearLayout card;

        public AddressViewHolder(@NonNull View v) {
            super(v);

            addressTypeView = v.findViewById(R.id.address_type);
            addressView = v.findViewById(R.id.address);
            btnDelete = v.findViewById(R.id.delete);
            addressTypeFace = v.findViewById(R.id.address_type_face);
            card = v.findViewById(R.id.card);

        }
    }


    public void deleteAddressFromDatabase(String addressId,int position) {


        Log.e(TAG, "saveAddress : called");


        final String URL = BASE_ADDRESS_DELETE+addressId;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);


                try {
                    JSONObject jsonObject = new JSONObject(response);

                    int status = jsonObject.getInt("status");

                    if(status != 200)
                    {
                        Toast.makeText(context,"Something went wrong (server)",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Toast.makeText(context,"Address Deleted",Toast.LENGTH_SHORT).show();
                    addressList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,addressList.size());


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.toString());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());

                Toast.makeText(context,"server problem",Toast.LENGTH_SHORT).show();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();

                String tokenType = new LoginSessionManager(context).getUserDetailsFromSP().get(TOKEN_TYPE);
                String accessToken = new LoginSessionManager(context).getUserDetailsFromSP().get(ACCESS_TOKEN);

                header.put("Accept", "application/json");
                header.put("Authorization", tokenType + " " + accessToken);
                Log.e(TAG,"Authorization: "+ tokenType + " " + accessToken);


                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {




                return super.getParams();
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy((RETRY_SECONDS),
                NO_OF_RETRY, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }


}
