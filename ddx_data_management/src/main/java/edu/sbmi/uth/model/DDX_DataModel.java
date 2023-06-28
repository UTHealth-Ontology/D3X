/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.sbmi.uth.model;

import java.util.HashSet;
import java.util.Set;
import org.semanticweb.owlapi.model.OWLClass;

/**
 *
 * @author tuan
 */
public class DDX_DataModel {

    public String getDDX_ID() {
        return DDX_ID;
    }

    public void setDDX_ID(String DDX_ID) {
        this.DDX_ID = DDX_ID;
    }

    public String getLABEL() {
        if(LABEL.isBlank()){
            //return LABEL;
            return this.DDX_ID;
        }
        
        return LABEL;
        
    }
    
    public boolean hasLabel(){
        if(LABEL.isBlank()){
            return false;
        }
        
        if(LABEL.isEmpty()){
            return false;
        }
        
        return true;
    }

    public void setLABEL(String LABEL) {
        this.LABEL = LABEL;
    }

    public String getLINK() {
        return LINK;
    }

    public void setLINK(String LINK) {
        this.LINK = LINK;
    }

    public OWLClass getTypeClass() {
        return typeClass;
    }

    public void setTypeClass(OWLClass typeClass) {
        this.typeClass = typeClass;
    }

    public Set<String> getNotes() {
        return notes;
    }

    public void setNotes(Set<String> notes) {
        this.notes.addAll(notes);
    }
    
    

    public OWLClass getLicense() {
        return license;
    }

    public void setLicense(OWLClass license) {
        this.license = license;
    }
    
    public void setDDXClass(OWLClass DDX_Class){
        this.DDX = DDX_Class;
    }
    
    public OWLClass getDDX(){
        return this.DDX;
    }
    
    private String DDX_ID = "";
    private String LABEL = "";
    private String LINK = "";
    private OWLClass typeClass;
    private Set<String> notes = new HashSet<String>();
    private OWLClass license;
    private OWLClass DDX;
    
    public DDX_DataModel(){
        
    }
    
    
}
