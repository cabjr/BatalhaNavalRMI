/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testebtnaval;

import java.awt.Color;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Carlos
 */
public interface ServerBtInterface extends Remote {
    
    public void Start() throws RemoteException;
    public void StartBattle(ClientInterface client) throws RemoteException;
    public boolean Attack(ClientInterface client, int linha, int coluna) throws RemoteException;
    public void setFields(ClientInterface client, boolean value) throws RemoteException;
    public void setEnemyFields(ClientInterface client, boolean value) throws RemoteException;
    public void setEnemyFieldsValue(ClientInterface client, int linha, int coluna, Color color) throws RemoteException;
    public int getOtherPlayerLife(ClientInterface client) throws RemoteException;
    public void End(ClientInterface client) throws RemoteException;
    public void Restart() throws RemoteException;
    public void sendMessage(String message) throws RemoteException;
    public void setOtherPlayerStatus(ClientInterface client, String status) throws RemoteException;
    public void GiveUp(ClientInterface client) throws RemoteException;
    
}
