package edu.cbohan3;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.visualizer.StateRenderLayer;
import burlap.visualizer.Visualizer;

public class FrozenLake implements DomainGenerator {
	public static final double SUCCESS_CHANCE = .7;
	
	public static final String VAR_X = "x";
	public static final String VAR_Y = "y";

	public static final String ACTION_NORTH = "north";
	public static final String ACTION_SOUTH = "south";
	public static final String ACTION_EAST = "east";
	public static final String ACTION_WEST = "west";
	
	public static final String CLASS_AGENT = "agent";
	
	public static final int oo = 0; //Open ground
	public static final int XX = 1; //Wall
	public static final int HH = 2; //Hole
	public static int[][] map = new int[][] {
		{XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX},
		{XX,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,XX},
		{XX,HH,oo,HH,oo,oo,HH,oo,oo,oo,HH,XX},
		{XX,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,XX},
		{XX,HH,oo,HH,oo,oo,HH,oo,oo,oo,HH,XX},
		{XX,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,XX},
		{XX,oo,oo,oo,oo,oo,oo,oo,oo,oo,oo,XX},
		{XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX,XX},
	};
	
	static int goalX = 1;
	static int goalY = 1;
	static int startX = 1;
	static int startY = 10;

	public OOSADomain generateDomain() {
		OOSADomain domain = new OOSADomain();
		
		domain.addStateClass(CLASS_AGENT, FrozenLakeState.class);
		
		FrozenLakeStateModel sModel = new FrozenLakeStateModel(SUCCESS_CHANCE);
		RewardFunction rf = new FrozenLakeRewardFunction(FrozenLake.goalX, FrozenLake.goalY);
		TerminalFunction tf = new FrozenLakeTerminalFunction(FrozenLake.goalX, FrozenLake.goalY);
		
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
		FrozenLake.goalX = goalX;
		FrozenLake.goalY = goalY;
	}
	
	public StateRenderLayer getStateRenderLayer() {
		StateRenderLayer rl = new StateRenderLayer();
		rl.addStatePainter(new FrozenLakeVisualizer());
		return rl;
	}
	
	public Visualizer getVisualizer() {
		return new Visualizer(this.getStateRenderLayer());
	}
	
	public FrozenLakeState getInitialState() {
		return new FrozenLakeState(startX, startY);
	}
}
