/**
 * FeatureMatchingGeographyType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.usc.webgis;

public class FeatureMatchingGeographyType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected FeatureMatchingGeographyType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Unknown = "Unknown";
    public static final java.lang.String _GPS = "GPS";
    public static final java.lang.String _BuildingCentroid = "BuildingCentroid";
    public static final java.lang.String _BuildingDoor = "BuildingDoor";
    public static final java.lang.String _Parcel = "Parcel";
    public static final java.lang.String _StreetSegment = "StreetSegment";
    public static final java.lang.String _StreetIntersection = "StreetIntersection";
    public static final java.lang.String _StreetCentroid = "StreetCentroid";
    public static final java.lang.String _USPSZipPlus5 = "USPSZipPlus5";
    public static final java.lang.String _USPSZipPlus4 = "USPSZipPlus4";
    public static final java.lang.String _USPSZipPlus3 = "USPSZipPlus3";
    public static final java.lang.String _USPSZipPlus2 = "USPSZipPlus2";
    public static final java.lang.String _USPSZipPlus1 = "USPSZipPlus1";
    public static final java.lang.String _USPSZip = "USPSZip";
    public static final java.lang.String _ZCTAPlus5 = "ZCTAPlus5";
    public static final java.lang.String _ZCTAPlus4 = "ZCTAPlus4";
    public static final java.lang.String _ZCTAPlus3 = "ZCTAPlus3";
    public static final java.lang.String _ZCTAPlus2 = "ZCTAPlus2";
    public static final java.lang.String _ZCTAPlus1 = "ZCTAPlus1";
    public static final java.lang.String _ZCTA = "ZCTA";
    public static final java.lang.String _City = "City";
    public static final java.lang.String _ConsolidatedCity = "ConsolidatedCity";
    public static final java.lang.String _MinorCivilDivision = "MinorCivilDivision";
    public static final java.lang.String _CountySubRegion = "CountySubRegion";
    public static final java.lang.String _County = "County";
    public static final java.lang.String _State = "State";
    public static final java.lang.String _Country = "Country";
    public static final java.lang.String _Unmatchable = "Unmatchable";
    public static final FeatureMatchingGeographyType Unknown = new FeatureMatchingGeographyType(_Unknown);
    public static final FeatureMatchingGeographyType GPS = new FeatureMatchingGeographyType(_GPS);
    public static final FeatureMatchingGeographyType BuildingCentroid = new FeatureMatchingGeographyType(_BuildingCentroid);
    public static final FeatureMatchingGeographyType BuildingDoor = new FeatureMatchingGeographyType(_BuildingDoor);
    public static final FeatureMatchingGeographyType Parcel = new FeatureMatchingGeographyType(_Parcel);
    public static final FeatureMatchingGeographyType StreetSegment = new FeatureMatchingGeographyType(_StreetSegment);
    public static final FeatureMatchingGeographyType StreetIntersection = new FeatureMatchingGeographyType(_StreetIntersection);
    public static final FeatureMatchingGeographyType StreetCentroid = new FeatureMatchingGeographyType(_StreetCentroid);
    public static final FeatureMatchingGeographyType USPSZipPlus5 = new FeatureMatchingGeographyType(_USPSZipPlus5);
    public static final FeatureMatchingGeographyType USPSZipPlus4 = new FeatureMatchingGeographyType(_USPSZipPlus4);
    public static final FeatureMatchingGeographyType USPSZipPlus3 = new FeatureMatchingGeographyType(_USPSZipPlus3);
    public static final FeatureMatchingGeographyType USPSZipPlus2 = new FeatureMatchingGeographyType(_USPSZipPlus2);
    public static final FeatureMatchingGeographyType USPSZipPlus1 = new FeatureMatchingGeographyType(_USPSZipPlus1);
    public static final FeatureMatchingGeographyType USPSZip = new FeatureMatchingGeographyType(_USPSZip);
    public static final FeatureMatchingGeographyType ZCTAPlus5 = new FeatureMatchingGeographyType(_ZCTAPlus5);
    public static final FeatureMatchingGeographyType ZCTAPlus4 = new FeatureMatchingGeographyType(_ZCTAPlus4);
    public static final FeatureMatchingGeographyType ZCTAPlus3 = new FeatureMatchingGeographyType(_ZCTAPlus3);
    public static final FeatureMatchingGeographyType ZCTAPlus2 = new FeatureMatchingGeographyType(_ZCTAPlus2);
    public static final FeatureMatchingGeographyType ZCTAPlus1 = new FeatureMatchingGeographyType(_ZCTAPlus1);
    public static final FeatureMatchingGeographyType ZCTA = new FeatureMatchingGeographyType(_ZCTA);
    public static final FeatureMatchingGeographyType City = new FeatureMatchingGeographyType(_City);
    public static final FeatureMatchingGeographyType ConsolidatedCity = new FeatureMatchingGeographyType(_ConsolidatedCity);
    public static final FeatureMatchingGeographyType MinorCivilDivision = new FeatureMatchingGeographyType(_MinorCivilDivision);
    public static final FeatureMatchingGeographyType CountySubRegion = new FeatureMatchingGeographyType(_CountySubRegion);
    public static final FeatureMatchingGeographyType County = new FeatureMatchingGeographyType(_County);
    public static final FeatureMatchingGeographyType State = new FeatureMatchingGeographyType(_State);
    public static final FeatureMatchingGeographyType Country = new FeatureMatchingGeographyType(_Country);
    public static final FeatureMatchingGeographyType Unmatchable = new FeatureMatchingGeographyType(_Unmatchable);
    public java.lang.String getValue() { return _value_;}
    public static FeatureMatchingGeographyType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        FeatureMatchingGeographyType enumeration = (FeatureMatchingGeographyType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static FeatureMatchingGeographyType fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FeatureMatchingGeographyType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://webgis.usc.edu/", "FeatureMatchingGeographyType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
