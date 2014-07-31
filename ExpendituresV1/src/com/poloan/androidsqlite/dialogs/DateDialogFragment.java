package com.poloan.androidsqlite.dialogs;

import org.joda.time.DateTime;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DateDialogFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	public interface DateDialogCallback {

		void changeDate(DateTime date);
	}

	private DateDialogCallback dddcBack;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final DateTime now = new DateTime();

		int nowYear = now.getYear();
		int nowMonth = now.getMonthOfYear() - 1; // what a pity
		// months in joda are 1 - 12
		// android, (or java date api numbers it 0-11)
		int nowDay = now.getDayOfMonth();

		dddcBack = (DateDialogCallback) getActivity();

		return new DatePickerDialog(getActivity(), this, nowYear, nowMonth,
				nowDay);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		DateTime date = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
		dddcBack.changeDate(date);
	}
}
