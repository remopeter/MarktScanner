package ch.bpeter.marktscanner.datenbank;

import android.app.SearchManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MarktScannerDatenbank extends SQLiteOpenHelper{

	private static final String DATENBANKNAME="markscanner.db";
	private static final int DATENBANK_VERSION=14;
	public static final String KEY_WORD = SearchManager.SUGGEST_COLUMN_TEXT_1;
    public static final String KEY_DEFINITION = SearchManager.SUGGEST_COLUMN_TEXT_2;
	
	private static final String SQL_CREATE_T_ARTIKEL="" +
			"CREATE TABLE T_ARTIKEL (" +
				"ARTIKEL_ID INTEGER NOT NULL CONSTRAINT PK_T_ARTIKEL PRIMARY KEY AUTOINCREMENT,  " +
				"NAME TEXT NOT NULL,  " +
				"BARCODE TEXT NOT NULL,  " +
				"FOTO TEXT,  " +
				"MARKE_ID INTEGER,  CONSTRAINT FK_T_ARTIKEL_1 FOREIGN KEY (MARKE_ID) REFERENCES T_MARKE (MARKE_ID));";
	
	private static final String SQL_CREATE_T_MARKE="" +
			"CREATE TABLE T_MARKE (" +
				"MARKE_ID INTEGER NOT NULL CONSTRAINT PK_T_MARKE PRIMARY KEY AUTOINCREMENT, " +
				"MARKENNAME VARCHAR(100) NOT NULL);";
	
	private static final String SQL_CREATE_T_HAENDLER="" +
			"CREATE TABLE T_HAENDLER (" +
				"HAENDLER_ID INTEGER NOT NULL CONSTRAINT PK_T_HAENDLER PRIMARY KEY AUTOINCREMENT, " +
				"HAENDLERNAME VARCHAR(100) NOT NULL);";
	
	private static final String SQL_INSERT_T_HAENDLER="INSERT INTO T_HAENDLER ('HAENDLERNAME') VALUES('Coop');";
	
	private static final String SQL_CREATE_T_PREIS="" +
			"CREATE TABLE T_PREIS (" +
				"PREIS TEXT, " +
				"ARTIKEL_ID INTEGER NOT NULL, " +
				"HAENDLER_ID INTEGER NOT NULL, " +
				"CONSTRAINT FK_T_PREIS_1 FOREIGN KEY (ARTIKEL_ID) REFERENCES T_ARTIKEL (ARTIKEL_ID), " +
				"CONSTRAINT FK_T_PREIS_2 FOREIGN KEY (HAENDLER_ID) REFERENCES T_HAENDLER (HAENDLER_ID));";

	private static final String SQL_BACKUP_ARTIKEL="INSERT INTO T_ARTIKEL_TMP (NAME, BARCODE) SELECT NAME, BARCODE FROM T_ARTIKEL;";
	private static final String SQL_CREATE_T_ARTIKEL_TMP="CREATE TABLE T_ARTIKEL_TMP (NAME TEXT, BARCODE TEXT);";
	private static final String SQL_DROP_T_ARTIKEL="DROP TABLE IF EXISTS T_ARTIKEL;";
	private static final String SQL_DROP_T_MARKE="DROP TABLE IF EXISTS T_MARKE;";
	private static final String SQL_DROP_T_HAENDLER="DROP TABLE IF EXISTS T_HAENDLER;";
	private static final String SQL_DROP_T_PREIS="DROP TABLE IF EXISTS T_PREIS;";
	private static final String SQL_DROP_T_ARTIKEL_TMP="DROP TABLE IF EXISTS T_ARTIKEL_TMP;";
	private static final String SQL_RESTORE_ARTIKEL="INSERT INTO T_ARTIKEL (NAME, BARCODE) SELECT NAME, BARCODE FROM T_ARTIKEL_TMP;";
	
	
	public MarktScannerDatenbank(Context context) {
		super(context, DATENBANKNAME, null, DATENBANK_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_DROP_T_ARTIKEL);
		db.execSQL(SQL_DROP_T_MARKE);
		db.execSQL(SQL_DROP_T_HAENDLER);
		db.execSQL(SQL_DROP_T_PREIS);
		db.execSQL(SQL_CREATE_T_ARTIKEL);
		db.execSQL(SQL_CREATE_T_MARKE);
		db.execSQL(SQL_CREATE_T_HAENDLER);
		db.execSQL(SQL_CREATE_T_PREIS);
		db.execSQL(SQL_INSERT_T_HAENDLER);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("*************************************DB neu erstellen!**************");
		db.execSQL(SQL_DROP_T_ARTIKEL_TMP);
		db.execSQL(SQL_CREATE_T_ARTIKEL_TMP);
		db.execSQL(SQL_BACKUP_ARTIKEL);
		onCreate(db);
		db.execSQL(SQL_RESTORE_ARTIKEL);
		db.execSQL(SQL_DROP_T_ARTIKEL_TMP);
	}
}
