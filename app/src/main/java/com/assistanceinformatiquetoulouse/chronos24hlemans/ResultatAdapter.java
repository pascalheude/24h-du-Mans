package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

// Class ResultatAdapter
public class ResultatAdapter extends ArrayAdapter<Resultat> {
    // Attributs privés
    private Context pContext;
    private int pLayoutResourceId;
    private LayoutInflater pInflater;
    private ArrayList<Resultat> pListeResultats;

    // Constructeur
    public ResultatAdapter(Context context, int resource, ArrayList<Resultat> listeResultats) {
        super(context, resource, listeResultats);
        this.pContext = context;
        this.pLayoutResourceId = resource;
        this.pInflater = LayoutInflater.from(context);
        pListeResultats = listeResultats;
    }
    @Override
    public int getCount() {
        return(pListeResultats.size());
    }

    @Override
    public Resultat getItem(int position) {
        return(super.getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return(super.getItemId(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View lView = convertView;
        if (lView == null) {
            lView = pInflater.inflate(pLayoutResourceId, parent, false);
        }
        else {
        }
        Resultat lResultat = pListeResultats.get(position);
        long lDuree = lResultat.lireDuree();
        TextView lTextViewNumeroTour = (TextView) lView.findViewById(R.id.textViewNumeroTour);
        TextView lTextViewTemps = (TextView) lView.findViewById(R.id.textViewTemps);
        TextView lTextViewNom = (TextView) lView.findViewById(R.id.textViewNom);
        lTextViewNumeroTour.setText(String.format("%03d", lResultat.lireNuméro()));
        lTextViewTemps.setText(String.format("%02d min %02d sec", (lDuree / (60 * 1000)) % 60, (lDuree / 1000) % 60));
        lTextViewNom.setText(String.format("%s", lResultat.lireNom()));
        return (lView);
    }
}
