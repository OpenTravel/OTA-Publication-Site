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

import java.util.Date;

import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationState;
import org.opentravel.pubs.model.PublicationType;

/**
 * Builder used to construct new <code>Publication</code> instances.
 *
 * @author S. Livezey
 */
public class PublicationBuilder {
	
	private String name;
	private PublicationType type;
	private PublicationState state;
	private Date publicationDate = new Date();
	private String releaseNotesFilename;
	
	/**
	 * Constructs the new <code>Publication</code> instance based on the current
	 * state of this builder.
	 * 
	 * @return Publication
	 */
	public Publication build() {
		Publication p = new Publication();
		
		p.setName( name );
		p.setType( type );
		p.setState( state );
		p.setPublicationDate( publicationDate );
		p.setReleaseNotesFilename( releaseNotesFilename );
		return p;
	}

	/**
	 * Assigns the value of the 'name' field.
	 *
	 * @param name  the field value to assign
	 * @return PublicationBuilder
	 */
	public PublicationBuilder setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Assigns the value of the 'type' field.
	 *
	 * @param type  the field value to assign
	 * @return PublicationBuilder
	 */
	public PublicationBuilder setType(PublicationType type) {
		this.type = type;
		return this;
	}

	/**
	 * Assigns the value of the 'state' field.
	 *
	 * @param state  the field value to assign
	 * @return PublicationBuilder
	 */
	public PublicationBuilder setState(PublicationState state) {
		this.state = state;
		return this;
	}

	/**
	 * Assigns the value of the 'publicationDate' field.
	 *
	 * @param publicationDate  the field value to assign
	 * @return PublicationBuilder
	 */
	public PublicationBuilder setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
		return this;
	}

	/**
	 * Assigns the value of the 'releaseNotesFilename' field.
	 *
	 * @param releaseNotesFilename  the field value to assign
	 * @return PublicationBuilder
	 */
	public PublicationBuilder setReleaseNotesFilename(String releaseNotesFilename) {
		this.releaseNotesFilename = releaseNotesFilename;
		return this;
	}
	
}
