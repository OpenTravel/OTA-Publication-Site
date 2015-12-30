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

/**
 * Enumeration used to indicate the type of a publication.
 * 
 * @author S. Livezey
 */
public enum PublicationType {
	
	/** Indicates an OTA1.0 publication type. */
	OTA_1_0( "1.0", "OTA 1.0" ),
	
	/** Indicates an OTA2.0 publication type. */
	OTA_2_0( "2.0", "OTA 2.0" );
	
	private String displayId;
	private String displayValue;
	
	/**
	 * Constructor that specifies the display ID and value for an enumeration.
	 * 
	 * @param displayValue  the display value
	 */
	private PublicationType(String displayId, String displayValue) {
		this.displayId = displayId;
		this.displayValue = displayValue;
	}
	
	/**
	 * Returns the display identifier.
	 * 
	 * @return String
	 */
	public String getDisplayId() {
		return displayId;
	}
	
	/**
	 * Returns the display value.
	 * 
	 * @return String
	 */
	public String getDisplayValue() {
		return displayValue;
	}
	
}
