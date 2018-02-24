package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

// Class Chronos24hLeMansActivity
public class Chronos24hLeMansActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    // Attributs privés
    private static final int REQUEST_CODE = 10;
    private String pFichier;
    private static Context pContext;
    private TabLayout pTabLayout;
    private ViewPager pViewPager;
    private Toolbar pToolbar;
    private ResultatSQLiteOpenHelper pResultatSQLiteOpenHelper;
    private static SQLiteDatabase pSQLiteDatabase;
    // Attributs publics
    public static String pAbsoluteInternalPath;
    public static String pAbsoluteExternalPath;
    public static Vibreur aVibreur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pFichier = null;
        pContext = getApplicationContext();
        setContentView(R.layout.activity_chronos24h_le_mans);
        // Bloquer l'application en mode portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Bloquer l'extinction automatique de l'écran
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        pAbsoluteInternalPath = this.getFilesDir().getAbsolutePath();
        pAbsoluteExternalPath = this.getExternalFilesDir(null).getAbsolutePath();
        aVibreur = new Vibreur();
        pResultatSQLiteOpenHelper = new ResultatSQLiteOpenHelper(this);
        pSQLiteDatabase = pResultatSQLiteOpenHelper.getWritableDatabase();
        pResultatSQLiteOpenHelper.onCreate(pSQLiteDatabase);
        //Adding toolbar to the activity
        pToolbar = (Toolbar) findViewById(R.id.toolbar);
        pToolbar.showOverflowMenu();
        setSupportActionBar(pToolbar);
        //Initializing the tablayout
        pTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        // Ajout des 3 onglets
        pTabLayout.addTab(pTabLayout.newTab().setText("EQUIPE"));
        pTabLayout.addTab(pTabLayout.newTab().setText("COURSE"));
        pTabLayout.addTab(pTabLayout.newTab().setText("RESULTATS"));
        pTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        //Initializing viewPager
        pViewPager = (ViewPager) findViewById(R.id.pager);
        //Creating our pager adapter
        Pager adapter = new Pager(getSupportFragmentManager(), pTabLayout.getTabCount());
        //Adding adapter to pager
        pViewPager.setAdapter(adapter);
        pViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(pTabLayout));
        //Adding onTabSelectedListener to swipe views
        pTabLayout.setOnTabSelectedListener(this);
// TODO : les lignes suivantes ne fonctionnent pas (suppression des barres haute et basse)
        /*getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        // remove the following flag for version < API 19
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch(item.getItemId()) {
            case R.id.about :
                AlertDialog.Builder lAlertDialog = new AlertDialog.Builder(this);
                lAlertDialog.setTitle("Chronos24hLeMans\nVersion " + this.getString(R.string.version));
                lAlertDialog.setMessage("Gestion des temps pour les coureurs des 24h du Mans rollers\n© AIT 2017 (pascalh)\n\nassistanceinformatiquetoulouse@gmail.com");
                lAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }});
                lAlertDialog.setIcon(R.drawable.ic_roller);
                lAlertDialog.create().show();
                break;
            case R.id.parametres :
                Intent lIntent = new Intent(this, ParametresActivity.class);
                lIntent.putExtra("vibreur_actif_in", aVibreur.lireEtatActif());
                lIntent.putExtra("vibreur_retard_in", aVibreur.lireRetard());
                lIntent.putExtra("vibreur_duree_in", aVibreur.lireDuree());
                startActivityForResult(lIntent, REQUEST_CODE);
                break;
            case R.id.effacer :
                // Effacer le contenu de la database qui cootient les résultats
                new AlertDialog.Builder(this)
                        .setTitle("Confirmation")
                        .setMessage("Suppression database ?")
                        .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                pSQLiteDatabase.execSQL("DROP TABLE RESULTAT;");
                                pResultatSQLiteOpenHelper.onCreate(pSQLiteDatabase);
                                Toast.makeText(pContext, "Database effacée", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }).show();
                break;
            case R.id.exporter :
                // Dumper la database des résultats dans un fichier CSV
                pFichier = exporterDatabaseDansCSV(TabEquipe.pEquipe.lireNomEquipe());
                if (pFichier != null) {
                    new AlertDialog.Builder(this)
                            .setTitle("Exportation")
                            .setMessage(String.format("Le fichier suivant a été crée :\n%s", pFichier))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) { }
                            }).show();
                    // Rend selectable le 2ème menu (R.id.envoyer)
                    pToolbar.getMenu().getItem(2).setEnabled(true);
                }
                else {
                }
                break;
            case R.id.envoyer :
                // Envoyer le dump de la database des résultats par courriel (adresse dans strings.xml/courriel
                if (pFichier != null) {
                    final String lCourriel = this.getString(R.string.courriel);
                    new AlertDialog.Builder(this)
                            .setTitle("Courriel")
                            .setMessage("Envoyer le fichier exporté par courriel sur " + lCourriel)
                            .setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent lIntent = new Intent(Intent.ACTION_SENDTO);
                                    lIntent.setType("text/plain");
                                    lIntent.setData(Uri.parse("mailto:"+ lCourriel));
                                    lIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { lCourriel });
                                    lIntent.putExtra(Intent.EXTRA_SUBJECT, String.format("Résultats équipe %s", TabEquipe.pEquipe.lireNomEquipe()));
                                    lIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + pFichier));
                                    try {
                                        startActivity(Intent.createChooser(lIntent, "Choisir le client de courriel"));
                                    }
                                    catch(android.content.ActivityNotFoundException e) {
                                    }
                                }
                            })
                            .setNegativeButton("NON", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) { }
                            }).show();
                    item.setEnabled(false);
                }
                else {
                }
                break;
            case R.id.quitter :
                // Sortir de l'application
                this.finish();
                break;
            default : ;
        }
        return (true);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) { }

    @Override
    public void onTabReselected(TabLayout.Tab tab) { }

    // Méthode insererDatabase
    public static void insererDatabase(ContentValues contentValues) {
        pSQLiteDatabase.insert("RESULTAT", null, contentValues);
    }

    // Méthode exporterDatabaseDansCSV
    private static String exporterDatabaseDansCSV(String nom) {
        Cursor lCursor;
        try {
            int lNbLigne;
            int lNbColonne;
            lCursor = pSQLiteDatabase.rawQuery("select * from RESULTAT", null);
            File lFile = new File(pAbsoluteExternalPath + File.separator + nom + ".csv");
            FileWriter lFileWriter = new FileWriter(lFile);

            BufferedWriter lBufferedWriter = new BufferedWriter(lFileWriter);
            lNbLigne = lCursor.getCount();
            lNbColonne = lCursor.getColumnCount();
            if (lNbLigne > 0) {
                lCursor.moveToFirst();
                for (int i = 0; i < lNbColonne; i++) {
                    if (i != (lNbColonne - 1)) {
                        lBufferedWriter.write(lCursor.getColumnName(i) + ",");
                    }
                    else {
                        lBufferedWriter.write(lCursor.getColumnName(i));
                    }
                }
                lBufferedWriter.newLine();
                for (int i = 0; i < lNbLigne; i++) {
                    lCursor.moveToPosition(i);
                    for (int j = 0; j < lNbColonne; j++) {
                        if (j != (lNbColonne - 1)) {
                            lBufferedWriter.write(lCursor.getString(j) + ",");
                        }
                        else {
                            lBufferedWriter.write(lCursor.getString(j));
                        }
                    }
                    lBufferedWriter.newLine();
                }
                lBufferedWriter.flush();
                lBufferedWriter.close();
            }
            else {
            }
            return (lFile.getAbsolutePath());
        } catch (IOException e) {
            Toast.makeText(pContext, String.format("Erreur en écrivant le fichier %s", pAbsoluteExternalPath + File.separator + "resultats.csv"), Toast.LENGTH_LONG).show();
            return (null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE) && (resultCode == RESULT_OK)) {
            if (intent.hasExtra("vibreur_actif_out")) {
                Boolean lActif = intent.getBooleanExtra("vibreur_actif_out", true);
                aVibreur.ecrireEtatActif(lActif);
            }
            else {
            }
            if (intent.hasExtra("vibreur_retard_out")) {
                long lRetard = intent.getLongExtra("vibreur_retard_out", (long)66);
                aVibreur.ecrireRetard(lRetard);
            }
            else {
            }
            if (intent.hasExtra("vibreur_duree_out")) {
                long lDuree = intent.getLongExtra("vibreur_duree_out", (long)20);
                aVibreur.ecrireRetard(lDuree);
            }
            else {
            }
        }
        else {
        }
    }

    @Override
    public void onBackPressed() {
        // Ne rien faire (désactivation du bouton retour)
        // super.onBackPressed();
    }
}