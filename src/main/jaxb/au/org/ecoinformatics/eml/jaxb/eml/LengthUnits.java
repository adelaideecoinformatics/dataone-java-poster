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
 * <p>Java class for lengthUnits.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="lengthUnits">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="meter"/>
 *     &lt;enumeration value="nanometer"/>
 *     &lt;enumeration value="micrometer"/>
 *     &lt;enumeration value="micron"/>
 *     &lt;enumeration value="millimeter"/>
 *     &lt;enumeration value="centimeter"/>
 *     &lt;enumeration value="decimeter"/>
 *     &lt;enumeration value="dekameter"/>
 *     &lt;enumeration value="hectometer"/>
 *     &lt;enumeration value="kilometer"/>
 *     &lt;enumeration value="megameter"/>
 *     &lt;enumeration value="angstrom"/>
 *     &lt;enumeration value="inch"/>
 *     &lt;enumeration value="Foot_US"/>
 *     &lt;enumeration value="foot"/>
 *     &lt;enumeration value="Foot_Gold_Coast"/>
 *     &lt;enumeration value="fathom"/>
 *     &lt;enumeration value="nauticalMile"/>
 *     &lt;enumeration value="yard"/>
 *     &lt;enumeration value="Yard_Indian"/>
 *     &lt;enumeration value="Link_Clarke"/>
 *     &lt;enumeration value="Yard_Sears"/>
 *     &lt;enumeration value="mile"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "lengthUnits", namespace = "eml://ecoinformatics.org/spatialReference-2.1.1")
@XmlEnum
public enum LengthUnits {

    @XmlEnumValue("meter")
    METER("meter"),
    @XmlEnumValue("nanometer")
    NANOMETER("nanometer"),
    @XmlEnumValue("micrometer")
    MICROMETER("micrometer"),
    @XmlEnumValue("micron")
    MICRON("micron"),
    @XmlEnumValue("millimeter")
    MILLIMETER("millimeter"),
    @XmlEnumValue("centimeter")
    CENTIMETER("centimeter"),
    @XmlEnumValue("decimeter")
    DECIMETER("decimeter"),
    @XmlEnumValue("dekameter")
    DEKAMETER("dekameter"),
    @XmlEnumValue("hectometer")
    HECTOMETER("hectometer"),
    @XmlEnumValue("kilometer")
    KILOMETER("kilometer"),
    @XmlEnumValue("megameter")
    MEGAMETER("megameter"),
    @XmlEnumValue("angstrom")
    ANGSTROM("angstrom"),
    @XmlEnumValue("inch")
    INCH("inch"),
    @XmlEnumValue("Foot_US")
    FOOT_US("Foot_US"),
    @XmlEnumValue("foot")
    FOOT("foot"),
    @XmlEnumValue("Foot_Gold_Coast")
    FOOT_GOLD_COAST("Foot_Gold_Coast"),
    @XmlEnumValue("fathom")
    FATHOM("fathom"),
    @XmlEnumValue("nauticalMile")
    NAUTICAL_MILE("nauticalMile"),
    @XmlEnumValue("yard")
    YARD("yard"),
    @XmlEnumValue("Yard_Indian")
    YARD_INDIAN("Yard_Indian"),
    @XmlEnumValue("Link_Clarke")
    LINK_CLARKE("Link_Clarke"),
    @XmlEnumValue("Yard_Sears")
    YARD_SEARS("Yard_Sears"),
    @XmlEnumValue("mile")
    MILE("mile");
    private final String value;

    LengthUnits(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static LengthUnits fromValue(String v) {
        for (LengthUnits c: LengthUnits.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
