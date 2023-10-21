package projetojogo;



import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.BoxLayout;
import java.lang.System;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

public class Menu extends JFrame {

    private BackgroundPanel backgroundPanel;
    
    //method to fase
    public static void Menu() {
        System.out.println("Exibindo tela de menu...");
    }

    public Menu() {
        //window
        setTitle("Reciclagem");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);

        //title
        backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel("Aprendendo a Reciclar");
        Font titleFont = new Font("Arial", Font.BOLD, 60);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleFont);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        //buttons
        JButton iniciarJogoButton = new JButton("Iniciar Jogo");
    
        JButton instrucoesButton = new JButton("Instruções");
        JButton sairButton = new JButton("Sair");
        
        //buttons size
        Dimension buttonSize = new Dimension(250, 40);
        iniciarJogoButton.setPreferredSize(buttonSize);
        instrucoesButton.setPreferredSize(buttonSize);
        sairButton.setPreferredSize(buttonSize);       
               
        //buttons place
        Component verticalSpace = Box.createVerticalStrut(100);


        iniciarJogoButton.setAlignmentX(CENTER_ALIGNMENT);
        instrucoesButton.setAlignmentX(CENTER_ALIGNMENT);
        sairButton.setAlignmentX(CENTER_ALIGNMENT);

        iniciarJogoButton.addActionListener(e -> iniciarJogo());
        instrucoesButton.addActionListener(e -> exibirInstrucoes());
        sairButton.addActionListener(e -> System.exit(0));

        backgroundPanel.add(Box.createVerticalGlue());
        backgroundPanel.add(titleLabel);
        backgroundPanel.add(Box.createVerticalGlue());
        backgroundPanel.add(iniciarJogoButton);
        backgroundPanel.add(Box.createVerticalStrut(10));
        backgroundPanel.add(instrucoesButton);
        backgroundPanel.add(Box.createVerticalStrut(10));
        backgroundPanel.add(sairButton);
        backgroundPanel.add(Box.createVerticalGlue());
        backgroundPanel.add(verticalSpace);


        add(backgroundPanel);
        
        // Move setVisible(true) to the end
        setVisible(true);
    }

    //start button
    private void iniciarJogo() {
        JOptionPane.showMessageDialog(this, "Jogo iniciado. Boa sorte!");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Fase fase = new Fase(); 
               
                dispose();
            }
        });
    }

    //info button
    private void exibirInstrucoes() {
        JOptionPane.showMessageDialog(this, "Arraste o objeto até a lixeira correta utilizando o mouse!\nSe fizer menos de 0 pontos você perde!\nO jogo tem duração de 1 minuto!\nBoa sorte!"); 
    }
    
    //background
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
        	ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("fundo.jpg");
 
               try {
                    backgroundImage = ImageIO.read(inputStream);
                } catch (IOException e) {
                    // Lida com erros de carregamento da imagem
                    e.printStackTrace();
                }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu());
    }
}
