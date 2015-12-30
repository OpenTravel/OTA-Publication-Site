/**
 * Copyright (C) 2015 OpenTravel Alliance (info@opentravel.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opentravel.pubs.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Model object representing a single item that was submitted as a component (typically
 * within a zip archive) of a publication.
 * 
 * @author S. Livezey
 */
@Entity
@Table( name = "PUBLICATION_ITEM" )
public class PublicationItem {
	
	@Id
	@Column( name = "ID", nullable = false )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "GROUP_ID" )
	private PublicationGroup owner;
	
	@NotNull
	@Column( name = "ITEM_TYPE", nullable = false, length = 20 )
	@Enumerated( EnumType.STRING )
	private PublicationItemType type;
	
	@NotNull
	@Size( min = 1, max = 50 )
	@Column( name = "ITEM_FILENAME", nullable = false, length = 50 )
	private String itemFilename;
	
	@OneToOne( fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE } )
	@JoinColumn( name = "ITEM_CONTENT_ID" )
	private FileContent itemContent;
	
	@NotNull
	@Column( name = "FILE_SIZE", nullable = false )
	private long fileSize;
	
	@NotNull
	@Column( name = "CREATE_DATE", nullable = false )
	private Date createDate;
	
	@OneToMany( mappedBy = "publicationItem", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE } )
	@OrderBy( "commentNumber ASC" )
	private List<Comment> submittedComments;
	
	@ManyToMany( mappedBy = "downloadedPublicationItems" )
	@OrderBy( "registrationDate ASC" )
	private List<Registrant> downloadedBy;

	/**
	 * Returns the value of the 'id' field.
	 *
	 * @return long
	 */
	public long getId() {
		return id;
	}

	/**
	 * Assigns the value of the 'id' field.
	 *
	 * @param id  the field value to assign
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Returns the value of the 'owner' field.
	 *
	 * @return PublicationGroup
	 */
	public PublicationGroup getOwner() {
		return owner;
	}

	/**
	 * Assigns the value of the 'owner' field.
	 *
	 * @param owner  the field value to assign
	 */
	public void setOwner(PublicationGroup owner) {
		this.owner = owner;
	}

	/**
	 * Returns the value of the 'type' field.
	 *
	 * @return PublicationItemType
	 */
	public PublicationItemType getType() {
		return type;
	}

	/**
	 * Assigns the value of the 'type' field.
	 *
	 * @param type  the field value to assign
	 */
	public void setType(PublicationItemType type) {
		this.type = type;
	}

	/**
	 * Returns the value of the 'itemFilename' field.
	 *
	 * @return String
	 */
	public String getItemFilename() {
		return itemFilename;
	}

	/**
	 * Assigns the value of the 'itemFilename' field.
	 *
	 * @param itemFilename  the field value to assign
	 */
	public void setItemFilename(String itemFilename) {
		this.itemFilename = itemFilename;
	}

	/**
	 * Returns the value of the 'itemContent' field.
	 *
	 * @return FileContent
	 */
	public FileContent getItemContent() {
		return itemContent;
	}

	/**
	 * Assigns the value of the 'itemContent' field.
	 *
	 * @param itemContent  the field value to assign
	 */
	public void setItemContent(FileContent itemContent) {
		this.itemContent = itemContent;
	}

	/**
	 * Returns the value of the 'fileSize' field.
	 *
	 * @return long
	 */
	public long getFileSize() {
		return fileSize;
	}

	/**
	 * Assigns the value of the 'fileSize' field.
	 *
	 * @param fileSize  the field value to assign
	 */
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * Returns the value of the 'createDate' field.
	 *
	 * @return Date
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Assigns the value of the 'createDate' field.
	 *
	 * @param createDate  the field value to assign
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Returns the value of the 'submittedComments' field.
	 *
	 * @return List<Comment>
	 */
	public List<Comment> getSubmittedComments() {
		return submittedComments;
	}

	/**
	 * Assigns the value of the 'submittedComments' field.
	 *
	 * @param submittedComments  the field value to assign
	 */
	public void setSubmittedComments(List<Comment> submittedComments) {
		this.submittedComments = submittedComments;
	}

	/**
	 * Returns the value of the 'downloadedBy' field.
	 *
	 * @return List<Registrant>
	 */
	public List<Registrant> getDownloadedBy() {
		return downloadedBy;
	}

	/**
	 * Assigns the value of the 'downloadedBy' field.
	 *
	 * @param downloadedBy  the field value to assign
	 */
	public void setDownloadedBy(List<Registrant> downloadedBy) {
		this.downloadedBy = downloadedBy;
	}
	
}
