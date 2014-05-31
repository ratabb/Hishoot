package dcsms.hishoot2.fragment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import dcsms.hishoot2.R;

public class PreviewSkin extends DialogFragment implements View.OnClickListener {
	private Dialog mDialog;
	private static String mDescription;
	private static Bitmap mBitmap;

	private static PreviewListener mPreviewListener;

	public static PreviewSkin construct(PreviewListener listener, String desc,
			Bitmap bmp) {
		mDescription = desc;
		mBitmap = bmp;
		mPreviewListener = listener;
		return new PreviewSkin();
	}

	public interface PreviewListener {
		void onApply();

		void onCancel();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDialog = super.onCreateDialog(savedInstanceState);
		mDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		mDialog.setContentView(dialogView());

		return mDialog;
	}

	private View dialogView() {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.preview, null);

		Button mButtonApply = (Button) view.findViewById(R.id.bApply);
		Button mButtonCancel = (Button) view.findViewById(R.id.bCancel);
		ImageView ivPreview = (ImageView) view.findViewById(R.id.preview_image);
		TextView tvDescription = (TextView) view.findViewById(R.id.description);

		ivPreview.setImageBitmap(mBitmap);
		tvDescription.setText(mDescription);
		mButtonCancel.setOnClickListener(this);
		mButtonApply.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.bApply)
			mPreviewListener.onApply();

		dismiss();
	}

}