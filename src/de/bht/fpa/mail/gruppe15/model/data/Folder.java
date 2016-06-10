package de.bht.fpa.mail.gruppe15.model.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Simone Strippgen
 *
 */
public class Folder extends Component {

    private final boolean expandable;
    private final ArrayList<Component> content;
    private final ArrayList<Email> emails;
    private boolean contentLoaded;

    public Folder(final File path, final boolean expandable) {
        super(path);
        this.expandable = expandable;
        content = new ArrayList<>();
        emails = new ArrayList<>();
        contentLoaded = false;
    }

    @Override
    public boolean isExpandable() {
        return expandable;
    }

    @Override
    public void addComponent(final Component comp) {
        content.add(comp);
    }

    @Override
    public List<Component> getComponents() {
        return content;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void addEmail(final Email message) {
        emails.add(message);
    }
    
    public void setContentLoaded(){
        contentLoaded = true;
    }

    @Override
    public String toString() {
        if (contentLoaded == true && !this.getEmails().isEmpty()) {
            return this.getName() + " (" + this.emails.size() + ")";
        } else if (contentLoaded == true && this.getEmails().isEmpty()) {
            return this.getName() + " (0)";
        }
        return this.getName();
    }
}
