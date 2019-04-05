package com.kuyuzhiqi.testdemo.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.kuyuzhiqi.testdemo.R;

public class ToolsFragment extends Fragment {

    public ToolsFragment() {
        // Required empty public constructor
    }

    public static ToolsFragment newInstance(String param1, String param2) {
        ToolsFragment fragment = new ToolsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tools, container, false);
    }


}
