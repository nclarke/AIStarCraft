package javabot;

import java.util.*;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.List;

import javabot.model.*;
import javabot.types.*;
import javabot.types.OrderType.OrderTypeTypes;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;

public class JavaBot implements BWAPIEventListener {
	
	// Some miscelaneous variables. Feel free to add yours.
	int homePositionX;
	int homePositionY;
	
	int marinesTotal = 0;
	
	/* HashMap for us to know what UnitTypes to train per Level - Level is determined by how well AI is doing in the game 
	 * (AI is very rich in the beginning, gets its level changed from ZERO to TWO) */
	HashMap<Level, ArrayList<UnitType.UnitTypes>> unitTypesPerLevel;
	
	private JNIBWAPI bwapi;
	public static void main(String[] args) {
		
		new JavaBot();
		
	}
	public JavaBot() {
		bwapi = new JNIBWAPI(this);
		bwapi.start();
		
		/* HashMap inits */
		unitTypesPerLevel = new HashMap<Level, ArrayList<UnitType.UnitTypes>>();
		initMap(unitTypesPerLevel);
	} 
	public void connected() {
		bwapi.loadTypeData();
	}
	
	// Method called at the beginning of the game.
	public void gameStarted() {		
		System.out.println("Game Started");

		// allow me to manually control units during the game
		bwapi.enableUserInput();
		
		// set game speed to 30 (0 is the fastest. Tournament speed is 20)
		// You can also change the game speed from within the game by "/speed X" command.
		bwapi.setGameSpeed(30);
		
		// analyze the map
		bwapi.loadMapData(true);
		
		// ============== YOUR CODE GOES HERE =======================

		// This is called at the beginning of the game. You can 
		// initialize some data structures (or do something similar) 
		// if needed. For example, you should maintain a memory of seen 
		// enemy buildings.
		
		bwapi.printText("Hello world!");
		bwapi.printText("This map is called "+bwapi.getMap().getName());
		bwapi.printText("My race ID: "+String.valueOf(bwapi.getSelf().getRaceID()));				// Z=0,T=1,P=2
		bwapi.printText("Enemy race ID: "+String.valueOf(bwapi.getEnemies().get(0).getRaceID()));	// Z=0,T=1,P=2
		
		// ==========================================================
	}
	
	/* Initializes a HashMap of UnitTypes we want per Level */
	public void initMap(HashMap<Level, ArrayList<UnitTypes>> lvlMap)
	{
		ArrayList<UnitTypes> levelZero = new ArrayList<UnitTypes>(Arrays.asList(UnitTypes.Terran_Marine));
		lvlMap.put(Level.ZERO, levelZero);
		
		ArrayList<UnitTypes> levelOne = new ArrayList<UnitTypes>(
				Arrays.asList(UnitTypes.Terran_Marine, UnitTypes.Terran_Medic));
		lvlMap.put(Level.ONE, levelOne);
		
		ArrayList<UnitTypes> levelTwo = new ArrayList<UnitTypes>(
				Arrays.asList(UnitTypes.Terran_Marine, UnitTypes.Terran_Medic, UnitTypes.Terran_Vulture));
		lvlMap.put(Level.TWO, levelTwo);
	}
	
	/* 
	 * Helper function for unitFormation
	 * 
	 * levelEnum:         takes in a Level Enum type (i.e. Level.ZERO)
	 * unitTypesPerLevel: for reading in the different UnitTypes we want per level
	 * 
	 * Returns an ArrayList of Units (max = 12) of the UnitTypes wanted in levelEnum
	 */
	public ArrayList<Unit> unitFormationHelper(Level levelEnum, HashMap<Level, ArrayList<UnitType.UnitTypes>> unitTypesPerLevel)
	{
		final int maxUnits = 12;
		ArrayList<Unit> unitRally = new ArrayList<Unit>();
		ArrayList<UnitType.UnitTypes> unitArr = unitTypesPerLevel.get(levelEnum);
		
		for (Unit unit : bwapi.getMyUnits())
		{
			for(int index = 0; index < unitArr.size(); index++)
			{
				if(unit.getTypeID() == unitArr.get(index).ordinal())
				{
					if(unitRally.size() < maxUnits)
					{
						unitRally.add(unit);
					}
				}
			}
		}
		return unitRally;
	}
	
	/*
	 * Handles the cases of different levels of the AI during the Game
	 * 
	 * levelEnum:         takes in a Level Enum type (i.e. Level.ZERO)
	 * unitTypesPerLevel: for reading in the different UnitTypes we want per level
	 * 
	 * Returns the ArrayList of Units created from unitFormationHelper
	 */
	public ArrayList<Unit> unitFormation(Level levelEnum, HashMap<Level, ArrayList<UnitType.UnitTypes>> unitTypesPerLevel)
	{	
		if(levelEnum.equals(Level.ZERO))
		{
			return unitFormationHelper(levelEnum, unitTypesPerLevel);
		}
		else if(levelEnum.equals(Level.ONE))
		{
			return unitFormationHelper(levelEnum, unitTypesPerLevel);
		}
		else if(levelEnum.equals(Level.TWO))
		{
			return unitFormationHelper(levelEnum, unitTypesPerLevel);
		}
		else
		{
			return unitFormationHelper(Level.ZERO, unitTypesPerLevel);
		}
	}
	
	/*
	 * Commands the Units stored in an ArrayList to attack a location on the map
	 * 
	 * unitFormation:   ArrayList of Units we wanted to rally
	 * pixelPositionX:  Rally position's X
	 * pixelPositionY:  Rally position's Y
	 * 
	 */
	public void rallyUnits(ArrayList<Unit> unitFormation, int pixelPositionX, int pixelPositionY )
	{
		for(int index = 0; index < unitFormation.size(); index++)
		{
			bwapi.move(unitFormation.get(index).getID(), pixelPositionX, pixelPositionY);
		}
	}
	
	/*
	 * Rally the Units stored in an ArrayList to a location on the map
	 * 
	 * unitFormation:   ArrayList of Units we wanted to rally
	 * pixelPositionX:  Rally position's X
	 * pixelPositionY:  Rally position's Y
	 * 
	 */
	public void attackEnemyLocation(ArrayList<Unit> unitFormation, int pixelPositionX, int pixelPositionY )
	{
		for(int index = 0; index < unitFormation.size(); index++)
		{
			bwapi.attack(unitFormation.get(index).getID(), pixelPositionX, pixelPositionY);
		}
	}
	
	/*
	 * Returns the current marines on the map (testing out Level.ZERO atm)
	 */
	int getCurrentUnitCount()
	{
		int unitsTotal = 0;
		
		for (Unit unit : bwapi.getMyUnits())
		{
			if(unit.getTypeID() == UnitTypes.Terran_Marine.ordinal())
			{
				unitsTotal++;
				marinesTotal++;
			}
		}
		return unitsTotal;
	}
	
	/*
	 * Gets the scout unit at the very start of the game
	 * 
	 * Returns the scout Unit, a Unit with ID of -1 if not
	 */
	Unit getScoutUnit()
	{		
		for (Unit unit : bwapi.getMyUnits())
		{
			// if this unit is a command center (Terran_SCV)
			if (unit.getTypeID() == UnitTypes.Terran_SCV.ordinal())
			{
				return new Unit(unit.getID());
			}
		}
		
		return new Unit(-1);
	}
	
	/*
	 * Gets all the possible the base locations on the map
	 * 
	 * Returns an ArrayList of those base locations other than our home base location
	 */
	ArrayList<BaseLocation> getEnemyBases()
	{
		ArrayList<BaseLocation> enemyBaseLocsArr = new ArrayList<BaseLocation>();
		
		for (BaseLocation b : bwapi.getMap().getBaseLocations()) 
		{
			if (b.isStartLocation() && (b.getX() != homePositionX) && (b.getY() != homePositionY)) 
			{
				enemyBaseLocsArr.add(b);
			}
		}
		return enemyBaseLocsArr;
	}
	
	/*
	 * Action performed to tell the Scout to scout the base locations
	 * 
	 * scoutUnitID:   a unit's ID
	 * enemyBaseLocs:  ArrayList of enemy's base locations
	 * 
	 */
	void scoutEnemyBases(Unit scoutUnitID, ArrayList<BaseLocation> enemyBaseLocs)
	{		
		for (int index = 0; index < enemyBaseLocs.size(); index++) 
		{
			bwapi.move(scoutUnitID.getID(), enemyBaseLocs.get(index).getX(), enemyBaseLocs.get(index).getY());
			
			 /* tried to get the scout to not do anything once it reaches the enemy base - not working atm for some reason */
			 if( ((scoutUnitID.getX() == enemyBaseLocs.get(index).getX()) 
					&& (scoutUnitID.getY() == enemyBaseLocs.get(index).getY())) 
					|| scoutUnitID.isAttacking() || scoutUnitID.isGatheringGas() 
					|| scoutUnitID.isGatheringMinerals() )
			{
				/* if it did do something, tell it to go back to home base */
				bwapi.move(scoutUnitID.getID(), homePositionX, homePositionY);
			} 
		}
	}
	
	/*
	 * The high-level function called to do the scouting
	 */
	void scoutOperation()
	{
		Unit scout = getScoutUnit();
		scoutEnemyBases(scout, getEnemyBases());
	}
	
	// Method called once every second.
	public void act() {
		
		// ============== YOUR CODE GOES HERE =======================

		// This method is called every 30th frame (approx. once a 
		// second). You can use other methods in this class, but the 
		// majority of your agent's behaviour will probably be here.
		
		
		// First, let's train workers at our Command Center.
		// Cycle over all my units,
		for (Unit unit : bwapi.getMyUnits()) {
			// if this unit is a command center (Terran_Command_Center)
			if (unit.getTypeID() == UnitTypes.Terran_Command_Center.ordinal()) {
				// if it's training queue is empty
				if (unit.getTrainingQueueSize() == 0) {
					// check if we have enough minerals and supply, and (if we do) train one worker (Terran_SCV)
					if ((bwapi.getSelf().getMinerals() >= 50) && (bwapi.getSelf().getSupplyTotal()-bwapi.getSelf().getSupplyUsed() >= 2)) 
						bwapi.train(unit.getID(), UnitTypes.Terran_SCV.ordinal());
				}
			}
			
			if (unit.getTypeID() == UnitTypes.Terran_Barracks.ordinal()) {
				// if it's training queue is empty
				if (unit.getTrainingQueueSize() == 0) {
					// check if we have enough minerals and supply, and (if we do) train one worker (Terran_SCV)
					if ((bwapi.getSelf().getMinerals() >= 50) && (bwapi.getSelf().getSupplyTotal()-bwapi.getSelf().getSupplyUsed() >= 2)) 
						bwapi.train(unit.getID(), UnitTypes.Terran_Marine.ordinal());
				}
			}
		} 
		
		// Now let's mine minerals with your idle workers.
		// Cycle over all my units,
		for (Unit unit : bwapi.getMyUnits()) {
			// if this unit is Terran_SCV (worker),
			if (unit.getTypeID() == UnitTypes.Terran_SCV.ordinal()) {
				// and if it is idle (not doing anything),
				if (unit.isIdle()) {
					// then find the closest mineral patch (if we see any)
					int closestId = -1;
					double closestDist = 99999999;
					for (Unit neu : bwapi.getNeutralUnits()) {
						if (neu.getTypeID() == UnitTypes.Resource_Mineral_Field.ordinal()) {
							double distance = Math.sqrt(Math.pow(neu.getX() - unit.getX(), 2) + Math.pow(neu.getY() - unit.getY(), 2));
							if ((closestId == -1) || (distance < closestDist)) {
								closestDist = distance;
								closestId = neu.getID();
							}
						}
					}
					// and (if we found it) send this worker to gather it.
					if (closestId != -1) bwapi.rightClick(unit.getID(), closestId);
				}
			}
		}
		
		// And let's build some Supply Depots if we are low on supply (if free supply is less than 3).
		if (((bwapi.getSelf().getSupplyTotal() - bwapi.getSelf().getSupplyUsed())/2) < 3) {
			// Check if we have enough minerals,
			if (bwapi.getSelf().getMinerals() >= 100) {
				// try to find the worker near our home position
				int worker = getNearestUnit(UnitTypes.Terran_SCV.ordinal(), homePositionX, homePositionY);
				if (worker != -1) {
					// if we found him, try to select appropriate build tile position for supply depot (near our home base)
					Point buildTile = getBuildTile(worker, UnitTypes.Terran_Supply_Depot.ordinal(), homePositionX, homePositionY);
					Point buildBarrackTile = getBuildTile(worker, UnitTypes.Terran_Barracks.ordinal(), homePositionX, homePositionY);
					// if we found a good build position, and we aren't already constructing a Supply Depot, 
					// order our worker to build it
					if ((buildTile.x != -1) && (!weAreBuilding(UnitTypes.Terran_Supply_Depot.ordinal()))) {
						bwapi.build(worker, buildTile.x, buildTile.y, UnitTypes.Terran_Supply_Depot.ordinal());
					}
					
					if ((buildTile.x != -1) && (!weAreBuilding(UnitTypes.Terran_Barracks.ordinal())))
					{
						bwapi.build(worker, buildBarrackTile.x, buildBarrackTile.y, UnitTypes.Terran_Barracks.ordinal());
					}
				}
			}
		}
		
		if((getCurrentUnitCount() == 12) && (marinesTotal == 12))
		{
			rallyUnits(unitFormation(Level.ZERO, unitTypesPerLevel), homePositionX, homePositionY);
			marinesTotal = 0;
		}
		
		// ==========================================================
	}
	
	
	// Method called on every frame (approximately 30x every second).
	public void gameUpdate() {
		
		// Remember our homeTilePosition at the first frame
		if (bwapi.getFrameCount() == 1) {
			int cc = getNearestUnit(UnitTypes.Terran_Command_Center.ordinal(), 0, 0);
			if (cc == -1) cc = getNearestUnit(UnitTypes.Zerg_Hatchery.ordinal(), 0, 0);
			if (cc == -1) cc = getNearestUnit(UnitTypes.Protoss_Nexus.ordinal(), 0, 0);
			homePositionX = bwapi.getUnit(cc).getX();
			homePositionY = bwapi.getUnit(cc).getY();
			scoutOperation();
		}
		
		// Draw debug information on screen
		drawDebugInfo();
		
		

		// Call the act() method every 30 frames
		if (bwapi.getFrameCount() % 30 == 0) {
			act();
		}
		
		
	}

	// Some additional event-related methods.
	public void gameEnded() {}
	public void matchEnded(boolean winner) {}
	public void nukeDetect(int x, int y) {}
	public void nukeDetect() {}
	public void playerLeft(int id) {}
	public void unitCreate(int unitID) {}
	public void unitDestroy(int unitID) {}
	public void unitDiscover(int unitID) {}
	public void unitEvade(int unitID) {}
	public void unitHide(int unitID) {}
	public void unitMorph(int unitID) {}
	public void unitShow(int unitID) {}
	public void keyPressed(int keyCode) {}
	

    // Returns the id of a unit of a given type, that is closest to a pixel position (x,y), or -1 if we
    // don't have a unit of this type
    public int getNearestUnit(int unitTypeID, int x, int y) {
    	int nearestID = -1;
	    double nearestDist = 9999999;
	    for (Unit unit : bwapi.getMyUnits()) {
	    	if ((unit.getTypeID() != unitTypeID) || (!unit.isCompleted())) continue;
	    	double dist = Math.sqrt(Math.pow(unit.getX() - x, 2) + Math.pow(unit.getY() - y, 2));
	    	if (nearestID == -1 || dist < nearestDist) {
	    		nearestID = unit.getID();
	    		nearestDist = dist;
	    	}
	    }
	    return nearestID;
    }	
	
	// Returns the Point object representing the suitable build tile position
	// for a given building type near specified pixel position (or Point(-1,-1) if not found)
	// (builderID should be our worker)
	public Point getBuildTile(int builderID, int buildingTypeID, int x, int y) {
		Point ret = new Point(-1, -1);
		int maxDist = 3;
		int stopDist = 40;
		int tileX = x/32; int tileY = y/32;
		
		// Refinery, Assimilator, Extractor
		if (bwapi.getUnitType(buildingTypeID).isRefinery()) {
			for (Unit n : bwapi.getNeutralUnits()) {
				if ((n.getTypeID() == UnitTypes.Resource_Vespene_Geyser.ordinal()) && 
						( Math.abs(n.getTileX()-tileX) < stopDist ) &&
						( Math.abs(n.getTileY()-tileY) < stopDist )
						) return new Point(n.getTileX(),n.getTileY());
			}
		}
		
		while ((maxDist < stopDist) && (ret.x == -1)) {
			for (int i=tileX-maxDist; i<=tileX+maxDist; i++) {
				for (int j=tileY-maxDist; j<=tileY+maxDist; j++) {
					if (bwapi.canBuildHere(builderID, i, j, buildingTypeID, false)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : bwapi.getAllUnits()) {
							if (u.getID() == builderID) continue;
							if ((Math.abs(u.getTileX()-i) < 4) && (Math.abs(u.getTileY()-j) < 4)) unitsInWay = true;
						}
						if (!unitsInWay) {
							ret.x = i; ret.y = j;
							return ret;
						}
						// creep for Zerg (this may not be needed - not tested yet)
						if (bwapi.getUnitType(buildingTypeID).isRequiresCreep()) {
							boolean creepMissing = false;
							for (int k=i; k<=i+bwapi.getUnitType(buildingTypeID).getTileWidth(); k++) {
								for (int l=j; l<=j+bwapi.getUnitType(buildingTypeID).getTileHeight(); l++) {
									if (!bwapi.hasCreep(k, l)) creepMissing = true;
									break;
								}
							}
							if (creepMissing) continue; 
						}
						// psi power for Protoss (this seems to work out of the box)
						if (bwapi.getUnitType(buildingTypeID).isRequiresPsi()) {}
					}
				}
			}
			maxDist += 2;
		}
		
		if (ret.x == -1) bwapi.printText("Unable to find suitable build position for "+bwapi.getUnitType(buildingTypeID).getName());
		return ret;
	}
	
	// Returns true if we are currently constructing the building of a given type.
	public boolean weAreBuilding(int buildingTypeID) {
		for (Unit unit : bwapi.getMyUnits()) {
			if ((unit.getTypeID() == buildingTypeID) && (!unit.isCompleted())) return true;
			if (bwapi.getUnitType(unit.getTypeID()).isWorker() && unit.getConstructingTypeID() == buildingTypeID) return true;
		}
		return false;
	}
	
	// Draws debug information on the screen. 
	// Reimplement this function however you want. 
	public void drawDebugInfo() {
		
		int spacing = 10;
		
		// Draw our home position.
		bwapi.drawText(new Point(5,0), "Our home position: "+String.valueOf(homePositionX)+","+String.valueOf(homePositionY), true);
		for (BaseLocation b : bwapi.getMap().getBaseLocations()) {
			if (b.isStartLocation()) {
				int index  = spacing/10;
				bwapi.drawText(new Point(5,spacing), "Base position " + index + ": " +String.valueOf(b.getX())+","+String.valueOf(b.getY()), true);
			}
			spacing= spacing + 10;
		}
		
		
		//int scv1 = getScoutUnit().getID();
		//bwapi.drawText(new Point(5, 120), "ScoutID1: " + String.valueOf(scv1), true);
		
		/* int sp = 10;
		for (Unit unit : bwapi.getMyUnits()) 
		{
			
			if (unit.getTypeID() == UnitTypes.Terran_SCV.ordinal())
			{
				bwapi.drawText(new Point(5, 110+sp), "ScoutID: " + String.valueOf(unit.getID()), true);
				sp+=10;
			}
		} */
		
		
		// Draw circles over workers (blue if they're gathering minerals, green if gas, yellow if they're constructing).
		for (Unit u : bwapi.getMyUnits())  {
			if (u.isGatheringMinerals()) bwapi.drawCircle(u.getX(), u.getY(), 12, BWColor.BLUE, false, false);
			else if (u.isGatheringGas()) bwapi.drawCircle(u.getX(), u.getY(), 12, BWColor.GREEN, false, false);
		} 
		
	}
	
}
