import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Abhinav on 17-04-2015.
 */
public class Updater extends Thread {

    JTextPane pane;
    MulticastSocket msock;
    Integer port;
    String nickname;

    Updater(int port, JTextPane pane, String nickname) {
        this.pane = pane;
        this.port = port;
        this.nickname = nickname;
    }

    @Override
    public void run() {
        byte[] buf = new byte[256];
        try {
            msock = new MulticastSocket(port);
            InetAddress address = InetAddress.getByName("228.1.1.1");
            msock.joinGroup(address);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            DatagramPacket pk = new DatagramPacket(buf,buf.length);
            try {
                msock.receive(pk);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String msg = new String(buf,0,pk.getLength());
            String[] msga = msg.split("`");
            if(msga[0].contentEquals("msg")){
                if(msga[1].contentEquals(nickname))
                    pane.setText(pane.getText() + "You: " + msga[2] + "\n");
                else
                    pane.setText(pane.getText() + msga[1] + " says " + msga[2] + "\n");
            }
            else{
                if(msga[1].contentEquals(nickname))
                    pane.setText(pane.getText() + "You are now connected." + "\n");
                else
                    pane.setText(pane.getText() + msga[1] + msga[2] + "\n");
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
