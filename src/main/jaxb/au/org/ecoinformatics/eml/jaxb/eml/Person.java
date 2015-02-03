//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.03 at 05:40:36 PM CST 
//


package au.org.ecoinformatics.eml.jaxb.eml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Person complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Person">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="salutation" type="{eml://ecoinformatics.org/resource-2.1.1}i18nNonEmptyStringType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="givenName" type="{eml://ecoinformatics.org/resource-2.1.1}i18nNonEmptyStringType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="surName" type="{eml://ecoinformatics.org/resource-2.1.1}i18nNonEmptyStringType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Person", namespace = "eml://ecoinformatics.org/party-2.1.1", propOrder = {
    "salutation",
    "givenName",
    "surName"
})
public class Person {

    protected List<I18NNonEmptyStringType> salutation;
    protected List<I18NNonEmptyStringType> givenName;
    @XmlElement(required = true)
    protected I18NNonEmptyStringType surName;

    /**
     * Gets the value of the salutation property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the salutation property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSalutation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link I18NNonEmptyStringType }
     * 
     * 
     */
    public List<I18NNonEmptyStringType> getSalutation() {
        if (salutation == null) {
            salutation = new ArrayList<I18NNonEmptyStringType>();
        }
        return this.salutation;
    }

    /**
     * Gets the value of the givenName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the givenName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGivenName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link I18NNonEmptyStringType }
     * 
     * 
     */
    public List<I18NNonEmptyStringType> getGivenName() {
        if (givenName == null) {
            givenName = new ArrayList<I18NNonEmptyStringType>();
        }
        return this.givenName;
    }

    /**
     * Gets the value of the surName property.
     * 
     * @return
     *     possible object is
     *     {@link I18NNonEmptyStringType }
     *     
     */
    public I18NNonEmptyStringType getSurName() {
        return surName;
    }

    /**
     * Sets the value of the surName property.
     * 
     * @param value
     *     allowed object is
     *     {@link I18NNonEmptyStringType }
     *     
     */
    public void setSurName(I18NNonEmptyStringType value) {
        this.surName = value;
    }

    public Person withSalutation(I18NNonEmptyStringType... values) {
        if (values!= null) {
            for (I18NNonEmptyStringType value: values) {
                getSalutation().add(value);
            }
        }
        return this;
    }

    public Person withSalutation(Collection<I18NNonEmptyStringType> values) {
        if (values!= null) {
            getSalutation().addAll(values);
        }
        return this;
    }

    public Person withGivenName(I18NNonEmptyStringType... values) {
        if (values!= null) {
            for (I18NNonEmptyStringType value: values) {
                getGivenName().add(value);
            }
        }
        return this;
    }

    public Person withGivenName(Collection<I18NNonEmptyStringType> values) {
        if (values!= null) {
            getGivenName().addAll(values);
        }
        return this;
    }

    public Person withSurName(I18NNonEmptyStringType value) {
        setSurName(value);
        return this;
    }

}
