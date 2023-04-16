package com.example.mynciapp.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mynciapp.Adapter.MyPurposeAdapter;
import com.example.mynciapp.Common.Common;
import com.example.mynciapp.Common.SpacesItemDecoration;
import com.example.mynciapp.Model.Purpose;
import com.example.mynciapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_purpose)
    RecyclerView recycler_purpose;

    private BroadcastReceiver purposeDoneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Purpose> purposeArrayList = intent.getParcelableArrayListExtra(Common.KEY_PURPOSE_LOAD_DONE);

            MyPurposeAdapter adapter = new MyPurposeAdapter(getContext(),purposeArrayList);
            recycler_purpose.setAdapter(adapter);

        }
    };


    static BookingStep2Fragment instance;

    public static BookingStep2Fragment getInstance(){
        if(instance==null)
            instance = new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(purposeDoneReceiver, new IntentFilter(Common.KEY_PURPOSE_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(purposeDoneReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View itemView = inflater.inflate(R.layout.fragment_booking_step2, container, false);

        unbinder = ButterKnife.bind(this, itemView);

        initView();

        return itemView;
    }

    private void initView() {
        recycler_purpose.setHasFixedSize(true);
        recycler_purpose.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_purpose.addItemDecoration(new SpacesItemDecoration(4));
    }
}
