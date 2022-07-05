package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

// Class TabCourse
public class TabCourse extends Fragment {
    // Classe privée
    private class AlerteCoureur extends CountDownTimer {
        // Attributs privés
        private Vibrator pVibreur;

        // Constructeur
        public AlerteCoureur(long startTime, long interval) {
            super(startTime, interval);
            pVibreur = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        }

        // Méthode onTick
        // Ne rien faire
        @Override
        public void onTick(long millisUntilFinished) { }

        // Méthode onFinish
        // Faire vibrer le téléphone pendant 2 sec
        @Override
        public void onFinish() {
            if (pVibreur.hasVibrator()) {
                pVibreur.vibrate(Chronos24hLeMansActivity.aVibreur.lireDuree() * 100);
            }
            else {
            }
            if (Chronos24hLeMansActivity.aParametres.lireSonActif()) {
                pMediaPlayer.start();
            }
            else {
            }
        }
    }
    // Attributs privés
    private final long TEMPORISATION = 5000;
    private int pNbCoureur;
    private int pIndexCoureur;
    private String pNomCoureur;
    private AlerteCoureur pAlerteCoureur;
    private boolean pCourseDemarree;
    private boolean pPauseDemarree;
    private long pHeureDebut;
    private Date pDateDebut;
    private long pHeureFin;
    private Date pDateFin;
    private long pHeurePause;
    private BoutonTemporise pBoutonCoureur[];
    private Button pBoutonPause;
    private Button pBoutonArreter;
    private TextView pTextViewNbTour;
    private Chronometer pChronometre;
    private MediaPlayer pMediaPlayer;

    // Méthode programmerAlerteCoureur
    private void programmerAlerteCoureur(String nom) {
        long lTemps;
        // Lire le temps moyen du coureur
        lTemps = TabResultats.lireTempsMoyenCoureur(nom);
        // Si le temps moyen est non nul (1er tour) et le vibreur est activé alors programmer l'alerte et demmarer l'alerte
        if ((lTemps != 0) && (Chronos24hLeMansActivity.aVibreur.lireEtatActif())) {
            pAlerteCoureur = new AlerteCoureur(Chronos24hLeMansActivity.aVibreur.lireRetard() * lTemps / 100, lTemps);
            pAlerteCoureur.start();
        }
        else {
        }
    }

    // Méthode arreterAlerteCoureur
    private void arreterAlerteCoureur() {
        // Si l'alerte a été programmée alors arrêter l'alerte
        if (pAlerteCoureur != null) {
            pAlerteCoureur.cancel();
        }
        else {
        }
    }

    // Méthode afficherBouton
    private void afficherBouton() {
        int i;
        for (i = 0; i < pNbCoureur; i++) {
            if (TabEquipe.pEquipe.lireCoureur(i) != null) {
                pBoutonCoureur[i].setText(TabEquipe.pEquipe.lireCoureur(i));
                pBoutonCoureur[i].setEnabled(TabEquipe.pEquipe.lireEtatActif(i));
            } else {
                pBoutonCoureur[i].setText(String.format("Coureur %d", i + 1));
                pBoutonCoureur[i].setEnabled(false);
            }
        }
    }

    // Constructeur
    public TabCourse() {
        pNbCoureur = TabEquipe.pEquipe.lireNombreMaxCoureur();
    }

    // Méthode onCreate
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pIndexCoureur = pNbCoureur;
        pNomCoureur = "";
        pCourseDemarree = false;
        pPauseDemarree = false;
        pAlerteCoureur = null;
        pMediaPlayer = MediaPlayer.create(getContext(), R.raw.woodpecker);
    }

    // Méthode onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int i;
        int id;
        String lNom;
        final View lView = inflater.inflate(R.layout.tab_course, container, false);
        pBoutonCoureur = new BoutonTemporise[pNbCoureur];
        for (i = 0; i < pNbCoureur; i++) {
            switch (i) {
                default:
                    id = R.id.buttonCoureurBidon;
                    Toast.makeText(lView.getContext(), String.format("Coureur %d ignoré", i), Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    id = R.id.buttonCoureur1;
                    break;
                case 1:
                    id = R.id.buttonCoureur2;
                    break;
                case 2:
                    id = R.id.buttonCoureur3;
                    break;
                case 3:
                    id = R.id.buttonCoureur4;
                    break;
                case 4:
                    id = R.id.buttonCoureur5;
                    break;
                case 5:
                    id = R.id.buttonCoureur6;
                    break;
                case 6:
                    id = R.id.buttonCoureur7;
                    break;
                case 7:
                    id = R.id.buttonCoureur8;
                    break;
                case 8:
                    id = R.id.buttonCoureur9;
                    break;
                case 9:
                    id = R.id.buttonCoureur10;
                    break;
                case 10:
                    id = R.id.buttonCoureur11;
                    break;
                case 11:
                    id = R.id.buttonCoureur12;
                    break;
            }
            pBoutonCoureur[i] = (BoutonTemporise) lView.findViewById(id);
            pBoutonCoureur[i].ecrireTemporisation(TEMPORISATION);
            pBoutonCoureur[i].setBackgroundColor(getResources().getColor(R.color.bouton_nonselectionne));
        }
        afficherBouton();
        pBoutonPause = (Button) lView.findViewById(R.id.ButtonPause);
        pBoutonArreter = (Button) lView.findViewById(R.id.ButtonArreter);
        pTextViewNbTour = (TextView) lView.findViewById(R.id.textViewNbTour);
        pChronometre = (Chronometer) lView.findViewById(R.id.chronometer);
        if (!pCourseDemarree) {
            pBoutonPause.setEnabled(false);
            pBoutonArreter.setEnabled(false);
        } else {
        }
        pBoutonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i;
                if (! pPauseDemarree) {
                    pHeurePause = SystemClock.elapsedRealtime() - pChronometre.getBase();
                    pChronometre.stop();
                    arreterAlerteCoureur();
                    pBoutonPause.setText("Reprendre");
                    pBoutonArreter.setEnabled(false);
                    for (i = 0; i < pNbCoureur; i++) {
                        pBoutonCoureur[i].setEnabled(false);
                    }
                    pPauseDemarree = true;
                } else {
                    pChronometre.setBase(SystemClock.elapsedRealtime() - pHeurePause);
                    pChronometre.start();
                    pBoutonPause.setText("Pause");
                    pBoutonArreter.setEnabled(true);
                    afficherBouton();
                    pPauseDemarree = false;
                }
            }
        });
        pBoutonArreter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lNbTour;
                long lDuree;
                pBoutonPause.setEnabled(false);
                pBoutonArreter.setEnabled(false);
                pBoutonCoureur[pIndexCoureur].setBackgroundColor(getResources().getColor(R.color.bouton_nonselectionne));
                pChronometre.stop();
                arreterAlerteCoureur();
                pHeureFin = SystemClock.elapsedRealtime();
                pDateFin = new Date();
                lDuree = pHeureFin - pHeureDebut;
                lNbTour = TabResultats.ajouterResultat(pNomCoureur, pDateDebut, pDateFin, lDuree);
                pTextViewNbTour.setText(String.format("%03d", lNbTour));
                pCourseDemarree = false;
                pNomCoureur = "";
                pIndexCoureur = pNbCoureur;
            }
        });
        for (i = 0; i < pNbCoureur; i++) {
            final int final_i = i;
            pBoutonCoureur[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int lNbTour;
                    long lDuree;
                    if (pCourseDemarree) {
                        pBoutonCoureur[pIndexCoureur].setBackgroundColor(getResources().getColor(R.color.bouton_nonselectionne));
                        pChronometre.stop();
                        arreterAlerteCoureur();
                        pHeureFin = SystemClock.elapsedRealtime();
                        pDateFin = new Date();
                        lDuree = pHeureFin - pHeureDebut;
                        pHeureDebut = pHeureFin;
                        pChronometre.setBase(pHeureFin);
                        lNbTour = TabResultats.ajouterResultat(pNomCoureur, pDateDebut, pDateFin, lDuree);
                        pTextViewNbTour.setText(String.format("%03d", lNbTour));
                    } else {
                        pCourseDemarree = true;
                        pBoutonArreter.setEnabled(true);
                        pBoutonPause.setEnabled(true);
                        pHeureDebut = SystemClock.elapsedRealtime();
                        pChronometre.setBase(pHeureDebut);
                    }
                    pDateDebut = new Date();
                    pChronometre.start();
                    pIndexCoureur = final_i;
                    pNomCoureur = TabEquipe.pEquipe.lireCoureur(final_i);
                    programmerAlerteCoureur(pNomCoureur);
                    pBoutonCoureur[final_i].setEnabled(false);
                    pBoutonCoureur[final_i].setBackgroundColor(getResources().getColor(R.color.bouton_selectionne));
                    pBoutonCoureur[final_i].start();
                }
            });
        }
        return(lView);
    }

    // Méthode setUserVisibleHint
    // Cette méthode est appelée lorsque le fragment dans le pager redevient visible
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
            afficherBouton();
        }
        else {
        }
    }
}