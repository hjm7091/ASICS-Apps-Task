package com.donetop.domain.interfaces;

import com.donetop.domain.entity.folder.Folder;

public interface SingleFolderContainer<T extends Folder> extends FolderContainer<T> {

	T getFolderOrNew(String root);

}
