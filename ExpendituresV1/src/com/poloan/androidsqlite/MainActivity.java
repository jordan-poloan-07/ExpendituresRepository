package com.poloan.androidsqlite;

import java.math.BigDecimal;
import java.util.List;

import org.joda.time.DateTime;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.primitives.Doubles;
import com.poloan.androidsqlite.activitycommands.MainActivityCommand;
import com.poloan.androidsqlite.datasources.ExpenditureDatasource;
import com.poloan.androidsqlite.dialogs.DateDialogFragment;
import com.poloan.androidsqlite.dialogs.DateDialogFragment.DateDialogDateCallback;
import com.poloan.androidsqlite.entity.Expenditure;
import com.poloan.androidsqlite.utilities.Command;

public class MainActivity extends Activity implements View.OnClickListener,
		DateDialogDateCallback {

	private ExpenditureDatasource expenditureDatasource;

	private EditText expendAmountInput;
	private Button inputBtn;
	private ListView expendituresList;
	private TextView totalExpendAmount;
	private ArrayAdapter<Expenditure> arrayAdapter;

	private DateTime time;

	private MainActivityCommand changeListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		expendAmountInput = (EditText) findViewById(R.id.editText1);
		inputBtn = (Button) findViewById(R.id.button1);
		expendituresList = (ListView) findViewById(R.id.listView1);
		totalExpendAmount = (TextView) findViewById(R.id.textView2);

		expenditureDatasource = new ExpenditureDatasource(this);
		expenditureDatasource.open();

		final List<Expenditure> expenditures = expenditureDatasource
				.getAllExpendituresForToday();

		arrayAdapter = new ArrayAdapter<Expenditure>(this,
				android.R.layout.simple_list_item_1, expenditures);

		inputBtn.setOnClickListener(this);
		expendituresList.setAdapter(arrayAdapter);

		changeListener = new MainActivityCommand();

		changeListener.setUpdateExpendituresCommand(new Command() {
			@Override
			public void execute() {
				expendAmountInput.setText(R.string.clear_string);
				arrayAdapter.notifyDataSetChanged();
			}
		});

		changeListener.setDateChangeCommand(new Command() {
			@Override
			public void execute() {
				expenditures.clear();
				List<Expenditure> selectedExpenditures = expenditureDatasource
						.getSelectedExpenditures(time);
				expenditures.addAll(selectedExpenditures);
				arrayAdapter.notifyDataSetChanged();
			}
		});

		changeListener.setUpdateTotalCommand(new Command() {
			@Override
			public void execute() {
				BigDecimal total = BigDecimal.ZERO;

				for (Object obj : expenditures) {
					Expenditure exp = (Expenditure) obj;
					total = total.add(exp.getAmount());
				}

				totalExpendAmount.setText(getString(R.string.totalLabel)
						+ total.toPlainString());
			}
		});
	}

	@Override
	public void onClick(View v) {

		if (v == inputBtn) {

			String amount = expendAmountInput.getText().toString();
			String date = String.valueOf(time == null ? System
					.currentTimeMillis() : time.toInstant().getMillis());

			Expenditure exp = addExpenditure(amount, date);

			if (exp != null) {
				arrayAdapter.add(exp);
				changeListener.inputChange();
			}

		}

	}

	private Expenditure addExpenditure(String amount, String date) {

		if (Doubles.tryParse(amount) != null) {
			return expenditureDatasource.addExpenditure(amount, date);
		} else {
			toastMessage("Input a valid number in the text field");
			return null;
		}

	}

	private void toastMessage(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void changeDate(DateTime date) {
		time = date;
		changeListener.dateChange();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.change_date:
			DialogFragment newFragment = new DateDialogFragment();
			newFragment.show(getFragmentManager(), "datePicker");
			return true;
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onResume() {
		if (expenditureDatasource != null)
			expenditureDatasource.open();
		changeListener.totalChange();
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (expenditureDatasource != null)
			expenditureDatasource.close();
		super.onPause();
	}

}