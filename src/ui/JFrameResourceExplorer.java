package ui;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import src.RDFEnpointInpector;
import src.RDFFileInspector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author mauricio
 */
public class JFrameResourceExplorer extends javax.swing.JFrame {
    
    public String prefixes;
    DefaultListModel listModel = new DefaultListModel();
    public String endpointURL;
    public boolean isLocalFile;
    public String filepath;
    
    public JFrameResourceExplorer() {
        initComponents();
        
        this.jList1.setModel(listModel);
        this.isLocalFile = false;
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jTextField = new javax.swing.JTextField();
        jButtonInspect = new javax.swing.JButton();
        jLabelTiempo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Explorador de recursos");

        jScrollPane1.setViewportView(jList1);

        jTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextFieldKeyPressed(evt);
            }
        });

        jButtonInspect.setText("Buscar");
        jButtonInspect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButtonInspectMouseClicked(evt);
            }
        });

        jLabelTiempo.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabelTiempo.setText("--");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButtonInspect)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelTiempo)
                .addGap(113, 113, 113))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonInspect))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelTiempo))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldKeyPressed
        if (evt.getKeyChar() == KeyEvent.VK_ENTER){
            this.jButtonInspectMouseClicked(null);
        }
    }//GEN-LAST:event_jTextFieldKeyPressed

    private void jButtonInspectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonInspectMouseClicked
        String inputText = this.jTextField.getText();
        long inicio = System.currentTimeMillis();
        if (isLocalFile){
            ArrayList<String> properties = RDFFileInspector.getProperties(this.filepath, inputText);
            if (properties.size() > 0)
                this.addElements(this.listModel, properties);
            else {
                ArrayList<String> resources = RDFFileInspector.getResources(this.filepath, inputText);
                if (resources.size() > 0)
                    this.addElements(listModel, resources);
            }
        }
        else { // endpoint
            ArrayList<String> properties = RDFEnpointInpector.getProperties(this.endpointURL, this.prefixes, inputText);
            if (properties.size() > 0)
                this.addElements(listModel, properties);
            else {
                ArrayList<String> resources = RDFEnpointInpector.getResources(this.endpointURL, this.prefixes, inputText);
                if (resources.size() > 0)
                    this.addElements(listModel, resources);
            }
        }
        
        long fin = System.currentTimeMillis();
        this.jLabelTiempo.setText("" + (fin - inicio)/1000.0 + "[segs]");
        
//        long inicio = System.currentTimeMillis();
//        
//        try {
//            Query query = QueryFactory.create(queryString);
//            QueryExecution qexec;
//            
//            if (isLocalFile){    // rdf file
//                qexec = QueryExecutionFactory.create(query);
//            }
//            else                // endpoint
//                qexec = QueryExecutionFactory.sparqlService(this.endpointURL, query);
//            
//            ResultSet results = qexec.execSelect();
//
//            listModel.clear();
//            int rows = 0;
//            while( results.hasNext() ) {
//                QuerySolution row = results.next();
//                RDFNode prop = row.get("y");
//                this.listModel.addElement("<" + prop.toString() + ">");
//                rows++;
//            }
//            
//            if (rows == 0)
//                JOptionPane.showMessageDialog(this, "El recurso no existe", "Error", JOptionPane.ERROR_MESSAGE);
//
//            qexec.close() ;
//
//            // no se pudo parsear la query debido al valor introducido en el textfield. NO es un URI
//        } catch (QueryParseException ex){  // si no es un recurso, buscar los recursos posibles
//            System.out.println("buscando recursos...");
//            queryString = "select distinct * where { ?x ?y ?z FILTER regex(?x, \"" + inputText + "\", \"i\")  } LIMIT 100";
//            
//            if (isLocalFile){
//                queryString = "select distinct ?x from <file:" + filepath + "> where { ?x ?y ?z FILTER regex(str(?x), \"" + inputText + "\", \"i\")  } LIMIT 100";
//            }
//            
//            
//            Query query = QueryFactory.create(queryString);
//            QueryExecution qexec;
//            
//            if (isLocalFile)
//                qexec = QueryExecutionFactory.create(query);
//            else
//                qexec = QueryExecutionFactory.sparqlService(this.endpointURL, query);
//            
//            System.out.println("antes del select");
//            qexec.setTimeout(-1);
//            ResultSet results = qexec.execSelect();
//            System.out.println("despues del select");
//
//            listModel.clear();
//            int rows = 0;
//            while (results.hasNext()) {
//                QuerySolution row = results.next();
//                RDFNode prop = row.get("x");
//                this.listModel.addElement("<" + prop.toString() + ">");
//                rows++;
//            }
//            System.out.println("rows: " + rows);
//        }
//        
//        long fin = System.currentTimeMillis();
//        this.jLabelTiempo.setText("" + (fin - inicio)/1000.0 + "[segs]");
    }//GEN-LAST:event_jButtonInspectMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameResourceExplorer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameResourceExplorer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameResourceExplorer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameResourceExplorer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameResourceExplorer().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonInspect;
    private javax.swing.JLabel jLabelTiempo;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField;
    // End of variables declaration//GEN-END:variables

    private void addElements(DefaultListModel listModel, ArrayList<String> elements) {
        this.listModel.clear();
        for (String e : elements)
            listModel.addElement(e);
    }
}
