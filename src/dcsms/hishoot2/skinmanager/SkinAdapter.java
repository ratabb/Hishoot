package dcsms.hishoot2.skinmanager;

import dcsms.hishoot2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SkinAdapter extends ArrayAdapter<SkinItem> {
	Context mContext;

	public SkinAdapter(Context context) {
		super(context, 0);
		mContext = context;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		if (view == null) {
			view = LayoutInflater.from(mContext)
					.inflate(R.layout.skinrow, null);
		}
		TextView tvDes = (TextView) view.findViewById(R.id.description);
		ImageView ivIcon = (ImageView) view.findViewById(R.id.ivIcon);

		view.setTag(getItem(position).pkgName);
		tvDes.setText(getItem(position).des);
		ivIcon.setImageDrawable(getItem(position).icon);

		return view;
	}
}
