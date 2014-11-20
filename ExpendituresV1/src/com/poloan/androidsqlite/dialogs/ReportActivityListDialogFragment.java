package com.poloan.androidsqlite.dialogs;

import com.poloan.androidsqlite.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class ReportActivityListDialogFragment extends DialogFragment {

	public interface ReportActivityListCallback {

		void executeReportActivity(String reportActivity);
	}

	private ReportActivityListCallback ralcBack;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		ralcBack = (ReportActivityListCallback) getActivity();

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// TODO change this
		final String[] choices = { "Red", "Blue", "Green" };

		builder.setTitle(R.string.report_dialog_title).setItems(choices,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						ralcBack.executeReportActivity(choices[which]);
					}
				});

		return builder.create();
	}

}
