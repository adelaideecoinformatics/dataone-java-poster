//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.19 at 11:10:14 AM CST 
//


package au.org.ecoinformatics.eml.jaxb.sysmeta;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * A list of :term:`Subjects`, including both
 *       :class:`Types.Person` and :class:`Types.Group` entries returned from
 *       the :func:`CNIdentity.getSubjectInfo` service and
 *       :func:`CNIdentity.listSubjects` services.
 * 
 * <p>Java class for SubjectInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubjectInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="person" type="{http://ns.dataone.org/service/types/v1}Person" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="group" type="{http://ns.dataone.org/service/types/v1}Group" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubjectInfo", propOrder = {
    "persons",
    "groups"
})
@XmlRootElement(name = "subjectInfo")
public class SubjectInfo {

    @XmlElement(name = "person")
    protected List<Person> persons;
    @XmlElement(name = "group")
    protected List<Group> groups;

    /**
     * Gets the value of the persons property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the persons property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPersons().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Person }
     * 
     * 
     */
    public List<Person> getPersons() {
        if (persons == null) {
            persons = new ArrayList<Person>();
        }
        return this.persons;
    }

    /**
     * Gets the value of the groups property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groups property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroups().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Group }
     * 
     * 
     */
    public List<Group> getGroups() {
        if (groups == null) {
            groups = new ArrayList<Group>();
        }
        return this.groups;
    }

    public SubjectInfo withPersons(Person... values) {
        if (values!= null) {
            for (Person value: values) {
                getPersons().add(value);
            }
        }
        return this;
    }

    public SubjectInfo withPersons(Collection<Person> values) {
        if (values!= null) {
            getPersons().addAll(values);
        }
        return this;
    }

    public SubjectInfo withGroups(Group... values) {
        if (values!= null) {
            for (Group value: values) {
                getGroups().add(value);
            }
        }
        return this;
    }

    public SubjectInfo withGroups(Collection<Group> values) {
        if (values!= null) {
            getGroups().addAll(values);
        }
        return this;
    }

}
