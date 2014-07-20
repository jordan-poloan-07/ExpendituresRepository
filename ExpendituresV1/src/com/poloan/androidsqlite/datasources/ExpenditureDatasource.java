package com.poloan.androidsqlite.datasources;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.poloan.androidsqlite.database.SQLiteHelper;
import com.poloan.androidsqlite.entity.Expenditure;

public class ExpenditureDatasource {

	private SQLiteDatabase database;

	private SQLiteHelper sqlHelper;

	private String[] columns = { SQLiteHelper.COLUMN_ID,
			SQLiteHelper.COLUMN_EXPENDITURES, SQLiteHelper.COLUMN_DATE };

	public ExpenditureDatasource(Context context) {
		sqlHelper = new SQLiteHelper(context);
	}

	public void open() {
		database = sqlHelper.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	public Expenditure addExpenditure(String amountString, String dateLongString) {

		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_EXPENDITURES, amountString);
		values.put(SQLiteHelper.COLUMN_DATE, dateLongString);

		long insertId = database
				.insert(SQLiteHelper.EXPEN_TABLES, null, values);

		Cursor cursor = database.query(SQLiteHelper.EXPEN_TABLES, columns,
				SQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);

		cursor.moveToFirst();

		Expenditure newExpenditure = cursorToExpenditure(cursor);

		cursor.close();

		return newExpenditure;
	}

	public void removeExpenditure(Expenditure expenditure) {

	}

	public List<Expenditure> getAllExpenditures() {

		List<Expenditure> expenditures = new ArrayList<Expenditure>();

		Cursor cursor = database.query(SQLiteHelper.EXPEN_TABLES, columns,
				null, null, null, null, null);

		cursor.moveToFirst();

		while (!cursor.isAfterLast()) {
			Expenditure expenditure = cursorToExpenditure(cursor);
			expenditures.add(expenditure);
			cursor.moveToNext();
		}

		cursor.close();

		return expenditures;

	}

	public List<Expenditure> getAllExpendituresForToday() {

		DateTime now = new DateTime();

		return getSelectedExpenditures(now);

	}

	public List<Expenditure> getSelectedExpenditures(final DateTime time) {

		Predicate<Expenditure> timePredicate = new Predicate<Expenditure>() {
			@Override
			public boolean apply(Expenditure exp) {

				int expDay = exp.getDateTime().getDayOfMonth();
				int expMonth = exp.getDateTime().getMonthOfYear();
				int expYear = exp.getDateTime().getYear();

				return (time.getYear() == expYear
						&& time.getMonthOfYear() == expMonth && time
						.getDayOfMonth() == expDay);
			}
		};

		return getSelectedExpenditures(time, timePredicate);
	}

	public List<Expenditure> getSelectedExpenditures(final DateTime time,
			Predicate<Expenditure> timePredicate) {

		List<Expenditure> expenditures = new ArrayList<Expenditure>(
				Collections2.filter(getAllExpenditures(), timePredicate));

		return expenditures;
	}

	private Expenditure cursorToExpenditure(Cursor cursor) {

		Expenditure expenditure = new Expenditure();

		BigDecimal amount = null;
		DateTime dateTime = null;

		try {
			amount = new BigDecimal(cursor.getString(1));
			dateTime = new DateTime(Long.valueOf(cursor.getString(2)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return null;
		}

		expenditure.setAmount(amount);
		expenditure.setDateTime(dateTime);

		return expenditure;
	}

}
