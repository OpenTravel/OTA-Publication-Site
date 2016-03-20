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

import java.util.Arrays;
import java.util.List;

import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationState;

/**
 * Form used for the creation and update of specifications.
 */
public class SpecificationForm extends AbstractForm {
	
	private Long publicationId;
	private String name;
	private String specType;
	private String pubState;
	
	/**
	 * Initializes the contents of this form using value supplied from the
	 * given publication.
	 * 
	 * @param publication  the publication from which to initialize this form
	 */
	public void initialize(Publication publication) {
		this.publicationId = publication.getId();
		this.name = publication.getName();
		this.specType = publication.getType().toString();
		this.pubState = publication.getState().toString();
	}
	
	/**
	 * Returns the value of the 'publicationId' field.
	 *
	 * @return Long
	 */
	public Long getPublicationId() {
		return publicationId;
	}
	
	/**
	 * Assigns the value of the 'publicationId' field.
	 *
	 * @param publicationId  the field value to assign
	 */
	public void setPublicationId(Long publicationId) {
		this.publicationId = publicationId;
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
	 * Returns the value of the 'specType' field.
	 *
	 * @return String
	 */
	public String getSpecType() {
		return specType;
	}
	
	/**
	 * Assigns the value of the 'specType' field.
	 *
	 * @param specType  the field value to assign
	 */
	public void setSpecType(String specType) {
		this.specType = specType;
	}
	
	/**
	 * Returns the value of the 'pubState' field.
	 *
	 * @return String
	 */
	public String getPubState() {
		return pubState;
	}
	
	/**
	 * Assigns the value of the 'pubState' field.
	 *
	 * @param pubState  the field value to assign
	 */
	public void setPubState(String pubState) {
		this.pubState = pubState;
	}
	
	/**
	 * Returns the list of all valid <code>PublicationState</code> values.
	 * 
	 * @return List<PublicationState>
	 */
	public List<PublicationState> getPublicationStates() {
		return Arrays.asList( PublicationState.values() );
	}
	
}
