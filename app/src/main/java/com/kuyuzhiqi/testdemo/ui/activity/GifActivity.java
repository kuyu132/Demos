package com.kuyuzhiqi.testdemo.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.kuyuzhiqi.testdemo.R;
import pl.droidsonroids.gif.GifImageView;

public class GifActivity extends AppCompatActivity {

    private GifImageView iv_gif;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);
        iv_gif = findViewById(R.id.iv_gif);
    }
}
