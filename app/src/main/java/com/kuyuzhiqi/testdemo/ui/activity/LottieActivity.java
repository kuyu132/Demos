package com.kuyuzhiqi.testdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.airbnb.lottie.LottieAnimationView;
import com.kuyuzhiqi.testdemo.R;

public class LottieActivity extends AppCompatActivity {

    private LottieAnimationView lottie_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);
        lottie_view = findViewById(R.id.lottie_view);
        //lottie_view.setImageAssetsFolder("images/");
        lottie_view.setAnimation("data.json");
        lottie_view.playAnimation();
    }
}
