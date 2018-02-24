package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

// Class BoutonTemporise
public class BouttonTemporise extends Button {
    // Classe privée
    private class A extends CountDownTimer {
        // Constructeur
        public A (long startTime, long interval) {
            super(startTime, interval);
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
    public BouttonTemporise(Context context) {
        super(context);
    }
}
