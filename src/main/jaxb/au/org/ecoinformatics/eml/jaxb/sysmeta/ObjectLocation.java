//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.03 at 05:40:32 PM CST 
//


package au.org.ecoinformatics.eml.jaxb.sysmeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Portion of an :class:`Types.ObjectLocationList`
 *       indicating the node from which the object can be retrieved. The
 *       principal information on each location is found in the *nodeIdentifier*,
 *       all other fields are provided for convenience, but could also be looked
 *       up from the :class:`Types.NodeList` information obtained from 
 *       :func:`CNCore.listNodes`.
 * 
 * <p>Java class for ObjectLocation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ObjectLocation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nodeIdentifier" type="{http://ns.dataone.org/service/types/v1}NodeReference"/>
 *         &lt;element name="baseURL" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="version" type="{http://ns.dataone.org/service/types/v1}ServiceVersion" maxOccurs="unbounded"/>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *         &lt;element name="preference" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectLocation", propOrder = {
    "nodeIdentifier",
    "baseURL",
    "versions",
    "url",
    "preference"
})
public class ObjectLocation {

    @XmlElement(required = true)
    protected NodeReference nodeIdentifier;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String baseURL;
    @XmlElement(name = "version", required = true)
    protected List<String> versions;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String url;
    protected Integer preference;

    /**
     * Gets the value of the nodeIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link NodeReference }
     *     
     */
    public NodeReference getNodeIdentifier() {
        return nodeIdentifier;
    }

    /**
     * Sets the value of the nodeIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link NodeReference }
     *     
     */
    public void setNodeIdentifier(NodeReference value) {
        this.nodeIdentifier = value;
    }

    /**
     * Gets the value of the baseURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBaseURL() {
        return baseURL;
    }

    /**
     * Sets the value of the baseURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBaseURL(String value) {
        this.baseURL = value;
    }

    /**
     * Gets the value of the versions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the versions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVersions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getVersions() {
        if (versions == null) {
            versions = new ArrayList<String>();
        }
        return this.versions;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the preference property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPreference() {
        return preference;
    }

    /**
     * Sets the value of the preference property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPreference(Integer value) {
        this.preference = value;
    }

    public ObjectLocation withNodeIdentifier(NodeReference value) {
        setNodeIdentifier(value);
        return this;
    }

    public ObjectLocation withBaseURL(String value) {
        setBaseURL(value);
        return this;
    }

    public ObjectLocation withVersions(String... values) {
        if (values!= null) {
            for (String value: values) {
                getVersions().add(value);
            }
        }
        return this;
    }

    public ObjectLocation withVersions(Collection<String> values) {
        if (values!= null) {
            getVersions().addAll(values);
        }
        return this;
    }

    public ObjectLocation withUrl(String value) {
        setUrl(value);
        return this;
    }

    public ObjectLocation withPreference(Integer value) {
        setPreference(value);
        return this;
    }

}
