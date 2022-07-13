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
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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
    private static String pAbsoluteExternalPath;
    private static Context pContext;
    private TabLayout pTabLayout;
    private ViewPager pViewPager;
    private Toolbar pToolbar;
    private ResultatSQLiteOpenHelper pResultatSQLiteOpenHelper;
    private static SQLiteDatabase pSQLiteDatabase;
    // Attributs publics
    public static String aXMLFile;
    public static Vibreur aVibreur;
    public static Parametres aParametres;

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
        aXMLFile = this.getFilesDir().getAbsolutePath() + File.separator + getString(R.string.fichier_xml);
        pAbsoluteExternalPath = this.getExternalFilesDir(null).getAbsolutePath();
        aVibreur = new Vibreur();
        aParametres = new Parametres();
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
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                                                         View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                                         View.SYSTEM_UI_FLAG_LOW_PROFILE);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) != 0) {
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                                         WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                else {
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                                                                     View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                                                     View.SYSTEM_UI_FLAG_LOW_PROFILE);
                }
            }
        });
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
                lAlertDialog.setMessage("Gestion des temps pour les coureurs des 24h du Mans rollers\n© AIT 2022 (pascalh)\n\nassistanceinformatiquetoulouse@gmail.com");
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
            case R.id.supprimer:
                File lFile = new File(aXMLFile);
                if (lFile.isFile()) {
                    lFile.delete();
                    Toast.makeText(pContext, String.format("Fichier %s supprimé", aXMLFile), Toast.LENGTH_LONG).show();
                } else {
                }
                try {
                    lFile.createNewFile();
                } catch (IOException e) {
                    Toast.makeText(pContext, String.format("Erreur création fichier %s", aXMLFile), Toast.LENGTH_LONG).show();
                }
                if (Build.VERSION.SDK_INT >= 21) {
                    this.finishAndRemoveTask();
                } else {
                    this.finishAffinity();
                    System.exit(0);
                }
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
                String lNomEquipe = TabEquipe.pEquipe.lireNomEquipe();
                if (lNomEquipe != null) {
                    pFichier = exporterDatabaseDansCSV(lNomEquipe);
                    if (pFichier != null) {
                        new AlertDialog.Builder(this)
                                .setTitle("Exportation")
                                .setMessage(String.format("Le fichier suivant a été crée :\n%s", pFichier))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                }).show();
                        // Rend selectable le menu (R.id.envoyer)
                        pToolbar.getMenu().findItem(R.id.envoyer).setEnabled(true);
                    } else {
                    }
                } else {
                    Toast.makeText(pContext, "Nom d'équipe non renseigné\nExport impossible", Toast.LENGTH_LONG).show();
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
                if (Build.VERSION.SDK_INT >= 21) {
                    this.finishAndRemoveTask();
                } else {
                    this.finishAffinity();
                    System.exit(0);
                }
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
            Toast.makeText(pContext, String.format("Erreur en écrivant le fichier %s", pAbsoluteExternalPath + File.separator + nom + ".csv"), Toast.LENGTH_LONG).show();
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
                aVibreur.ecrireDuree(lDuree);
            }
            else {
            }
            if (intent.hasExtra("distance_out")) {
                float distance = intent.getFloatExtra("distance", Parametres.aDistance);
                aParametres.ecrireDistance(distance);
            }
            else {
            }
            if (intent.hasExtra("son_actif_out")) {
                boolean sonActif = intent.getBooleanExtra("son_actif_out", true);
                aParametres.ecrireSonActif(sonActif);
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