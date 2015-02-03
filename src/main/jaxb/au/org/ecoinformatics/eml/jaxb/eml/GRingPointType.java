//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.03 at 05:40:36 PM CST 
//


package au.org.ecoinformatics.eml.jaxb.eml;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GRingPointType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GRingPointType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gRingLatitude">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="-90.0"/>
 *               &lt;maxInclusive value="90.0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="gRingLongitude">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               &lt;minInclusive value="-180.0"/>
 *               &lt;maxInclusive value="180.0"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GRingPointType", namespace = "eml://ecoinformatics.org/coverage-2.1.1", propOrder = {
    "gRingLatitude",
    "gRingLongitude"
})
public class GRingPointType {

    @XmlElement(required = true)
    protected BigDecimal gRingLatitude;
    @XmlElement(required = true)
    protected BigDecimal gRingLongitude;

    /**
     * Gets the value of the gRingLatitude property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGRingLatitude() {
        return gRingLatitude;
    }

    /**
     * Sets the value of the gRingLatitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGRingLatitude(BigDecimal value) {
        this.gRingLatitude = value;
    }

    /**
     * Gets the value of the gRingLongitude property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGRingLongitude() {
        return gRingLongitude;
    }

    /**
     * Sets the value of the gRingLongitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGRingLongitude(BigDecimal value) {
        this.gRingLongitude = value;
    }

    public GRingPointType withGRingLatitude(BigDecimal value) {
        setGRingLatitude(value);
        return this;
    }

    public GRingPointType withGRingLongitude(BigDecimal value) {
        setGRingLongitude(value);
        return this;
    }

}
