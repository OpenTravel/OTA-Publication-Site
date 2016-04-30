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
package org.opentravel.pubs.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;

/**
 * Container for the <code>ConstraintViolations</code> detected during the validation
 * of a model object.
 * 
 * @author S. Livezey
 */
public class ValidationResults {
	
	private Map<String,List<ConstraintViolation<Object>>> violationMap = new HashMap<>();
	
	/**
	 * Default constructor.
	 */
	public ValidationResults() {}
	
	/**
	 * Constructor that provides the set of validation for the results.
	 * 
	 * @param violations  the set of constraint violations detected during validation
	 */
	ValidationResults(Set<ConstraintViolation<Object>> violations) {
		addAll( violations );
	}
	
	/**
	 * Merges the contents of the given <code>ValidationResults</code> set with
	 * this one.  Any duplicate path entries are overwritten.
	 * 
	 * @param otherResults  the collection of validation results to merge
	 */
	public void addAll(ValidationResults otherResults) {
		if (otherResults != null) {
			Set<ConstraintViolation<Object>> otherViolations = new HashSet<>();
			
			for (List<ConstraintViolation<Object>> vList : otherResults.violationMap.values()) {
				otherViolations.addAll( vList );
			}
			addAll( otherViolations );
		}
	}
	
	/**
	 * Adds the given set of violations to the violation map.
	 * 
	 * @param violations  the set of constraint violations to add
	 */
	@SuppressWarnings("unchecked")
	private void addAll(Set<ConstraintViolation<Object>> violations) {
		if (violations != null) {
			for (ConstraintViolation<?> violation : violations) {
				Object leafBean = violation.getLeafBean();
				String basePath = (leafBean == null) ? "UNKNOWN" : leafBean.getClass().getSimpleName();
				
				for (Path.Node pathNode : violation.getPropertyPath()) {
					String path = basePath + "." + pathNode.getName();
					
					List<ConstraintViolation<Object>> pathViolations = violationMap.get( path );
					
					if (pathViolations == null) {
						pathViolations = new ArrayList<>();
						violationMap.put( path, pathViolations );
					}
					pathViolations.add( (ConstraintViolation<Object>) violation );
				}
			}
		}
	}
	
	/**
	 * Manually adds an error message that did not originate from commons validation
	 * processing.
	 * 
	 * @param bean  the object where the error was detected
	 * @param propertyName  the name of the property that caused the error
	 * @param errorMessage  the error message
	 */
	@SuppressWarnings("unchecked")
	public void add(Object bean, String propertyName, String errorMessage) {
		ConstraintViolation<Object> violation = ConstraintViolationImpl.forBeanValidation(
				null, null, errorMessage, (Class<Object>) bean.getClass(), bean, bean, null,
				PathImpl.createPathFromString( propertyName ), null, null );
		Set<ConstraintViolation<Object>> violations = new HashSet<>();
		
		violations.add( violation );
		addAll( violations );
	}
	
	/**
	 * Returns the collection of path names for which constraint violations were detected.
	 * 
	 * @return Set<String>
	 */
	public Set<String> getViolationPaths() {
		return Collections.unmodifiableSet( violationMap.keySet() );
	}
	
	/**
	 * Returns true if one or more constraint violations were detected.
	 * 
	 * @return boolean
	 */
	public boolean hasViolations() {
		return !violationMap.isEmpty();
	}
	
	/**
	 * Returns true if one or more constraint violations were detected for the specified
	 * path name.
	 * 
	 * @param path  the path name to check for the existence of violations
	 * @return boolean
	 */
	public boolean hasViolation(String path) {
		return violationMap.containsKey( path );
	}
	
	/**
	 * Returns the collection of constraint violations for the specified path.
	 * 
	 * @param path  the path name for which to return constraint violations
	 * @return List<ConstraintViolation<?>>
	 */
	public List<ConstraintViolation<Object>> getViolations(String path) {
		List<ConstraintViolation<Object>> violations = violationMap.get( path );
		
		if (violations == null) {
			violations = Collections.emptyList();
		}
		return violations;
	}
	
	/**
	 * Returns the constraint validation messages for the specified path.
	 * 
	 * @param path  the path name for which to return constraint violation messages
	 * @return List<String>
	 */
	public List<String> getMessages(String path) {
		List<String> messageList = new ArrayList<>();
		
		for (ConstraintViolation<?> violation : getViolations( path )) {
			messageList.add( violation.getMessage() );
		}
		return messageList;
	}
	
	/**
	 * Returns the first constraint validation message for the specified path or null
	 * if no violations were detected at the path.
	 * 
	 * @param path  the path name for which to return a constraint violation message
	 * @return String
	 */
	public String getMessage(String path) {
		List<ConstraintViolation<Object>> violations = getViolations( path );
		
		return violations.isEmpty() ? null : violations.get( 0 ).getMessage();
	}
	
}
