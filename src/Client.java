//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

import com.sun.org.apache.xpath.internal.SourceTree;
import net.miginfocom.swing.MigLayout;

public class Client extends NetworkClient {

    private JLabel usernameLabel;
    private JLabel ipAddressLabel;
    private JLabel portLabel;
    private JTextField usernameField;
    private JLabel enterLabel;
    private JTextField enterField;
    private JTextArea setupArea;
    private JScrollPane chatDisplayArea;
    private JButton connectBtn;
    private JTextField ipAddressField;
    private JTextField portNoField;

    private String ipAddress;
    private int port;


    private AtomicBoolean connected = new AtomicBoolean(false);

    private String username;

    private SwingWorker worker;

    private StringBuffer sendBuffer;
    private AtomicBoolean sendData = new AtomicBoolean(false);
    private AtomicBoolean closeClient = new AtomicBoolean(false);

    public Client(String host, int port) {
        super(host, port);
    }

    public void createAndShowGUI(Client gui) {
        //logger.entering(CLASS_NAME, "createAndShowGUI");

        //logger.log(Level.INFO, "Creating GUI");
        JFrame frame = new JFrame("Client");
        frame.setSize(600, 600);
        frame.setLocation(600, 300);

        frame.setContentPane(gui.createContentPane());

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);
        //logger.exiting(CLASS_NAME, "createAndShowGUI");
    }

    public boolean isConnected(){
        return connected.get();
    }

    //Create the GUI look and feel. Init and setup up all components in the GUI.
    private JPanel createContentPane() {
        //logger.entering(CLASS_NAME, "createContentPane");
        JPanel totalGUI = new JPanel();
        totalGUI.setSize(600, 600);
        totalGUI.setLayout(new MigLayout("", "", ""));

        // The main story for the JTextArea
        String text = "";

        usernameLabel = new JLabel("Username: ");

        usernameField = new JTextField();
        usernameField.setEnabled(true);
        usernameField.setColumns(40);

        ipAddressLabel = new JLabel("Host IP Address: ");

        ipAddressField = new JTextField();
        ipAddressField.setEnabled(true);
        ipAddressField.setColumns(40);

        portLabel = new JLabel("Host Port Number: ");

        portNoField = new JTextField();
        portNoField.setEnabled(true);
        portNoField.setColumns(40);


        connectBtn = new JButton("Connect");
        connectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = usernameField.getText();
                if(username.length() > 0)
                {
                    if(ipAddressField.getText().length() > 0)
                    {
                        Client.super.setHost(ipAddressField.getText());
                        ipAddress = ipAddressField.getText();
                        ipAddressField.setEditable(false);
                    }
                    else
                    {
                        ipAddressField.setText("127.0.0.1");
                    }
                    if(portNoField.getText().length() > 0)
                    {
                        Client.super.setPort(Integer.parseInt(portNoField.getText()));
                        port = Integer.parseInt(portNoField.getText());
                    }
                    else
                    {
                        portNoField.setText("8112");
                    }
                    ipAddressField.setEditable(false);
                    portNoField.setEditable(false);
                    enterLabel.setVisible(true);
                    enterField.setVisible(true);
                    chatDisplayArea.setVisible(true);
                    enterField.setEditable(true);
                    connectBtn.setVisible(false);
                    usernameLabel.setVisible(false);
                    usernameField.setVisible(false);
                    connected.set(true);
                }
            }
        });

        enterLabel = new JLabel("Chat Input: ");
        enterLabel.setVisible(false);

        enterField = new JTextField();
        enterField.setEditable(false);
        //enterField.setColumns(40);
        enterField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendBuffer = new StringBuffer();
                sendBuffer.append(e.getActionCommand());
                sendData.set(true);
                Client.this.enterField.setText("");
            }
        });
        enterField.setVisible(false);


        //Instantiate the TextArea to suit the GUI
        setupArea = new JTextArea(text, 5, 30);
        setupArea.setEditable(false);
        setupArea.setLineWrap(true);
        setupArea.setWrapStyleWord(true);
        DefaultCaret caret = (DefaultCaret) setupArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);



        // Create the ScrollPane and instantiate it with the TextArea as an argument
        // along with two constants that define the behaviour of the scrollbars.
        chatDisplayArea = new JScrollPane(setupArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chatDisplayArea.setVisible(false);

        //Where the GUI is constructed:
        totalGUI.add(usernameLabel, "grow");
        totalGUI.add(usernameField, "span, grow");
        totalGUI.add(ipAddressLabel, "grow");
        totalGUI.add(ipAddressField, "span, grow");
        totalGUI.add(portLabel, "grow");
        totalGUI.add(portNoField, "span, grow");
        totalGUI.add(connectBtn, "span, grow");
        totalGUI.add(enterLabel, "grow");
        totalGUI.add(enterField, "span, grow");
        totalGUI.add(chatDisplayArea, "span, push, grow");


        totalGUI.setOpaque(true);
        //logger.exiting(CLASS_NAME, "createContentPane");
        return totalGUI;
    }

    @Override
    protected void handleConnection(Socket clientSocket) throws IOException {
        Socket connection = clientSocket;

        try(
                BufferedReader in = SocketUtils.getReader(connection);
                PrintWriter out = SocketUtils.getWriter(connection);

                ){
            processInputConnection(in);
            processOutputConnection(out);
            while(closeClient.get() == false);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void processInputConnection(BufferedReader br) {

        worker = new SwingWorker<Integer, String>() {
            @Override
            protected Integer doInBackground() throws Exception {
                String str = "";
                StringBuffer sb = new StringBuffer();
                try (  BufferedReader in = br  ){
                    while(in.ready() == false) {
                        publish("Waiting on server connection ");
                        Thread.sleep(250);
                        publish("Waiting on server connection .");
                        Thread.sleep(250);
                        publish("Waiting on server connection . .");
                        Thread.sleep(250);
                        publish("Waiting on server connection . . .");
                        Thread.sleep(250);
                    }
                    while((str = in.readLine()) != null)
                    {
                        if(str.contains("<EOT>") == true)
                        {
                            publish(sb.toString());
                            sb = new StringBuffer();
                        } else {
                            sb.append(str + "\r\n");
                        }
                    }
                } catch (Exception var2) {
                    //break;
                }
                return 0;
            }

            @Override
            protected void process(List<String> chunks) {
                StringBuffer sb = new StringBuffer();
                for(String lines : chunks)
                {
                    sb.append(lines);
                }
                setupArea.setText(sb.toString());
            }

            @Override
            protected void done() {
                System.out.println("BufferedReader Thread Closed");
                setupArea.append("Connection Closed");
                closeClient.set(true);
                super.done();
            }
        };
        worker.execute();
    }

    private void processOutputConnection(PrintWriter pr)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try( PrintWriter out = pr){
                    String threadUsername = username;
                    while(closeClient.get() == false) {
                        if (sendData.get()) {
                            pr.println("<USER>" + threadUsername + "<USER>" + sendBuffer.toString());
                            sendData.set(false);
                        }
                        Thread.sleep(20);
                    }
                    System.out.println("PrintWriter Thread closed");
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
