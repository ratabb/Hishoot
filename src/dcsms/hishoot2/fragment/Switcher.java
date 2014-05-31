package dcsms.hishoot2.fragment;

/**
 * Switcher 
 * 
 */
public interface Switcher {
	/**
	 * onSwitchContent {@link Switcher}
	 * 
	 * @param obj
	 */
	void onSwitchContent(Object obj);

	/**
	 * setLoading {@link Switcher}
	 * 
	 * @param load
	 */
	void setLoading(Boolean load);

	/**
	 * onCustomActionBar {@link Switcher}
	 * 
	 * @param main
	 * @param title
	 * @param sub
	 */
	void onCustomActionBar(Boolean main, Integer title, Integer sub);
}
