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
package org.opentravel.pubs.config;

import java.util.Properties;

/**
 * Provides configuration settings for the application.
 * 
 * @author S. Livezey
 */
public class ConfigSettings {
	
	public static final String CONFIG_MAIN_SITE_URL   = "mainSite.url";
	public static final String CONFIG_LOCAL_SITE_URL  = "localSite.url";
	public static final String CONTENT_CACHE_LOCATION = "content.cache.location";
	public static final String ENCRYPTION_PASSWORD    = "encryption.password";
	public static final String ENVIRONMENT_ID         = "environmentId";
	
	private Properties configProps;
	
	/**
	 * Constructor that provides the underlying properties for the configuration
	 * settings.
	 * 
	 * @param configProps  the underlying properties for the configuraiton settings
	 */
	ConfigSettings(Properties configProps) {
		this.configProps = configProps;
	}
	
	/**
	 * Returns the base URL of the main OpenTravel web site.
	 * 
	 * @return String
	 */
	public String getMainSiteUrl() {
		return getProperty( CONFIG_MAIN_SITE_URL );
	}
	
	/**
	 * Returns the base URL of the OpenTravel publication site (this web application).
	 * 
	 * @return String
	 */
	public String getLocalSiteUrl() {
		return getProperty( CONFIG_LOCAL_SITE_URL );
	}
	
	/**
	 * Returns the location of the file content cache.
	 * 
	 * @return String
	 */
	public String getContentCacheLocation() {
		return getProperty( CONTENT_CACHE_LOCATION );
	}
	
	/**
	 * Returns the password used to encrypt password entries that are stored
	 * in persistent storage as application configuration settings.
	 * 
	 * @return String
	 */
	public String getEncryptionPassword() {
		String password = getProperty( ENCRYPTION_PASSWORD );
		
		if (password == null) {
			password = PasswordHelper.DEFAULT_ENCRYPTION_PASSWORD;
		}
		return password;
	}
	
	/**
	 * Returns the unique identifier for the local environment.
	 * 
	 * @return String
	 */
	public String getEnvironmentId() {
		return getProperty( ENVIRONMENT_ID );
	}
	
	/**
	 * Returns the value of the configuration setting property with the specified name.
	 * 
	 * @param propertyName  the name of the configuration setting property to return
	 * @return String
	 */
	public String getProperty(String propertyName) {
		return configProps.getProperty( propertyName );
	}
	
}
