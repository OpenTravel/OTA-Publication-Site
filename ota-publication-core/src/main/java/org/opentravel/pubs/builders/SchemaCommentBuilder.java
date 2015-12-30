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
package org.opentravel.pubs.builders;

import org.opentravel.pubs.model.CommentType;
import org.opentravel.pubs.model.PublicationItem;
import org.opentravel.pubs.model.PublicationState;
import org.opentravel.pubs.model.Registrant;
import org.opentravel.pubs.model.SchemaComment;

/**
 * Builder used to construct new <code>SchemaComment</code> instances.
 *
 * @author S. Livezey
 */
public class SchemaCommentBuilder {
	
	private PublicationItem publicationItem;
	private String otherItemName;
	private PublicationState publicationState;
	private CommentType commentType;
	private String commentText;
	private String suggestedChange;
	private String commentXPath;
	private String modifyXPath;
	private String newAnnotations;
	private Registrant submittedBy;
	
	/**
	 * Constructs the new <code>SchemaComment</code> instance based on the current
	 * state of this builder.
	 * 
	 * @return SchemaComment
	 */
	public SchemaComment build() {
		SchemaComment sc = new SchemaComment();
		
		if (publicationItem != null) {
			sc.setPublicationItem( publicationItem );
		} else {
			sc.setOtherItemName( otherItemName );
		}
		sc.setPublicationState( publicationState );
		sc.setCommentType( commentType );
		sc.setCommentText( commentText );
		sc.setSuggestedChange( suggestedChange );
		sc.setCommentXPath( commentXPath );
		sc.setModifyXPath( modifyXPath );
		sc.setNewAnnotations( newAnnotations );
		sc.setSubmittedBy( submittedBy );
		return sc;
	}
	
	/**
	 * Assigns the value of the 'publicationItem' field.
	 *
	 * @param publicationItem  the field value to assign
	 * @return SchemaCommentBuilder
	 */
	public SchemaCommentBuilder setPublicationItem(PublicationItem publicationItem) {
		this.publicationItem = publicationItem;
		return this;
	}
	
	/**
	 * Assigns the value of the 'otherItemName' field.
	 *
	 * @param otherItemName  the field value to assign
	 * @return SchemaCommentBuilder
	 */
	public SchemaCommentBuilder setOtherItemName(String otherItemName) {
		this.otherItemName = otherItemName;
		return this;
	}
	
	/**
	 * Assigns the value of the 'publicationState' field.
	 *
	 * @param publicationState  the field value to assign
	 * @return SchemaCommentBuilder
	 */
	public SchemaCommentBuilder setPublicationState(PublicationState publicationState) {
		this.publicationState = publicationState;
		return this;
	}

	/**
	 * Assigns the value of the 'commentType' field.
	 *
	 * @param commentType  the field value to assign
	 * @return SchemaCommentBuilder
	 */
	public SchemaCommentBuilder setCommentType(CommentType commentType) {
		this.commentType = commentType;
		return this;
	}
	
	/**
	 * Assigns the value of the 'commentText' field.
	 *
	 * @param commentText  the field value to assign
	 * @return SchemaCommentBuilder
	 */
	public SchemaCommentBuilder setCommentText(String commentText) {
		this.commentText = commentText;
		return this;
	}
	
	/**
	 * Assigns the value of the 'suggestedChange' field.
	 *
	 * @param suggestedChange  the field value to assign
	 * @return SchemaCommentBuilder
	 */
	public SchemaCommentBuilder setSuggestedChange(String suggestedChange) {
		this.suggestedChange = suggestedChange;
		return this;
	}
	
	/**
	 * Assigns the value of the 'commentXPath' field.
	 *
	 * @param commentXPath  the field value to assign
	 * @return SchemaCommentBuilder
	 */
	public SchemaCommentBuilder setCommentXPath(String commentXPath) {
		this.commentXPath = commentXPath;
		return this;
	}
	
	/**
	 * Assigns the value of the 'modifyXPath' field.
	 *
	 * @param modifyXPath  the field value to assign
	 * @return SchemaCommentBuilder
	 */
	public SchemaCommentBuilder setModifyXPath(String modifyXPath) {
		this.modifyXPath = modifyXPath;
		return this;
	}
	
	/**
	 * Assigns the value of the 'newAnnotations' field.
	 *
	 * @param newAnnotations  the field value to assign
	 * @return SchemaCommentBuilder
	 */
	public SchemaCommentBuilder setNewAnnotations(String newAnnotations) {
		this.newAnnotations = newAnnotations;
		return this;
	}
	
	/**
	 * Assigns the value of the 'submittedBy' field.
	 *
	 * @param submittedBy  the field value to assign
	 * @return SchemaCommentBuilder
	 */
	public SchemaCommentBuilder setSubmittedBy(Registrant submittedBy) {
		this.submittedBy = submittedBy;
		return this;
	}
	
}
