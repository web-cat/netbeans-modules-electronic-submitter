package org.webcat.netbeans.submitter.ui;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import org.netbeans.api.project.Project;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.filesystems.FileUtil;
import org.webcat.netbeans.submitter.model.SubmitterModel;
import org.webcat.netbeans.submitter.util.Utils;
import org.webcat.submitter.targets.AssignmentTarget;
import org.webcat.submitter.targets.SubmissionTarget;

//--------------------------------------------------------------------------
/**
 * The submission dialog that allows the user to select the assignment to
 * submit and send it to its destination electronically.
 * 
 * @author Tony Allevato, based on the original wizard by Robert Poole,
 *     Rusty Todd, and Stephan McCarn
 */
public class SubmitProjectPanel extends javax.swing.JPanel
{
    //~ Instance/static variables .............................................

    private DialogDescriptor dialogDescriptor;
    private SubmitterModel model;
    private SubmissionTargetModel treeModel;
    private ProgressMonitor progressMonitor;
    private static Dialog dialog = null;

    private static final String NO_PROJECT_SELECTED = "(No project selected)";

    
    //~ Constructors ..........................................................

    // ----------------------------------------------------------
    /**
     * Initializes a new SubmitProjectPanel.
     * 
     * @param model the submitter model
     * @param initialProject the project that will be initially selected in the
     *     dialog
     */
    private SubmitProjectPanel(SubmitterModel model, Project initialProject)
    {
        initComponents();
        
        this.model = model;
        targetTree.setModel(null);

        usernameField.setText(model.getUsername());

        File projDir = FileUtil.toFile(initialProject.getProjectDirectory());
        projectNameField.setText(projDir.getName());

        targetTree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        targetTree.setCellRenderer(new SubmissionTargetRenderer());
    }


    // ----------------------------------------------------------
    private void start()
    {
        runTask("Finding assignments that can be submitted...",
                new FindSubmissionTargets());
    }

    
    // ----------------------------------------------------------
    private void runTask(String title, final SwingWorker<?, ?> task)
    {
        progressMonitor = new ProgressMonitor(this, title, "", 0, 100);
        progressMonitor.setProgress(0);
        progressMonitor.setMillisToDecideToPopup(1);
        
        task.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce)
            {
                if ("progress".equals(pce.getPropertyName()))
                {
                    int progress = (Integer) pce.getNewValue();
                    progressMonitor.setProgress(progress);
                }
            }            
        });

        task.execute();
    }

    
    // ----------------------------------------------------------
    public static void showSubmitDialog(Project initialProject)
    {
        final SubmitProjectPanel panel = new SubmitProjectPanel(
                SubmitterModel.getInstance(), initialProject);

        DialogDescriptor dd = new DialogDescriptor(panel, "Submit Project",
                true, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if (ae.getSource() == NotifyDescriptor.OK_OPTION)
                {
                    if (panel.validateInputs())
                    {
                        panel.doSubmission();
                    }
                }
            }
        });
        dd.setClosingOptions(new Object[] { NotifyDescriptor.CANCEL_OPTION });
        panel.dialogDescriptor = dd;

        dialog = DialogDisplayer.getDefault().createDialog(dd);
        panel.start();
        dialog.setVisible(true);
    }

    
    // ----------------------------------------------------------
    private void doSubmission()
    {
        runTask("Submitting project...", new SubmitProjectTask());
    }

    
    // ----------------------------------------------------------
    private boolean validateInputs()
    {
        String invalidMessage = null;

        if (NO_PROJECT_SELECTED.equals(projectNameField.getText()))
        {
            invalidMessage = "Please select a project to submit.";
            chooseProjectButton.requestFocusInWindow();
        }
        else if (usernameField.getText().length() == 0)
        {
            invalidMessage = "Please enter your username.";
            usernameField.requestFocusInWindow();
        }
        else if (!(targetTree.getLastSelectedPathComponent()
                instanceof AssignmentTarget))
        {
            if (targetTree.getLastSelectedPathComponent() == null)
            {
                invalidMessage = "Please select an assignment.";
            }
            else
            {
                invalidMessage = "Please select an assignment, not a folder.";
            }
        }
        
        if (invalidMessage != null)
        {
            JOptionPane.showMessageDialog(this, invalidMessage);
            return false;
        }
        else
        {
            return true;
        }

    }

    
    // ----------------------------------------------------------
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        instructions = new javax.swing.JLabel();
        projectLabel2 = new javax.swing.JLabel();
        projectNameField = new javax.swing.JTextField();
        chooseProjectButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        submitAs = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        targetTree = new javax.swing.JTree();
        username1 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        password1 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        jSeparator2 = new javax.swing.JSeparator();

        instructions.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        instructions.setText(org.openide.util.NbBundle.getMessage(SubmitProjectPanel.class, "SubmitProjectPanel.instructions.text")); // NOI18N

        projectLabel2.setText(org.openide.util.NbBundle.getMessage(SubmitProjectPanel.class, "SubmitProjectPanel.project.text")); // NOI18N

        projectNameField.setEditable(false);
        projectNameField.setText(org.openide.util.NbBundle.getMessage(SubmitProjectPanel.class, "SubmitProjectPanel.projectNameField.text")); // NOI18N

        chooseProjectButton.setText(org.openide.util.NbBundle.getMessage(SubmitProjectPanel.class, "SubmitProjectPanel.chooseProject.text")); // NOI18N
        chooseProjectButton.setVerifyInputWhenFocusTarget(false);
        chooseProjectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseProjectButtonActionPerformed(evt);
            }
        });

        submitAs.setText(org.openide.util.NbBundle.getMessage(SubmitProjectPanel.class, "SubmitProjectPanel.submitAs.text")); // NOI18N

        targetTree.setRootVisible(false);
        jScrollPane2.setViewportView(targetTree);

        username1.setText(org.openide.util.NbBundle.getMessage(SubmitProjectPanel.class, "SubmitProjectPanel.username.text")); // NOI18N

        password1.setText(org.openide.util.NbBundle.getMessage(SubmitProjectPanel.class, "SubmitProjectPanel.password.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jSeparator1)
                    .add(instructions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(projectLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(projectNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 319, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chooseProjectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(password1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(username1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(usernameField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 203, Short.MAX_VALUE)
                            .add(passwordField))
                        .add(0, 0, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(submitAs)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jScrollPane2))
                    .add(jSeparator2))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(13, 13, 13)
                .add(instructions, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(projectLabel2)
                    .add(projectNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(chooseProjectButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(submitAs)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 203, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(usernameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(username1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(password1)
                    .add(passwordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jSeparator2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // ----------------------------------------------------------
    private void chooseProjectButtonActionPerformed(//GEN-FIRST:event_chooseProjectButtonActionPerformed
            java.awt.event.ActionEvent evt)
    {//GEN-HEADEREND:event_chooseProjectButtonActionPerformed
        String[] projectsList = model.getProjectsList();

        if (projectsList.length < 1)
        {
            JOptionPane.showMessageDialog(this,
                    "You have no projects to select.");
            return;
        }
         
        Icon newIcon = new ImageIcon(getClass().getResource(
                "submit_folder.gif"));
        String s = (String) JOptionPane.showInputDialog(this,
                "Select a project to submit:", "Project Select",
                JOptionPane.PLAIN_MESSAGE, newIcon,
                projectsList, projectsList[0]);
         
        if ((s != null) && (s.length() > 0))
        {
            projectNameField.setText(s);
            return;
        }

        projectNameField.setText("No Project Selected");
    }//GEN-LAST:event_chooseProjectButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseProjectButton;
    private javax.swing.JLabel instructions;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel password1;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel projectLabel2;
    private javax.swing.JTextField projectNameField;
    private javax.swing.JLabel submitAs;
    private javax.swing.JTree targetTree;
    private javax.swing.JLabel username1;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables


    //~ Inner classes .........................................................
    
    // ----------------------------------------------------------
    private class SubmissionTargetRenderer extends DefaultTreeCellRenderer
    {
        // ----------------------------------------------------------
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean selected,
                                                      boolean expanded,
                                                      boolean leaf, int row,
                                                      boolean focused)
        {
            if (value instanceof SubmissionTarget)
            {
                SubmissionTarget target = (SubmissionTarget) value;
                value = target.getName();
            }

            return super.getTreeCellRendererComponent(tree, value,
                    selected, expanded, leaf, row, focused);
        }
    }

    
    // ----------------------------------------------------------
    private class FindSubmissionTargets extends SwingWorker<Void, Void>
    {
        private IOException exception;

        
        // ----------------------------------------------------------
        public FindSubmissionTargets()
        {
            dialogDescriptor.setValid(false);
        }

        
        // ----------------------------------------------------------
        @Override
        public Void doInBackground()
        {
            setProgress(1);

            try
            {
                treeModel = model.getTreeModel();
            }
            catch (IOException e)
            {
                exception = e;
            }

            progressMonitor.setProgress(100);
            return null;
        }

        
        // ----------------------------------------------------------
        @Override
        public void done()
        {
            if (exception != null)
            {
                JOptionPane.showMessageDialog(
                        SubmitProjectPanel.this, "Could not access the "
                        + "submission URL because of the following error:\n\n"
                        + exception.toString());
            }

            if (treeModel != null)
            {
                dialogDescriptor.setValid(true);
            }

            targetTree.setModel(treeModel);
        }
    }
    
    
    // ----------------------------------------------------------
    private class SubmitProjectTask extends SwingWorker<Void, Void>
    {
        private IOException exception;

        
        // ----------------------------------------------------------
        @Override
        public Void doInBackground()
        {
            setProgress(1);

            model.setUsername(usernameField.getText());
            model.setPassword(new String(passwordField.getPassword()));
            model.setProjectToSubmit(projectNameField.getText());
            model.setSubmitTarget(
                    (SubmissionTarget) targetTree.getLastSelectedPathComponent());
            //model.setLastExpandedTreePaths(targetTree.getCurrentExpandedPathNames());
            
            try
            {
                model.submitProject();
            }
            catch (IOException e)
            {
                exception = e;
            }

            setProgress(100);

            return null;
        }
        
        
        // ----------------------------------------------------------
        @Override
        public void done()
        {
            dialog.setVisible(false);

            String response = model.getServerResponse();
            if (response != null)
            {
                try
                {
                    URI uri = Utils.writeSubmissionResults(response);
                    java.awt.Desktop.getDesktop().browse(uri);
                }
                catch (IOException e)
                {
                    // Do nothing.
                }
            }
        }
    }
}
