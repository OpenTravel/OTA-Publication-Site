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
 * Enumeration that indicates the current state of a publication.
 * 
 * @author S. Livezey
 */
public enum PublicationState {
	
	/** Indicates that a publication is open for review by OpenTravel members. */
	MEMBER_REVIEW( "Member Review" ),
	
	/** Indicates that a publication is open for review by the general public. */
	PUBLIC_REVIEW( "Public Review" ),
	
	/** Indicates that a publication has been officially released. */
	RELEASED( "Released" );
	
	private String displayValue;
	
	/**
	 * Constructor that specifies the display value for an enumeration.
	 * 
	 * @param displayValue  the display value
	 */
	private PublicationState(String displayValue) {
		this.displayValue = displayValue;
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
