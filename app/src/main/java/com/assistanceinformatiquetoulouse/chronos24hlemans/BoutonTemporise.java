package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.Button;

// Class BoutonTemporise
public class BoutonTemporise extends Button {
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
            BoutonTemporise.this.setEnabled(true);
        }
    }

    // Constructeur
    // TODO Mettre en paramètres la tempo de 5s
    public BoutonTemporise(Context context) {
        super(context);
        pCountDownTimer = new TemporisationBouton(5000, 5000);
    }

    // Constructeur
    // Ce constructeur est nécessaire afin d'éviter une exception java.lang.NoSuchMethodException
    public BoutonTemporise(Context context, AttributeSet attrs) {
        super(context, attrs);
        pCountDownTimer = new TemporisationBouton(5000, 5000);
    }

    // Méthode start
    public void start() {
        this.setEnabled(false);
        pCountDownTimer.start();
    }
}
