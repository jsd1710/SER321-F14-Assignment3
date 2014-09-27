package server;

import java.rmi.*;
import java.util.*;

/**
 * Purpose: demonstrate using the RMI API
 * remote interface for the employee server.
 * @author Tim Lindquist (Tim@asu.edu), ASU Polytechnic
 * @version June 2014
 */
public interface WaypointServer extends Remote
{
   public Waypoint getWaypoint(String name) throws RemoteException;
   
   public ArrayList<String> getWaypointList() throws RemoteException;
   
   public void removeWaypoint(String name) throws RemoteException;
   
   public void addWaypoint(double lat, double lon, double ele, String name) throws RemoteException;
   
   public void modifyWaypoint(double lat, double lon, double ele, String name) throws RemoteException;

   public double getDistanceGCTo(Waypoint w1, Waypoint w2) throws RemoteException;
   
   public double getBearingGCInitTo(Waypoint w1, Waypoint w2) throws RemoteException;
}
