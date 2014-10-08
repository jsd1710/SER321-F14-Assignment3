package client.java;

import java.net.*;

/**
 * Purpose: program to print the os.name property and the hostname
 * @author Tim Lindquist (Tim@asu.edu), ASU Polytechnic
 * @version June 2014
 */
public class OSName {
   public static void main(String args[]) {
      try {
         System.out.println(System.getProperty("os.name"));
         System.out.println(
                    InetAddress.getLocalHost().getCanonicalHostName());
      }catch (Exception e) {
         e.printStackTrace();
      }
   }
}
