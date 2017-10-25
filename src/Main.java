import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 8112);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                client.createAndShowGUI(client);
            }
        });
        while(client.isConnected() == false);
        client.connect();
    }
}
