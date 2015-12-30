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

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Factory that provides access to <code>ConfigurationSettings</code> for the
 * application.  If the configuration properties file is deployed on the local file
 * system (as opposed to the classpath), this factory will detect real-time changes
 * and automatically update the cached settings.
 * 
 * <p>The location of the configuration file is determined by evaluating the following
 * criteria in order:
 * <ul>
 *   <li>If present, the system property 'pubs.config.location' specifies the name and
 *       location of the configuration properties file on the local file system.  If the
 *       file does not exist on the local file system, the classpath location is also
 *       checked.</li>
 *   <li>If running within an Apache Tomcat container, the <code>/conf</code> directory
 *       will be checked for a file named 'ota-publication-site.properties'.</li>
 *   <li>The local classpath  will be checked for a file named
 *       '/config/ota-publication-site.properties'.</li>
 *   <li>The current user directory (identified by the 'user.dir' system property) will
 *       be checked for a file named 'ota-publication-site.properties'.</li>
 * </ul>
 * @author S. Livezey
 */
public class ConfigSettingsFactory {
	
	public static final String SYSPROP_CONFIG_LOCATION = "pubs.config.location";
	public static final String DEFAULT_CONFIG_FILENAME = "ota-publication-site.properties";
	
    private static final Logger log = LoggerFactory.getLogger( ConfigSettingsFactory.class );
    
	private static ConfigSettingsResource fileResource;
	private static ConfigSettings classpathSettings;
	
	/**
	 * Private constructor.
	 */
	private ConfigSettingsFactory() {}
	
	/**
	 * Returns a new <code>ConfigSettings</code> instance that provides access to
	 * the application's configuration settings.
	 * 
	 * @return ConfigSettings
	 */
	public static ConfigSettings getConfig() {
		return (fileResource != null) ? fileResource.getResource() : classpathSettings;
	}
	
	/**
	 * Searches for the configuration settings file according to the criteria mentioned
	 * in the class description.
	 */
	private static void initConfigSettings() {
		try {
			String systemConfigLocation = System.getProperty( SYSPROP_CONFIG_LOCATION );
			boolean isInitialized = false;
			
			// Attempt to load from a file or classpath location specified by
			// a system property.
			if (systemConfigLocation != null) {
				File configFile = new File( systemConfigLocation );
				
				if (configFile.exists()) {
					fileResource = new ConfigSettingsResource( configFile );
					isInitialized = true;
					
				} else {
					isInitialized = initFromClasspath( systemConfigLocation );
				}
			}
			
			// Attempt to load from the /conf directory of a Tomcat installation
			String tomcatHome = System.getProperty( "catalina.base" );
			
			if (!isInitialized && (tomcatHome != null)) {
				File configFile = new File( tomcatHome, "/conf/" + DEFAULT_CONFIG_FILENAME );
				
				if (configFile.exists()) {
					fileResource = new ConfigSettingsResource( configFile );
					isInitialized = true;
				}
			}
			
			// Attempt to load from the default location on the classpath
			if (!isInitialized) {
				isInitialized = initFromClasspath( "/config/" + DEFAULT_CONFIG_FILENAME );
			}
			
			// Attempt to load from the user's current working directory
			if (!isInitialized) {
				File configFile = new File( System.getProperty( "user.dir" ), DEFAULT_CONFIG_FILENAME );
				
				if (configFile.exists()) {
					fileResource = new ConfigSettingsResource( configFile );
					isInitialized = true;
				}
			}
			
			// If not initialized, log a warning and initialize with an empty collection
			// of properties
			if (!isInitialized) {
				log.warn("Application configuration settings not found.");
				classpathSettings = new ConfigSettings( new Properties() );
			}
			
		} catch (Throwable t) {
			log.error("Error initializing configuration settings for the application.", t);
			classpathSettings = new ConfigSettings( new Properties() );
		}
	}
	
	/**
	 * Initializes the configuration settings from a file location on the classpath.  If
	 * successful, this method will return true.
	 * 
	 * @param classpathLocation  the classpath location 
	 * @return boolean
	 */
	private static boolean initFromClasspath(String classpathLocation) {
		InputStream is = ConfigSettingsFactory.class.getResourceAsStream( classpathLocation );
		boolean success = false;
		try {
			if (is != null) {
				Properties configProps = new Properties();
				
				configProps.load( is );
				classpathSettings = new ConfigSettings( configProps );
				success = true;
			}
		} catch (Throwable t) {
			log.error("Error reading configuraiton settings from classpath location: " +
					classpathLocation);
			
		} finally {
			try {
				if (is != null) is.close();
			} catch (Throwable t) {}
		}
		return success;
	}
	
	/**
	 * Initializes the configuration settings for the factory.
	 */
	static {
		try {
			initConfigSettings();
			
		} catch (Throwable t) {
			throw new ExceptionInInitializerError( t );
		}
	}
	
}
