//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.03 at 05:40:36 PM CST 
//


package au.org.ecoinformatics.eml.jaxb.eml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OnlineType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OnlineType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="onlineDescription" type="{eml://ecoinformatics.org/resource-2.1.1}i18nNonEmptyStringType" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="url" type="{eml://ecoinformatics.org/resource-2.1.1}UrlType"/>
 *           &lt;element name="connection" type="{eml://ecoinformatics.org/resource-2.1.1}ConnectionType"/>
 *           &lt;element name="connectionDefinition" type="{eml://ecoinformatics.org/resource-2.1.1}ConnectionDefinitionType"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OnlineType", namespace = "eml://ecoinformatics.org/resource-2.1.1", propOrder = {
    "onlineDescription",
    "url",
    "connection",
    "connectionDefinition"
})
public class OnlineType {

    protected I18NNonEmptyStringType onlineDescription;
    protected UrlType url;
    protected ConnectionType connection;
    protected ConnectionDefinitionType connectionDefinition;

    /**
     * Gets the value of the onlineDescription property.
     * 
     * @return
     *     possible object is
     *     {@link I18NNonEmptyStringType }
     *     
     */
    public I18NNonEmptyStringType getOnlineDescription() {
        return onlineDescription;
    }

    /**
     * Sets the value of the onlineDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link I18NNonEmptyStringType }
     *     
     */
    public void setOnlineDescription(I18NNonEmptyStringType value) {
        this.onlineDescription = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link UrlType }
     *     
     */
    public UrlType getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link UrlType }
     *     
     */
    public void setUrl(UrlType value) {
        this.url = value;
    }

    /**
     * Gets the value of the connection property.
     * 
     * @return
     *     possible object is
     *     {@link ConnectionType }
     *     
     */
    public ConnectionType getConnection() {
        return connection;
    }

    /**
     * Sets the value of the connection property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectionType }
     *     
     */
    public void setConnection(ConnectionType value) {
        this.connection = value;
    }

    /**
     * Gets the value of the connectionDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link ConnectionDefinitionType }
     *     
     */
    public ConnectionDefinitionType getConnectionDefinition() {
        return connectionDefinition;
    }

    /**
     * Sets the value of the connectionDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConnectionDefinitionType }
     *     
     */
    public void setConnectionDefinition(ConnectionDefinitionType value) {
        this.connectionDefinition = value;
    }

    public OnlineType withOnlineDescription(I18NNonEmptyStringType value) {
        setOnlineDescription(value);
        return this;
    }

    public OnlineType withUrl(UrlType value) {
        setUrl(value);
        return this;
    }

    public OnlineType withConnection(ConnectionType value) {
        setConnection(value);
        return this;
    }

    public OnlineType withConnectionDefinition(ConnectionDefinitionType value) {
        setConnectionDefinition(value);
        return this;
    }

}
