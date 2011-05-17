package ch.bpeter.marktscanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;

import ch.bpeter.marktscanner.datenbank.MarktScannerDatenbank;
import ch.bpeter.marktscanner.hardware.Kamera;
import ch.bpeter.marktscanner.hardware.SDCard;

import com.biggu.barcodescanner.client.android.Intents;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Startseite extends Activity {
	private static final int SCANNER_REQUEST_CODE = 0;
	private static final int KAMERA = 1;
	
	private MarktScannerDatenbank dbManager;
	private SQLiteDatabase db;
	private String bild=null; 
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startseite);
        dbManager=new MarktScannerDatenbank(this);
        db=dbManager.getWritableDatabase();
        Button button = (Button)findViewById(R.id.btn_scanobj);
		button.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View v) 
			{
				Intent intent = new Intent(v.getContext(), ch.bpeter.marktscanner.ScannerActivity.class);
				intent.putExtra(Intents.Preferences.ENABLE_BEEP, true);
				intent.putExtra(Intents.Preferences.ENABLE_VIBRATE, true);
				((Activity)v.getContext()).startActivityForResult(intent, SCANNER_REQUEST_CODE);
			}
		});
		Button btn_takePicture = (Button)findViewById(R.id.btn_takePicture);
		btn_takePicture.setEnabled(false);
		/**ImageView image = (ImageView)findViewById(R.id.iv_produktBild);
        Bitmap bMap = BitmapFactory.decodeFile("/data/data/ch.bpeter.marktscanner/files/bild.jpeg");
        image.setImageBitmap(bMap);
        image.setVisibility(ImageView.VISIBLE);**/
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


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Handeln des Result des Scanners
		if (resultCode == Activity.RESULT_OK && requestCode == SCANNER_REQUEST_CODE) {

			Bundle extras = data.getExtras();
			String barcode = extras.getString("SCAN_RESULT");
			TextView textView = (TextView)findViewById(R.id.txt_scanresult);
			bild=barcode+".jpg";
			textView.setText(barcode);
			textView.setEnabled(false);
			long id;
			String artikelName=null;
			String artikelBarcode=null;
			String artikelFoto=null;
			Cursor cursor = db.query("T_ARTIKEL", new String[]{"ARTIKEL_ID","NAME","BARCODE", "FOTO"}, "BARCODE=?", new String[]{barcode}, "", "", "");
			if(cursor.getColumnCount()==0){
				SQLiteStatement stmtInsert =db.compileStatement(
				"insert into T_ARTIKEL (NAME,BARCODE) values (?,?)");
				stmtInsert.bindString(1,"Test");
				stmtInsert.bindString(2,barcode);
				id = stmtInsert.executeInsert();
			}else{
				id = cursor.getLong(0);
				artikelName= cursor.getString(1);
				artikelBarcode= cursor.getString(2);
				artikelFoto= cursor.getString(3);
			}
			if(artikelFoto!=null){
				if(!artikelFoto.equals("")){
					Button btn_takePicture = (Button)findViewById(R.id.btn_takePicture);
					btn_takePicture.setEnabled(true);
				}
			}
			Toast.makeText(Startseite.this, "Barcode mit der ID: "+id+" in der DB gepeichert gespeichert.", Toast.LENGTH_LONG).show();	
		}
		
		
		
		// Handeln des Result der Kamera
		if(resultCode == Activity.RESULT_OK && requestCode==KAMERA){
			try{
				Toast.makeText(Startseite.this, "Bild Gespeichert!", Toast.LENGTH_LONG).show();
				//ImageView image = (ImageView)findViewById(R.id.iv_produktBild);
		        /**Bitmap bMap = BitmapFactory.decodeFile("C:/temp/fotos/thomas_dopfer.jpg");
		        image.setImageBitmap(bMap);
		        image.setVisibility(ImageView.VISIBLE);**/
				//Toast.makeText(Startseite.this, "Gespeichert unter: "+data.getExtras().getString("Bild"), Toast.LENGTH_SHORT).show();
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
		startActivityForResult(intent, KAMERA);
		/**Button button = (Button)findViewById(R.id.btn_takePicture);
		if(button.isEnabled())
			button.setEnabled(false);
		else button.setEnabled(true);**/
	}
	
	
}
