package ch.bpeter.marktscanner.datenbank;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MarktScannerDatenbank extends SQLiteOpenHelper{

	private static final String DATENBANKNAME="markscanner.db";
	private static final int DATENBANK_VERSION=1;
	
	private static final String SQL_CREATE_DB="" +
			"CREATE TABLE T_ARTIKEL " +
			"(ARTIKEL_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
			"NAME TEXT NOT NULL, " +
			"FOTO BLOB, " +
			"BARCODE INTEGER NOT NULL);";
    /**"create table HAENDLER" +
			"create table ARTIKEL_GRUPPE" +
			"create table PREIS";**/
	private static final String SQL_DROP_DB="";
	
	
	public MarktScannerDatenbank(Context context) {
		super(context, DATENBANKNAME, null, DATENBANK_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_DB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DROP_DB);
		onCreate(db);
	}

}
