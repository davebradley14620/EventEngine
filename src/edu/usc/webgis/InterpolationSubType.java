/**
 * InterpolationSubType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.usc.webgis;

public class InterpolationSubType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected InterpolationSubType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Unknown = "Unknown";
    public static final java.lang.String _LinearInterpolationAddressRange = "LinearInterpolationAddressRange";
    public static final java.lang.String _LinearInterpolationUniformLot = "LinearInterpolationUniformLot";
    public static final java.lang.String _LinearInterpolationActualLot = "LinearInterpolationActualLot";
    public static final java.lang.String _LinearInterpolationMidPoint = "LinearInterpolationMidPoint";
    public static final java.lang.String _ArealInterpolationBoundingBoxCentroid = "ArealInterpolationBoundingBoxCentroid";
    public static final java.lang.String _ArealInterpolationConvexHullCentroid = "ArealInterpolationConvexHullCentroid";
    public static final java.lang.String _ArealInterpolationGeometricCentroid = "ArealInterpolationGeometricCentroid";
    public static final java.lang.String _None = "None";
    public static final java.lang.String _NotAttempted = "NotAttempted";
    public static final InterpolationSubType Unknown = new InterpolationSubType(_Unknown);
    public static final InterpolationSubType LinearInterpolationAddressRange = new InterpolationSubType(_LinearInterpolationAddressRange);
    public static final InterpolationSubType LinearInterpolationUniformLot = new InterpolationSubType(_LinearInterpolationUniformLot);
    public static final InterpolationSubType LinearInterpolationActualLot = new InterpolationSubType(_LinearInterpolationActualLot);
    public static final InterpolationSubType LinearInterpolationMidPoint = new InterpolationSubType(_LinearInterpolationMidPoint);
    public static final InterpolationSubType ArealInterpolationBoundingBoxCentroid = new InterpolationSubType(_ArealInterpolationBoundingBoxCentroid);
    public static final InterpolationSubType ArealInterpolationConvexHullCentroid = new InterpolationSubType(_ArealInterpolationConvexHullCentroid);
    public static final InterpolationSubType ArealInterpolationGeometricCentroid = new InterpolationSubType(_ArealInterpolationGeometricCentroid);
    public static final InterpolationSubType None = new InterpolationSubType(_None);
    public static final InterpolationSubType NotAttempted = new InterpolationSubType(_NotAttempted);
    public java.lang.String getValue() { return _value_;}
    public static InterpolationSubType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        InterpolationSubType enumeration = (InterpolationSubType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static InterpolationSubType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(InterpolationSubType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://webgis.usc.edu/", "InterpolationSubType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
