package dcsms.hishoot2.skinmanager;

import android.graphics.drawable.Drawable;

public class SkinItem {
	String pkgName, des;
	Drawable icon;

	public SkinItem(String _pkg, String _des, Drawable _icon) {
		this.pkgName = _pkg;
		this.des = _des;
		this.icon = _icon;
	}
}
