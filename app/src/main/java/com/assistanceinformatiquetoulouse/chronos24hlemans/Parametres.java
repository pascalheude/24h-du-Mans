package com.assistanceinformatiquetoulouse.chronos24hlemans;

// Class Parametres
public class Parametres
{
    // Attributs privés
    private float pDistance;
    private boolean pSonActif;

    // Atributs publics
    public static final float aDistance = (float)4.185;

    // Constructeur
    public Parametres() {
        this.pDistance = aDistance;
        this.pSonActif = true;
    }

    // Méthode ecrireDistance
    public void ecrireDistance(float distance) {
        this.pDistance = distance;
    }

    // Méthode lireDistance
    public float lireDistance() {
        return(this.pDistance);
    }

    // Méthode ecrireSonActif
    public void ecrireSonActif(boolean sonActif) {
        this.pSonActif = sonActif; }

    // Méthode lireSonActif
    public boolean lireSonActif(){
        return(this.pSonActif); }
}