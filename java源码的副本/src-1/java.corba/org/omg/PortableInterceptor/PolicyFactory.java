package org.omg.PortableInterceptor;


/**
* org/omg/PortableInterceptor/PolicyFactory.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from t:/workspace/corba/src/java.corba/share/classes/org/omg/PortableInterceptor/Interceptors.idl
* Tuesday, December 19, 2017 6:16:17 PM PST
*/


/**
   * Enables policy types to be constructed using 
   * <code>CORBA.ORB.create_policy</code>.
   * <p>
   * A portable ORB service implementation registers an instance of the 
   * <code>PolicyFactory</code> interface during ORB initialization in order 
   * to enable its policy types to be constructed using 
   * <code>CORBA.ORB.create_policy</code>. The POA is required to preserve 
   * any policy which is registered with <code>ORBInitInfo</code> in this 
   * manner.
   *
   * @see ORBInitInfo#register_policy_factory
   */
public interface PolicyFactory extends PolicyFactoryOperations, org.omg.CORBA.Object, org.omg.CORBA.portable.IDLEntity 
{
} // interface PolicyFactory
