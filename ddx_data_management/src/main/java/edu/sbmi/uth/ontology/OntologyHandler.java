/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.sbmi.uth.ontology;

import edu.sbmi.uth.util.Config;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 *
 * @author tuan
 */
public class OntologyHandler {

    ///Users/mac/Desktop/ddx-120122.owl
    IRI iri_ddx = IRI.create("https://medicine.wustl.edu/ontology/ddx.owl");
    IRI iri_swo = IRI.create("http://www.ebi.ac.uk/swo/license/");
    IRI iri_obo = IRI.create("http://purl.obolibrary.org/obo/");

    private static OntologyHandler INSTANCE = null;

    private OWLOntology ontology = null;

    public OWLOntology getOntology() {
        return ontology;
    }

    public OWLOntologyManager getOntologymanager() {
        return ontologymanager;
    }

    private OWLOntologyManager ontologymanager = null;

    private OWLDataFactory datafactory = null;

    public static OntologyHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OntologyHandler();
        }

        return INSTANCE;
    }

    private OntologyHandler() {
        String ontologyfile = Config.getInstance().getOntologyFile();
        
        initFromPath(ontologyfile);
    }
    
    public OWLDataFactory getDataFactory(){
        return this.datafactory;
    }

    public void initFromPath(String ontologyFilePath) {
        ontologymanager = OWLManager.createOWLOntologyManager();

        try {
            this.ontology = ontologymanager.loadOntologyFromOntologyDocument(new File(ontologyFilePath));

        } catch (OWLOntologyCreationException ex) {
            Logger.getLogger(OntologyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.datafactory = ontology.getOWLOntologyManager().getOWLDataFactory();
    }

    public void saveOntology(String fileName) {
        try {

            this.ontologymanager.saveOntology(this.ontology, new OWLXMLDocumentFormat(), new FileOutputStream(new File(fileName)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OntologyHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(OntologyHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public IRI getIRI_ddx() {
        return iri_ddx;
    }

    public IRI getIRI_swo() {
        return iri_swo;
    }

    public IRI getIRI_obo() {
        return iri_obo;
    }
    
    public static void main(String[] args) {
        String ontologyfile = Config.getInstance().getOntologyFile();
        
        OntologyHandler oh = OntologyHandler.getInstance();
        
        oh.initFromPath(ontologyfile);
    }

}
