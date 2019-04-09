package edu.cbohan3;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import javax.imageio.ImageIO;
import burlap.mdp.core.state.State;
import burlap.visualizer.StatePainter;

public class DoorsAndKeysVisualizer implements StatePainter {
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
		float fWidth = DoorsAndKeys.map[0].length;
		float fHeight = DoorsAndKeys.map.length;
		
		//Figure out how big each tile is.
		int width = (int) Math.floor(cWidth / fWidth);
		int height = (int) Math.floor(cHeight / fHeight);
		
		//Paint all the walls.
		for (int y = 0; y < DoorsAndKeys.map.length; y++) {
			for (int x = 0; x < DoorsAndKeys.map[0].length; x++) {
				float rx = x * width;
				float ry = y * height;
				
				if (x == DoorsAndKeys.goalX && y == DoorsAndKeys.goalY)
					g2.drawImage(chalice, (int)rx, (int)ry, width, height, null);
				else if (DoorsAndKeys.map[y][x] == 1) 
					g2.drawImage(wallBrick, (int)rx, (int)ry, width, height, null);
				else if (DoorsAndKeys.map[y][x] == 0 || DoorsAndKeys.map[y][x] == DoorsAndKeys.K1 ||
						DoorsAndKeys.map[y][x] == DoorsAndKeys.K2 || DoorsAndKeys.map[y][x] == DoorsAndKeys.D1 
						|| DoorsAndKeys.map[y][x] == DoorsAndKeys.D2 )
					g2.drawImage(floor, (int)rx, (int)ry, width, height, null);
			}
		}
		
		//Draw the player.
		//Get the agent's position.
		int ax = (Integer)s.get(DoorsAndKeys.VAR_X);
		int ay = (Integer)s.get(DoorsAndKeys.VAR_Y);
		
		float rx = ax * width;
		float ry = ay * height;
		
		g2.drawImage(agent, (int)rx, (int)ry, width, height, null);
		
		//Draw the keys.		
		int key1InInventory = (Integer)s.get(DoorsAndKeys.VAR_KEY1_IN_INVENTORY);
		int key2InInventory = (Integer)s.get(DoorsAndKeys.VAR_KEY2_IN_INVENTORY);
		int key1X = DoorsAndKeys.getObjectPosition(DoorsAndKeys.K1)[0];
		int key1Y = DoorsAndKeys.getObjectPosition(DoorsAndKeys.K1)[1];
		int key2X = DoorsAndKeys.getObjectPosition(DoorsAndKeys.K2)[0];
		int key2Y = DoorsAndKeys.getObjectPosition(DoorsAndKeys.K2)[1];
		
		if (key1InInventory == 0) 
			g2.drawImage(key, (int)key1X * width, (int)key1Y * height, width, height, null);
		
		if (key2InInventory == 0) 
			g2.drawImage(key, (int)key2X * width, (int)key2Y * height, width, height, null);
		
		//Draw the doors.		
		int door1Open = (Integer)s.get(DoorsAndKeys.VAR_DOOR1_OPEN);
		int door2Open = (Integer)s.get(DoorsAndKeys.VAR_DOOR2_OPEN);
		
		if (door1Open == 0) {
			int door1X = DoorsAndKeys.getObjectPosition(DoorsAndKeys.D1)[0];
			int door1Y = DoorsAndKeys.getObjectPosition(DoorsAndKeys.D1)[1];
			g2.drawImage(door, (int)door1X * width, (int)door1Y * height, width, height, null);
		}
		
		if (door2Open == 0) {
			int door2X = DoorsAndKeys.getObjectPosition(DoorsAndKeys.D2)[0];
			int door2Y = DoorsAndKeys.getObjectPosition(DoorsAndKeys.D2)[1];
			g2.drawImage(door, (int)door2X * width, (int)door2Y * height, width, height, null);
		}
	}

}
