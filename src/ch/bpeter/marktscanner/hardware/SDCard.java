package ch.bpeter.marktscanner.hardware;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Environment;

public class SDCard extends Activity{
	public String speichereBild(byte[] bildArr, String name) {
		/**SDCard sdCard = new SDCard();
		try {
			return sdCard.speichereBild(bildArr, name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Error";**/
		String state = Environment.getExternalStorageState();
		boolean mExternalStorageAvailable=false;
		boolean mExternalStorageWriteable=false;
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		
		if(mExternalStorageAvailable&&mExternalStorageWriteable){
			File path = Environment.getExternalStorageDirectory();
			File bild = new File(path, name);
			try {
				FileOutputStream bildOut = new FileOutputStream(bild);
				bildOut.write(bildArr);
				bildOut.flush();
				bildOut.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return "FileNotFoundException: Konnte nicht gespeichert werden";
			} catch (IOException e) {
				e.printStackTrace();
				return "IOException: Konnte nicht gespeichert werden";
			} catch (NullPointerException e){
				e.printStackTrace();
				return "NullPointerException: Konnte nicht gespeichert werden";
			}
			
			return bild.getAbsolutePath();
		}else
			return "Die Speicherkarte ist nicht verfügbar. Die Datei konnte nicht gespeichert werden";
	}
}
