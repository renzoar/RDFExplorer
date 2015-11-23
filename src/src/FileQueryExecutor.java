package src;


import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.util.ArrayList;
import org.apache.jena.riot.RDFDataMgr;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mauricio
 */
public class FileQueryExecutor extends QueryExecutor{
 
    private String filename;
    private Model model; 
    
    public FileQueryExecutor(String filename){
        this.filename = filename;
        this.model = RDFDataMgr.loadModel(filename) ;
    }
    
    public FileQueryExecutor(Model model){
        this.model = model;
    }
    
    public ArrayList<String> getResources(){
        
        ArrayList<String> resources = new ArrayList<>();
        
        String queryString = "select distinct ?x " + 
                              "where { ?x ?y ?z }";
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, this.model);
        
        
        ResultSet results = qe.execSelect();
        
        while (results.hasNext()){
            QuerySolution next = results.next();
            resources.add(next.getResource("?x").toString());
        }
        
        return resources;
    }

    public ArrayList<String> getProperties(String resource) {
        
        ArrayList<String> properties = new ArrayList<>();
        
        String queryString = "select distinct ?y " +
                             "where { <" + resource + "> ?y ?z }";
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, this.model);
        
        ResultSet results = qe.execSelect();
        
        while (results.hasNext()){
            QuerySolution next = results.next();
            properties.add(next.get("?y").toString());
        }
        
        return properties;
    }
    
    public String getObject(String resource, String property){
        String queryString = "select distinct ?z "+
                             "where { " + resource + " " + property + " ?z } limit 1";
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(query, this.model);
        
        ResultSet results = qe.execSelect();
        String value = results.next().get("?z").toString();
        
//        if (value.contains("#"))
//            return value.substring(0, value.indexOf("#"));
        return value;
    } 
}
