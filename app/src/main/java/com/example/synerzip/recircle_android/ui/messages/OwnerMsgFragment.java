package com.example.synerzip.recircle_android.ui.messages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.synerzip.recircle_android.R;

import butterknife.ButterKnife;

/**
 * Created by Prajakta Patil on 7/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

//TODO screen not implememted

public class OwnerMsgFragment extends Fragment {
    /**
     * empty constructor for OwnerMsgFragment
     */
    public OwnerMsgFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_owner_msg, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
