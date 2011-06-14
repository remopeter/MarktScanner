package ch.bpeter.marktscanner.datenbank.tabellen;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import ch.bpeter.marktscanner.datenbank.MarktScannerDatenbank;
import ch.bpeter.marktscanner.interfaces.I_MarktScannerDAO;

public class ArtikelDAO implements I_MarktScannerDAO{
	private SQLiteDatabase db;
	
	public ArtikelDAO(MarktScannerDatenbank dbManager){
		db=dbManager.getWritableDatabase();
	}
	
	// Suchmethoden
	public ArrayList<ArtikelVO> findAll() {
	    Cursor cursor = db.query("T_ARTIKEL", new String[]{"ARTIKEL_ID","NAME","BARCODE", "FOTO", "MARKE_ID"}, null, null, "", "", "");
	    ArrayList<ArtikelVO> list=new ArrayList<ArtikelVO>();
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i=0;i<cursor.getCount();i++){
				ArtikelVO artikelVO = new ArtikelVO();
				artikelVO.setArtikel_id(cursor.getString(0));
				artikelVO.setName(cursor.getString(1));
				artikelVO.setBarcode(cursor.getString(2));
				artikelVO.setFoto(cursor.getString(3));
				artikelVO.setMarke_id(cursor.getString(4));
				list.add(artikelVO);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return list;
	}

	public ArrayList<ArtikelVO> findByAttributes(String[] attributes, String[] values) {
		String attr="";
		for(int i=0;i<attributes.length;i++){
			attr=attr+attributes[i]+"=?";
			if(!(i-attributes.length==-1)){
				attr=attr+", ";
			}
		}
	    Cursor cursor = db.query("T_ARTIKEL", new String[]{"ARTIKEL_ID","NAME","BARCODE", "FOTO", "MARKE_ID"}, attr, values, "", "", "");
		ArrayList<ArtikelVO> list=new ArrayList<ArtikelVO>();
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i=0;i<cursor.getCount();i++){
				ArtikelVO artikelVO = new ArtikelVO();
				artikelVO.setArtikel_id(cursor.getString(0));
				artikelVO.setName(cursor.getString(1));
				artikelVO.setBarcode(cursor.getString(2));
				artikelVO.setFoto(cursor.getString(3));
				artikelVO.setMarke_id(cursor.getString(4));
				list.add(artikelVO);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return list;
	}

	public ArtikelVO findById(String id) {
		Cursor cursor = db.query("T_ARTIKEL", new String[]{"ARTIKEL_ID","NAME","BARCODE", "FOTO", "MARKE_ID"}, "ARTIKEL_ID=?",  new String[]{id}, "", "", "");
		ArtikelVO artikelVO=new ArtikelVO();
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i=0;i<cursor.getCount();i++){
				artikelVO.setArtikel_id(cursor.getString(0));
				artikelVO.setName(cursor.getString(1));
				artikelVO.setBarcode(cursor.getString(2));
				artikelVO.setFoto(cursor.getString(3));
				artikelVO.setMarke_id(cursor.getString(4));
			}
		}
		cursor.close();
		return artikelVO;
	}

	public ArtikelVO save(Object obj) {
		ArtikelVO artikelVO=(ArtikelVO)obj;
		if(artikelVO.getArtikel_id()==null||artikelVO.getArtikel_id().equals("")){
			SQLiteStatement stmtInsert =db.compileStatement(
				"insert into T_ARTIKEL (NAME,BARCODE,FOTO,MARKE_ID) values (?,?,?,?)");
			if(artikelVO.getName()==null)
				stmtInsert.bindString(1,"null");
			else stmtInsert.bindString(1,artikelVO.getName());
			
			if(artikelVO.getBarcode()==null)
				stmtInsert.bindString(2,"null");
			else stmtInsert.bindString(2,artikelVO.getBarcode());
			
			if(artikelVO.getFoto()==null)
				stmtInsert.bindString(3,"null");
			else stmtInsert.bindString(3,artikelVO.getFoto());
			
			if(artikelVO.getMarke_id()==null||artikelVO.getMarke_id().equals(""))
				stmtInsert.bindString(4,"null");
			else stmtInsert.bindString(4,artikelVO.getMarke_id());
			long artikel_id=stmtInsert.executeInsert();
			artikelVO.setArtikel_id(new Long(artikel_id).toString());
			return artikelVO;
		}
		else{
			SQLiteStatement stmtInsert =db.compileStatement(
				"update T_ARTIKEL set NAME=?, BARCODE=?, FOTO=?, MARKE_ID=? where ARTIKEL_ID=?");
			if(artikelVO.getName()==null)
				stmtInsert.bindString(1,"null");
			else stmtInsert.bindString(1,artikelVO.getName());
			
			if(artikelVO.getBarcode()==null)
				stmtInsert.bindString(2,"null");
			else stmtInsert.bindString(2,artikelVO.getBarcode());
			
			if(artikelVO.getFoto()==null)
				stmtInsert.bindString(3,"null");
			else stmtInsert.bindString(3,artikelVO.getFoto());
			
			if(artikelVO.getMarke_id()==null||artikelVO.getMarke_id().equals(""))
				stmtInsert.bindString(4,"null");
			else stmtInsert.bindString(4,artikelVO.getMarke_id());
			stmtInsert.bindString(5,artikelVO.getArtikel_id());
			stmtInsert.execute();
			return artikelVO;
		}
	}
}
