package com.donetop.domain.interfaces;

import com.donetop.domain.entity.folder.Folder;
import com.donetop.enums.folder.FolderType;

public interface MultipleFolderContainer<T extends Folder> extends FolderContainer<T> {

	T getFolderOrNew(String root, FolderType folderType);

}
