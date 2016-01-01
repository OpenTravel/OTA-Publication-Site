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

import javax.persistence.EntityManager;

/**
 * Base class for all DAO's that provides a number of commom methods.
 * 
 * @author S. Livezey
 */
public abstract class AbstractDAO {
	
	protected static final int BUFFER_SIZE = 4096;
    
	protected static ContentCacheManager cacheManager = ContentCacheManager.getInstance();
	
	private DAOFactory factory;
	
	/**
	 * Constructor that supplies the factory which created this DAO instance.
	 * 
	 * @param factory  the factory that created this DAO
	 */
	AbstractDAO(DAOFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * Returns the factory that created this DAO instance.
	 * 
	 * @return DAOFactory
	 */
	protected DAOFactory getFactory() {
		return factory;
	}
	
	/**
	 * Returns the JPS entity manager associated with the factory that created this DAO.
	 * 
	 * @return EntityManager
	 */
	protected EntityManager getEntityManager() {
		return factory.getEntityManager();
	}
	
}
