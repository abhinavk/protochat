import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Abhinav on 17-04-2015.
 */
public class Chat1 {
    private JPanel panel1;
    private JButton loginButton;
    private JTextField textField1;
    private JPanel login;
    private JPanel chatscreen;
    private JTextField textField2;
    private JTextField textField3;
    private JTextPane textPane1;
    private JTextField textField4;
    private JButton sendMessageButton;

    Socket socket;
    BufferedWriter writer;

    Integer port=15656;
    String nickname="Yeah";
    static JFrame frame;
    Updater updater;

    public static void main(String[] args) {
        frame = new JFrame("Chatroom P2P");
        frame.setBounds(50, 40, 640, 500);
        frame.setContentPane(new Chat1().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        frame.setVisible(true);
    }

    public Chat1() {
        chatscreen.setVisible(false);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login.setVisible(false);
                chatscreen.setVisible(true);
                if (!textField2.getText().isEmpty())
                    port = Integer.parseInt(textField2.getText());
                if (!textField3.getText().isEmpty())
                    nickname = textField3.getText();
                frame.setTitle("ChatP2P - Logged in as "+nickname);
                String msg = "con`" + nickname + "` is now connected.`";
                try {
                    DatagramPacket outPacket;
                    DatagramSocket socket = new DatagramSocket();
                    byte[] outb = msg.getBytes();
                    InetAddress address = InetAddress.getByName("228.1.1.1");
                    outPacket = new DatagramPacket(outb, outb.length, address, port);
                    socket.send(outPacket);
                } catch (IOException e1) {
                }

                updater = new Updater(port, textPane1, nickname);
                updater.start();


            }
        });

        socket = null;

        ActionListener cmnd = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = "msg`" + nickname + "`" + textField4.getText() + "`";

                try {
                    DatagramPacket outPacket;
                    DatagramSocket socket = new DatagramSocket();
                    byte[] outb = msg.getBytes();
                    InetAddress address = InetAddress.getByName("228.1.1.1");
                    outPacket = new DatagramPacket(outb, outb.length, address, port);
                    socket.send(outPacket);
                } catch (IOException e1) {
                }
                textField4.setText("");
                textField4.grabFocus();
            }
        };

        // Add the action listener to both button and textfield
        sendMessageButton.addActionListener(cmnd);
        textField4.addActionListener(cmnd);
    }
}
