package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.appLogic.xml.FolderStrategy;
import de.bht.fpa.mail.gruppe15.model.data.Component;
import de.bht.fpa.mail.gruppe15.model.data.FileElement;
import de.bht.fpa.mail.gruppe15.model.data.Folder;
import java.io.File;

/*
 * This class manages a hierarchy of folders and their content which is loaded 
 * from a given directory path.
 * 
 * @author Simone Strippgen
 */
public class FolderManager implements FolderManagerIF {

    //top Folder of the managed hierarchy
    private final Folder topFolder;

    /**
     * Constructs a new FileManager object which manages a folder hierarchy,
     * where file contains the path to the top directory. The contents of the
     * directory file are loaded into the top folder
     *
     * @param file File which points to the top directory
     */
    public FolderManager(final File file) {
        topFolder = new Folder(file, true);
    }

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

    /**
     * Get current root folder.
     *
     * @return current root folder.
     */
    @Override
    public Folder getTopFolder() {
        return topFolder;
    }
    
    @Override
    public void setFolderStrategy(final FolderStrategy strategy) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
