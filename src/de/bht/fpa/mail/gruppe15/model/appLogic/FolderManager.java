package de.bht.fpa.mail.gruppe15.model.appLogic;

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
    Folder topFolder;

    /**
     * Constructs a new FileManager object which manages a folder hierarchy,
     * where file contains the path to the top directory. The contents of the
     * directory file are loaded into the top folder
     *
     * @param file File which points to the top directory
     */
    public FolderManager(File file) {
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
    public void loadContent(Folder f) {
        f.getComponents().clear();
        File file = new File(f.getPath());
        for (File fi : file.listFiles()) {
            if (fi.isDirectory()) {
                if (fi.list().length == 0) {
                    Folder y = new Folder(fi.getAbsoluteFile(), false);
                    f.addComponent(y);
                } else {
                    Folder y = new Folder(fi.getAbsoluteFile(), true);
                    f.addComponent(y);
                }
            }
        }
    }

    @Override
    public Folder getTopFolder() {
        return topFolder;
    }
}
