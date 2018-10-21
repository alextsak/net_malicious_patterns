/**
 * StatisticalReports.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.webserver.server;

public class StatisticalReports  implements java.io.Serializable {
    private java.lang.String UID;

    private java.lang.String local_Time;

    private java.lang.String device;

    private java.lang.String deviceIP;

    private java.lang.String pattern;

    private java.lang.String frequency;

    public StatisticalReports() {
    }

    public StatisticalReports(
           java.lang.String UID,
           java.lang.String local_Time,
           java.lang.String device,
           java.lang.String deviceIP,
           java.lang.String pattern,
           java.lang.String frequency) {
           this.UID = UID;
           this.local_Time = local_Time;
           this.device = device;
           this.deviceIP = deviceIP;
           this.pattern = pattern;
           this.frequency = frequency;
    }


    /**
     * Gets the UID value for this StatisticalReports.
     * 
     * @return UID
     */
    public java.lang.String getUID() {
        return UID;
    }


    /**
     * Sets the UID value for this StatisticalReports.
     * 
     * @param UID
     */
    public void setUID(java.lang.String UID) {
        this.UID = UID;
    }


    /**
     * Gets the local_Time value for this StatisticalReports.
     * 
     * @return local_Time
     */
    public java.lang.String getLocal_Time() {
        return local_Time;
    }


    /**
     * Sets the local_Time value for this StatisticalReports.
     * 
     * @param local_Time
     */
    public void setLocal_Time(java.lang.String local_Time) {
        this.local_Time = local_Time;
    }


    /**
     * Gets the device value for this StatisticalReports.
     * 
     * @return device
     */
    public java.lang.String getDevice() {
        return device;
    }


    /**
     * Sets the device value for this StatisticalReports.
     * 
     * @param device
     */
    public void setDevice(java.lang.String device) {
        this.device = device;
    }


    /**
     * Gets the deviceIP value for this StatisticalReports.
     * 
     * @return deviceIP
     */
    public java.lang.String getDeviceIP() {
        return deviceIP;
    }


    /**
     * Sets the deviceIP value for this StatisticalReports.
     * 
     * @param deviceIP
     */
    public void setDeviceIP(java.lang.String deviceIP) {
        this.deviceIP = deviceIP;
    }


    /**
     * Gets the pattern value for this StatisticalReports.
     * 
     * @return pattern
     */
    public java.lang.String getPattern() {
        return pattern;
    }


    /**
     * Sets the pattern value for this StatisticalReports.
     * 
     * @param pattern
     */
    public void setPattern(java.lang.String pattern) {
        this.pattern = pattern;
    }


    /**
     * Gets the frequency value for this StatisticalReports.
     * 
     * @return frequency
     */
    public java.lang.String getFrequency() {
        return frequency;
    }


    /**
     * Sets the frequency value for this StatisticalReports.
     * 
     * @param frequency
     */
    public void setFrequency(java.lang.String frequency) {
        this.frequency = frequency;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StatisticalReports)) return false;
        StatisticalReports other = (StatisticalReports) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.UID==null && other.getUID()==null) || 
             (this.UID!=null &&
              this.UID.equals(other.getUID()))) &&
            ((this.local_Time==null && other.getLocal_Time()==null) || 
             (this.local_Time!=null &&
              this.local_Time.equals(other.getLocal_Time()))) &&
            ((this.device==null && other.getDevice()==null) || 
             (this.device!=null &&
              this.device.equals(other.getDevice()))) &&
            ((this.deviceIP==null && other.getDeviceIP()==null) || 
             (this.deviceIP!=null &&
              this.deviceIP.equals(other.getDeviceIP()))) &&
            ((this.pattern==null && other.getPattern()==null) || 
             (this.pattern!=null &&
              this.pattern.equals(other.getPattern()))) &&
            ((this.frequency==null && other.getFrequency()==null) || 
             (this.frequency!=null &&
              this.frequency.equals(other.getFrequency())));
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
        if (getUID() != null) {
            _hashCode += getUID().hashCode();
        }
        if (getLocal_Time() != null) {
            _hashCode += getLocal_Time().hashCode();
        }
        if (getDevice() != null) {
            _hashCode += getDevice().hashCode();
        }
        if (getDeviceIP() != null) {
            _hashCode += getDeviceIP().hashCode();
        }
        if (getPattern() != null) {
            _hashCode += getPattern().hashCode();
        }
        if (getFrequency() != null) {
            _hashCode += getFrequency().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StatisticalReports.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://server.webserver.com/", "statisticalReports"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("UID");
        elemField.setXmlName(new javax.xml.namespace.QName("", "UID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("local_Time");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Local_Time"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("device");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Device"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("deviceIP");
        elemField.setXmlName(new javax.xml.namespace.QName("", "DeviceIP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("pattern");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Pattern"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("frequency");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Frequency"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
