package com.xiuye.imshow;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.opencv.core.Mat;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class OpenCVImageViewer extends Application implements ChangeListener<Scene> {

	private static HashMap<String, Image> images = new HashMap<String, Image>();
	public static KeyCode keyCode = null;
	public static boolean windowStarted = false;

	@Override
	public void start(Stage primaryStage) throws Exception {

		windowStarted = true;

		// 让所有的窗口显示
		Set<Entry<String, Image>> imagesSet = images.entrySet();
		for (Entry<String, Image> entry : imagesSet) {
			Stage imageViewer = createStage(entry.getKey(), entry.getValue());
			imageViewer.show();
		}



		// primaryStage不能添加键盘事件
		// 可以不需要这一句
		// primaryStage.show();
	}

	private EventHandler<KeyEvent> createKeyEventHandler() {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {

//				log("键盘事件生效");

				// log(event.getSource());
				keyCode = event.getCode();
				// log(keyCode == KeyCode.ESCAPE);
				// Enum 类型可以用"=="
				if (keyCode == KeyCode.ESCAPE) {
					// 只能关闭一个窗口
					// for(Stage imageViewer : imageViewers){
					// //fireEvent 相当于 send(发送) 事件
					// Event.fireEvent(event.getTarget(),new
					// WindowEvent(null,WindowEvent.WINDOW_CLOSE_REQUEST));
					// }
					// 关闭所有窗口
					System.exit(0);// 直接程序结束
				}
			}
		};
	}

	private EventHandler<WindowEvent> createWindowEventHandler() {

		return new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
//				log("窗口关闭事件生效");
				// 通过consume可以让右上角关闭按钮失效
				event.consume();

			}
		};
	}

	// 创建显示Mat(Image)的窗口
	// 只有在Applciation的线程中创建才有效
	private Stage createStage(String title, Image image) {

		ImageView iv = new ImageView(image);
		// 采用流式布局(layout)
		FlowPane root = new FlowPane();
		root.getChildren().add(iv);
		double width = image.getWidth();
		double height = image.getHeight();

		// 将image的控件增加到Scene(相当于窗口的客户区)上显示
		Scene scene = new Scene(root, width, height);

		// scene.setOnKeyPressed(createKeyEventHandler());
		// same as below
		// scene.addEventHandler(KeyEvent.KEY_PRESSED, createKeyEvent());

		// 创建窗口
		Stage imageViewer = new Stage();
		// 添加鼠标事件, 设置本事件目的是为了获取键盘的点击的code
		imageViewer.addEventHandler(KeyEvent.KEY_PRESSED, createKeyEventHandler());
		// 设置窗口关闭事 件
		imageViewer.setOnCloseRequest(createWindowEventHandler());

		imageViewer.setTitle(title);
		// 相当于设置窗口的客户区显示
		imageViewer.setScene(scene);
		return imageViewer;

	}

	// Mat转换为Image
	private static Image mat2Image(Mat img) {

		// mat的宽度和高度
		int width = img.cols();
		int height = img.rows();
		// 将mat的数据保存到BufferedImage对象中
		// 注意mat的type(主要是通道数)和BufferedImage的type一致
		int channels = img.channels();
		BufferedImage bf = null;
		switch (channels) {
		case 1:
			bf = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
			break;
		case 3:
			bf = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		default:
			break;
		}


		byte[] data = new byte[width * height * (int) img.elemSize()];
		img.get(0, 0, data);
		// 以下方法设置颜色有问题,原图像显示偏蓝,不正常
		// bf.getRaster().setDataElements(0, 0, width, height, data);
		// 此方式设置的颜色能正常显示没问题
		byte[] tartgetData = ((DataBufferByte) bf.getRaster().getDataBuffer()).getData();
		System.arraycopy(data, 0, tartgetData, 0, data.length);
		// 将BufferedImage转换为JavaFX能的显示的Image图像!
		Image image = SwingFXUtils.toFXImage(bf, null);
		return image;
	}

	/**
	 *
	 * 创建添加Mat到map中为以后的显示做准备
	 *
	 * @param title
	 * @param mat
	 */
	// stage的创建只有在Application的线程中才有效,
	// 所以control的创建工作不要在其他线程中创建.
	// 只有调用Application(子类)的本地方法(Applciation线程)中使用才有效
	public static void addImage(String title, Mat mat) {
		// 验证mat是不是为空
		if (mat == null) {
			throw new NullPointerException("imshow img(Mat) is null");
		}
		// 输出args获取的参数
		// log(this.getParameters().getRaw());
		if (title == null) {
			title = "ImageViewer";
		}

		images.put(title, mat2Image(mat));
	}

	private static <T> void log(T t) {
		System.out.println(t);
	}

	@Override
	public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
		log("listener");
	}
}
