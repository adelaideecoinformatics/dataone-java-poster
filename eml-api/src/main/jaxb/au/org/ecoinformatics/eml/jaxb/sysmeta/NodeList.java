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
 * NodeList is described in
 *        :mod:`NodeList`.
 * 
 * <p>Java class for NodeList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NodeList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="node" type="{http://ns.dataone.org/service/types/v1}Node" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NodeList", propOrder = {
    "nodes"
})
@XmlRootElement(name = "nodeList")
public class NodeList {

    @XmlElement(name = "node", required = true)
    protected List<Node> nodes;

    /**
     * Gets the value of the nodes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nodes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNodes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Node }
     * 
     * 
     */
    public List<Node> getNodes() {
        if (nodes == null) {
            nodes = new ArrayList<Node>();
        }
        return this.nodes;
    }

    public NodeList withNodes(Node... values) {
        if (values!= null) {
            for (Node value: values) {
                getNodes().add(value);
            }
        }
        return this;
    }

    public NodeList withNodes(Collection<Node> values) {
        if (values!= null) {
            getNodes().addAll(values);
        }
        return this;
    }

}
