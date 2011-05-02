package ch.bpeter.marktscanner;

import java.io.File;
import java.io.FileOutputStream;

import ch.bpeter.marktscanner.datenbank.MarktScannerDatenbank;
import ch.bpeter.marktscanner.hardware.Kamera;

import com.biggu.barcodescanner.client.android.Intents;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startseite);
        dbManager=new MarktScannerDatenbank(this);
        
        
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
    }
    
    
    @Override
	protected void onPause() {
		db.close();
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
			//textView.setText(barcode);
			
			SQLiteStatement stmtInsert =db.compileStatement(
					"insert into T_ARTIKEL (NAME,BARCODE) values (?,?)");
				stmtInsert.bindString(1,"Test");
				stmtInsert.bindString(2,barcode);
				long id = stmtInsert.executeInsert();
				
			textView.setText("Datensatz mit der ID: "+id+" gespeichert.");

		}
		
		// Handeln des Result der Kamera
		if(resultCode == Activity.RESULT_OK && requestCode==KAMERA){
			try{
				byte[] bildArr = data.getByteArrayExtra("BildData");
				File bild = new File(Startseite.this.getFilesDir() + "/bild.jpg");
				FileOutputStream bildOut = new FileOutputStream(bild);
				bildOut.write(bildArr);
				bildOut.flush();
				bildOut.close();
				Toast.makeText(Startseite.this, "Gespeichert unter: "+bild.getAbsolutePath(), Toast.LENGTH_SHORT).show();
				ImageView image = (ImageView)findViewById(R.id.iv_produktBild);
				Bitmap bm = BitmapFactory.decodeFile("Pfadangabe");
				image.setImageBitmap(bm);
				//Toast.makeText(Startseite.this, "Gespeichert unter: "+data.getExtras().getString("Bild"), Toast.LENGTH_SHORT).show();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public void takePicture(View v){
		startActivityForResult(new Intent(this, Kamera.class), KAMERA);
		/**Button button = (Button)findViewById(R.id.btn_takePicture);
		if(button.isEnabled())
			button.setEnabled(false);
		else button.setEnabled(true);**/
	}
}