package com.kuyuzhiqi.testdemo.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.kuyuzhiqi.testdemo.R;

public class LoginActivity extends AppCompatActivity {

    private EditText edt_name, edt_pwd;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    private void initViews() {
        edt_name = findViewById(R.id.edt_name);
        edt_pwd = findViewById(R.id.edt_pwd);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String name = edt_name.getText().toString();
                String pwd = edt_pwd.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(v.getContext(), "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                isCorrectInfo(name, pwd);
            }
        });
    }

    private void isCorrectInfo(String name, String pwd) {
        if (name.equals("kuyu") && pwd.equals("123456")) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
        }
    }
}
