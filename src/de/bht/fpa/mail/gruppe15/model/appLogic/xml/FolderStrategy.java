package de.bht.fpa.mail.gruppe15.model.appLogic.xml;

import de.bht.fpa.mail.gruppe15.model.appLogic.FolderStrategyIF;
import de.bht.fpa.mail.gruppe15.model.data.Component;
import de.bht.fpa.mail.gruppe15.model.data.FileElement;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;

/**
 *
 * @author Ferhan Kaplanseren
 * @author Ömür Düner
 */
public class FolderStrategy implements FolderStrategyIF {

    /**
     * Loads all relevant content in the directory path of a folder object into
     * the folder.
     *
     * @param f the folder into which the content of the corresponding directory
     * should be loaded
     */
    @Override
    public void loadContent(final Folder f) {
        if (f != null) {
            if (f.getComponents().isEmpty()) {
                final File file;
                file = new File(f.getPath());
                for (final File fi : file.listFiles()) {
                    if (fi.isDirectory()) {
                        final Component y;
                        y = new Folder(fi.getAbsoluteFile(), hasSubDir(fi));
                        f.addComponent(y);
                    } else if (fi.isFile()) {
                        final Component y;
                        y = new FileElement(fi.getAbsoluteFile());
                        f.addComponent(y);
                    }
                }
            }
        }
    }

    /**
     * Method to check, whether the file contains subfolder or not. Returns true
     * if when containing subfolders and false if not.
     *
     * @param fi the file which shall be checked for subdirectorys.
     * @return boolean
     */
    private boolean hasSubDir(File fi) {
        if (fi != null) {
            for (File x : fi.listFiles()) {
                if (x.isDirectory()) {
                    return true;
                }
            }
        }
        return false;
    }

}
