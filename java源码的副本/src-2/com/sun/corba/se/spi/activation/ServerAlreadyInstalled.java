package com.sun.corba.se.spi.activation;


/**
* com/sun/corba/se/spi/activation/ServerAlreadyInstalled.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from ../../../../src/share/classes/com/sun/corba/se/spi/activation/activation.idl
* Friday, February 3, 2012 8:32:56 PM PST
*/

public final class ServerAlreadyInstalled extends org.omg.CORBA.UserException
{
  public int serverId = (int)0;

  public ServerAlreadyInstalled ()
  {
    super(ServerAlreadyInstalledHelper.id());
  } // ctor

  public ServerAlreadyInstalled (int _serverId)
  {
    super(ServerAlreadyInstalledHelper.id());
    serverId = _serverId;
  } // ctor


  public ServerAlreadyInstalled (String $reason, int _serverId)
  {
    super(ServerAlreadyInstalledHelper.id() + "  " + $reason);
    serverId = _serverId;
  } // ctor

} // class ServerAlreadyInstalled
