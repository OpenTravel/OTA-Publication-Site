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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.opentravel.pubs.util.FileResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * File resource that obtains its <code>ConfigSettings</code> content from a standard
 * Java properties file.
 * 
 * @author S. Livezey
 */
public class ConfigSettingsResource extends FileResource<ConfigSettings> {
	
    private static final Logger log = LoggerFactory.getLogger( ConfigSettingsResource.class );
    
    /**
     * Constructor that specifies the data file from which the resource is to be loaded.
     * 
     * @param dataFile  the data file that provides persistent content for this resource instance
     */
	public ConfigSettingsResource(File dataFile) {
		super( dataFile );
	}

	/**
	 * @see org.opentravel.pubs.util.FileResource#loadResource(java.io.File)
	 */
	@Override
	protected ConfigSettings loadResource(File dataFile) throws IOException {
		Properties props = new Properties();
		
		if (dataFile != null) {
			if (dataFile.exists()) {
				try (InputStream is = new FileInputStream( dataFile )) {
					props.load( is );
					
				} catch (IOException e) {
					log.warn("Warning - Error reading from file: " + dataFile.getName());
				}
				
			} else {
				log.warn("Warning - Property file does not exist: " + dataFile.getAbsolutePath());
			}
		}
		return new ConfigSettings( props );
	}
	
}
