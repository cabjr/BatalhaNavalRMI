/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testebtnaval;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import jdk.nashorn.internal.runtime.regexp.joni.constants.NodeType;

/**
 *
 * @author Carlos
 */
public class Tile extends JButton implements MouseListener, Serializable {

    private int coluna, linha;
    public boolean horizontal = true;
    public String tipo = "A";

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        this.linha = linha;
    }
    private String estado = "vazio"; // vazio, atacado, barco
    public TelaTeste tab;
    private int barco;

    public int getBarco() {
        return barco;
    }

    public void setBarco(int barco) {
        this.barco = barco;
    }

    public int getTamBarco() {
        return tamBarco;
    }

    public void setTamBarco(int tamBarco) {
        this.tamBarco = tamBarco;
    }
    private int tamBarco;

    public TelaTeste getTab() {
        return tab;
    }

    public void setTab(TelaTeste tab) {
        this.tab = tab;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean isAcabou() {
        return acabou;
    }

    public void setAcabou(boolean acabou) {
        this.acabou = acabou;
    }
    private boolean acabou = false;

    public Tile() {
        super();
    }

    public Tile(int x, int y, TelaTeste tab, boolean horizontal, String estado) {
        super();
        setOpaque(false);
        this.linha = y;
        this.coluna = x;
        addMouseListener(this);
        //this.setVisible(true);
        this.tab = tab;
        this.estado = estado;
        this.horizontal = horizontal;
        //repaint();
    }

    public boolean setBarco(int x, int y, int tam, boolean horizontal) {
        if (!verificaOcupado(x, y) && verificaComprimento(x, y, tam, horizontal) && verificaTiles(x, y, tam, horizontal)) {
            Tile barco;

            // get shipt type according to its length
            switch (tam) {
                case 2:
                    // Submarino
                    barco = new Tile(x, y, tab, horizontal, "S");
                    break;
                case 3:
                    //Contra Torpedeiro
                    barco = new Tile(x, y, tab, horizontal, "CT");
                    break;
                case 4:
                    // Navio Tanque
                    barco = new Tile(x, y, tab, horizontal, "NT");
                    break;
                case 5:
                    // Porta Aviões
                    barco = new Tile(x, y, tab, horizontal, "PA");
                    break;
                default:
                    // boat
                    barco = new Tile(x, y, tab, horizontal, "CT");
                    break;
            }

            for (int i = 0; i < tam; i++) {
                if (horizontal) {
                    setField(x + i, y, barco);
                } else {
                    setField(x, y + i, barco);
                }
            }
            return true;
        }
        return false;
    }

    public void setField(int x, int y, Tile tile) {
        if (x >= 0 && y >= 0 && x < 10 && y < 10) {
            tab.Matriz[y][x].setEnabled(false);
            tab.Matriz[y][x].setBackground(Color.RED);
            tab.Matriz[y][x].tipo = tile.estado;
            tab.Matriz[y][x].setText(tile.estado);
        }
    }

    private Tile getTile(int x, int y) {
        if (x >= 0 && y >= 0 && x < 10 && y < 10) {
            return tab.Matriz[y][x];
        }
        return null;
    }

    public boolean verificaOcupado(int x, int y) {
        if (getTile(x, y).tipo.equals("A")) {
            return false;
        } else {
            return true;
        }
        //return !(getTile(x, y) instanceof Tile);
    }

    private boolean verificaTiles(int x, int y, int length, boolean horizontal) {
        if (horizontal) {
            if (!getTile(x + length - 1, y).tipo.equals("A") || !getTile(x, y).tipo.equals("A")) {
                return false;
            }
        } else {
            if (!getTile(x , y+ length - 1).tipo.equals("A") || !getTile(x, y).tipo.equals("A")) {
                return false;
            }
        }
        for (int i = 0; i < length; i++) {
            if (horizontal) {
                if (!getTile(x + i, y).tipo.equals("A")) {
                    return false;
                }
            } else {
                if (!getTile(x, y + i).tipo.equals("A")) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean verificaComprimento(int x, int y, int tam, boolean horizontal) {
        if (x < 10 && y < 10) {
            if (horizontal) {
                if (x + tam-1 < 10) {
                    return true;
                }
            } else if (y + tam -1< 10) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.isEnabled()) {
            if (!tab.Pronto) {
                if (tab.tamanho == 2 && tab.qtdS > 0) {
                    if (setBarco(this.linha, this.coluna, tab.tamanho, horizontal)) {
                        System.out.println("Inserindo submarino");
                        tab.qtdS--;
                        tab.qtdeVida += 2;
                    }
                } else if (tab.tamanho == 3 && tab.qtdCT > 0) {
                    if (setBarco(this.linha, this.coluna, tab.tamanho, horizontal)) {
                        System.out.println("Inserindo Contra Torpedeiro");
                        tab.qtdCT--;
                        tab.qtdeVida += 3;
                    }
                } else if (tab.tamanho == 4 && tab.qtdNT > 0) {
                    if (setBarco(this.linha, this.coluna, tab.tamanho, horizontal)) {
                        System.out.println("Inserindo Navio Tanque");
                        tab.qtdNT--;
                        tab.qtdeVida += 4;
                    }
                } else if (tab.tamanho == 5 && tab.qtdPA > 0) {
                    if (setBarco(this.linha, this.coluna, tab.tamanho, horizontal)) {
                        System.out.println("Inserindo Porta Aviões");
                        tab.qtdPA--;
                        tab.qtdeVida += 5;
                    }
                }
                if (tab.qtdS == 0 && tab.qtdCT == 0 && tab.qtdNT == 0 && tab.qtdPA == 0) {
                    tab.Pronto = true;
                    tab.setStatus("Barcos inseridos");
                    //tab.setFields(false);
                    try {
                        tab.servidor.StartBattle(tab.client);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                try {
                    if (tab.servidor.Attack(tab.client, linha, coluna)) {
                        //SE ACERTAR O INIMIGO
                        tab.pontos++;
                        this.setText("B");
                        tab.setScore(tab.pontos);
                        this.setEnabled(false);
                        this.setBackground(Color.red);
                        this.repaint();
                        tab.servidor.setEnemyFieldsValue(tab.client, coluna, linha, Color.orange);
                        tab.client.setStatus("Acertou! você tem direito a mais uma jogada");
                        if (tab.pontos == tab.servidor.getOtherPlayerLife(tab.client)/*vida oponente*/) {
                            Thread t = new Thread(new Runnable() {
                                public void run() {
                                    try {
                                        Thread.sleep(300);
                                        tab.servidor.End(tab.client);
                                    } catch (RemoteException ex) {
                                        Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
                                    } catch (InterruptedException ex) {
                                        Logger.getLogger(Tile.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            });
                            t.start();
                        }
                    } else {
                        tab.setEnemyFields(false);
                        tab.setStatus("Errou! é o turno do outro jogador");
                        this.setText(tipo);
                        this.setBackground(Color.blue);
                        tab.MatrizInimigo[this.coluna][this.linha].setBackground(Color.blue);
                        tab.servidor.setEnemyFields(tab.client, true);
                        tab.servidor.setEnemyFieldsValue(tab.client, coluna, linha, Color.orange);
                        //tab.servidor
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(TelaTeste.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e
    ) {
        //System.out.println("X: "+x + " Y: " + y );
    }

    @Override
    public void mouseReleased(MouseEvent e
    ) {

    }

    @Override
    public void mouseEntered(MouseEvent e
    ) {
        //System.out.println("X: " + linha + " Y: " + coluna + "Tipo: " + tipo + "  Pontos Restantes: " + tab.qtdeVida);
    }

    @Override
    public void mouseExited(MouseEvent e
    ) {

    }

}
