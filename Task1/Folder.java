package com.donetop.domain.entity.folder;

import com.donetop.domain.entity.file.File;
import com.donetop.dto.folder.FolderDTO;
import com.donetop.enums.folder.DomainType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.toList;

@Entity
@Table(name = "tbFolder")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor
public class Folder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(512) default ''")
	private String path;

	@Enumerated(value = EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(10) default ''")
	private DomainType domainType;

	@OneToMany(mappedBy = "folder", cascade = CascadeType.REMOVE)
	private final Set<File> files = new HashSet<>();

	@Builder
	public Folder(final String path, final DomainType domainType) {
		this.path = path;
		this.domainType = domainType;
	}

	public static Folder of(final DomainType domainType, final String root, final long id) {
		return Folder.builder()
			.domainType(domainType)
			.path(domainType.buildPathFrom(root, id))
			.build();
	}

	public void add(final File... files) {
		this.files.addAll(List.of(files));
	}

	public void deleteAllFiles() {
		this.files.clear();
	}

	public boolean isNew() {
		return this.id == 0L;
	}

	public FolderDTO toDTO() {
		final FolderDTO folderDTO = new FolderDTO();
		folderDTO.setId(this.id);
		folderDTO.setPath(this.path);
		folderDTO.setFiles(this.files.stream().sorted(comparingLong(File::getId)).map(File::toDTO).collect(toList()));
		return folderDTO;
	}

}
