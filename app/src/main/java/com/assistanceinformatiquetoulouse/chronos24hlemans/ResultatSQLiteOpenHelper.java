package com.assistanceinformatiquetoulouse.chronos24hlemans;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// Class ResultatSQLiteOpenHelper
public class ResultatSQLiteOpenHelper extends SQLiteOpenHelper {
    // Attributs
    private final static int kVersion = 1;
    private final static String kNomDeFichier = "chronos24hLeMans.db";

    // Constructeur
    public ResultatSQLiteOpenHelper(Context context) {
        super(context, kNomDeFichier, null, kVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String lSQLFillTable =  "CREATE TABLE IF NOT EXISTS RESULTAT (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT, chrono TEXT, heure_debut TEXT, heure_fin TEXT);";
        db.execSQL(lSQLFillTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int i;
        for(i = oldVersion;i < newVersion;i++) {
            if ((i + 1) == 2) {

            }
            else if ((i + 1 )== 3) {

            }
            else {
            }
        }
    }
}
