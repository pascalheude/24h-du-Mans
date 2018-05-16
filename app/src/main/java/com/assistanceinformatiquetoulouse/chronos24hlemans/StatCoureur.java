package com.assistanceinformatiquetoulouse.chronos24hlemans;

// Class StatCoureur
public class StatCoureur {
    // Attributs privés
    private String pCoureur;
    private int pNbTour;
    private long pTempsMin;
    private long pTempsMoyen;
    private long pTempsMax;
    private long pTempsTotal;

    // Constructeur
    public StatCoureur() {
        pCoureur = new String("");
        pNbTour = 0;
        pTempsMin = Long.MAX_VALUE;
        pTempsMoyen = 0;
        pTempsMax = 0;
        pTempsTotal = 0;
    }

    // Méthode ajouterStat
    public void ajouterStat(String coureur, long temps) {
        pCoureur = coureur;
        if (temps < pTempsMin) {
            pTempsMin = temps;
        }
        else {
        }
        if (temps > pTempsMax) {
            pTempsMax = temps;
        }
        else {
        }
        pTempsMoyen = (pNbTour * pTempsMoyen + temps) / (pNbTour + 1);
        pNbTour++;
        pTempsTotal += temps;
    }

    // Méthode lireNom
    // Retourne le nom du coureur
    public String lireNom() { return (pCoureur); }

    // Méthode lireNombreTours
    // Retourne le nombre de tours
    public int lireNombreTours() { return (pNbTour); }

    // Méthode lireTempsMini
    // Retourne le temps mini
    public long lireTempsMini() { return (pTempsMin); }

    // Méthode lireTempsMoyen
    // Retourne le temps moyen
    public long lireTempsMoyen() { return (pTempsMoyen); }

    // Méthode lireTempsMax
    // Retourne le temps max
    public long lireTempsMax() { return (pTempsMax); }

    // Méthode lireTempsTotal
    // Retourne le temps total
    public long lireTempsTotal() { return (pTempsTotal); }
}
