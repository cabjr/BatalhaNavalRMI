/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testebtnaval;

import java.awt.Color;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos
 */
public class ServerBtImplementation extends UnicastRemoteObject implements ServerBtInterface {

    StringBuilder messages;
    ClientInterface player1;
    ClientInterface player2;

    public ServerBtImplementation() throws RemoteException {
    }

    public ServerBtImplementation(ClientInterface player1, ClientInterface player2) throws RemoteException {
        this.player1 = player1;
        this.player2 = player2;
        messages = new StringBuilder();
        this.Start();
    }

    @Override
    public void Start() throws RemoteException {
        player1.StartBattle(this);
        player2.StartBattle(this);
        player2.HideMainScreen();
        player1.HideMainScreen();
        this.sendMessage("Defina o posicionamento dos seus barcos para que a partida inicie.");
    }

    @Override
    public boolean Attack(ClientInterface client, int linha, int coluna) throws RemoteException {
        if (this.player1.equals(client)) {
            return player2.receiveAttack(linha, coluna);
        } else {
            return player1.receiveAttack(linha, coluna);
        }
    }

    @Override
    public void End(ClientInterface client) throws RemoteException {
        if (client.equals(player1)) {

            setEnemyFields(player1, false);
            setEnemyFields(player2, false);
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        player1.setStatus("Parabéns! Você ganhou :)");
                        player2.setStatus("Parabéns! Você perdeu, otário");
                        Thread.sleep(1000);
                        Restart();
                    } catch (RemoteException ex) {
                        Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            t.start();
        } else {
            setEnemyFields(player1, false);
            setEnemyFields(player2, false);
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        player1.setStatus("Parabéns! Você perdeu, otário");
                        player2.setStatus("Parabéns! Você ganhou :)");
                        Thread.sleep(1000);
                        Restart();
                    } catch (RemoteException ex) {
                        Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            t.start();
        }

    }

    @Override
    public void StartBattle(ClientInterface client) throws RemoteException {
        if (player1.equals(client)) {
            player1.setReady(true);
            player1.setFields(false);
        } else {
            player2.setReady(true);
            player2.setFields(false);
        }
        if (player1.getReady() && player2.getReady()) {
            System.out.println("Jogadores prontos para a batalha");
            this.sendMessage("Jogadores prontos.");
            this.sendMessage(player1.GetName() + " começa.");
            player2.setStatus("Aguardando turno do outro jogador.");
            player1.setEnemyFields(true);
            //player2.setEnemyFields(false);
        }
    }

    @Override
    public void setFields(ClientInterface client, boolean value) throws RemoteException {
        if (client.equals(player1)) {
            player2.setFields(value);
        } else {
            player1.setFields(value);
        }
    }

    @Override
    public void sendMessage(String message) throws RemoteException {
        messages.append(message + "\n");
        player1.updateChat(messages.toString());
        player2.updateChat(messages.toString());
    }

    @Override
    public void setEnemyFields(ClientInterface client, boolean value) throws RemoteException {
        if (client.equals(player1)) {
            player2.setEnemyFields(value);
            player2.setStatus("É o seu turno");
            player1.setStatus("Aguardando turno do outro jogador.");
        } else {
            player1.setEnemyFields(value);
            player1.setStatus("É o seu turno");
            player2.setStatus("Aguardando turno do outro jogador.");
        }
    }

    @Override
    public void setEnemyFieldsValue(ClientInterface client, int linha, int coluna, Color color) throws RemoteException {
        if (client.equals(player1)) {
            player2.setFieldsValue(linha, coluna, color);
        } else {
            player1.setFieldsValue(linha, coluna, color);
        }
    }

    @Override
    public int getOtherPlayerLife(ClientInterface client) throws RemoteException {
        if (client.equals(player1)) {
            return player2.getLife();
        } else {
            return player1.getLife();
        }
    }

    @Override
    public void setOtherPlayerStatus(ClientInterface client, String status) throws RemoteException {
        if (client.equals(player1)) {
            player2.setStatus(status);
        } else {
            player1.setStatus(status);
        }
    }

    @Override
    public void Restart() throws RemoteException {
        boolean answer1 = player1.PlayAgain(), answer2 = player2.PlayAgain();
        if (answer1 && answer2)// SE OS DOIS QUISEREM JOGAR NOVAMENTE
        {
            player1.RestartBattle();
            player2.RestartBattle();
            this.messages = new StringBuilder();
            this.sendMessage("Defina o posicionamento dos seus barcos para que a partida inicie.");
        } else if (answer1 && !answer2) // SE O 1 QUISER JOGAR NOVAMENTE E O 2 NAO
        {

            player1.ShowMainScren();
            player2.ShowMainScren();
            player1.MessageDialog("O jogador 2 não deseja jogar novamente.");
        } else if (!answer1 && answer2) // SE O 2 QUISER JOGAR NOVAMENTE E O 1 NAO
        {
            player1.ShowMainScren();
            player2.ShowMainScren();
            player2.MessageDialog("O jogador 1 não deseja jogar novamente.");
        } else //SE NENHUM QUISER JOGAR NOVAMENTE
        {
            player1.ShowMainScren();
            player2.ShowMainScren();
        }
    }

    @Override
    public void GiveUp(ClientInterface client) throws RemoteException {
        if (client.equals(player1)) {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        player1.ShowMainScren();
                        player2.ShowMainScren();
                        Thread.sleep(1000);
                        player2.MessageDialog("O outro jogador saiu da partida.");
                    } catch (RemoteException ex) {
                        Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            t.start();
        } else {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    try {
                        player1.ShowMainScren();
                        player2.ShowMainScren();
                        Thread.sleep(1000);
                        player1.MessageDialog("O outro jogador saiu da partida.");
                    } catch (RemoteException ex) {
                        Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            t.start();
        }
    }
}
