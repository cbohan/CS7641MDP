package edu.cbohan3;

import burlap.behavior.singleagent.auxiliary.performance.LearningAlgorithmExperimenter;
import burlap.behavior.singleagent.auxiliary.performance.PerformanceMetric;
import burlap.behavior.singleagent.auxiliary.performance.TrialMode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.LearningAgentFactory;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.domain.singleagent.gridworld.GridWorldDomain;
import burlap.domain.singleagent.gridworld.state.GridAgent;
import burlap.domain.singleagent.gridworld.state.GridLocation;
import burlap.domain.singleagent.gridworld.state.GridWorldState;
import burlap.mdp.auxiliary.common.ConstantStateGenerator;
import burlap.mdp.auxiliary.common.SinglePFTF;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class PlotTest {

	public static void main(String[] args) {

		GridWorldDomain gw = new GridWorldDomain(11,11);
		gw.setMapToFourRooms();
		gw.setProbSucceedTransitionDynamics(.8);
		
		final TerminalFunction tf = new SinglePFTF(
				PropositionalFunction.findPF(gw.generatePfs(), GridWorldDomain.PF_AT_LOCATION));
		
		final RewardFunction rf = new GoalBasedRF(new TFGoalCondition(tf), 5., -0.1);
		
		gw.setTf(tf);
		gw.setRf(rf);
		
		final OOSADomain domain = gw.generateDomain();
		
		GridWorldState s = new GridWorldState(	new GridAgent(0, 0),
												new GridLocation(10, 10, "loc0"));
		
		final ConstantStateGenerator sg = new ConstantStateGenerator(s);
		
		final SimpleHashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		
		LearningAgentFactory qLearningFactory = new LearningAgentFactory() {
			public String getAgentName() {
				return "Q-Learning";
			}
			
			public LearningAgent generateAgent() {
				return new QLearning(domain, 0.99, hashingFactory, 0.3, 0.1);
			}
		};
		
		SimulatedEnvironment env = new SimulatedEnvironment(domain, sg);
		
		LearningAlgorithmExperimenter exp = new LearningAlgorithmExperimenter(env, 
				10, 100, qLearningFactory);
		
		exp.setUpPlottingConfiguration(500, 250, 2, 1000, 
				TrialMode.MOST_RECENT_AND_AVERAGE,
				PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE,
				PerformanceMetric.AVERAGE_EPISODE_REWARD);
		
		exp.startExperiment();
	}

}