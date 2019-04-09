package edu.cbohan3;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.StateUtilities;
import burlap.mdp.core.state.UnknownKeyException;

public class FrozenLakeState implements MutableState, ObjectInstance {
	
	public int x, y;
	public String name;
	
	private final List<Object> keys = Arrays.<Object>asList(FrozenLake.VAR_X, FrozenLake.VAR_Y);
	
	public FrozenLakeState() {}
	public FrozenLakeState(int x, int y) {
		this.x = x;
		this.y = y;
		name = "agent";
	}

	public FrozenLakeState copy() {
		return new FrozenLakeState(this.x, this.y);
	}

	public Object get(Object variableKey) {
		if (variableKey.equals(FrozenLake.VAR_X)) {
			return this.x;
		} else if (variableKey.equals(FrozenLake.VAR_Y)) {
			return this.y;
		}
		
		throw new UnknownKeyException(variableKey);
	}
	
	public List<Object> variableKeys() {
		return this.keys;
	}
	
	public MutableState set(Object variableKey, Object value) {
		if (variableKey.equals(FrozenLake.VAR_X)) {
			this.x = StateUtilities.stringOrNumber(value).intValue();
		} else if (variableKey.equals(FrozenLake.VAR_Y)) {
			this.y = StateUtilities.stringOrNumber(value).intValue();
		} else {
			throw new UnknownKeyException(variableKey);
		}
		
		return this;
	}
	
	public String toString() {
		return StateUtilities.stateToString(this);
	}
	public String className() {
		return FrozenLake.CLASS_AGENT;
	}
	public ObjectInstance copyWithName(String objectName) {
		FrozenLakeState state = this.copy();
		state.name = objectName;
		return state;
	}
	public String name() {
		return null;
	}
}
