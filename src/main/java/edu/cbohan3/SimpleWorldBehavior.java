package edu.cbohan3;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.auxiliary.StateReachability;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.ValueFunctionVisualizerGUI;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.ArrowActionGlyph;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.LandmarkColorBlendInterpolation;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.PolicyGlyphPainter2D;
import burlap.behavior.singleagent.auxiliary.valuefunctionvis.common.StateValuePainter2D;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner.PlanningFailedException;
import burlap.behavior.singleagent.planning.deterministic.informed.Heuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.astar.AStar;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.behavior.singleagent.planning.stochastic.policyiteration.PolicyIteration;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.vardomain.VariableDomain;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;

public class SimpleWorldBehavior {
	public SimpleWorld simpleWorld;
	public OOSADomain domain;
	public RewardFunction rf;
	public TerminalFunction tf;
	public StateConditionTest goalCondition;
	public State initialState;
	public HashableStateFactory hashingFactory;
	public SimulatedEnvironment env;
	
	public SimpleWorldBehavior() {
		simpleWorld = new SimpleWorld();
		tf = new SimpleWorldTerminalFunction(SimpleWorld.goalX, SimpleWorld.goalY);
		rf = new SimpleWorldRewardFunction(SimpleWorld.goalX, SimpleWorld.goalY);
		goalCondition = new TFGoalCondition(tf);
		domain = simpleWorld.generateDomain();
		initialState = simpleWorld.getInitialState();
		hashingFactory = new SimpleHashableStateFactory();
		env = new SimulatedEnvironment(domain, initialState);
		
	}
	
	public void visualize(String outputPath) {
		Visualizer v = simpleWorld.getVisualizer();
		new EpisodeSequenceVisualizer(v, domain, outputPath);
	}

	public void doBFS(String outputPath) {
		try {
			DeterministicPlanner planner = new BFS(domain, goalCondition, hashingFactory);
			Policy p = planner.planFromState(initialState);
			PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "bfs");
		} catch (PlanningFailedException e) {
			System.err.println("Breadth first search failed to find solution for Simple World.");
		}
	}
	
	public void doValueIteration(String outputPath) {
		long startTime = System.nanoTime();
		
		Planner planner = new ValueIteration(domain, 0.99, hashingFactory, 0.001, 200);
		Policy p = planner.planFromState(initialState);
		PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "vi");
		valueFunctionVis((ValueFunction)planner, p);
		
		long endTime = System.nanoTime();
		double duration = (endTime - startTime) / 1000000000.;
		System.out.println("Value iteration took: " + duration + " seconds.");
	}
	
	public void doPolicyIteration(String outputPath) {
		Planner planner = new PolicyIteration(domain, 0.99, hashingFactory, 0.001, 100, 100);
		Policy p = planner.planFromState(initialState);
		PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "pi");
	}
	
	public void doAStar(String outputPath) {
		try {
			Heuristic distanceHeuristic = new Heuristic() {
				public double h(State s) {
					SimpleWorldState sws = (SimpleWorldState)s;
					double distanceXGoal = Math.abs(SimpleWorld.goalX - sws.x);
					double distanceYGoal = Math.abs(SimpleWorld.goalY - sws.y);
					
					double heuristic = -(distanceXGoal + distanceYGoal);					
					return heuristic;
				}
			};
			
			DeterministicPlanner planner = new AStar(domain, goalCondition, hashingFactory, distanceHeuristic);
			Policy p = planner.planFromState(initialState);
			PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "astar");
		} catch (PlanningFailedException e) {
			System.err.println("A* failed to find solution for Simple World.");
		}
	}
	
	public void valueFunctionVis(ValueFunction valueFunction, Policy p) {
		List<State> allStates = StateReachability.getReachableStates(initialState, domain, hashingFactory);
		
		Integer hasKey1 = 0;
		Integer hasKey2 = 1;
		List<State> someStates = new ArrayList<State>();
		for (State s : allStates) {
			if (s.get((String)SimpleWorld.VAR_KEY1_IN_INVENTORY) == hasKey1 &&
			s.get((String)SimpleWorld.VAR_KEY2_IN_INVENTORY) == hasKey2) {
				someStates.add(s);
			}
		}
		
		LandmarkColorBlendInterpolation rb = new LandmarkColorBlendInterpolation();
		rb.addNextLandMark(0., Color.RED);
		rb.addNextLandMark(1., Color.BLUE);
		
		StateValuePainter2D svp = new StateValuePainter2D(rb);
		svp.setXYKeys("x", "y", new VariableDomain(0, 15), new VariableDomain(0, 18), 1, 1);
	
		ValueFunctionVisualizerGUI gui = new ValueFunctionVisualizerGUI(someStates, svp, valueFunction);
		
		PolicyGlyphPainter2D spp = new PolicyGlyphPainter2D();
		spp.setXYKeys("x", "y", new VariableDomain(0, 15), new VariableDomain(0, 18), 1, 1);
		
		spp.setActionNameGlyphPainter(SimpleWorld.ACTION_NORTH, new ArrowActionGlyph(1));
		spp.setActionNameGlyphPainter(SimpleWorld.ACTION_SOUTH, new ArrowActionGlyph(0));
		spp.setActionNameGlyphPainter(SimpleWorld.ACTION_EAST, new ArrowActionGlyph(2));
		spp.setActionNameGlyphPainter(SimpleWorld.ACTION_WEST, new ArrowActionGlyph(3));
		spp.setRenderStyle(PolicyGlyphPainter2D.PolicyGlyphRenderStyle.DISTSCALED);
		
		gui.setSpp(spp);
		gui.setPolicy(p);
		
		gui.setBgColor(Color.GRAY);
		
		gui.initGUI();
	}
	
	public void experimentAndPlotter() {
		((FactoredModel)domain.getModel()).setRf(new GoalBasedRF(this.goalCondition, 1.0, -0.01));
	}
}
