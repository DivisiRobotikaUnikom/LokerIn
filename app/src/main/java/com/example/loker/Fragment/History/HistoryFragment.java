package com.example.loker.Fragment.History;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loker.Adapter.HistoryAdapter;
import com.example.loker.Database.DatabaseInit;
import com.example.loker.Model.HistoryModel;
import com.example.loker.Model.StandModel;
import com.example.loker.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    RecyclerView myHistory;
    ArrayList<HistoryModel> list;
    HistoryAdapter historyAdapter;
    private String lokasi;
    private int i = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_history, container, false);
        DatabaseInit db = new DatabaseInit();
        db.users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseInit db = new DatabaseInit();
                FirebaseUser user = db.mAuth.getCurrentUser();
                String url = dataSnapshot.child(user.getUid()).child("profile").getValue().toString();
                ImageView img = root.findViewById(R.id.ivUser);
                Picasso.with(img.getContext()).load(url).placeholder(R.drawable.ic_person_black_24dp).into(img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myHistory = root.findViewById(R.id.myHistory);
        myHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
        list = new ArrayList<HistoryModel>();
        db.booking.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.child("status").getValue().toString().equals("Finish")) {
                        DatabaseInit db = new DatabaseInit();
                        i++;
                        Calendar cal = Calendar.getInstance();
                        Long res = Long.parseLong(ds.child("time").getValue().toString());
                        cal.setTimeInMillis(res * 1000);
                        String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();

                        String loker = "Loker " + ds.child("loker").getValue().toString();
                        String stand = "Stand " + ds.child("stand").getValue().toString().substring(5);

                        HistoryModel p = new HistoryModel(loker, date, stand);
                        list.add(p);
                    }
                }
                historyAdapter = new HistoryAdapter(getActivity(), list);
                myHistory.setAdapter(historyAdapter);
                historyAdapter.notifyDataSetChanged();
                TextView tvJumlah1 = root.findViewById(R.id.tvJumlah1);
                tvJumlah1.setText(Integer.toString(i) + " History");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        return root;
    }

    public String a() {
        Log.d("TAG", "A" + lokasi);
        return lokasi;
    }
}
