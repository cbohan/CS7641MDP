package edu.cbohan3;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class FrozenLakeTerminalFunction implements TerminalFunction {
	int goalX;
	int goalY;
	
	public FrozenLakeTerminalFunction(int goalX, int goalY) {
		this.goalX = goalX;
		this.goalY = goalY;
	}
	
	public boolean isTerminal(State s) {
		int ax = (Integer)s.get(FrozenLake.VAR_X);
		int ay = (Integer)s.get(FrozenLake.VAR_Y);
		
		if (ax == this.goalX && ay == this.goalY) 
			return true;
		
		if (FrozenLake.map[ay][ax] == FrozenLake.HH)
			return true;
		
		return false;
	}
}
