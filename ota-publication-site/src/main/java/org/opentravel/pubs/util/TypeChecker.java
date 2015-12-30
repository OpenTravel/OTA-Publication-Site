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

package org.opentravel.pubs.util;

/**
 * Provides function(s) for type checking within the context of a JSP page.
 * 
 * @author S. Livezey
 */
public class TypeChecker {
	
	private static TypeChecker instance = new TypeChecker();
	
	/**
	 * Private constructor. 
	 */
	public TypeChecker() {}
	
	/**
	 * Returns the <code>TypeChecker</code> instance.
	 * 
	 * @return TypeChecker
	 */
	public static TypeChecker getInstance() {
		return instance;
	}
	
	/**
	 * Returns true if the Java 'instanceof' operator matches the given class name
	 * to the object.
	 * 
	 * @param obj  the object to check
	 * @param className  the name of the class to apply for the 'instanceof' operator
	 * @return boolean
	 */
	public boolean instanceOf(Object obj, String className) {
		boolean result = false;
		try {
			if (obj != null) {
				result = Class.forName( className ).isAssignableFrom( obj.getClass() );
			}
			
		} catch (ClassNotFoundException e) {
			// No error - return false
		}
		return result;
	}
	
}
