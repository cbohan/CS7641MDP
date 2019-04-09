package edu.cbohan3;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.StateUtilities;
import burlap.mdp.core.state.UnknownKeyException;

public class DoorsAndKeysState implements MutableState, ObjectInstance {
	
	public int x, y;
	public int key1InInventory, key2InInventory;
	public int door1Open, door2Open;
	public String name;
	
	private final List<Object> keys = Arrays.<Object>asList(DoorsAndKeys.VAR_X, DoorsAndKeys.VAR_Y, DoorsAndKeys.VAR_KEY1_IN_INVENTORY, DoorsAndKeys.VAR_KEY2_IN_INVENTORY, DoorsAndKeys.VAR_DOOR1_OPEN, DoorsAndKeys.VAR_DOOR2_OPEN);
	
	public DoorsAndKeysState() {}
	public DoorsAndKeysState(int x, int y, int key1InInventory, int key2InInventory, int door1Open, int door2Open) {
		this.x = x;
		this.y = y;
		this.key1InInventory = key1InInventory;
		this.key2InInventory = key2InInventory;
		this.door1Open = door1Open;
		this.door2Open = door2Open;
		name = "agent";
	}

	public DoorsAndKeysState copy() {
		return new DoorsAndKeysState(this.x, this.y, this.key1InInventory, this.key2InInventory, this.door1Open, this.door2Open);
	}

	public Object get(Object variableKey) {
		if (variableKey.equals(DoorsAndKeys.VAR_X)) {
			return this.x;
		} else if (variableKey.equals(DoorsAndKeys.VAR_Y)) {
			return this.y;
		} else if (variableKey.equals(DoorsAndKeys.VAR_KEY1_IN_INVENTORY)) {
			return this.key1InInventory;
		} else if (variableKey.equals(DoorsAndKeys.VAR_KEY2_IN_INVENTORY)) {
			return this.key2InInventory;
		} else if (variableKey.equals(DoorsAndKeys.VAR_DOOR1_OPEN)) {
			return this.door1Open;
		} else if (variableKey.equals(DoorsAndKeys.VAR_DOOR2_OPEN)) {
			return this.door2Open;
		}
		
		throw new UnknownKeyException(variableKey);
	}
	
	public List<Object> variableKeys() {
		return this.keys;
	}
	
	public MutableState set(Object variableKey, Object value) {
		if (variableKey.equals(DoorsAndKeys.VAR_X)) {
			this.x = StateUtilities.stringOrNumber(value).intValue();
		} else if (variableKey.equals(DoorsAndKeys.VAR_Y)) {
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
		return DoorsAndKeys.CLASS_AGENT;
	}
	public ObjectInstance copyWithName(String objectName) {
		DoorsAndKeysState state = this.copy();
		state.name = objectName;
		return state;
	}
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}
}
