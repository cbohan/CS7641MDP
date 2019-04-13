Code can be found at: https://github.com/cbohan/CS7641MDP

This project uses Burlap.


The only code you should need to alter is in the Main class. To 'play' the MDPs manually, go the 
the main method in the Main class and uncomment either "playDoorsAndKeys()" or "playFrozenLake()".
To perform value iteration, policy iteration, or Q learning, go into the correct solve method
(either solveFrozenLake() if you want to do the frozen lake MDP or solveDoorsAndKeys() if you want
to do the doors and keys MDP) and comment out all the things you don't want to do. For example: if 
you just want to do value iteration comment out the following lines:
behavior.doPolicyIteration(outputPath);
behavior.doQLearning(outputPath);

Value iteration, policy iteration, and Q learning all save episodes to the output folder. This should
be part of the repo but if it isn't make sure to create one. I would suggest commenting out 
everything you don't currently need, otherwise the program will take a very long time to run.