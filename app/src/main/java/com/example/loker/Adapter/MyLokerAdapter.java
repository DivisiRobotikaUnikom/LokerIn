package com.example.loker.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loker.Database.DatabaseInit;
import com.example.loker.Fragment.Account.AccountFragment;
import com.example.loker.MainActivity;
import com.example.loker.Model.MyLokerModel;
import com.example.loker.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyLokerAdapter extends RecyclerView.Adapter <MyLokerAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyLokerModel> myLoker;

    public MyLokerAdapter(Context context, ArrayList<MyLokerModel> myLoker) {
        this.context = context;
        this.myLoker = myLoker;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyLokerAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.card_myloker, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.tvLoker.setText(myLoker.get(position).getLoker());
        holder.tvTanggal.setText(myLoker.get(position).getTanggal());
        holder.tvJam.setText(myLoker.get(position).getJam());

        holder.btnSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new android.app.AlertDialog.Builder(view.getContext())
                        .setTitle("Konfirmasi")
                        .setMessage("Sudah selesai menyewa?")
                        .setCancelable(false)
                        .setPositiveButton("Selesai", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseInit db = new DatabaseInit();
                                final FirebaseUser user = db.mAuth.getCurrentUser();
                                db.booking.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            if (ds.child("uid").getValue().toString().equals(user.getUid())) {
                                                String[] res = myLoker.get(position).getLoker().split(" - ");
                                                String[] stand = res[0].split("\\s+");
                                                String[] loker = res[1].split("\\s+");
                                                if (ds.child("stand").getValue().toString().equals(stand[0].toLowerCase() + stand[1])) {
                                                    if (ds.child("loker").getValue().toString().equals(loker[1])) {
                                                        if (ds.child("status").getValue().toString().equals("Datang")) {
                                                            DatabaseInit db = new DatabaseInit();
                                                            db.booking.child(ds.getKey()).child("status").setValue("Finish");
                                                            db.stand.child(ds.child("stand").getValue().toString())
                                                                    .child(ds.child("loker").getValue().toString())
                                                                    .child("status").setValue("available");

                                                            MainActivity activity = (MainActivity) view.getContext();
                                                            Fragment fragment = new AccountFragment();
                                                            activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                                            activity.getSupportFragmentManager().popBackStack();
                                                            Toast.makeText(view.getContext(), "Penyewaan Loker " + loker[1] + " di Stand " + stand[1] + " Selesai!",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
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
                        })
                        .setNegativeButton("Batal", null).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myLoker.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTanggal, tvJam, tvLoker;
        Button btnSelesai;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLoker = itemView.findViewById(R.id.tvLoker);
            tvTanggal = itemView.findViewById(R.id.tvTanggal);
            tvJam = itemView.findViewById(R.id.tvJam);
            btnSelesai = itemView.findViewById(R.id.btnSelesai);
        }
    }
}
