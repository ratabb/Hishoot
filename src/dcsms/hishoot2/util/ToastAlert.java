package dcsms.hishoot2.util;

import android.content.Context;
import android.widget.Toast;

public class ToastAlert {
	public final static ToastAlert create(Context context, String message,
			Boolean toastlong) {

		Toast.makeText(context, message,
				(toastlong) ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
		return null;

	}
}
