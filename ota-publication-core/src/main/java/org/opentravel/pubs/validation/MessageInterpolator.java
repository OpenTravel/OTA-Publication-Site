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

import java.util.Locale;
import java.util.ResourceBundle;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

/**
 * Custom interpolator used for the formatting of bean validation messages.
 * 
 * @author S. Livezey
 */
public class MessageInterpolator extends ResourceBundleMessageInterpolator {
	
	public static final String PUBLICATION_MESSAGES_LOCATION = "org.opentravel.pubs.publication-messages";
	
	/**
	 * Default constructor.
	 */
	public MessageInterpolator() {
		super( new ResourceBundleLocator() {
			public ResourceBundle getResourceBundle(Locale locale) {
				return ResourceBundle.getBundle( PUBLICATION_MESSAGES_LOCATION, locale );
			}
		});
	}
	
}
