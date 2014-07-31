package com.poloan.androidsqlite.entity;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

import android.annotation.SuppressLint;

public class Expenditure {

	public BigDecimal amount;
	public DateTime dateTime;

	public Expenditure() {
		super();
	}

	public Expenditure(BigDecimal amount, Date date) {
		super();
		this.amount = amount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public String toString() {
		return String.format("%s %.2f",
				Currency.getInstance(Locale.getDefault()), amount.floatValue());
	}

}
