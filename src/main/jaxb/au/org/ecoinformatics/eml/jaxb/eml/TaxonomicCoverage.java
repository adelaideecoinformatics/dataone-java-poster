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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TaxonomicCoverage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaxonomicCoverage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;sequence>
 *           &lt;element name="taxonomicSystem" minOccurs="0">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="classificationSystem" maxOccurs="unbounded">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="classificationSystemCitation" type="{eml://ecoinformatics.org/literature-2.1.1}CitationType"/>
 *                               &lt;element name="classificationSystemModifications" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="identificationReference" type="{eml://ecoinformatics.org/literature-2.1.1}CitationType" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="identifierName" type="{eml://ecoinformatics.org/party-2.1.1}ResponsibleParty" maxOccurs="unbounded"/>
 *                     &lt;element name="taxonomicProcedures" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                     &lt;element name="taxonomicCompleteness" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
 *                     &lt;element name="vouchers" maxOccurs="unbounded" minOccurs="0">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="specimen" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                               &lt;element name="repository">
 *                                 &lt;complexType>
 *                                   &lt;complexContent>
 *                                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                       &lt;sequence>
 *                                         &lt;element name="originator" type="{eml://ecoinformatics.org/party-2.1.1}ResponsibleParty" maxOccurs="unbounded"/>
 *                                       &lt;/sequence>
 *                                     &lt;/restriction>
 *                                   &lt;/complexContent>
 *                                 &lt;/complexType>
 *                               &lt;/element>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="generalTaxonomicCoverage" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
 *           &lt;element name="taxonomicClassification" type="{eml://ecoinformatics.org/coverage-2.1.1}TaxonomicClassificationType" maxOccurs="unbounded"/>
 *         &lt;/sequence>
 *         &lt;group ref="{eml://ecoinformatics.org/resource-2.1.1}ReferencesGroup"/>
 *       &lt;/choice>
 *       &lt;attribute name="id" type="{eml://ecoinformatics.org/resource-2.1.1}IDType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaxonomicCoverage", namespace = "eml://ecoinformatics.org/coverage-2.1.1", propOrder = {
    "taxonomicSystem",
    "generalTaxonomicCoverage",
    "taxonomicClassification",
    "references"
})
@XmlSeeAlso({
    au.org.ecoinformatics.eml.jaxb.eml.Coverage.TaxonomicCoverage.class
})
public class TaxonomicCoverage {

    protected TaxonomicCoverage.TaxonomicSystem taxonomicSystem;
    protected String generalTaxonomicCoverage;
    protected List<TaxonomicClassificationType> taxonomicClassification;
    protected au.org.ecoinformatics.eml.jaxb.eml.ViewType.References references;
    @XmlAttribute(name = "id")
    protected List<String> id;

    /**
     * Gets the value of the taxonomicSystem property.
     * 
     * @return
     *     possible object is
     *     {@link TaxonomicCoverage.TaxonomicSystem }
     *     
     */
    public TaxonomicCoverage.TaxonomicSystem getTaxonomicSystem() {
        return taxonomicSystem;
    }

    /**
     * Sets the value of the taxonomicSystem property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaxonomicCoverage.TaxonomicSystem }
     *     
     */
    public void setTaxonomicSystem(TaxonomicCoverage.TaxonomicSystem value) {
        this.taxonomicSystem = value;
    }

    /**
     * Gets the value of the generalTaxonomicCoverage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeneralTaxonomicCoverage() {
        return generalTaxonomicCoverage;
    }

    /**
     * Sets the value of the generalTaxonomicCoverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeneralTaxonomicCoverage(String value) {
        this.generalTaxonomicCoverage = value;
    }

    /**
     * Gets the value of the taxonomicClassification property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taxonomicClassification property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaxonomicClassification().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaxonomicClassificationType }
     * 
     * 
     */
    public List<TaxonomicClassificationType> getTaxonomicClassification() {
        if (taxonomicClassification == null) {
            taxonomicClassification = new ArrayList<TaxonomicClassificationType>();
        }
        return this.taxonomicClassification;
    }

    /**
     * Gets the value of the references property.
     * 
     * @return
     *     possible object is
     *     {@link au.org.ecoinformatics.eml.jaxb.eml.ViewType.References }
     *     
     */
    public au.org.ecoinformatics.eml.jaxb.eml.ViewType.References getReferences() {
        return references;
    }

    /**
     * Sets the value of the references property.
     * 
     * @param value
     *     allowed object is
     *     {@link au.org.ecoinformatics.eml.jaxb.eml.ViewType.References }
     *     
     */
    public void setReferences(au.org.ecoinformatics.eml.jaxb.eml.ViewType.References value) {
        this.references = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the id property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getId() {
        if (id == null) {
            id = new ArrayList<String>();
        }
        return this.id;
    }

    public TaxonomicCoverage withTaxonomicSystem(TaxonomicCoverage.TaxonomicSystem value) {
        setTaxonomicSystem(value);
        return this;
    }

    public TaxonomicCoverage withGeneralTaxonomicCoverage(String value) {
        setGeneralTaxonomicCoverage(value);
        return this;
    }

    public TaxonomicCoverage withTaxonomicClassification(TaxonomicClassificationType... values) {
        if (values!= null) {
            for (TaxonomicClassificationType value: values) {
                getTaxonomicClassification().add(value);
            }
        }
        return this;
    }

    public TaxonomicCoverage withTaxonomicClassification(Collection<TaxonomicClassificationType> values) {
        if (values!= null) {
            getTaxonomicClassification().addAll(values);
        }
        return this;
    }

    public TaxonomicCoverage withReferences(au.org.ecoinformatics.eml.jaxb.eml.ViewType.References value) {
        setReferences(value);
        return this;
    }

    public TaxonomicCoverage withId(String... values) {
        if (values!= null) {
            for (String value: values) {
                getId().add(value);
            }
        }
        return this;
    }

    public TaxonomicCoverage withId(Collection<String> values) {
        if (values!= null) {
            getId().addAll(values);
        }
        return this;
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
     *         &lt;element name="classificationSystem" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="classificationSystemCitation" type="{eml://ecoinformatics.org/literature-2.1.1}CitationType"/>
     *                   &lt;element name="classificationSystemModifications" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="identificationReference" type="{eml://ecoinformatics.org/literature-2.1.1}CitationType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="identifierName" type="{eml://ecoinformatics.org/party-2.1.1}ResponsibleParty" maxOccurs="unbounded"/>
     *         &lt;element name="taxonomicProcedures" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *         &lt;element name="taxonomicCompleteness" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
     *         &lt;element name="vouchers" maxOccurs="unbounded" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="specimen" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *                   &lt;element name="repository">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="originator" type="{eml://ecoinformatics.org/party-2.1.1}ResponsibleParty" maxOccurs="unbounded"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
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
    @XmlType(name = "", propOrder = {
        "classificationSystem",
        "identificationReference",
        "identifierName",
        "taxonomicProcedures",
        "taxonomicCompleteness",
        "vouchers"
    })
    public static class TaxonomicSystem {

        @XmlElement(required = true)
        protected List<TaxonomicCoverage.TaxonomicSystem.ClassificationSystem> classificationSystem;
        protected List<CitationType> identificationReference;
        @XmlElement(required = true)
        protected List<ResponsibleParty> identifierName;
        @XmlElement(required = true)
        protected String taxonomicProcedures;
        protected String taxonomicCompleteness;
        protected List<TaxonomicCoverage.TaxonomicSystem.Vouchers> vouchers;

        /**
         * Gets the value of the classificationSystem property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the classificationSystem property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getClassificationSystem().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TaxonomicCoverage.TaxonomicSystem.ClassificationSystem }
         * 
         * 
         */
        public List<TaxonomicCoverage.TaxonomicSystem.ClassificationSystem> getClassificationSystem() {
            if (classificationSystem == null) {
                classificationSystem = new ArrayList<TaxonomicCoverage.TaxonomicSystem.ClassificationSystem>();
            }
            return this.classificationSystem;
        }

        /**
         * Gets the value of the identificationReference property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the identificationReference property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIdentificationReference().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CitationType }
         * 
         * 
         */
        public List<CitationType> getIdentificationReference() {
            if (identificationReference == null) {
                identificationReference = new ArrayList<CitationType>();
            }
            return this.identificationReference;
        }

        /**
         * Gets the value of the identifierName property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the identifierName property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getIdentifierName().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ResponsibleParty }
         * 
         * 
         */
        public List<ResponsibleParty> getIdentifierName() {
            if (identifierName == null) {
                identifierName = new ArrayList<ResponsibleParty>();
            }
            return this.identifierName;
        }

        /**
         * Gets the value of the taxonomicProcedures property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTaxonomicProcedures() {
            return taxonomicProcedures;
        }

        /**
         * Sets the value of the taxonomicProcedures property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTaxonomicProcedures(String value) {
            this.taxonomicProcedures = value;
        }

        /**
         * Gets the value of the taxonomicCompleteness property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTaxonomicCompleteness() {
            return taxonomicCompleteness;
        }

        /**
         * Sets the value of the taxonomicCompleteness property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTaxonomicCompleteness(String value) {
            this.taxonomicCompleteness = value;
        }

        /**
         * Gets the value of the vouchers property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the vouchers property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getVouchers().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link TaxonomicCoverage.TaxonomicSystem.Vouchers }
         * 
         * 
         */
        public List<TaxonomicCoverage.TaxonomicSystem.Vouchers> getVouchers() {
            if (vouchers == null) {
                vouchers = new ArrayList<TaxonomicCoverage.TaxonomicSystem.Vouchers>();
            }
            return this.vouchers;
        }

        public TaxonomicCoverage.TaxonomicSystem withClassificationSystem(TaxonomicCoverage.TaxonomicSystem.ClassificationSystem... values) {
            if (values!= null) {
                for (TaxonomicCoverage.TaxonomicSystem.ClassificationSystem value: values) {
                    getClassificationSystem().add(value);
                }
            }
            return this;
        }

        public TaxonomicCoverage.TaxonomicSystem withClassificationSystem(Collection<TaxonomicCoverage.TaxonomicSystem.ClassificationSystem> values) {
            if (values!= null) {
                getClassificationSystem().addAll(values);
            }
            return this;
        }

        public TaxonomicCoverage.TaxonomicSystem withIdentificationReference(CitationType... values) {
            if (values!= null) {
                for (CitationType value: values) {
                    getIdentificationReference().add(value);
                }
            }
            return this;
        }

        public TaxonomicCoverage.TaxonomicSystem withIdentificationReference(Collection<CitationType> values) {
            if (values!= null) {
                getIdentificationReference().addAll(values);
            }
            return this;
        }

        public TaxonomicCoverage.TaxonomicSystem withIdentifierName(ResponsibleParty... values) {
            if (values!= null) {
                for (ResponsibleParty value: values) {
                    getIdentifierName().add(value);
                }
            }
            return this;
        }

        public TaxonomicCoverage.TaxonomicSystem withIdentifierName(Collection<ResponsibleParty> values) {
            if (values!= null) {
                getIdentifierName().addAll(values);
            }
            return this;
        }

        public TaxonomicCoverage.TaxonomicSystem withTaxonomicProcedures(String value) {
            setTaxonomicProcedures(value);
            return this;
        }

        public TaxonomicCoverage.TaxonomicSystem withTaxonomicCompleteness(String value) {
            setTaxonomicCompleteness(value);
            return this;
        }

        public TaxonomicCoverage.TaxonomicSystem withVouchers(TaxonomicCoverage.TaxonomicSystem.Vouchers... values) {
            if (values!= null) {
                for (TaxonomicCoverage.TaxonomicSystem.Vouchers value: values) {
                    getVouchers().add(value);
                }
            }
            return this;
        }

        public TaxonomicCoverage.TaxonomicSystem withVouchers(Collection<TaxonomicCoverage.TaxonomicSystem.Vouchers> values) {
            if (values!= null) {
                getVouchers().addAll(values);
            }
            return this;
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
         *         &lt;element name="classificationSystemCitation" type="{eml://ecoinformatics.org/literature-2.1.1}CitationType"/>
         *         &lt;element name="classificationSystemModifications" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
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
            "classificationSystemCitation",
            "classificationSystemModifications"
        })
        public static class ClassificationSystem {

            @XmlElement(required = true)
            protected CitationType classificationSystemCitation;
            protected String classificationSystemModifications;

            /**
             * Gets the value of the classificationSystemCitation property.
             * 
             * @return
             *     possible object is
             *     {@link CitationType }
             *     
             */
            public CitationType getClassificationSystemCitation() {
                return classificationSystemCitation;
            }

            /**
             * Sets the value of the classificationSystemCitation property.
             * 
             * @param value
             *     allowed object is
             *     {@link CitationType }
             *     
             */
            public void setClassificationSystemCitation(CitationType value) {
                this.classificationSystemCitation = value;
            }

            /**
             * Gets the value of the classificationSystemModifications property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getClassificationSystemModifications() {
                return classificationSystemModifications;
            }

            /**
             * Sets the value of the classificationSystemModifications property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setClassificationSystemModifications(String value) {
                this.classificationSystemModifications = value;
            }

            public TaxonomicCoverage.TaxonomicSystem.ClassificationSystem withClassificationSystemCitation(CitationType value) {
                setClassificationSystemCitation(value);
                return this;
            }

            public TaxonomicCoverage.TaxonomicSystem.ClassificationSystem withClassificationSystemModifications(String value) {
                setClassificationSystemModifications(value);
                return this;
            }

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
         *         &lt;element name="specimen" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
         *         &lt;element name="repository">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="originator" type="{eml://ecoinformatics.org/party-2.1.1}ResponsibleParty" maxOccurs="unbounded"/>
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
        @XmlType(name = "", propOrder = {
            "specimen",
            "repository"
        })
        public static class Vouchers {

            @XmlElement(required = true)
            protected String specimen;
            @XmlElement(required = true)
            protected TaxonomicCoverage.TaxonomicSystem.Vouchers.Repository repository;

            /**
             * Gets the value of the specimen property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSpecimen() {
                return specimen;
            }

            /**
             * Sets the value of the specimen property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSpecimen(String value) {
                this.specimen = value;
            }

            /**
             * Gets the value of the repository property.
             * 
             * @return
             *     possible object is
             *     {@link TaxonomicCoverage.TaxonomicSystem.Vouchers.Repository }
             *     
             */
            public TaxonomicCoverage.TaxonomicSystem.Vouchers.Repository getRepository() {
                return repository;
            }

            /**
             * Sets the value of the repository property.
             * 
             * @param value
             *     allowed object is
             *     {@link TaxonomicCoverage.TaxonomicSystem.Vouchers.Repository }
             *     
             */
            public void setRepository(TaxonomicCoverage.TaxonomicSystem.Vouchers.Repository value) {
                this.repository = value;
            }

            public TaxonomicCoverage.TaxonomicSystem.Vouchers withSpecimen(String value) {
                setSpecimen(value);
                return this;
            }

            public TaxonomicCoverage.TaxonomicSystem.Vouchers withRepository(TaxonomicCoverage.TaxonomicSystem.Vouchers.Repository value) {
                setRepository(value);
                return this;
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
             *         &lt;element name="originator" type="{eml://ecoinformatics.org/party-2.1.1}ResponsibleParty" maxOccurs="unbounded"/>
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
                "originator"
            })
            public static class Repository {

                @XmlElement(required = true)
                protected List<ResponsibleParty> originator;

                /**
                 * Gets the value of the originator property.
                 * 
                 * <p>
                 * This accessor method returns a reference to the live list,
                 * not a snapshot. Therefore any modification you make to the
                 * returned list will be present inside the JAXB object.
                 * This is why there is not a <CODE>set</CODE> method for the originator property.
                 * 
                 * <p>
                 * For example, to add a new item, do as follows:
                 * <pre>
                 *    getOriginator().add(newItem);
                 * </pre>
                 * 
                 * 
                 * <p>
                 * Objects of the following type(s) are allowed in the list
                 * {@link ResponsibleParty }
                 * 
                 * 
                 */
                public List<ResponsibleParty> getOriginator() {
                    if (originator == null) {
                        originator = new ArrayList<ResponsibleParty>();
                    }
                    return this.originator;
                }

                public TaxonomicCoverage.TaxonomicSystem.Vouchers.Repository withOriginator(ResponsibleParty... values) {
                    if (values!= null) {
                        for (ResponsibleParty value: values) {
                            getOriginator().add(value);
                        }
                    }
                    return this;
                }

                public TaxonomicCoverage.TaxonomicSystem.Vouchers.Repository withOriginator(Collection<ResponsibleParty> values) {
                    if (values!= null) {
                        getOriginator().addAll(values);
                    }
                    return this;
                }

            }

        }

    }

}
