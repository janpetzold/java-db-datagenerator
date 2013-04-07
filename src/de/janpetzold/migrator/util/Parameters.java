package de.janpetzold.migrator.util;

public class Parameters {
	public static int checkParameters(String[] args) {
		if(args.length == 1 && args[0].equals("-read")) {
			return 1;
		} else if(args.length == 2 && args[0].equals("-write")) {
			return 2;
		} else {
			Log.console("**************************************************************");
			Log.console("*                                                            *");
			Log.console("* The parameters were not set correctly.                     *");
			Log.console("*                                                            *");
			Log.console("* Syntax:                                                    *");
			Log.console("*                                                            *");
			Log.console("* -read Reads source data                                    *");
			Log.console("* -write <Number> Writes <Number> of new entries to database *");
			Log.console("*                                                            *");
			Log.console("**************************************************************");
			System.exit(0);
		}
		return 0;
	}
}
