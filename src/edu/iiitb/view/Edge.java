package edu.iiitb.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;

public class Edge {
	
    private Process process;
    private Resource resource;
    private Point startEdgePos;
    private Point endEdgePos;
    
    enum EdgeType {CLAIM, ALLOCATED, REQUEST};
    private EdgeType edgeType;
    
    private final int ARROW_HEAD = 10;


    //constructor to draw an edge from resource to process
    public Edge(Process process, Resource resource, Point startEdgePos, Point endEdgePos, EdgeType edgeType) {
    	MainUI.logger.log(Level.INFO,"Adding Edge from R"+resource.getResourceId()+" to P"+process.getProcessId());
		this.process = process;
		this.resource = resource;
		this.startEdgePos = startEdgePos;
		this.endEdgePos = endEdgePos;
		this.edgeType = edgeType;
	}
    
   
  //constructor to draw edge from process to resource
    public Edge(Resource resource, Process process,	Point startEdgePos, Point endEdgePos, EdgeType edgeType) {
    	MainUI.logger.log(Level.INFO,"Adding Edge from P"+process.getProcessId()+" to R"+resource.getResourceId());
		this.process = process;
		this.resource = resource;
		this.startEdgePos = startEdgePos;
		this.endEdgePos = endEdgePos;
		this.edgeType = edgeType;
	}

	public void draw(Graphics g) {
		Stroke drawingStroke;
		Graphics2D g2d = (Graphics2D)g.create();
		switch(this.edgeType) {
			case CLAIM:
				drawingStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				g2d.setColor(Color.darkGray);
				break;
			case ALLOCATED:
				drawingStroke = new BasicStroke(3);
				g2d.setColor(new Color(30, 120, 10));
				break;
			case REQUEST:
				drawingStroke = new BasicStroke(3);
				g2d.setColor(Color.red);
				break;
			default:
				drawingStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
				break;
		}
        
        g2d.setStroke(drawingStroke);
        
        double dx = endEdgePos.x - startEdgePos.x;
        double dy = endEdgePos.y - startEdgePos.y;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx*dx + dy*dy);
        
        AffineTransform at = AffineTransform.getTranslateInstance(startEdgePos.x, startEdgePos.y);
        at.concatenate(AffineTransform.getRotateInstance(angle));
        g2d.transform(at);
        g2d.drawLine(0, 0, len, 0);
        g2d.fillPolygon(new int[] {len, len-ARROW_HEAD, len-ARROW_HEAD}, new int[] {0, -ARROW_HEAD, ARROW_HEAD}, 3);
    }
		
	public Process getProcess() {
		return process;
	}


	public void setProcess(Process process) {
		this.process = process;
	}


	public Resource getResource() {
		return resource;
	}


	public void setResource(Resource resource) {
		this.resource = resource;
	}


	public Point getStartEdgePos() {
		return startEdgePos;
	}


	public void setStartEdgePos(Point startEdgePos) {
		this.startEdgePos = startEdgePos;
	}


	public Point getEndEdgePos() {
		return endEdgePos;
	}


	public void setEndEdgePos(Point endEdgePos) {
		this.endEdgePos = endEdgePos;
	}
}
