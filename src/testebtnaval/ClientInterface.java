/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testebtnaval;

import java.awt.Color;
import java.rmi.RemoteException;
import javax.swing.JFrame;

/**
 *
 * @author Carlos
 */
public interface ClientInterface extends java.rmi.Remote {

    public String notifyMe(String resultado) throws RemoteException;

    public String GetName() throws RemoteException;

    public void setName(String nome) throws RemoteException;

    public boolean Invite(String player) throws RemoteException;

    public void InvalidInvite() throws RemoteException;

    public void setMainScreen(ClientPrincipal screen) throws RemoteException;

    public void HideMainScreen() throws RemoteException;

    public void ShowMainScren() throws RemoteException;

    public void StartBattle(ServerBtInterface server) throws RemoteException;

    public void RestartBattle() throws RemoteException;

    public void setReady(boolean ready) throws RemoteException;

    public boolean getReady() throws RemoteException;

    public void setInGame(boolean inGame) throws RemoteException;

    public boolean getInGame() throws RemoteException;

    public boolean receiveAttack(int linha, int coluna) throws RemoteException;

    public void setStatus(String status) throws RemoteException;

    public void setLife(int life) throws RemoteException;

    public int getLife() throws RemoteException;

    public void setFields(boolean value) throws RemoteException;

    public void setFieldsValue(int linha, int coluna, Color color) throws RemoteException;

    public void setEnemyFields(boolean value) throws RemoteException;

    public void updateChat(String messages) throws RemoteException;

    public void MessageDialog(String message) throws RemoteException;

    public void updateGlobalChat(String messages) throws RemoteException;

    public boolean PlayAgain() throws RemoteException;

}
