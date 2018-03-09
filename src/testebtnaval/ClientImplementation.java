/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testebtnaval;

import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

/**
 *
 * @author Carlos
 */
public class ClientImplementation extends UnicastRemoteObject implements ClientInterface {

    public String name;
    public ClientPrincipal mainScreen;
    public TelaTeste battleScreen;
    private boolean ready = false;
    private boolean inGame = false;

    public void setName(String nome) throws RemoteException {
        this.name = nome;
    }

    public ClientImplementation() throws RemoteException {
        super();
    }

    public String GetName() throws RemoteException {
        return this.name;
    }

    public String notifyMe(String message) {
        String returnMessage = "Call back received: " + message;
        System.out.println(returnMessage);
        return returnMessage;
    }

    @Override
    public boolean Invite(String player) throws RemoteException {
        int dialogResult = JOptionPane.showConfirmDialog(null, "O jogador " + player + " te convidou para o jogo, você aceita?", "Convite", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void InvalidInvite() throws RemoteException {
        //JOptionPane.showMessageDialog(null,"O jogador que você enviou convite não está mais disponível.");
        System.out.println("O Jogador não está mais disponível");
    }

    @Override
    public void StartBattle(ServerBtInterface server) throws RemoteException {
        TelaTeste tela = new TelaTeste(server, this);
        battleScreen = tela;
        tela.setVisible(true);
    }

    @Override
    public void RestartBattle() throws RemoteException {
        TelaTeste tela = new TelaTeste(battleScreen.servidor, this);
        battleScreen.setVisible(false);
        battleScreen.dispose();
        battleScreen = tela;
        tela.setVisible(true);
        this.ready = false;
        this.inGame = true;
    }

    @Override
    public void HideMainScreen() throws RemoteException {
        mainScreen.setVisible(false);
        //mainScreen.dispose();
    }

    @Override
    public void ShowMainScren() throws RemoteException {
        mainScreen.setVisible(true);
        battleScreen.setVisible(false);
        battleScreen.dispose();
        battleScreen = null;
        this.ready = false;
        this.inGame = false;
    }

    @Override
    public void setMainScreen(ClientPrincipal screen) throws RemoteException {
        this.mainScreen = screen;
    }

    @Override
    public void setReady(boolean ready) throws RemoteException {
        this.ready = ready;
    }

    @Override
    public boolean getReady() throws RemoteException {
        return ready;
    }

    @Override
    public boolean receiveAttack(int linha, int coluna) throws RemoteException {
        if (battleScreen.getTileOccupied(linha, coluna)) {
            return true;
        }
        return false;
    }

    @Override
    public void setFields(boolean value) throws RemoteException {
        battleScreen.setFields(value);
    }

    @Override
    public void setEnemyFields(boolean value) throws RemoteException {
        battleScreen.setEnemyFields(value);
    }

    @Override
    public void setInGame(boolean inGame) throws RemoteException {
        this.inGame = inGame;
    }

    @Override
    public boolean getInGame() throws RemoteException {
        return this.inGame;
    }

    @Override
    public void updateChat(String messages) throws RemoteException {
        battleScreen.UpdateChat(messages);
    }

    @Override
    public void updateGlobalChat(String messages) throws RemoteException {
        mainScreen.updateChat(messages);
    }

    @Override
    public void setStatus(String status) throws RemoteException {
        battleScreen.setStatus(status);
    }

    @Override
    public void setFieldsValue(int linha, int coluna, Color color) throws RemoteException {
        battleScreen.setFieldValue(linha, coluna, color);
    }

    @Override
    public void setLife(int life) throws RemoteException {
        battleScreen.qtdeVida = life;
    }

    @Override
    public int getLife() throws RemoteException {
        return battleScreen.qtdeVida;
    }

    @Override
    public boolean PlayAgain() throws RemoteException {
        int dialogResult = JOptionPane.showConfirmDialog(null, "A partida acabou\nVocê marcou " + battleScreen.getScore() + " pontos, deseja jogar novamente?", "Fim de Jogo", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void MessageDialog(String message) throws RemoteException {
        JOptionPane.showMessageDialog(null, message, "!", INFORMATION_MESSAGE);
    }

}
