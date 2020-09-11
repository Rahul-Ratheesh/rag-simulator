package edu.iiitb.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.logging.Level;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;

import edu.iiitb.controller.ResourceManager;

public class ControlPanel extends JToolBar {

	static JButton processButton;
	static JButton resourceButton;
	static JButton preventButton;
	static JButton avoidButton;
	//static JButton detectButton;
	static JButton recoverButton;
	static JButton detect;
	static JButton recover;
	static JButton edgeButton;

	static char modeFlg;

	static ResourceManager rmgr = new ResourceManager();

	ImageIcon resourceIcon = new ImageIcon(getClass().getResource("/resource.png"));
	private ResourceAction resourceAction = new ResourceAction("Resource    ",resourceIcon);

	ImageIcon processIcon = new ImageIcon(getClass().getResource("/process.png"));
	private ProcessAction processAction = new ProcessAction("Process      ",processIcon);

	ImageIcon preventIcon = new ImageIcon(getClass().getResource("/P.png"));
	private PreventAction preventAction = new PreventAction("Prevention ",preventIcon);

	ImageIcon avoidIcon = new ImageIcon(getClass().getResource("/A.png"));
	private AvoidAction avoidAction = new AvoidAction("Avoidance  ", avoidIcon);

	/*ImageIcon detectIcon = new ImageIcon(getClass().getResource("/D.png"));
	private DetectAction detectAction = new DetectAction("Detection   ",
			detectIcon);*/

	ImageIcon recoverIcon = new ImageIcon(getClass().getResource("/D.png"));
	private RecoverAction recoverAction = new RecoverAction("Detection   ",recoverIcon);

	ImageIcon edgeIcon = new ImageIcon(getClass().getResource("/arrow.png"));
	private EdgeAction edgeAction = new EdgeAction("Claim Edge ", edgeIcon);

	ImageIcon detectIcon1 = new ImageIcon(getClass().getResource("/detect.png"));
	private DetectDeadlockAction detectDeadlock = new DetectDeadlockAction("Check          ", detectIcon1);

	ImageIcon recoverIcon1 = new ImageIcon(getClass().getResource("/R.png"));
	private RecoverDeadlockAction recoverAction1 = new RecoverDeadlockAction("Recover       ", recoverIcon);

	ControlPanel() {

		ProcessAction.processCount = -1;
		ResourceAction.resourceCount = -1;

		this.setBackground(new Color(220, 220, 220));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(new BevelBorder(BevelBorder.RAISED));

		resourceButton = new JButton(resourceAction);
		resourceButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		resourceButton.setEnabled(true);
		this.add(resourceButton);

		processButton = new JButton(processAction);
		processButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		processButton.setEnabled(true);
		this.add(processButton);

		preventButton = new JButton(preventAction);
		preventButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.add(preventButton);

		avoidButton = new JButton(avoidAction);
		avoidButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.add(avoidButton);

		/*detectButton = new JButton(detectAction);
		detectButton.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.RAISED));
		this.add(detectButton);*/

		recoverButton = new JButton(recoverAction);
		recoverButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.add(recoverButton);

		edgeButton = new JButton(edgeAction);
		edgeButton.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.add(edgeButton);
		edgeButton.setVisible(false);
		edgeButton.setEnabled(false);

		// As detect button to be visible only in Detection mode, therefore
		// disabling it
		detect = new JButton(detectDeadlock);
		detect.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.add(detect);
		detect.setVisible(false);
		detect.setEnabled(false);

		recover = new JButton(recoverAction1);
		recover.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		this.add(recover);
		recover.setVisible(false);
		recover.setEnabled(false);

	}

}

class ProcessAction extends AbstractAction {

	// processCount is accessed by PreventAction,
	public static int processCount = -1;

	public ProcessAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		if (DrawingPanel.mousePos != null) {
			Process p = new Process(++processCount, DrawingPanel.mousePos);
			p.setProcessSelected(true);
			MainUI.processList.add(p);
			MainUI.ui.repaint();
		}
	}
}

class ResourceAction extends AbstractAction {

	public static int resourceCount = -1;

	public ResourceAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		if (DrawingPanel.mousePos != null) {
			Resource r = new Resource(++resourceCount, 1, DrawingPanel.mousePos);
			r.setResourceSelected(true);
			MainUI.resourceList.add(r);
			MainUI.ui.repaint();
		}
	}
}

// Prevention Button
class PreventAction extends AbstractAction {

	public PreventAction(String name, Icon icon) {
		super(name, icon);
	}

	// call Resource Manager to initialise the data structures
	public void actionPerformed(ActionEvent e) {
		MainUI.statusbar.setText("Deadlock Prevention mode");
		MainUI.logger.log(Level.INFO,"Deadlock Prevention mode");
		new ResourceManager().setDataStructure(ProcessAction.processCount + 1, ResourceAction.resourceCount + 1);
		ControlPanel.resourceButton.setEnabled(false);// disabling resource and process button
		ControlPanel.processButton.setEnabled(false);
		ControlPanel.preventButton.setForeground(Color.red);
		ControlPanel.avoidButton.setForeground(Color.black);
		//ControlPanel.detectButton.setForeground(Color.black);
		ControlPanel.recoverButton.setForeground(Color.black);
		ControlPanel.edgeButton.setVisible(true);
		ControlPanel.edgeButton.setEnabled(true);

		int numProcess = ProcessAction.processCount + 1;
		int numResource = ResourceAction.resourceCount + 1;

		ControlPanel.rmgr.setDataStructure(numProcess, numResource);
		for (Resource r : MainUI.resourceList) {
			ControlPanel.rmgr.setAvailable(r.getResourceId(),
					r.getNumOfInstances());
		}

		MainUI.statusbar.setText("Declare the max resources needed for each process by right clicking on the process");
		JOptionPane.showMessageDialog(null, "Deadlock Prevention by dissatisfying hold and wait.\n" +
											"All resources requested by a process are allocated to it before its commencement.");

		ControlPanel.modeFlg = 'P';
		


	}
}

class AvoidAction extends AbstractAction {

	public AvoidAction(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		MainUI.statusbar.setText("Deadlock Avoidance mode");
		MainUI.logger.log(Level.INFO,"Deadlock Avoidance mode");
		
		ControlPanel.resourceButton.setEnabled(false);
		ControlPanel.processButton.setEnabled(false);
		ControlPanel.preventButton.setForeground(Color.black);
		ControlPanel.avoidButton.setForeground(Color.red);
		//ControlPanel.detectButton.setForeground(Color.black);
		ControlPanel.recoverButton.setForeground(Color.black);
		ControlPanel.edgeButton.setVisible(true);
		ControlPanel.edgeButton.setEnabled(true);

		int numProcess = ProcessAction.processCount + 1;
		int numResource = ResourceAction.resourceCount + 1;

		ControlPanel.rmgr.setDataStructure(numProcess, numResource);
		for (Resource r : MainUI.resourceList) {
			ControlPanel.rmgr.setAvailable(r.getResourceId(), r.getNumOfInstances());
		}

		ControlPanel.modeFlg = 'A';
		JOptionPane.showMessageDialog(null, "Deadlock Avoidance using Banker's algorithm");
		MainUI.statusbar.setText("Declare the max resources needed for each process");

	}
}

class DetectAction extends AbstractAction {

	public DetectAction(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		MainUI.statusbar.setText("Deadlock Detection mode");
		MainUI.logger.log(Level.INFO,"Deadlock Detection mode");
		
		ControlPanel.resourceButton.setEnabled(false);
		ControlPanel.processButton.setEnabled(false);
		ControlPanel.preventButton.setForeground(Color.black);
		ControlPanel.avoidButton.setForeground(Color.black);
		//ControlPanel.detectButton.setForeground(Color.red);
		ControlPanel.recoverButton.setForeground(Color.black);
		ControlPanel.detect.setVisible(true);
		ControlPanel.detect.setEnabled(true);
		ControlPanel.edgeButton.setVisible(true);
		ControlPanel.edgeButton.setEnabled(true);

		int numProcess = ProcessAction.processCount + 1;
		int numResource = ResourceAction.resourceCount + 1;

		ControlPanel.rmgr.setDataStructure(numProcess, numResource);
		for (Resource r : MainUI.resourceList) {
			ControlPanel.rmgr.setAvailable(r.getResourceId(), r.getNumOfInstances());
		}

		ControlPanel.modeFlg = 'D';
		

	}
}

class DetectDeadlockAction extends AbstractAction {

	public DetectDeadlockAction(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (ControlPanel.modeFlg == 'D') {

			String result = ControlPanel.rmgr.detectDeadlock();
			MainUI.statusbar.setText(result);
			MainUI.logger.log(Level.INFO,result);
		} else if (ControlPanel.modeFlg == 'R') {
			String result = ControlPanel.rmgr.detectDeadlock();

			if (result.equals("System is in Deadlock")) {
				ControlPanel.detect.setEnabled(false);
				ControlPanel.recover.setVisible(true);
				ControlPanel.recover.setEnabled(true);

			}
			MainUI.statusbar.setText(result);
			MainUI.logger.log(Level.INFO,result);
		}

	}
}

class RecoverAction extends AbstractAction {

	public RecoverAction(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		MainUI.statusbar.setText("Deadlock Detection mode");
		MainUI.logger.log(Level.INFO,"Deadlock Detection mode");
		
		ControlPanel.resourceButton.setEnabled(false);
		ControlPanel.processButton.setEnabled(false);
		ControlPanel.preventButton.setForeground(Color.black);
		ControlPanel.avoidButton.setForeground(Color.black);
		//ControlPanel.detectButton.setForeground(Color.black);
		ControlPanel.recoverButton.setForeground(Color.red);
		ControlPanel.detect.setVisible(true);
		ControlPanel.detect.setEnabled(true);
		ControlPanel.edgeButton.setVisible(true);
		ControlPanel.edgeButton.setEnabled(true);

		int numProcess = ProcessAction.processCount + 1;
		int numResource = ResourceAction.resourceCount + 1;

		ControlPanel.rmgr.setDataStructure(numProcess, numResource);
		for (Resource r : MainUI.resourceList) {
			ControlPanel.rmgr.setAvailable(r.getResourceId(),r.getNumOfInstances());
		}

		ControlPanel.modeFlg = 'R';
		JOptionPane.showMessageDialog(null, "Deadlock Detection using Banker's algorithm");
	}
}

class RecoverDeadlockAction extends AbstractAction {
//will recover the system from deadlock, by removing processes until system reaches a stable state
	public RecoverDeadlockAction(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		MainUI.statusbar.setText("Deadlock Recovery mode");
		JOptionPane.showMessageDialog(null, "Deadlock Recovery by killing a process");
		MainUI.logger.log(Level.INFO,"Deadlock Recovery mode");
		
		ControlPanel.rmgr.recoverSytemFromDeadlock();
		ControlPanel.detect.setEnabled(true);
		ControlPanel.recover.setVisible(false);
		ControlPanel.recover.setEnabled(false);
		
		
		
	}
}

class EdgeAction extends AbstractAction {

	static boolean edgeCreationMode = false;

	public EdgeAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		edgeCreationMode = true;

	}
}
