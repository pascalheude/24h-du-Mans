package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.assistanceinformatiquetoulouse.chronos24hlemans.Chronos24hLeMansActivity.aXMLFile;

// Class TabEquipe
public class TabEquipe extends Fragment {
    // Attributs privés
    private final int kNbCoureurMax = 12;
    private int pNbCoureurs;
    private int pCoureurSelectionne;
    private CoureurAdapter pCoureurAdapter;
    private EditText pEditTextNomEquipe;
    private TextView pTextViewNbCoureurs;
    private EditText pEditTextNomCoureur;
    private Button pBoutonAjouter;
    private TextView pTextViewActif;
    private CheckBox pBoiteActif;
    private ListView pListViewEquipe;
    // Attributs publics
    public static Equipe pEquipe;

    // Constructeur
    public TabEquipe() {
        String lXML_file;
        pNbCoureurs = 0;
        pCoureurSelectionne = 0;
        //lXML_file = Chronos24hLeMansActivity.aAbsoluteInternalPath + File.separator + getContext().getString(R.string.fichier_xml);
        pEquipe = new Equipe(true, aXMLFile, kNbCoureurMax);
    }

    //Méthode onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View lView = inflater.inflate(R.layout.tab_equipe, container, false);
        pEquipe.lireXML();
        pNbCoureurs = pEquipe.lireListeCoureurs().size();
        pCoureurAdapter = new CoureurAdapter(lView.getContext(), R.layout.list_view_coureur, R.id.textViewCoureur, pEquipe.lireListeCoureurs());
        pEditTextNomEquipe = (EditText) lView.findViewById(R.id.editTextNomEquipe);
        if (pEquipe.lireNomEquipe() != null) {
            pEditTextNomEquipe.setText(pEquipe.lireNomEquipe());
        }
        else {
        }
        pTextViewNbCoureurs = (TextView) lView.findViewById(R.id.textViewNbCoureurs);
        pTextViewNbCoureurs.setText(String.format("%d", pNbCoureurs));
        pEditTextNomCoureur = (EditText) lView.findViewById(R.id.editTextNomCoureur);
        pBoutonAjouter = (Button) lView.findViewById(R.id.buttonAjouter);
        pTextViewActif = (TextView) lView.findViewById(R.id.textViewActif);
        pBoiteActif = (CheckBox) lView.findViewById(R.id.checkBoxActif);
        pListViewEquipe = (ListView) lView.findViewById(R.id.listViewEquipe);
        pListViewEquipe.setAdapter(pCoureurAdapter);
        pBoutonAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pEditTextNomCoureur.length() == 0) {
                    Toast.makeText(view.getContext(), "Entrez un nom pour ajouter", Toast.LENGTH_LONG).show();
                } else if (pNbCoureurs < kNbCoureurMax) {
                    Toast.makeText(view.getContext(), "Nombre maximum de coureurs atteint", Toast.LENGTH_LONG).show();
                } else if (! pEquipe.ajouterCoureur(pEditTextNomCoureur.getText().toString())) {
                    Toast.makeText(view.getContext(), "Impossible d'écrire le fichier XML", Toast.LENGTH_LONG).show();
                } else {
                    pNbCoureurs++;
                    pTextViewNbCoureurs.setText(String.format("%d", pNbCoureurs));
                    pCoureurAdapter.notifyDataSetChanged();
                    Toast.makeText(view.getContext(), String.format("Coureur %s ajouté", pEditTextNomCoureur.getText().toString()), Toast.LENGTH_SHORT).show();
                    pEditTextNomCoureur.setText("");
                }
            }
        });
        pBoiteActif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Attention, l'etat de la CheckBox semble être modifié automatiquement
                // C'est pour cela que le code qui suit fait l'inverse de ce qui est prévu
                if (pBoiteActif.isChecked()) {
                    pEquipe.ecrireEtatActif(pCoureurSelectionne, true);
                }
                else {
                    pEquipe.ecrireEtatActif(pCoureurSelectionne, false);
                }
            }
        });
        pEditTextNomEquipe.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (! hasFocus) {
                    if (pEditTextNomEquipe.getText().length() != 0) {
                        pEquipe.ecrireNomEquipe(pEditTextNomEquipe.getText().toString());
                        Toast.makeText(view.getContext(), String.format("Nom de l'équipe %s enregistré", pEditTextNomEquipe.getText().toString()), Toast.LENGTH_LONG).show();
                    }
                    else {
                    }
                }
            }
        });
        pListViewEquipe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pCoureurSelectionne = position;
                pTextViewActif.setVisibility(View.VISIBLE);
                pBoiteActif.setVisibility(View.VISIBLE);
                pBoiteActif.setChecked(pEquipe.lireEtatActif(position));
                //Toast.makeText(view.getContext(), String.format("Selection %d", position), Toast.LENGTH_SHORT).show();
            }
        });
        pListViewEquipe.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int lPosition = position;
                final String lCoureur = pEquipe.lireCoureur(position);
                final View lView = view;
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Confirmation")
                        .setMessage(String.format("Suppression coureur %s ?", lCoureur))
                        .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (pEquipe.supprimerCoureur(lPosition)) {
                                    Toast.makeText(lView.getContext(), String.format("Coureur %s supprimé", lCoureur), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(lView.getContext(), String.format("Coureur %s supprimé, mais impossible d'écrire le fichier XML", lCoureur), Toast.LENGTH_LONG).show();
                                }
                                pNbCoureurs--;
                                pTextViewNbCoureurs.setText(String.format("%d", pNbCoureurs));
                                pCoureurAdapter.notifyDataSetChanged();
                                pTextViewActif.setVisibility(View.INVISIBLE);
                                pBoiteActif.setVisibility(View.INVISIBLE);
                                pBoiteActif.setChecked(false);
                            }
                        })
                        .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .show();
                return (true);
            }
        });
        //Returning the layout file after inflating
        //Change R.layout.tab_equipe in you classes
        return (lView);
    }

    @Override
    public void onPause() {
        super.onPause();
        pTextViewActif.setVisibility(View.INVISIBLE);
        pBoiteActif.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        pBoiteActif.setVisibility(View.INVISIBLE);
    }
}