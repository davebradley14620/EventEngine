/**
 * FeatureMatchingSelectionMethod.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package edu.usc.webgis;

public class FeatureMatchingSelectionMethod implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected FeatureMatchingSelectionMethod(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _FeatureClassBased = "FeatureClassBased";
    public static final java.lang.String _UncertaintySingleFeatureArea = "UncertaintySingleFeatureArea";
    public static final java.lang.String _UncertaintyMultiFeatureGraviational = "UncertaintyMultiFeatureGraviational";
    public static final java.lang.String _UncertaintyMultiFeatureTopological = "UncertaintyMultiFeatureTopological";
    public static final FeatureMatchingSelectionMethod FeatureClassBased = new FeatureMatchingSelectionMethod(_FeatureClassBased);
    public static final FeatureMatchingSelectionMethod UncertaintySingleFeatureArea = new FeatureMatchingSelectionMethod(_UncertaintySingleFeatureArea);
    public static final FeatureMatchingSelectionMethod UncertaintyMultiFeatureGraviational = new FeatureMatchingSelectionMethod(_UncertaintyMultiFeatureGraviational);
    public static final FeatureMatchingSelectionMethod UncertaintyMultiFeatureTopological = new FeatureMatchingSelectionMethod(_UncertaintyMultiFeatureTopological);
    public java.lang.String getValue() { return _value_;}
    public static FeatureMatchingSelectionMethod fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        FeatureMatchingSelectionMethod enumeration = (FeatureMatchingSelectionMethod)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static FeatureMatchingSelectionMethod fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(FeatureMatchingSelectionMethod.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://webgis.usc.edu/", "FeatureMatchingSelectionMethod"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
