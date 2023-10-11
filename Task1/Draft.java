package com.donetop.domain.entity.draft;

import com.donetop.domain.entity.comment.Comment;
import com.donetop.domain.entity.folder.DraftFolder;
import com.donetop.domain.entity.payment.PaymentInfo;
import com.donetop.domain.interfaces.MultipleFolderContainer;
import com.donetop.dto.comment.CommentDTO;
import com.donetop.dto.draft.DraftDTO;
import com.donetop.enums.draft.DraftStatus;
import com.donetop.enums.draft.PaymentMethod;
import com.donetop.enums.folder.FolderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Entity
@Table(
	name = "tbDraft",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"paymentInfoId"})
	}
)
@DynamicUpdate
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Draft implements MultipleFolderContainer<DraftFolder>, Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private long id;

	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String customerName;

	@Builder.Default
	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String companyName = "";

	@Builder.Default
	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String inChargeName = "";

	@Column(nullable = false, columnDefinition = "varchar(128) default ''")
	private String email;

	@Column(nullable = false, columnDefinition = "varchar(15) default ''")
	private String phoneNumber;

	@Column(nullable = false, columnDefinition = "varchar(64) default ''")
	private String categoryName;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(50) default ''")
	private DraftStatus draftStatus = DraftStatus.HOLDING;

	@Column(nullable = false, columnDefinition = "varchar(256) default ''")
	private String address;

	@Column(nullable = false, columnDefinition = "varchar(256) default ''")
	private String detailAddress;

	@Column(nullable = false, columnDefinition = "bigint(20) default 0")
	private long price;

	@Builder.Default
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = "varchar(50) default ''")
	private PaymentMethod paymentMethod = PaymentMethod.CASH;

	@Builder.Default
	@Column(nullable = false, columnDefinition = "varchar(3000) default ''")
	private String memo = "";

	@Column(nullable = false, columnDefinition = "varchar(512) default ''")
	private String password;

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime createTime = LocalDateTime.now();

	@Builder.Default
	@Column(nullable = false)
	private LocalDateTime updateTime = LocalDateTime.now();

	@OneToMany(mappedBy = "draft", cascade = CascadeType.REMOVE)
	private final List<DraftFolder> draftFolders = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JoinColumn(name = "paymentInfoId")
	private PaymentInfo paymentInfo;

	@OneToMany(mappedBy = "draft", cascade = CascadeType.REMOVE)
	private final List<Comment> comments = new ArrayList<>();

	public Draft updateCustomerName(final String customerName) {
		this.customerName = customerName;
		return this;
	}

	public Draft updateCompanyName(final String companyName) {
		this.companyName = companyName;
		return this;
	}

	public Draft updateInChargeName(final String inChargeName) {
		this.inChargeName = inChargeName;
		return this;
	}

	public Draft updateEmail(final String email) {
		this.email = email;
		return this;
	}

	public Draft updatePhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public Draft updateCategoryName(final String categoryName) {
		this.categoryName = categoryName;
		return this;
	}

	public Draft updateDraftStatus(final DraftStatus draftStatus) {
		this.draftStatus = draftStatus;
		return this;
	}

	public Draft updateAddress(final String address) {
		this.address = address;
		return this;
	}

	public Draft updateDetailAddress(final String detailAddress) {
		this.detailAddress = detailAddress;
		return this;
	}

	public Draft updatePrice(final long price) {
		this.price = price;
		return this;
	}

	public Draft updatePaymentMethod(final PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
		return this;
	}

	public Draft updateMemo(final String memo) {
		this.memo = memo;
		return this;
	}

	public Draft updatePassword(final String password) {
		this.password = password;
		return this;
	}

	public Draft setUpdateTime(final LocalDateTime localDateTime) {
		this.updateTime = localDateTime;
		return this;
	}

	@Override
	public void addFolder(final DraftFolder folder) {
		this.draftFolders.add(folder);
	}

	@Override
	public DraftFolder getFolderOrNew(final String root, final FolderType folderType) {
		return !hasFolder(folderType) ? DraftFolder.of(folderType, root, this)
			: this.draftFolders.stream().filter(draftFolder -> draftFolder.getFolderType() == folderType).findFirst().orElseThrow();
	}

	@Override
	public boolean hasFolder() {
		return !this.draftFolders.isEmpty();
	}

	public boolean hasComment() {
		return !this.comments.isEmpty();
	}

	public boolean hasFolder(final FolderType folderType) {
		return this.draftFolders.stream().anyMatch(draftFolder -> draftFolder.getFolderType() == folderType);
	}

	public void addPaymentInfo(final PaymentInfo paymentInfo) {
		this.paymentInfo = paymentInfo;
	}

	public PaymentInfo getOrNewPaymentInfo() {
		return this.paymentInfo == null ? new PaymentInfo() : this.paymentInfo;
	}

	public Draft copy() {
		return new Draft().toBuilder()
			.customerName(this.customerName)
			.companyName(this.companyName)
			.inChargeName(this.inChargeName)
			.email(this.email)
			.phoneNumber(this.phoneNumber)
			.categoryName(this.categoryName)
			.draftStatus(this.draftStatus)
			.address(this.address)
			.detailAddress(this.detailAddress)
			.price(this.price)
			.paymentMethod(this.paymentMethod)
			.memo(this.memo)
			.password(this.password)
			.build();
	}

	public DraftDTO toDTO(final boolean includeSubObjectInfo) {
		final DraftDTO draftDTO = DraftDTO.builder()
			.id(this.id)
			.customerName(this.customerName)
			.companyName(this.companyName)
			.inChargeName(this.inChargeName)
			.email(this.email)
			.phoneNumber(this.phoneNumber)
			.categoryName(this.categoryName)
			.draftStatus(this.draftStatus.toDTO())
			.address(this.address)
			.detailAddress(this.detailAddress)
			.price(this.price)
			.paymentMethod(this.paymentMethod.toDTO())
			.memo(this.memo)
			.createTime(this.createTime)
			.updateTime(this.updateTime).build();
		if (includeSubObjectInfo) {
			draftDTO.setFolders(this.draftFolders.stream().map(DraftFolder::toDTO).collect(toList()));
			draftDTO.setPaymentInfo(this.paymentInfo == null ? null : this.paymentInfo.toDTO());
			draftDTO.setComments(this.comments.stream().map(Comment::toDTO).sorted(comparing(CommentDTO::getCreateTime)).collect(toList()));
		}
		return draftDTO;
	}

	@Override
	public String toString() {
		return "Draft{" +
			"id=" + id +
			", customerName='" + customerName + '\'' +
			", companyName='" + companyName + '\'' +
			", inChargeName='" + inChargeName + '\'' +
			", email='" + email + '\'' +
			", phoneNumber='" + phoneNumber + '\'' +
			", categoryName='" + categoryName + '\'' +
			", draftStatus=" + draftStatus +
			", address='" + address + '\'' +
			", detailAddress='" + detailAddress + '\'' +
			", price=" + price +
			", paymentMethod=" + paymentMethod +
			", memo='" + memo + '\'' +
			", password='" + password + '\'' +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			'}';
	}
}
