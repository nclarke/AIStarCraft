package javabot;

/**
 * Example of a Java AI Client that does nothing.
 */
import javabot.model.Unit;
import javabot.util.BWColor;
public class MinimalAIClient implements BWAPIEventListener {

	JNIBWAPI bwapi;
	public static void main(String[] args) {
		new MinimalAIClient();
	}
	
	public MinimalAIClient() {
		bwapi = new JNIBWAPI(this);
		bwapi.start();
	} 

        // ex: build(UnitTypes.Terran_Supply_Depot) to build supply depots
	public void build(UnitTypes bldg) {
		int Mreq = 0, Greq = 0;

		if(bldg.compareTo(UnitTypes.Terran_Academy) == 0){
			Mreq = 150;
			Greq = 0;
		} else if (bldg.compareTo(UnitTypes.Terran_Armory) == 0) {
			Mreq = 100;
			Greq = 50;
		} else if (bldg.compareTo(UnitTypes.Terran_Barracks) == 0) {
			Mreq = 150;
			Greq = 0;
		} else if (bldg.compareTo(UnitTypes.Terran_Bunker) == 0) {
			Mreq = 100;
			Greq = 0;
		} else if (bldg.compareTo(UnitTypes.Terran_Comsat_Station) == 0) {
			Mreq = 50;
			Greq = 50;
		} else if (bldg.compareTo(UnitTypes.Terran_Control_Tower) == 0) {
			Mreq = 50;
			Greq = 50;
		} else if (bldg.compareTo(UnitTypes.Terran_Covert_Ops) == 0) {			
			Mreq = 50;
		    Greq = 50;
		} else if (bldg.compareTo(UnitTypes.Terran_Engineering_Bay) == 0) {
			Mreq = 125;
			Greq = 0;
		} else if (bldg.compareTo(UnitTypes.Terran_Factory) == 0) {
			Mreq = 200;
			Greq = 100;
		} else if (bldg.compareTo(UnitTypes.Terran_Machine_Shop) == 0) {
			Mreq = 50;
			Greq = 50;
		} else if (bldg.compareTo(UnitTypes.Terran_Missile_Turret) == 0) {			
			Mreq = 75;
		    Greq = 0;
		} else if (bldg.compareTo(UnitTypes.Terran_Nuclear_Silo) == 0) {
			Mreq = 100;
			Greq = 100;
		} else if (bldg.compareTo(UnitTypes.Terran_Physics_Lab) == 0) {			
			Mreq = 50;
		    Greq = 50;
		} else if (bldg.compareTo(UnitTypes.Terran_Refinery) == 0) {
			Mreq = 100;
			Greq = 0;
		} else if (bldg.compareTo(UnitTypes.Terran_Science_Facility) == 0) {
			Mreq = 100;
			Greq = 150;
		} else if (bldg.compareTo(UnitTypes.Terran_Starport) == 0) {
			Mreq = 150;
			Greq = 100;
		} else if (bldg.compareTo(UnitTypes.Terran_Supply_Depot) == 0) {
			Mreq = 100;
			Greq = 0;
		}

			// Check if we have enough minerals,
		if (bwapi.getSelf().getMinerals() >= Mreq && bwapi.getSelf().getGas() >= Greq) {
			// try to find the worker near our home position
			int worker = getNearestUnit(UnitTypes.Terran_SCV.ordinal(), homePositionX, homePositionY);
			if (worker != -1) {
				// if we found him, try to select appropriate build tile position for bldg (near our home base close to similar structures)
				int xtile = homePositionX, ytile = homePositionY;
				
				for(Unit unit : bwapi.getMyUnits()) {
					if(unit.getTypeID() == bldg.ordinal()) {
						xtile = unit.getX();
						ytile = unit.getY();
						break;
					}
				}
				   Point buildTile = getBuildTile(worker, bldg.ordinal(), xtile, ytile);
				// if we found a good build position, and we aren't already constructing a bldg 
				// order our worker to build it
				if ((buildTile.x != -1) && (!weAreBuilding(bldg.ordinal()))) {
					bwapi.build(worker, buildTile.x, buildTile.y, bldg.ordinal());
				}
			}
		}
		else {
			// report insufficient resources
		}
	}

	public void connected() {}	
	public void gameStarted() {}
	public void gameUpdate() 
	{
		for(Unit u : bwapi.getAllUnits())
		{
			bwapi.drawCircle(u.getX(), u.getY(), 5, BWColor.RED, true, false);
		}
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
