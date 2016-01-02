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
 * Model object representing a comment on a non-publication artifact that was submitted
 * by a registered user.
 * 
 * @author S. Livezey
 */
@Entity
@Table( name = "artifact_comment" )
@Cache( usage = CacheConcurrencyStrategy.READ_WRITE )
public class ArtifactComment extends Comment {
	
	private static final long serialVersionUID = -7187981847041093740L;

	@NotNull
	@Size( min = 1, max = 100 )
	@Column( name = "page_numbers", nullable = false, length = 100 )
	private String pageNumbers;
	
	@NotNull
	@Size( min = 1, max = 100 )
	@Column( name = "line_numbers", nullable = false, length = 100 )
	private String lineNumbers;
	
	/**
	 * Returns the value of the 'pageNumbers' field.
	 *
	 * @return String
	 */
	public String getPageNumbers() {
		return pageNumbers;
	}

	/**
	 * Assigns the value of the 'pageNumbers' field.
	 *
	 * @param pageNumbers  the field value to assign
	 */
	public void setPageNumbers(String pageNumbers) {
		this.pageNumbers = pageNumbers;
	}

	/**
	 * Returns the value of the 'lineNumbers' field.
	 *
	 * @return String
	 */
	public String getLineNumbers() {
		return lineNumbers;
	}

	/**
	 * Assigns the value of the 'lineNumbers' field.
	 *
	 * @param lineNumbers  the field value to assign
	 */
	public void setLineNumbers(String lineNumbers) {
		this.lineNumbers = lineNumbers;
	}

}
