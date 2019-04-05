package com.kuyuzhiqi.testdemo.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.kuyuzhiqi.acrossdemo.ui.FingerPrintActivity;
import com.kuyuzhiqi.testdemo.R;
import com.tencent.mmkv.MMKV;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tv_hello;
    private RecyclerView rc_content;
    private ContentAdapter mContentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String dir = MMKV.initialize(this);
        Toast.makeText(this, dir, Toast.LENGTH_LONG).show();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        tv_hello = findViewById(R.id.tv_hello);
        rc_content = findViewById(R.id.rc_content);
        List<String> contentList = new ArrayList<>();
        contentList.add("Xposed测试");
        contentList.add("登录Xposed劫持");
        contentList.add("MMKV测试");
        contentList.add("吸顶滑动列表");
        contentList.add("吸顶滑动tab");
        contentList.add("指纹识别");
        contentList.add("Path Animation");
        mContentAdapter = new ContentAdapter(contentList);
        rc_content.setAdapter(mContentAdapter);
        rc_content.setLayoutManager(new LinearLayoutManager(this));
        rc_content.addOnItemTouchListener(new SimpleClickListener() {
            @Override public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0:
                        tv_hello.setText("天平");
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, MMKVActivity.class));
                        break;
                    case 3:
                        startActivity(new Intent(MainActivity.this, ScrollActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, ScrollTabActivity2.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, FingerPrintActivity.class));
                        break;
                    case 6:
                        startActivity(new Intent(MainActivity.this, PathAnimationActivity.class));
                        break;
                }
            }

            @Override public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        new Thread() {
            @Override public void run() {
                tv_hello.setText("线程改变tv值");
            }
        }.start();
    }

    class ContentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public ContentAdapter(@Nullable List<String> data) {
            super(R.layout.item_content, data);
        }

        @Override protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_title, item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
