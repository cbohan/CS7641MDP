package edu.cbohan3;

import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.shell.visual.VisualExplorer;
import burlap.visualizer.Visualizer;

public class Main {
	public static void main(String[] args) {
		
		
	}
	
	private static void playSimpleWorld() {
		SimpleWorld simpleWorld = new SimpleWorld();
		SADomain domain = simpleWorld.generateDomain();
		State initialState = simpleWorld.getInitialState();
		SimulatedEnvironment env = new SimulatedEnvironment(domain, initialState);
		
		Visualizer v = simpleWorld.getVisualizer();
		VisualExplorer exp = new VisualExplorer(domain, env, v);
		
		exp.addKeyAction("w", SimpleWorld.ACTION_NORTH, "");
		exp.addKeyAction("d", SimpleWorld.ACTION_EAST, "");
		exp.addKeyAction("s", SimpleWorld.ACTION_SOUTH, "");
		exp.addKeyAction("a", SimpleWorld.ACTION_WEST, "");
		
		exp.initGUI();
	}
}
