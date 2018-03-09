/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testebtnaval;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Carlos
 */
public class ServerImplementation extends UnicastRemoteObject implements ServerInterface {

    private ArrayList<ClientInterface> clientList;
    private StringBuilder globalMessages;

    public ServerImplementation() throws RemoteException {
        super();
        clientList = new ArrayList<ClientInterface>();
        globalMessages = new StringBuilder();
    }

    public String sayHello() throws java.rmi.RemoteException {
        return ("Conectado ao servidor.");
    }

    public synchronized void registerClient(ClientInterface callbackClientObject)
            throws java.rmi.RemoteException {
        // store the callback object into the vector
        if (!(clientList.contains(callbackClientObject))) {
            clientList.add(callbackClientObject);
            //System.out.println("Novo cliente registrado! ");
            this.sendGlobalMessage("O jogador " + callbackClientObject.GetName()+ " entrou na sala.");
            //doCallbacks();
        } // end if
    }

    public synchronized void unregisterForCallback(ClientInterface callbackClientObject)
            throws java.rmi.RemoteException {
        if (clientList.remove(callbackClientObject)) {
            //System.out.println("Cliente desregistrado ");
            this.sendGlobalMessage("O jogador " + callbackClientObject.GetName()+ " saiu na sala.");
        } else {
            System.out.println("Desregistro: o cliente não foi registrado.");
        }
    }

    private synchronized void doCallbacks() throws java.rmi.RemoteException {
        // Criar callback para cada cliente registrado
        System.out.println("**************************************\n" + "Iniciando callbacks ---");
        for (int i = 0; i < clientList.size(); i++) {
            System.out.println("Fazendo o " + (i + 1) + "º callback\n");
            // Converte o objeto do vetor em um objeto de callback
            ClientInterface nextClient = (ClientInterface) clientList.get(i);
            // Invoca o método de callback
            nextClient.notifyMe("Número de clientes registrados=" + clientList.size());
        }// end for
        System.out.println("********************************\n"
                + "O servidor completou os callbacks ---");
    } // doCallbacks

    @Override
    public synchronized ArrayList<ClientInterface> GetAllClients() throws RemoteException {
        return this.clientList;
    }

    @Override
    public synchronized void generateInvite(String player1, String player2) throws RemoteException {
        boolean found = false;
        ClientInterface p1, p2;
        p1 = new ClientImplementation();
        p2 = new ClientImplementation();
        for (int i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).GetName().equals(player2)) {
                p2 = clientList.get(i);
                if (clientList.get(i).Invite(player1)) {
                    System.out.println("Jogador " + player2 + " aceitou o convite de " + player1);
                    for (int j = 0; j < clientList.size(); j++) {
                        if (clientList.get(j).GetName().equals(player1)) {
                            p1 = clientList.get(j);
                            break;
                        }
                    }
                    p1.setInGame(true);
                    p2.setInGame(true);
                    ServerBtImplementation svBt = new ServerBtImplementation(p1, p2);
                    break;
                }
                found = true;
            }
        }
        if (!found) {
            for (int i = 0; i < clientList.size(); i++) {
                if (clientList.get(i).GetName().equals(player1)) {
                    clientList.get(i).InvalidInvite();
                    break;
                }
            }
        }
    }

    @Override
    public void sendGlobalMessage(String message) throws RemoteException {
        globalMessages.append(message + "\n");
        for (int i = 0; i < clientList.size(); i++) {
            clientList.get(i).updateGlobalChat(globalMessages.toString());
        }
    }
}

