/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.sbmi.uth.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mac
 */
public class Config {
    
    InputStream is = null;
    static public Config INSTANCE = null;
    
    private String dataFile;
    private String ontologyFile;
    
    private Config(){
        
        
        is = this.getClass().getClassLoader().getResourceAsStream("config.properties");
        
        if(is !=null){
            Properties properties = new Properties();
            try {
                
                properties.load(is);
                dataFile = properties.getProperty("datafile");
                ontologyFile = properties.getProperty("ontology");
                
            } catch (IOException ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
    }

    public String getDataFile() {
        return dataFile;
    }

    public String getOntologyFile() {
        return ontologyFile;
    }
    
    
    
    static public Config getInstance(){
        
        if(INSTANCE == null){
            INSTANCE = new Config();
        }
        
        return INSTANCE;
        
        
    }
    
    public static void main(String[] args) {
        
        Config c = Config.getInstance();
        
        System.out.println(c.getDataFile());
        System.out.println(c.getOntologyFile());
        
    }
    
}
