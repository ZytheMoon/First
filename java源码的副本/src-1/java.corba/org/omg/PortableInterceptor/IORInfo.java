package org.omg.PortableInterceptor;


/**
* org/omg/PortableInterceptor/IORInfo.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from t:/workspace/corba/src/java.corba/share/classes/org/omg/PortableInterceptor/Interceptors.idl
* Tuesday, December 19, 2017 6:16:17 PM PST
*/


/**
   * Provides the server-side ORB service with access to the applicable 
   * policies during IOR construction and the ability to add components. 
   * The ORB passes an instance of its implementation of this interface as 
   * a parameter to <code>IORInterceptor.establish_components</code>.
   *
   * @see IORInterceptor
   */
public interface IORInfo extends IORInfoOperations, org.omg.CORBA.Object, org.omg.CORBA.portable.IDLEntity 
{
} // interface IORInfo
