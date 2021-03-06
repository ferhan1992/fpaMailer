package de.bht.fpa.mail.gruppe15.model.appLogic;

import de.bht.fpa.mail.gruppe15.model.data.Folder;

/*
 * This is the interface for classes that manage
 * folders.
 * 
 * @author Simone Strippgen
 */
public interface FolderManagerIF {

    /**
     * Get current root folder.
     *
     * @return current root folder.
     */
    Folder getTopFolder();

    /**
     * Loads all relevant content in the directory path of a folder into the
     * folder.
     *
     * @param f the folder into which the content of the corresponding directory
     * should be loaded
     */
    void loadContent(final Folder f);

    /**
     * Method to set the folder strategy
     *
     * @param strategy the strategy which shall be used by the manager..
     */
    void setFolderStrategy(final FolderStrategyIF strategy);

    /**
     * Set the root folder.
     *
     * @param folder the folder which shall be set as the new top folder.
     */
    void setTopFolder(final Folder folder);

}
