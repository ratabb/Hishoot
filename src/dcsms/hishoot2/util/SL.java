package dcsms.hishoot2.util;

import android.graphics.drawable.NinePatchDrawable;
import android.support.v7.app.ActionBarActivity;

import dcsms.hishoot2.R;

public class SL {
	public SL(ActionBarActivity s,String title,String sub){
		s.getSupportActionBar().setLogo(R.drawable.ic_launcher);
		NinePatchDrawable bg =  (NinePatchDrawable) s.getResources().getDrawable(R.drawable.head);
		s.getSupportActionBar().setBackgroundDrawable(bg);
		s.getSupportActionBar().setTitle(title);
		s.getSupportActionBar().setSubtitle(sub);
	}

}
