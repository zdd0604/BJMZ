package com.hjnerp.websocket;

import java.io.Reader;
import java.io.Writer;

public abstract class HjConnection {

	
	
	   /**
     * The Reader which is used for the debugger.
     */
    protected Reader reader;

    /**
     * The Writer which is used for the debugger.
     */
    protected Writer writer;
    
    /**
     * Returns true if currently connected to the XMPP server.
     * 
     * @return true if connected.
     */
    public abstract boolean isConnected();
    
    
    public abstract String getUser();
    
    public abstract String getEqu();
    
    public abstract String url();
    public abstract void connect() throws Exception;
    
    public abstract void login(String username, String Equ) throws Exception ;
    
    public abstract void sendPacket(String  packet);
    
    public abstract void disconnect( );
    
    
}
