package javabot.AIStarCraft;

/**
 * Example of a Java AI Client that does nothing.
 */
import java.awt.Point;
import javabot.model.*;
import javabot.types.*;
import javabot.*;
import javabot.types.OrderType.OrderTypeTypes;
import javabot.types.UnitType.UnitTypes;
import javabot.util.BWColor;

public class RaynorsRaiders implements BWAPIEventListener {

	int homePositionX, homePositionY, mainMins;
	
	JNIBWAPI bwapi;
	/* Name AIs here */
	core_reactive ai_core;
	build_manager ai_builder;
	
	public static void main(String[] args) {
		new RaynorsRaiders();
	}
	
	public RaynorsRaiders() {
		bwapi = new JNIBWAPI(this);
		bwapi.start();
	} 
	
	
	public void connected() {
		bwapi.loadTypeData();
	}	
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
