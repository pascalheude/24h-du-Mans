package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Class TabResultats
public class TabResultats extends Fragment {
    // Atributs privés
    private final int kNbResultatMax = 300;
    // TODO Supprimer la déclaration en double
    private final int kNbCoureurMax = 20;
    private static ResultatAdapter pResultatAdapter;
    private TextView pTextViewNomCoureur;
    private TextView pTextViewNbTour;
    private TextView pTextViewTempsMini;
    private TextView pTextViewTempsMoyen;
    private TextView pTextViewTempsMax;
    private GridView pGridViewResultats;
    private static ArrayList<Resultat> pListeResultats;
    private static ArrayList<StatCoureur> pListeStatCoureurs;

    // Constructeur
    public TabResultats() {
        pListeResultats = new ArrayList<Resultat>(kNbResultatMax);
        pListeStatCoureurs = new ArrayList<StatCoureur>(kNbCoureurMax);
    }

    // Méthode onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View lView = inflater.inflate(R.layout.tab_resultats, container, false);
        pResultatAdapter = new ResultatAdapter(lView.getContext(), R.layout.grid_view_resultat, pListeResultats);
        pTextViewNomCoureur = (TextView) lView.findViewById(R.id.textViewNomCoureur);
        pTextViewNbTour = (TextView) lView.findViewById(R.id.textViewNbTour);
        pTextViewTempsMini = (TextView) lView.findViewById(R.id.textViewTempsMini);
        pTextViewTempsMoyen = (TextView) lView.findViewById(R.id.textViewTempsMoyen);
        pTextViewTempsMax = (TextView) lView.findViewById(R.id.textViewTempsMax);
        pGridViewResultats = (GridView) lView.findViewById(R.id.gridViewResultats);
        pGridViewResultats.setAdapter(pResultatAdapter);
        pGridViewResultats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int lPosition;
                String lNomCoureur;
                StatCoureur lStatCoureur;
                lNomCoureur = pListeResultats.get(position).lireNom();
                lPosition = positionCoureur(lNomCoureur);
                if (lPosition == -1) {
                    pTextViewNomCoureur.setText(lNomCoureur);
                    pTextViewNbTour.setText("-");
                    pTextViewTempsMini.setText("-");
                    pTextViewTempsMoyen.setText("-");
                    pTextViewTempsMax.setText("-");
                }
                else {
                    lStatCoureur = pListeStatCoureurs.get(lPosition);
                    pTextViewNomCoureur.setText(lStatCoureur.lireNom());
                    pTextViewNbTour.setText(String.format("%02d", lStatCoureur.lireNombreTours()));
                    pTextViewTempsMini.setText(String.format("%02d:%02d", (lStatCoureur.lireTempsMini() / (60 * 1000)) % 60, (lStatCoureur.lireTempsMini() / 1000) % 60));
                    pTextViewTempsMoyen.setText(String.format("%02d:%02d", (lStatCoureur.lireTempsMoyen() / (60 * 1000)) % 60, (lStatCoureur.lireTempsMoyen() / 1000) % 60));
                    pTextViewTempsMax.setText(String.format("%02d:%02d", (lStatCoureur.lireTempsMax() / (60 * 1000)) % 60, (lStatCoureur.lireTempsMax() / 1000) % 60));
                }
                // Toast.makeText(view.getContext(), String.format("Position sélectionnée : %d", position), Toast.LENGTH_SHORT).show();
            }
        });
        return (lView);
    }

    // Méthode positionCoureur
    // Retourne la position du coureur dans la liste des stats en fonction de son nom, -1 si le coureur n'a pas été trouvé
    private int positionCoureur(String nom) {
        for(StatCoureur lStatCoureur : pListeStatCoureurs) {
            if (nom.equals(lStatCoureur.lireNom())) {
                return (pListeStatCoureurs.indexOf(lStatCoureur));
            }
            else {
            }
        }
        return (-1);
    }

    // Méthode lireTempsMoyenCoureur
    // Retourne le temps moyen du coureur, 0 si le coureur n'a pas été trouvé
    public static long lireTempsMoyenCoureur(String nom) {
        for(StatCoureur lStatCoureur : pListeStatCoureurs) {
            if (nom.equals(lStatCoureur.lireNom())) {
                return (lStatCoureur.lireTempsMoyen());
            }
            else {
            }
        }
        return (0);
    }
    // Méthode ajouterResultat
    // Ajoute un résultat à la liste des résutats
    public static int ajouterResultat(String nom, Date debut, Date fin, long duree) {
        int i = 0;
        int lPosition = -1;
        // Ajouter le résultat à la liste des résultats, au début et affichage
        Resultat lResutat = new Resultat(pListeResultats.size() + 1, nom, duree);
        pListeResultats.add(0, lResutat);
        pResultatAdapter.notifyDataSetChanged();
        // Ajouter la statistique du coureur
        StatCoureur lStatCoureur;
        while((i < pListeStatCoureurs.size()) && (lPosition == -1)) {
            if (pListeStatCoureurs.get(i).lireNom().equals(nom)) {
                lPosition = i;
            }
            else {
            }
            i++;
        }
        if (lPosition == -1) {
            lStatCoureur = new StatCoureur();
            lStatCoureur.ajouterStat(nom, duree);
            pListeStatCoureurs.add(lStatCoureur);
        }
        else {
            lStatCoureur = pListeStatCoureurs.get(lPosition);
            lStatCoureur.ajouterStat(nom, duree);
            pListeStatCoureurs.set(lPosition, lStatCoureur);
        }
        // Ecrire dans la base de données
        ContentValues lValues = new ContentValues();
        SimpleDateFormat lSDF = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        lValues.put("nom", nom);
        lValues.put("chrono", String.format("%02d:%02d", (duree / (60 * 1000)) % 60, (duree / 1000) % 60));
        lValues.put("heure_debut", lSDF.format(debut));
        lValues.put("heure_fin", lSDF.format(fin));
        Chronos24hLeMansActivity.insererDatabase(lValues);
        return (pListeResultats.size());
    }

    @Override
    public void onResume() {
        super.onResume();
        pResultatAdapter.notifyDataSetChanged();
    }
}

