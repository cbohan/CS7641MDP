package edu.cbohan3;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.visualizer.StateRenderLayer;
import burlap.visualizer.Visualizer;

public class SimpleWorld implements DomainGenerator {
	public static final String VAR_X = "x";
	public static final String VAR_Y = "y";
	public static final String VAR_KEY1_IN_INVENTORY = "key1InInventory";
	public static final String VAR_KEY2_IN_INVENTORY = "key2InInventory";
	public static final String VAR_DOOR1_OPEN = "door1Open";
	public static final String VAR_DOOR2_OPEN = "door2Open";

	public static final String ACTION_NORTH = "north";
	public static final String ACTION_SOUTH = "south";
	public static final String ACTION_EAST = "east";
	public static final String ACTION_WEST = "west";
	
	public static final String CLASS_AGENT = "agent";
	
	public static final int K1 = 1001; //Key 1
	public static final int K2 = 1002; //Key 2
	public static final int D1 = 100001; //Door1
	public static final int D2 = 100002; //Door2
	public static final int oo = 0; //Open ground
	public static final int XX = 1; //Wall
	public static int[][] map = new int[][] {
		{XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,D2,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,oo,oo,oo,oo,oo,oo,XX},
		{XX,XX,XX,D1,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,K1,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,XX,XX,XX,XX,XX,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,XX,XX,XX,XX,XX,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,XX,K2,oo,oo,oo,oo,oo,XX},
		{XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX},
	};
	
	public int goalX = 13;
	public int goalY = 1;
	public int startX = 1;
	public int startY = 16;
	public int key1X = 8;
	public int key1Y = 9;
	public int key2X = 8;
	public int key2Y = 16;
	

	public OOSADomain generateDomain() {
		OOSADomain domain = new OOSADomain();
		
		domain.addStateClass(CLASS_AGENT, SimpleWorldState.class);
		
		SimpleWorldStateModel sModel = new SimpleWorldStateModel();
		RewardFunction rf = new SimpleWorldRewardFunction(this.goalX, this.goalY);
		TerminalFunction tf = new SimpleWorldTerminalFunction(this.goalX, this.goalY);
		
		FactoredModel model = new FactoredModel(sModel, rf, tf);
		domain.setModel(model);
		
		domain.addActionTypes(
				new UniversalActionType(ACTION_NORTH),
				new UniversalActionType(ACTION_EAST),
				new UniversalActionType(ACTION_SOUTH),
				new UniversalActionType(ACTION_WEST));
		
		return domain;
	}
	
	public void setGoalLocation(int goalX, int goalY) {
		this.goalX = goalX;
		this.goalY = goalY;
	}
	
	public StateRenderLayer getStateRenderLayer() {
		StateRenderLayer rl = new StateRenderLayer();
		rl.addStatePainter(new SimpleWorldVisualizer());
		return rl;
	}
	
	public Visualizer getVisualizer() {
		return new Visualizer(this.getStateRenderLayer());
	}
	
	public SimpleWorldState getInitialState() {
		return new SimpleWorldState(startX, startY, 0, 0, 0, 0);
	}
	
	public static int[] getObjectPosition(int object) {
		for (int y = 0; y < map.length; y++) 
			for (int x = 0; x < map[0].length; x++)
				if (map[y][x] == object)
					return new int[] { x, y };
		return null;
	}
}
