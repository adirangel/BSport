package com.example.bsport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MenuAccessorAdapter extends FragmentPagerAdapter
{

    public MenuAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new PrivateAreaFragment();
            case 2:
                return new ActivitiesListFragment();
            case 3:
                return new SportFacFragment();
            case 4:
                return new MyActivitiesFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "דף\nהבית";
            case 1:
                return "איזור\nאישי";
            case 2:
                return "רשימת פעילויות";
            case 3:
                return "ספורט בעיר";
            case 4:
                return "הפעילות שלי";
            default:
                return null;

        }
    }
}
