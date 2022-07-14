package com.assistanceinformatiquetoulouse.chronos24hlemans;

// Class coureur
public class Coureur {
    // Attributs privés
    private String pNom;
    private boolean pActif;

    // Constructeur
    public Coureur(String nom, boolean actif) {
        this.pNom = nom;
        this.pActif = actif;
    }

    // Méthode lireNom
    public String lireNom() {
        return (this.pNom);
    }

    // Méthode lireEtatActif
    public boolean lireEtatActif() {
        return (this.pActif);
    }

    // Méthode ecrireEtatActif
    public void ecrireEtatActif(boolean actif) {
        this.pActif = actif;
    }
}
