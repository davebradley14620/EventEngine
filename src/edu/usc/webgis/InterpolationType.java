/**
 * InterpolationType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.usc.webgis;

public class InterpolationType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected InterpolationType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _Unknown = "Unknown";
    public static final java.lang.String _LinearInterpolation = "LinearInterpolation";
    public static final java.lang.String _ArealInterpolation = "ArealInterpolation";
    public static final java.lang.String _None = "None";
    public static final java.lang.String _NotAttempted = "NotAttempted";
    public static final InterpolationType Unknown = new InterpolationType(_Unknown);
    public static final InterpolationType LinearInterpolation = new InterpolationType(_LinearInterpolation);
    public static final InterpolationType ArealInterpolation = new InterpolationType(_ArealInterpolation);
    public static final InterpolationType None = new InterpolationType(_None);
    public static final InterpolationType NotAttempted = new InterpolationType(_NotAttempted);
    public java.lang.String getValue() { return _value_;}
    public static InterpolationType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        InterpolationType enumeration = (InterpolationType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static InterpolationType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(InterpolationType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://webgis.usc.edu/", "InterpolationType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
