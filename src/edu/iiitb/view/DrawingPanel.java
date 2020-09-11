package edu.iiitb.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import edu.iiitb.controller.ResourceManager;

class DrawingPanel extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

	static Point mousePos;
	private static Point startEdgePos;
	private static Point endEdgePos;
	private Edge edge;
	private Process currProcess;
	private Resource currResource;
	private boolean contains = false;

	private JPopupMenu resourcePopup;
	private JPopupMenu processPopup;

	ResourceManager rmgr = new ResourceManager();

	public DrawingPanel() {

		JMenuItem addInstance = new JMenuItem("Add instances for this resource");
		addInstance.addActionListener(this);
		resourcePopup = new JPopupMenu();
		resourcePopup.add(addInstance);

		JMenuItem declareMax = new JMenuItem("Declare Maximum resources needed for this process");
		declareMax.addActionListener(this);
		processPopup = new JPopupMenu();
		processPopup.add(declareMax);

		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void paintComponent(Graphics g) {
		// System.out.println("Repainting...");
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (Resource r : MainUI.resourceList) {
			r.draw(g);
		}

		for (Process p : MainUI.processList) {
			p.draw(g);
		}
		if (EdgeAction.edgeCreationMode) {
			this.draw(g);
		}
		for (Edge e : MainUI.edgeList) {
			if (e != null)
				e.draw(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mousePos = e.getPoint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mousePos = e.getPoint();

		// When a resource is right clicked a pop-up is shown to add instances
		if (e.isPopupTrigger()) {
			for (Resource r : MainUI.resourceList) {
				contains = r.getResourceBorder().contains(mousePos);
				if (contains) {
					currResource = r;
					resourcePopup.show(this, e.getX(), e.getY());
				}
			}
			if (ControlPanel.modeFlg == 'A' || ControlPanel.modeFlg == 'P') {
				for (Process p : MainUI.processList) {
					contains = p.getProcessBorder().contains(mousePos);
					if (contains) {
						currProcess = p;
						processPopup.show(this, e.getX(), e.getY());
					}
				}
			}

		}

		// if the current mouse location does not contain any process
		// then deselect all the processes
		if (!Process.selectOne(MainUI.processList, mousePos)) {
			Process.deSelectAll(MainUI.processList);

		}

		if (!Resource.selectOne(MainUI.resourceList, mousePos)) {
			Resource.deSelectAll(MainUI.resourceList);
		}

		// get starting position of the edge
		if (EdgeAction.edgeCreationMode) {
			startEdgePos = e.getPoint();
			for (Process p : MainUI.processList) {
				contains = p.getProcessBorder().contains(startEdgePos);
				if (contains) {
					currProcess = p;
				}
			}
		} else {
			this.repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		String result = "";
		if (EdgeAction.edgeCreationMode) {
			endEdgePos = e.getPoint();
			currResource = null;
			EdgeAction.edgeCreationMode = false;

			for (Resource r : MainUI.resourceList) {
				contains = r.getResourceBorder().contains(endEdgePos);
				if (contains) {
					currResource = r;
				}
			}
			if (currResource == null) {
				MainUI.statusbar.setText("Invalid edge");
			} else {
				// create a claimed edge
				if (ControlPanel.modeFlg == 'P') {
					result = rmgr.multishot(currProcess.getProcessId(), currResource.getResourceId());
					if (result.equals("invalid1"))
						JOptionPane.showMessageDialog(this,"All requests to this resource were allocated for this process");
					else if (result.equals("invalid2"))
						JOptionPane.showMessageDialog(this,"Resource cannot be allocated as all requests are not allocated \nfor the previous process");
					else if (result.equals("pending")) {
						edge = new Edge(currProcess, currResource, startEdgePos, endEdgePos, Edge.EdgeType.REQUEST);
						MainUI.edgeList.add(edge);
					} else if (result.equals("available")) {
						edge = new Edge(currProcess, currResource, endEdgePos, startEdgePos, Edge.EdgeType.ALLOCATED);
						MainUI.edgeList.add(edge);
					}
				} else if (ControlPanel.modeFlg == 'A') {
					result = rmgr.callResourceRequestAlgo(currProcess.getProcessId(), currResource.getResourceId());
					if (result.equals("Success")) {
						edge = new Edge(currProcess, currResource, endEdgePos, startEdgePos, Edge.EdgeType.ALLOCATED);
						MainUI.edgeList.add(edge);

						boolean flag = rmgr.findSafetySeq(currProcess.getProcessId(), currResource.getResourceId());
						if (flag) {
							String seq = rmgr.getSafetySeq();
							MainUI.statusbar.setText(seq);
							MainUI.logger.log(Level.INFO,seq);
						} else {
							MainUI.statusbar.setText("Not safe");
							MainUI.logger.log(Level.INFO,"Not Safe");
						}
					} else {
						MainUI.statusbar.setText(result);
						MainUI.logger.log(Level.INFO,result);

					}
				} else if (ControlPanel.modeFlg == 'D'|| ControlPanel.modeFlg == 'R') {// for deadlock
															// detection mode
					int output = rmgr.assignRequests(
							currProcess.getProcessId(),
							currResource.getResourceId(),
							currResource.getNumOfInstances());
					// if resource is available to be allocated draw arrow from
					// the resource to process-> ALLOCATED
					if (output == 3) {
						edge = new Edge(currProcess, currResource, endEdgePos, startEdgePos, Edge.EdgeType.ALLOCATED);
						MainUI.edgeList.add(edge);
					}
					// if resource not currently available draw arrow from the
					// process to resource ->REQUEST
					else if (output == 2) {
						edge = new Edge(currResource, currProcess, startEdgePos, endEdgePos, Edge.EdgeType.REQUEST);
						MainUI.edgeList.add(edge);
					}
					// if process is requesting for the resource more than its
					// number of instances the reject the request
					else {
						MainUI.statusbar.setText("This Request exceeds the number of resource available");
						MainUI.logger.log(Level.INFO, "This Request exceeds the number of resource available");
					}

				}
			}
			this.repaint();
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (EdgeAction.edgeCreationMode) {
			endEdgePos = e.getPoint();
			this.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("Add instances for this resource")) {
			String numOfInstances = JOptionPane.showInputDialog(this, "Number of instances");
			try {
				currResource.setNumOfInstances(Integer.parseInt(numOfInstances));
			} catch (NumberFormatException exp) {
				System.out.println("User Entered invalid data for Number of instances of Resource");
			}
			this.repaint();
		}
		if (e.getActionCommand().equalsIgnoreCase("Declare Maximum resources needed for this process")) {
			for (int i = 0; i <= ResourceAction.resourceCount; i++) {
				String max = JOptionPane.showInputDialog(this, "Max R" + i);
				try {
					int m = Integer.parseInt(max);
					rmgr.setMaximum(currProcess.getProcessId(), i, m);
				} catch (NumberFormatException exp) {
					System.out.println("User Entered invalid data for max needed resources");
				}
			}

		}
	}

	public void draw(Graphics g) {
		Stroke drawingStroke;
		drawingStroke = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(drawingStroke);
		g2d.setColor(Color.darkGray);

		if (startEdgePos != null && endEdgePos != null) {
			double dx = endEdgePos.x - startEdgePos.x;
			double dy = endEdgePos.y - startEdgePos.y;
			double angle = Math.atan2(dy, dx);
			int len = (int) Math.sqrt(dx * dx + dy * dy);

			AffineTransform at = AffineTransform.getTranslateInstance(startEdgePos.x, startEdgePos.y);
			at.concatenate(AffineTransform.getRotateInstance(angle));
			g2d.transform(at);
			g2d.drawLine(0, 0, len, 0);
			g2d.fillPolygon(new int[] { len, len - 10, len - 10 }, new int[] {0, -10, 10 }, 3);
		}
	}
}
