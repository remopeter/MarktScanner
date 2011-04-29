package ch.bpeter.marktscanner;

import ch.bpeter.marktscanner.datenbank.MarktScannerDatenbank;

import com.biggu.barcodescanner.client.android.Intents;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Startseite extends Activity {
	private static final int SCANNER_REQUEST_CODE = 0;
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
	}
}