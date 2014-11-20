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

	public List<Expenditure> getExpendituresForToday() {

		DateTime now = new DateTime();

		return getExpendituresByDate(now);
	}

	public List<Expenditure> getExpendituresByDate(final DateTime date) {

		Predicate<Expenditure> timePredicate = new Predicate<Expenditure>() {
			@Override
			public boolean apply(Expenditure exp) {

				int expDay = exp.getDateTime().getDayOfMonth();
				int expMonth = exp.getDateTime().getMonthOfYear();
				int expYear = exp.getDateTime().getYear();

				return (date.getYear() == expYear
						&& date.getMonthOfYear() == expMonth && date
						.getDayOfMonth() == expDay);
			}
		};

		return getSelectedExpenditures(timePredicate);
	}

	public List<Expenditure> getExpendituresThisWeek(final DateTime setDate) {

		Predicate<Expenditure> weekPredicate = new Predicate<Expenditure>() {
			@Override
			public boolean apply(Expenditure exp) {
				DateTime now = (setDate != null) ? setDate : getNow();

				boolean week = exp.getDateTime().getWeekOfWeekyear() == now
						.getWeekOfWeekyear();

				boolean year = exp.getDateTime().getYear() == now.getYear();

				return week && year;
			}
		};

		return getSelectedExpenditures(weekPredicate);
	}

	public List<Expenditure> getExpendituresThisMonth(final DateTime setDate) {

		Predicate<Expenditure> monthPredicate = new Predicate<Expenditure>() {
			@Override
			public boolean apply(Expenditure exp) {

				DateTime now = (setDate != null) ? setDate : getNow();

				boolean month = exp.getDateTime().getMonthOfYear() == now
						.getMonthOfYear();

				boolean year = exp.getDateTime().getYear() == now.getYear();

				return month && year;
			}
		};

		return getSelectedExpenditures(monthPredicate);
	}

	public List<Expenditure> getExpendituresThisYear(final DateTime setDate) {

		Predicate<Expenditure> yearPredicate = new Predicate<Expenditure>() {
			@Override
			public boolean apply(Expenditure exp) {
				DateTime now = (setDate != null) ? setDate : getNow();

				return exp.getDateTime().getYear() == now.getYear();
			}
		};

		return getSelectedExpenditures(yearPredicate);
	}

	public List<Expenditure> getExpendituresInBetween(final DateTime from,
			final DateTime to) {

		Predicate<Expenditure> inBetweenPredicate = new Predicate<Expenditure>() {
			@Override
			public boolean apply(Expenditure exp) {

				long millis = exp.getDateTime().toInstant().getMillis();

				return from.isBefore(millis) && to.isAfter(millis);
			}
		};

		return getSelectedExpenditures(inBetweenPredicate);
	}

	public List<Expenditure> getSelectedExpenditures(
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

	private DateTime getNow() {
		return new DateTime();
	}

}
