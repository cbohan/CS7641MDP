package edu.cbohan3;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class DoorsAndKeysTerminalFunction implements TerminalFunction {
	int goalX;
	int goalY;
	
	public DoorsAndKeysTerminalFunction(int goalX, int goalY) {
		this.goalX = goalX;
		this.goalY = goalY;
	}
	
	public boolean isTerminal(State s) {
		int ax = (Integer)s.get(DoorsAndKeys.VAR_X);
		int ay = (Integer)s.get(DoorsAndKeys.VAR_Y);
		
		if (ax == this.goalX && ay == this.goalY) 
			return true;
		
		return false;
	}
}
