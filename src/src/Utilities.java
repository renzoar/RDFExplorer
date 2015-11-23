package src;


import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.tdb.TDBFactory;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;


public class Utilities {
    
    // rdf extensions
    public static String TURTLE_EXTENSION = ".ttl";
    public static String RDF_XML_EXTENSION = ".rdf";
    public static String NTRIPLES_EXTENSION = ".n3";
    public static String RDF_JSON_EXTENSION = ".rj";
    
    // program states
    public static int IDLE = -1;
    public static int USING_JENA_RAM = 0;
    public static int USING_JENA_TDB = 1;
    public static int USING_ENDPOINT = 2;
    
    // default strings
    public static String APP_TITLE = "RDF TOOL";
    public static String DEFAULT_SPARQL_QUERY = "select distinct *\nwhere { ?x ?y ?z }\nlimit 100";
    
    // Return the selected JRadioButton in a ButtonGroup
    public static JRadioButton getSelection(ButtonGroup group) {
        for (Enumeration e=group.getElements(); e.hasMoreElements(); ) 
        {
            JRadioButton b = (JRadioButton)e.nextElement();
            if (b.getModel() == group.getSelection()) 
            {
                return b;
            }
        }

        return null;
    }

    public static String getFileExtension(String filePath) {
        int i = filePath.lastIndexOf(".");
        return filePath.substring(i);
    }

    public static String removeExtension(String filePath) {
        int i = filePath.lastIndexOf(".");
        return filePath.substring(0, i);
    }
    
    public static ArrayList<String> getNamedGraphsTDB(String path) {
        // select all graph URI's
        String queryString = "select distinct ?g where { graph ?g { ?x ?y ?z }}";
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.create(
                                            query,
                                            TDBFactory.createDataset(path));
        ResultSet results = qe.execSelect();
        
        ArrayList<String> graphNames = new ArrayList<>();
        
        while (results.hasNext()){
            QuerySolution next = results.next();
            graphNames.add("" + next.get("?g"));
        }
        
        qe.close();
        return graphNames;
    }
    
    public static ArrayList<String> getNamedGraphsEndpoint(String URL){
        // select all graph URI's
        String queryString = "select distinct ?g where { graph ?g { ?x ?y ?z }}";
        
        Query query = QueryFactory.create(queryString);
        QueryExecution qe = QueryExecutionFactory.sparqlService(URL, query);
        ResultSet results = qe.execSelect();
        
        ArrayList<String> graphNames = new ArrayList<>();
        
        while (results.hasNext()){
            QuerySolution next = results.next();
            graphNames.add("" + next.get("?g"));
        }
        
        qe.close();
        return graphNames;
    }
    
    public static HashMap<String, String> getDefaultPrefixes(){
        HashMap<String, String> prefixes = new HashMap<>();
        prefixes.put("http://purl.org/dc/elements/1.1/", "dc:");
        prefixes.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "rdf:");
        prefixes.put("http://xmlns.com/foaf/0.1/", "foaf:");
        prefixes.put("http://www.w3.org/2000/01/rdf-schema#", "rdfs:");
        prefixes.put("http://dbpedia.org/ontology/", "dbpedia-owl:");
        prefixes.put("http://www.w3.org/2004/02/skos/core#", "skos:");
        prefixes.put("http://purl.org/stuff/rev#", "rev:");
        prefixes.put("http://dbpedia.org/property/", "dbpedia2:");
        prefixes.put("http://dbpedia.org/resource/", ":");
        
        return prefixes;
    }
}
