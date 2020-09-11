package edu.iiitb.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.logging.Level;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Resource {
	
	private static final int LENGTH = 100;
	private static final int BREADTH = 60;
	private static final int RADIUS = 5;
	
	private int resourceId;
	private int numOfInstances;
	private Point resourceLoc;
	private Rectangle resourceBorder = new Rectangle();
	private Color orange = new Color(255,170,50);
	private boolean resourceSelected = false;
	
	public Resource(int id, int numOfInstances, Point loc) {
		MainUI.logger.log(Level.INFO,"Adding R"+id);
		this.resourceId = id;
		this.numOfInstances = numOfInstances;
		this.resourceLoc = loc;
		resourceBorder.setBounds(resourceLoc.x - BREADTH/2, resourceLoc.y - LENGTH/2, BREADTH, LENGTH);
	}
	
    public void draw(Graphics g) {
    	g.setColor(orange);
        g.fillRect(resourceLoc.x - BREADTH/2, resourceLoc.y - LENGTH/2, BREADTH, LENGTH);
        
        g.setColor(Color.black);
        g.setFont(new Font("default", Font.BOLD, 12));
	    g.drawString("R"+resourceId, resourceLoc.x - RADIUS, resourceLoc.y - LENGTH/2); 
        
        g.setColor(Color.black);
        g.setFont(new Font("default", Font.BOLD, 12));
	    g.drawString(""+numOfInstances, resourceLoc.x - RADIUS, resourceLoc.y); 

       
        if (resourceSelected) {
            g.setColor(Color.blue);
            g.drawRect(resourceLoc.x - BREADTH/2, resourceLoc.y - LENGTH/2, BREADTH, LENGTH);
        }
    }

    // de-selects all resources
    public static void deSelectAll(List<Resource> resourceList) {
        for (Resource r : resourceList) {
            r.setResourceSelected(false);
        }
    }
    
    // select the resource under the mouse location
    public static boolean selectOne(List<Resource> resourceList, Point mouseLoc) {
        for (Resource r : resourceList) {
            if (r.contains(mouseLoc)) {
                if (!r.isResourceSelected()) {
                	Resource.deSelectAll(resourceList);
                    r.setResourceSelected(true);
                }
                return true;
            }
        }
        return false;
    }
    
    // returns true if the process border is within the mouse location
    public boolean contains(Point mouseLoc) {
        return resourceBorder.contains(mouseLoc);
    }
    
    public void updatePosition(Point mouseLoc) {
        this.resourceLoc.x = mouseLoc.x;
        this.resourceLoc.y = mouseLoc.y;
    }

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public int getNumOfInstances() {
		return numOfInstances;
	}

	public void setNumOfInstances(int numOfInstances) {
		MainUI.logger.log(Level.INFO,"Number of Instances of R"+this.resourceId+"="+numOfInstances);
		this.numOfInstances = numOfInstances;
	}

	public Point getResourceLoc() {
		return resourceLoc;
	}

	public void setResourceLoc(Point resourceLoc) {
		this.resourceLoc = resourceLoc;
	}

	public boolean isResourceSelected() {
		return resourceSelected;
	}

	public void setResourceSelected(boolean resourceSelected) {
		this.resourceSelected = resourceSelected;
	}

	public Rectangle getResourceBorder() {
		return resourceBorder;
	}

	public void setResourceBorder(Rectangle resourceBorder) {
		this.resourceBorder = resourceBorder;
	}


}
