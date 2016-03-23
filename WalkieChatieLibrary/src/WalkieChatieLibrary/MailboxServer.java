/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WalkieChatieLibrary;

import DataContract.Config;
import DataContract.Contact;
import DataContract.DataTypes;
import DataContract.Letter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;
import javax.swing.Timer;

/**
 *
 * @author Andy
 */
public class MailboxServer extends Mailbox{
    private Timer timer;
    
    public MailboxServer() {
        super(new Contact(Config.SERVER_NAME, Config.SERVER_ADDRESS, Config.SERVER_PORT_TCP, 0, true));
        
        // set timer to update client list
        timer = new Timer(Config.UDP_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                broadcastClients();
            }
        });
        timer.start();
    }
    
    //server to client
    private boolean forward(Letter letter)
    {
        //look up real ip address and port for recipient
        String name = letter.getRecipient();
        
        Contact recipient = addressBook.Lookup(name);
        if (recipient == null)
        {
            returnLetter(letter);
            return false;
        }
        
        letter.setRecipient(recipient.getName());
        if (!outbox.send(recipient, letter))
        {
            returnLetter(letter);
            return false;
        }
        
        return true;
    }
    
    private void returnLetter(Letter letter)
    {
        //look up real ip address and port for recipient
        String name = letter.getRecipient();
        
        String msg = "Failed to send \"" + letter.getMessage() + "\", " +
                name + " is offline or is invisible.";
        
        letter.setMessageType(DataTypes.MessageType.Message_Delivery_Failed);
        letter.setMessage(msg);
        letter.setRecipient(letter.getSender());
        
        Contact recipient = addressBook.Lookup(letter.getSender());
        if (recipient != null)
        {
            outbox.send(recipient, letter);
        }  
    }

    private synchronized void broadcast(Letter letter)
    {
        for (Map.Entry<String, Contact> entry : addressBook.map.entrySet()) {
            if (!entry.getValue().getIsOnline()) continue;
            
            letter.setRecipient(entry.getValue().getName());
            outbox.send(entry.getValue(), letter);
        }    
    }
    
    @Override
    public void newMessageArrived()
    {
        do {
            Letter letter = inbox.MessageQueue.poll();
            if (letter == null) {
                break;
            }

            DataTypes.MessageType msgType = letter.getMessageType();
            switch (msgType) {
                case Message_Individual:
                    forward(letter);
                    break;
                case Message_Broadcast:
                    broadcast(letter);
                    break;
                case Message_Delivery_Successful:
                    break;
                case Message_Delivery_Failed:
                    break;
                case User_Update:
                    break;
                case User_Login:
                    String []ports = letter.getMessage().split(":");
                    if (ports.length == 3) {
                        Contact sender = new Contact(letter.getSender(), ports[0], Integer.parseInt(ports[1]), Integer.parseInt(ports[2]), true);
                        addressBook.add(sender);
                        
                        readLetter(letter);
                    }
                    break;
                case User_Logout:
                    readLetter(letter);
                    addressBook.updateStatus(letter.getSender(), false);
                    
                    //inform others
                    timer.stop();
                    broadcastClients();
                    timer.restart();
                    
                    break;
            }
        } while (true);
    }
    
    private void readLetter(Letter letter)
    {
        Contact sender = addressBook.Lookup(letter.getSender());
        if (sender == null)
        {
            System.out.println("Unknown Message: " + letter.getMessage() );
            return;
        }
        
        
        Contact recipient = addressBook.Lookup(letter.getRecipient());
        if (recipient == null) recipient = owner;

        String msg = letter.getMessage();
        StringBuilder log = new StringBuilder();
        log.append(letter.getMessageType().toString() + ": ");
        log.append(sender.getName() + "@" + sender.getAddress() + ":" + sender.getPort());
        log.append(" --> " + recipient.getName() + "@" + recipient.getAddress() + ":" + recipient.getPort());
        log.append(" Content: " + msg);
        
        System.out.println(log.toString());
    }
    
    private synchronized void broadcastClients()
    {
        String msg;
        for (Map.Entry<String, Contact> you : addressBook.map.entrySet()) {
            if (!you.getValue().getIsOnline()) continue;
            
            for (Map.Entry<String, Contact> other : addressBook.map.entrySet()) {
                if (you.getValue().getName().equals(other.getValue().getName())) continue;
                
                msg = other.getValue().getIsOnline()? "1:":"0:";
                msg += other.getValue().getName();
                sendUDP(you.getValue(), msg);
            }
        } 
    }
    
    private void sendUDP(Contact receiver, String msg)
    {
        DatagramSocket socket = null;
        InetAddress hostAddress = null;
        
        try {
            socket = new DatagramSocket();
            byte[] data = msg.getBytes();
            hostAddress = InetAddress.getByName(receiver.getAddress());
            
            DatagramPacket sendDatagram = new DatagramPacket(data,
                    data.length, hostAddress, receiver.getPortUdp());
            socket.send(sendDatagram);
        } catch (SocketException e) {
            System.err.println("Unable to create socket: " + e);
        }catch (UnknownHostException e) {
            System.err.println("Unknown host: " + e);
        } catch (IOException e) {
            System.err.println("IOException: " + e);
        }
    }
}
