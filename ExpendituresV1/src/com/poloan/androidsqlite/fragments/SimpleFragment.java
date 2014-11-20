package com.poloan.androidsqlite.fragments;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.poloan.androidsqlite.R;
import com.poloan.androidsqlite.datasources.ExpenditureDatasource;
import com.poloan.androidsqlite.entity.Expenditure;

public class SimpleFragment extends Fragment {

	private BigDecimal total;
	private String mode;
	private DateTime setDate;
	private String intro;

	@Override
	public void onCreate(Bundle bundle) {

		super.onCreate(bundle);

		List<Expenditure> expenditures = null;

		ExpenditureDatasource datasource = new ExpenditureDatasource(
				getActivity());
		datasource.open();

		mode = getArguments().getString("mode");
		setDate = (DateTime) getArguments().get("setDate");

		if (mode.equals(getString(R.string.report_activity_week))) {

			expenditures = datasource.getExpendituresThisWeek(setDate);

			DateTime start = setDate.minusDays(setDate.dayOfWeek().get() - 1);
			DateTime end = setDate.plusDays(7 - setDate.dayOfWeek().get());

			String dateFormat = "yyyy-MM-dd";

			intro = String.format("Week no. %s, %s --- %s",
					setDate.getWeekOfWeekyear(), start.toString(dateFormat),
					end.toString(dateFormat));

		} else if (mode.equals(getString(R.string.report_activity_month))) {
			expenditures = datasource.getExpendituresThisMonth(setDate);

			intro = "Month of " + setDate.monthOfYear().getAsText();

		} else if (mode.equals(getString(R.string.report_activity_year))) {
			expenditures = datasource.getExpendituresThisYear(setDate);

			intro = "Year " + setDate.year().getAsText();
		}

		total = BigDecimal.ZERO;

		for (Expenditure exp : expenditures) {
			total = total.add(exp.getAmount());
		}

		datasource.close();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// you wanna know why ?
		// the three fragments share a common layout (R.layout.fragment_simple),
		// each onCreateView (tab select -> fragment adding/attaching) will add
		// the layout to container
		// FragmentTransaction.detach does not destroy the view of the it's
		// fragment
		// so each tabs layout stacks
		if (container.findViewById(R.id.fragment_simple_id) == null)
			inflater.inflate(R.layout.fragment_simple, container);
		TextView textView = (TextView) container.findViewById(R.id.textView1);
		textView.setText(String.format("%s\n%s %s", intro, mode, total));
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
