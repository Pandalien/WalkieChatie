/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataContract;

import java.util.Date;

/**
 *XML User data
 * @author AndyChen
 */
public class Contact {
    public Contact(){}
    public Contact(String name, String address, int port, int portUdp, boolean isOnline)
    {
        this.name = name;
        this.address = address;
        this.port = port;
        this.portUdp = portUdp;
        this.isOnline = isOnline;
        
        lastAtiveTime = new Date();
    }
    public Date lastAtiveTime;
    
    private String name;
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name  = name;
    }
    private String address;
    public String getAddress()
    {
        return address;
    }
    public void setAddress(String address)
    {
        this.address  = address;
    }
    private int port;
    public int getPort()
    {
        return port;
    }
    public void setPort(int port)
    {
        this.port  = port;
    }
    
    private int portUdp;
    public int getPortUdp()
    {
        return portUdp;
    }
    public void setPortUdp(int port)
    {
        portUdp  = port;
    }
    
    private boolean isOnline;
    public boolean getIsOnline()
    {
        return isOnline;
    }
    public void setIsOnline(boolean online)
    {
        isOnline  = online;
    }
}
