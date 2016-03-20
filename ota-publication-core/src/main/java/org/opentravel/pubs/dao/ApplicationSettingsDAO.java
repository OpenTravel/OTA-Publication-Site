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
package org.opentravel.pubs.dao;

import java.util.List;
import java.util.Properties;

import javax.persistence.TypedQuery;

import org.opentravel.pubs.model.ApplicationSettings;

/**
 * DAO that provides operations related to the storage and retrieval of application
 * configuration settings.
 */
public class ApplicationSettingsDAO extends AbstractDAO {
	
	/**
	 * Constructor that supplies the factory which created this DAO instance.
	 * 
	 * @param factory  the factory that created this DAO
	 */
	ApplicationSettingsDAO(DAOFactory factory) {
		super(factory);
	}
	
	/**
	 * Returns the set of application settings with the given name.  If no such set of
	 * application settings have been defined, this method will return null.
	 * 
	 * @param name  the name of the application settings to retrieve
	 * @return Properties
	 */
	public Properties getSettings(String name) {
		ApplicationSettings settings = getApplicationSettings( name );
		return (settings == null) ? null : settings.getProperties();
	}
	
	/**
	 * Saves the given application settings to persistent storage.
	 * 
	 * @param name  the name of the application settings to save
	 * @param settings  the application settings to save
	 */
	public void saveSettings(String name, Properties configSettings) {
		ApplicationSettings settings = getApplicationSettings( name );
		
		if (settings == null) {
			settings = new ApplicationSettings();
			settings.setName( name );
			settings.setProperties( configSettings );
			getEntityManager().persist( settings );
			
		} else {
			settings.setProperties( configSettings );
		}
	}
	
	/**
	 * Deletes the application settings with the given name from persistent storage.
	 * 
	 * @param name  the name of the application settings to delete
	 */
	public void deleteSettings(String name) {
		ApplicationSettings settings = getApplicationSettings( name );
		
		if (settings != null) {
			getEntityManager().remove( settings );
		}
	}
	
	/**
	 * Retrieves the <code>ApplictionSettings</code> model object with the given name.  If
	 * no such set of configuration settings exists, this method will return null.
	 * 
	 * @param name  the name of the application settings set to retrieve
	 * @return ApplicationSettings
	 */
	private ApplicationSettings getApplicationSettings(String name) {
		TypedQuery<ApplicationSettings> query = getEntityManager().createNamedQuery(
				"settingsFindByName", ApplicationSettings.class );
		
		query.setParameter( "sName", name );
		
		List<ApplicationSettings> queryResults = query.getResultList();
		return queryResults.isEmpty() ? null : queryResults.get( 0 );
	}
	
}
