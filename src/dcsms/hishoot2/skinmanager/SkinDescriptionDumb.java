package dcsms.hishoot2.skinmanager;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.util.Log;

public class SkinDescriptionDumb {
	String TAG = getClass().getSimpleName();
	String device = null;
	String author = null;
	int tx, ty, bx, by;
	int densType;

	public SkinDescriptionDumb(Context context, InputStream inputStream) {
		// TODO
		Log.d(TAG, "parser begin");

		XmlPullParserFactory factory;
		XmlPullParser xpp;
		try {
			factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			xpp = factory.newPullParser();

			xpp.setInput(inputStream, null);

			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				// if (eventType == XmlPullParser.START_DOCUMENT) {
				// System.out.println("Start document");
				// } else if (eventType == XmlPullParser.START_TAG) {
				// System.out.println("Start tag " + xpp.getName());
				// } else if (eventType == XmlPullParser.END_TAG) {
				// System.out.println("End tag " + xpp.getName());
				// } else if (eventType == XmlPullParser.TEXT) {
				// System.out.println("Text " + xpp.getText());
				// }

				if (eventType == XmlPullParser.START_TAG) {
					Log.d(TAG, "tag name:" + xpp.getName());
				}
				if (eventType == XmlPullParser.TEXT) {
					Log.d(TAG, "text:" + xpp.getText());
				}

				eventType = xpp.nextToken();
			}
			// System.out.println("End document");

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String getDevice() {
		return this.device;
	}

	public String getAuthor() {
		return this.author;
	}

	public int getdensType() {
		return this.densType;
	}

	public int getTx() {
		return this.tx;
	}

	public int getTy() {
		return this.ty;
	}

	public int getBx() {
		return this.bx;
	}

	public int getBy() {
		return this.by;
	}
}
