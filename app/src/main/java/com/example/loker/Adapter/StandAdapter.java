package com.example.loker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loker.Fragment.Booking.BookingLokerFragment;
import com.example.loker.MainActivity;
import com.example.loker.Model.StandModel;
import com.example.loker.R;

import java.util.ArrayList;

public class StandAdapter extends RecyclerView.Adapter<StandAdapter.MyViewHolder> {

    Context context;
    ArrayList<StandModel> myStand;

    public StandAdapter(Context context, ArrayList<StandModel> myStand) {
        this.context = context;
        this.myStand = myStand;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_loker, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvNama.setText(myStand.get(position).getNama());
        holder.tvLokasi.setText(myStand.get(position).getLokasi());
        holder.tvHarga.setText(myStand.get(position).getHarga());

        final String getNama = myStand.get(position).getNama();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity activity = (MainActivity) view.getContext();
                Fragment fragment = new BookingLokerFragment();
                Bundle bundle = new Bundle();
                bundle.putString("nama", getNama);
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myStand.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama, tvLokasi, tvHarga;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvLokasi = itemView.findViewById(R.id.tvLokasi);
            tvHarga = itemView.findViewById(R.id.tvHarga);
        }
    }

}
