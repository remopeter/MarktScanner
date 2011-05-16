package ch.bpeter.marktscanner.hardware;

import java.io.IOException;

import ch.bpeter.marktscanner.R;
import ch.bpeter.marktscanner.Startseite;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.widget.Toast;

public class Kamera extends Activity implements Callback {

	SurfaceView mSurfaceView;
	private Camera mCamera;
	private static Intent intent=new Intent();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.kamera);
		mSurfaceView = (SurfaceView) findViewById(R.id.sv_kamera);
		mSurfaceView.getHolder().addCallback(this);
		mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Parameters parameter = mCamera.getParameters();
		parameter.setPreviewSize(width, height);
		parameter.setPictureFormat(PixelFormat.JPEG);
		mCamera.setParameters(parameter);
		mCamera.startPreview();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			e.printStackTrace();
			mCamera.stopPreview();
			mCamera.release();
			mCamera=null;
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
		mCamera=null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		PictureCallback picCallback = new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
				intent.putExtra("BildData", data);
			}
		};
		mCamera.takePicture(null, null, picCallback);
		intent.putExtra("Bild", "onTouch OK");
		setResult(RESULT_OK, intent);
		super.finish();
		return super.onTouchEvent(event);
	}
}
