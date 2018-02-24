package com.assistanceinformatiquetoulouse.chronos24hlemans;

// Class Vibreur
public class Vibreur {
    // Attributs privés
    private long pRetard;
    private long pDuree;
    private Boolean pEtatActif;

    // Constructeur
    public Vibreur() {
        pEtatActif = true;
        pRetard = (long)66;
        pDuree = (long)20;
    }

    // Constructeur
    public Vibreur(Boolean etatActif, long retard, long duree) {
        pEtatActif = etatActif;
        pRetard = retard;
        pDuree = duree;
    }

    // Méthode lireEtatActif
    public Boolean lireEtatActif() { return (pEtatActif); }

    // Méthode ecrireEtatActif
    public void ecrireEtatActif(Boolean etatActif) { pEtatActif = etatActif; }

    // Méthode lireRetard
    public long lireRetard() { return (pRetard); }

    // Méthode ecrireRetard
    public void ecrireRetard(long retard) { pRetard = retard; }

    // Méthode lireDuree
    public long lireDuree() { return (pDuree); }

    // Méthode ecrireDuree
    public void ecrireDuree(long duree) { pDuree = duree; }
}
