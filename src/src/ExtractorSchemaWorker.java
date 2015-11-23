/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import java.util.ArrayList;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import ui.JDialogExtractSchemaStatus;
import ui.JFrame;

/**
 *
 * @author mauricio
 */
public class ExtractorSchemaWorker extends SwingWorker<String, Integer>{

    private JProgressBar progressBar;
    private JDialogExtractSchemaStatus schemaStatus;
    private SchemaExtractor extractor;
    private ArrayList<CSet> schema ;
    private JFrame jFrame;
    
    public ExtractorSchemaWorker(JDialogExtractSchemaStatus schemaStatus, SchemaExtractor extractor, JFrame jFrame) {
        this.progressBar = schemaStatus.getProgressBar();
        this.extractor = extractor;
        this.schemaStatus = schemaStatus;
        this.jFrame = jFrame;
    }
    
    
    @Override
    protected String doInBackground() throws Exception {
        extractor.setWorker(this);
        this.schema = extractor.getSchema();
        return "";
    }
    
    public void publish(int i) {
        this.progressBar.setValue(i);
        if (i > 99){
            this.schemaStatus.setVisible(false);
        }
    }
    
    public void publish(String text){
        this.schemaStatus.setStatus(text);
    }

    public ArrayList<CSet> getResult() {
        return schema;
    }
    
    @Override
    protected void done() {
        this.jFrame.writeSchema(this.schema);
    }
    
    
    
}
