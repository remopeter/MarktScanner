package ch.bpeter.marktscanner;

import java.util.ArrayList;
import java.util.Iterator;

import ch.bpeter.marktscanner.datenbank.MarktScannerDatenbank;
import ch.bpeter.marktscanner.datenbank.tabellen.HaendlerDAO;
import ch.bpeter.marktscanner.datenbank.tabellen.HaendlerVO;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class HaendlerErfassen extends Activity {
	private MarktScannerDatenbank dbManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.haendlererfassen);
		dbManager=new MarktScannerDatenbank(this);
	}
	
	public void speichern(View v){
		TextView tx_haendlername = (TextView)findViewById(R.id.tv_neuerHaendler);
		HaendlerDAO haendlerDAO = new HaendlerDAO(dbManager);
		HaendlerVO haendlerVO = new HaendlerVO();
		ArrayList<HaendlerVO> haendlerList;
		haendlerList = haendlerDAO.findByHaendlername(tx_haendlername.getText().toString());
		if(haendlerList.size()==0){
			haendlerVO.setHaendlername(tx_haendlername.getText().toString());
			haendlerVO=haendlerDAO.save(haendlerVO);
			Toast.makeText(HaendlerErfassen.this, "Der Haendler wurde gespeichert. ID="+haendlerVO.getHaendler_id(), Toast.LENGTH_SHORT).show();
		}
		if(haendlerList.size()>0){
			Toast.makeText(HaendlerErfassen.this, R.string.tx_haendler_existiert, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void loeschen(View v){
		TextView tx_haendlername = (TextView)findViewById(R.id.tv_neuerHaendler);
		HaendlerDAO haendlerDAO = new HaendlerDAO(dbManager);
		if(haendlerDAO.deleteByHaendlername(tx_haendlername.getText().toString()))
			Toast.makeText(HaendlerErfassen.this, R.string.tx_haendler_geloescht, Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(HaendlerErfassen.this, R.string.tx_haendler_nichtGeloescht, Toast.LENGTH_SHORT).show();
	}
	
	public void zurueck(View v){
		setResult(RESULT_OK);
		super.finish();
	}
}
