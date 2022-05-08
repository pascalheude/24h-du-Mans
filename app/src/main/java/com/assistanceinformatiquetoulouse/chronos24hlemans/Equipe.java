package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.io.*;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

// Class Equipe
public class Equipe {
    // Atributs privés
    private static final String TAG = "Equipe";     // Tag pour le log
    private boolean pLireXML;
    private int pNbCoureurMax;                      // Nombre maximum de coureurs
    private Context pContext;                       // Context pour lire et ecrire le fichier XML
    private String pNomEquipe;                      // Nom de l'équipe
    private ArrayList<String> pListeCoureurs;       // Liste des coureurs
    private ArrayList<String> pListeCoureursActifs; // Liste des coureurs actifs
    private ArrayList<Boolean> pListeEtatsCoureurs; // Liste des états coureurs (true => actif, false => inactif)

    // Méthode lireXMLEquipe
    // Retourne une chaine contenant le texte du fichier XML si le fichier a pu être lu, null sinon
    private String lireXMLEquipe() {
        byte[] lBytes;
        String lString;
        File lFile;
        FileInputStream lFileInputStream;
        try {
            //lFile = new File(pContext.getFilesDir().getAbsolutePath(), "equipe.xml");
            lFile = new File(Chronos24hLeMansActivity.aAbsoluteInternalPath, "equipe.xml");
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
            //lFileOutputStream = new FileOutputStream(pContext.getFilesDir().getAbsolutePath() + File.separator + "equipe.xml");
            lFileOutputStream = new FileOutputStream(Chronos24hLeMansActivity.aAbsoluteInternalPath + File.separator + "equipe.xml");
            ecrireChaine(lFileOutputStream, "<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            ecrireChaine(lFileOutputStream, String.format("<EQUIPE nom=\"%s\" nombre=\"%d\">", pNomEquipe, pListeCoureurs.size()));
            for (i = 0; i < pListeCoureurs.size(); i++) {
                if (pListeEtatsCoureurs.get(i)) {
                    ecrireChaine(lFileOutputStream, String.format("    <COUREUR actif=\"YES\">" + pListeCoureurs.get(i) + "</COUREUR>"));
                }
                else {
                    ecrireChaine(lFileOutputStream, String.format("    <COUREUR actif=\"NO\">" + pListeCoureurs.get(i) + "</COUREUR>"));
                }
            }
            ecrireChaine(lFileOutputStream, "</EQUIPE>");
            lFileOutputStream.close();
            Log.i(TAG, "Fichier XML écrit");
            return (true);
        }
        catch(IOException e) {
            Toast.makeText(pContext, "Impossible d'écrire le fichier XML", Toast.LENGTH_SHORT).show();
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
                                pListeCoureurs.add(lText);
                                if (lActif) {
                                    pListeCoureursActifs.add(lText);
                                    pListeEtatsCoureurs.add(true);
                                }
                                else {
                                    pListeEtatsCoureurs.add(false);
                                }
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
            pListeCoureursActifs.clear();
            pListeEtatsCoureurs.clear();
        }
        catch(IOException e) {
            pListeCoureurs.clear();
            pListeCoureursActifs.clear();
            pListeEtatsCoureurs.clear();
        }
    }

    // Constructeur
    public Equipe(boolean lireXML, int nombre_coureur) {
        this.pNbCoureurMax = nombre_coureur;
        this.pLireXML = lireXML;
        this.pContext = null;
        pListeCoureurs = new ArrayList<String>(nombre_coureur);
        pListeCoureursActifs = new ArrayList<String>(nombre_coureur);
        pListeEtatsCoureurs = new ArrayList<Boolean>(nombre_coureur);
    }

    // Méthode lireXML
    public void lireXML(Context context) {
        String lChaine;
        this.pContext = context;
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
            return (pListeCoureurs.get(position));
        }
        else {
            return(null);
        }
    }

    // Méthode lireEtatActif
    // Retourne l'état actif à la position
    public Boolean lireEtatActif(int position) {
        return (pListeEtatsCoureurs.get(position));
    }

    // Méthode ecrireEtatActif
    // Ecrit l'état actif à la position et met à jour la liste des coureurs actifs
    public void ecrireEtatActif(int position, boolean actif) {
        int i;
        pListeEtatsCoureurs.set(position, actif);
        pListeCoureursActifs.clear();
        for(i=0;i < pListeCoureurs.size();i++) {
            if (pListeEtatsCoureurs.get(i)) {
                pListeCoureursActifs.add(pListeCoureurs.get(i));
            }
            else {
            }
        }
    }

    // Méthode lireListeCoureurs
    // Retourne la liste des coureurs
    public ArrayList<String> lireListeCoureurs() {
        return (pListeCoureurs);
    }

    // Méthode lireListeCoureursActifs
    // Retourne la liste de coureurs actifs
    public ArrayList<String> lireListeCoureursActifs() {
        return (pListeCoureursActifs);
    }

    // Méthode ajouterCoureur
    // Ajoute le coureur dans l'équipe si cela est possible
    // Retourne true si le coureur a été ajouté, false sinon
    public boolean ajouterCoureur(String coureur) {
        if (pListeCoureurs.size() < pNbCoureurMax) {
            pListeCoureurs.add(coureur);
            pListeCoureursActifs.add(coureur);
            pListeEtatsCoureurs.add(true);
            return (ecrireXMLEquipe());
        }
	    else {
	        return (false);
	    }
    }

    // Méthode supprimerCoureur
    // Supprime un coureur à la position
    public void supprimerCoureur(int position) {
        if (position <= pListeCoureurs.size()) {
            if (pListeEtatsCoureurs.get(position)) {
                pListeCoureursActifs.remove(pListeCoureurs.get(position));
            }
            else {
            }
            pListeCoureurs.remove(position);
            pListeEtatsCoureurs.remove(position);
            ecrireXMLEquipe();
        }
        else {
        }
    }

    // Methode lireProchainCoureur
    // Retourne le prochain coureur actif de la liste
    public String lireProchainCoureurActif(String coureur) {
        int position = pListeCoureursActifs.indexOf(coureur) + 1;
        if (position >= pListeCoureursActifs.size()) {
            position = 0;
        }
        else {
        }
        return (pListeCoureursActifs.get(position));
    }

    // Méthode inverserCoureurs
    // Inverse la position de 2 coureurs dans toutes les listes
    public void inverserCoureurs(int position1, int position2) {
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
    }

    // Méthode lireNombreMaxCoureur
    // Retourne le nommbre maximum de coureurs
    public int lireNombreMaxCoureur() {
        return (this.pNbCoureurMax);
    }
}