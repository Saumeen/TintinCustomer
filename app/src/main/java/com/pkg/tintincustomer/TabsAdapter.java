package com.pkg.tintincustomer;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabsAdapter extends FragmentStatePagerAdapter {

    private Bundle bundle;
    private String[] tabtittles = new String[]{"Tiffin Menu","Home Menu"};
    public TabsAdapter(FragmentManager fm , Bundle bundle){
        super(fm);

        this.bundle = bundle;
    }


    @Override
    public int getCount() {
        return tabtittles.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabtittles[position];

    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                TiffinMenuFragment tiffinMenuFragment = new TiffinMenuFragment();
                tiffinMenuFragment.setArguments(bundle);
                return tiffinMenuFragment;

            case 1:
                HomeMenuFragment homeMenuFragment = new HomeMenuFragment();
                homeMenuFragment.setArguments(bundle);
                return homeMenuFragment;

            default:
                return null;
        }
    }
}
