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
 * Abstract base class that provides a nested registration form instance.
 */
public abstract class AbstractRegistrantContainerForm extends AbstractForm {
	
	private RegistrantForm registrantForm = new RegistrantForm();
	
	/**
	 * Returns the value of the 'registrantForm' field.
	 *
	 * @return RegistrantForm
	 */
	public RegistrantForm getRegistrantForm() {
		return registrantForm;
	}
	
}
