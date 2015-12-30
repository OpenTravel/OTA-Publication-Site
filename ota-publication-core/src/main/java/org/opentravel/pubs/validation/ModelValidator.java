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

import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Performs the validation of persistent model objects and and reports the findings
 * as <code>ValidationResults</code>.
 * 
 * @author S. Livezey
 */
public class ModelValidator {
	
    private static Validator validator;
    
    /**
     * Validates the given model object and returns the collection of
     * <code>ValidationResults</code>.
     * 
     * @param obj  the model object to be validated
     * @return ValidationResults
     */
    public static ValidationResults validate(Object obj) {
    	return new ValidationResults( validator.validate( obj ) );
    }
    
    static {
    	try {
    		validator = Validation.buildDefaultValidatorFactory().getValidator();
    		
    	} catch (Throwable t) {
    		throw new ExceptionInInitializerError( t );
    	}
    }
    
}
