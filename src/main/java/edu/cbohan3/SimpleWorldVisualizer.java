package edu.cbohan3;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import burlap.mdp.core.state.State;
import burlap.visualizer.StatePainter;

public class SimpleWorldVisualizer implements StatePainter {
	public void paint(Graphics2D g2, State s, float cWidth, float cHeight) {
		Image wallBrick = null;
		Image floor = null;
		Image agent = null;
		Image key = null;
		Image door = null;
		Image chalice = null;
		try {
			wallBrick = ImageIO.read(new File("gfx/wall_brick.png"));
			floor = ImageIO.read(new File("gfx/floor.png"));
			agent = ImageIO.read(new File("gfx/agent.png"));
			key = ImageIO.read(new File("gfx/key.png"));
			door = ImageIO.read(new File("gfx/door.png"));
			chalice = ImageIO.read(new File("gfx/chalice.png"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Draw the walls.
		//Get the width and height of our map.
		float fWidth = SimpleWorld.map[0].length;
		float fHeight = SimpleWorld.map.length;
		
		//Figure out how big each tile is.
		int width = (int) Math.floor(cWidth / fWidth);
		int height = (int) Math.floor(cHeight / fHeight);
		
		//Paint all the walls.
		for (int y = 0; y < SimpleWorld.map.length; y++) {
			for (int x = 0; x < SimpleWorld.map[0].length; x++) {
				float rx = x * width;
				float ry = y * height;
				
				if (x == SimpleWorld.goalX && y == SimpleWorld.goalY)
					g2.drawImage(chalice, (int)rx, (int)ry, width, height, null);
				else if (SimpleWorld.map[y][x] == 1) 
					g2.drawImage(wallBrick, (int)rx, (int)ry, width, height, null);
				else if (SimpleWorld.map[y][x] == 0 || SimpleWorld.map[y][x] == SimpleWorld.K1 ||
						SimpleWorld.map[y][x] == SimpleWorld.K2 || SimpleWorld.map[y][x] == SimpleWorld.D1 
						|| SimpleWorld.map[y][x] == SimpleWorld.D2 )
					g2.drawImage(floor, (int)rx, (int)ry, width, height, null);
			}
		}
		
		//Draw the player.
		//Get the agent's position.
		int ax = (Integer)s.get(SimpleWorld.VAR_X);
		int ay = (Integer)s.get(SimpleWorld.VAR_Y);
		
		float rx = ax * width;
		float ry = ay * height;
		
		g2.drawImage(agent, (int)rx, (int)ry, width, height, null);
		
		//Draw the keys.		
		int key1InInventory = (Integer)s.get(SimpleWorld.VAR_KEY1_IN_INVENTORY);
		int key2InInventory = (Integer)s.get(SimpleWorld.VAR_KEY2_IN_INVENTORY);
		int key1X = SimpleWorld.getObjectPosition(SimpleWorld.K1)[0];
		int key1Y = SimpleWorld.getObjectPosition(SimpleWorld.K1)[1];
		int key2X = SimpleWorld.getObjectPosition(SimpleWorld.K2)[0];
		int key2Y = SimpleWorld.getObjectPosition(SimpleWorld.K2)[1];
		
		if (key1InInventory == 0) 
			g2.drawImage(key, (int)key1X * width, (int)key1Y * height, width, height, null);
		
		if (key2InInventory == 0) 
			g2.drawImage(key, (int)key2X * width, (int)key2Y * height, width, height, null);
		
		//Draw the doors.		
		int door1Open = (Integer)s.get(SimpleWorld.VAR_DOOR1_OPEN);
		int door2Open = (Integer)s.get(SimpleWorld.VAR_DOOR2_OPEN);
		
		if (door1Open == 0) {
			int door1X = SimpleWorld.getObjectPosition(SimpleWorld.D1)[0];
			int door1Y = SimpleWorld.getObjectPosition(SimpleWorld.D1)[1];
			g2.drawImage(door, (int)door1X * width, (int)door1Y * height, width, height, null);
		}
		
		if (door2Open == 0) {
			int door2X = SimpleWorld.getObjectPosition(SimpleWorld.D2)[0];
			int door2Y = SimpleWorld.getObjectPosition(SimpleWorld.D2)[1];
			g2.drawImage(door, (int)door2X * width, (int)door2Y * height, width, height, null);
		}
	}

}
