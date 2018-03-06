package com.xiuye.imshow;

import org.opencv.core.Mat;

import javafx.application.Application;
import javafx.scene.input.KeyCode;

public class IV {

	// application是否已经在运行的状态
	public static boolean applicationStarted = false;

	/**
	 * 将mat加载到window的控件中,在调用waitKey(...)时显示.
	 *
	 * @param t
	 * @param mat
	 * @param args
	 */
	public static void imshow(String t, final Mat mat) {
		OpenCVImageViewer.addImage(t, mat);
	}



	public static KeyCode waitKey(int param, String... args) {

		//application运行后只会运行一次start(Stage)方法
		if (!applicationStarted) {
			//为了让主线程不停顿,启用其他线程运行application
			//方便waitKey(...)实时返回值
			Thread t = new Thread(new Runnable(){

				@Override
				public void run() {
					Application.launch(OpenCVImageViewer.class,args);
				}

			});

			t.start();
			applicationStarted = true;
		}

		return OpenCVImageViewer.keyCode;
	}

	private static <T> void log(T t) {
		System.out.println(t);
	}

}
