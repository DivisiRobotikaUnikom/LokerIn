package com.example.loker.Fragment.Booking;


import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loker.Adapter.LokerAdapter;
import com.example.loker.Database.DatabaseInit;
import com.example.loker.Model.LokerModel;
import com.example.loker.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingLokerFragment extends Fragment {

    RecyclerView myStand;
    ArrayList<LokerModel> list;
    LokerAdapter standAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_booking, container, false);

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

        myStand = root.findViewById(R.id.myStand);
        int col = 2;
        myStand.setLayoutManager(new GridLayoutManager(getActivity(), col));
        list = new ArrayList<LokerModel>();

        final String res = getArguments().getString("nama");
        final String result = getArguments().getString("status");

        db.stand.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("nama").getValue().toString().equals(res)) {
                        for (DataSnapshot ds1 : ds.getChildren()) {
                            if (ds1.getKey().matches("[0-9]+")) {
                                i++;
                                String key = ds1.getKey();
                                LokerModel p = ds.child(key).getValue(LokerModel.class);
                                list.add(p);
                            }
                        }
                    }
                }
                standAdapter = new LokerAdapter(getActivity(), list, res);
                myStand.setAdapter(standAdapter);
                standAdapter.notifyDataSetChanged();
                TextView tvJumlah = root.findViewById(R.id.tvJumlah);
                tvJumlah.setText(Integer.toString(i) + " Loker");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        // Inflate the layout for this fragment
        return root;
    }
}
