package edu.cbohan3;

import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.common.VisualActionObserver;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.shell.visual.VisualExplorer;
import burlap.visualizer.Visualizer;

public class Main {
	public static void main(String[] args) {
		//playDoorsAndKeys();
		playFrozenLake();
		
		//DoorsAndKeysBehavior behavior = new DoorsAndKeysBehavior();
		String outputPath = "output/";
		
		/*VisualActionObserver observer = new VisualActionObserver(behavior.simpleWorld.getVisualizer());
		observer.initGUI();
		behavior.env.addObservers(observer);*/
		
		//behavior.doBFS(outputPath);
		//behavior.doAStar(outputPath);
		//behavior.doValueIteration(outputPath);
		//behavior.doPolicyIteration(outputPath);
		//behavior.visualize(outputPath);
		
	}
	
	private static void playDoorsAndKeys() {
		DoorsAndKeys doorsAndKeysWorld = new DoorsAndKeys();
		OOSADomain domain = doorsAndKeysWorld.generateDomain();
		State initialState = doorsAndKeysWorld.getInitialState();
		SimulatedEnvironment env = new SimulatedEnvironment(domain, initialState);
		
		Visualizer v = doorsAndKeysWorld.getVisualizer();
		VisualExplorer exp = new VisualExplorer(domain, env, v);
		
		exp.addKeyAction("w", DoorsAndKeys.ACTION_NORTH, "");
		exp.addKeyAction("d", DoorsAndKeys.ACTION_EAST, "");
		exp.addKeyAction("s", DoorsAndKeys.ACTION_SOUTH, "");
		exp.addKeyAction("a", DoorsAndKeys.ACTION_WEST, "");
		
		exp.initGUI();
	}
	
	private static void playFrozenLake() {
		FrozenLake frozenLakeWorld = new FrozenLake();
		OOSADomain domain = frozenLakeWorld.generateDomain();
		State initialState = frozenLakeWorld.getInitialState();
		SimulatedEnvironment env = new SimulatedEnvironment(domain, initialState);
		
		Visualizer v = frozenLakeWorld.getVisualizer();
		VisualExplorer exp = new VisualExplorer(domain, env, v);
		
		exp.addKeyAction("w", FrozenLake.ACTION_NORTH, "");
		exp.addKeyAction("d", FrozenLake.ACTION_EAST, "");
		exp.addKeyAction("s", FrozenLake.ACTION_SOUTH, "");
		exp.addKeyAction("a", FrozenLake.ACTION_WEST, "");
		
		exp.initGUI();
	}
}
