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
 * <p>Java class for ImagingConditionCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ImagingConditionCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="blurredimage"/>
 *     &lt;enumeration value="cloud"/>
 *     &lt;enumeration value="degradingObliquity"/>
 *     &lt;enumeration value="fog"/>
 *     &lt;enumeration value="heavySmokeorDust"/>
 *     &lt;enumeration value="night"/>
 *     &lt;enumeration value="rain"/>
 *     &lt;enumeration value="semiDarkness"/>
 *     &lt;enumeration value="shadow"/>
 *     &lt;enumeration value="snow"/>
 *     &lt;enumeration value="terrainMasking"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ImagingConditionCode", namespace = "eml://ecoinformatics.org/spatialRaster-2.1.1")
@XmlEnum
public enum ImagingConditionCode {

    @XmlEnumValue("blurredimage")
    BLURREDIMAGE("blurredimage"),
    @XmlEnumValue("cloud")
    CLOUD("cloud"),
    @XmlEnumValue("degradingObliquity")
    DEGRADING_OBLIQUITY("degradingObliquity"),
    @XmlEnumValue("fog")
    FOG("fog"),
    @XmlEnumValue("heavySmokeorDust")
    HEAVY_SMOKEOR_DUST("heavySmokeorDust"),
    @XmlEnumValue("night")
    NIGHT("night"),
    @XmlEnumValue("rain")
    RAIN("rain"),
    @XmlEnumValue("semiDarkness")
    SEMI_DARKNESS("semiDarkness"),
    @XmlEnumValue("shadow")
    SHADOW("shadow"),
    @XmlEnumValue("snow")
    SNOW("snow"),
    @XmlEnumValue("terrainMasking")
    TERRAIN_MASKING("terrainMasking");
    private final String value;

    ImagingConditionCode(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ImagingConditionCode fromValue(String v) {
        for (ImagingConditionCode c: ImagingConditionCode.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
