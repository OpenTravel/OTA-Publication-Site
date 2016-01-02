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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Model object representing a comment on a publication that was submitted by a
 * registered user.
 * 
 * @author S. Livezey
 */
@Entity
@Table( name = "schema_comment" )
@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="daoCache" )
public class SchemaComment extends Comment {
	
	private static final long serialVersionUID = -6696641913793342983L;

	@NotNull
	@Size( min = 1, max = 200 )
	@Column( name = "comment_xpath", nullable = false, length = 200 )
	private String commentXPath;
	
	@NotNull
	@Size( min = 1, max = 200 )
	@Column( name = "modify_xpath", nullable = false, length = 200 )
	private String modifyXPath;
	
	@NotNull
	@Size( min = 0, max = 65536 )
	@Column( name = "new_annotations", nullable = false )
	private String newAnnotations;
	
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
