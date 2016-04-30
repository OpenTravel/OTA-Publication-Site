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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Model object representing a code list release that is available for download from the
 * OpenTravel web site.
 *
 * @author S. Livezey
 */
@NamedQueries({
	@NamedQuery(
			name  = "codeListFindByReleaseDate",
			query = "SELECT c FROM CodeList c WHERE c.releaseDate = :releaseDate" ),
	@NamedQuery(
			name  = "codeListLatestByReleaseDate",
			query = "SELECT c FROM CodeList c WHERE c.releaseDate = "
					+ "(SELECT MAX(c2.releaseDate) FROM CodeList c2)" ),
	@NamedQuery(
			name  = "codeListFindAll",
			query = "SELECT c FROM CodeList c ORDER BY c.releaseDate DESC" ),
})
@NamedNativeQueries({
	@NamedNativeQuery(
		name  = "codeListDeleteDownloads",
		query = "DELETE FROM code_list_download WHERE code_list_id = :codeListId" ),
})
@Entity
@Table( name = "code_list" )
@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="daoCache" )
public class CodeList implements Serializable {
	
	public static final DateFormat labelFormat = new SimpleDateFormat( "yyyy-MM-dd" );
	
	private static final long serialVersionUID = 450398850690016382L;
	
	@Id
	@Column( name = "id", nullable = false )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id = -1L;
	
	@NotNull
	@Column( name = "release_date", nullable = false )
	private Date releaseDate;
	
	@Column( name = "archive_filename", nullable = false, length = 50 )
	private String archiveFilename;
	
	@OneToOne( fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE } )
	@JoinColumn( name = "archive_content_id" )
	private FileContent archiveContent;
	
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
	 * Returns the value of the 'releaseDate' field.
	 *
	 * @return Date
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * Assigns the value of the 'releaseDate' field.
	 *
	 * @param releaseDate  the field value to assign
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	/**
	 * Returns a formatted display label for the release date field.
	 * 
	 * @return String
	 */
	public String getReleaseDateLabel() {
		return (releaseDate == null) ? "Unknown" : labelFormat.format( releaseDate );
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
