package dcsms.hishoot2;

import android.graphics.drawable.NinePatchDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import dcsms.hishoot2.fragment.HelloFragment;
import dcsms.hishoot2.fragment.MenuSliding;
import dcsms.hishoot2.fragment.Switcher;


import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Hello extends ActionBarActivity implements Switcher {
	private SlidingMenu mSlidingMenu;
	private boolean isLoading, isMain;
	private ActionBar mActionBar;
	static {
		System.loadLibrary("photoprocessing");
	}

	public native String dafuq();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme);
		mSlidingMenu = new SlidingMenu(this);

		mActionBar = getSupportActionBar();
		mActionBar.setIcon(R.drawable.ic_launcher);
		NinePatchDrawable bg = (NinePatchDrawable) this.getResources()
				.getDrawable(R.drawable.head);
		mActionBar.setBackgroundDrawable(bg);

		setContentView(R.layout.main_app);

		onSwitchContent(MAIN());
		initiateSlideMenu(mSlidingMenu);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menubar, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		if (mSlidingMenu.isMenuShowing()) {
			mSlidingMenu.showContent();
		} else if (!isMain) {
			onSwitchContent(MAIN());
		} else if (isLoading) {
			// FIXME:

		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item:
			if (isLoading) {
			} else {
				mSlidingMenu.toggle();
			}
			return true;

		case android.R.id.home:
			onSwitchContent(MAIN());
			return true;

		}
		return false;
	}

	private void initiateSlideMenu(SlidingMenu sm) {
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		sm.setMenu(R.layout.slidingmenu);
		sm.setMode(SlidingMenu.RIGHT);
		switchFragment(R.id.menu_frame, new MenuSliding());
	}

	private void switchFragment(int id, Object obj) {
		getSupportFragmentManager().beginTransaction()
				.replace(id, (Fragment) obj).commit();
	}

	public final HelloFragment MAIN() {
		return new HelloFragment();
	}

	/** public methods override/implement interface Switcher */

	/** {@link Switcher#onSwitchContent(Object)} */
	@Override
	public void onSwitchContent(Object o) {
		switchFragment(R.id.content_frame, o);
		if (mSlidingMenu.isMenuShowing())
			mSlidingMenu.showContent();
	}

	/** {@link Switcher#setLoading(Boolean)} */
	@Override
	public void setLoading(Boolean load) {
		isLoading = load;
		mSlidingMenu.setTouchModeAbove(load ? SlidingMenu.TOUCHMODE_NONE
				: SlidingMenu.TOUCHMODE_FULLSCREEN);
	}

	/** {@link Switcher#onCustomActionBar(Boolean, Integer, Integer)} */
	@Override
	public void onCustomActionBar(Boolean main, Integer title, Integer sub) {
		mActionBar.setDisplayHomeAsUpEnabled(!main);
		mActionBar.setHomeButtonEnabled(!main);
		mActionBar.setTitle(title);
		mActionBar.setSubtitle(sub);
		isMain = main;
	}
}