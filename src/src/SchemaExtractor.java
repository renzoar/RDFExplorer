package src;


import java.util.ArrayList;
import java.util.BitSet;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mauricio
 */
public class SchemaExtractor {
    
    private QueryExecutor queryExecutor;
    private float mergeValue;
    ExtractorSchemaWorker worker;
    
    public SchemaExtractor(QueryExecutor queryExecutor, float mergeValue){
        this.queryExecutor = queryExecutor;
        this.mergeValue = mergeValue;
    }
    
    
    
    public void setWorker(ExtractorSchemaWorker worker){
        this.worker = worker;
    }

    /*
    Metodo que extrae todos los CS Basicos (paso 1)
    */
    private ArrayList<CSet> extract() {
        ArrayList<CSet> sets = new ArrayList<>();
        int i = 0;
        this.worker.publish("Discovering resources...");
        ArrayList<String> resources = this.queryExecutor.getResources();    // obtener todos los recursos/sujetos
        
        System.out.println("[+] recursos obtenidos: " + resources.size());
        
        int total = resources.size();
        int num = 1;
        
        this.worker.publish("Generating CSs...");
        for (String resource : resources){  // para cada sujeto buscar todas sus propiedades
            
            int x = num*100/total;
            this.worker.publish(x);
            
            
            //if (num % 100 == 0)
                System.out.println("num: " + num + " [" + x+ "%]");
            num++;
            
//            System.out.println("generando CS para recurso: " + resource);
            ArrayList<String> properties = this.queryExecutor.getProperties(resource);
//            System.out.println("listo");
            
            
            if (properties.size() > 0){
                if (properties.contains("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")){    // usar propiedad rdf:type para etiquetar Set de propiedades
                    sets.add(new CSet(queryExecutor.getObject("<"+resource+">", "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>"), properties));
                }
                else {
                    sets.add(new CSet("CS"+i, properties));
                    i++;
                }
                
            }
        }
        
        System.out.println("[+] CS generados");
        System.out.println("");
        return sets;
    }
    
    
    /*
    Metodo encargado de combinar los CS (Characteristic Set's)
    para producir un esquema mas compacto 
    */
    private ArrayList<CSet> merge(ArrayList<CSet> basicSets){
        System.out.println("merging...");
        int size = basicSets.size();
        ArrayList<CSet> merged = new ArrayList<>();
        BitSet bitSet = new BitSet(size);
        int iName = 0;
        
        
        // refactorizar!
        for (int i = 0; i < size; i++) {
            for (int j = 1; j < size; j++) {
                CSet cSet1 = basicSets.get(i);
                CSet cSet2 = basicSets.get(j);  // tomar dos CS
                if ( (i != j) && !bitSet.get(i) && !bitSet.get(j) && cSet1.similarity(cSet2) >= mergeValue){     
                    // caso 1: ambos tienen nombres genericos
                    if (cSet1.getName().startsWith("CS") && cSet2.getName().startsWith("CS")){
                        merged.add(new CSet("CS"+iName).addProperties(cSet1.getProperties())
                            .addProperties(cSet2.getProperties()));
                        iName++;
                        bitSet.set(i);
                        bitSet.set(j);
                    }
                    // caso 2: al menos uno tiene nombre real
                    else if (!cSet1.getName().startsWith("CS") || !cSet2.getName().startsWith("CS")){
                        String name;
                        if (!cSet1.getName().startsWith("CS") && cSet2.getName().startsWith("CS")){ // :1 real y :2 generico
                            name = cSet1.getName();
                            System.out.println("name = " + name);
                            merged.add(new CSet(name).addProperties(cSet1.getProperties())
                                .addProperties(cSet2.getProperties()));
                            bitSet.set(i);
                            bitSet.set(j);
                        }
                        else if (cSet1.getName().startsWith("CS") && !cSet2.getName().startsWith("CS")){ // :1 generico y :2 real
                            name = cSet2.getName();
                            //System.out.println("name = " + name);
                                merged.add(new CSet(name).addProperties(cSet1.getProperties())
                                .addProperties(cSet2.getProperties()));
                            bitSet.set(i);
                            bitSet.set(j);
                        }
                        else { // caso 3: ambos tienen nombre real. No mezclar si no son iguales
                            if (cSet1.getName().equals(cSet2.getName())){  // reales e iguales
                                merged.add(new CSet(cSet1.getName()).addProperties(cSet1.getProperties())
                                    .addProperties(cSet2.getProperties()));
                                bitSet.set(i);
                                bitSet.set(j);
                            }
                        }
                        
                    }
                }
            }
        }
        

        // incluir en el merge aquellos CS que no se pudieron agrupar
        for (int i = 0; i < size; i++) {
            if (!bitSet.get(i) && basicSets.get(i).getProperties().size() > 0){
                if (basicSets.get(i).getName().startsWith("CS")){
                    merged.add(basicSets.get(i).setName("CS" + iName));
                    iName++;
                }
                else {
                    merged.add(basicSets.get(i));
                }
            }
        }
        
        return merged;
    }
    
    
    /*
    Metodo encargado de retornar el esquema
    paso 1: extraer todos los distintos sujetos
    paso 2: para cada sujeto, crear un CS (Characterist Set) en base a sus propiedades
    paso 3: Mezclar aquellos CS que tengan un grado de similitud
    Repetir el paso 3 tantas veces como sea posible para obtener un esquema comprimido.
    */
    public ArrayList<CSet> getSchema(){
        // get basic CSs from each distinct resource
        ArrayList<CSet> basicCSs = this.extract();
        
        // merging once
        this.worker.publish("Merging and labeling...");
        ArrayList<CSet> merged = this.merge(basicCSs);
        ArrayList<CSet> aux;

//      merging several times to generate a reduced schema
        while (!(aux = this.merge(merged)).equals(merged)){
            merged = aux;
        }
        
        return merged;
    }
    
}
