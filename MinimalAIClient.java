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

        // input: upgrade(UpgradeTypes.UPGRADE.ordinal()) where UPGRADE is the upgrade type
        // method will do resource check before attempting upgrade
        // so far, will only upgrade at base price
	public void upgrade(int lvlup) {
		UpgradeType gear = bwapi.getUpgradeType(lvlup);
		
		if(bwapi.getSelf().getMinerals() >= gear.getMineralPriceBase() && bwapi.getSelf().getGas() >= gear.getGasPriceBase()) {
			for(Unit unit : bwapi.getMyUnits()) {
				if(unit.getTypeID() == gear.getWhatUpgradesTypeID()) {
					bwapi.upgrade(unit.getID(), gear.getID());
				}
			}
		}
	}

        // input: TechTypes.TECHNOLOGY.ordinal() where TECHNOLOGY is the tech to research
        // method will do resource check before attempting research
	public void research(int item) {
		TechType tech = bwapi.getTechType(item);
		
		if(bwapi.getSelf().getMinerals() >= tech.getMineralPrice() && bwapi.getSelf().getGas() >= tech.getGasPrice()) {
			for(Unit unit : bwapi.getMyUnits()) {
				if(unit.getTypeID() == tech.getWhatResearchesTypeID()) {
					bwapi.research(unit.getID(), tech.getID());
				}
			}
		}
	}
	
        // input: build(UnitTypes.BUILDING.ordinal()) where BUILDING is the building to build
        // will build near similar building types
        // will also build add-ons (not sure if structure will attempt relocating if there's not enough room)
	public void build(int structure) {
		UnitType bldg = bwapi.getUnitType(structure);
		
		if (bwapi.getSelf().getMinerals() >= bldg.getMineralPrice() && bwapi.getSelf().getGas() >= bldg.getGasPrice()) {
			if(bldg.isAddon()) {
				//find parent structure
				for(Unit unit : bwapi.getMyUnits()) {
					if(unit.getTypeID() == bldg.getWhatBuildID()) {
						bwapi.buildAddon(unit.getID(), bldg.getID());
						break;
					}
				}
				
			} else {
			// try to find the worker near our home position
				int worker = getNearestUnit(UnitTypes.Terran_SCV.ordinal(), homePositionX, homePositionY);
				if (worker != -1) {
				// if we found him, try to select appropriate build tile position for bldg (near our home base)
					int xtile = homePositionX, ytile = homePositionY;
				
					for(Unit unit : bwapi.getMyUnits()) {
						if(unit.getTypeID() == bldg.getID()) {
							xtile = unit.getX();
							ytile = unit.getY();
							break;
						}
					}
				    Point buildTile = getBuildTile(worker, bldg.getID(), xtile, ytile);
				    // if we found a good build position, and we aren't already constructing a bldg 
				    // order our worker to build it
				    if ((buildTile.x != -1) && (!weAreBuilding(bldg.getID()))) {
				    	bwapi.build(worker, buildTile.x, buildTile.y, bldg.getID());
				    }
				}
			}
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
