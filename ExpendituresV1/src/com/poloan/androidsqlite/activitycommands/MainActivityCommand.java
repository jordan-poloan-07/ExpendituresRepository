package com.poloan.androidsqlite.activitycommands;

import com.poloan.androidsqlite.utilities.ActivityCommand;
import com.poloan.androidsqlite.utilities.Command;

public class MainActivityCommand extends ActivityCommand {

	private Command updateExpendituresCommand;
	private Command updateTotalCommand;
	private Command dateChangeCommand;

	
	public void setUpdateExpendituresCommand(Command updateExpendituresCommand) {
		this.updateExpendituresCommand = updateExpendituresCommand;
	}

	public void setUpdateTotalCommand(Command updateTotalCommand) {
		this.updateTotalCommand = updateTotalCommand;
	}

	public void setDateChangeCommand(Command dateChangeCommand) {
		this.dateChangeCommand = dateChangeCommand;
	}

	public void totalChange() {
		updateTotalCommand.execute();
	}

	public void inputChange() {
		updateExpendituresCommand.execute();
		updateTotalCommand.execute();
	}

	public void dateChange() {
		dateChangeCommand.execute();
		updateTotalCommand.execute();
	}

}
