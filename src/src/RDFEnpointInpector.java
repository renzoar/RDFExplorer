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
import java.awt.image.ImageObserver;
import java.util.ArrayList;

/**
 *
 * @author mauricio
 */
public class RDFEnpointInpector {
    
    public static ArrayList<String> getResources(String endpointURL, String prefixes, String pattern){
        String queryString = "select distinct * where { ?x ?y ?z FILTER regex(?x, \"" + pattern + "\", \"i\")  } LIMIT 100";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.sparqlService(endpointURL, query);
        qexec.setTimeout(-1);
        ResultSet results = qexec.execSelect();
        
        ArrayList<String> resources = new ArrayList<>();
        
        while (results.hasNext()) {
            QuerySolution row = results.next();
            RDFNode prop = row.get("x");
            resources.add("<" + prop.toString() + ">");
        }
        
        return resources;
    }
    
    public static ArrayList<String> getProperties(String endpointURL, String prefixes, String resource){
        String queryString = prefixes + "\nselect distinct ?y where { " + resource + " ?y ?z } limit 100";
        ArrayList<String> properties = new ArrayList<>();
        
        try{
            Query query = QueryFactory.create(queryString);
            QueryExecution qexec = QueryExecutionFactory.sparqlService(endpointURL, query);
            ResultSet results = qexec.execSelect();

            while( results.hasNext() ) {
                QuerySolution row = results.next();
                RDFNode prop = row.get("y");
                properties.add("<" + prop.toString() + ">");
            }
        }
        catch(QueryParseException ex){
        }
        
        return properties;
    }
    
    
    
}
