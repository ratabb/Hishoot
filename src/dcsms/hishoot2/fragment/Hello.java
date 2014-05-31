package dcsms.hishoot2.fragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import dcsms.hishoot2.MainActivity;
import dcsms.hishoot2.R;
import dcsms.hishoot2.skinmanager.GetResources;
import dcsms.hishoot2.skinmanager.SkinDescription;
import dcsms.hishoot2.util.DrawView;
import dcsms.hishoot2.util.HiPref;
import dcsms.hishoot2.util.Save;

import com.android.camera.CropImageIntentBuilder;

public class Hello extends Fragment implements OnClickListener {
	private Activity mActivity;
	private Context mContext;
	private View mRoot;
	private HiPref pref;
	private int tinggi, lebar, wall_tinggi, wall_lebar;
	private ImageView iv_prev, load;
	private Button ss1, ss2, wall, save;
	private boolean onsave = false;
	private ProgressDialog dialog;
	private String WTF = "watepak";
	private int KODE_SS1 = 1, KODE_SS2 = 2, KODE_WALL = 3, KODE_CROP = 4;
	private String IMG_1 = "watepak", IMG_2 = "watepak", IMG_WALL = "watepak";
	private int TL, TT, BL, BT;
	private SkinDescription desc;
	private Uri wallcrop;

	private Switcher mSwitch;

	private void deleteCache(Uri uri) {
		File f = new File(uri.getPath());
		if (f.exists())
			f.delete();
		IMG_WALL = "watepak";
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (MainActivity) getActivity();
		mContext = mActivity;
		wallcrop = Uri.fromFile(new File(mContext.getExternalCacheDir(),
				"wallcrop.jpg"));
		deleteCache(wallcrop);

		mSwitch = (Switcher) mActivity;
		mSwitch.onCustomActionBar(true, R.string.title_app,
				R.string.subtitle_app);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.main, container, false);

		findView(v);
		pref = new HiPref(mContext);
		tinggi = pref.getHi(getActivity());
		lebar = pref.getWi(getActivity());

		setFungsi();
		return v;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindDrawables(mRoot);
		deleteCache(wallcrop);
		System.gc();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			onsave = false;
			if (requestCode == KODE_SS1) {
				IMG_1 = getDataStringFromUri(data.getData());
				setFungsi();
			} else if (requestCode == KODE_SS2) {
				IMG_2 = getDataStringFromUri(data.getData());
				setFungsi();
			}

			else if (requestCode == KODE_WALL) {
				String gw = getDataStringFromUri(data.getData());

				CropImageIntentBuilder cropImage = new CropImageIntentBuilder(
						wall_lebar, wall_tinggi, wall_lebar, wall_tinggi,
						wallcrop).setScale(true).setScaleUpIfNeeded(true)
						.setDoFaceDetection(false)
						.setSourceImage(Uri.fromFile(new File(gw)));

				startActivityForResult(cropImage.getIntent(mContext), KODE_CROP);

				setFungsi();

			} else if (requestCode == KODE_CROP) {
				IMG_WALL = getDataStringFromUri(wallcrop);
				setFungsi();

			}

		}
	}

	private void findView(View v) {
		mRoot = (View) v.findViewById(R.id.rootView);

		iv_prev = (ImageView) v.findViewById(R.id.iv_preview);
		ss1 = (Button) v.findViewById(R.id.btn_ss1);
		ss2 = (Button) v.findViewById(R.id.btn_ss2);
		wall = (Button) v.findViewById(R.id.btn_wall);
		save = (Button) v.findViewById(R.id.btn_save);
		load = (ImageView) v.findViewById(R.id.loading);
		load.setVisibility(View.GONE);

	}

	private void setFungsi() {
		ss1.setOnClickListener(this);
		ss2.setOnClickListener(this);
		wall.setOnClickListener(this);
		save.setOnClickListener(this);
		loading(true);
		try {
			new Tasking().execute();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}

	}

	private String getDataStringFromUri(Uri uri) {
		File f = new File(uri.getPath());
		return (!f.isFile()) ? getImagePath(uri) : uri.getPath();
	}

	private String getImagePath(Uri uri) {
		Cursor cursor = mContext.getContentResolver().query(uri, null, null,
				null, null);
		cursor.moveToFirst();
		int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
		return cursor.getString(idx);

	}

	private void unbindDrawables(View view) {
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			((ViewGroup) view).removeAllViews();
		}

	}

	private Bitmap createBitmapFromDecode(String string, Options opts) {
		return (string.equalsIgnoreCase(WTF)) ? BitmapFactory.decodeResource(
				getResources(), R.drawable.img_default, opts) : BitmapFactory
				.decodeFile(string, opts);
	}

	private class Tasking extends AsyncTask<Bitmap, Bitmap, Bitmap> {

		@Override
		protected void onPostExecute(Bitmap result) {
			wall_lebar = result.getWidth();
			wall_tinggi = result.getHeight();
			if (onsave) {
				new SavingTask().execute(result);
			} else {
				Bitmap b = DrawView.resizeImage(result, lebar, lebar);
				iv_prev.setImageBitmap(b);
				result.recycle();
			}
			loading(false);

		}

		@Override
		protected Bitmap doInBackground(Bitmap... bmp) {
			Bitmap bwall = null;
			Options opts = new Options();
			opts.inDither = false; // Disable Dithering mode

			opts.inPurgeable = true; // Tell to gc that whether it needs free
										// memory, the Bitmap can be cleared
			opts.inTempStorage = new byte[32 * 1024];

			Options options = new Options();
			options.inSampleSize = 2;

			bwall = createBitmapFromDecode(IMG_WALL, options);

			Bitmap shot1 = Bitmap.createScaledBitmap(
					createBitmapFromDecode(IMG_1, opts), lebar, tinggi, true);
			Bitmap shot2 = Bitmap.createScaledBitmap(
					createBitmapFromDecode(IMG_2, opts), lebar, tinggi, true);

			// 27 28 27 54
			TL = 26;
			TT = 27;
			BL = 27;
			BT = 54;

			Bitmap mixthem = null;
			Bitmap frame = null;

			int topx = 0;
			int topy = 0;
			int totx = 0;
			int toty = 0;
			topx = (int) (TL);
			topy = (int) (TT);
			totx = (int) ((TL + BL));
			toty = (int) ((TT + BT));

			String skin = pref.getPaketName();
			if (skin != null) {
				Bitmap framefrom = new GetResources(mContext).getImage(skin,
						"skin", opts);
				Context c;
				InputStream is = null;
				try {
					c = mContext.createPackageContext(skin, 0);
					AssetManager am = c.getAssets();
					is = am.open("keterangan.xml");
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				desc = new SkinDescription();
				if (is != null) {

					desc.getKeterangan(is);
				}
				int tx, ty, bx, by;
				tx = desc.getTX();
				ty = desc.getTY();
				bx = desc.getBX();
				by = desc.getBY();

				TL = (tx > 0) ? tx : 1;
				TT = (ty > 0) ? ty : 1;
				BL = (bx > 0) ? bx : 1;
				BT = (by > 0) ? by : 1;

				topx = (int) (TL);
				topy = (int) (TT);
				totx = (int) ((TL + BL));
				toty = (int) ((TT + BT));

				frame = Bitmap.createScaledBitmap(framefrom, lebar + totx,
						tinggi + toty, true);

			} else {
				// LOG(its(totx));
				// LOG(its(toty));

				frame = DrawView.getNine(0, 0, R.drawable.frame1, lebar + totx,
						tinggi + toty, mContext, opts);

			}

			Bitmap mix1 = DrawView.DrawMe(frame, 0, 0, shot1, topx, topy,
					mContext, lebar + totx, tinggi + toty);
			Bitmap mix2 = DrawView.DrawMe(frame, 0, 0, shot2, topx, topy,
					mContext, lebar + totx, tinggi + toty);

			mixthem = Bitmap.createBitmap(mix1.getWidth() * 2,
					mix1.getHeight(), Bitmap.Config.ARGB_8888);
			int maxwid = mixthem.getWidth(), maxhi = mixthem.getHeight();

			if (bwall.getHeight() < mixthem.getHeight()) {
				maxhi = mixthem.getHeight();
				maxwid = mixthem.getWidth() * mixthem.getWidth();
			}
			if (bwall.getWidth() < mixthem.getWidth()) {
				maxhi = mixthem.getHeight() * mixthem.getHeight();
				maxwid = mixthem.getWidth();
			}

			Bitmap wallfcuk = DrawView.resizeImage(bwall, maxwid, maxhi);

			// TODO watermark with res, native code??
			Bitmap wm = BitmapFactory.decodeResource(getResources(),
					R.drawable.watermark);
			// Bitmap wm = BitmapFactory.decodeStream(new ByteArrayInputStream(
			// Base64.decode(dafuq(), Base64.DEFAULT)));

			Canvas cc = new Canvas(mixthem);
			cc.drawBitmap(wallfcuk, 0, 0, null);
			cc.drawBitmap(mix1, 0, 0, null);
			cc.drawBitmap(mix2, mix1.getWidth(), 0, null);
			cc.drawBitmap(wm, mix1.getWidth() - (wm.getWidth() / 2),
					(mix1.getHeight() - wm.getHeight()), null);

			// TODO: coordinate dynamic? draw 'tanda air'
			String il = pref.getPref().getString(About.ILLEGAL, "ILLEGAL");
			Paint ler = paint(il);
			int ils = il.length();
			float le = (float) (cc.getWidth() - (float) (ils * 20));
			float ga = (float) (cc.getHeight() - 20f);

			cc.drawText(il, le, ga, ler);

			System.gc();
			return mixthem;
		}
	}

	// TODO:  'tanda air'
	private Paint paint(String s) {
		Paint p = new Paint();
		p.setTextAlign(Paint.Align.RIGHT);
		p.setColor(0x55ffffff);
		p.setShadowLayer(1f, 0f, .8f, 0x7f444444);
		p.setTextSize(24f);
		p.setStyle(Paint.Style.FILL_AND_STROKE);

		return p;
	}

	private class SavingTask extends AsyncTask<Bitmap, Integer, File> {
		private SavingTask() {
			dialog = new ProgressDialog(mContext);
			dialog.setCancelable(false);
			dialog.setMessage("Please Wait");
			dialog.show();
		}

		@Override
		protected void onPostExecute(File result) {
			if (result.exists()) {
				dialog.setMessage("SaveComplete");
				dialog.dismiss();
				String s = result.getPath();
				Log.d(getClass().getName(), s);
				deleteCache(wallcrop);
				ShareDialog sf = new ShareDialog();
				sf.setData(s);
				mSwitch.onSwitchContent(sf);
			} else {
				dialog.setMessage("Failed");
				dialog.dismiss();
			}
		}

		@Override
		protected File doInBackground(Bitmap... bmp) {
			Save s = new Save(mContext);
			File f = s.SaveBitmap(bmp[0]);
			return f;
		}

	}

	private void loading(Boolean run) {
		// TODO
		mSwitch.setLoading(run);
		final AnimationDrawable anima = (AnimationDrawable) load.getDrawable();
		if (run) {
			load.setVisibility(View.VISIBLE);
			load.post(new Runnable() {
				@Override
				public void run() {
					anima.start();
				}
			});
		} else {
			load.setVisibility(View.GONE);
			anima.stop();
		}

	}

	// private String its(Integer i) {
	// return Integer.toString(i);
	// }
	//
	// private void LOG(String msg) {
	// Log.d("OOM", msg);
	// }

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_ss1:
			getImageChooser(getString(R.string.ss1), KODE_SS1);
			break;
		case R.id.btn_ss2:
			getImageChooser(getString(R.string.ss2), KODE_SS2);
			break;
		case R.id.btn_wall:
			getImageChooser(getString(R.string.wallpaper), KODE_WALL);
			break;
		case R.id.btn_save:
			SavingShit();
			break;
		}
	}

	private void SavingShit() {
		onsave = true;
		setFungsi();
	}

	private void getImageChooser(String j, int rekueskode) {
		String chooser = getString(R.string.chooser);
		String judul = String.format(chooser, j);
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.setType("image/*");
		startActivityForResult(Intent.createChooser(i, judul), rekueskode);
	}

}
