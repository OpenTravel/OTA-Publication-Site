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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator used to enforce a comma-separated list of valid email address strings.
 */
public class EmailAddressListValidator implements ConstraintValidator<EmailAddressList,String> {
	
	/**
	 * @see javax.validation.ConstraintValidator#initialize(java.lang.annotation.Annotation)
	 */
	@Override
	public void initialize(EmailAddressList constraintAnnotation) {}
	
	/**
	 * @see javax.validation.ConstraintValidator#isValid(java.lang.Object, javax.validation.ConstraintValidatorContext)
	 */
	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		boolean result = true;
		
		if ((value != null) && ((value = value.trim()).length() > 0)) {
			String[] emailAddresses = value.split( "," );
			
			for (String emailAddress : emailAddresses) {
				emailAddress = emailAddress.trim();
				
				if (!(result = EmailAddressValidator.emailPattern.matcher( emailAddress ).matches())) {
					break;
				}
			}
		}
		return result;
	}
	
}
