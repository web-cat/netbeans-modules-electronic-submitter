package org.webcat.netbeans.submitter.options;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import org.netbeans.spi.options.OptionsPanelController;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;

//--------------------------------------------------------------------------
/**
 * The controller that loads/persists the preferences for the electronic
 * submission plug-in.
 * 
 * @author Tony Allevato, based on code by Robert Poole, Rusty Todd, and
 *     Stephan McCarn
 */
@OptionsPanelController.TopLevelRegistration(
    categoryName = "#OptionsCategory_Name_Submitter",
    iconBase = "org/webcat/netbeans/submitter/options/submitter-icon.png",
    keywords = "#OptionsCategory_Keywords_Submitter",
    keywordsCategory = "Submitter")
@org.openide.util.NbBundle.Messages(
{
    "OptionsCategory_Name_Submitter=Electronic Submission",
    "OptionsCategory_Keywords_Submitter=submit, submission"
})
public final class SubmitterOptionsPanelController
    extends OptionsPanelController
{
    //~ Instance/static variables .............................................

    private SubmitterPanel panel;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private boolean changed;

    
    //~ Methods ...............................................................

    // ----------------------------------------------------------
    public void update()
    {
        getPanel().load();
        changed = false;
    }


    // ----------------------------------------------------------
    public void applyChanges()
    {
        getPanel().store();
        changed = false;
    }


    // ----------------------------------------------------------
    public void cancel()
    {
        // need not do anything special, if no changes have been persisted yet
    }


    // ----------------------------------------------------------
    public boolean isValid()
    {
        return getPanel().valid();
    }


    // ----------------------------------------------------------
    public boolean isChanged()
    {
        return changed;
    }


    // ----------------------------------------------------------
    public HelpCtx getHelpCtx()
    {
        return null; // new HelpCtx("...ID") if you have a help set
    }


    // ----------------------------------------------------------
    public JComponent getComponent(Lookup masterLookup)
    {
        return getPanel();
    }


    // ----------------------------------------------------------
    public void addPropertyChangeListener(PropertyChangeListener l)
    {
        pcs.addPropertyChangeListener(l);
    }


    // ----------------------------------------------------------
    public void removePropertyChangeListener(PropertyChangeListener l)
    {
        pcs.removePropertyChangeListener(l);
    }


    // ----------------------------------------------------------
    private SubmitterPanel getPanel()
    {
        if (panel == null)
        {
            panel = new SubmitterPanel(this);
        }
        return panel;
    }


    // ----------------------------------------------------------
    void changed()
    {
        if (!changed)
        {
            changed = true;
            pcs.firePropertyChange(OptionsPanelController.PROP_CHANGED, false,
                    true);
        }

        pcs.firePropertyChange(OptionsPanelController.PROP_VALID, null, null);
    }
}
