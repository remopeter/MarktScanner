package ch.bpeter.marktscanner;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.bpeter.marktscanner.datenbank.MarktScannerDatenbank;
import ch.bpeter.marktscanner.datenbank.tabellen.ArtikelDAO;
import ch.bpeter.marktscanner.datenbank.tabellen.ArtikelVO;
import ch.bpeter.marktscanner.datenbank.tabellen.HaendlerDAO;
import ch.bpeter.marktscanner.datenbank.tabellen.HaendlerVO;
import ch.bpeter.marktscanner.datenbank.tabellen.PreisDAO;
import ch.bpeter.marktscanner.datenbank.tabellen.PreisVO;
import ch.bpeter.marktscanner.hardware.Kamera;

import com.biggu.barcodescanner.client.android.Intents;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Startseite extends Activity {
	private static final int SCANNER_REQUEST_CODE = 0;
	private static final int KAMERA_REQUEST_CODE = 1;
	private static final int NEW_HAENDLER_REQUEST_CODE = 2;
	
	private MarktScannerDatenbank dbManager;
	private SQLiteDatabase db;
	private String bild=null; 
	private ArtikelDAO artikelDAO;
	private PreisDAO preisDAO;
	private HaendlerDAO haendlerDAO;
	
	private static final String[] HAENDLER = new String[] {
        "Coop", "Migros", "Denner", "Aldi", "Lidl"
    };
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startseite);
        dbManager=new MarktScannerDatenbank(this);
        db=dbManager.getWritableDatabase();
        artikelDAO=new ArtikelDAO(dbManager);
        haendlerDAO = new HaendlerDAO(dbManager);
        preisDAO = new PreisDAO(dbManager);
        Spinner tx_haendler = (Spinner)findViewById(R.id.tv_haendler);
		ArrayList<HaendlerVO> haendlerList = haendlerDAO.findAll();
		String []haendlername=new String[haendlerList.size()];
		for(int i=0;i<haendlerList.size();i++){
			haendlername[i]=haendlerList.get(i).getHaendlername();
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, haendlername);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		tx_haendler.setAdapter(adapter);
        Button btn_scan = (Button)findViewById(R.id.btn_scanobj);
        btn_scan.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				Intent intent = new Intent(v.getContext(), ch.bpeter.marktscanner.ScannerActivity.class);
				intent.putExtra(Intents.Preferences.ENABLE_BEEP, true);
				intent.putExtra(Intents.Preferences.ENABLE_VIBRATE, true);
				((Activity)v.getContext()).startActivityForResult(intent, SCANNER_REQUEST_CODE);
			}
		});
		//Button btn_takePicture = (Button)findViewById(R.id.btn_takePicture);
		//btn_takePicture.setEnabled(true);
		/**ImageView image = (ImageView)findViewById(R.id.iv_produktBild);
        Bitmap bMap = BitmapFactory.decodeFile("/data/data/ch.bpeter.marktscanner/files/bild.jpeg");
        image.setImageBitmap(bMap);
        image.setVisibility(ImageView.VISIBLE);**/
    }


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Handeln des Result des Scanners
		if (resultCode == Activity.RESULT_OK && requestCode == SCANNER_REQUEST_CODE) {

			Bundle extras = data.getExtras();
			String barcode = extras.getString("SCAN_RESULT");
			TextView textView = (TextView)findViewById(R.id.tv_scanresult);
			TextView tx_name = (TextView)findViewById(R.id.tv_name);
			tx_name.setText("");
			bild=barcode+".jpg";
			textView.setText(barcode);
			textView.setEnabled(false);
			String artikelName=null;
			String artikelFoto=null;
			Cursor cursor = db.query("T_ARTIKEL", new String[]{"ARTIKEL_ID","NAME","BARCODE", "FOTO"}, "BARCODE=?", new String[]{barcode}, "", "", "");
			cursor.moveToNext();
			if(cursor.getCount()==1){
				artikelName= cursor.getString(1);
				artikelFoto= cursor.getString(3);
				if(artikelName!=null)
					tx_name.setText(artikelName);
				Toast.makeText(Startseite.this, R.string.tx_code_gefunden, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(Startseite.this, R.string.tx_code_neu, Toast.LENGTH_SHORT).show();
			}
			
			/**Button btn_takePicture = (Button)findViewById(R.id.btn_takePicture);
			btn_takePicture.setEnabled(false);
			if(artikelFoto!=null){
				if(!artikelFoto.equals("")){
					btn_takePicture.setEnabled(true);
				}
			}**/
			cursor.close();
		}
		
		
		
		// Handeln des Result der Kamera
		if(resultCode == Activity.RESULT_OK && requestCode==KAMERA_REQUEST_CODE){
			try{
				Toast.makeText(Startseite.this, "Bild Gespeichert!", Toast.LENGTH_LONG).show();
				ImageView image = (ImageView)findViewById(R.id.iv_produktBild);
		        Bitmap bMap = BitmapFactory.decodeFile("/sdcard/default.jpg");
		        image.setImageBitmap(bMap);
		        image.setVisibility(ImageView.VISIBLE);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(resultCode == Activity.RESULT_OK && requestCode==NEW_HAENDLER_REQUEST_CODE){
			Spinner tx_haendler = (Spinner)findViewById(R.id.tv_haendler);
			ArrayList<HaendlerVO> haendlerList = haendlerDAO.findAll();
			String []haendlername=new String[haendlerList.size()];
			for(int i=0;i<haendlerList.size();i++){
				haendlername[i]=haendlerList.get(i).getHaendlername();
			}
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, haendlername);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			tx_haendler.setAdapter(adapter);
		}
	}

	public void takePicture(View v){
		if(bild==null)
			bild="default.jpg";
		else
			if(!(bild.contains(".jpg")||bild.contains(".jpeg")))
				bild=bild+".jpg";
		Intent intent = new Intent(v.getContext(), Kamera.class);
		intent.putExtra("BildName", bild);
		startActivityForResult(intent, KAMERA_REQUEST_CODE);
		/**Button button = (Button)findViewById(R.id.btn_takePicture);
		if(button.isEnabled())
			button.setEnabled(false);
		else button.setEnabled(true);**/
	}
	
	public void speichern(View v){
		TextView tx_barcode = (TextView)findViewById(R.id.tv_scanresult);
		TextView tx_name = (TextView)findViewById(R.id.tv_name);
		TextView tx_preis = (TextView)findViewById(R.id.tv_preis);
		Spinner tx_haendler = (Spinner)findViewById(R.id.tv_haendler);
		String barcode=tx_barcode.getText().toString();
		String name=tx_name.getText().toString();
		String preis=tx_preis.getText().toString();
		String haendlername=tx_haendler.getSelectedItem().toString();
		ArrayList<ArtikelVO> artikel = artikelDAO.findByAttributes(new String[]{"BARCODE"}, new String[]{barcode});
		if(artikel.size()==1){
			artikelDAO.save(artikel.get(0));
			Toast.makeText(Startseite.this, R.string.tx_daten_gespeichert, Toast.LENGTH_SHORT).show();
		}else if(artikel.size()==0){
			ArtikelVO artikelObj = new ArtikelVO();
			HaendlerVO haendlerObj = new HaendlerVO();
			PreisVO preisObj = new PreisVO();
			artikelObj.setBarcode(barcode);
			artikelObj.setName(name);
			artikelObj = artikelDAO.save(artikelObj);
			ArrayList<HaendlerVO> haendlerList = haendlerDAO.findByHaendlername(haendlername);
			if(!preis.equals("")){
				try{
					preis="12.22";
					Pattern pat = Pattern.compile("\\d+\\.\\d\\d");
					Matcher m = pat.matcher(preis);
					if(m.matches()){
						Double doublePreis = new Double(preis);
					}else{
						Toast.makeText(Startseite.this, R.string.tx_preis_nicht_gueltig, Toast.LENGTH_LONG).show();
						return;
					}
					
				}catch(Exception e){
					Toast.makeText(Startseite.this, R.string.tx_preis_nicht_gueltig, Toast.LENGTH_LONG).show();
					return;
				}
				preisObj.setPreis(preis);
				preisObj.setArtikel_id(artikelObj.getArtikel_id());
				
			}else{
				Toast.makeText(Startseite.this, R.string.tx_kein_preis, Toast.LENGTH_LONG).show();
			}
			if(haendlerList.size()==1){
				haendlerObj = haendlerList.get(0);
				preisObj.setHaendler_id(haendlerObj.getHaendler_id());
				preisDAO.save(preisObj);
			}
			if(haendlerList.size()==0){
				Toast.makeText(Startseite.this, R.string.tx_haendler_nicht_ausgewaehlt, Toast.LENGTH_LONG).show();
				return;
			}
			if(haendlerList.size()>1){
				Toast.makeText(Startseite.this, R.string.tx_haendler_nicht_eindeutig, Toast.LENGTH_SHORT).show();
				return;
			}
			
			Toast.makeText(Startseite.this, "Die Daten wurden gespeichert. ID = "+artikelObj.getArtikel_id(), Toast.LENGTH_SHORT).show();
		}else
			Toast.makeText(Startseite.this, R.string.tx_barcode_nicht_eindeutig, Toast.LENGTH_SHORT).show();
	}
	
	public void neuerHaendler(View v){
		Intent intent = new Intent(v.getContext(), HaendlerErfassen.class);
		startActivityForResult(intent, NEW_HAENDLER_REQUEST_CODE);
	}
	
	@Override
	protected void onDestroy() {
		db.close();
		super.onDestroy();
	}


	@Override
	protected void onPause() {
		super.onPause();
	}


	@Override
	protected void onResume() {
		super.onResume();
		db=dbManager.getWritableDatabase();
	}
}
