package com.donetop.domain.entity.folder;

import com.donetop.domain.entity.draft.Draft;
import com.donetop.dto.folder.DraftFolderDTO;
import com.donetop.dto.folder.FolderDTO;
import com.donetop.enums.folder.FolderType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static com.donetop.enums.folder.DomainType.DRAFT;

@Entity
@Table(
	name = "tbDraftFolder",
	uniqueConstraints = @UniqueConstraint(columnNames = {"folderType", "draftId"})
)
@PrimaryKeyJoinColumn(name = "folderId")
@Getter
@NoArgsConstructor
public class DraftFolder extends Folder {

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(50) default ''")
	private FolderType folderType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "draftId", nullable = false)
	private Draft draft;

	@Builder(builderMethodName = "draftFolderBuilder")
	public DraftFolder(final String path, final FolderType folderType, final Draft draft) {
		super(path, DRAFT);
		this.folderType = folderType;
		this.draft = draft;
		draft.addFolder(this);
	}

	public static DraftFolder of(final FolderType folderType, final String root, final Draft draft) {
		return DraftFolder.draftFolderBuilder()
			.path(folderType.buildPathFrom(DRAFT.buildPathFrom(root, draft.getId()), draft.getId()))
			.folderType(folderType)
			.draft(draft)
			.build();
	}

	public DraftFolderDTO toDTO() {
		final FolderDTO folderDTO = super.toDTO();
		final DraftFolderDTO draftFolderDTO = new DraftFolderDTO();
		draftFolderDTO.setId(folderDTO.getId());
		draftFolderDTO.setPath(folderDTO.getPath());
		draftFolderDTO.setFiles(folderDTO.getFiles());
		draftFolderDTO.setFolderType(this.folderType);
		return draftFolderDTO;
	}

}
