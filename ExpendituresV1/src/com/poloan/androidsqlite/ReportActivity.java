package com.poloan.androidsqlite;

import org.joda.time.DateTime;
import com.poloan.androidsqlite.fragments.SimpleFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class ReportActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DateTime date = (DateTime) getIntent().getSerializableExtra("setDate");

		// Log.d("datetime set!!", date.toString());

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		TabListener<SimpleFragment> weekTabListener = TabListener
				.newFragmentListener(SimpleFragment.class, this,
						getString(R.string.report_activity_week), date);

		Tab weekTab = actionBar.newTab().setText("Week")
				.setTabListener(weekTabListener);

		TabListener<SimpleFragment> monthTabListener = TabListener
				.newFragmentListener(SimpleFragment.class, this,
						getString(R.string.report_activity_month), date);

		Tab monthTab = actionBar.newTab().setText("Month")
				.setTabListener(monthTabListener);

		TabListener<SimpleFragment> yearTabListener = TabListener
				.newFragmentListener(SimpleFragment.class, this,
						getString(R.string.report_activity_year), date);

		Tab yearTab = actionBar.newTab().setText("Year")
				.setTabListener(yearTabListener);

		// TabListener<SimpleFragment> customTabListener = TabListener
		// .newFragmentListener(SimpleFragment.class, this,
		// getString(R.string.report_activity_custom), date);
		//
		// Tab customRangeTab = actionBar.newTab().setText("Custom")
		// .setTabListener(customTabListener);

		actionBar.addTab(weekTab);
		actionBar.addTab(monthTab);
		actionBar.addTab(yearTab);
		// actionBar.addTab(customRangeTab);

	}

	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {

		private Fragment mFragment;
		private final Class<T> mClass;
		private final Activity mActivity;
		private final String mTag;
		private final DateTime mDate;

		private TabListener(Class<T> clz, Activity activity, String tag,
				DateTime date) {
			mClass = clz;
			mActivity = activity;
			mTag = tag;
			mDate = date;
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				Bundle bundle = new Bundle();
				bundle.putString("mode", mTag);
				bundle.putSerializable("setDate", mDate);

				mFragment = Fragment.instantiate(mActivity, mClass.getName(),
						bundle);

				ft.add(android.R.id.content, mFragment, mTag);

			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}

		}

		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
		}

		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}

		public static <T1 extends Fragment> TabListener<T1> newFragmentListener(
				Class<T1> clz, Activity activity, String tag, DateTime dateTime) {
			return new TabListener<T1>(clz, activity, tag, dateTime);
		}
	}

}
