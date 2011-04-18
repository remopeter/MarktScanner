package ch.bpeter.marktscanner;

import com.biggu.barcodescanner.client.android.Intents;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Startseite extends Activity {
	private static final int SCANNER_REQUEST_CODE = 0;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startseite);
        
        Button button = (Button)findViewById(R.id.btn_scanobj);
		button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(), ch.bpeter.marktscanner.ScannerActivity.class);
				intent.putExtra(Intents.Preferences.ENABLE_BEEP, true);
				intent.putExtra(Intents.Preferences.ENABLE_VIBRATE, true);

				((Activity)v.getContext()).startActivityForResult(intent, SCANNER_REQUEST_CODE);
			}
		});
    }
    
    
public void onActivityResult(int requestCode, int resultCode, Intent data) {
	if (resultCode == Activity.RESULT_OK && requestCode == SCANNER_REQUEST_CODE) {

			Bundle extras = data.getExtras();
			String result = extras.getString("SCAN_RESULT");
			TextView textView = (TextView)findViewById(R.id.txt_scanresult);
			textView.setText(result);
		}
	}
}