package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.util.Log;
import java.util.ArrayList;
import java.io.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

// Class Equipe
public class Equipe {
    // Atributs privés
    private static final String TAG = "Equipe";     // Tag pour le log
    private boolean pLireXML;                       // True pour lire le fichier XML
    private int pNbCoureurMax;                      // Nombre maximum de coureurs
    private String pNomFichierXML;                  // Nom du fichier XML
    private String pNomEquipe;                      // Nom de l'équipe
    private ArrayList<Coureur> pListeCoureurs;       // Liste des coureurs

    // Méthode lireXMLEquipe
    // Retourne une chaine contenant le texte du fichier XML si le fichier a pu être lu, null sinon
    private String lireXMLEquipe() {
        byte[] lBytes;
        String lString;
        File lFile;
        FileInputStream lFileInputStream;
        try {
            lFile = new File(pNomFichierXML);
            lFileInputStream = new FileInputStream(lFile);
            lBytes = new byte[(int) lFile.length()];
            lFileInputStream.read(lBytes);
            lFileInputStream.close();
            lString = "";
            for (byte b : lBytes) {
                lString += String.format("%c", b);
            }
            Log.i(TAG, "Fichier XML lu");
            return(lString);
        }
        catch(IOException e) {
            Log.i(TAG, "Erreur en lisant le fichier XML");
            return(null);
        }
        catch(Exception e) {
            Log.i(TAG, "Autre erreur en lisant le fichier XML");
            return(null);
        }
    }

    // Méthode ecrireChaine
    // Ecrit une chaine de caractères dans un fichier
    private void ecrireChaine(FileOutputStream fileOutputStream, String chaine) throws IOException {
        fileOutputStream.write(chaine.getBytes());
    }

    // Méthode ecrireXMLEquipe
    // Retourne true si le fichier a pu être écrit, false sinon
    private boolean ecrireXMLEquipe() {
        int i;
        FileOutputStream lFileOutputStream;
        try {
            lFileOutputStream = new FileOutputStream(pNomFichierXML);
            ecrireChaine(lFileOutputStream, "<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            ecrireChaine(lFileOutputStream, String.format("<EQUIPE nom=\"%s\" nombre=\"%d\">", pNomEquipe, pListeCoureurs.size()));
            for (i = 0; i < pListeCoureurs.size(); i++) {
                if (pListeCoureurs.get(i).lireEtatActif()) {
                    ecrireChaine(lFileOutputStream, String.format("<COUREUR actif=\"YES\">" + pListeCoureurs.get(i).lireNom() + "</COUREUR>"));
                }
                else {
                    ecrireChaine(lFileOutputStream, String.format("<COUREUR actif=\"NO\">" + pListeCoureurs.get(i).lireNom() + "</COUREUR>"));
                }
            }
            ecrireChaine(lFileOutputStream, "</EQUIPE>");
            lFileOutputStream.close();
            Log.i(TAG, "Fichier XML écrit");
            return (true);
        }
        catch(IOException e) {
            Log.i(TAG, "Impossible d'écrire le fichier XML");
            return (false);
        }
    }

    // Méthode ParserXML
    private void ParserXML(String chaine) {
        boolean lActif = true;
        String lTagName;
        String lText = "";
        try {
            XmlPullParserFactory lFactory = XmlPullParserFactory.newInstance();
            lFactory.setNamespaceAware(true);
            XmlPullParser lXmlPullParser =  lFactory.newPullParser();
            lXmlPullParser.setInput(new StringReader(chaine));
            int lEventType = lXmlPullParser.getEventType();
            while(lEventType != XmlPullParser.END_DOCUMENT) {
                switch(lEventType) {
                    case XmlPullParser.START_DOCUMENT :
                    case XmlPullParser.END_DOCUMENT :
                        break;
                    case XmlPullParser.START_TAG :
                        lTagName = lXmlPullParser.getName();
                        if (lTagName.equals("EQUIPE")) {
                            this.pNomEquipe = lXmlPullParser.getAttributeValue(null, "nom");
                        }
                        else if (lTagName.equals("COUREUR")) {
                            if (lXmlPullParser.getAttributeCount() == 1) {
                                lActif = lXmlPullParser.getAttributeValue(null, "actif").equals("YES");
                            }
                            else {
                            }
                        }
                        else {
                        }
                        break;
                    case XmlPullParser.END_TAG :
                        lTagName = lXmlPullParser.getName();
                        if (lTagName.equals("COUREUR")) {
                            if (pListeCoureurs.size() < pNbCoureurMax) {
                                pListeCoureurs.add(new Coureur(lText, lActif));
                            }
                            else {
                            }
                        }
                        else {
                        }
                        break;
                    case XmlPullParser.TEXT :
                        lText = lXmlPullParser.getText();
                        break;
                    default : ;
                }
                lEventType = lXmlPullParser.next();
            }
            Log.i(TAG, "Fichier XML parsé");
        }
        catch(XmlPullParserException e) {
            Log.i(TAG, "Erreur en parsant fichier XML");
            pListeCoureurs.clear();
        }
        catch(IOException e) {
            pListeCoureurs.clear();
        }
    }

    // Constructeur
    public Equipe(boolean lireXML, String nom_fichier, int nombre_coureur) {
        this.pNbCoureurMax = nombre_coureur;
        this.pLireXML = lireXML;
        pNomFichierXML = nom_fichier;
        pListeCoureurs = new ArrayList<Coureur>();
    }

    // Méthode lireXML
    public void lireXML() {
        String lChaine;
        if (this.pLireXML) {
            this.pLireXML = false;
            lChaine = lireXMLEquipe();
            if (lChaine != null) {
                ParserXML(lChaine);
            }
            else {
            }
        }
        else {
        }
    }

    // Méthode lireNomEquipe
    // Retourne le nom de l'équipe
    public String lireNomEquipe() {
        return (pNomEquipe);
    }

    // Méthode ecrireNomEquipe
    // Ecrit le nom de l'équipe
    public void ecrireNomEquipe(String nom) {
        this.pNomEquipe = nom;
    }

    // Méthode lireCoureur
    // Retourne le coureur à la position
    public String lireCoureur(int position) {
        if (position < pListeCoureurs.size()) {
            return (pListeCoureurs.get(position).lireNom());
        }
        else {
            return(null);
        }
    }

    // Méthode lireEtatActif
    // Retourne l'état actif à la position
    public boolean lireEtatActif(int position) {
        return (pListeCoureurs.get(position).lireEtatActif());
    }

    // Méthode ecrireEtatActif
    // Ecrit l'état actif à la position et met à jour la liste des coureurs actifs
    public void ecrireEtatActif(int position, boolean actif) {
        int i;
        pListeCoureurs.get(position).ecrireEtatActif(actif);
    }

    // Méthode lireListeCoureurs
    // Retourne la liste des coureurs
    public ArrayList<Coureur> lireListeCoureurs() {
        return (pListeCoureurs);
    }

    // Méthode ajouterCoureur
    // Ajoute le coureur dans l'équipe si cela est possible
    // Retourne true si le coureur a été ajouté, false sinon
    public boolean ajouterCoureur(String nom) {
        if (pListeCoureurs.size() < pNbCoureurMax) {
            pListeCoureurs.add(new Coureur(nom, true));
            return (ecrireXMLEquipe());
        } else {
            return (false);
        }
    }

    // Méthode supprimerCoureur
    // Supprime un coureur à la position
    // Retourne true si le fichier XML a été modifié, false sinon
    public boolean supprimerCoureur(int position) {
        if (position <= pListeCoureurs.size()) {
            pListeCoureurs.remove(position);
            return (ecrireXMLEquipe());
        }
        else {
            return (false);
        }
    }

    // Méthode lirePositionCoureur
    // Retourne la position du coureur dans la liste
    // Si le coureur n'est pas dans la liste, retourne pNbCoureurMax
    public int lirePositionCoureur(String nom) {
        int i;
        for (i = 0; i < pListeCoureurs.size(); i++) {
            if (pListeCoureurs.get(i).lireNom().equals(nom)) {
                return (i);
            } else {
            }
        }
        return (pNbCoureurMax);
    }

    // Methode lireProchainCoureur
    // Retourne le prochain coureur actif de la liste
    // Méthode jamais utilisée
    /* TBR => public String lireProchainCoureurActif(String coureur) {
        int position = pListeCoureursActifs.indexOf(coureur) + 1;
        if (position >= pListeCoureursActifs.size()) {
            position = 0;
        }
        else {
        }
        return (pListeCoureursActifs.get(position));
    } => TBR */

    // Méthode inverserCoureurs
    // Inverse la position de 2 coureurs dans toutes les listes
    // Méthode jamais utilisée
    /* TBR => public void inverserCoureurs(int position1, int position2) {
        int i;
        boolean lActif = pListeEtatsCoureurs.get(position1);
        String lCoureur = pListeCoureurs.get(position1);
        pListeEtatsCoureurs.set(position1, pListeEtatsCoureurs.get(position2));
        pListeCoureurs.set(position1, pListeCoureurs.get(position2));
        pListeEtatsCoureurs.set(position2, lActif);
        pListeCoureurs.set(position2, lCoureur);
        pListeCoureursActifs.clear();
        for(i=0;i < pListeCoureurs.size();i++) {
            if (pListeEtatsCoureurs.get(i)) {
                pListeCoureursActifs.add(pListeCoureurs.get(i));
            }
            else {
            }
        }
        ecrireXMLEquipe();
    } => TBR */

    // Méthode lireNombreMaxCoureur
    // Retourne le nommbre maximum de coureurs
    public int lireNombreMaxCoureur() {
        return (this.pNbCoureurMax);
    }
}