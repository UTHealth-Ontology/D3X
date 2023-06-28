/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.sbmi.uth.misc;

import edu.sbmi.uth.model.DDX_DataModel;
import edu.sbmi.uth.ontology.OntologyHandler;
import edu.sbmi.uth.util.Config;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;

/**
 *
 * @author tuan
 */
public class ExternalImportManagement {
    
    private static ExternalImportManagement INSTANCE = null;

    public Set<DDX_DataModel> getDDX_Data() {
        return ddx_data;
    }
    
    
    private Set <DDX_DataModel> ddx_data = null;
    
    DataFormatter formatter = new DataFormatter();
    
    
    
    private ExternalImportManagement(){
        
    }
    
    public static ExternalImportManagement getInstance(){
        if(INSTANCE == null){
            INSTANCE = new ExternalImportManagement();
        }
        
        return INSTANCE;
    }
    
    public void importFile(String pathToFile){
        
        ddx_data = new HashSet<DDX_DataModel>();
        OntologyHandler oh = OntologyHandler.getInstance();
        OWLDataFactory odf = oh.getDataFactory();
        
        FileInputStream file = null;
        try {
            file = new FileInputStream(new File(pathToFile));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            
            for(org.apache.poi.ss.usermodel.Row row: sheet){
                
                if(row.getRowNum() !=0)
                {
                    DDX_DataModel ddx_datamodel = null;
                    ddx_datamodel = new DDX_DataModel();
                    //System.out.println(row.getCell(1).getStringCellValue());
                    ddx_datamodel.setDDX_ID(row.getCell(1).getStringCellValue());
                    ddx_datamodel.setLABEL(formatter.formatCellValue(row.getCell(2)));
                    ddx_datamodel.setLINK(formatter.formatCellValue(row.getCell(3)));
                    
                    Set<String>notes = new HashSet<String>();
                    if(formatter.formatCellValue(row.getCell(6)) != null){
                        notes.add(formatter.formatCellValue(row.getCell(6)));
                    }
                    
                    if(formatter.formatCellValue(row.getCell(7)) != null){
                        notes.add(formatter.formatCellValue(row.getCell(7)));
                    }
                    
                    if(notes.size()>0){
                        ddx_datamodel.setNotes(notes);
                    }
                    
                    //add licensing information
                    //oh.getIRI_swo();
                    if(formatter.formatCellValue(row.getCell(8)) != null &&
                            formatter.formatCellValue(row.getCell(8)).length()>0
                            && !formatter.formatCellValue(row.getCell(8)).isEmpty()){
                        
                        
                        OWLClass licence = odf.getOWLClass(
                                oh.getIRI_swo().toString() + formatter.formatCellValue(row.getCell(8)));
                        ddx_datamodel.setLicense(licence);
                        
                    }
                    
                    //type of information
                    //System.out.println(oh.getIRI_ddx());
                    if(formatter.formatCellValue(row.getCell(9)) != null &&
                            formatter.formatCellValue(row.getCell(9)).length()>0
                            && !formatter.formatCellValue(row.getCell(9)).isEmpty()){
                        
                            //System.out.println(oh.getIRI_ddx()+"#"+formatter.formatCellValue(row.getCell(9)));

                            OWLClass ddxClass = odf.getOWLClass(oh.getIRI_ddx()+"#"+formatter.formatCellValue(row.getCell(9)));
                            ddx_datamodel.setTypeClass(ddxClass);
                    }
                    
                    //ddx association
                    //oh.getIRI_ddx()
                    if(formatter.formatCellValue(row.getCell(4)) != null &&
                            formatter.formatCellValue(row.getCell(4)).length()>0
                            && !formatter.formatCellValue(row.getCell(4)).isEmpty()){
                        
                        OWLClass ddxClass = odf.getOWLClass(oh.getIRI_ddx()+"#"+formatter.formatCellValue(row.getCell(4)));
                        ddx_datamodel.setDDXClass(ddxClass);
                        
                    }
                    
                    this.ddx_data.add(ddx_datamodel);
                }
                
                
            }
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExternalImportManagement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExternalImportManagement.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
        
    }
    
    public static void main(String[] args) {
        String datafile = Config.getInstance().getDataFile();
        
        String pathToData = datafile;
        
        ExternalImportManagement em = ExternalImportManagement.getInstance();
        
        em.importFile(pathToData);
    }
}
