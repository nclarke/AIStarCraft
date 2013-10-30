package javabot.AIStarCraft;

import java.awt.Point;

import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;
import javabot.types.UpgradeType.UpgradeTypes;
import javabot.util.BWColor;
// Cannot import core reactive, primary and secondary constructors will init the AI core communication

public class build_manager {
	
	public build_manager() {
		//SET UP ALL INTERNAL VARIABLES HERE
	}
	
	public void AI_link_build_manager(JNIBWAPI d_bwapi, core_reactive d_core) {
		//Here you get your pointers to the other AI cores (JINBWAPI, core, ect ect ect)
		//The Raynons Raiders code should call this "constructor" after all the other AI parts have
		// been created.
		
	}
	
        // input: build(UnitTypes.BUILDING.ordinal()) where BUILDING is the building to build
        // will build near similar building types
        // will also build add-ons (not sure if structure will attempt relocating if there's not enough room)
	public void build(UnitTypes structure) {
		UnitType bldg = bwapi.getUnitType(structure.ordinal());
		
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
		else {
			// report insufficient resources
		}
	}

    // input: upgrade(UpgradeTypes.UPGRADE) where UPGRADE is the upgrade type
    // method will do resource check before attempting upgrade
    // so far, will only upgrade at base price
	public void upgrade(UpgradeTypes lvlup) {
		UpgradeType gear = bwapi.getUpgradeType(lvlup.ordinal());
	
		if(bwapi.getSelf().getMinerals() >= gear.getMineralPriceBase() && bwapi.getSelf().getGas() >= gear.getGasPriceBase()) {
			for(Unit unit : bwapi.getMyUnits()) {
				if(unit.getTypeID() == gear.getWhatUpgradesTypeID()) {
					bwapi.upgrade(unit.getID(), gear.getID());
				}
			}
		}
		else {
			// report insufficient resources
		}
	}

   // input: TechTypes.TECHNOLOGY where TECHNOLOGY is the tech to research
    // method will do resource check before attempting research
	public void research(TechTypes item) {
		TechType tech = bwapi.getTechType(item.ordinal());
	
		if(bwapi.getSelf().getMinerals() >= tech.getMineralPrice() && bwapi.getSelf().getGas() >= tech.getGasPrice()) {
			for(Unit unit : bwapi.getMyUnits()) {
				if(unit.getTypeID() == tech.getWhatResearchesTypeID()) {
					bwapi.research(unit.getID(), tech.getID());
				}
			}
		}
		else {
			// report insufficient resources
		}
	}
}
