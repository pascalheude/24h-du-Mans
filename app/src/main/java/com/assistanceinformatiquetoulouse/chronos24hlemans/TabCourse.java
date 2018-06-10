package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
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
    private AlerteCoureur pAlerteCoureur;
    private boolean pCourseDemarree;
    private boolean pPauseDemarree;
    private long pHeureDebut;
    private Date pDateDebut;
    private long pHeureFin;
    private Date pDateFin;
    private long pHeurePause;
    private CoureurAdapter pCoureurAdapter;
    private Button pBoutonDemarrer;
    private Button pBoutonPause;
    private Button pBoutonArreter;
    private TextView pTextViewNbTour;
    private TextView pTextViewNonCoureurEnCourse;
    private Chronometer pChronometre;
    private Button pBoutonContinuer;
    private Button pBoutonPasserRelai;
    private TextView pTextViewProchainCoureur;
    private ListView pListViewProchainCoureur;
    private CountDownTimer pCountDownTimerContinuer;
    private CountDownTimer pCountDownTimerPasserRelai;
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

    // Méthode onCreate
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pCourseDemarree = false;
        pPauseDemarree = false;
        pAlerteCoureur = null;
        pMediaPlayer = MediaPlayer.create(getContext(), R.raw.woodpecker);
    }

    // Méthode onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View lView = inflater.inflate(R.layout.tab_course, container, false);
        pCoureurAdapter = new CoureurAdapter(lView.getContext(), R.layout.list_view_coureur, R.id.textViewCoureur, TabEquipe.pEquipe.lireListeCoureursActifs());
        pBoutonDemarrer = (Button) lView.findViewById(R.id.ButtonDemarrer);
        pBoutonPause = (Button) lView.findViewById(R.id.ButtonPause);
        pBoutonArreter = (Button) lView.findViewById(R.id.ButtonArreter);
        pTextViewNbTour = (TextView) lView.findViewById(R.id.textViewNbTour);
        pTextViewNonCoureurEnCourse = (TextView) lView.findViewById(R.id.textViewNomCoureurEnCourse);
        pChronometre = (Chronometer) lView.findViewById(R.id.chronometer);
        pBoutonContinuer = (Button) lView.findViewById(R.id.buttonContinuer);
        pBoutonPasserRelai = (Button) lView.findViewById(R.id.buttonPasserRelai);
        pTextViewProchainCoureur = (TextView) lView.findViewById(R.id.textViewProchainCoureur);
        pListViewProchainCoureur = (ListView) lView.findViewById(R.id.listViewProchainCoureur);
        pListViewProchainCoureur.setAdapter(pCoureurAdapter);
        // TODO Mettre en paramètres la tempo de 5s
        pCountDownTimerContinuer = new CountDownTimer(5000,5000) {
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {
                pBoutonContinuer.setEnabled(true);
            }
        };
        pCountDownTimerPasserRelai = new CountDownTimer(5000,5000) {
            @Override
            public void onTick(long millisUntilFinished) { }

            @Override
            public void onFinish() {
                pBoutonPasserRelai.setEnabled(true);
            }
        };
        if (! pCourseDemarree) {
            pBoutonDemarrer.setEnabled(false);
            pBoutonPause.setEnabled(false);
            pBoutonArreter.setEnabled(false);
            pBoutonContinuer.setEnabled(false);
            pBoutonPasserRelai.setEnabled(false);
        }
        else {
        }
        pBoutonDemarrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pBoutonDemarrer.setEnabled(false);
                pBoutonArreter.setEnabled(true);
                pBoutonPause.setEnabled(true);
                pBoutonContinuer.setEnabled(true);
                pBoutonPasserRelai.setEnabled(true);
                pTextViewNonCoureurEnCourse.setText(pTextViewProchainCoureur.getText());
                pHeureDebut = SystemClock.elapsedRealtime();
                pDateDebut = new Date();
                pChronometre.setBase(pHeureDebut);
                pChronometre.start();
                pTextViewProchainCoureur.setText(TabEquipe.pEquipe.lireProchainCoureurActif(pTextViewProchainCoureur.getText().toString()));
                pTextViewProchainCoureur.setBackgroundColor(Color.parseColor("#0080FF"));
            }
        });
        pBoutonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (! pPauseDemarree) {
                    pHeurePause = SystemClock.elapsedRealtime() - pChronometre.getBase();
                    pChronometre.stop();
                    arreterAlerteCoureur();
                    pBoutonPause.setText("Reprendre");
                    pBoutonArreter.setEnabled(false);
                    pBoutonContinuer.setEnabled(false);
                    pBoutonPasserRelai.setEnabled(false);
                    pPauseDemarree = true;
                }
                else {
                    pChronometre.setBase(SystemClock.elapsedRealtime() - pHeurePause);
                    pChronometre.start();
                    pBoutonPause.setText("Pause");
                    pBoutonArreter.setEnabled(true);
                    pBoutonContinuer.setEnabled(true);
                    pBoutonPasserRelai.setEnabled(true);
                    pPauseDemarree = false;
                }
            }
        });
        pBoutonArreter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lNbTour;
                long lDuree;
                pBoutonDemarrer.setEnabled(false);
                pBoutonPause.setEnabled(false);
                pBoutonArreter.setEnabled(false);
                pBoutonContinuer.setEnabled(false);
                pBoutonPasserRelai.setEnabled(false);
                pChronometre.stop();
                arreterAlerteCoureur();
                pHeureFin = SystemClock.elapsedRealtime();
                pDateFin = new Date();
                lDuree = pHeureFin - pHeureDebut;
                lNbTour = TabResultats.ajouterResultat(pTextViewNonCoureurEnCourse.getText().toString(), pDateDebut, pDateFin, lDuree);
                pTextViewNbTour.setText(String.format("%03d", lNbTour));
                pTextViewNonCoureurEnCourse.setText("");
                pTextViewProchainCoureur.setText("");
                pTextViewProchainCoureur.setBackgroundColor(Color.RED);
                pCourseDemarree = false;
            }
        });
        pBoutonContinuer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lNbTour;
                long lDuree;
                pChronometre.stop();
                arreterAlerteCoureur();
                pHeureFin = SystemClock.elapsedRealtime();
                pDateFin = new Date();
                lDuree = pHeureFin - pHeureDebut;
                pChronometre.setBase(pHeureFin);
                lNbTour = TabResultats.ajouterResultat(pTextViewNonCoureurEnCourse.getText().toString(), pDateDebut, pDateFin, lDuree);
                pTextViewNbTour.setText(String.format("%03d", lNbTour));
                pDateDebut = new Date();
                pHeureDebut = pHeureFin;
                pChronometre.start();
                programmerAlerteCoureur(pTextViewNonCoureurEnCourse.getText().toString());
                pBoutonContinuer.setEnabled(false);
                pCountDownTimerContinuer.start();
            }
        });
        pBoutonPasserRelai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int lNbTour;
                int position;
                long lDuree;
                if (pTextViewProchainCoureur.getText().equals("")) {
                    Toast.makeText(view.getContext(), "Sélectionner un coureur dans la liste", Toast.LENGTH_SHORT).show();
                }
                else {
                    pChronometre.stop();
                    arreterAlerteCoureur();
                    pHeureFin = SystemClock.elapsedRealtime();
                    pDateFin = new Date();
                    lDuree = pHeureFin - pHeureDebut;
                    pChronometre.setBase(pHeureFin);
                    lNbTour = TabResultats.ajouterResultat(pTextViewNonCoureurEnCourse.getText().toString(), pDateDebut, pDateFin, lDuree);
                    pTextViewNbTour.setText(String.format("%03d", lNbTour));
                    pDateDebut = new Date();
                    pHeureDebut = pHeureFin;
                    pChronometre.start();
                    programmerAlerteCoureur(pTextViewProchainCoureur.getText().toString());
                    pTextViewNonCoureurEnCourse.setText(pTextViewProchainCoureur.getText());
                    pTextViewProchainCoureur.setText(TabEquipe.pEquipe.lireProchainCoureurActif(pTextViewProchainCoureur.getText().toString()));
                    pTextViewProchainCoureur.setBackgroundColor(Color.parseColor("#0080FF"));
                    pBoutonPasserRelai.setEnabled(false);
                    pCountDownTimerPasserRelai.start();
                }
            }
        });
        pListViewProchainCoureur.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //pTextViewProchainCoureur.setText(TabEquipe.pEquipe.lireCoureur(position));
                pTextViewProchainCoureur.setText(pListViewProchainCoureur.getItemAtPosition(position).toString());
                pTextViewProchainCoureur.setBackgroundColor(Color.parseColor("#CECECE"));
                if (! pCourseDemarree) {
                    pBoutonDemarrer.setEnabled(true);
                    pCourseDemarree = true;
                }
                else {
                }
                return (true);
            }
        });
        //Returning the layout file after inflating
        //Change R.layout.tab_course in you classes
        return (lView);
    }

    // Méthode onResume
    @Override
    public void onResume() {
        super.onResume();
        pCoureurAdapter.notifyDataSetChanged();
    }
}