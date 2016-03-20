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

/**
 * Base class for all form model object types.
 */
public class AbstractForm {
	
	private boolean processForm = false;
	
	/**
	 * Returns the flag value indicating whether the form has been initialized and submitted
	 * by the user for processing.
	 *
	 * @return boolean
	 */
	public boolean isProcessForm() {
		return processForm;
	}
	
	/**
	 * Assigns the flag value indicating whether the form has been initialized and submitted
	 * by the user for processing.
	 *
	 * @param processForm  the field value to assign
	 */
	public void setProcessForm(boolean processForm) {
		this.processForm = processForm;
	}
	
}
