package edu.cbohan3;

import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;

public class SimpleWorldBehavior {
	static SimpleWorld simpleWorld;
	static SADomain simpleWorldDomain;
	static OOSADomain domain;
	static RewardFunction rf;
	static TerminalFunction tf;
	static StateConditionTest goalCondition;
	static State initialState;
	static HashableStateFactory hashingFactory;
	static SimulatedEnvironment env;
	
	public static void run() {
		simpleWorld = new SimpleWorld();
		tf = new SimpleWorldTerminalFunction(simpleWorld.goalX, simpleWorld.goalY);
		goalCondition = new TFGoalCondition(tf);
		simpleWorldDomain = simpleWorld.generateDomain();
		initialState = simpleWorld.getInitialState();
		hashingFactory = new SimpleHashableStateFactory();
		env = new SimulatedEnvironment(simpleWorldDomain, initialState);
		
	}
	
	public void visualize(String outputPath) {
		Visualizer v = simpleWorld.getVisualizer();
		new EpisodeSequenceVisualizer(v, simpleWorldDomain, outputPath);
	}
}
