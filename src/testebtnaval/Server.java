/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testebtnaval;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Carlos
 */
public class Server {

    public static void main(String args[]) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        String portNum, registryURL;
        try {
            portNum = "7171";
            int RMIPortNum = Integer.parseInt(portNum);

            startRegistry(RMIPortNum);

            ServerImplementation exportedObj = new ServerImplementation();
            registryURL = "rmi://localhost:" + portNum + "/callback";

            Naming.rebind(registryURL, exportedObj);
            System.out.println("Servidor online.");
        }// end try
        catch (Exception re) {
            System.out.println("Exception in Server.main: " + re);
        } // end catch
    } // end main

    // This method starts a RMI registry on the local host, if
    // it does not already exists at the specified port number.
    private static void startRegistry(int RMIPortNum) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(RMIPortNum);
            registry.list();
            // This call will throw an exception
            // if the registry does not already exist
        } catch (RemoteException e) {
            // No valid registry at that port.
            Registry registry = LocateRegistry.createRegistry(RMIPortNum);
        }
    } // end startRegistry
}
