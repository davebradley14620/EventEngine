/**
 * GeocoderService_V02_95Locator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.usc.webgis;

public class GeocoderService_V02_95Locator extends org.apache.axis.client.Service implements edu.usc.webgis.GeocoderService_V02_95 {

    public GeocoderService_V02_95Locator() {
    }


    public GeocoderService_V02_95Locator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GeocoderService_V02_95Locator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GeocoderService_V02_95Soap
    private java.lang.String GeocoderService_V02_95Soap_address = "https://webgis.usc.edu/Services/Geocode/WebService/GeocoderService_V02_95.asmx";

    public java.lang.String getGeocoderService_V02_95SoapAddress() {
        return GeocoderService_V02_95Soap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GeocoderService_V02_95SoapWSDDServiceName = "GeocoderService_V02_95Soap";

    public java.lang.String getGeocoderService_V02_95SoapWSDDServiceName() {
        return GeocoderService_V02_95SoapWSDDServiceName;
    }

    public void setGeocoderService_V02_95SoapWSDDServiceName(java.lang.String name) {
        GeocoderService_V02_95SoapWSDDServiceName = name;
    }

    public edu.usc.webgis.GeocoderService_V02_95Soap getGeocoderService_V02_95Soap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GeocoderService_V02_95Soap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGeocoderService_V02_95Soap(endpoint);
    }

    public edu.usc.webgis.GeocoderService_V02_95Soap getGeocoderService_V02_95Soap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            edu.usc.webgis.GeocoderService_V02_95SoapStub _stub = new edu.usc.webgis.GeocoderService_V02_95SoapStub(portAddress, this);
            _stub.setPortName(getGeocoderService_V02_95SoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGeocoderService_V02_95SoapEndpointAddress(java.lang.String address) {
        GeocoderService_V02_95Soap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (edu.usc.webgis.GeocoderService_V02_95Soap.class.isAssignableFrom(serviceEndpointInterface)) {
                edu.usc.webgis.GeocoderService_V02_95SoapStub _stub = new edu.usc.webgis.GeocoderService_V02_95SoapStub(new java.net.URL(GeocoderService_V02_95Soap_address), this);
                _stub.setPortName(getGeocoderService_V02_95SoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("GeocoderService_V02_95Soap".equals(inputPortName)) {
            return getGeocoderService_V02_95Soap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://webgis.usc.edu/", "GeocoderService_V02_95");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://webgis.usc.edu/", "GeocoderService_V02_95Soap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GeocoderService_V02_95Soap".equals(portName)) {
            setGeocoderService_V02_95SoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
