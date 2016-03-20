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
 * Form used during the view and download specification pages to pass web
 * site registrant information.
 */
public class ViewSpecificationForm extends AbstractRegistrantContainerForm {
	
	/**
	 * @see org.opentravel.pubs.forms.AbstractForm#setProcessForm(boolean)
	 */
	@Override
	public void setProcessForm(boolean processForm) {
		getRegistrantForm().setProcessForm( processForm );
		super.setProcessForm( processForm );
	}
	
}
