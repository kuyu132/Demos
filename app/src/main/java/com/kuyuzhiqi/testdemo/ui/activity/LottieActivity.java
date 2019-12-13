package com.kuyuzhiqi.testdemo.ui.activity;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.airbnb.lottie.LottieAnimationView;
import com.kuyuzhiqi.testdemo.R;

public class LottieActivity extends AppCompatActivity {

    private LottieAnimationView lottie_view;
    private TextView tv_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lottie);
        lottie_view = findViewById(R.id.lottie_view);
        //lottie_view.setImageAssetsFolder("images/");
        lottie_view.setAnimation("dynamic_praise.json");
        lottie_view.playAnimation();
//        tv_name = findViewById(R.id.tv_name);
//        SpannableString ss  = new SpannableString(tv_name.getText());
//        ss.setSpan(new ForegroundColorSpan((Color.YELLOW)),0,20, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        tv_name.setText(ss);
    }
}
