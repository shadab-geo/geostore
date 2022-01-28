//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2022.01.27 at 06:33:24 PM IST 
//


package it.geosolutions.geostore.soap.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WhereQueryListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WhereQueryListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://wse.vlaanderen.be/BeschikbareEntitlementArtefactService/common/DataTypes/v100}WhereQuery" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WhereQueryListType", propOrder = {
    "whereQuery"
})
public class WhereQueryListType {

    @XmlElement(name = "WhereQuery", required = true)
    protected List<WhereQueryType> whereQuery;

    /**
     * The list of WhereQueries. Gets the value of the whereQuery property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the whereQuery property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWhereQuery().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WhereQueryType }
     * 
     * 
     */
    public List<WhereQueryType> getWhereQuery() {
        if (whereQuery == null) {
            whereQuery = new ArrayList<WhereQueryType>();
        }
        return this.whereQuery;
    }

}
