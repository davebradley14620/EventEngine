/**
 * GeocoderService_V02_95Soap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.usc.webgis;

public interface GeocoderService_V02_95Soap extends java.rmi.Remote {
    public edu.usc.webgis.WebServiceGeocodeResult geocodeAddressParsed(java.lang.String number, java.lang.String numberFractional, java.lang.String preDirectional, java.lang.String preQualifier, java.lang.String preType, java.lang.String preArticle, java.lang.String name, java.lang.String suffix, java.lang.String postArticle, java.lang.String postQualifier, java.lang.String postDirectional, java.lang.String suiteType, java.lang.String suiteNumber, java.lang.String city, java.lang.String state, java.lang.String zip, java.lang.String apiKey, double version, boolean shouldCalculateCensus, boolean shouldReturnReferenceGeometry, boolean shouldNotStoreTransactionDetails) throws java.rmi.RemoteException;
    public edu.usc.webgis.WebServiceGeocodeResult geocodeAddressParsedInternational(java.lang.String number, java.lang.String numberFractional, java.lang.String preDirectional, java.lang.String preQualifier, java.lang.String preType, java.lang.String preArticle, java.lang.String name, java.lang.String suffix, java.lang.String postArticle, java.lang.String postQualifier, java.lang.String postDirectional, java.lang.String suiteType, java.lang.String suiteNumber, java.lang.String city, java.lang.String state, java.lang.String zip, java.lang.String country, java.lang.String apiKey, double version, boolean shouldCalculateCensus, boolean shouldReturnReferenceGeometry, boolean shouldNotStoreTransactionDetails) throws java.rmi.RemoteException;
    public edu.usc.webgis.WebServiceGeocodeResult geocodeAddressParsedInternationalAdvanced(java.lang.String number, java.lang.String numberFractional, java.lang.String preDirectional, java.lang.String preQualifier, java.lang.String preType, java.lang.String preArticle, java.lang.String name, java.lang.String suffix, java.lang.String postArticle, java.lang.String postQualifier, java.lang.String postDirectional, java.lang.String suiteType, java.lang.String suiteNumber, java.lang.String city, java.lang.String state, java.lang.String zip, java.lang.String country, boolean shouldUseRelaxation, boolean shouldUseSubstring, boolean shouldUseSoundex, boolean shouldUseUncertaintyHierarchy, java.lang.String relaxableAttributes, java.lang.String soundexAttributes, java.lang.String referenceSources, java.lang.String apiKey, double version, boolean shouldCalculateCensus, boolean shouldReturnReferenceGeometry, boolean shouldNotStoreTransactionDetails) throws java.rmi.RemoteException;
    public edu.usc.webgis.WebServiceGeocodeResult geocodeAddressNonParsed(java.lang.String streetAddress, java.lang.String city, java.lang.String state, java.lang.String zip, java.lang.String apiKey, double version, boolean shouldCalculateCensus, boolean shouldReturnReferenceGeometry, boolean shouldNotStoreTransactionDetails) throws java.rmi.RemoteException;
    public edu.usc.webgis.WebServiceGeocodeResult geocodeAddressNonParsedInternational(java.lang.String streetAddress, java.lang.String city, java.lang.String state, java.lang.String zip, java.lang.String country, java.lang.String apiKey, double version, boolean shouldCalculateCensus, boolean shouldReturnReferenceGeometry, boolean shouldNotStoreTransactionDetails) throws java.rmi.RemoteException;
    public edu.usc.webgis.WebServiceGeocodeResult geocodeAddressNonParsedInternationalAdvanced(java.lang.String streetAddress, java.lang.String city, java.lang.String state, java.lang.String zip, java.lang.String country, boolean shouldUseRelaxation, boolean shouldUseSubstring, boolean shouldUseSoundex, boolean shouldUseUncertaintyHierarchy, java.lang.String relaxableAttributes, java.lang.String soundexAttributes, java.lang.String referenceSources, java.lang.String apiKey, double version, boolean shouldCalculateCensus, boolean shouldReturnReferenceGeometry, boolean shouldNotStoreTransactionDetails) throws java.rmi.RemoteException;
}
