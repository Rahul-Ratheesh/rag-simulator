package edu.iiitb.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.logging.Level;

public class Process {
	
	private static final int RADIUS = 35;
	
	private int processId;
	private Point processLoc;
	private Color blue = new Color(77,138,232);
	private Rectangle processBorder = new Rectangle();
	private boolean processSelected = false;
	
	public Process(int id, Point loc) {
		MainUI.logger.log(Level.INFO,"Adding P"+id);
		processId = id;
		processLoc = loc;
		processBorder.setBounds(loc.x - RADIUS, loc.y - RADIUS, 2*RADIUS, 2*RADIUS);
	}
	
	// draws the process object on the drawing panel
    public void draw(Graphics g) {
        g.setColor(blue);
        g.fillOval(processLoc.x - RADIUS, processLoc.y - RADIUS, 2*RADIUS, 2*RADIUS);
        g.setColor(Color.black);
        g.fillOval(processLoc.x - RADIUS/10, processLoc.y - RADIUS/10, RADIUS/5, RADIUS/5);
        
        g.setColor(Color.black);
        g.setFont(new Font("default", Font.BOLD, 12));
	    g.drawString("P"+processId, processLoc.x, processLoc.y - RADIUS); 
	    
        if (processSelected) {
            g.setColor(Color.darkGray);
            g.drawRect(processBorder.x, processBorder.y, processBorder.width, processBorder.height);
        }
    }
    
    // de-selects all processes
    public static void deSelectAll(List<Process> processList) {
        for (Process p : processList) {
            p.setProcessSelected(false);
        }
    }
    
    // select the process under the mouse location
    public static boolean selectOne(List<Process> processList, Point mouseLoc) {
        for (Process p : processList) {
            if (p.contains(mouseLoc)) {
                if (!p.isProcessSelected()) {
                    Process.deSelectAll(processList);
                    p.setProcessSelected(true);
                }
                return true;
            }
        }
        return false;
    }
    
    public void updatePosition(Point mouseLoc) {
    	processLoc.x = mouseLoc.x;
    	processLoc.y = mouseLoc.y;
    }
    
    // returns true if the process border is within the mouse location
    public boolean contains(Point mouseLoc) {
        return processBorder.contains(mouseLoc);
    }

	public int getProcessId() {
		return processId;
	}

	public void setProcessId(int processId) {
		this.processId = processId;
	}

	public Point getProcessLoc() {
		return processLoc;
	}

	public void setProcessLoc(Point processLoc) {
		this.processLoc = processLoc;
	}

	public Rectangle getProcessBorder() {
		return processBorder;
	}

	public void setProcessBorder(Rectangle processBorder) {
		this.processBorder = processBorder;
	}

	public boolean isProcessSelected() {
		return processSelected;
	}

	public void setProcessSelected(boolean processSelected) {
		this.processSelected = processSelected;
	}



}
