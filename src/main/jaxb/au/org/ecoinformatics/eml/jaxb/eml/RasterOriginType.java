//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.03 at 05:40:36 PM CST 
//


package au.org.ecoinformatics.eml.jaxb.eml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rasterOriginType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="rasterOriginType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Upper Left"/>
 *     &lt;enumeration value="Lower Left"/>
 *     &lt;enumeration value="Upper Right"/>
 *     &lt;enumeration value="Lower Right"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "rasterOriginType", namespace = "eml://ecoinformatics.org/spatialRaster-2.1.1")
@XmlEnum
public enum RasterOriginType {

    @XmlEnumValue("Upper Left")
    UPPER_LEFT("Upper Left"),
    @XmlEnumValue("Lower Left")
    LOWER_LEFT("Lower Left"),
    @XmlEnumValue("Upper Right")
    UPPER_RIGHT("Upper Right"),
    @XmlEnumValue("Lower Right")
    LOWER_RIGHT("Lower Right");
    private final String value;

    RasterOriginType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RasterOriginType fromValue(String v) {
        for (RasterOriginType c: RasterOriginType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
