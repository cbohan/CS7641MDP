package edu.cbohan3;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import burlap.mdp.core.state.State;
import burlap.visualizer.StatePainter;

public class SimpleWorldVisualizer implements StatePainter {
	public void paint(Graphics2D g2, State s, float cWidth, float cHeight) {
		//Draw the walls.
		//Set our drawing color to black.
		g2.setColor(Color.black);
		
		//Get the width and height of our map.
		float fWidth = SimpleWorld.map[0].length;
		float fHeight = SimpleWorld.map.length;
		
		//Figure out how big each tile is.
		float width = cWidth / fWidth;
		float height = cHeight / fHeight;
		
		//Paint all the walls.
		for (int y = 0; y < SimpleWorld.map.length; y++) {
			for (int x = 0; x < SimpleWorld.map[0].length; x++) {
				if (SimpleWorld.map[y][x] == 1) {
					float rx = x * width;
					float ry = y * height;
					g2.fill(new Rectangle2D.Float(rx, ry, width, height));
				}
			}
		}
		
		//Draw the player.
		//Set the draw color to grey.
		g2.setColor(Color.GRAY);
		
		//Get the agent's position.
		int ax = (Integer)s.get(SimpleWorld.VAR_X);
		int ay = (Integer)s.get(SimpleWorld.VAR_Y);
		
		float rx = ax * width;
		float ry = ay * height;
		
		g2.fill(new Ellipse2D.Float(rx, ry, width, height));
		
		//Draw the keys.
		g2.setColor(Color.YELLOW);
		
		int key1InInventory = (Integer)s.get(SimpleWorld.VAR_KEY1_IN_INVENTORY);
		int key2InInventory = (Integer)s.get(SimpleWorld.VAR_KEY2_IN_INVENTORY);
		
		if (key1InInventory == 0) {
			int key1X = SimpleWorld.getObjectPosition(SimpleWorld.K1)[0];
			int key1Y = SimpleWorld.getObjectPosition(SimpleWorld.K1)[1];
			g2.fill(new Ellipse2D.Float(key1X * width, key1Y * height, width, height));
		}
		
		if (key2InInventory == 0) {
			int key2X = SimpleWorld.getObjectPosition(SimpleWorld.K2)[0];
			int key2Y = SimpleWorld.getObjectPosition(SimpleWorld.K2)[1];
			g2.fill(new Ellipse2D.Float(key2X * width, key2Y * height, width, height));
		}
		
		//Draw the doors.
		g2.setColor(Color.GREEN);
		
		int door1Open = (Integer)s.get(SimpleWorld.VAR_DOOR1_OPEN);
		int door2Open = (Integer)s.get(SimpleWorld.VAR_DOOR2_OPEN);
		
		if (door1Open == 0) {
			int door1X = SimpleWorld.getObjectPosition(SimpleWorld.D1)[0];
			int door1Y = SimpleWorld.getObjectPosition(SimpleWorld.D1)[1];
			g2.fill(new Rectangle2D.Float(door1X * width, door1Y * height, width, height));
		}
		
		if (door2Open == 0) {
			int door2X = SimpleWorld.getObjectPosition(SimpleWorld.D2)[0];
			int door2Y = SimpleWorld.getObjectPosition(SimpleWorld.D2)[1];
			g2.fill(new Rectangle2D.Float(door2X * width, door2Y * height, width, height));
		}
	}
}
