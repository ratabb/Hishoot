package dcsms.hishoot2.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

//import dcsms.hishoot2b.MainActivity;
import dcsms.hishoot2.MainActivity;
import dcsms.hishoot2.R;

public class MenuSliding extends ListFragment {
	private Activity mActivity;
	private int[] menus = { R.string.menu_about, R.string.menu_template };
	private int[] ic_menus = { android.R.drawable.ic_menu_info_details,
			android.R.drawable.ic_menu_gallery };

	private Switcher mSwitch;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (MainActivity) getActivity();
		MenuAdapter adapter = new MenuAdapter(getActivity());

		for (int i = 0; i < menus.length; i++) {
			adapter.add(new MenuItem(menus[i], ic_menus[i]));
		}
		setListAdapter(adapter);
		mSwitch = (Switcher) mActivity;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		switch (v.getId()) {
		default:
			return;
		case R.string.menu_about:
			mSwitch.onSwitchContent(new About());
			break;
		case R.string.menu_template:
			mSwitch.onSwitchContent(new SkinChooser());
			break;
		}

	}

	private class MenuAdapter extends ArrayAdapter<MenuItem> {

		private MenuAdapter(Context context) {
			super(context, 0);

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView
					.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView
					.findViewById(R.id.row_title);
			title.setText(getItem(position).title);
			convertView.setId(getItem(position).title);

			return convertView;

		}

	}

	private class MenuItem {
		private int title;
		private int iconRes;

		private MenuItem(int title, int iconRes) {
			this.title = title;
			this.iconRes = iconRes;
		}
	}

}
