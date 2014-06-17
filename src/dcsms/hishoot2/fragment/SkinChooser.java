package dcsms.hishoot2.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import dcsms.hishoot2.Hello;
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

	private HiPref pref;

	private ArrayList<Pair<String, Drawable>> paket = new ArrayList<Pair<String, Drawable>>();

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

	private Hello getMainActivity() {
		return (Hello) getActivity();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		pref = new HiPref(getMainActivity());
		c = new CekDensiti(getMainActivity());
		mydensiti = c.getDensitiType();

		util = new SkinsUtil(getMainActivity());
		SkinAdapter adapter = new SkinAdapter(getMainActivity());
		mSwitch = (Switcher) getMainActivity();
		mSwitch.onCustomActionBar(false, R.string.title_skin,
				R.string.subtitle_skin);

		// TODO add default template
		adapter.add(new SkinItem(util.DEFAULT, String.format(
				"Device: %s\nAuthor: %s", "Default Template", "DCSMS"),
				getMainActivity().getResources().getDrawable(R.drawable.ic_launcher)));

		loadSkinPackage();

		if (paket != null) {
			for (int i = 0; i < paket.size(); i++) {
				Pair<String, Drawable> pair = paket.get(i);
				String p = pair.first;
				Drawable d = pair.second;
				// TODO
				adapter.add(new SkinItem(p, getStringSkin(p, false), d));
			}
		}

		setListAdapter(adapter);

	}

	private void loadSkinPackage() {
		paket.clear();

		PackageManager pm = getMainActivity().getPackageManager();
		Intent i = new Intent(Intent.ACTION_MAIN, null);
		i.addCategory("dcsms.hishoot.SKINTEMPLATE");
		List<ResolveInfo> apps = pm.queryIntentActivities(i, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));
		for (ResolveInfo skin : apps) {
			ActivityInfo ai = skin.activityInfo;
			String pkg = ai.packageName;
			Drawable icon = ai.loadIcon(pm);
			// TODO: filtered type with deviceDpi
			// util.getSkinInfo(pkg);
			// if (util.getType() == mydensiti)

			paket.add(Pair.create(pkg, icon));
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
					c.getHi(), getMainActivity(), null);
			isCompatible = true;
			mSkinMessage = null;
		} else {
			isCompatible = (getBooleanSkin(mSkinPackageName));
			mSkinMessage = (isCompatible) ? null : getStringSkin(
					mSkinPackageName, true);
			description = getStringSkin(mSkinPackageName, false);
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

	private String getStringSkin(String pkg, Boolean isMessage) {
		String formatStringMessage = "Not compatible\nTemplate: %s\nYour device: %s";
		String formatString = "Device: %s  (%s)\nAuthor: %s";

		util.getSkinInfo(pkg);

		return (isMessage) ? String.format(formatStringMessage,
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

				mSwitch.onSwitchContent(((Hello) getMainActivity()).MAIN());
			} else {
				ToastAlert.create(getMainActivity(), mSkinMessage, false);
			}
		}

		@Override
		public void onCancel() {
		}
	};
}
