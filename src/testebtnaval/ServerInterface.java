/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testebtnaval;

import java.rmi.Remote;
import java.util.ArrayList;

/**
 *
 * @author Carlos
 */
public interface ServerInterface extends Remote {

    public String sayHello() throws java.rmi.RemoteException;

    public void registerClient(ClientInterface callbackClientObject) throws java.rmi.RemoteException;
    
    public ArrayList<ClientInterface> GetAllClients() throws java.rmi.RemoteException;
    
    public void unregisterForCallback(ClientInterface callbackClientObject) throws java.rmi.RemoteException;
    
    public void generateInvite(String player1, String player2) throws java.rmi.RemoteException;
    
    public void sendGlobalMessage(String message) throws java.rmi.RemoteException;

}
