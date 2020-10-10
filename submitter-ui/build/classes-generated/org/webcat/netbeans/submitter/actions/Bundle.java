package org.webcat.netbeans.submitter.actions;
/** Localizable strings for {@link org.webcat.netbeans.submitter.actions}. */
class Bundle {
    /**
     * @return <i>Submit Project...</i>
     * @see SubmitProjectAction
     */
    static String CTL_SubmitProjectAction() {
        //return org.openide.util.NbBundle.getMessage(Bundle.class, "CTL_SubmitProjectAction");
        return IdeaLoggingEvent.getMessage(Bundle.class, "CTL_SubmitProjectAction");
    }
    private Bundle() {}
}
