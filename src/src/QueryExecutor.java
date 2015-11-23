/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;

/**
 *
 * @author mauricio
 */
public abstract class QueryExecutor {

    public abstract ArrayList<String> getResources();

    public abstract ArrayList<String> getProperties(String resource);

    public abstract String getObject(String resource, String property);
    
    
    
}
