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

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Model object representing the binary content of a file or other publication artifact.
 * 
 * @author S. Livezey
 */
@Entity
@Cacheable( false )
@Table( name = "FILE_CONTENT" )
public class FileContent {
	
	@Id
	@Column( name = "ID", nullable = false )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column( name = "FILE_BYTES", nullable = false )
	private byte[] fileBytes;

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
	 * Returns the value of the 'fileBytes' field.
	 *
	 * @return byte[]
	 */
	public byte[] getFileBytes() {
		return fileBytes;
	}

	/**
	 * Assigns the value of the 'fileBytes' field.
	 *
	 * @param fileBytes  the field value to assign
	 */
	public void setFileBytes(byte[] fileBytes) {
		this.fileBytes = fileBytes;
	}
	
}
