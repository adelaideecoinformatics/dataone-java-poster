//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.03 at 05:40:32 PM CST 
//


package au.org.ecoinformatics.eml.jaxb.sysmeta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * .. _PID documentation: http://mule1.dataone.org/ArchitectureDocs-current/design/PIDs.html
 *       
 * 
 * <p>Java class for Identifier complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Identifier">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://ns.dataone.org/service/types/v1>NonEmptyNoWhitespaceString800">
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Identifier", propOrder = {
    "value"
})
@XmlRootElement(name = "identifier")
public class Identifier {

    @XmlValue
    protected String value;

    /**
     * A NonEmptyNoWhitespaceString800 is a NonEmptyString800
     *       string that doesn't allow whitespace characters (space, tab, newline,
     *       carriage return). Unicode whitespace characters outside of the ASCII
     *       character set need to be checked programmatically.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    public Identifier withValue(String value) {
        setValue(value);
        return this;
    }

}
