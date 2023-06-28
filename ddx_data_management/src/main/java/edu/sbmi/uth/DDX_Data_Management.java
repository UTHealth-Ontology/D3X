/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.sbmi.uth;

import edu.sbmi.uth.misc.ExternalImportManagement;
import edu.sbmi.uth.model.DDX_DataModel;
import edu.sbmi.uth.ontology.OntologyHandler;
import edu.sbmi.uth.util.Config;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 *
 * @author tuan
 */
public class DDX_Data_Management {

    private OntologyHandler oh;

    private ExternalImportManagement em;

    private IRI schema_photo;

    private IRI denoted_by;

    private URL repodata;
    
    public DDX_Data_Management() {
        //https://schema.org/photo

        initializeOntologyModel();
        schema_photo = IRI.create("https://schema.org/photo");
        denoted_by = IRI.create("http://purl.obolibrary.org/obo/IAO_0000235");
    }

    public void initializeOntologyModel() {
        oh = OntologyHandler.getInstance();
    }

    public void importSpreadsheetData() {
        String data_file = Config.getInstance().getDataFile();
        
        em = ExternalImportManagement.getInstance();
        em.importFile(data_file);
    }

    public void importFromRepo(){
        
    }
    
    public void addInstance() {

        Set<DDX_DataModel> DDX_Data = em.getDDX_Data();
        for (DDX_DataModel ddm : DDX_Data) {
            this.addDDXImageData(ddm);
        }

    }

    public void modifyDDXClassRestriction() {
        Set<DDX_DataModel> DDX_Data = em.getDDX_Data();
        for (DDX_DataModel ddm : DDX_Data) {
            this.linkClassAssertionForDDX(ddm);
        }
    }

    private void addDDXImageData(DDX_DataModel dm) {

        OWLDataFactory odf = oh.getDataFactory();

        OWLNamedIndividual ddxdata = odf.getOWLNamedIndividual(dm.getDDX_ID());

        OWLClass ddxdata_class = dm.getTypeClass();

        OWLAnnotation label = odf.getRDFSLabel(dm.getLABEL());

        OWLLiteral linkLiteral = odf.getOWLLiteral(dm.getLINK());
        //System.out.println(linkLiteral.toString());
        OWLAnnotation linkPhotoAnnotation = odf.getOWLAnnotation(odf.getOWLAnnotationProperty(this.schema_photo), linkLiteral);
        //System.out.println(linkPhotoAnnotation.toString());

        OWLOntology ontology = oh.getOntology();
        //link class type
        OWLClassAssertionAxiom linkClassType = odf.getOWLClassAssertionAxiom(ddxdata_class, ddxdata);
        ////add to ontology

        oh.getOntologymanager().addAxiom(ontology, linkClassType);

        //link label
        if (dm.hasLabel()) {
            OWLLiteral labelLiteral = odf.getOWLLiteral(dm.getLABEL());
            OWLAnnotation labelAnnotation = odf.getOWLAnnotation(odf.getRDFSLabel(), labelLiteral);
            OWLAnnotationAssertionAxiom owlLabel = odf.getOWLAnnotationAssertionAxiom(ddxdata.getIRI(), labelAnnotation);
            ////add ontology
            oh.getOntologymanager().applyChange(new AddAxiom(ontology, owlLabel));
        }

        //link the photo
        OWLAnnotationAssertionAxiom linkphoto = odf.getOWLAnnotationAssertionAxiom(ddxdata.getIRI(), linkPhotoAnnotation);
        ////add to ontology
        oh.getOntologymanager().applyChange(new AddAxiom(ontology, linkphoto));

        //CLASS LEVEL LINK-----------------------
        OWLClass ddx = dm.getDDX();

        OWLObjectProperty denotedByProperty = odf.getOWLObjectProperty(this.denoted_by);
        //odf.getOWLObjectSomeValuesFrom(denotedByProperty, ddxdata);
        OWLObjectHasValue ddx_denotedby = odf.getOWLObjectHasValue(denotedByProperty, ddxdata);

        OWLObjectIntersectionOf intersection = odf.getOWLObjectIntersectionOf(ddx_denotedby);

        oh.getOntologymanager().applyChange(new AddAxiom(ontology, odf.getOWLSubClassOfAxiom(ddx, intersection)));

    }

    public void linkClassAssertionForDDX(DDX_DataModel dm) {

        OWLDataFactory odf = oh.getDataFactory();
        OWLNamedIndividual ddxdata = odf.getOWLNamedIndividual(dm.getDDX_ID());
        OWLClass ddx = dm.getDDX();

    }

   

    public void exportOWLXML(String fileName) {
        OWLXMLDocumentFormat owlXMLFormat = new OWLXMLDocumentFormat();

        File outputFile = new File(fileName + ".owl");

        try {
            oh.getOntologymanager().saveOntology(oh.getOntology(), owlXMLFormat, new FileOutputStream(outputFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DDX_Data_Management.class.getName()).log(Level.SEVERE, null, ex);
        } catch (OWLOntologyStorageException ex) {
            Logger.getLogger(DDX_Data_Management.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        DDX_Data_Management ddd = new DDX_Data_Management();

        ddd.importSpreadsheetData();

        ddd.addInstance();

        ddd.exportOWLXML("prelim_import.owl");
    }

}
