/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataContract;

/**
 *
 * @author AndyChen
 */
public abstract class Config {
    public static final String SERVER_NAME = "server";
    public static final String SERVER_ADDRESS = "localhost";
    public static final int SERVER_PORT_TCP = 8888;
    public static final int SERVER_PORT_UDP = 8899;
    public static final int PORT_TCP_TIMEOUT = 3000;
    public static final int PORT_UDP_TIMEOUT = 3000;
    public static final int UDP_INTERVAL = 3000;
}
