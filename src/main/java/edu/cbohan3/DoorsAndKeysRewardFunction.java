package edu.cbohan3;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

public class DoorsAndKeysRewardFunction implements RewardFunction {
	int goalX;
	int goalY;
	
	public DoorsAndKeysRewardFunction(int goalX, int goalY) {
		this.goalX = goalX;
		this.goalY = goalY;
	}
	
	public double reward(State s, Action a, State sprime) {
		int ax = (Integer)sprime.get(DoorsAndKeys.VAR_X);
		int ay = (Integer)sprime.get(DoorsAndKeys.VAR_Y);
		
		if (ax == this.goalX && ay == this.goalY) 
			return 1.;
		
		return -.01;
	}
}
