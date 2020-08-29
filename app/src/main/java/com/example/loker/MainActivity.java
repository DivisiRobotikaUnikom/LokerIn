package com.example.loker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.loker.Database.DatabaseInit;
import com.example.loker.Fragment.Account.AccountFragment;
import com.example.loker.Fragment.Auth.LoginActivity;
import com.example.loker.Fragment.Booking.BookingFragment;
import com.example.loker.Fragment.History.HistoryFragment;
import com.example.loker.Fragment.Home.HomeFragment;
import com.example.loker.Scan.CaptureAct;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class MainActivity extends FragmentActivity {

    private boolean exit = false;
    private boolean click = false;
    SpaceNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        nav = findViewById(R.id.space);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

        nav.initWithSaveInstanceState(savedInstanceState);
        nav.addSpaceItem(new SpaceItem("", R.drawable.ic_home_black_24dp));
        nav.addSpaceItem(new SpaceItem("", R.drawable.ic_format_list_bulleted_black_24dp));
        nav.addSpaceItem(new SpaceItem("", R.drawable.ic_history_black_24dp));
        nav.addSpaceItem(new SpaceItem("", R.drawable.ic_person_black_24dp));

        nav.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                scanCode();
                click = true;
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                int count = getSupportFragmentManager().getBackStackEntryCount();
                if (count > 0) {
                    getSupportFragmentManager().popBackStack();
                }
                Fragment selectedFragment = null;
                switch (itemIndex) {
                    case 0:
                        selectedFragment = new HomeFragment();
                        break;
                    case 1:
                        selectedFragment = new BookingFragment();
                        break;
                    case 2:
                        selectedFragment = new HistoryFragment();
                        break;
                    case 3:
                        selectedFragment = new AccountFragment();
                        break;
                    default:
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Keluar Aplikasi")
                    .setMessage("Yakin ingin keluar?")
                    .setCancelable(false)
                    .setPositiveButton("Keluar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("Batal", null).show();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void scanCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null) {
                DatabaseInit db = new DatabaseInit();

                db.booking.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        int hitung = 0;
                        boolean datang = false;
                        String loker = "";
                        int i = 0;
                        long count = dataSnapshot.getChildrenCount();

                        if (click == true) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                i++;
                                DatabaseInit db = new DatabaseInit();
                                FirebaseUser user = db.mAuth.getCurrentUser();

                                if (user.getUid().equals(ds.child("uid").getValue().toString()) && datang == false) {
                                    String[] res = result.getContents().split("\\s+");
                                    String stand = res[0].toLowerCase() + res[1];

                                    if (stand.equals(ds.child("stand").getValue().toString())) {
                                        if (ds.child("status").getValue().toString().equals("Datang")) {
                                            if (i == count) {
                                                hitung = 1;
                                                datang = true;
                                            }
                                        } else if (ds.child("status").getValue().equals("Booking")) {
                                                datang = true;
                                                hitung = 4;
                                                loker = ds.child("loker").getValue().toString();
                                                db.daftar.child(ds.child("stand").getValue().toString()).child("id").setValue(ds.child("loker").getValue().toString());
                                                db.booking.child(ds.getKey()).child("status").setValue("Datang");
                                                Calendar calendar = Calendar.getInstance();
                                                String tss = DateFormat.format("EEE, dd-MM-yyyy HH:mm:ss", calendar.getTime()).toString();
                                                db.booking.child(ds.getKey()).child("time").setValue(tss);
                                        }
                                    } else {
                                        if (datang == false) {
                                            hitung = 2;
                                        } else {
                                            hitung = 4;
                                        }
                                    }
                                } else {
                                    if (datang == false) {
                                        hitung = 3;
                                    } else {
                                        hitung = 4;
                                    }
                                }
                            }

                            if (hitung == 2) {
                                builder.setTitle("Warning")
                                        .setMessage("Anda Salah Stand!")
                                        .setCancelable(true);
                            } else if (hitung == 3) {
                                builder.setTitle("Warning")
                                        .setMessage("Anda Belum Booking!")
                                        .setCancelable(true);
                            } else if (hitung == 1) {
                                builder.setTitle("Warning")
                                        .setMessage("Anda Sudah Mengkonfirmasi Kedatangan di Semua Loker!")
                                        .setCancelable(true);
                            } else if (hitung == 4){
                                builder.setTitle("Success");
                                builder.setMessage("Silahkan Tempel Jari Anda di Sensor Fingerprint untuk Loker " + loker + "!");
                                builder.setCancelable(true);
                            }
                            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    click = false;
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } else {
                Toast.makeText(this, "No Results", Toast.LENGTH_SHORT).show();
                click = false;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            click = false;
        }
    }
}
