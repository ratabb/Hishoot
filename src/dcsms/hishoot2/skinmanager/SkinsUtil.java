package dcsms.hishoot2.skinmanager;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;

public class SkinsUtil {
	protected static final boolean DEBUG = false;
	protected final String TAG = getClass().getSimpleName();
	public final String DEFAULT = "default";
	private Context mContext;
	private String mSkinName;
	private String mAuthorName;
	private int mType;

	public SkinsUtil(Context context) {
		this.mContext = context;
	}

	public String getSkinName() {
		return this.mSkinName;
	}

	public String getAuthorName() {
		return this.mAuthorName;
	}

	public int getType() {
		return this.mType;
	}

	public Bitmap getBitmapSkin(String pkg) {
		return new GetResources(mContext).getImage(pkg, "skin", null);
	}

	public void getSkinInfo(String pkg) {
		if (pkg.equalsIgnoreCase(DEFAULT))
			return;

		try {
			Context c = mContext.createPackageContext(pkg, 0);
			AssetManager am = c.getAssets();
			InputStream is = am.open("keterangan.xml");

			SkinDescription k = new SkinDescription(mContext, is);
			this.mSkinName = k.getDevice();
			this.mAuthorName = k.getAuthor();
			this.mType = k.getDensType();
			
			if (DEBUG)
				Log.d(TAG, k.getDevice());

			is.close();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String typeSkin(int d) {
		String type = "";
		switch (d) {
		default:
			type = "unknown";
			break;
		case 0:
			type = "LDPI";
			break;
		case 1:
			type = "MDPI";
			break;
		case 2:
			type = "HDPI";
			break;
		case 3:
			type = "XHDPI";
			break;
		case 4:
			type = "XXHDPI";
			break;
		}
		return type;
	}
}
