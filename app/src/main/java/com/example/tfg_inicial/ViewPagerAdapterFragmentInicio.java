package com.example.tfg_inicial;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapterFragmentInicio extends FragmentStateAdapter {
    public ViewPagerAdapterFragmentInicio(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ViewPagerAdapterFragmentInicio(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerAdapterFragmentInicio(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        //return null;

        switch (position) {
            case 0:
                return new inicio_FragmentCarteleras();
            case 1:
                return new inicio_FragmentPeleadores();
            default:
                return new inicio_FragmentCarteleras();
        }
    }

    @Override
    public int getItemCount() {
        return 2; //Hay 2 pestañas
    }
}
