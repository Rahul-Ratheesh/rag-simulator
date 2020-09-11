package edu.iiitb.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import edu.iiitb.model.SimulatorModel;

public class DisplayPanel extends JPanel{
	
	DisplayPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JLabel max = new JLabel("          Maximum Matrix          ");
		add(max);
		displayMatrix(SimulatorModel.getMaximumDataStrucure());
		
		JLabel req = new JLabel("          Request Matrix          ");
		add(req);
		displayMatrix(SimulatorModel.getRequestDataStrucure());
		
		JLabel need = new JLabel("           Need Matrix           ");
		add(need);
		displayMatrix(SimulatorModel.getNeedDataStrucure());
		
		JLabel avail = new JLabel("        Available Vector        ");
		add(avail);
		displayArray(SimulatorModel.getAvailableDataStrucure());   
		
		JLabel alloc = new JLabel("        Allocation Matrix        ");
		add(alloc);
		displayMatrix(SimulatorModel.getAllocationDataStrucure());
		
	}
	
    public void displayMatrix(int[][] m) {
    	
    	if (m == null) {
    		JLabel label = new JLabel("<html><br><br>Displayed after mode selection<br><br></html>");
    		Font font = new Font("Verdana", Font.BOLD, 12);
    		label.setForeground(Color.RED);
    		label.setFont(font);
    		add(label);
    		return;
    	}
    	
        JPanel panel = new JPanel(new GridLayout(m.length+1, m[0].length+1));
        
        int rows = m.length;
        int columns = m[0].length;
        
        JLabel l = null;
        
        
        for(int i=0;i<rows+1;i++){
            for(int j=0;j<columns+1;j++){
            	if (i == 0 && j == 0)
            		l = new JLabel("-", JLabel.CENTER);
            	else if (i == 0) {
            		l = new JLabel("R" + (j-1), JLabel.CENTER);
            		Font font = new Font("Verdana", Font.BOLD, 14);
            		l.setForeground(Color.red);
            		l.setFont(font);
            	}
            	else if (j == 0) {
            		l = new JLabel("P" + (i-1), JLabel.CENTER);
            		Font font = new Font("Verdana", Font.BOLD, 14);
            		l.setForeground(Color.red);
            		l.setFont(font);
            	}
            	else {
            		l = new JLabel("" + m[i-1][j-1], JLabel.CENTER);
            		Font font = new Font("Verdana", Font.ITALIC, 14);
            		l.setForeground(Color.darkGray);
            		l.setFont(font);
            	}
            	l.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        		panel.add(l);
        	}
        }
        add(panel);

    }
    
    public void displayArray(int[] m) {
    	
    	if (m == null) {
    		JLabel label = new JLabel("<html><br><br>Displayed after mode selection<br><br></html>");
    		Font font = new Font("Verdana", Font.BOLD, 12);
    		label.setForeground(Color.RED);
    		label.setFont(font);
    		add(label);
    		return;
    	}
    	
        JPanel panel = new JPanel(new GridLayout(2, m.length));
        
        int rows = 2;
        int columns = m.length;
        
        JLabel l = null;
        
        for(int i=0;i<rows;i++){
            for(int j=0;j<columns;j++){
            	if (i == 0) {
            		l = new JLabel("R" + j, JLabel.CENTER);
            		Font font = new Font("Verdana", Font.BOLD, 14);
            		l.setForeground(Color.red);
            		l.setFont(font);
            	}
            	else {
            		l = new JLabel("" + m[j], JLabel.CENTER);
            		Font font = new Font("Verdana", Font.ITALIC, 14);
            		l.setForeground(Color.darkGray);
            		l.setFont(font);
            	}
            	l.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        		panel.add(l);
        	}
        }
        add(panel);

    }
	
}
