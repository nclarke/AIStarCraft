package javabot.AIStarCraft;

/**
 * Example of a Java AI Client that does nothing.
 */
import java.util.LinkedList;

import javabot.types.*;
import javabot.types.UnitType.UnitTypes;
import javabot.util.*;
import javabot.model.*;
public class leija_overhead implements BWAPIEventListener {

	JNIBWAPI bwapi;
	LinkedList<UnitTypes> core_econ_buildingStack;
	LinkedList<UnitTypes> core_econ_unitsStack;
	LinkedList<BuildAlerts> core_econ_buildAlert;
	BuildMode core_econ_buildMode;
	
	public enum BuildMode {
		FIRST_POSSIBLE, BLOCKING_STACK, HOLD_ALL
	};
	
	public enum BuildAlerts {
		NO_MINERALS, NO_GAS, NO_ROOM
	};
	
	public static void main(String[] args) {
		new MinimalAIClient();
	}
	
	public leija_overhead() {
		bwapi = new JNIBWAPI(this);
		bwapi.start();
	} 

	public void connected() {}	
	public void gameStarted() {
		//call coreLoad
	}
	public void gameUpdate() 
	{
		for(Unit u : bwapi.getAllUnits())
		{
			bwapi.drawCircle(u.getX(), u.getY(), 5, BWColor.RED, true, false);
		}
		
		//call action every 30 seconds
		// Call the act() method every 30 frames
		if (bwapi.getFrameCount() % 30 == 0) {
			leija_coreAction(); 
		}		
	}
	
	public void leija_coreLoad() {
		// Set up build stack
		core_econ_buildingStack = new LinkedList<UnitTypes>();
		core_econ_unitsStack = new LinkedList<UnitTypes>();
		
		// Set default build mode to process everything in the stack in-order and normally
		core_econ_buildMode = BuildMode.BLOCKING_STACK;
		
		// Now populate the buildingStack
		core_econ_buildingStack.push(UnitTypes.Terran_Supply_Depot);
		core_econ_buildingStack.push(UnitTypes.Terran_Supply_Depot);
		core_econ_buildingStack.push(UnitTypes.Terran_Supply_Depot);
		core_econ_buildingStack.push(UnitTypes.Terran_Supply_Depot);
		
		// Set up exploration map
		// Set up unit assignment groups hash map
		
		// Set up locations of units
	}
	
	public void leija_coreAction() {
		// Address resources
		// Address build stack with respect to military
		// Add to threat list
		author_econAction();
		
		author_militaryAction();
	}
	
	public void gameEnded() { }
	public void keyPressed(int keyCode) {}
	public void matchEnded(boolean winner) { }
	public void nukeDetect(int x, int y) { }
	public void nukeDetect() { }
	public void playerLeft(int id) { }
	public void unitCreate(int unitID) { }
	public void unitDestroy(int unitID) { }
	public void unitDiscover(int unitID) { }
	public void unitEvade(int unitID) { }
	public void unitHide(int unitID) { }
	public void unitMorph(int unitID) { }
	public void unitShow(int unitID) { }
}
