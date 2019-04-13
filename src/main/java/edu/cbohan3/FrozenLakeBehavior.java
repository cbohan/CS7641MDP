package edu.cbohan3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.auxiliary.StateReachability;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.ArrowActionGlyph;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.LandmarkColorBlendInterpolation;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.PolicyGlyphPainter2D;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.StateValuePainter2D;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.vardomain.VariableDomain;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;

public class FrozenLakeBehavior {
	public FrozenLake frozenLakeWorld;
	public OOSADomain domain;
	public RewardFunction rf;
	public TerminalFunction tf;
	public StateConditionTest goalCondition;
	public State initialState;
	public HashableStateFactory hashingFactory;
	public SimulatedEnvironment env;
	
	public FrozenLakeBehavior() {
		frozenLakeWorld = new FrozenLake();
		tf = new FrozenLakeTerminalFunction(FrozenLake.goalX, FrozenLake.goalY);
		rf = new FrozenLakeRewardFunction(FrozenLake.goalX, FrozenLake.goalY);
		goalCondition = new TFGoalCondition(tf);
		domain = frozenLakeWorld.generateDomain();
		initialState = frozenLakeWorld.getInitialState();
		hashingFactory = new SimpleHashableStateFactory();
		env = new SimulatedEnvironment(domain, initialState);
		
	}
	
	public void visualize(String outputPath) {
		Visualizer v = frozenLakeWorld.getVisualizer();
		new EpisodeSequenceVisualizer(v, domain, outputPath);
	}
	
	public void doValueIteration(String outputPath) {
		long startTime = System.nanoTime();
		
		Planner planner = new ValueIteration(domain, 0.99, hashingFactory, 0.001, 200);
		Policy p = planner.planFromState(initialState);
		PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "frozen_lake_vi");
		valueFunctionVis((ValueFunction)planner, p);
		
		long endTime = System.nanoTime();
		double duration = (endTime - startTime) / 1000000000.;
		System.out.println("Value iteration took: " + duration + " seconds.");
	}
	
	public void doPolicyIteration(String outputPath) {
		long startTime = System.nanoTime();
		
		Planner planner = new PolicyIteration(domain, 0.99, hashingFactory, 0.001, 200, 200);
		Policy p = planner.planFromState(initialState);
		PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "frozen_lake_pi");
		valueFunctionVis((ValueFunction)planner, p);
		
		long endTime = System.nanoTime();
		double duration = (endTime - startTime) / 1000000000.;
		System.out.println("Value iteration took: " + duration + " seconds.");
	}
	
	public void doQLearning(String outputPath) {
		LearningAgent agent = new QLearning(domain, .99, hashingFactory, .5, .5);
		
		for (int i = 0; i <= 1000; i ++) {
			Episode e = agent.runLearningEpisode(env);
			
			if (i % 100 == 0) {
				e.write(outputPath + "frozen_lake_ql_" + i);
				System.out.println(i + ": " + e.maxTimeStep());
			}
			
			env.resetEnvironment();
		}
	}
	
	public void valueFunctionVis(ValueFunction valueFunction, Policy p) {
		List<State> allStates = StateReachability.getReachableStates(initialState, domain, hashingFactory);
		
		LandmarkColorBlendInterpolation rb = new LandmarkColorBlendInterpolation();
		rb.addNextLandMark(0., Color.RED);
		rb.addNextLandMark(1., Color.BLUE);
		
		StateValuePainter2D svp = new StateValuePainter2D(rb);
		svp.setXYKeys("x", "y", new VariableDomain(0, 15), new VariableDomain(0, 18), 1, 1);
				
		PolicyGlyphPainter2D spp = new PolicyGlyphPainter2D();
		spp.setXYKeys("x", "y", new VariableDomain(0, 15), new VariableDomain(0, 18), 1, 1);
		
		spp.setActionNameGlyphPainter(FrozenLake.ACTION_NORTH, new ArrowActionGlyph(1));
		spp.setActionNameGlyphPainter(FrozenLake.ACTION_SOUTH, new ArrowActionGlyph(0));
		spp.setActionNameGlyphPainter(FrozenLake.ACTION_EAST, new ArrowActionGlyph(2));
		spp.setActionNameGlyphPainter(FrozenLake.ACTION_WEST, new ArrowActionGlyph(3));
		spp.setRenderStyle(PolicyGlyphPainter2D.PolicyGlyphRenderStyle.DISTSCALED);
		
		if (valueFunction != null) {
			ValueFunctionVisualizerGUI gui = new ValueFunctionVisualizerGUI(allStates, svp, valueFunction);
			gui.setSpp(spp);
			gui.setPolicy(p);
			gui.setBgColor(Color.GRAY);
			gui.initGUI();
		}
	}
}
