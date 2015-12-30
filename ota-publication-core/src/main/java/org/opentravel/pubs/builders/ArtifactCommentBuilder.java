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

import org.opentravel.pubs.model.ArtifactComment;
import org.opentravel.pubs.model.CommentType;
import org.opentravel.pubs.model.PublicationItem;
import org.opentravel.pubs.model.PublicationState;
import org.opentravel.pubs.model.Registrant;

/**
 * Builder used to construct new <code>ArtifactComment</code> instances.
 *
 * @author S. Livezey
 */
public class ArtifactCommentBuilder {
	
	private PublicationItem publicationItem;
	private String otherItemName;
	private PublicationState publicationState;
	private CommentType commentType;
	private String commentText;
	private String pageNumbers;
	private String lineNumbers;
	private String suggestedChange;
	private Registrant submittedBy;
	
	/**
	 * Constructs the new <code>ArtifactComment</code> instance based on the current
	 * state of this builder.
	 * 
	 * @return ArtifactComment
	 */
	public ArtifactComment build() {
		ArtifactComment ac = new ArtifactComment();
		
		if (publicationItem != null) {
			ac.setPublicationItem( publicationItem );
		} else {
			ac.setOtherItemName( otherItemName );
		}
		ac.setPublicationState( publicationState );
		ac.setCommentType( commentType );
		ac.setCommentText( commentText );
		ac.setSuggestedChange( suggestedChange );
		ac.setPageNumbers( pageNumbers );
		ac.setLineNumbers( lineNumbers );
		ac.setSubmittedBy( submittedBy );
		return ac;
	}
	
	/**
	 * Assigns the value of the 'publicationItem' field.
	 *
	 * @param publicationItem  the field value to assign
	 * @return ArtifactCommentBuilder
	 */
	public ArtifactCommentBuilder setPublicationItem(PublicationItem publicationItem) {
		this.publicationItem = publicationItem;
		return this;
	}
	
	/**
	 * Assigns the value of the 'otherItemName' field.
	 *
	 * @param otherItemName  the field value to assign
	 * @return ArtifactCommentBuilder
	 */
	public ArtifactCommentBuilder setOtherItemName(String otherItemName) {
		this.otherItemName = otherItemName;
		return this;
	}
	
	/**
	 * Assigns the value of the 'publicationState' field.
	 *
	 * @param publicationState  the field value to assign
	 * @return ArtifactCommentBuilder
	 */
	public ArtifactCommentBuilder setPublicationState(PublicationState publicationState) {
		this.publicationState = publicationState;
		return this;
	}

	/**
	 * Assigns the value of the 'commentType' field.
	 *
	 * @param commentType  the field value to assign
	 * @return ArtifactCommentBuilder
	 */
	public ArtifactCommentBuilder setCommentType(CommentType commentType) {
		this.commentType = commentType;
		return this;
	}
	
	/**
	 * Assigns the value of the 'commentText' field.
	 *
	 * @param commentText  the field value to assign
	 * @return ArtifactCommentBuilder
	 */
	public ArtifactCommentBuilder setCommentText(String commentText) {
		this.commentText = commentText;
		return this;
	}
	
	/**
	 * Assigns the value of the 'pageNumbers' field.
	 *
	 * @param pageNumbers  the field value to assign
	 * @return ArtifactCommentBuilder
	 */
	public ArtifactCommentBuilder setPageNumbers(String pageNumbers) {
		this.pageNumbers = pageNumbers;
		return this;
	}
	
	/**
	 * Assigns the value of the 'lineNumbers' field.
	 *
	 * @param lineNumbers  the field value to assign
	 * @return ArtifactCommentBuilder
	 */
	public ArtifactCommentBuilder setLineNumbers(String lineNumbers) {
		this.lineNumbers = lineNumbers;
		return this;
	}
	
	/**
	 * Assigns the value of the 'suggestedChange' field.
	 *
	 * @param suggestedChange  the field value to assign
	 * @return ArtifactCommentBuilder
	 */
	public ArtifactCommentBuilder setSuggestedChange(String suggestedChange) {
		this.suggestedChange = suggestedChange;
		return this;
	}
	
	/**
	 * Assigns the value of the 'submittedBy' field.
	 *
	 * @param submittedBy  the field value to assign
	 * @return ArtifactCommentBuilder
	 */
	public ArtifactCommentBuilder setSubmittedBy(Registrant submittedBy) {
		this.submittedBy = submittedBy;
		return this;
	}
	
}
