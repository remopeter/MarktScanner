package ch.bpeter.marktscanner.datenbank.tabellen;

import java.util.ArrayList;
import java.util.Iterator;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import ch.bpeter.marktscanner.datenbank.MarktScannerDatenbank;
import ch.bpeter.marktscanner.interfaces.I_MarktScannerDAO;

public class HaendlerDAO implements I_MarktScannerDAO{
	private SQLiteDatabase db;
	
	public HaendlerDAO(MarktScannerDatenbank dbManager){
		db=dbManager.getWritableDatabase();
	}
	// Suchmethoden
	public ArrayList<HaendlerVO> findAll(){
	    Cursor cursor = db.query("T_HAENDLER", new String[]{"HAENDLER_ID","HAENDLERNAME"}, null, null, "", "", "HAENDLERNAME");
	    ArrayList<HaendlerVO> list=new ArrayList<HaendlerVO>();
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i=0;i<cursor.getCount();i++){
				HaendlerVO haendlerVO = new HaendlerVO();
				haendlerVO.setHaendler_id(cursor.getString(0));
				haendlerVO.setHaendlername(cursor.getString(1));
				list.add(haendlerVO);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return list;
	}
	
	public ArrayList<HaendlerVO> findByHaendlername(String name){
	    Cursor cursor = db.query("T_HAENDLER", new String[]{"HAENDLER_ID","HAENDLERNAME"}, "HAENDLERNAME=?",  new String[]{name}, "", "", "HAENDLERNAME");
		ArrayList<HaendlerVO> list=new ArrayList<HaendlerVO>();
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i=0;i<cursor.getCount();i++){
				HaendlerVO haendlerVO = new HaendlerVO();
				haendlerVO.setHaendler_id(cursor.getString(0));
				haendlerVO.setHaendlername(cursor.getString(1));
				list.add(haendlerVO);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return list;
	}
	
	public HaendlerVO findById(String id){
		Cursor cursor = db.query("T_HAENDLER", new String[]{"HAENDLER_ID","HAENDLERNAME"}, "HAENDLER_ID=?",  new String[]{id}, "", "", "");
		HaendlerVO haendlerVO=new HaendlerVO();
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i=0;i<cursor.getCount();i++){
				haendlerVO.setHaendler_id(cursor.getString(0));
				haendlerVO.setHaendlername(cursor.getString(1));
			}
		}
		cursor.close();
		return haendlerVO;
	}
	
	public HaendlerVO save(Object obj){
		HaendlerVO haendlerVO=(HaendlerVO)obj;
		if(haendlerVO.getHaendler_id()==null||haendlerVO.getHaendler_id().equals("")){
			SQLiteStatement stmtInsert =db.compileStatement(
				"insert into T_HAENDLER (HAENDLERNAME) values (?)");
			stmtInsert.bindString(1,haendlerVO.getHaendlername());
			long haendler_id=stmtInsert.executeInsert();
			haendlerVO.setHaendler_id(new Long(haendler_id).toString());
			return haendlerVO;
		}
		else{
			SQLiteStatement stmtInsert =db.compileStatement(
				"update T_HAENDLER set HAENDLERNAME='"+haendlerVO.getHaendlername()+"' where HAENDLER_ID="+haendlerVO.getHaendler_id());
			stmtInsert.execute();
			return haendlerVO;
		}
	}

	public ArrayList<HaendlerVO> findByAttributes(String[] attributes, String[] values) {
		String attr="";
		for(int i=0;i<attributes.length;i++){
			attr=attr+attributes[i]+"=?";
			if(!(i-attributes.length==-1)){
				attr=attr+", ";
			}
		}
	    Cursor cursor = db.query("T_HAENDLER", new String[]{"HAENDLER_ID","HAENDLERNAME"}, attr, values, "", "", "HAENDLERNAME");
		ArrayList<HaendlerVO> list=new ArrayList<HaendlerVO>();
		if(cursor.getCount()>0){
			cursor.moveToFirst();
			for(int i=0;i<cursor.getCount();i++){
				HaendlerVO haendlerVO = new HaendlerVO();
				haendlerVO.setHaendler_id(cursor.getString(0));
				haendlerVO.setHaendlername(cursor.getString(1));
				list.add(haendlerVO);
				cursor.moveToNext();
			}
		}
		cursor.close();
		return list;
	}
	
	public boolean deleteById(String id) {
		try{
			SQLiteStatement stmtDelete =db.compileStatement(
				"delete T_HAENDLER where ARTIKEL_ID=?");
			stmtDelete.bindString(1,id);
			stmtDelete.execute();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean deleteByHaendlername(String haendlername){
		ArrayList<HaendlerVO> haendlerList=findByHaendlername(haendlername);
		Iterator<HaendlerVO> iter = haendlerList.iterator();
		boolean deleted=true;
		while(iter.hasNext()){
			HaendlerVO haendler = iter.next();
			if(!deleteById(haendler.getHaendler_id()))
				deleted=false;
		}
		return deleted;
	}
}
