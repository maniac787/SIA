/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dmrr.asistenciasx;

import bareMysqlTables.Profesor;
import com.digitalpersona.onetouch.DPFPFeatureSet;
import com.digitalpersona.onetouch.DPFPGlobal;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.scene.input.KeyCode;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import static javax.swing.SwingConstants.CENTER;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.ResultType;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.JTableBinding.ColumnBinding;
import org.jdesktop.swingbinding.SwingBindings;

/**
 *
 * @author diego
 */
public class ListaYCapturaDeAsistencias extends javax.swing.JFrame {

    private javax.persistence.EntityManager entityManager;
    private javax.persistence.Query registrosQuery;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;

    StartScreen parent;

    MonitorDeHuella monitorDeHuella;
    EntityManager em;
    List profesorList;
    List templates = null;
    private List<Map> listaDeRegistros;
    JLabel jLabelHuellaNoReconocida;
    Query profesorQuery;

    public ListaYCapturaDeAsistencias() {
        this.setUndecorated(true);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        Clock clock = new Clock(jLabelReloj, "hh:mm:ss MM/dd/yyyy");
        this.generaTablaRegistros();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //jScrollPane1.setViewportView(new MensajeMantengaPresionado());
        monitorDeHuella = new MonitorDeHuella(jTextArea1, this);
        em = javax.persistence.Persistence.createEntityManagerFactory("asistenciasx?zeroDateTimeBehavior=convertToNullPU").createEntityManager();

        profesorQuery = em.createQuery("SELECT p FROM Profesor p WHERE p.huella is not null");

        ventanaDeProfesor = new VentanaDeAsistenciaDeProfesor();
        createjLabelHuellaNoReconocida();

        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
        Action escapeAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (parent == null) {
                    System.exit(0);
                };
                if (parent.parent == null) {
                    parent.setAlwaysOnTop(true);
                    parent.setVisible(true);
                    setVisible(false);
                    dispose();
                    return;
                }
                parent.parent.setAlwaysOnTop(true);
                parent.parent.setVisible(true);
                setVisible(false);
                dispose();
            }
        };
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "ESCAPE");
        this.getRootPane().getActionMap().put("ESCAPE", escapeAction);
    }

    VentanaDeAsistenciaDeProfesor ventanaDeProfesor;

    public void checarAsistencia(DPFPVerification verification, DPFPFeatureSet huellaCapturada) {
        Boolean teacherFound = false;
        profesorList = profesorQuery.getResultList();
        for (Iterator it = profesorList.iterator(); it.hasNext();) {
            Profesor profesor = (Profesor) it.next();
            DPFPVerificationResult resultado;
            resultado = verification.verify(huellaCapturada, DPFPGlobal.getTemplateFactory().createTemplate(profesor.getHuella()));

            if (resultado.isVerified()) {
                jTextArea1.append("El profesor: " + profesor.getNombres() + " puso su dedo en el sensor\n");
                ventanaDeProfesor.llenaVentanaDeDatos(profesor);
                jScrollPane1.setViewportView(ventanaDeProfesor.getRootPane());
                setTablaDeRegistrosBackAfterSomeTime();
                teacherFound = true;
                break;
            }
        }
        if (!teacherFound) {
            //jScrollPane1.setViewportView(jTableRegistro);
            //jTextArea1.append("Huella no reconocida trate de nuevo\n");
            jScrollPane1.setViewportView(jLabelHuellaNoReconocida);
            setTablaDeRegistrosBackAfterSomeTime();
            //generaTablaRegistros();
        }
    }
    Boolean contando = false;

    public void setTablaDeRegistrosBackAfterSomeTime() {
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        Runnable setBack = new Runnable() {
            @Override
            public void run() {
                if (jScrollPane1.getViewport().getComponent(0) != jTableRegistro) {
                    jScrollPane1.setViewportView(jTableRegistro);
                    generaTablaRegistros();
                    contando = false;
                }
            }
        };
        if (!contando) {
            service.schedule(setBack, 5, TimeUnit.SECONDS);
            contando = true;
        }
    }

    ListaYCapturaDeAsistencias(StartScreen aThis) {
        this();
        this.parent = aThis;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jLabelReloj = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableRegistro = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Consolas", 0, 48)); // NOI18N
        jLabel5.setText("SIA");

        jLabelReloj.setFont(new java.awt.Font("Consolas", 0, 48)); // NOI18N
        jLabelReloj.setText("20:44:11");
        jLabelReloj.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jTableRegistro.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jTableRegistro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTableRegistro.setRowHeight(28);
        jScrollPane1.setViewportView(jTableRegistro);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/minilogo_udg64.png"))); // NOI18N

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 285, Short.MAX_VALUE)
                        .addComponent(jLabelReloj, javax.swing.GroupLayout.PREFERRED_SIZE, 523, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1)
                    .addComponent(jLabel5)
                    .addComponent(jLabelReloj))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        if (parent == null) {
        } else {
            parent.setAlwaysOnTop(true);
            parent.show();
        }
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_formWindowClosing

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ListaYCapturaDeAsistencias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ListaYCapturaDeAsistencias().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabelReloj;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTableRegistro;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables

    private void generaTablaRegistros() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();
        try {
            entityManager = javax.persistence.Persistence.createEntityManagerFactory("asistenciasx?zeroDateTimeBehavior=convertToNullPU").createEntityManager();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Cheque la conexion a la base de datos.");
        }
        registrosQuery = entityManager.createNativeQuery("SELECT * FROM asistenciasx.registrodeasistencias");
        registrosQuery.setHint(QueryHints.RESULT_TYPE, ResultType.Map);
        registrosQuery.setMaxResults(30);
        listaDeRegistros = registrosQuery.getResultList();

        for (Map row : listaDeRegistros) {
            row.replace("fechayhora", row.get("fechayhora").toString().substring(0, 19));
        }

        JTableBinding jTableBinding;
        jTableBinding = SwingBindings.createJTableBinding(AutoBinding.UpdateStrategy.READ, listaDeRegistros, jTableRegistro);
        ColumnBinding columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${nombres} ${apellidos}"));
        columnBinding.setColumnName("Profesor");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${nombre}"));
        columnBinding.setColumnName("Materia");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${edif} ${aula}"));
        columnBinding.setColumnName("Salon");
        columnBinding.setColumnClass(String.class);

        columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${fechayhora}"));
        columnBinding.setColumnName("Fecha y Hora");
        columnBinding.setColumnClass(String.class);

        bindingGroup.addBinding(jTableBinding);
        bindingGroup.bind();

        jTableRegistro.getColumnModel().getColumn(0).setPreferredWidth(150);

        jTableRegistro.getColumnModel().getColumn(2).setPreferredWidth(25);

    }

    private void createjLabelHuellaNoReconocida() {
        jLabelHuellaNoReconocida = new javax.swing.JLabel();

        jLabelHuellaNoReconocida.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        jLabelHuellaNoReconocida.setText("Huella no reconocida, trate de nuevo.");
        jLabelHuellaNoReconocida.setHorizontalAlignment(CENTER);
    }
}