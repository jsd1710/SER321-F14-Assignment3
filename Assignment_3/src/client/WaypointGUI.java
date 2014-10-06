package client;

import java.rmi.*;
import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import server.*;
import java.text.DecimalFormat;

/**
 * Copyright (c) 2014 Tim Lindquist, Software Engineering, Arizona State
 * University at the Polytechnic campus
 * <p/>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation version 2 of the License.
 * <p/>
 * This program is distributed in the hope that it will be useful, but without
 * any warranty or fitness for a particular purpose.
 * <p/>
 * Please review the GNU General Public License at:
 * http://www.gnu.org/licenses/gpl-2.0.html see also:
 * https://www.gnu.org/licenses/gpl-faq.html so you are aware of the terms and
 * your rights with regard to this software. Or, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301,USA
 * <p/>
 * Purpose: Java client UI for Waypoint management. This class creates Gui
 * components for a UI to manage waypoints. This software is meant to run on
 * OSX, and Windows Cygwin using g++.
 * <p/>
 * Ser321 Principles of Distributed Software Systems
 * 
 * @see <a
 *      href="http://pooh.poly.asu.edu/Cst420">http://pooh.poly.asu.edu/Cst420</a>
 * @see <a href="../../JavaWaypointGUI.png">JavaWaypointGUI.png</a>
 * @author Tim Lindquist (Tim.Lindquist@asu.edu) CIDSE - Software Engineering
 *         Ira Fulton Schools of Engineering, ASU Polytechnic
 * @file WaypointGUI.java
 * @date September, 2014
 * @license See above
 **/
public class WaypointGUI extends JFrame implements ActionListener, ItemListener
{
	protected ArrayList<String> waypointsList;
	protected String hostId;
	protected String regPort;
	WaypointServer server;
	private DecimalFormat format = new DecimalFormat("#.000");

	/**
	 * frWps is the JComboBox in the upper left of the waypoint browser.
	 */
	protected JComboBox frWps;

	/**
	 * toWps is the JComboBox just above the Distance and Bearing JButton in the
	 * waypoint browser.
	 */
	protected JComboBox toWps;

	/**
	 * latIn is the JTextField labeled lat in the waypoint browser.
	 */
	protected JTextField latIn;

	/**
	 * lonIn is the JTextField labeled lon in the waypoint browser.
	 */
	protected JTextField lonIn;

	/**
	 * eleIn is the JTextField labeled ele in the waypoint browser.
	 */
	protected JTextField eleIn;

	/**
	 * nameIn is the JTextField labeled name in the waypoint browser.
	 */
	protected JTextField nameIn;

	/**
	 * distBearIn is the JTextField to the right of the Distance and Bearing
	 * button in the waypoint browser. The field is for displaying the distance
	 * and bearing between from and to waypoints.
	 */
	protected JTextField distBearIn;

	/**
	 * addrIn is the JTextArea to the right of addr label. Its for entering and
	 * displaying a waypoint's address.
	 */
	protected JTextArea addrIn;

	/**
	 * removeWPButt is the JButton just below the to waypoint drop-down. When
	 * the user clicks Remove Waypoint, the waypoint named in the nameIn
	 * JTextField should be removed from the server.
	 */
	protected JButton removeWPButt;

	/**
	 * addWPButt is the JButton labeled Add Waypoint. When the user clicks Add
	 * Waypoint, the current values of the fields on the right of the GUI are
	 * used to create and register a new waypoint with the server
	 */
	protected JButton addWPButt;

	/**
	 * modWPButt is the JButton labeled Modify Waypoint When the user clicks
	 * Modify Waypoint, the fields on the right side of the GUI are used modify
	 * an existing waypoint. The name of a Waypoint cannot be modified.
	 */
	protected JButton modWPButt;

	/**
	 * getAddrButt is the JButton labeled Get Addr for lat/lon. This button will
	 * be used in a later assignment. When the user clicks this button, the
	 * client uses a web service to obtain the street address of the specified
	 * lat/lon.
	 */
	protected JButton getAddrButt;

	/**
	 * getLatLonButt is the JButton labeled Get lat/lon for Addr. This button
	 * will be used in a later assignment. When the user clicks this button, the
	 * client uses a web service to obtain the latitude and longitude of the
	 * address specified in the address text area.
	 */
	protected JButton getLatLonButt;

	/**
	 * distBearButt is the JButton bottom button. When the user clicks Distance
	 * and Bearing, the direction and distance between the from waypoint and the
	 * to waypoint should be displayed in the distBearIn text field.
	 */
	protected JButton distBearButt;

	private JLabel latLab, lonLab, eleLab, nameLab, addrLab, fromLab, toLab;

	public WaypointGUI(String hostId, String regPort, String title) 
	{
		super(title);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		try 
		{
			this.hostId = hostId;
			this.regPort = regPort;
			// Note client uses the remote interface (WaypointServer) not the
			// implementation class (WaypointServerImpl).
			server = (WaypointServer) Naming.lookup("rmi://" + hostId + ":"
					+ regPort + "/WaypointServer");
			System.out.println("Client obtained remote object reference to"
					+ " the WaypointServer at:\n" + "rmi://" + hostId + ":"
					+ regPort + "/WaypointServer");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		Toolkit tk = Toolkit.getDefaultToolkit();

		getContentPane().setLayout(null);
		setSize(500, 350);

		frWps = new JComboBox(); // From Waypoints List
		frWps.setBounds(40, 10, 160, 25);
		getContentPane().add(frWps);
		frWps.addItem("from waypoint");
		frWps.setSelectedIndex(0);
		fromLab = new JLabel("from");
		fromLab.setBounds(10, 10, 30, 25);
		getContentPane().add(fromLab);

		toWps = new JComboBox();
		toWps.setBounds(40, 45, 160, 25);
		getContentPane().add(toWps);
		toWps.addItem("to waypoint");
		toWps.setSelectedIndex(0);
		toLab = new JLabel("to");
		toLab.setBounds(10, 45, 30, 25);
		getContentPane().add(toLab);

		removeWPButt = new JButton("Remove Waypoint");
		removeWPButt.setBounds(20, 80, 180, 25);
		removeWPButt.setActionCommand("Remove");
		getContentPane().add(removeWPButt);

		addWPButt = new JButton("Add Waypoint");
		addWPButt.setBounds(20, 115, 180, 25);
		addWPButt.setActionCommand("Add");
		getContentPane().add(addWPButt);

		modWPButt = new JButton("Modify Waypoint");
		modWPButt.setBounds(20, 150, 180, 25);
		modWPButt.setActionCommand("Modify");
		getContentPane().add(modWPButt);

		getAddrButt = new JButton("Get Addr for lat/lon");
		getAddrButt.setBounds(20, 185, 180, 25);
		getAddrButt.setActionCommand("GetAddr");
		getContentPane().add(getAddrButt);

		getLatLonButt = new JButton("Get lat/lon for Addr");
		getLatLonButt.setBounds(20, 220, 180, 25);
		getLatLonButt.setActionCommand("GetLatLon");
		getContentPane().add(getLatLonButt);

		distBearButt = new JButton("Distance and Bearing");
		distBearButt.setBounds(15, 260, 190, 25);
		distBearButt.setActionCommand("Distance");
		getContentPane().add(distBearButt);

		latIn = new JTextField("lat");
		latIn.setBounds(250, 10, 230, 25);
		getContentPane().add(latIn);
		latLab = new JLabel("lat");
		latLab.setBounds(225, 10, 25, 25);
		getContentPane().add(latLab);

		lonIn = new JTextField("lon");
		lonIn.setBounds(250, 45, 230, 25);
		getContentPane().add(lonIn);
		lonLab = new JLabel("lon");
		lonLab.setBounds(225, 45, 25, 25);
		getContentPane().add(lonLab);

		eleIn = new JTextField("ele");
		eleIn.setBounds(250, 80, 230, 25);
		getContentPane().add(eleIn);
		eleLab = new JLabel("ele");
		eleLab.setBounds(225, 80, 25, 25);
		getContentPane().add(eleLab);

		nameIn = new JTextField("name");
		nameIn.setBounds(250, 115, 230, 25);
		getContentPane().add(nameIn);
		nameLab = new JLabel("name");
		nameLab.setBounds(210, 115, 35, 25);
		getContentPane().add(nameLab);

		addrIn = new JTextArea("addr");
		addrIn.setBounds(250, 150, 230, 70);
		getContentPane().add(addrIn);
		addrLab = new JLabel("addr");
		addrLab.setBounds(210, 150, 35, 25);
		getContentPane().add(addrLab);

		distBearIn = new JTextField("dist/bearing");
		distBearIn.setBounds(225, 260, 255, 25);
		getContentPane().add(distBearIn);
		
		removeWPButt.addActionListener(this);
		addWPButt.addActionListener(this);
		modWPButt.addActionListener(this);
		getAddrButt.addActionListener(this);
		getLatLonButt.addActionListener(this);
		distBearButt.addActionListener(this);
		frWps.addItemListener(this);
		toWps.addItemListener(this);
		
		buildWaypointsList();

		setVisible(true);
	}

	private void buildWaypointsList() 
	{
		try 
		{
			waypointsList = new ArrayList<String>(server.getWaypointList());
			System.out.println("Successful Waypoint grab.");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		if (!waypointsList.isEmpty())
		{
			frWps.removeAllItems();
			for (int i = 0; i < waypointsList.size(); i++) // Adding From Waypoints
			{
				frWps.addItem(waypointsList.get(i));	
			}
			System.out.println("Rebuilt 'From' Waypoints.");
			
			toWps.removeAllItems();
			for (int i = 0; i < waypointsList.size(); i++) // Adding To Waypoints
			{
				toWps.addItem(waypointsList.get(i));
			}
			System.out.println("Rebuilt 'To' Waypoints.");
		}
		else
		{
			frWps.removeAllItems();
			frWps.addItem("No Waypoints");
			toWps.removeAllItems();
			toWps.addItem("No Waypoints");
			System.out.println("There are no waypoints.");
		}
	}
	public void actionPerformed(ActionEvent ae) 
	{
		String action = ae.getActionCommand();
		if (action.equals("Remove")) 
		{
			try 
			{
				server.removeWaypoint(frWps.getSelectedItem().toString());
				System.out.println("Successfully removed: " + frWps.getSelectedItem().toString());
		
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			buildWaypointsList();
		}
		if (action.equals("Add")) 
		{
			try 
			{
				server.addWaypoint(
						Double.parseDouble(latIn.getText()), 
						Double.parseDouble(lonIn.getText()), 
						Double.parseDouble(eleIn.getText()), 
						nameIn.getText()
						);
				System.out.println("Successfully added: " + nameIn.getText());
				
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			buildWaypointsList();
		}
		if (action.equals("Modify"))
		{
			try 
			{
				server.modifyWaypoint(
						Double.parseDouble(latIn.getText()), 
						Double.parseDouble(lonIn.getText()), 
						Double.parseDouble(eleIn.getText()), 
						nameIn.getText()
						);
				System.out.println("Successfully modified: " + nameIn.getText());
				
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			buildWaypointsList();
		}
		if (action.equals("Distance"))
		{
			try 
			{
				Waypoint w1 = server.getWaypoint(frWps.getSelectedItem().toString());
				Waypoint w2 = server.getWaypoint(toWps.getSelectedItem().toString());
				distBearIn.setText(format.format(server.getDistanceGCTo(w1, w2)) + "/" + format.format(server.getBearingGCInitTo(w1, w2)));
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public void itemStateChanged(ItemEvent e) 
	{
		if(e.getStateChange() == ItemEvent.SELECTED)
		{
			String waypointItem = (String)e.getItem();
			try 
			{
				Waypoint waypointSelected = server.getWaypoint(waypointItem);
				latIn.setText(String.valueOf(waypointSelected.lat));
				lonIn.setText(String.valueOf(waypointSelected.lon));
				eleIn.setText(String.valueOf(waypointSelected.ele));
				nameIn.setText(waypointSelected.name);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}


	public static void main(String args[]) 
	{
		String hostId = "localhost";
		String regPort = "2222";
		if (args.length >= 2) 
		{
			hostId = args[0];
			regPort = args[1];
		}
		System.setSecurityManager(new RMISecurityManager());
		WaypointGUI waypointclient = new WaypointGUI(hostId, regPort, "JACOB");
	}
}
