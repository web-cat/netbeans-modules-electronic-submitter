package org.webcat.netbeans.submitter.ui;

import java.util.ArrayList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import org.webcat.submitter.SubmissionTargetException;
import org.webcat.submitter.targets.RootTarget;
import org.webcat.submitter.targets.SubmissionTarget;

//--------------------------------------------------------------------------
/**
 * The class implementing the required TreeModel interface to allow the data's
 * representation in a JTree.
 *
 * @author Robert Poole, Rusty Todd, Stephan McCarn
 * @version 2011.04.15
 */
public class SubmissionTargetModel
        implements TreeModel {

    private ArrayList<TreeModelListener> submissionTargetModelListeners =
            new ArrayList<TreeModelListener>();
    private RootTarget rootTarget;

    /**
     * Instantiates a new submission target model.
     *
     * @param rootTarget
     *            the root target
     */
    public SubmissionTargetModel(RootTarget rootTarget) {
        this.rootTarget = rootTarget;
    }

    /**
     * Returns the children of a submission target.
     *
     * @param target
     *            the target
     */
    private SubmissionTarget[] getChildren(SubmissionTarget target) {
        try {
            return target.getChildren();
        } catch (SubmissionTargetException e) {
            // error
        }
        return null;
    }

    /**
     * Returns a text formatted tree structure.
     *
     * @return the string
     */
    @Override
    public String toString() {
        StringBuffer rtn = new StringBuffer();
        toStringHelper(rootTarget, 0, rtn);
        return rtn.toString();
    }

    /**
     * To string helper. Recursively constructs the tree.
     *
     * @param target
     *            the target
     * @param indent
     *            the indent
     * @param writeTo
     *            the write to
     */
    private void toStringHelper(
            SubmissionTarget target,
            int indent,
            StringBuffer writeTo) {
        String indentStr = "";
        for (int i = 0; i < indent; i++) {
            indentStr = indentStr + "\t";
        }

        writeTo.append(indentStr).append(target.getName()).append("\n");
        if (getChildren(target).length > 0) {
            for (SubmissionTarget st : getChildren(target)) {
                toStringHelper(st, indent + 1, writeTo);
            }
        }
    }

    // ---- REQUIRED METHODS --------------------------------------
    /**
     * Gets the root for the model.
     *
     * @return the root
     */
    public Object getRoot() {
        return rootTarget;
    }

    /**
     * Returns the child of parent at index index in the parent's child array.
     *
     * @param parent
     *            the parent
     * @param index
     *            the index of the child
     * @return the child, null if index out of bounds
     */
    public Object getChild(Object parent, int index) {

        SubmissionTarget target = (SubmissionTarget) parent;
        if (index < 0 || getChildren(target).length <= index) {
            return null;
        }
        return getChildren(target)[index];
    }

    /**
     * Returns the number of children of parent.
     *
     * @param parent
     *            the parent
     * @return the child count
     */
    public int getChildCount(Object parent) {
        SubmissionTarget target = (SubmissionTarget) parent;
        return getChildren(target).length;
    }

    /**
     * Returns true if node is a leaf.
     *
     * @param node
     *            the node
     * @return true if a leaf
     */
    public boolean isLeaf(Object node) {
        SubmissionTarget target = (SubmissionTarget) node;
        return getChildren(target).length == 0;
    }

    /**
     * -----------------NOT USED------------------------ Messaged when the user
     * has altered the value for the item identified by path to newValue. Not
     * used by this model.
     *
     * @param path
     *            the path
     * @param newValue
     *            the new value
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        System.out.println("*** valueForPathChanged : " + path + " --> "
                + newValue);
    }

    /**
     * Gets the index of a given child and its parent.
     *
     * @param parent
     *            the parent
     * @param child
     *            the child
     * @return the index, -1 if child or parent is null
     */
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) {
            return -1;
        }
        SubmissionTarget targetParent = (SubmissionTarget) parent;
        SubmissionTarget targetChild = (SubmissionTarget) child;

        int position = -1;

        for (int i = 0; i < getChildren(targetParent).length; i++) {
            if (getChildren(targetParent)[i].getName().equals(
                    targetChild.getName())) {
                position = i;
            }

        }

        return position;

    }

    /**
     * Adds a TreeModelListener
     *
     * @param l
     *            the listener
     */
    public void addTreeModelListener(TreeModelListener l) {
        submissionTargetModelListeners.add(l);
    }

    /**
     * Removes a TreeModelListener
     *
     * @param l
     *            the listener
     */
    public void removeTreeModelListener(TreeModelListener l) {
        submissionTargetModelListeners.remove(l);

    }
}
