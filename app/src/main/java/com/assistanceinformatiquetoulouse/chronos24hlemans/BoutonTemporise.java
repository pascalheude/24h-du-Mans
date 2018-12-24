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
    public BoutonTemporise(Context context) {
        super(context);
    }

    // Constructeur
    // Ce constructeur est nécessaire afin d'éviter une exception java.lang.NoSuchMethodException
    public BoutonTemporise(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // Méthode start
    public void start() {
        if (pCountDownTimer != null) {
            this.setEnabled(false);
            pCountDownTimer.start();
        }
        else {
        }
    }

    // Méthode ecrireTemporisation
    public void ecrireTemporisation(long temporisation) {
        pCountDownTimer = new TemporisationBouton(temporisation, temporisation);
    }
}
