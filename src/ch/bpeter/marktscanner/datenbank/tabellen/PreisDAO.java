package ch.bpeter.marktscanner.datenbank.tabellen;

import java.util.ArrayList;

import ch.bpeter.marktscanner.datenbank.MarktScannerDatenbank;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class PreisDAO implements I_MarktScannerDAO {
	private SQLiteDatabase db;
	
	public PreisDAO(MarktScannerDatenbank dbManager){
		db=dbManager.getWritableDatabase();
	}

	public ArrayList<PreisVO> findAll() {
		Cursor cursor = db.query("T_PREIS", new String[]{"PREIS","HAENDLER_ID","ARTIKEL_ID"}, null, null, "", "", "");
	    ArrayList<PreisVO> list=new ArrayList<PreisVO>();
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i=0;i<cursor.getCount();i++){
				PreisVO preisVO = new PreisVO();
				preisVO.setPreis(cursor.getString(0));
				preisVO.setHaendler_id(cursor.getString(1));
				preisVO.setArtikel_id(cursor.getString(2));
				list.add(preisVO);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return list;
	}

	public ArrayList<PreisVO> findByHaendlerID(String haendler_id) {
		String []value=new String[]{haendler_id};
		String []attr=new String[]{"HAENDLER_ID"};
		return findByAttributes(attr, value);
	}
	
	public ArrayList<PreisVO> findByArtikelID(String artikel_id) {
		String []value=new String[]{artikel_id};
		String []attr=new String[]{"ARTIKEL_ID"};
		return findByAttributes(attr, value);
	} 
	
	public ArrayList<PreisVO> findByAttributes(String[] attributes, String[] values) {
		String attr="";
		for(int i=0;i<attributes.length;i++){
			attr=attr+attributes[i]+"=?";
			if(!(i-attributes.length==-1)){
				attr=attr+", ";
			}
		}
	    Cursor cursor = db.query("T_PREIS", new String[]{"PREIS","HAENDLER_ID","ARTIKEL_ID"}, attr, values, "", "", "");
		ArrayList<PreisVO> list=new ArrayList<PreisVO>();
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i=0;i<cursor.getCount();i++){
				PreisVO preisVO = new PreisVO();
				preisVO.setPreis(cursor.getString(0));
				preisVO.setHaendler_id(cursor.getString(1));
				preisVO.setArtikel_id(cursor.getString(2));
				list.add(preisVO);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return list;
	}

	/*
	 * Die Tabelle T_PREIS enthält kein PK. findById() gibt deshalb null zurück.
	 */
	public PreisVO findById(String id) {
		return null;
	}

	public PreisVO save(Object obj) {
		PreisVO preisVO=(PreisVO)obj;
		if((preisVO.getHaendler_id()==null||preisVO.getHaendler_id().equals("0"))&&(preisVO.getArtikel_id()==null||preisVO.getArtikel_id().equals("0"))){
			SQLiteStatement stmtInsert =db.compileStatement(
				"insert into T_PREIS (PREIS,HAENDLER_ID,ARTIKEL_ID) values (?,?,?)");
			stmtInsert.bindString(1,preisVO.getPreis());
			stmtInsert.bindString(2,preisVO.getHaendler_id());
			stmtInsert.bindString(3,preisVO.getArtikel_id());
			stmtInsert.execute();
			stmtInsert.executeInsert();
			return preisVO;
		}
		else{
			SQLiteStatement stmtInsert =db.compileStatement(
				"update T_PREIS set PREIS='"+preisVO.getPreis()+"' where HAENDLER_ID="+preisVO.getHaendler_id()+" AND ARTIKEL_ID="+preisVO.getArtikel_id());
			stmtInsert.execute();
			return preisVO;
		}
	}

}
