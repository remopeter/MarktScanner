package ch.bpeter.marktscanner.datenbank.tabellen;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import ch.bpeter.marktscanner.datenbank.MarktScannerDatenbank;

public interface I_MarktScannerDAO {
	
	public ArrayList findAll();
	
	public ArrayList findByAttributes(String []attributes, String []values);
	
	public Object findById(String id);
	
	public Object save(Object obj);
}
