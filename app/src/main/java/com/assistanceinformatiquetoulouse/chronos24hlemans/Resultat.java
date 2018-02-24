package com.assistanceinformatiquetoulouse.chronos24hlemans;

// Class Resultat
public class Resultat {
    // Attributs privés
    int pNumero;
    String pNom;
    long pDuree;

    // Constructeur
    public Resultat(int numero, String nom, long duree) {
        this.pNumero = numero;
        this.pNom = nom;
        this.pDuree = duree;
    }

    // Méthode LireNuméro
    public int lireNuméro() {
        return this.pNumero;
    }

    // Méthode lireNomEquipe
    public String lireNom() {
        return this.pNom;
    }

    // Méthode lireRetard
    public long lireDuree() {
        return this.pDuree;
    }
}