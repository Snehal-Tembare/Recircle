package com.example.synerzip.recircle_android.ui.messages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.synerzip.recircle_android.R;
import com.example.synerzip.recircle_android.models.user_messages.RootMessageInfo;

import butterknife.BindView;

/**
 * Created by Prajakta Patil on 7/6/17.
 * Copyright © 2017 Synerzip. All rights reserved
 */

//TODO the implementation is in progress

public class OwnerMsgFragment extends Fragment {

    private OwnerMessagesAdapter mOwnerMessagesAdapter;

    private RecyclerView mRecyclerView;

    private RootMessageInfo mMessageInfo;

    /**
     * empty constructor for OwnerMsgFragment
     */
    public OwnerMsgFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_owner_msg, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.owner_recycler_view);
    }

    /**
     * get message details from HomeActivity
     *
     * @param rootMessageInfo
     */
    public void getMessageDetails(RootMessageInfo rootMessageInfo) {

        this.mMessageInfo = rootMessageInfo;

        if (mMessageInfo != null) {
            mOwnerMessagesAdapter = new OwnerMessagesAdapter(getActivity(), mMessageInfo);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mOwnerMessagesAdapter);
        }
    }

}
