package com.example.loker.Fragment.Home;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loker.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    TextView tvDay, tvWelcome, tvDate, tvGuideTitle, tvGuideSubTitle;
    Button btnGuide;
    ImageView ivGuide;
    Animation alfatogo, alfatogotwo, alfatogothree;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home2, container, false);

        // Get time now in HomeFragment
        tvDay = root.findViewById(R.id.tvDay);
        tvWelcome = root.findViewById(R.id.tvWelcome);
        tvDate = root.findViewById(R.id.tvDate);

        final Date date;
        Calendar cal = Calendar.getInstance();

        Log.d("date", String.valueOf(new Date()));
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");

        date = cal.getTime();

        String formattedDate = dateFormat.format(date);
        String hour = sdf.format(new Date());

        int hourNow = Integer.valueOf(hour);

//        Log.d("date", String.valueOf(hourNow));
        tvDay.setText(date.toString().substring(0,3));
        tvDate.setText(formattedDate);

        if (hourNow >= 3 && hourNow <= 11) {
            tvWelcome.setText("Good Morning");
        }
        if (hourNow >= 12 && hourNow <= 17) {
            tvWelcome.setText("Good Afternoon");
        }
        if (hourNow >= 18 && hourNow <= 23) {
            tvWelcome.setText("Good Evening");
        }
        if (hourNow >= 0 && hourNow <= 2) {
            tvWelcome.setText("Good Night");
        }

        // Get animation
//        alfatogo = AnimationUtils.loadAnimation(this, R.anim.alfatogo);
//        alfatogotwo = AnimationUtils.loadAnimation(this, R.anim.alfatogotwo);
//        alfatogothree = AnimationUtils.loadAnimation(this, R.anim.alfatogothree);
//
//        tvGuideTitle = root.findViewById(R.id.tvGuideTitle);
//        tvGuideSubTitle = root.findViewById(R.id.tvGuideSubtitle);
//        ivGuide = root.findViewById(R.id.ivGuide);
//        btnGuide = root.findViewById(R.id.btnGuide);
//
//        ivGuide.startAnimation(alfatogo);
//        tvGuideTitle.startAnimation(alfatogotwo);
//        tvGuideSubTitle.startAnimation(alfatogotwo);
//        btnGuide.startAnimation(alfatogothree);

        return root;
    }

}
