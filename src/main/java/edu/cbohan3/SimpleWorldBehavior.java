package edu.cbohan3;

import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner.PlanningFailedException;
import burlap.behavior.singleagent.planning.deterministic.informed.Heuristic;
import burlap.behavior.singleagent.planning.deterministic.informed.astar.AStar;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
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
		tf = new SimpleWorldTerminalFunction(simpleWorld.goalX, simpleWorld.goalY);
		rf = new SimpleWorldRewardFunction(simpleWorld.goalX, simpleWorld.goalY);
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
		Planner planner = new ValueIteration(domain, 0.99, hashingFactory, 0.001, 100);
		Policy p = planner.planFromState(initialState);
		PolicyUtils.rollout(p, initialState, domain.getModel()).write(outputPath + "vi");
	}
	
	public void doAStar(String outputPath) {
		try {
			Heuristic distanceHeuristic = new Heuristic() {
				public double h(State s) {
					SimpleWorldState sws = (SimpleWorldState)s;
					double distanceXGoal = Math.abs(simpleWorld.goalX - sws.x);
					double distanceYGoal = Math.abs(simpleWorld.goalY - sws.y);
					double distanceXKey1 = Math.abs(simpleWorld.key1X - sws.x);
					double distanceYKey1 = Math.abs(simpleWorld.key1Y - sws.y);
					double distanceXKey2 = Math.abs(simpleWorld.key2X - sws.x);
					double distanceYKey2 = Math.abs(simpleWorld.key2Y - sws.y);
					
					double heuristic;
					if (sws.key1InInventory == 0) {
						heuristic = -(distanceXKey1 + distanceYKey1 + 40);
					} else if (sws.key2InInventory == 0) {
						heuristic = -(distanceXKey2 + distanceYKey2 + 20);
					} else {
						heuristic = -(distanceXGoal + distanceYGoal);
					}
					
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
	
}
