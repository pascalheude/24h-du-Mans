package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

// Class BoutonTemporise
public class BouttonTemporise extends Button {
    // Attributs privés
    TemporisationBouton pCountDownTimer;
    // Classe privée
    private class TemporisationBouton extends CountDownTimer {
        // Constructeur
        public TemporisationBouton(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        // Méthode onTick
        // Ne rien faire
        @Override
        public void onTick(long millisUntilFinished) { }

        // Méthode onFinish
        // Autoriser de nouveau le bouton
        @Override
        public void onFinish() {
            BouttonTemporise.this.setEnabled(true);
        }
    }

    // Constructeur
    // TODO Mettre en paramètres la tempo de 5s
    public BouttonTemporise(Context context) {
        super(context);
        pCountDownTimer = new TemporisationBouton(5000, 5000);
    }

    // Méthode start
    public void start() {
        this.setEnabled(false);
        pCountDownTimer.start();
    }
}
