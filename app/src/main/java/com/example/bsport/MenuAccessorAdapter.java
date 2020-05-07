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
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                PrivateAreaFragment privateAreaFragment = new PrivateAreaFragment();
                return privateAreaFragment;
            case 2:
                ActivitiesListFragment activitiesListFragment = new ActivitiesListFragment();
                return activitiesListFragment;
            case 3:
                SportFacFragment sportFacFragment = new SportFacFragment();
                return sportFacFragment;
            case 4:
                MyActivitiesFragment myActivitiesFragment = new MyActivitiesFragment();
                return myActivitiesFragment;
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
                return "איזור אישי";
            case 2:
                return "רשימת פעילויות";
            case 3:
                return "ספורט בעיר";
            case 4:
                return "הפעילויות שלי";
            default:
                return null;

        }
    }
}
