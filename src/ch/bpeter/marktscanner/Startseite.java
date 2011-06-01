package ch.bpeter.marktscanner;

import java.util.ArrayList;

import ch.bpeter.marktscanner.datenbank.MarktScannerDatenbank;
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
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Startseite extends Activity {
	private static final int SCANNER_REQUEST_CODE = 0;
	private static final int KAMERA_REQUEST_CODE = 1;
	
	private MarktScannerDatenbank dbManager;
	private SQLiteDatabase db;
	private String bild=null; 
	
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
			AutoCompleteTextView tx_haendler = (AutoCompleteTextView)findViewById(R.id.tv_haendler);
			tx_haendler.setEnabled(true);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, HAENDLER);
			tx_haendler.setAdapter(adapter);
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
		        Bitmap bMap = BitmapFactory.decodeFile("/sdcard/defailt.jpg");
		        image.setImageBitmap(bMap);
		        image.setVisibility(ImageView.VISIBLE);
			}catch(Exception e){
				e.printStackTrace();
			}
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
		String barcode=tx_barcode.getText().toString();
		String name=tx_name.getText().toString();
		Cursor cursor = db.query("T_ARTIKEL", new String[]{"ARTIKEL_ID","NAME","BARCODE", "FOTO"}, "BARCODE=?", new String[]{barcode}, "", "", "");
		if(cursor.getCount()==1){
			cursor.moveToNext();
			SQLiteStatement stmtInsert =db.compileStatement(
				"update T_ARTIKEL set NAME='"+name+"', BARCODE='"+barcode+"' where ARTIKEL_ID="+cursor.getLong(0));
			stmtInsert.execute();
			Toast.makeText(Startseite.this, R.string.tx_daten_gespeichert, Toast.LENGTH_SHORT).show();
		}else if(cursor.getCount()==0){
			SQLiteStatement stmtInsert =db.compileStatement(
			"insert into T_ARTIKEL (NAME,BARCODE) values (?,?)");
			stmtInsert.bindString(1,name);
			stmtInsert.bindString(2,barcode);
			long id = stmtInsert.executeInsert();
			Toast.makeText(Startseite.this, R.string.tx_daten_gespeichert+" ID = "+id, Toast.LENGTH_SHORT).show();
		}else
			Toast.makeText(Startseite.this, "Der Barcode ist in der Datenbank nicht eindeutig.", Toast.LENGTH_SHORT).show();
		cursor.close();
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
