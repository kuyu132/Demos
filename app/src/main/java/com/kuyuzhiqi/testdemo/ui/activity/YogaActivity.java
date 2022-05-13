package com.kuyuzhiqi.testdemo.ui.activity;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.soloader.SoLoader;
import com.facebook.yoga.YogaEdge;
import com.facebook.yoga.YogaFlexDirection;
import com.facebook.yoga.YogaNode;
import com.facebook.yoga.YogaNodeJNIFinalizer;

import java.util.ArrayList;

public class YogaActivity extends AppCompatActivity {

    private static final int VIEW_WIDTH = 200;
    private static final int VIEW_HEIGHT = 200;

    private ArrayList<View> mViewList = new ArrayList<>();
    private ArrayList<YogaNode> mYogaNodeList = new ArrayList<>();

    private String[][] colors = new String[][]{
            new String[]{"#6200ea", "#651fff", "#7c4dff", "#b388ff"},
            new String[]{"#d50000", "#ff1744", "#ff5252", "#ff8a80"},
            new String[]{"#c51162", "#f50057", "#ff4081", "#ff80ab"},
            new String[]{"#aa00ff", "#d500f9", "#e040fb", "#ea80fc"}
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoLoader.init(this, false);
        FrameLayout container = new FrameLayout(this);
        Point windowPoint = new Point();
        getWindowManager().getDefaultDisplay().getSize(windowPoint);
        float screenWidth = windowPoint.x;
        float screenHeight = windowPoint.y;

        YogaNode root = new YogaNodeJNIFinalizer();
        root.setWidth(screenWidth);
        root.setHeight(screenHeight);
        root.setFlexDirection(YogaFlexDirection.COLUMN);

        createRowNodeAndView(root, 0);
        createRowNodeAndView(root, 1);
        createRowNodeAndView(root, 2);
        createRowNodeAndView(root, 3);

        root.calculateLayout(screenWidth, screenHeight);

        for (int i = 0; i < mViewList.size(); i++) {
            View v = mViewList.get(i);
            YogaNode n = mYogaNodeList.get(i);
            YogaNode r = n.getOwner();
            v.setX(r.getLayoutX() + n.getLayoutX());
            v.setY(r.getLayoutY() + n.getLayoutY());
            container.addView(v);
        }

        setContentView(container);
    }

    private void createRowNodeAndView(YogaNode root, int index) {
        YogaNode row = new YogaNodeJNIFinalizer();
        row.setHeight(VIEW_HEIGHT);
        row.setWidth(VIEW_WIDTH * 4);
        row.setFlexDirection(YogaFlexDirection.ROW);
        row.setMargin(YogaEdge.ALL, 20);

        for (int i = 0; i < 4; i++) {
            YogaNode n = new YogaNodeJNIFinalizer();
            n.setWidth(VIEW_WIDTH);
            n.setHeight(VIEW_HEIGHT);
            View v = createView(colors[index][i]);
            row.addChildAt(n, i);
            mYogaNodeList.add(n);
            mViewList.add(v);
        }

        root.addChildAt(row, index);
    }

    private View createView(String color) {
        View view = new View(this);
        view.setBackgroundColor(Color.parseColor(color));
        ViewGroup.LayoutParams lp = new FrameLayout.LayoutParams(VIEW_WIDTH, VIEW_HEIGHT);
        view.setLayoutParams(lp);
        return view;
    }
}