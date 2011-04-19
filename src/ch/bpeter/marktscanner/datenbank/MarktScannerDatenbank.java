package ch.bpeter.marktscanner.datenbank;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class MarktScannerDatenbank extends SQLiteOpenHelper{

	private static final String DATENBANKNAME="markscanner.db";
	private static final int DATENBANK_VERSION=1;
	
	private static final String SQL_CREATE_DB="";
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
