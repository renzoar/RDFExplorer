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
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author mauricio
 */
public class EndpointQueryExecutor extends QueryExecutor{

    private String endpoint;
    private String defaulGraph;

    public EndpointQueryExecutor(String endpoint, String defaulGraph) {
        this.endpoint = endpoint;
        this.defaulGraph = defaulGraph;
    }
    
    public ArrayList<String> getResources() {
        ArrayList<String> resources = new ArrayList<>();
        
        String queryString = "select distinct ?x " + 
                              "where { ?x ?y ?z }";
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.sparqlService(this.endpoint, query, this.defaulGraph);
        
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
        QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query, this.defaulGraph);

        ResultSet results = qe.execSelect();
        while (results.hasNext()){
            QuerySolution next = results.next();
            properties.add(next.get("?y").toString());
        }
        
        qe.close();        
        return properties;
    }

    public String getObject(String resource, String property) {
        String queryString = "select distinct ?z "+
                             "where { " + resource + " " + property + " ?z } limit 1";
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query, this.defaulGraph);
        
        ResultSet results = qe.execSelect();
        String value = results.next().get("?z").toString();
        
//        if (value.contains("#"))
//            return value.substring(0, value.indexOf("#"));
        return value;
    }
    
}
