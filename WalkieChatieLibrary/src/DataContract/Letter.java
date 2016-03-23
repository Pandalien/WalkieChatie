/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataContract;

import DataContract.DataTypes.MessageType;

/**
 *
 * @author AndyChen
 */
public class Letter {
    private MessageType _messageType;
    private String recipient;
    private String sender;
    private String message;
    
    public Letter()
    {
        
    }
    public Letter(DataTypes.MessageType type, String recipient, String sender, String msg)
    {
        _messageType = type;
        this.recipient = recipient;
        this.sender = sender;    
        message = msg;
    }
    
    public MessageType getMessageType()
    {
        return _messageType;    
    }
    public void setMessageType(MessageType messageType)
    {
        _messageType = messageType;    
    }
    public String getSender()
    {
        return sender;    
    }
    public void setSender(String sender)
    {
        this.sender = sender;    
    }
    public String getRecipient()
    {
        return recipient;    
    }
    public void setRecipient(String recipient)
    {
        this.recipient = recipient;    
    }
    public String getMessage()
    {
        return message;    
    }
    public void setMessage(String message)
    {
        this.message = message;    
    }
}
