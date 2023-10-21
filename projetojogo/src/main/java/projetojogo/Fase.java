package projetojogo;


import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.io.InputStream;
import java.util.TimerTask;
import java.util.Random;
import java.util.Timer;


public class Fase extends JFrame {
    private Lixeira papelLixeira, plasticoLixeira, vidroLixeira, metaisLixeira;
    private Lixo lixoAtual;
    private int tipoLixoAtual;
    private JLabel pontuacaoLabel;
    private int pontuacao;
    private JPanel jogoPanel;
    private boolean existeLixo;
    private Timer gameTimer;
    private JLabel tempoLabel;
    
    //window
    public Fase() {
        setTitle("Reciclagem");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setResizable(false);
        existeLixo = false;
        
        //game duration plus score 
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            private int timeRemaining = 60; //game duration

            @Override
            public void run() {
                if (timeRemaining <= 0) {
                    timeGameOver();
                } 
                else if(pontuacao < 0) {
                	scoreGameOver();
                }
                else {
                    timeRemaining--;
                    tempoLabel.setText("TEMPO: " + timeRemaining);
                }
            }
            
        }, 1000, 1000);
    
        //timer cancel if close 
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
				gameTimer.cancel();
                dispose();
            }
        });
        
        //trash images
        papelLixeira = new Lixeira("Lixeira de Papel", getClass().getResource("/lixopapel.png").getPath(), 100, 400, "Papel");
        plasticoLixeira = new Lixeira("Lixeira de Plástico", getClass().getResource("/lixoplastico.png").getPath(), 350, 400, "Plástico");
        vidroLixeira = new Lixeira("Lixeira de Vidro", getClass().getResource("/lixovidro.png").getPath(), 600, 400, "Vidro");
        metaisLixeira = new Lixeira("Lixeira de Metal", getClass().getResource("/lixometal.png").getPath(), 850, 400, "Metal");
        
        //score info
        pontuacaoLabel = new JLabel("SCORE: 0");
        pontuacaoLabel.setBounds(50, 250, 150, 30);
        pontuacaoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        pontuacaoLabel.setForeground(Color.WHITE);
        
        //time info
        final int tempoRestante = 60; //game duration
        tempoLabel = new JLabel("TEMPO: " + tempoRestante);
        tempoLabel.setBounds(50, 300, 150, 30);
        tempoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        tempoLabel.setForeground(Color.WHITE);

        //background
        jogoPanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon(getClass().getResource("/fundo.jpg")).getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        
        jogoPanel.setLayout(null);
        jogoPanel.add(papelLixeira);
        jogoPanel.add(plasticoLixeira);
        jogoPanel.add(vidroLixeira);
        jogoPanel.add(metaisLixeira);
        jogoPanel.add(pontuacaoLabel);   
        jogoPanel.add(tempoLabel);
        
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (!existeLixo) {
                    Random random = new Random();
                    int tipoLixo;
                    do {
                        tipoLixo = random.nextInt(8);                                           
                    } while (tipoLixo == tipoLixoAtual); //different material from the previous
                    tipoLixoAtual = tipoLixo;

                    //generate random places 
                    int x = random.nextInt(getWidth() - 500);
                    int y = random.nextInt(getHeight() - 500);

                    switch (tipoLixo) {
                    case 0:
                    	lixoAtual = new Lixo("Papel","src/main/resources/papel.png", x, y);                        break;
                    case 1:
                    	lixoAtual = new Lixo("Plástico","src/main/resources/plastico.png", x, y);                        break;
                    case 2:
                    	lixoAtual = new Lixo("Vidro","src/main/resources/vidro.png", x, y);                        break;
                    case 3:
                    	lixoAtual = new Lixo("Lata","src/main/resources/lata.png", x, y);
                    	break;
                    case 4:
                    	lixoAtual = new Lixo("Papelao","src/main/resources/papelao.png", x, y);
                    	break;
                    case 5:
                    	lixoAtual = new Lixo("Sache","src/main/resources/sache.png", x, y);
                    	break;
                    case 6:
                    	lixoAtual = new Lixo("Lampada","src/main/resources/lampada.png", x, y);
                    	break;
                    case 7:
                        lixoAtual = new Lixo("Sacola","src/main/resources/sacola.png", x, y);
                        break;
                    default:
                        return;
                }


                    jogoPanel.add(lixoAtual);
                    jogoPanel.repaint();
                    existeLixo = true;
                }
            }
        }, 0, 1000);
        add(jogoPanel);
        setVisible(true);
    }
     
    //trash info
    private class Lixeira extends JLabel {
        public Lixeira(String texto, String imagem, int x, int y, String tipoLixo) {
            setText(texto);
            setBounds(x, y, 300, 300);
            setOpaque(false);
            setIcon(new ImageIcon(imagem));
            setVerticalTextPosition(SwingConstants.BOTTOM);
            setTransferHandler(new LixoTransferHandler(this));
        }
    }

    //materials info
    public class Lixo extends JLabel {
        private boolean isDragging;

    	public Lixo(String texto, String imagem, int x, int y) {
    		setBounds(x, y, 300, 300);
    	    setIcon(new ImageIcon(imagem));
    	    setTransferHandler(new LixoTransferHandler(this));    	    
    	    setOpaque(false); //not opaque   	  
    	    setForeground(new Color(0, 0, 0, 0)); //set text color to transparent
    	    setText(texto); //set JLabel text with material name
            isDragging = false;
    	    
    	    addMouseListener(new MouseAdapter() {
    	        public void mousePressed(MouseEvent e) {
                    isDragging = true;
    	            JComponent c = (JComponent) e.getSource();
    	            TransferHandler handler = c.getTransferHandler();
    	            handler.exportAsDrag(c, e, TransferHandler.MOVE);
    	        }    	    
    	    });
    	    
    	    addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent e) {
                    if (isDragging) {
                        JComponent c = (JComponent) e.getSource();
                        TransferHandler handler = c.getTransferHandler();
                        handler.exportAsDrag(c, e, TransferHandler.MOVE);
                    }
                }
            });
    	}

		public Lixo(String texto, InputStream resourceAsStream, int x, int y) {
			// TODO Auto-generated constructor stub
		}
    }

    private class LixoTransferHandler extends TransferHandler {
        public LixoTransferHandler(JLabel lixeira) {
        }

        //move material
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        public Transferable createTransferable(JComponent c) {
            return new StringSelection(((JLabel) c).getText());
        }

        //remove material after moved
        public void exportDone(JComponent c, Transferable t, int action) {
            if (action == MOVE) {
                Container parent = c.getParent();
                if (parent != null) {
                    parent.remove(c);
                    parent.repaint();
                    existeLixo = false;
                }
            }
        }

        public boolean canImport(TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        public boolean importData(TransferSupport support) {
            if (!canImport(support)) {
                return false;
            	}
            
            Transferable transferable = support.getTransferable();
            String data;
            try {
                data = (String) transferable.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                return false;
            }

            //points and message
            Component component = support.getComponent();
            if (component instanceof JLabel) {
                JLabel lixeira = (JLabel) component;
                if (lixeira.equals(papelLixeira)) {
                    if (data.equals("Papel")) {
                        pontuacao += 5;
                       
                    } else if(data.equals("Papelao")){
                    	pontuacao += 5;
                       
                    }
                    else {
                        pontuacao -=5;
                        
                    }
                } else if (lixeira == plasticoLixeira) {
                    if (data.equals("Plástico")) {
                        pontuacao += 5;
                     
                    } else if(data.equals("Sacola")){
                    	pontuacao += 5;
                       
                    }
                    else {
                    	pontuacao -=5;
                    	
                    }
                } else if (lixeira == vidroLixeira) {
                    if (data.equals("Vidro")) {
                        pontuacao +=5;
                      
                    } else if(data.equals("Lampada")){
                    	pontuacao += 5;
                      
                    }
                    else {
                        pontuacao -=5;
                        
                    }
                }else if (lixeira == metaisLixeira) {
                        if (data.equals("Lata")) {
                            pontuacao +=5;
                           
                        } else if(data.equals("Sache")){
                        	pontuacao += 5;
                          
                        }
                        else {
                            pontuacao -=5;
                           
                        } 
                    }                                                                            
                pontuacaoLabel.setText("SCORE: " + pontuacao);
                }

                return true;
        }
    }
    
    //game over by time
    private void timeGameOver() {
        gameTimer.cancel();        
        JOptionPane.showMessageDialog(this, "Jogo Finalizado! \n" + "Sua pontuação foi de " + pontuacao + " pontos!");
        
        //call menu when it ends
        Menu telamenu = new Menu();
        telamenu.setVisible(true);
        dispose();
                
        TimerTask closeWindowTask = new TimerTask() {
            @Override
            public void run() {
                Fase.this.dispose();
            }
        };
    
        Timer closeWindowTimer = new Timer();
        closeWindowTimer.schedule(closeWindowTask, 3000);
    }   
    
    //game over by score
    private void scoreGameOver() {
        gameTimer.cancel();        
        JOptionPane.showMessageDialog(this, "Jogo Finalizado!\nSua pontuação foi abaixo de 0!");
        
        //call menu when it ends
        Menu telamenu = new Menu();
        telamenu.setVisible(true);
        dispose();
                
        TimerTask closeWindowTask = new TimerTask() {
            @Override
            public void run() {
                Fase.this.dispose();
            }
        };
    
        Timer closeWindowTimer = new Timer();
        closeWindowTimer.schedule(closeWindowTask, 3000);
    }  

    //menu
    public void exibirTelaMenu() {
    	Menu.Menu();
    }

    public static void main(String[] args) {
        new Fase();
        new Timer();
        Fase fase = new Fase();
        fase.exibirTelaMenu();
    }
}