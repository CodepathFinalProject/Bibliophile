package com.codepath.bibliophile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.codepath.bibliophile.R;

import static com.codepath.bibliophile.utils.SharedPreferenceUtility.getAddressLine1;
import static com.codepath.bibliophile.utils.SharedPreferenceUtility.getAddressLine2;
import static com.codepath.bibliophile.utils.SharedPreferenceUtility.getCity;
import static com.codepath.bibliophile.utils.SharedPreferenceUtility.getState;
import static com.codepath.bibliophile.utils.SharedPreferenceUtility.getZipcode;

public class ProfileFragment extends BaseFragment {

    EditText etAddress;

    public ProfileFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("bookshelf", "onCreateView: ");
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etAddress = (EditText) view.findViewById(R.id.etAddress);
        etAddress.setText(getAddressLine1(getContext()) + " " + getAddressLine2(getContext()) +", " + getCity(getContext()) + ", " + getState(getContext()) + " - " + getZipcode(getContext()));
        etAddress.setSelection(etAddress.length());
    }
}
