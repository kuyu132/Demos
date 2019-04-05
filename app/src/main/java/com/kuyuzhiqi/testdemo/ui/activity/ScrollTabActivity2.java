package com.kuyuzhiqi.testdemo.ui.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;
import com.kuyuzhiqi.testdemo.R;
import com.kuyuzhiqi.testdemo.ui.fragment.TaskFragment;
import com.kuyuzhiqi.testdemo.ui.fragment.ToolsFragment;
import com.kuyuzhiqi.testdemo.utils.DisplayUtils;
import com.kuyuzhiqi.testdemo.utils.TabViewUtils;
import com.kuyuzhiqi.testdemo.widget.ScrollLinearLayout2;

public class ScrollTabActivity2 extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private View mBlankView;
    private ScrollLinearLayout2 sll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_tab2);

        initViews();
    }

    private void initViews() {
        int height = DisplayUtils.getStatusBarHeight(this);
        Toast.makeText(this, "height:" + height, Toast.LENGTH_SHORT).show();
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(new ColorDrawable(Color.TRANSPARENT));
        TabHost.TabSpec tabSpec = mTabHost.newTabSpec(TaskFragment.class.getName())
                .setIndicator(TabViewUtils.getTabItemView(this, R.mipmap.ic_task_select, "任务",
                        R.color.colorPrimary));
        mTabHost.addTab(tabSpec, TaskFragment.class, null);

        TabHost.TabSpec tabSpec1 = mTabHost.newTabSpec(ToolsFragment.class.getName())
                .setIndicator(TabViewUtils.getTabItemView(this, R.mipmap.ic_score_statistics, "工具", R.color.colorPrimary));
        mTabHost.addTab(tabSpec1, ToolsFragment.class, null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override public void onTabChanged(String tabId) {

            }
        });

        int blankViewHeight = (int) (DisplayUtils.getScreenHeightPixels(this)
                        - DisplayUtils.getAppContentHeight(this)*(1.0f/15)
                        - DisplayUtils.getStatusBarHeight(this));
        sll = findViewById(R.id.sll);
        sll.setContentViewTranslateY(blankViewHeight);
    }
}
