package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

// Classe ParametresActivity
public class ParametresActivity extends AppCompatActivity {
    // TODO Implementer la prise en compte de la checkbox Sortir
    // Attributs privés
    private Vibreur pVibreur;
    private Toolbar pToolbar;
    private CheckBox pCheckBoxVibreur;
    private SeekBar pSeekBarRetard;
    private SeekBar pSeekBarDuree;
    private TextView pTextViewRetard;
    private TextView pTextViewDuree;
    private CheckBox pCheckBoxSortir;

    // Méthode onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametres);
        pToolbar = (Toolbar) findViewById(R.id.toolbar);
        pCheckBoxVibreur = (CheckBox) findViewById(R.id.checkBoxVibreur);
        pSeekBarRetard = (SeekBar) findViewById(R.id.seekBarRetard);
        pSeekBarDuree = (SeekBar) findViewById(R.id.seekBarDuree);
        pTextViewRetard = (TextView) findViewById(R.id.textViewRetard);
        pTextViewDuree = (TextView) findViewById(R.id.textViewDuree);
        pCheckBoxSortir = (CheckBox) findViewById(R.id.checkBoxSortir);
        setSupportActionBar(pToolbar);
        Intent lIntent = getIntent();
        pVibreur = new Vibreur(lIntent.getBooleanExtra("vibreur_actif_in", true),
                               lIntent.getLongExtra("vibreur_retard_in", (long)66),
                               lIntent.getLongExtra("vibreur_duree_in", (long)20));
        pSeekBarRetard.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lProgress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lProgress = progress;
                pTextViewRetard.setText(String.format("%d %%", lProgress));
                pVibreur.ecrireRetard((long) lProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pTextViewRetard.setText(String.format("%d %%", lProgress));
                pVibreur.ecrireRetard((long) lProgress);
            }
        });
        pSeekBarDuree.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lProgress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 10) {
                    lProgress = 10;
                    pSeekBarDuree.setProgress(10);
                }
                else {
                    lProgress = progress;
                }
                pTextViewDuree.setText(String.format("%d s", lProgress / 10));
                pVibreur.ecrireDuree((long) lProgress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pTextViewDuree.setText(String.format("%d s", lProgress / 10));
                pVibreur.ecrireDuree((long) lProgress);
            }
        });
        pCheckBoxVibreur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pVibreur.ecrireEtatActif(pCheckBoxVibreur.isChecked());
                pSeekBarRetard.setEnabled(pCheckBoxVibreur.isChecked());
                pSeekBarDuree.setEnabled(pCheckBoxVibreur.isChecked());
            }
        });
        if (pVibreur.lireEtatActif()) {
            pCheckBoxVibreur.setChecked(true);
            pSeekBarRetard.setEnabled(true);
            pSeekBarDuree.setEnabled(true);
        }
        else {
            pCheckBoxVibreur.setChecked(false);
            pSeekBarRetard.setEnabled(false);
            pSeekBarDuree.setEnabled(false);
        }
        pSeekBarRetard.setProgress((int) pVibreur.lireRetard());
        pTextViewRetard.setText(String.format("%d %%", (int) pVibreur.lireRetard()));
        pSeekBarDuree.setProgress((int) pVibreur.lireDuree());
        pTextViewDuree.setText(String.format("%d s", (int) (pVibreur.lireDuree() / 10)));
    }

    // Méthode finish
    // On écrit l'état et la durée du vibreur dans l'intent
    @Override
    public void finish() {
        Intent lIntent = new Intent();
        lIntent.putExtra("vibreur_actif_out", pVibreur.lireEtatActif());
        lIntent.putExtra("vibreur_retard_out", pVibreur.lireRetard());
        lIntent.putExtra("vibreur_duree_out", pVibreur.lireDuree());
        setResult(RESULT_OK, lIntent);
        super.finish();
    }
}
