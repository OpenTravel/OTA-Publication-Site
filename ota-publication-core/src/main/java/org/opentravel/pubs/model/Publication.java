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

import java.io.Serializable;
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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Model object representing a publication or specification that is available for download
 * from the OpenTravel web site.
 * 
 * @author S. Livezey
 */
@NamedQueries({
	@NamedQuery(
		name  = "publicationFindByNameType",
		query = "SELECT p FROM Publication p WHERE p.type = :pType AND p.name = :pName" ),
	@NamedQuery(
		name  = "publicationLatestByType",
		query = "SELECT p FROM Publication p WHERE p.type = :pType AND p.name = "
				+ "(SELECT MAX(p2.name) FROM Publication p2 WHERE p2.type = :pType)" ),
	@NamedQuery(
		name  = "publicationLatestByTypeStateFilter",
		query = "SELECT p FROM Publication p WHERE p.type = :pType AND p.name = "
				+ "(SELECT MAX(p2.name) FROM Publication p2 WHERE (p2.type = :pType) and (p2.state in (:pStates)))" ),
	@NamedQuery(
		name  = "publicationFindAll",
		query = "SELECT p FROM Publication p WHERE p.type = :pType ORDER BY p.name DESC" ),
})
@NamedNativeQueries({
	@NamedNativeQuery(
		name  = "publicationDeleteDownloads",
		query = "DELETE FROM publication_download WHERE publication_id = :publicationId" ),
})
@Entity
@Table( name = "publication" )
@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="daoCache" )
public class Publication implements Serializable {
	
	private static final long serialVersionUID = -3897797233461922352L;

	@Id
	@Column( name = "id", nullable = false )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id = -1L;
	
	@NotNull
	@Size( min = 1, max = 50 )
	@Column( name = "name", nullable = false, length = 50 )
	private String name;
	
	@NotNull
	@Column( name = "pub_type", nullable = false, length = 20 )
	@Enumerated( EnumType.STRING )
	private PublicationType type;
	
	@NotNull
	@Column( name = "pub_state", nullable = false, length = 20 )
	@Enumerated( EnumType.STRING )
	private PublicationState state;
	
	@NotNull
	@Column( name = "pub_date", nullable = false )
	private Date publicationDate;
	
	@Column( name = "archive_filename", nullable = false, length = 50 )
	private String archiveFilename;
	
	@OneToOne( fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE } )
	@JoinColumn( name = "archive_content_id" )
	private FileContent archiveContent;
	
	@Column( name = "release_notes_filename", nullable = false, length = 50 )
	private String releaseNotesFilename;
	
	@OneToOne( fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE } )
	@JoinColumn( name = "release_notes_content_id" )
	private FileContent releaseNotesContent;
	
	@OneToMany( mappedBy = "owner", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE } )
	@OrderBy( "sortOrder ASC" )
	@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="collectionCache" )
	private List<PublicationGroup> publicationGroups;
	
	@ManyToMany( mappedBy = "downloadedPublications", fetch = FetchType.LAZY  )
	@OrderBy( "registrationDate ASC" )
	@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="collectionCache" )
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
	 * Returns the value of the 'name' field.
	 *
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Assigns the value of the 'name' field.
	 *
	 * @param name  the field value to assign
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value of the 'type' field.
	 *
	 * @return PublicationType
	 */
	public PublicationType getType() {
		return type;
	}

	/**
	 * Assigns the value of the 'type' field.
	 *
	 * @param type  the field value to assign
	 */
	public void setType(PublicationType type) {
		this.type = type;
	}

	/**
	 * Returns the value of the 'state' field.
	 *
	 * @return PublicationState
	 */
	public PublicationState getState() {
		return state;
	}

	/**
	 * Assigns the value of the 'state' field.
	 *
	 * @param state  the field value to assign
	 */
	public void setState(PublicationState state) {
		this.state = state;
	}

	/**
	 * Returns the value of the 'publicationDate' field.
	 *
	 * @return Date
	 */
	public Date getPublicationDate() {
		return publicationDate;
	}

	/**
	 * Assigns the value of the 'publicationDate' field.
	 *
	 * @param publicationDate  the field value to assign
	 */
	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	/**
	 * Returns the value of the 'archiveFilename' field.
	 *
	 * @return String
	 */
	public String getArchiveFilename() {
		return archiveFilename;
	}

	/**
	 * Assigns the value of the 'archiveFilename' field.
	 *
	 * @param archiveFilename  the field value to assign
	 */
	public void setArchiveFilename(String archiveFilename) {
		this.archiveFilename = archiveFilename;
	}

	/**
	 * Returns the value of the 'archiveContent' field.
	 *
	 * @return FileContent
	 */
	public FileContent getArchiveContent() {
		return archiveContent;
	}

	/**
	 * Assigns the value of the 'archiveContent' field.
	 *
	 * @param archiveContent  the field value to assign
	 */
	public void setArchiveContent(FileContent archiveContent) {
		this.archiveContent = archiveContent;
	}

	/**
	 * Returns the value of the 'releaseNotesFilename' field.
	 *
	 * @return String
	 */
	public String getReleaseNotesFilename() {
		return releaseNotesFilename;
	}

	/**
	 * Assigns the value of the 'releaseNotesFilename' field.
	 *
	 * @param releaseNotesFilename  the field value to assign
	 */
	public void setReleaseNotesFilename(String releaseNotesFilename) {
		this.releaseNotesFilename = releaseNotesFilename;
	}

	/**
	 * Returns the value of the 'releaseNotesContent' field.
	 *
	 * @return FileContent
	 */
	public FileContent getReleaseNotesContent() {
		return releaseNotesContent;
	}

	/**
	 * Assigns the value of the 'releaseNotesContent' field.
	 *
	 * @param releaseNotesContent  the field value to assign
	 */
	public void setReleaseNotesContent(FileContent releaseNotesContent) {
		this.releaseNotesContent = releaseNotesContent;
	}

	/**
	 * Returns the value of the 'publicationGroups' field.
	 *
	 * @return List<PublicationGroup>
	 */
	public List<PublicationGroup> getPublicationGroups() {
		return publicationGroups;
	}

	/**
	 * Assigns the value of the 'publicationGroups' field.
	 *
	 * @param publicationGroups  the field value to assign
	 */
	public void setPublicationGroups(List<PublicationGroup> publicationGroups) {
		this.publicationGroups = publicationGroups;
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
