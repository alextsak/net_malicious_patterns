/**
 * MaliciousPatterns.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.webserver.server;

public class MaliciousPatterns  implements java.io.Serializable {
    private int ipposition;

    private java.lang.String[] newPatterns;

    public MaliciousPatterns() {
    }

    public MaliciousPatterns(
           int ipposition,
           java.lang.String[] newPatterns) {
           this.ipposition = ipposition;
           this.newPatterns = newPatterns;
    }


    /**
     * Gets the ipposition value for this MaliciousPatterns.
     * 
     * @return ipposition
     */
    public int getIpposition() {
        return ipposition;
    }


    /**
     * Sets the ipposition value for this MaliciousPatterns.
     * 
     * @param ipposition
     */
    public void setIpposition(int ipposition) {
        this.ipposition = ipposition;
    }


    /**
     * Gets the newPatterns value for this MaliciousPatterns.
     * 
     * @return newPatterns
     */
    public java.lang.String[] getNewPatterns() {
        return newPatterns;
    }


    /**
     * Sets the newPatterns value for this MaliciousPatterns.
     * 
     * @param newPatterns
     */
    public void setNewPatterns(java.lang.String[] newPatterns) {
        this.newPatterns = newPatterns;
    }

    public java.lang.String getNewPatterns(int i) {
        return this.newPatterns[i];
    }

    public void setNewPatterns(int i, java.lang.String _value) {
        this.newPatterns[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MaliciousPatterns)) return false;
        MaliciousPatterns other = (MaliciousPatterns) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.ipposition == other.getIpposition() &&
            ((this.newPatterns==null && other.getNewPatterns()==null) || 
             (this.newPatterns!=null &&
              java.util.Arrays.equals(this.newPatterns, other.getNewPatterns())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += getIpposition();
        if (getNewPatterns() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getNewPatterns());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getNewPatterns(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MaliciousPatterns.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.webserver.com/", "maliciousPatterns"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ipposition");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ipposition"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("newPatterns");
        elemField.setXmlName(new javax.xml.namespace.QName("", "newPatterns"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
