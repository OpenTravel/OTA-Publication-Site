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

import org.opentravel.pubs.model.CodeList;

/**
 * Form used for the creation and update of code lists.
 */
public class CodeListForm extends AbstractForm {
	
	private Long codeListId;
	private String releaseDateLabel;
	
	/**
	 * Initializes the contents of this form using value supplied from the
	 * given codeList.
	 * 
	 * @param codeList  the codeList from which to initialize this form
	 */
	public void initialize(CodeList codeList) {
		this.codeListId = codeList.getId();
		this.releaseDateLabel = codeList.getReleaseDateLabel();
	}

	/**
	 * Returns the value of the 'codeListId' field.
	 *
	 * @return Long
	 */
	public Long getCodeListId() {
		return codeListId;
	}

	/**
	 * Assigns the value of the 'codeListId' field.
	 *
	 * @param codeListId  the field value to assign
	 */
	public void setCodeListId(Long codeListId) {
		this.codeListId = codeListId;
	}

	/**
	 * Returns the value of the 'releaseDateLabel' field.
	 *
	 * @return String
	 */
	public String getReleaseDateLabel() {
		return releaseDateLabel;
	}

	/**
	 * Assigns the value of the 'releaseDateLabel' field.
	 *
	 * @param releaseDateLabel  the field value to assign
	 */
	public void setReleaseDateLabel(String releaseDateLabel) {
		this.releaseDateLabel = releaseDateLabel;
	}

}
