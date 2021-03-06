package dcsms.hishoot2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import dcsms.hishoot2.Hello;
import dcsms.hishoot2.R;
import dcsms.hishoot2.util.HiPref;
import dcsms.hishoot2.util.ToastAlert;

public class About extends Fragment implements View.OnClickListener {

	private Activity mActivity;
	private Switcher mSwitch;
	private Context mContext;
	private HiPref pref;

	private long[] mHits = new long[5];
	private EditText edit;
	public static final String ILLEGAL = "org.illegal.grepe.hishoot";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (Hello) getActivity();
		mSwitch = (Switcher) mActivity;
		mSwitch.onCustomActionBar(false, R.string.title_about,
				R.string.subtitle_about);
		mContext = getActivity();
		pref = new HiPref(mContext);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about, null);
		ImageView logo = (ImageView) view.findViewById(R.id.ivLogo);
		logo.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.ivLogo) {
			// TODO:
			System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
			mHits[mHits.length - 1] = SystemClock.uptimeMillis();
			if (mHits[0] >= (SystemClock.uptimeMillis() - 1000)) {
				LinearLayout rootDialog = new LinearLayout(mContext);
				edit = new EditText(mContext);
				edit.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
				edit.setMaxLines(1);
				edit.setHint(pref.getPref().getString(ILLEGAL, "ILLEGAL"));

				rootDialog.addView(edit, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
				dialog.setCancelable(false);
				dialog.setTitle(android.R.string.dialog_alert_title);
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setNeutralButton(android.R.string.ok,
						new OnClickListener() {

							@SuppressLint("DefaultLocale")
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								String input = edit.getText().toString()
										.toUpperCase();
								pref.PrefString(ILLEGAL, input);
								dialog.dismiss();
								ToastAlert.create(mContext, input, false);
								mSwitch.onSwitchContent(((Hello) mActivity)
										.MAIN());

							}
						});

				dialog.setView(rootDialog);
				dialog.show();

			}
		}
	}

}
