//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.19 at 11:10:18 AM CST 
//


package au.org.ecoinformatics.eml.jaxb.eml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GeometryType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GeometryType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Point"/>
 *     &lt;enumeration value="LineString"/>
 *     &lt;enumeration value="LinearRing"/>
 *     &lt;enumeration value="Polygon"/>
 *     &lt;enumeration value="MultiPoint"/>
 *     &lt;enumeration value="MultiLineString"/>
 *     &lt;enumeration value="MultiPolygon"/>
 *     &lt;enumeration value="MultiGeometry"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GeometryType", namespace = "eml://ecoinformatics.org/spatialVector-2.1.1")
@XmlEnum
public enum GeometryType {

    @XmlEnumValue("Point")
    POINT("Point"),
    @XmlEnumValue("LineString")
    LINE_STRING("LineString"),
    @XmlEnumValue("LinearRing")
    LINEAR_RING("LinearRing"),
    @XmlEnumValue("Polygon")
    POLYGON("Polygon"),
    @XmlEnumValue("MultiPoint")
    MULTI_POINT("MultiPoint"),
    @XmlEnumValue("MultiLineString")
    MULTI_LINE_STRING("MultiLineString"),
    @XmlEnumValue("MultiPolygon")
    MULTI_POLYGON("MultiPolygon"),
    @XmlEnumValue("MultiGeometry")
    MULTI_GEOMETRY("MultiGeometry");
    private final String value;

    GeometryType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GeometryType fromValue(String v) {
        for (GeometryType c: GeometryType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
