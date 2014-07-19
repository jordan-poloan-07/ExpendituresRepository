package com.poloan.androidsqlite.entity;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.DateTime;

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

	@Override
	public String toString() {
		return "Php " + amount.toPlainString() + ", " + dateTime.toString();
	}

}
