package com.example.loker.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loker.Database.DatabaseInit;
import com.example.loker.Fragment.Booking.BookingFragment;
import com.example.loker.Fragment.Booking.BookingLokerFragment;
import com.example.loker.MainActivity;
import com.example.loker.Model.LokerModel;
import com.example.loker.R;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class LokerAdapter extends RecyclerView.Adapter<LokerAdapter.MyViewHolder> {

    Context context;
    ArrayList<LokerModel> myStand;
    String res;

    public LokerAdapter(Context context, ArrayList<LokerModel> myStand, String res) {
        this.context = context;
        this.myStand = myStand;
        this.res = res;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LokerAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_booking_loker, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.tvId.setText("Loker " + Integer.toString(myStand.get(position).getId()));
        String status = "";
        if (myStand.get(position).getStatus().equals("available")) {
            status = "Available";
        } else if (myStand.get(position).getStatus().equals("not available")) {
            status = "Not Available";
        } else if (myStand.get(position).getStatus().equals("booked")) {
            status = "Booked";
        }
        holder.tvStatus.setText(status);

        final String getNama = Integer.toString(myStand.get(position).getId());
        final String getStatus = myStand.get(position).getStatus();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                if (holder.tvStatus.getText().toString().equals("Available")) {
                        builder.setMessage("Ingin Menyewa Loker Ini?")
                        .setTitle("Sewa Loker")
                        .setCancelable(false)
                        .setPositiveButton("Sewa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseInit db = new DatabaseInit();
                                FirebaseUser user = db.mAuth.getCurrentUser();
                                Long ts = System.currentTimeMillis()/1000;
                                String tss = ts.toString();

                                String[] stand = res.split("\\s+");
                                String[] loker = holder.tvId.getText().toString().split("\\s+");

                                db.booking.child(user.getUid() + tss).child("uid").setValue(user.getUid());
                                db.booking.child(user.getUid() + tss).child("stand").setValue("stand" + stand[1]);
                                db.booking.child(user.getUid() + tss).child("loker").setValue(loker[1]);
                                db.booking.child(user.getUid() + tss).child("status").setValue("Booking");
                                db.booking.child(user.getUid() + tss).child("time").setValue(tss);

                                db.stand.child("stand" + stand[1]).child(loker[1]).child("status").setValue("booked");

                                MainActivity activity = (MainActivity) view.getContext();
                                Fragment fragment = new BookingFragment();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                activity.getSupportFragmentManager().popBackStack();
                                Toast.makeText(view.getContext(), "Booking Berhasil!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Batal", null);
                } else if (holder.tvStatus.getText().toString().equals("Not Available")) {
                    builder.setMessage("Loker Tidak Tersedia")
                            .setTitle("Sewa Loker")
                            .setCancelable(false)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                } else {
                    builder.setMessage("Loker Sudah Terbooking")
                            .setTitle("Sewa Loker")
                            .setCancelable(false)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                }
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myStand.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
