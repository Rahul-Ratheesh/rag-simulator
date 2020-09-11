package edu.iiitb.view;

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

public class MainUI extends JFrame {
	
	public static MainUI ui;
	public static List<Process> processList = new ArrayList<Process>();
	static List<Resource> resourceList = new ArrayList<Resource>();
	static List<Edge> edgeList = new ArrayList<Edge>();
	
	JMenuItem display;
	JMenuItem reset;
	JMenuItem close;
	JMenuItem about;
	
	public static Logger logger= Logger.getLogger("DeadLocklog");
	public static FileHandler fh;
	
	public static JLabel statusbar;
	
	public MainUI() {
		
        try {  
            // This block configures the logger with handler and formatter
        	DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH:mm:ss");
        	Date date = new Date();
        	
        	fh=new FileHandler("./Deadlock_"+dateFormat.format(date)+".log");
            logger.addHandler(fh);  
            
            
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);  
              
            // the following statement is used to log any messages  
            logger.setUseParentHandlers(false);
            logger.log(Level.INFO,"START");  
              
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
        
		setLocation(200,100);
		setSize(1024, 768);
		setTitle("Deadlock Simulator");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		processList = new ArrayList<Process>();
    	resourceList = new ArrayList<Resource>();
    	edgeList = new ArrayList<Edge>();
    	
    	display = new JMenuItem("Display Datastructures");
    	reset = new JMenuItem("Reset");
    	close = new JMenuItem("Close");
    	about = new JMenuItem("About");
		display();
	}
	
	public  void display() {
		
        JMenuBar menubar = new JMenuBar();
        menubar.setBorder(new BevelBorder(BevelBorder.RAISED));
        
        JMenu file = new JMenu("File");
        
        display.addActionListener(new MenuBarMethod());
        reset.addActionListener(new MenuBarMethod());
        close.addActionListener(new MenuBarMethod());
        
        file.add(display);
        file.add(reset);
        file.add(close);
        menubar.add(file);
               
        JMenu help = new JMenu("Help");
        about.addActionListener(new MenuBarMethod());
        file.add(about);
        help.add(about);
        menubar.add(help);
        
        setJMenuBar(menubar);
        
        ControlPanel cp = new ControlPanel();
        add(cp, BorderLayout.WEST);
        
		DrawingPanel d = new DrawingPanel();
		add(d, BorderLayout.CENTER);
		
        statusbar = new JLabel("Status");
        statusbar.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        add(statusbar, BorderLayout.SOUTH);
	}
	
	public static void main(String[] args) {
    	ui = new MainUI();
        ui.setVisible(true);
    }

	public static void removeProcess() {
		Process p=null;
		p=MainUI.processList.get(MainUI.processList.size()-1);
		ArrayList<Edge> edgeToBeRemoved=new ArrayList<Edge>();
		
		for(Edge e:edgeList)
		{
			if(e.getProcess().equals(p))
			{
				edgeToBeRemoved.add(e);
			}
		}
		
		for(Edge e:edgeToBeRemoved)
		{
			edgeList.remove(e);
		}
		MainUI.ui.repaint();
	}
    
}


class MenuBarMethod implements ActionListener{
    

	@Override
	public void actionPerformed(ActionEvent e) {
		if("Display Datastructures".equals(e.getActionCommand())){
            Window parentWindow = MainUI.ui;
            JDialog dialog = new JDialog(parentWindow);
            dialog.setModal(true);
            dialog.add(new DisplayPanel());
            dialog.pack();
            dialog.setVisible(true);
		}
        if("Reset".equals(e.getActionCommand())){
        	MainUI.logger.log(Level.INFO, "Application Reset");
        	MainUI.ui.dispose();
        	MainUI.ui= new MainUI();
        	MainUI.ui.setVisible(true);
        }
        else if("Close".equals(e.getActionCommand())){
        	MainUI.logger.log(Level.INFO, "Application Closed");
            MainUI.ui.dispose();
         }
        else if("About".equals(e.getActionCommand()))
        {
        	JOptionPane.showMessageDialog(null, "Developed by \n" +
        										"Rahul R, Michael Peter, Chirag Saraiya and Priyanka Shukla \n" +
        										"under guidance of Prof. L.T JayPrakash \n" +
        										"as a part of Semester 1 OS Project, 2013.");
        }        
    }
	
}

