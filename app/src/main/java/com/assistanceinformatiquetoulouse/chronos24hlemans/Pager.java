package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

// Class Pager
public class Pager extends FragmentStatePagerAdapter {
    // Attributs privés
    int tabCount;
    TabEquipe pTabEquipe;
    TabCourse pTabCourse;
    TabResultats pTabResultats;

    //Constructeur
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
        this.pTabEquipe = new TabEquipe();
        this.pTabCourse = new TabCourse();
        this.pTabResultats = new TabResultats();
    }

    // Méthode getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                return (this.pTabEquipe);
            case 1:
                return (this.pTabCourse);
            case 2:
                return (this.pTabResultats);
            default:
                return (null);
        }
}
    // Méthode getCount
    // Retourne le nombre d'onglets
    @Override
    public int getCount() {
        return (tabCount);
    }
}