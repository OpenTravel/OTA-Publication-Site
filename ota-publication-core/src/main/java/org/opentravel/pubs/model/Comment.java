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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Abstract base class for all types of comments that can be submitted by site
 * registrants.
 * 
 * @author S. Livezey
 */
@NamedQueries({
	@NamedQuery(
		name  = "commentFindByPublication",
		query = "SELECT c FROM Comment c, Publication p, PublicationGroup g, PublicationItem i "
    		+ "WHERE c.publicationState = :state AND c.publicationItem = i AND i.owner = g AND g.owner = p AND p.id = :publicationId "
    		+ "ORDER BY c.commentNumber ASC" ),
	@NamedQuery(
		name  = "commentFindByPublicationDateRange",
		query = "SELECT c FROM Comment c, Publication p, PublicationGroup g, PublicationItem i, Registrant r "
    		+ "WHERE c.publicationState = :state AND r.registrationDate >= :rDate AND c.publicationItem = i AND c.submittedBy = r AND i.owner = g AND g.owner = p AND p.id = :publicationId "
    		+ "ORDER BY c.commentNumber ASC" ),
})
@NamedNativeQueries({
	@NamedNativeQuery(
		name  = "commentCounterNextVal",
		query = "SELECT next_val FROM comment_counter" ),
	@NamedNativeQuery(
		name  = "commentCounterCreate",
		query = "INSERT INTO comment_counter VALUES ( :nextValue )" ),
	@NamedNativeQuery(
		name  = "commentCounterUpdate",
		query = "UPDATE comment_counter SET next_val = :nextValue" ),
})
@Entity
@Table( name = "COMMENT" )
@Inheritance( strategy = InheritanceType.JOINED )
@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="daoCache" )
public abstract class Comment {
	
	@Id
	@Column( name = "ID", nullable = false )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	@Column( name = "COMMENT_NUMBER", nullable = false )
	private int commentNumber;
	
	@NotNull
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "PUBLICATION_ITEM_ID" )
	private PublicationItem publicationItem;
	
	@Size( min = 1, max = 200 )
	@Column( name = "OTHER_ITEM_NAME", nullable = true, length = 200 )
	private String otherItemName;
	
	@NotNull
	@Column( name = "PUB_STATE", nullable = false, length = 20 )
	@Enumerated( EnumType.STRING )
	private PublicationState publicationState;
	
	@NotNull
	@Column( name = "COMMENT_TYPE", nullable = false, length = 20 )
	@Enumerated( EnumType.STRING )
	private CommentType commentType;
	
	@NotNull
	@Size( min = 1, max = 65536 )
	@Column( name = "COMMENT_TEXT", nullable = false )
	private String commentText;
	
	@NotNull
	@Size( min = 1, max = 65536 )
	@Column( name = "SUGGESTED_CHANGE", nullable = false )
	private String suggestedChange;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "REGISTRANT_ID" )
	private Registrant submittedBy;

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
	 * Returns the value of the 'commentNumber' field.
	 *
	 * @return int
	 */
	public int getCommentNumber() {
		return commentNumber;
	}

	/**
	 * Assigns the value of the 'commentNumber' field.
	 *
	 * @param commentNumber  the field value to assign
	 */
	public void setCommentNumber(int commentNumber) {
		this.commentNumber = commentNumber;
	}

	/**
	 * Returns the value of the 'publicationItem' field.
	 *
	 * @return PublicationItem
	 */
	public PublicationItem getPublicationItem() {
		return publicationItem;
	}

	/**
	 * Assigns the value of the 'publicationItem' field.
	 *
	 * @param publicationItem  the field value to assign
	 */
	public void setPublicationItem(PublicationItem publicationItem) {
		this.publicationItem = publicationItem;
	}

	/**
	 * Returns the value of the 'otherItemName' field.
	 *
	 * @return String
	 */
	public String getOtherItemName() {
		return otherItemName;
	}

	/**
	 * Assigns the value of the 'otherItemName' field.
	 *
	 * @param otherItemName  the field value to assign
	 */
	public void setOtherItemName(String otherItemName) {
		this.otherItemName = otherItemName;
	}

	/**
	 * Returns the value of the 'publicationState' field.
	 *
	 * @return PublicationState
	 */
	public PublicationState getPublicationState() {
		return publicationState;
	}

	/**
	 * Assigns the value of the 'publicationState' field.
	 *
	 * @param publicationState  the field value to assign
	 */
	public void setPublicationState(PublicationState publicationState) {
		this.publicationState = publicationState;
	}

	/**
	 * Returns the value of the 'commentType' field.
	 *
	 * @return CommentType
	 */
	public CommentType getCommentType() {
		return commentType;
	}

	/**
	 * Assigns the value of the 'commentType' field.
	 *
	 * @param commentType  the field value to assign
	 */
	public void setCommentType(CommentType commentType) {
		this.commentType = commentType;
	}

	/**
	 * Returns the value of the 'commentText' field.
	 *
	 * @return String
	 */
	public String getCommentText() {
		return commentText;
	}

	/**
	 * Assigns the value of the 'commentText' field.
	 *
	 * @param commentText  the field value to assign
	 */
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	/**
	 * Returns the value of the 'suggestedChange' field.
	 *
	 * @return String
	 */
	public String getSuggestedChange() {
		return suggestedChange;
	}

	/**
	 * Assigns the value of the 'suggestedChange' field.
	 *
	 * @param suggestedChange  the field value to assign
	 */
	public void setSuggestedChange(String suggestedChange) {
		this.suggestedChange = suggestedChange;
	}

	/**
	 * Returns the value of the 'submittedBy' field.
	 *
	 * @return Registrant
	 */
	public Registrant getSubmittedBy() {
		return submittedBy;
	}

	/**
	 * Assigns the value of the 'submittedBy' field.
	 *
	 * @param submittedBy  the field value to assign
	 */
	public void setSubmittedBy(Registrant submittedBy) {
		this.submittedBy = submittedBy;
	}
	
}
