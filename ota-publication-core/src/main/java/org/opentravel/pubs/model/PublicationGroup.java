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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Model object representing a grouping of publication items.
 * 
 * @author S. Livezey
 */
@Entity
@Table( name = "PUBLICATION_GROUP" )
public class PublicationGroup {
	
	@Id
	@Column( name = "ID", nullable = false )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne( fetch = FetchType.LAZY )
	@JoinColumn( name = "PUBLICATION_ID" )
	private Publication owner;
	
	@NotNull
	@Size( min = 1, max = 50 )
	@Column( name = "NAME", nullable = false, length = 50 )
	private String name;
	
	@NotNull
	@Column( name = "MEMBER_TYPE", nullable = false, length = 20 )
	@Enumerated( EnumType.STRING )
	private PublicationItemType memberType;
	
	@NotNull
	@Column( name = "SORT_ORDER", nullable = false )
	private int sortOrder;
	
	@OneToMany( mappedBy = "owner", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE } )
	@OrderBy( "itemFilename ASC" )
	private List<PublicationItem> publicationItems;

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
	 * @return Publication
	 */
	public Publication getOwner() {
		return owner;
	}

	/**
	 * Assigns the value of the 'owner' field.
	 *
	 * @param owner  the field value to assign
	 */
	public void setOwner(Publication owner) {
		this.owner = owner;
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
	 * Returns the value of the 'memberType' field.
	 *
	 * @return PublicationItemType
	 */
	public PublicationItemType getMemberType() {
		return memberType;
	}

	/**
	 * Assigns the value of the 'memberType' field.
	 *
	 * @param memberType  the field value to assign
	 */
	public void setMemberType(PublicationItemType memberType) {
		this.memberType = memberType;
	}

	/**
	 * Returns the value of the 'sortOrder' field.
	 *
	 * @return int
	 */
	public int getSortOrder() {
		return sortOrder;
	}

	/**
	 * Assigns the value of the 'sortOrder' field.
	 *
	 * @param sortOrder  the field value to assign
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * Returns the value of the 'publicationItems' field.
	 *
	 * @return List<PublicationItem>
	 */
	public List<PublicationItem> getPublicationItems() {
		return publicationItems;
	}

	/**
	 * Assigns the value of the 'publicationItems' field.
	 *
	 * @param publicationItems  the field value to assign
	 */
	public void setPublicationItems(List<PublicationItem> publicationItems) {
		this.publicationItems = publicationItems;
	}
	
}
