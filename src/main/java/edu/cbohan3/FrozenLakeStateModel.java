package edu.cbohan3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;

public class FrozenLakeStateModel implements FullStateModel {
	protected double [][] transitionProbs;
	protected double successChance;
	protected Random rand;
	
	public FrozenLakeStateModel(double successChance) {
		this.successChance = successChance;
		this.transitionProbs = new double[4][4];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				this.transitionProbs[i][j] = (i == j) ? successChance : ((1. - successChance) / 3.); 
		rand = new Random(System.currentTimeMillis());
	} 
	
	protected int actionDir(Action a) {
		int adir = -1;
		if (a.actionName().equals(FrozenLake.ACTION_NORTH)) {
			adir = 0;
		} else if (a.actionName().equals(FrozenLake.ACTION_EAST)) {
			adir = 1;
		} else if (a.actionName().equals(FrozenLake.ACTION_SOUTH)) {
			adir = 2;
		} else if (a.actionName().equals(FrozenLake.ACTION_WEST)) {
			adir = 3;
		}
		
		return adir;
	}
	
	protected int[] moveResult(int curX, int curY, int direction) {
		int nextX = curX, nextY = curY;
		
		if (direction == 0) 
			nextY--;
		else if (direction == 1)
			nextX++;
		else if (direction == 2)
			nextY++;
		else if (direction == 3)
			nextX--;
		
		//Walk into wall.
		if (FrozenLake.map[nextY][nextX] == 1) 
			return new int[] { curX, curY };	
		
		//Just walking on space with nothing on it.
		return new int[] { nextX, nextY };
	}
	
	
	public State sample(State s, Action a) {
		s = s.copy();
		FrozenLakeState sws = (FrozenLakeState)s;
		
		int adir = actionDir(a);
		
		double r = rand.nextDouble();
		double sumProb = 0.;
		int dir	= 0;
		for (int i = 0; i < 4; i++) {
			sumProb += this.transitionProbs[adir][i];
			if (r < sumProb) {
				dir = i;
				break;
			}
		}
		
		//Update the state after the action.
		int[] newState = this.moveResult(sws.x, sws.y, dir);
		
		sws.x = newState[0];
		sws.y = newState[1];
		
		//Return the state we just modified.
		return sws;
	}

	public List<StateTransitionProb> stateTransitions(State s, Action a) {
		//Get the current state.
		FrozenLakeState sws = (FrozenLakeState)s;
		
		int adir = actionDir(a);
		
		List<StateTransitionProb> tps = new ArrayList<StateTransitionProb>(4);
		StateTransitionProb noChange = null;
		for (int dir = 0; dir < 4; dir++) {
			int[] newState = this.moveResult(sws.x, sws.y, dir);
			
			if (newState[0] != sws.x || newState[1] != sws.y) {
				//New possible outcome.
				FrozenLakeState newSws = sws.copy();
				newSws.x = newState[0];
				newSws.y = newState[1];
				
				//Create transition probability object and add it to our list of possible outcomes.
				tps.add(new StateTransitionProb(newSws, this.transitionProbs[adir][dir]));
			} else {
				//The action we tried to perform didn't change the state. Add the probability to the
				//noChange probability.
				if (noChange != null)
					noChange.p += this.transitionProbs[adir][dir];
				else {
					noChange = new StateTransitionProb(s.copy(), this.transitionProbs[adir][dir]);
					tps.add(noChange);
				}
			}
		}
		
		return tps;
	}
	
}
