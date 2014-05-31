package dcsms.hishoot2.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.NinePatchDrawable;

public class DrawView {
	public static Bitmap DrawMe(Bitmap frame, int framex, int framey,
			Bitmap ss, int ssx, int ssy, Context context, int w, int h) {
		Bitmap b = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(b);
		canvas.drawBitmap(ss, ssx, ssy, null);
		canvas.drawBitmap(frame, framex, framey, null);
		return b;

	}

	public static Bitmap getNine(int fromx, int fromy, int id, int x, int y,
			Context context, Options opts) {
		Bitmap out = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(out);

		NinePatchDrawable bg = (NinePatchDrawable) context.getResources()
				.getDrawable(id);
		if (bg != null) {
			bg.setBounds(0, 0, x, y);
			bg.draw(c);
		}
		return out;
	}

	public static Bitmap getImageBitmap(String path, String thumbpath)
			throws IOException {
		// Allocate files and objects outside of timingoops
		File file = new File(thumbpath);
		RandomAccessFile in = new RandomAccessFile(file, "rws");
		final FileChannel channel = in.getChannel();
		final int fileSize = (int) channel.size();
		final byte[] testBytes = new byte[fileSize];
		final ByteBuffer buff = ByteBuffer.allocate(fileSize);
		final byte[] buffArray = buff.array();
		@SuppressWarnings("unused")
		final int buffBase = buff.arrayOffset();

		// Read from channel into buffer, and batch read from buffer to byte
		// array;
		@SuppressWarnings("unused")
		long time1 = System.currentTimeMillis();
		channel.position(0);
		channel.read(buff);
		buff.flip();
		buff.get(testBytes);
		long time11 = System.currentTimeMillis();
		Bitmap bmp = Bitmap_process(buffArray);
		long time2 = System.currentTimeMillis();
		System.out.println("Time taken to load: " + (time2 - time11) + "ms");

		return bmp;
	}

	public static Bitmap Bitmap_process(byte[] buffArray) {
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inDither = false; // Disable Dithering mode
		options.inPurgeable = true; // Tell to gc that whether it needs free
									// memory, the Bitmap can be cleared
		options.inInputShareable = true; // Which kind of reference will be used
											// to recover the Bitmap data after
											// being clear, when it will be used
											// in the future
		options.inTempStorage = new byte[32 * 1024]; // Allocate some temporal
														// memory for decoding

		options.inSampleSize = 1;

		Bitmap imageBitmap = BitmapFactory.decodeByteArray(buffArray, 0,
				buffArray.length, options);
		return imageBitmap;
	}

	public static Bitmap resizeImage(Bitmap image, int maxWidth, int maxHeight) {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		double imageAspect = (double) imageWidth / imageHeight;
		double canvasAspect = (double) maxWidth / maxHeight;
		double scaleFactor;

		if (imageAspect < canvasAspect) {
			scaleFactor = (double) maxHeight / imageHeight;
		} else {
			scaleFactor = (double) maxWidth / imageWidth;
		}

		float scaleWidth = ((float) scaleFactor) * imageWidth;
		float scaleHeight = ((float) scaleFactor) * imageHeight;

		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);

		// recreate the new Bitmap
		return Bitmap.createScaledBitmap(image, (int) scaleWidth,
				(int) scaleHeight, true);
	}
}
