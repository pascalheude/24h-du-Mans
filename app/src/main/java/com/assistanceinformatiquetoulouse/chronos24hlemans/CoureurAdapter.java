package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

// Class CoureurAdapter
public class CoureurAdapter extends ArrayAdapter<Coureur> {
    // Atributs privés
    private Context pContext;
    private int pLayoutResourceId;
    private ArrayList<Coureur> pListeCoureurs;

    // Constructeur
    public CoureurAdapter(Context context, int resource, int textViewResourceId, ArrayList<Coureur> listeCoureurs) {
        super(context, resource, textViewResourceId, listeCoureurs);
        this.pContext = context;
        this.pLayoutResourceId = resource;
        this.pListeCoureurs = listeCoureurs;
    }

    private class ViewHolder {
        private int pPosition;
        private CheckBox pCheckBoxActif;
        private TextView pNomCoureur;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder lViewHolder;
        if (convertView == null) {
            lViewHolder = new ViewHolder();
            LayoutInflater lLayoutInflater = (LayoutInflater) pContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = lLayoutInflater.inflate(pLayoutResourceId, parent, false);
            lViewHolder.pPosition = position;
            lViewHolder.pCheckBoxActif = (CheckBox) convertView.findViewById(R.id.checkboxCoureurActif);
            lViewHolder.pNomCoureur = (TextView) convertView.findViewById(R.id.textViewCoureur);
            convertView.setTag(lViewHolder);
        }
        else {
            lViewHolder = (ViewHolder) convertView.getTag();
        }
        if ((pListeCoureurs != null) && (position < pListeCoureurs.size())) {
            lViewHolder.pCheckBoxActif.setChecked(pListeCoureurs.get(position).lireEtatActif());
            lViewHolder.pNomCoureur.setText(pListeCoureurs.get(position).lireNom());
        }
        else {
        }
        lViewHolder.pCheckBoxActif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pListeCoureurs.get(lViewHolder.pPosition).lireEtatActif()) {
                    pListeCoureurs.get(lViewHolder.pPosition).ecrireEtatActif(false);
                } else {
                    pListeCoureurs.get(lViewHolder.pPosition).ecrireEtatActif(true);
                }
            }
        });
        return (convertView);
    }

    @Override
    public int getCount() {
        return (pListeCoureurs.size());
    }

    @Override
    public Coureur getItem(int position) {
        return (pListeCoureurs.get(position));
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }
}