package com.example.mynciapp.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BookingStep3Fragment extends Fragment {

    static BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance(){
        if(instance==null)
            instance = new BookingStep3Fragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable @androidx.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull @androidx.annotation.NonNull LayoutInflater inflater, @Nullable @androidx.annotation.Nullable ViewGroup container, @Nullable @androidx.annotation.Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
