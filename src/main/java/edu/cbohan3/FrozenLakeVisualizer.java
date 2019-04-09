package edu.cbohan3;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.imageio.ImageIO;

import burlap.mdp.core.state.State;
import burlap.visualizer.StatePainter;

public class FrozenLakeVisualizer implements StatePainter {
	public void paint(Graphics2D g2, State s, float cWidth, float cHeight) {
		Image wallBrick = null;
		Image floor = null;
		Image hole = null;
		Image agent = null;
		Image chalice = null;
		try {
			wallBrick = ImageIO.read(new File("gfx/wall_brick.png"));
			floor = ImageIO.read(new File("gfx/floor.png"));
			agent = ImageIO.read(new File("gfx/agent.png"));
			hole = ImageIO.read(new File("gfx/hole.png"));
			chalice = ImageIO.read(new File("gfx/chalice.png"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Draw the walls.
		//Get the width and height of our map.
		float fWidth = FrozenLake.map[0].length;
		float fHeight = FrozenLake.map.length;
		
		//Figure out how big each tile is.
		int width = (int) Math.floor(cWidth / fWidth);
		int height = (int) Math.floor(cHeight / fHeight);
		
		//Paint all the walls.
		for (int y = 0; y < FrozenLake.map.length; y++) {
			for (int x = 0; x < FrozenLake.map[0].length; x++) {
				float rx = x * width;
				float ry = y * height;
				
				if (x == FrozenLake.goalX && y == FrozenLake.goalY)
					g2.drawImage(chalice, (int)rx, (int)ry, width, height, null);
				else if (FrozenLake.map[y][x] == FrozenLake.XX) 
					g2.drawImage(wallBrick, (int)rx, (int)ry, width, height, null);
				else if (FrozenLake.map[y][x] == FrozenLake.oo)
					g2.drawImage(floor, (int)rx, (int)ry, width, height, null);
				else if (FrozenLake.map[y][x] == FrozenLake.HH)
					g2.drawImage(hole, (int)rx, (int)ry, width, height, null);
			}
		}
		
		//Draw the player.
		//Get the agent's position.
		int ax = (Integer)s.get(DoorsAndKeys.VAR_X);
		int ay = (Integer)s.get(DoorsAndKeys.VAR_Y);
		
		float rx = ax * width;
		float ry = ay * height;
		
		g2.drawImage(agent, (int)rx, (int)ry, width, height, null);

	}
}
