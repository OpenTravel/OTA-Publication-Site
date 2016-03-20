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
package org.opentravel.pubs.forms;

import org.opentravel.pubs.model.CommentType;

/**
 * Form used for the submission of new schema comments.
 */
public class SchemaCommentForm extends AbstractRegistrantContainerForm {
	
    private Long itemId;
    private CommentType commentType;
    private String commentText;
    private String suggestedChange;
    private String commentXPath;
    private String modifyXPath;
    private String newAnnotations;
    
	/**
	 * Returns the value of the 'itemId' field.
	 *
	 * @return Long
	 */
	public Long getItemId() {
		return itemId;
	}
	
	/**
	 * Assigns the value of the 'itemId' field.
	 *
	 * @param itemId  the field value to assign
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
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
	 * Returns the value of the 'commentXPath' field.
	 *
	 * @return String
	 */
	public String getCommentXPath() {
		return commentXPath;
	}
	
	/**
	 * Assigns the value of the 'commentXPath' field.
	 *
	 * @param commentXPath  the field value to assign
	 */
	public void setCommentXPath(String commentXPath) {
		this.commentXPath = commentXPath;
	}
	
	/**
	 * Returns the value of the 'modifyXPath' field.
	 *
	 * @return String
	 */
	public String getModifyXPath() {
		return modifyXPath;
	}
	
	/**
	 * Assigns the value of the 'modifyXPath' field.
	 *
	 * @param modifyXPath  the field value to assign
	 */
	public void setModifyXPath(String modifyXPath) {
		this.modifyXPath = modifyXPath;
	}
	
	/**
	 * Returns the value of the 'newAnnotations' field.
	 *
	 * @return String
	 */
	public String getNewAnnotations() {
		return newAnnotations;
	}
	
	/**
	 * Assigns the value of the 'newAnnotations' field.
	 *
	 * @param newAnnotations  the field value to assign
	 */
	public void setNewAnnotations(String newAnnotations) {
		this.newAnnotations = newAnnotations;
	}
    
}
