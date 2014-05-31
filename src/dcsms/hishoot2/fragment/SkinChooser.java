package dcsms.hishoot2.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import dcsms.hishoot2.MainActivity;
import dcsms.hishoot2.R;
import dcsms.hishoot2.fragment.PreviewSkin.PreviewListener;
import dcsms.hishoot2.skinmanager.SkinAdapter;
import dcsms.hishoot2.skinmanager.SkinItem;
import dcsms.hishoot2.skinmanager.SkinsUtil;
import dcsms.hishoot2.util.CekDensiti;
import dcsms.hishoot2.util.DrawView;
import dcsms.hishoot2.util.HiPref;
import dcsms.hishoot2.util.ToastAlert;

public class SkinChooser extends ListFragment {
	private Activity mActivity;
	private Context mContext;
	private HiPref pref;

	private List<String> paket = new ArrayList<String>();
	private List<Drawable> paketIcon = new ArrayList<Drawable>();

	private CekDensiti c;
	private int mydensiti;

	private SkinsUtil util;

	private boolean isCompatible, isDefault;
	private String mSkinPackageName, mSkinMessage;

	private Switcher mSwitch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mActivity = (MainActivity) getActivity();
		mContext = mActivity;
		pref = new HiPref(mContext);
		c = new CekDensiti(mActivity);
		mydensiti = c.getDensitiType();

		util = new SkinsUtil(mContext);
		SkinAdapter adapter = new SkinAdapter(mContext);
		mSwitch = (Switcher) mActivity;
		mSwitch.onCustomActionBar(false, R.string.title_skin,
				R.string.subtitle_skin);

		// TODO add default template
		adapter.add(new SkinItem(util.DEFAULT, String.format(
				"Device: %s\nAuthor: %s", "Default Template", "DCSMS"),
				mContext.getResources().getDrawable(R.drawable.ic_launcher)));

		loadSkinPackage(paket, paketIcon);

		if (paket != null) {

			for (int i = 0; i < paket.size(); i++) {
				String p = paket.get(i);
				// TODO
				adapter.add(new SkinItem(p, getStringSkin(p,
						"Device: %s  (%s)\nAuthor: %s", false), paketIcon
						.get(i)));
			}
		}

		setListAdapter(adapter);

	}

	private void loadSkinPackage(List<String> p, List<Drawable> ic) {
		p.clear();
		ic.clear();
		PackageManager pm = mContext.getPackageManager();
		Intent i = new Intent(Intent.ACTION_MAIN, null);
		i.addCategory("dcsms.hishoot.SKINTEMPLATE");
		List<ResolveInfo> apps = pm.queryIntentActivities(i, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));
		for (ResolveInfo skin : apps) {
			ActivityInfo ai = skin.activityInfo;
			String pkg = ai.packageName;

			// TODO: filtered type with deviceDpi
			// util.getSkinInfo(pkg);
			// if (util.getType() == mydensiti)

			ic.add(ai.loadIcon(pm));
			p.add(pkg);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		showPreview(v.getTag().toString());
	}

	private void showPreview(String pkg) {
		String description = "";
		Bitmap bitmap = null;
		mSkinPackageName = pkg;
		// TODO
		isDefault = (mSkinPackageName.equalsIgnoreCase(util.DEFAULT));

		if (isDefault) {
			description = "Reset Default Template";
			bitmap = DrawView.getNine(0, 0, R.drawable.frame1, c.getWid(),
					c.getHi(), mContext, null);
			isCompatible = true;
			mSkinMessage = null;
		} else {
			isCompatible = (getBooleanSkin(mSkinPackageName));
			mSkinMessage = (isCompatible) ? null : getStringSkin(
					mSkinPackageName,
					"Not compatible\nTemplate: %s\nYour device: %s", true);
			description = getStringSkin(mSkinPackageName,
					"Device: %s  (%s)\nAuthor: %s", false);
			bitmap = util.getBitmapSkin(mSkinPackageName);
		}

		PreviewSkin preview = PreviewSkin.construct(listener, description,
				bitmap);
		preview.show(getFragmentManager(), null);

	}

	private Boolean getBooleanSkin(String pkg) {
		util.getSkinInfo(pkg);
		return (util.getType() == mydensiti);
	}

	private String getStringSkin(String pkg, String formatString,
			Boolean message) {
		util.getSkinInfo(pkg);

		return (message) ? String.format(formatString,
				util.typeSkin(util.getType()), util.typeSkin(mydensiti))
				: String.format(formatString, util.getSkinName(),
						util.typeSkin(util.getType()), util.getAuthorName());
	}

	private PreviewListener listener = new PreviewListener() {
		@Override
		public void onApply() {
			if (isCompatible) {
				if (isDefault)
					pref.resetSkin();
				else
					pref.cPaketName(mSkinPackageName);

				mSwitch.onSwitchContent(((MainActivity) mActivity).MAIN());
			} else {
				new ToastAlert(mContext, mSkinMessage, false);
			}
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
		}
	};
}
