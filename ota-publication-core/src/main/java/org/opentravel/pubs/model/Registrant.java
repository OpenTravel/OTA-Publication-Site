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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.opentravel.pubs.validation.EmailAddress;

/**
 * Model object representing a user who registered on the OpenTravel web site for the
 * purpose of submitting comments or downloading publications.
 * 
 * @author S. Livezey
 */
@NamedQueries({
	@NamedQuery(
		name  = "registrantFindAll",
		query = "SELECT r FROM Registrant r ORDER BY r.registrationDate ASC" ),
	@NamedQuery(
		name  = "registrantFindByDateRange",
		query = "SELECT r FROM Registrant r WHERE r.registrationDate >= :rDate ORDER BY r.registrationDate ASC" ),
	@NamedQuery(
		name  = "registrantFindAllPublicationDownloaders",
		query = "SELECT r FROM Registrant r JOIN r.downloadedPublications p "
    		+ "WHERE p.id = :publicationId ORDER BY r.registrationDate ASC" ),
	@NamedQuery(
		name  = "registrantFindAllPublicationDownloadersByDateRange",
		query = "SELECT r FROM Registrant r JOIN r.downloadedPublications p "
    		+ "WHERE p.id = :publicationId AND r.registrationDate >= :rDate "
    		+ "ORDER BY r.registrationDate ASC" ),
	@NamedQuery(
		name  = "registrantFindAllCodeListDownloaders",
		query = "SELECT r FROM Registrant r JOIN r.downloadedCodeLists c "
    		+ "WHERE c.id = :codeListId ORDER BY r.registrationDate ASC" ),
	@NamedQuery(
		name  = "registrantFindAllCodeListDownloadersByDateRange",
		query = "SELECT r FROM Registrant r JOIN r.downloadedCodeLists c "
    		+ "WHERE c.id = :codeListId AND r.registrationDate >= :rDate "
    		+ "ORDER BY r.registrationDate ASC" ),
})
@NamedNativeQueries({
	@NamedNativeQuery(
		name  = "registrantDeletePublicationDownloads",
		query = "DELETE FROM publication_download WHERE registrant_id = :registrantId" ),
	@NamedNativeQuery(
		name  = "registrantDeletePublicationItemDownloads",
		query = "DELETE FROM publication_item_download WHERE registrant_id = :registrantId" ),
	@NamedNativeQuery(
		name  = "registrantDeleteCodeListDownloads",
		query = "DELETE FROM code_list_download WHERE registrant_id = :registrantId" ),
})
@Entity
@Table( name = "registrant" )
@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="daoCache" )
public class Registrant implements Serializable {
	
	private static final long serialVersionUID = 5482356020754989732L;

	@Id
	@Column( name = "id", nullable = false )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id = -1L;
	
	@NotNull
	@Size( min = 1, max = 50 )
	@Column( name = "last_name", nullable = false, length = 50 )
	private String lastName;
	
	@NotNull
	@Size( min = 1, max = 50 )
	@Column( name = "first_name", nullable = false, length = 50 )
	private String firstName;
	
	@NotNull
	@Size( min = 1, max = 100 )
	@Column( name = "company", nullable = true, length = 100 )
	private String company;
	
	@NotNull
	@Size( min = 1, max = 100 )
	@Column( name = "title", nullable = true, length = 100 )
	private String title;
	
	@NotNull
	@EmailAddress
	@Size( min = 1, max = 50 )
	@Column( name = "email", nullable = false, length = 50 )
	private String email;
	
	@NotNull
	@Size( min = 1, max = 50 )
	@Column( name = "phone", nullable = true, length = 50 )
	private String phone;
	
	@NotNull
	@Column( name = "ota_member" )
	private Boolean otaMember;
	
	@Column( name = "registration_date", nullable = false )
	private Date registrationDate;
	
	@ManyToMany
	@JoinTable( name = "publication_download",
		joinColumns = { @JoinColumn( name = "registrant_id", referencedColumnName = "id" ) },
		inverseJoinColumns = { @JoinColumn( name = "publication_id", referencedColumnName = "id") } )
	@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="collectionCache" )
	private List<Publication> downloadedPublications;
	
	@ManyToMany
	@JoinTable( name = "publication_item_download",
		joinColumns = { @JoinColumn( name = "registrant_id", referencedColumnName = "id" ) },
		inverseJoinColumns = { @JoinColumn( name = "publication_item_id", referencedColumnName = "id") } )
	@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="collectionCache" )
	private List<PublicationItem> downloadedPublicationItems;
	
	@ManyToMany
	@JoinTable( name = "code_list_download",
		joinColumns = { @JoinColumn( name = "registrant_id", referencedColumnName = "id" ) },
		inverseJoinColumns = { @JoinColumn( name = "code_list_id", referencedColumnName = "id") } )
	@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="collectionCache" )
	private List<CodeList> downloadedCodeLists;
	
	@OneToMany( mappedBy = "submittedBy", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE } )
	@OrderBy( "commentNumber ASC" )
	@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="collectionCache" )
	private List<Comment> submittedComments;

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
	 * Returns the value of the 'lastName' field.
	 *
	 * @return String
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Assigns the value of the 'lastName' field.
	 *
	 * @param lastName  the field value to assign
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Returns the value of the 'firstName' field.
	 *
	 * @return String
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Assigns the value of the 'firstName' field.
	 *
	 * @param firstName  the field value to assign
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Returns the value of the 'company' field.
	 *
	 * @return String
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * Assigns the value of the 'company' field.
	 *
	 * @param company  the field value to assign
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * Returns the value of the 'title' field.
	 *
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Assigns the value of the 'title' field.
	 *
	 * @param title  the field value to assign
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the value of the 'email' field.
	 *
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Assigns the value of the 'email' field.
	 *
	 * @param email  the field value to assign
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Returns the value of the 'phone' field.
	 *
	 * @return String
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Assigns the value of the 'phone' field.
	 *
	 * @param phone  the field value to assign
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Returns the value of the 'otaMember' field.
	 *
	 * @return boolean
	 */
	public Boolean getOtaMember() {
		return otaMember;
	}

	/**
	 * Assigns the value of the 'otaMember' field.
	 *
	 * @param otaMember  the field value to assign
	 */
	public void setOtaMember(Boolean otaMember) {
		this.otaMember = otaMember;
	}

	/**
	 * Returns the value of the 'registrationDate' field.
	 *
	 * @return Date
	 */
	public Date getRegistrationDate() {
		return registrationDate;
	}

	/**
	 * Assigns the value of the 'registrationDate' field.
	 *
	 * @param registrationDate  the field value to assign
	 */
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	/**
	 * Returns the value of the 'downloadedPublications' field.
	 *
	 * @return List<Publication>
	 */
	public List<Publication> getDownloadedPublications() {
		return downloadedPublications;
	}

	/**
	 * Assigns the value of the 'downloadedPublications' field.
	 *
	 * @param downloadedPublications  the field value to assign
	 */
	public void setDownloadedPublications(List<Publication> downloadedPublications) {
		this.downloadedPublications = downloadedPublications;
	}

	/**
	 * Returns the value of the 'downloadedPublicationItems' field.
	 *
	 * @return List<PublicationItem>
	 */
	public List<PublicationItem> getDownloadedPublicationItems() {
		return downloadedPublicationItems;
	}

	/**
	 * Assigns the value of the 'downloadedPublicationItems' field.
	 *
	 * @param downloadedPublicationItems  the field value to assign
	 */
	public void setDownloadedPublicationItems(List<PublicationItem> downloadedPublicationItems) {
		this.downloadedPublicationItems = downloadedPublicationItems;
	}

	/**
	 * Returns the value of the 'downloadedCodeLists' field.
	 *
	 * @return List<CodeList>
	 */
	public List<CodeList> getDownloadedCodeLists() {
		return downloadedCodeLists;
	}

	/**
	 * Assigns the value of the 'downloadedCodeLists' field.
	 *
	 * @param downloadedCodeLists  the field value to assign
	 */
	public void setDownloadedCodeLists(List<CodeList> downloadedCodeLists) {
		this.downloadedCodeLists = downloadedCodeLists;
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

}
