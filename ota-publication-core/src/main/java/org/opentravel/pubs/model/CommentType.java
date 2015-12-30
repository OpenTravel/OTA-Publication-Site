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
 * Enumeration used to indicate the general type or purpose of a submitted comment.
 * 
 * @author S. Livezey
 */
public enum CommentType {
	
	/** Indicates a technical comment (e.g. schema, W3C, ebXML, etc.). */
	TECHNICAL( "Technical" ),
	
	/** Indicates an editorial comment (e.g. grammar, spelling, punctuation). */
	EDITORIAL( "Editorial" ),
	
	/** Indicates a business comment (e.g. proper use of rules, data fields). */
	BUSINESS( "Business" ),
	
	/** Indicates a comment that does not fall under one of the other categories. */
	OTHER( "Other" );
	
	private String displayName;
	
	/**
	 * Constructor that specifies the display name of the value.
	 * 
	 * @param displayName  the display name of the value
	 */
	private CommentType(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Returns the display name of the value.
	 *
	 * @return String
	 */
	public String getDisplayName() {
		return displayName;
	}
	
}
