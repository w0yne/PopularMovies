/*
 * Created by Stev Wayne on 2016/10/20
 * Copyright (c) 2016 Stev Wayne. All rights reserved.
 */

package com.w0yne.nanodegree.popularmovies.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseFragment extends Fragment {

    private Unbinder mUnbinder;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
