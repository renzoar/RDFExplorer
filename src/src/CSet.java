package src;


import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mauricio
 */
public class CSet {
    
    private String name;
    private ArrayList<String> properties;

    public CSet(String name, ArrayList<String> properties) {
        this.name = name;
        this.properties = properties;
    }
    
    public CSet(String name){
        this.name = name;
        this.properties = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getProperties() {
        return properties;
    }

    public void setProperties(ArrayList<String> properties) {
        this.properties = properties;
    }

    public int getPropertiesSize(){
        return this.properties.size();
    }

    public CSet addProperties(ArrayList<String> properties){
        for (String property : properties){
            if (!this.properties.contains(property)){
                this.properties.add(property);
            }
        }
        
        return this;
    }
    
    public float similarity(CSet cSet){
        int coincidences = 0;
        
        for (String property : cSet.getProperties()){
            if (this.properties.contains(property)){
                coincidences++;
            }
        }
        
        //System.out.println("coincidences " + this.name + " and " + cSet.name + " = " + coincidences + " de " + this.properties.size());
        return (float) coincidences/this.properties.size();
    }

    public CSet setName(String name) {
        this.name = name;
        return this;
    }
    
}
