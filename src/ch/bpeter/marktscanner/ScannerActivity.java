package ch.bpeter.marktscanner;

import android.content.Intent;
import android.view.MotionEvent;

import com.biggu.barcodescanner.client.android.CaptureActivity;

public class ScannerActivity extends CaptureActivity {

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Intent intent = getIntent();
		intent.putExtra("SCAN_RESULT", "NoCode");
		setResult(RESULT_OK, intent);
		super.finish();
		return super.onTouchEvent(event);
	}

	@Override
	public int get_R_id_preview_view() {

		return R.id.preview_view;
	}

	@Override
	public int get_R_id_viewfinder_view() {

		return R.id.viewfinder_view;
	}

	@Override
	public int get_R_layout_scanner() {

		return R.layout.scanner;
	}

	@Override
	public int get_R_raw_beep() {
		
		return R.raw.beep;
	}
}
