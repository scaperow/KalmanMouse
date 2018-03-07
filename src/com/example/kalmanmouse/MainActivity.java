package com.example.kalmanmouse;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.video.KalmanFilter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;

public class MainActivity extends Activity implements CvCameraViewListener {
	CameraBridgeViewBase surface_camera;
	Mat mat_img, mat_state, mat_process_noise, mat_measurement;
	Point cursor = null;
	KalmanFilter kalman = null;
	private BaseLoaderCallback loader_callback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				surface_camera.enableView();
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};

	@Override
	public void onCameraViewStopped() {
		// TODO Auto-generated method stub

	}

	@Override
	public Mat onCameraFrame(Mat frame) {

		return frame;
	}

	@Override
	public void onResume() {
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_3, this,
				loader_callback);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// TODO Auto-generated method stub
		if (surface_camera == null) {
			surface_camera = (CameraBridgeViewBase) findViewById(R.id.surface_camera);
		}

		surface_camera.setCvCameraViewListener(this);
		surface_camera.setOnHoverListener(new OnHoverListener() {

			@Override
			public boolean onHover(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				cursor = new Point(event.getRawX(), event.getRawY());
				return false;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	Point center = null;

	@Override
	public void onCameraViewStarted(int width, int height) {
		// TODO Auto-generated method stub
		kalman = new KalmanFilter(2, 1);
		mat_state = new Mat(2, 1, CvType.CV_32F);
		mat_process_noise = new Mat(2, 1, CvType.CV_32F);
		mat_measurement = Mat.zeros(1, 1, CvType.CV_32F);
		Core.randn(mat_state, 0, 0.1);

		center = new Point(width / 2, height / 2);
	}
	
	private void track(Mat frame){
		Mat mat_control = kalman.predict(frame);
	}
}
