package edu.cbohan3;

import java.util.ArrayList;
import java.util.List;

import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;

public class SimpleWorldStateModel implements FullStateModel {
	protected double [][] transitionProbs;
	
	public SimpleWorldStateModel() {
		this.transitionProbs = new double[4][4];
		for (int i = 0; i < 4; i++)
			for (int j = 0; j < 4; j++)
				this.transitionProbs[i][j] = (i == j) ? 1 : 0; //Fully deterministic world.
	} 
	
	protected int actionDir(Action a) {
		int adir = -1;
		if (a.actionName().equals(SimpleWorld.ACTION_NORTH)) {
			adir = 0;
		} else if (a.actionName().equals(SimpleWorld.ACTION_EAST)) {
			adir = 1;
		} else if (a.actionName().equals(SimpleWorld.ACTION_SOUTH)) {
			adir = 2;
		} else if (a.actionName().equals(SimpleWorld.ACTION_WEST)) {
			adir = 3;
		}
		
		return adir;
	}
	
	protected int[] moveResult(int curX, int curY, int direction, int key1InInventory, int key2InInventory,
	int door1Open, int door2Open) {
		int nextX = curX, nextY = curY;
		
		if (direction == 0) 
			nextY--;
		else if (direction == 1)
			nextX++;
		else if (direction == 2)
			nextY++;
		else if (direction == 3)
			nextX--;
		
		//System.out.println("X: " + curX + ", Y: " + curY + ", Key1: " + key1InInventory + ", Key2: " + key2InInventory + ", Door1: " + door1Open + ", Door2: " + door2Open);
		//System.out.println(hasKey(key1InInventory, key2InInventory));
		
		//Walk into wall.
		if (SimpleWorld.map[nextY][nextX] == 1) 
			return new int[] { curX, curY, key1InInventory, key2InInventory, door1Open, door2Open };
		
		//Walk into door with no key and door is closed.
		if (SimpleWorld.map[nextY][nextX] == SimpleWorld.D1 && door1Open == 0 && (hasKey(key1InInventory, key2InInventory) == false))
			return new int[] { curX, curY, key1InInventory, key2InInventory, door1Open, door2Open };	
		if (SimpleWorld.map[nextY][nextX] == SimpleWorld.D2 && door2Open == 0 && (hasKey(key1InInventory, key2InInventory) == false))
			return new int[] { curX, curY, key1InInventory, key2InInventory, door1Open, door2Open };	
		
		//Walk into door with key.
		if (SimpleWorld.map[nextY][nextX] == SimpleWorld.D1 && door1Open == 0 && key1InInventory == 1)
			return new int[] { nextX, nextY, -1, key2InInventory, 1, door2Open };
		else if (SimpleWorld.map[nextY][nextX] == SimpleWorld.D1 && door1Open == 0 && key2InInventory == 1)
			return new int[] { nextX, nextY, key1InInventory, -1, 1, door2Open };
		if (SimpleWorld.map[nextY][nextX] == SimpleWorld.D2  && door2Open == 0&& key1InInventory == 1)
			return new int[] { nextX, nextY, -1, key2InInventory, door1Open, 1 };
		else if (SimpleWorld.map[nextY][nextX] == SimpleWorld.D2 && door2Open == 0 && key2InInventory == 1) 
			return new int[] { nextX, nextY, key1InInventory, -1, door1Open, 1 };
		
		//Walk into key.
		if (SimpleWorld.map[nextY][nextX] == SimpleWorld.K1) {
			if (key1InInventory == 0)
				return new int[] { nextX, nextY, 1, key2InInventory, door1Open, door2Open };
			else
				return new int[] { nextX, nextY, key1InInventory, key2InInventory, door1Open, door2Open };
		} else if (SimpleWorld.map[nextY][nextX] == SimpleWorld.K2) {
			if (key2InInventory == 0)
				return new int[] { nextX, nextY, key1InInventory, 1, door1Open, door2Open };
			else
				return new int[] { nextX, nextY, key1InInventory, key2InInventory, door1Open, door2Open };
		}
		
		//Just walking on space with nothing on it.
		return new int[] { nextX, nextY, key1InInventory, key2InInventory, door1Open, door2Open };
	}
	
	private boolean hasKey(int key1, int key2) {
		return (key1 == 1) || (key2 == 1);
	}
	
	public State sample(State s, Action a) {
		s = s.copy();
		SimpleWorldState sws = (SimpleWorldState)s;
		
		int adir = actionDir(a);
		
		//At least initially, I'm not using probabilistic movement so this is not needed. But it's better
		//to need it and not have it than to have it and not need it.
		double r = Math.random();
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
		int[] newState = this.moveResult(sws.x, sws.y, dir, sws.key1InInventory, sws.key2InInventory,
				sws.door1Open, sws.door2Open);
		
		sws.x = newState[0];
		sws.y = newState[1];
		sws.key1InInventory = newState[2];
		sws.key2InInventory = newState[3];
		sws.door1Open = newState[4];
		sws.door2Open = newState[5];
		
		//Return the state we just modified.
		return sws;
	}

	public List<StateTransitionProb> stateTransitions(State s, Action a) {
		//Get the current state.
		SimpleWorldState sws = (SimpleWorldState)s;
		
		int adir = actionDir(a);
		
		List<StateTransitionProb> tps = new ArrayList<StateTransitionProb>(4);
		StateTransitionProb noChange = null;
		for (int dir = 0; dir < 4; dir++) {
			int[] newState = this.moveResult(sws.x, sws.y, dir, sws.key1InInventory, sws.key2InInventory,
											 sws.door1Open, sws.door2Open);
			
			if (newState[0] != sws.x || newState[1] != sws.y || newState[2] != sws.key1InInventory ||
			newState[3] != sws.key2InInventory || newState[4] != sws.door1Open || 
			newState[5] != sws.door2Open) {
				//New possible outcome.
				SimpleWorldState newSws = sws.copy();
				newSws.x = newState[0];
				newSws.y = newState[1];
				newSws.key1InInventory = newState[2];
				newSws.key2InInventory = newState[3];
				newSws.door1Open = newState[4];
				newSws.door2Open = newState[5];
				
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
