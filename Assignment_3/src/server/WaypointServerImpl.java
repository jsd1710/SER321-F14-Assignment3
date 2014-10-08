package server;

import java.io.*;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import org.json.*;



/**
 * Purpose: demonstrate using the RMI API Implementation of employee server -
 * create a remote server object (with a couple of employees). Register the
 * remote server object with the rmi registry.
 */
class WaypointServerImpl extends UnicastRemoteObject implements WaypointServer,
		java.io.Serializable
{
	protected Hashtable<String, Waypoint> waypointList = new Hashtable<String, Waypoint>();

	public WaypointServerImpl() throws RemoteException, Exception 
	{
		try 
		{
			BufferedReader fin = new BufferedReader(new FileReader(new File(
					"samples.txt")));
			String line;
			String[] attribute;
			Waypoint w;
			while ((line = fin.readLine()) != null) 
			{
				attribute = line.split(",");
				w = new Waypoint(
						Double.parseDouble(attribute[0]),
						Double.parseDouble(attribute[1]),
						Double.parseDouble(attribute[2]), 
						attribute[3]
						);
				waypointList.put(attribute[3], w);
			}

			fin.close();
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}

	public Waypoint getWaypoint(String name) throws RemoteException 
	{
		try 
		{
			if (waypointList.containsKey(name)) 
			{
				// System.out.println("Returning: " + name);
				return waypointList.get(name);
			}
			return null;
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}

	public ArrayList<String> getWaypointList() throws RemoteException 
	{
		try 
		{
			return Collections.list(waypointList.keys());
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}

	public void removeWaypoint(String name) throws RemoteException 
	{
		try 
		{
			if (waypointList.containsKey(name)) 
			{
				waypointList.remove(name);
				System.out.println("Removed: " + name
						+ " from the Waypoint List.");
			}
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}

	public void addWaypoint(double lat, double lon, double ele, String name)
			throws RemoteException 
	{
		try 
		{
			if (!waypointList.containsKey(name)) 
			{
				Waypoint w = new Waypoint(lat, lon, ele, name);
				waypointList.put(name, w);
				System.out.println("Added: " + name + " to the Waypoint List.");
			}
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}

	public void modifyWaypoint(double lat, double lon, double ele, String name)
			throws RemoteException 
	{
		if (waypointList.containsKey(name)) 
		{
			waypointList.remove(name);
			Waypoint w = new Waypoint(lat, lon, ele, name);
			waypointList.put(name, w);
			System.out.println("Modify: " + name
					+ " was modified in the Waypoint List.");
		} 
		else 
		{
			System.out.println("Modify: No changes were made.");
		}
	}

	public double getDistanceGCTo(Waypoint w1, Waypoint w2) throws RemoteException 
	{
		try 
		{
			return w1.distanceGCTo(w2, 0);
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}

	public double getBearingGCInitTo(Waypoint w1, Waypoint w2) throws RemoteException 
	{
		try 
		{
			return w1.bearingGCInitTo(w2, 0);
		} 
		catch (Exception e) 
		{
			throw e;
		}
	}

	public void exportToJSON() throws RemoteException
	{
		JSONArray jsonArray = new JSONArray();
		ArrayList<String> waypointArrayList = Collections.list(waypointList.keys());
		for (int i = 0; i < waypointList.size(); i++)
		{
			String name = waypointArrayList.get(i);
			Waypoint waypoint = waypointList.get(name);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("name", waypoint.name);
			jsonObject.put("lat", waypoint.lat);
			jsonObject.put("lon", waypoint.lon);
			jsonObject.put("ele", waypoint.ele);
			
			jsonArray.put(jsonObject);
		}
		
		Writer outputFile = null;
		
		try 
		{
			outputFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("waypoints.json"), "utf-8"));
			outputFile.write(jsonArray.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally 
		{
			try 
			{
				outputFile.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
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
			System.setSecurityManager(new RMISecurityManager());
			WaypointServer obj = new WaypointServerImpl();
			Naming.rebind(
					"rmi://" + hostId + ":" + regPort + "/WaypointServer", obj);
			System.out.println("Server bound in registry as: " + "rmi://"
					+ hostId + ":" + regPort + "/WaypointServer");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

}
