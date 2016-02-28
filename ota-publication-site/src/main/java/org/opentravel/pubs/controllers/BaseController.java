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

package org.opentravel.pubs.controllers;

import java.util.Random;

import org.opentravel.pubs.config.ConfigSettings;
import org.opentravel.pubs.config.ConfigSettingsFactory;
import org.opentravel.pubs.dao.DAOFactoryManager;
import org.opentravel.pubs.model.PublicationType;
import org.opentravel.pubs.validation.ValidationException;
import org.opentravel.pubs.validation.ValidationResults;
import org.springframework.ui.Model;

/**
 * Abstract base class of all Spring Web MVC controllers for the OpenTravel specification site.
 *
 * @author S. Livezey
 */
public abstract class BaseController {
	
	static final String DEFAULT_ERROR_MESSAGE = "An error occured while displaying the page (please contact the system administrator).";
	static final int BUFFER_SIZE = 4096;
	
	private static Random randomizer = new Random( System.currentTimeMillis() );
	
    /**
     * Applies all values to the given model that are common to the entire application.
     * If 'targetPage' value will always be returned by this method if a non-null value
     * is provided. If null, a default value of "homePage" will be returned.
     * 
     * @param model  the UI model to which the common values should be applied
     * @param targetPage  the target navigation page for the controller
     * @return String
     */
    protected String applyCommonValues(Model model, String targetPage) {
    	ConfigSettings config = ConfigSettingsFactory.getConfig();
    	
    	model.addAttribute( "config", config );
    	model.addAttribute( "sidebarImageUrl", config.getMainSiteUrl() + getRandomSidebarImage() );
        model.addAttribute( "currentPage", targetPage );
        return (targetPage == null) ? "specificationMain" : targetPage;
    }
	
    /**
     * Returns the URL to one of the random sidebar images from the OpenTravel web site.
     * 
     * @return String
     */
    private String getRandomSidebarImage() {
    	return "/Resources/Uploads/sideRotationImages/i" + (randomizer.nextInt( 18 ) + 1) + ".jpg";
    }
    
	/**
	 * Assigns the status message for the console page that will be displayed.
	 * 
	 * @param statusMessage  the status message text
	 * @param model  the model to which the error message should be applied
	 */
	protected void setStatusMessage(String statusMessage, Model model) {
		model.addAttribute( "statusMessage", statusMessage );
	}
	
	/**
	 * Assigns the error message for the console page that will be displayed.  A call
	 * to this method also marks the current transaction for rollback.
	 * 
	 * @param errorMessage  the error message text
	 * @param model  the model to which the error message should be applied
	 */
	protected void setErrorMessage(String errorMessage, Model model) {
		model.addAttribute( "errorMessage", errorMessage );
		DAOFactoryManager.markForRollback();
	}
	
	/**
	 * Adds the given set of validation results to the model provided.  If any validation
	 * results already exist in the model, the new results are merged into the original set.
	 * 
	 * <p>The results are stored as a model attribute under the key "validationErrors".
	 * 
	 * <p>A call to this method also marks the current transaction for rollback.
	 * 
	 * @param validationEx  the exception that contains the set of validation results to add
	 * @param model  the model that will contain the validation results
	 */
	protected void addValidationErrors(ValidationException validationEx, Model model) {
		ValidationResults validationResults = validationEx.getValidationResults();
		
		if (validationResults != null) {
			addValidationErrors( validationResults, model );
			
		} else { // use the exception message as the UI error message
			setErrorMessage( validationEx.getMessage(), model );
		}
		DAOFactoryManager.markForRollback();
	}

	/**
	 * Adds the given set of validation results to the model provided.  If any validation
	 * results already exist in the model, the new results are merged into the original set.
	 * 
	 * <p>The results are stored as a model attribute under the key "validationErrors".
	 * 
	 * <p>A call to this method also marks the current transaction for rollback.
	 * 
	 * @param validationResults  the set of validation results to add
	 * @param model  the model that will contain the validation results
	 */
	protected void addValidationErrors(ValidationResults validationResults, Model model) {
		if (validationResults != null) {
			ValidationResults existingResults = (ValidationResults) model.asMap().get( "validationErrors" );
			
			if (existingResults != null) {
				existingResults.addAll( validationResults );
				
			} else {
				model.addAttribute( "validationErrors", validationResults );
			}
		}
	}
	
    /**
     * Makes a best attempt to resolve the give publication-type string to
     * a valid enumeration value.  If no determination can be made, the default
     * value returned will be <code>OTA_1_0</code>.
     * 
     * @param pubTypeStr  the publication-type string to resolve
     * @return PublicationType
     */
    protected PublicationType resolvePublicationType(String pubTypeStr) {
    	PublicationType result = null;
    	
    	try {
    		result = (pubTypeStr == null) ?
    				PublicationType.OTA_1_0 : PublicationType.valueOf( pubTypeStr );
    		
    	} catch (IllegalArgumentException e) {}
    	
    	if (result == null) {
    		if (pubTypeStr.indexOf('2') >= 0) {
    			result = PublicationType.OTA_2_0;
    			
    		} else {
    			result = PublicationType.OTA_1_0;
    		}
    	}
    	return result;
    }
    
}
