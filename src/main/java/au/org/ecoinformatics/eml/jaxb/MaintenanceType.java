//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.02 at 04:58:44 PM CST 
//


package au.org.ecoinformatics.eml.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for MaintenanceType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MaintenanceType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description" type="{eml://ecoinformatics.org/text-2.1.1}TextType"/>
 *         &lt;element name="maintenanceUpdateFrequency" type="{eml://ecoinformatics.org/dataset-2.1.1}MaintUpFreqType" minOccurs="0"/>
 *         &lt;element name="changeHistory" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="changeScope" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                   &lt;element name="oldValue" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                   &lt;element name="changeDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                   &lt;element name="comment" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
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
@XmlType(name = "MaintenanceType", namespace = "eml://ecoinformatics.org/dataset-2.1.1", propOrder = {
    "description",
    "maintenanceUpdateFrequency",
    "changeHistory"
})
public class MaintenanceType {

    @XmlElement(required = true)
    protected TextType description;
    protected MaintUpFreqType maintenanceUpdateFrequency;
    protected List<MaintenanceType.ChangeHistory> changeHistory;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link TextType }
     *     
     */
    public TextType getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextType }
     *     
     */
    public void setDescription(TextType value) {
        this.description = value;
    }

    /**
     * Gets the value of the maintenanceUpdateFrequency property.
     * 
     * @return
     *     possible object is
     *     {@link MaintUpFreqType }
     *     
     */
    public MaintUpFreqType getMaintenanceUpdateFrequency() {
        return maintenanceUpdateFrequency;
    }

    /**
     * Sets the value of the maintenanceUpdateFrequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link MaintUpFreqType }
     *     
     */
    public void setMaintenanceUpdateFrequency(MaintUpFreqType value) {
        this.maintenanceUpdateFrequency = value;
    }

    /**
     * Gets the value of the changeHistory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the changeHistory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChangeHistory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MaintenanceType.ChangeHistory }
     * 
     * 
     */
    public List<MaintenanceType.ChangeHistory> getChangeHistory() {
        if (changeHistory == null) {
            changeHistory = new ArrayList<MaintenanceType.ChangeHistory>();
        }
        return this.changeHistory;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="changeScope" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *         &lt;element name="oldValue" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *         &lt;element name="changeDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *         &lt;element name="comment" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "changeScope",
        "oldValue",
        "changeDate",
        "comment"
    })
    public static class ChangeHistory {

        @XmlElement(required = true)
        protected String changeScope;
        @XmlElement(required = true)
        protected String oldValue;
        @XmlElement(required = true)
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar changeDate;
        protected String comment;

        /**
         * Gets the value of the changeScope property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getChangeScope() {
            return changeScope;
        }

        /**
         * Sets the value of the changeScope property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setChangeScope(String value) {
            this.changeScope = value;
        }

        /**
         * Gets the value of the oldValue property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getOldValue() {
            return oldValue;
        }

        /**
         * Sets the value of the oldValue property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setOldValue(String value) {
            this.oldValue = value;
        }

        /**
         * Gets the value of the changeDate property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getChangeDate() {
            return changeDate;
        }

        /**
         * Sets the value of the changeDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setChangeDate(XMLGregorianCalendar value) {
            this.changeDate = value;
        }

        /**
         * Gets the value of the comment property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getComment() {
            return comment;
        }

        /**
         * Sets the value of the comment property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setComment(String value) {
            this.comment = value;
        }

    }

}
