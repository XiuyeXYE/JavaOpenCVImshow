package com.xiuye.imshow.test;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import com.xiuye.imshow.IV;

public class ImageOp {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		Mat[] mats = new Mat[6];
		mats[0] = Highgui.imread("p1.jpg");
		mats[1] = Highgui.imread("background.jpg");
		mats[2] = Highgui.imread("out.png");
		mats[3] = Highgui.imread("houzi.jpg");
		mats[4] = Highgui.imread("dest.jpg");
		mats[5] = Highgui.imread("p2.jpg");


		while (true) {
//			log("begin");
			for (int i=0;i<6;i++) {
				IV.imshow("User-Defined Imshow"+i, mats[i]);
			}

			// IV.imshow("User-Defined Imshow2", mat);
			// mat = Highgui.imread("p2.jpg");
			// IV.imshow("Output Image", mat);

			IV.waitKey(0);
//			log("end");
		}

	}

	private static <T> void log(T t) {
		System.out.println(t);
	}

}
