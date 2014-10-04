package sk.fiit.jim.agent;

import sk.fiit.jim.Settings;
import sk.fiit.jim.agent.models.EnvironmentModel;
import sk.fiit.jim.log.Log;
import sk.fiit.jim.log.LogType;
import sk.fiit.robocup.library.init.Script;

/**
 *  Implements the planning module. Calls planning scripts implemented in ruby.
 *  A planning script contains a ruby class that has a method called <code>control</code>.
 *  For details on how the planner works, see the appropriate <a href="http://team17-11.ucebne.fiit.stuba.sk/wiki/Pl%C3%A1novanie_a_vykon%C3%A1vanie_pohybov#Hlavn.C3.BD_cyklus_agenta">page in our wiki</a>
 *
 *@Title	Jim
 *@author	marosurbanec
 *@author	Androids
 */
public class Planner{
	private static final double ENOUGH_TIME_FOR_PHASE = 200.0;
	private static final int ENOUGH_TIME = 5*1000*1000; //5 millisecond = 5,000,000 nanoseconds
	private static Script planningScript = Script.createScript(Settings.getString("Planner")+".instance.control");
	public static double nextTimePhaseEnds;
	public static long loopStart;
	
	/**
	 * Sets a new planner.
	 *
	 * @param planClassName the class name of the planner within the ruby script
	 */
	public static void setPlanner(String planClassName) {
		planningScript = Script.createScript(planClassName + ".instance.control");
	}
	
	/**
	 * Executes the planning script by calling the plan object's <code>control</code> method
	 */
	public static void proceed(){
		planningScript.execute();
	}
		
	public static void runGarbageCollectIfEnoughTime(){
		if (!Settings.getBoolean("runGcOnPhaseStart"))
			return;
		long now = System.nanoTime();
		double serverTime = EnvironmentModel.SIMULATION_TIME;
		Log.debug(LogType.PLANNING, "Loop took %d ms", (now - loopStart));
		if ((now - loopStart) < ENOUGH_TIME && (nextTimePhaseEnds - serverTime) > ENOUGH_TIME_FOR_PHASE){
			Log.debug(LogType.PLANNING, "We have enough time, running GC and finalization");
			runGc();
		}
	}

	private static void runGc(){
		new Thread(new Runnable(){
			public void run(){
				System.runFinalization();
				System.gc();
			}
		}).start();
	}
}