package com.example.synerzip.recircle_android.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.synerzip.recircle_android.R;

public class BottomBarFragment extends Fragment {

    public BottomBarFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bottom_bar, container, false);

        return view;
    }
}
