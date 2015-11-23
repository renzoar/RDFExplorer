/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import java.util.ArrayList;

/**
 *
 * @author mauricio
 */
public class RDFFileInspector {
    
    public static ArrayList<String> getResources(String filepath, String pattern){
        String queryString = "select distinct ?x from <file:" + filepath + "> where { ?x ?y ?z FILTER regex(str(?x), \"" +
                              pattern + "\", \"i\")  } LIMIT 100";
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query);
        ResultSet results = qexec.execSelect();
        
        ArrayList<String> resources = new ArrayList<>();
        
        while ( results.hasNext() ) {
            QuerySolution row = results.next();
            RDFNode prop = row.get("x");    
            resources.add("<" + prop.toString() + ">");
        }
        
        return resources;
    }
    
    
    public static ArrayList<String> getProperties(String filepath, String resource){
        String queryString = "select distinct ?y from <file:" + filepath + "> where { " + resource + " ?y ?z } limit 100" ;
        System.out.println("queryString = " + queryString);
        
        ArrayList<String> properties = new ArrayList<>();
        
        try{
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.create(query);
            ResultSet results = qexec.execSelect();

            

            while ( results.hasNext() ) {
                QuerySolution row = results.next();
                RDFNode prop = row.get("y");
                properties.add("<" + prop.toString() + ">");
            }
        }
        catch (QueryParseException ex){
        }
        
        return properties;
    }
    
    
    
}
