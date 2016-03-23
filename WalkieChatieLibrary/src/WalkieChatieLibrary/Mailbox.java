/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WalkieChatieLibrary;

import DataContract.DataTypes;
import DataContract.Letter;
import DataContract.Contact;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *
 * @author AndyChen
 */
public abstract class Mailbox implements DataTypes.MessageListener
{
    protected final Inbox inbox;
    protected final Outbox outbox;
    public final AddressBook addressBook;
    public final Contact owner;
    protected Contact recerver;
    public Queue<Letter> LetterQueue;
    
    public Mailbox(Contact ownerInfo)
    {
        owner = ownerInfo;
        
        addressBook = new AddressBook();
        LetterQueue = new LinkedList<>();
        
        outbox = new Outbox();   
        inbox = new Inbox(owner.getPort());    
        //recerver = new
        this.listeners = new ArrayList<>();
    }
    
    //start receiving messages
    public void start()
    {
        inbox.addNewMessageListener(this);
        inbox.start();
    }
    
    protected synchronized void stackLetter(Letter letter) {
        if (letter != null) {
            LetterQueue.add(letter);

            for (DataTypes.MessageListener item : listeners) {
                item.newMessageArrived();
            }
        }
    }
    
    @Override
    public abstract void newMessageArrived();
    
    private final List<DataTypes.MessageListener> listeners;

    public void addNewMessageListener(DataTypes.MessageListener listener) {
        listeners.add(listener);
    }
}
