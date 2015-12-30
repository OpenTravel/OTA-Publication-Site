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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the lifecycle of a thread local <code>DAOFactory</code> instance.
 * 
 * @author S. Livezey
 */
public class DAOFactoryManager {
	
    private static final Logger log = LoggerFactory.getLogger( DAOFactoryManager.class );
    
	private static ThreadLocal<DAOFactory> factoryInstance = new ThreadLocal<>();
	private static ThreadLocal<Boolean> markedForRollback = new ThreadLocal<Boolean>() {
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}
	};
	
	/**
	 * Private constructor.
	 */
	private DAOFactoryManager() {}
	
	/**
	 * Initializes a thread local factory instance if one does not already exist.
	 */
	public static void initFactory() {
		if (factoryInstance.get() == null) {
			DAOFactory factory = new DAOFactory();
			
			factory.beginTransaction();
			factoryInstance.set( factory );
			markedForRollback.set( Boolean.FALSE );
		}
	}
	
	/**
	 * Commits or rolls back the transaction before closing the factory instance
	 * associated with the current thread.
	 * 
	 * @throws IllegalStateException  thrown if no factory instance has been initialized for the current thread
	 */
	public static void closeFactory() {
		try {
			DAOFactory factory = factoryInstance.get();
			
			if (factory == null) {
				throw new IllegalStateException(
						"No factory instance has been initialized for the current thread.");
			}
			
			if (!markedForRollback.get()) {
				factory.commitTransaction();
				
			} else {
				factory.rollbackTransaction();
			}
			
		} catch (Throwable t) {
			log.error( "Error during transaction commit or rollback.", t );
			
		} finally {
			factoryInstance.set( null );
			markedForRollback.set( Boolean.FALSE );
		}
	}
	
	/**
	 * Returns the factory instance or null if the factory has not yet
	 * been initialized.
	 * 
	 * @return DAOFactory
	 */
	public static DAOFactory getFactory() {
		return factoryInstance.get();
	}
	
	/**
	 * Returns true if the current thread has been marked for rollback upon factory
	 * close.
	 * 
	 * @return boolean
	 */
	public static boolean isMarkedForRollback() {
		return markedForRollback.get();
	}
	
	/**
	 * Marks the current thread for rollback upon factory close.
	 */
	public static void markForRollback() {
		markedForRollback.set( Boolean.TRUE );
	}
	
}
