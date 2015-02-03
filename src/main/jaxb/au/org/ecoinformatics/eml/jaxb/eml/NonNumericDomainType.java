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
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NonNumericDomainType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NonNumericDomainType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;choice maxOccurs="unbounded">
 *           &lt;element name="enumeratedDomain">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;choice>
 *                     &lt;element name="codeDefinition" maxOccurs="unbounded">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="code" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                               &lt;element name="definition" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                               &lt;element name="source" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
 *                             &lt;/sequence>
 *                             &lt;attribute name="order" type="{http://www.w3.org/2001/XMLSchema}long" />
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="externalCodeSet">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="codesetName" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                               &lt;choice maxOccurs="unbounded">
 *                                 &lt;element name="citation" type="{eml://ecoinformatics.org/literature-2.1.1}CitationType"/>
 *                                 &lt;element name="codesetURL" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
 *                               &lt;/choice>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                     &lt;element name="entityCodeList">
 *                       &lt;complexType>
 *                         &lt;complexContent>
 *                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                             &lt;sequence>
 *                               &lt;element name="entityReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                               &lt;element name="valueAttributeReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                               &lt;element name="definitionAttributeReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                               &lt;element name="orderAttributeReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
 *                             &lt;/sequence>
 *                           &lt;/restriction>
 *                         &lt;/complexContent>
 *                       &lt;/complexType>
 *                     &lt;/element>
 *                   &lt;/choice>
 *                   &lt;attribute name="enforced" default="yes">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="yes"/>
 *                         &lt;enumeration value="no"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/attribute>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="textDomain">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                   &lt;sequence>
 *                     &lt;element name="definition" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
 *                     &lt;element name="pattern" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" maxOccurs="unbounded" minOccurs="0"/>
 *                     &lt;element name="source" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
 *                   &lt;/sequence>
 *                 &lt;/restriction>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
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
@XmlType(name = "NonNumericDomainType", namespace = "eml://ecoinformatics.org/attribute-2.1.1", propOrder = {
    "enumeratedDomainOrTextDomain",
    "references"
})
public class NonNumericDomainType {

    @XmlElements({
        @XmlElement(name = "enumeratedDomain", type = NonNumericDomainType.EnumeratedDomain.class),
        @XmlElement(name = "textDomain", type = NonNumericDomainType.TextDomain.class)
    })
    protected List<Object> enumeratedDomainOrTextDomain;
    protected au.org.ecoinformatics.eml.jaxb.eml.ViewType.References references;
    @XmlAttribute(name = "id")
    protected List<String> id;

    /**
     * Gets the value of the enumeratedDomainOrTextDomain property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enumeratedDomainOrTextDomain property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnumeratedDomainOrTextDomain().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NonNumericDomainType.EnumeratedDomain }
     * {@link NonNumericDomainType.TextDomain }
     * 
     * 
     */
    public List<Object> getEnumeratedDomainOrTextDomain() {
        if (enumeratedDomainOrTextDomain == null) {
            enumeratedDomainOrTextDomain = new ArrayList<Object>();
        }
        return this.enumeratedDomainOrTextDomain;
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

    public NonNumericDomainType withEnumeratedDomainOrTextDomain(Object... values) {
        if (values!= null) {
            for (Object value: values) {
                getEnumeratedDomainOrTextDomain().add(value);
            }
        }
        return this;
    }

    public NonNumericDomainType withEnumeratedDomainOrTextDomain(Collection<Object> values) {
        if (values!= null) {
            getEnumeratedDomainOrTextDomain().addAll(values);
        }
        return this;
    }

    public NonNumericDomainType withReferences(au.org.ecoinformatics.eml.jaxb.eml.ViewType.References value) {
        setReferences(value);
        return this;
    }

    public NonNumericDomainType withId(String... values) {
        if (values!= null) {
            for (String value: values) {
                getId().add(value);
            }
        }
        return this;
    }

    public NonNumericDomainType withId(Collection<String> values) {
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
     *       &lt;choice>
     *         &lt;element name="codeDefinition" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="code" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *                   &lt;element name="definition" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *                   &lt;element name="source" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
     *                 &lt;/sequence>
     *                 &lt;attribute name="order" type="{http://www.w3.org/2001/XMLSchema}long" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="externalCodeSet">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="codesetName" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *                   &lt;choice maxOccurs="unbounded">
     *                     &lt;element name="citation" type="{eml://ecoinformatics.org/literature-2.1.1}CitationType"/>
     *                     &lt;element name="codesetURL" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
     *                   &lt;/choice>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="entityCodeList">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="entityReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *                   &lt;element name="valueAttributeReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *                   &lt;element name="definitionAttributeReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *                   &lt;element name="orderAttributeReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/choice>
     *       &lt;attribute name="enforced" default="yes">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="yes"/>
     *             &lt;enumeration value="no"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "codeDefinition",
        "externalCodeSet",
        "entityCodeList"
    })
    public static class EnumeratedDomain {

        protected List<NonNumericDomainType.EnumeratedDomain.CodeDefinition> codeDefinition;
        protected NonNumericDomainType.EnumeratedDomain.ExternalCodeSet externalCodeSet;
        protected NonNumericDomainType.EnumeratedDomain.EntityCodeList entityCodeList;
        @XmlAttribute(name = "enforced")
        protected String enforced;

        /**
         * Gets the value of the codeDefinition property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the codeDefinition property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCodeDefinition().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link NonNumericDomainType.EnumeratedDomain.CodeDefinition }
         * 
         * 
         */
        public List<NonNumericDomainType.EnumeratedDomain.CodeDefinition> getCodeDefinition() {
            if (codeDefinition == null) {
                codeDefinition = new ArrayList<NonNumericDomainType.EnumeratedDomain.CodeDefinition>();
            }
            return this.codeDefinition;
        }

        /**
         * Gets the value of the externalCodeSet property.
         * 
         * @return
         *     possible object is
         *     {@link NonNumericDomainType.EnumeratedDomain.ExternalCodeSet }
         *     
         */
        public NonNumericDomainType.EnumeratedDomain.ExternalCodeSet getExternalCodeSet() {
            return externalCodeSet;
        }

        /**
         * Sets the value of the externalCodeSet property.
         * 
         * @param value
         *     allowed object is
         *     {@link NonNumericDomainType.EnumeratedDomain.ExternalCodeSet }
         *     
         */
        public void setExternalCodeSet(NonNumericDomainType.EnumeratedDomain.ExternalCodeSet value) {
            this.externalCodeSet = value;
        }

        /**
         * Gets the value of the entityCodeList property.
         * 
         * @return
         *     possible object is
         *     {@link NonNumericDomainType.EnumeratedDomain.EntityCodeList }
         *     
         */
        public NonNumericDomainType.EnumeratedDomain.EntityCodeList getEntityCodeList() {
            return entityCodeList;
        }

        /**
         * Sets the value of the entityCodeList property.
         * 
         * @param value
         *     allowed object is
         *     {@link NonNumericDomainType.EnumeratedDomain.EntityCodeList }
         *     
         */
        public void setEntityCodeList(NonNumericDomainType.EnumeratedDomain.EntityCodeList value) {
            this.entityCodeList = value;
        }

        /**
         * Gets the value of the enforced property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEnforced() {
            if (enforced == null) {
                return "yes";
            } else {
                return enforced;
            }
        }

        /**
         * Sets the value of the enforced property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEnforced(String value) {
            this.enforced = value;
        }

        public NonNumericDomainType.EnumeratedDomain withCodeDefinition(NonNumericDomainType.EnumeratedDomain.CodeDefinition... values) {
            if (values!= null) {
                for (NonNumericDomainType.EnumeratedDomain.CodeDefinition value: values) {
                    getCodeDefinition().add(value);
                }
            }
            return this;
        }

        public NonNumericDomainType.EnumeratedDomain withCodeDefinition(Collection<NonNumericDomainType.EnumeratedDomain.CodeDefinition> values) {
            if (values!= null) {
                getCodeDefinition().addAll(values);
            }
            return this;
        }

        public NonNumericDomainType.EnumeratedDomain withExternalCodeSet(NonNumericDomainType.EnumeratedDomain.ExternalCodeSet value) {
            setExternalCodeSet(value);
            return this;
        }

        public NonNumericDomainType.EnumeratedDomain withEntityCodeList(NonNumericDomainType.EnumeratedDomain.EntityCodeList value) {
            setEntityCodeList(value);
            return this;
        }

        public NonNumericDomainType.EnumeratedDomain withEnforced(String value) {
            setEnforced(value);
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
         *         &lt;element name="code" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
         *         &lt;element name="definition" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
         *         &lt;element name="source" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
         *       &lt;/sequence>
         *       &lt;attribute name="order" type="{http://www.w3.org/2001/XMLSchema}long" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "code",
            "definition",
            "source"
        })
        public static class CodeDefinition {

            @XmlElement(required = true)
            protected String code;
            @XmlElement(required = true)
            protected String definition;
            protected String source;
            @XmlAttribute(name = "order")
            protected Long order;

            /**
             * Gets the value of the code property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCode() {
                return code;
            }

            /**
             * Sets the value of the code property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCode(String value) {
                this.code = value;
            }

            /**
             * Gets the value of the definition property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDefinition() {
                return definition;
            }

            /**
             * Sets the value of the definition property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDefinition(String value) {
                this.definition = value;
            }

            /**
             * Gets the value of the source property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getSource() {
                return source;
            }

            /**
             * Sets the value of the source property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setSource(String value) {
                this.source = value;
            }

            /**
             * Gets the value of the order property.
             * 
             * @return
             *     possible object is
             *     {@link Long }
             *     
             */
            public Long getOrder() {
                return order;
            }

            /**
             * Sets the value of the order property.
             * 
             * @param value
             *     allowed object is
             *     {@link Long }
             *     
             */
            public void setOrder(Long value) {
                this.order = value;
            }

            public NonNumericDomainType.EnumeratedDomain.CodeDefinition withCode(String value) {
                setCode(value);
                return this;
            }

            public NonNumericDomainType.EnumeratedDomain.CodeDefinition withDefinition(String value) {
                setDefinition(value);
                return this;
            }

            public NonNumericDomainType.EnumeratedDomain.CodeDefinition withSource(String value) {
                setSource(value);
                return this;
            }

            public NonNumericDomainType.EnumeratedDomain.CodeDefinition withOrder(Long value) {
                setOrder(value);
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
         *         &lt;element name="entityReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
         *         &lt;element name="valueAttributeReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
         *         &lt;element name="definitionAttributeReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
         *         &lt;element name="orderAttributeReference" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
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
            "entityReference",
            "valueAttributeReference",
            "definitionAttributeReference",
            "orderAttributeReference"
        })
        public static class EntityCodeList {

            @XmlElement(required = true)
            protected String entityReference;
            @XmlElement(required = true)
            protected String valueAttributeReference;
            @XmlElement(required = true)
            protected String definitionAttributeReference;
            protected String orderAttributeReference;

            /**
             * Gets the value of the entityReference property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getEntityReference() {
                return entityReference;
            }

            /**
             * Sets the value of the entityReference property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setEntityReference(String value) {
                this.entityReference = value;
            }

            /**
             * Gets the value of the valueAttributeReference property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getValueAttributeReference() {
                return valueAttributeReference;
            }

            /**
             * Sets the value of the valueAttributeReference property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setValueAttributeReference(String value) {
                this.valueAttributeReference = value;
            }

            /**
             * Gets the value of the definitionAttributeReference property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDefinitionAttributeReference() {
                return definitionAttributeReference;
            }

            /**
             * Sets the value of the definitionAttributeReference property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDefinitionAttributeReference(String value) {
                this.definitionAttributeReference = value;
            }

            /**
             * Gets the value of the orderAttributeReference property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getOrderAttributeReference() {
                return orderAttributeReference;
            }

            /**
             * Sets the value of the orderAttributeReference property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setOrderAttributeReference(String value) {
                this.orderAttributeReference = value;
            }

            public NonNumericDomainType.EnumeratedDomain.EntityCodeList withEntityReference(String value) {
                setEntityReference(value);
                return this;
            }

            public NonNumericDomainType.EnumeratedDomain.EntityCodeList withValueAttributeReference(String value) {
                setValueAttributeReference(value);
                return this;
            }

            public NonNumericDomainType.EnumeratedDomain.EntityCodeList withDefinitionAttributeReference(String value) {
                setDefinitionAttributeReference(value);
                return this;
            }

            public NonNumericDomainType.EnumeratedDomain.EntityCodeList withOrderAttributeReference(String value) {
                setOrderAttributeReference(value);
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
         *         &lt;element name="codesetName" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
         *         &lt;choice maxOccurs="unbounded">
         *           &lt;element name="citation" type="{eml://ecoinformatics.org/literature-2.1.1}CitationType"/>
         *           &lt;element name="codesetURL" type="{http://www.w3.org/2001/XMLSchema}anyURI"/>
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
        @XmlType(name = "", propOrder = {
            "codesetName",
            "citationOrCodesetURL"
        })
        public static class ExternalCodeSet {

            @XmlElement(required = true)
            protected String codesetName;
            @XmlElements({
                @XmlElement(name = "citation", type = CitationType.class),
                @XmlElement(name = "codesetURL", type = String.class)
            })
            protected List<Object> citationOrCodesetURL;

            /**
             * Gets the value of the codesetName property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getCodesetName() {
                return codesetName;
            }

            /**
             * Sets the value of the codesetName property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setCodesetName(String value) {
                this.codesetName = value;
            }

            /**
             * Gets the value of the citationOrCodesetURL property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the citationOrCodesetURL property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getCitationOrCodesetURL().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link CitationType }
             * {@link String }
             * 
             * 
             */
            public List<Object> getCitationOrCodesetURL() {
                if (citationOrCodesetURL == null) {
                    citationOrCodesetURL = new ArrayList<Object>();
                }
                return this.citationOrCodesetURL;
            }

            public NonNumericDomainType.EnumeratedDomain.ExternalCodeSet withCodesetName(String value) {
                setCodesetName(value);
                return this;
            }

            public NonNumericDomainType.EnumeratedDomain.ExternalCodeSet withCitationOrCodesetURL(Object... values) {
                if (values!= null) {
                    for (Object value: values) {
                        getCitationOrCodesetURL().add(value);
                    }
                }
                return this;
            }

            public NonNumericDomainType.EnumeratedDomain.ExternalCodeSet withCitationOrCodesetURL(Collection<Object> values) {
                if (values!= null) {
                    getCitationOrCodesetURL().addAll(values);
                }
                return this;
            }

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
     *         &lt;element name="definition" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType"/>
     *         &lt;element name="pattern" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="source" type="{eml://ecoinformatics.org/resource-2.1.1}NonEmptyStringType" minOccurs="0"/>
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
        "definition",
        "pattern",
        "source"
    })
    public static class TextDomain {

        @XmlElement(required = true)
        protected String definition;
        protected List<String> pattern;
        protected String source;

        /**
         * Gets the value of the definition property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDefinition() {
            return definition;
        }

        /**
         * Sets the value of the definition property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDefinition(String value) {
            this.definition = value;
        }

        /**
         * Gets the value of the pattern property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the pattern property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPattern().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getPattern() {
            if (pattern == null) {
                pattern = new ArrayList<String>();
            }
            return this.pattern;
        }

        /**
         * Gets the value of the source property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSource() {
            return source;
        }

        /**
         * Sets the value of the source property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSource(String value) {
            this.source = value;
        }

        public NonNumericDomainType.TextDomain withDefinition(String value) {
            setDefinition(value);
            return this;
        }

        public NonNumericDomainType.TextDomain withPattern(String... values) {
            if (values!= null) {
                for (String value: values) {
                    getPattern().add(value);
                }
            }
            return this;
        }

        public NonNumericDomainType.TextDomain withPattern(Collection<String> values) {
            if (values!= null) {
                getPattern().addAll(values);
            }
            return this;
        }

        public NonNumericDomainType.TextDomain withSource(String value) {
            setSource(value);
            return this;
        }

    }

}
