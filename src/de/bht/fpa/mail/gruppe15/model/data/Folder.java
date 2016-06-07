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

    private boolean expandable;
    private ArrayList<Component> content;
    private ArrayList<Email> emails;

    public Folder(File path, boolean expandable) {
        super(path);
        this.expandable = expandable;
        content = new ArrayList<Component>();
        emails = new ArrayList<Email>();
    }

    public boolean isExpandable() {
        return expandable;
    }

    @Override
    public void addComponent(Component comp) {
        content.add(comp);
    }

    @Override
    public List<Component> getComponents() {
        return content;
    }

    public List<Email> getEmails() {
        return emails;
    }

    public void addEmail(Email message) {
        emails.add(message);
    }

    @Override
    public String toString() {
        if (!this.emails.isEmpty()) {
            return this.getName() + " (" + this.emails.size() + ")";
        }
        return this.getName();
    }
}
