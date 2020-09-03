package com.example.loker.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loker.Database.DatabaseInit;
import com.example.loker.Fragment.Home.HomeFragment;
import com.example.loker.Model.HomeModel;
import com.example.loker.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class HomeAdapter extends RecyclerView.Adapter <HomeAdapter.MyViewHolder> {

    Context context;
    ArrayList<HomeModel> myHome;
    private CountDownTimer countDownTimer;
    private long mTimeLeftInMillis;

    public HomeAdapter(Context context, ArrayList<HomeModel> myHome) {
        this.context = context;
        this.myHome = myHome;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_booking, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.tvLoker.setText(myHome.get(position).getLoker());
        holder.tvStand.setText(myHome.get(position).getStand());

        mTimeLeftInMillis = myHome.get(position).getTime();
        if (mTimeLeftInMillis > 0) {
            countDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long l) {
                    mTimeLeftInMillis = l;

                    int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
                    int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

                    String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                    holder.tvCount.setText(timeLeft);
                }

                @Override
                public void onFinish() {

                }
            }.start();
        } else {
            String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", 0, 0);
            holder.tvCount.setText(timeLeft);
        }

        if (mTimeLeftInMillis <= (15 * 60000) && mTimeLeftInMillis > 0) {
            holder.cardBooking.setCardBackgroundColor(Color.YELLOW);
        } else if (mTimeLeftInMillis <= 0) {
            holder.cardBooking.setCardBackgroundColor(Color.RED);
        } else {
            holder.cardBooking.setCardBackgroundColor(Color.WHITE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                String[] count = holder.tvCount.getText().toString().split(":");
                if (Integer.parseInt(count[0]) >= 00 && Integer.parseInt(count[1]) > 00) {
                    builder.setMessage("Silahkan Konfirmasi Kedatangan dengan Scan QR Code di Stand")
                            .setTitle("Konfirmasi Kedatangan")
                            .setCancelable(true)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                } else {
                    builder.setMessage("Silahkan booking kembali. Waktu checkout sudah habis!")
                            .setTitle("Re-booking")
                            .setCancelable(true)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    DatabaseInit db = new DatabaseInit();
                    final FirebaseUser user = db.mAuth.getCurrentUser();
                    db.booking.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                if (ds.child("uid").getValue().toString().equals(user.getUid())) {
                                    if (ds.child("status").getValue().toString().equals("Booking")) {
                                        String stand = "stand" + holder.tvStand.getText().toString().substring(6);
                                        String loker = holder.tvLoker.getText().toString().substring(6);
                                        if (ds.child("stand").getValue().toString().equals(stand) && ds.child("loker").getValue().toString().equals(loker)) {
                                            DatabaseInit db = new DatabaseInit();
                                            db.booking.child(ds.getKey()).removeValue();
                                            db.stand.child(stand).child(loker).child("status").setValue("available");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myHome.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvLoker, tvStand, tvCount;
        CardView cardBooking;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoker = itemView.findViewById(R.id.tvLoker);
            tvStand = itemView.findViewById(R.id.tvStand);
            tvCount = itemView.findViewById(R.id.tvCount);
            cardBooking = itemView.findViewById(R.id.cardBooking);
        }
    }
}
