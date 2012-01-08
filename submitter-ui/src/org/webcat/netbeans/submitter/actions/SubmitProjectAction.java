package org.webcat.netbeans.submitter.actions;

import org.netbeans.api.project.FileOwnerQuery;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileObject;
import org.openide.nodes.Node;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.NodeAction;
import org.webcat.netbeans.submitter.ui.SubmitProjectPanel;

//--------------------------------------------------------------------------
/**
 * An action placed in the toolbar and file menu that allows students to submit
 * the currently selected project electronically.
 * 
 * @author Tony Allevato
 */
@ActionID(
    category = "File",
    id = "org.webcat.netbeans.submitter.actions.SubmitProjectAction")
@ActionRegistration(
    displayName = "#CTL_SubmitProjectAction")
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 1001),
    @ActionReference(path = "Toolbars/File", position = 500)
})
@Messages(
    "CTL_SubmitProjectAction=Submit Project..."
)
public final class SubmitProjectAction extends NodeAction
{
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    @Override
    public String getName()
    {
        return "Submit Project...";
    }


    // ----------------------------------------------------------
    @Override
    protected String iconResource()
    {
        return "/org/webcat/netbeans/submitter/actions/submit.gif";
    }

    
    // ----------------------------------------------------------
    @Override
    public HelpCtx getHelpCtx()
    {
        return null;
    }


    // ----------------------------------------------------------
    @Override
    public boolean enable(Node[] activatedNodes)
    {
        if (activatedNodes.length == 1)
        {
            Node node = activatedNodes[0];
            return getProjectFromNode(node) != null;
        }
        
        return false;
    }


    // ----------------------------------------------------------
    @Override
    public void performAction(Node[] activatedNodes)
    {
        Project project = getProjectFromNode(activatedNodes[0]);
        SubmitProjectPanel.showSubmitDialog(project);
    }


    // ----------------------------------------------------------
    private Project getProjectFromNode(Node node)
    {
        Lookup lookup = node.getLookup();
        Project project = lookup.lookup(Project.class);

        if (project == null)
        {
            FileObject fobj = lookup.lookup(FileObject.class);
            if (fobj != null)
            {
                project = FileOwnerQuery.getOwner(fobj);
            }
        }

        return project;
    }
}
