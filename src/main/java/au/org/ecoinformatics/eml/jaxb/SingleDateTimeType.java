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
 * <p>Java class for SingleDateTimeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SingleDateTimeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element name="calendarDate" type="{eml://ecoinformatics.org/resource-2.1.1}yearDate"/>
 *           &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element name="alternativeTimeScale">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="timeScaleName" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                   &lt;element name="timeScaleAgeEstimate" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                   &lt;element name="timeScaleAgeUncertainty" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
 *                   &lt;element name="timeScaleAgeExplanation" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
 *                   &lt;element name="timeScaleCitation" type="{eml://ecoinformatics.org/literature-2.1.1}CitationType" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SingleDateTimeType", namespace = "eml://ecoinformatics.org/coverage-2.1.1", propOrder = {
    "calendarDate",
    "time",
    "alternativeTimeScale"
})
public class SingleDateTimeType {

    protected String calendarDate;
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar time;
    protected SingleDateTimeType.AlternativeTimeScale alternativeTimeScale;

    /**
     * Gets the value of the calendarDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCalendarDate() {
        return calendarDate;
    }

    /**
     * Sets the value of the calendarDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCalendarDate(String value) {
        this.calendarDate = value;
    }

    /**
     * Gets the value of the time property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTime(XMLGregorianCalendar value) {
        this.time = value;
    }

    /**
     * Gets the value of the alternativeTimeScale property.
     * 
     * @return
     *     possible object is
     *     {@link SingleDateTimeType.AlternativeTimeScale }
     *     
     */
    public SingleDateTimeType.AlternativeTimeScale getAlternativeTimeScale() {
        return alternativeTimeScale;
    }

    /**
     * Sets the value of the alternativeTimeScale property.
     * 
     * @param value
     *     allowed object is
     *     {@link SingleDateTimeType.AlternativeTimeScale }
     *     
     */
    public void setAlternativeTimeScale(SingleDateTimeType.AlternativeTimeScale value) {
        this.alternativeTimeScale = value;
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
     *         &lt;element name="timeScaleName" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *         &lt;element name="timeScaleAgeEstimate" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *         &lt;element name="timeScaleAgeUncertainty" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
     *         &lt;element name="timeScaleAgeExplanation" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
     *         &lt;element name="timeScaleCitation" type="{eml://ecoinformatics.org/literature-2.1.1}CitationType" maxOccurs="unbounded" minOccurs="0"/>
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
        "timeScaleName",
        "timeScaleAgeEstimate",
        "timeScaleAgeUncertainty",
        "timeScaleAgeExplanation",
        "timeScaleCitation"
    })
    public static class AlternativeTimeScale {

        @XmlElement(required = true)
        protected String timeScaleName;
        @XmlElement(required = true)
        protected String timeScaleAgeEstimate;
        protected String timeScaleAgeUncertainty;
        protected String timeScaleAgeExplanation;
        protected List<CitationType> timeScaleCitation;

        /**
         * Gets the value of the timeScaleName property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeScaleName() {
            return timeScaleName;
        }

        /**
         * Sets the value of the timeScaleName property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeScaleName(String value) {
            this.timeScaleName = value;
        }

        /**
         * Gets the value of the timeScaleAgeEstimate property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeScaleAgeEstimate() {
            return timeScaleAgeEstimate;
        }

        /**
         * Sets the value of the timeScaleAgeEstimate property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeScaleAgeEstimate(String value) {
            this.timeScaleAgeEstimate = value;
        }

        /**
         * Gets the value of the timeScaleAgeUncertainty property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeScaleAgeUncertainty() {
            return timeScaleAgeUncertainty;
        }

        /**
         * Sets the value of the timeScaleAgeUncertainty property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeScaleAgeUncertainty(String value) {
            this.timeScaleAgeUncertainty = value;
        }

        /**
         * Gets the value of the timeScaleAgeExplanation property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTimeScaleAgeExplanation() {
            return timeScaleAgeExplanation;
        }

        /**
         * Sets the value of the timeScaleAgeExplanation property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTimeScaleAgeExplanation(String value) {
            this.timeScaleAgeExplanation = value;
        }

        /**
         * Gets the value of the timeScaleCitation property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the timeScaleCitation property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getTimeScaleCitation().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CitationType }
         * 
         * 
         */
        public List<CitationType> getTimeScaleCitation() {
            if (timeScaleCitation == null) {
                timeScaleCitation = new ArrayList<CitationType>();
            }
            return this.timeScaleCitation;
        }

    }

}
