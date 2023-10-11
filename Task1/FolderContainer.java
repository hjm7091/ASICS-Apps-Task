package com.donetop.domain.interfaces;

import com.donetop.domain.entity.folder.Folder;

public interface FolderContainer<T extends Folder> {

	void addFolder(T folder);

	boolean hasFolder();

}
