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
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);

		// TODO Pass date to TabListener

		Tab weekTab = actionBar
				.newTab()
				.setText("Week")
				.setTabListener(
						new TabListener<SimpleFragment>(this, "week",
								SimpleFragment.class));

		Tab monthTab = actionBar
				.newTab()
				.setText("Month")
				.setTabListener(
						new TabListener<SimpleFragment>(this, "month",
								SimpleFragment.class));

		Tab yearTab = actionBar
				.newTab()
				.setText("Year")
				.setTabListener(
						new TabListener<SimpleFragment>(this, "day",
								SimpleFragment.class));

		Tab customRangeTab = actionBar
				.newTab()
				.setText("Custom Range")
				.setTabListener(
						new TabListener<SimpleFragment>(this, "custom",
								SimpleFragment.class));

		actionBar.addTab(weekTab);
		actionBar.addTab(monthTab);
		actionBar.addTab(yearTab);
		actionBar.addTab(customRangeTab);

	}

	public static class TabListener<T extends Fragment> implements
			ActionBar.TabListener {

		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		private TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// TODO and in here pass the date to the fragment

			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
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

	}

}
