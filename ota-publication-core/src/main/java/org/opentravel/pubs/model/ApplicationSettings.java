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
package org.opentravel.pubs.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Model object representing a set of application configuration settings that can be stored
 * and retrieved via persistent storage.
 * 
 * @author S. Livezey
 */
@NamedQueries({
	@NamedQuery(
		name  = "settingsFindByName",
		query = "SELECT s FROM ApplicationSettings s WHERE s.name = :sName" ),
})
@Entity
@Table( name = "application_settings" )
@Cache( usage = CacheConcurrencyStrategy.READ_WRITE, region="daoCache" )
public class ApplicationSettings implements Serializable {

    private static final Logger log = LoggerFactory.getLogger( ApplicationSettings.class );
	private static final long serialVersionUID = 6911867110788689571L;
	
	@Id
	@Column( name = "id", nullable = false )
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id = -1L;
	
	@NotNull
	@Size( min = 1, max = 50 )
	@Column( name = "name", nullable = false, length = 50 )
	private String name;
	
	
	@Column( name = "settings_bytes", nullable = false )
	private byte[] settingsBytes;
	
	private transient Properties settingsProps;
	
	/**
	 * Returns the value of the 'id' field.
	 *
	 * @return long
	 */
	public long getId() {
		return id;
	}

	/**
	 * Assigns the value of the 'id' field.
	 *
	 * @param id  the field value to assign
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Returns the value of the 'name' field.
	 *
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * Assigns the value of the 'name' field.
	 *
	 * @param name  the field value to assign
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value of the 'settingsBytes' field.
	 *
	 * @return byte[]
	 */
	public synchronized byte[] getSettingsBytes() {
		return settingsBytes;
	}

	/**
	 * Assigns the value of the 'settingsBytes' field.
	 *
	 * @param settingsBytes  the field value to assign
	 */
	public synchronized void setSettingsBytes(byte[] settingsBytes) {
		this.settingsBytes = settingsBytes;
		this.settingsProps = null;
	}
	
	/**
	 * Returns the application configuration settings as a <code>Properties</code> instance.
	 * 
	 * @return Properties
	 */
	public synchronized Properties getProperties() {
		if (settingsProps == null) {
			settingsProps = new Properties();
			
			if (settingsBytes != null) {
				try (InputStream is = new ByteArrayInputStream( settingsBytes ) ) {
					settingsProps.load( is );
					
				} catch (IOException e) {
					log.warn("Error reading application settings: " + name);
				}
			}
		}
		return settingsProps;
	}
	
	/**
	 * Assigns the application configuration settings for this set.
	 * 
	 * @param props  the name/value pairs of the application configuration settings
	 */
	public synchronized void setProperties(Properties props) {
		try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
			if (props == null) props = new Properties();
			props.store( out, null );
			settingsBytes = out.toByteArray();
			this.settingsProps = props;
			
		} catch (IOException e) {
			log.warn("Error writing application settings: " + name);
		}
	}
	
}
