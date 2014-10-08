package client.java;

import java.rmi.*;
import java.io.*;
import server.*;

public class RMIClient 
{
	public static void main(String args[]) 
	{
		try 
		{
			String hostId = "localhost";
			String regPort = "2222";
			if (args.length >= 2) 
			{
				hostId = args[0];
				regPort = args[1];
			}
			Waypoint waypoint;
			// Note client uses the remote interface (WaypointServer) not the
			// implementation class (WaypointServerImpl).
			WaypointServer server;
			server = (WaypointServer) Naming.lookup("rmi://" + hostId + ":"
					+ regPort + "/WaypointServer");
			System.out.println("Client obtained remote object reference to"
					+ " the WaypointServer");
			BufferedReader stdin = new BufferedReader(new InputStreamReader(
					System.in));
			String inStr;
			do 
			{
				System.out.print("Waypoint name or end>");
				inStr = stdin.readLine();
				if (inStr.equalsIgnoreCase("End"))
					break;
				// get the (serializable) employee object from the waypoint
				// server
				waypoint = server.getWaypoint(inStr);
				// Note that getWaypoint() runs on server; getName() runs in
				// client
				System.out.println(waypoint.name);
			} 
			while (true);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
