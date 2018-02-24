package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

// Class CoureurAdapter
public class CoureurAdapter extends ArrayAdapter<String> {
    // Atributs privés
    private Context pContext;
    private int pLayoutResourceId;
    private int pTextViewResourceId;
    private ArrayList<String> pListeCoureurs;
    private LayoutInflater pInflater;

    // Constructeur
    public CoureurAdapter(Context context, int resource, int textViewResourceId, ArrayList<String> listeCoureurs) {
        super(context, resource, textViewResourceId, listeCoureurs);
        this.pContext = context;
        this.pLayoutResourceId = resource;
        this.pTextViewResourceId = textViewResourceId;
        this.pListeCoureurs = listeCoureurs;
        this.pInflater = LayoutInflater.from(pContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View lView = convertView;
        if (lView == null) {
            lView = pInflater.inflate(pLayoutResourceId, parent, false);
        }
        else {
        }
        if ((pListeCoureurs != null) && (position < pListeCoureurs.size())) {
            TextView lTextView = (TextView) lView.findViewById(pTextViewResourceId);
            lTextView.setText(pListeCoureurs.get(position));
        }
        else {
        }
        return(lView);
    }

    @Override
    public int getCount() {
        return(pListeCoureurs.size());
    }

    @Override
    public String getItem(int position) {
        return (pListeCoureurs.get(position));
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }
}