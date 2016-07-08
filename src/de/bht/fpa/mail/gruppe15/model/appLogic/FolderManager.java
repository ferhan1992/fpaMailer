package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.appLogic.xml.XMLFolderStrategy;
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
    private Folder topFolder;
    private FolderStrategyIF folderStrategy;

    /**
     * Constructs a new FileManager object which manages a folder hierarchy,
     * where file contains the path to the top directory. The contents of the
     * directory file are loaded into the top folder
     *
     * @param file File which points to the top directory
     */
    public FolderManager(final File file) {
        topFolder = new Folder(file, true);
        folderStrategy = new XMLFolderStrategy();
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
            folderStrategy.loadContent(f);
        }
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
    public void setFolderStrategy(final FolderStrategyIF strategy) {
        this.folderStrategy = strategy;
    }
    
    @Override
    public void setTopFolder(final Folder folder) {
        topFolder = folder;
    }
}
