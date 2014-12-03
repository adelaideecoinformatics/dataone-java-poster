//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.02 at 04:58:44 PM CST 
//


package au.org.ecoinformatics.eml.jaxb;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FunctionType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="FunctionType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="download"/>
 *     &lt;enumeration value="information"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "FunctionType", namespace = "eml://ecoinformatics.org/resource-2.1.1")
@XmlEnum
public enum FunctionType {

    @XmlEnumValue("download")
    DOWNLOAD("download"),
    @XmlEnumValue("information")
    INFORMATION("information");
    private final String value;

    FunctionType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FunctionType fromValue(String v) {
        for (FunctionType c: FunctionType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
