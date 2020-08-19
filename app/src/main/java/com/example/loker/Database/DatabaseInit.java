package com.example.loker.Database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseInit {
    //Autentikasi
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener mAuthListener;

    //Database
    public FirebaseDatabase database;
    public DatabaseReference root;
    public DatabaseReference pegawai;
    public DatabaseReference users;

    //User
    public FirebaseUser user;

    public DatabaseInit() {
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
//        user = mAuth.getCurrentUser();

        pegawai = database.getReference("pegawai");
        users = database.getReference("user");
    }
}
