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
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory used to contstruct DAO instances.  The factory maintains a common <code>EntityManager</code>
 * and transaction context across all DAO instances that are created.
 * 
 * @author S. Livezey
 */
public class DAOFactory {
	
	public static final String JPA_PERSISTENCE_UNIT = "org.opentravel.pubs";
	
    private static final Logger log = LoggerFactory.getLogger( DAOFactory.class );
	
	private static EntityManagerFactory emFactory;
	
	private EntityManager entityManager;
	
	/**
	 * Returns a new <code>PublicationDAO</code> instance.
	 * 
	 * @return PublicationDAO
	 */
	public PublicationDAO newPublicationDAO() {
		return new PublicationDAO( this );
	}
	
	/**
	 * Returns a new <code>RegistrantDAO</code> instance.
	 * 
	 * @return RegistrantDAO
	 */
	public RegistrantDAO newRegistrantDAO() {
		return new RegistrantDAO( this );
	}
	
	/**
	 * Returns a new <code>CommentDAO</code> instance.
	 * 
	 * @return CommentDAO
	 */
	public CommentDAO newCommentDAO() {
		return new CommentDAO( this );
	}
	
	/**
	 * Returns a new <code>DownloadDAO</code> instance.
	 * 
	 * @return DownloadDAO
	 */
	public DownloadDAO newDownloadDAO() {
		return new DownloadDAO( this );
	}
	
	/**
	 * Returns a new <code>AdminDAO</code> instance.
	 * 
	 * @return AdminDAO
	 */
	public AdminDAO newAdminDAO() {
		return new AdminDAO( this );
	}
	
	/**
	 * Shuts down the underlying <code>EntityManagerFactory</code> and releases all associated
	 * system resources.  This method should only be called upon application shutdown.
	 */
	public static void shutdownJPAServices() {
		emFactory.close();
	}
	
	/**
	 * Initializes the underlying <code>EntityManagerFactory</code>.  This method is normally
	 * called automatically during class initialization, so it is not typically needed.  It
	 * is, however, useful for re-initializing the entity manager factory after a call to
	 * 'finalShutdown()'.
	 */
	public static void initializeJPAServices() {
		if ((emFactory == null) || !emFactory.isOpen()) {
			emFactory = Persistence.createEntityManagerFactory( JPA_PERSISTENCE_UNIT );
		}
	}
	
	/**
	 * Returns an initialized entity manager for this factory.  If an entity manager
	 * is not yet active, one is created automatically.
	 * 
	 * @return EntityManager
	 */
	protected synchronized EntityManager getEntityManager() {
		if (entityManager == null) {
			entityManager = emFactory.createEntityManager();
		}
		return entityManager;
	}
	
	/**
	 * Returns a standalone entity manager instance whose lifecycle is not managed by this
	 * factory.  It is the caller's responsibility to dispose of the entity manager properly.
	 * 
	 * @return EntityManager
	 */
	protected EntityManager getStandaloneEntityManager() {
		return emFactory.createEntityManager();
	}
	
	/**
	 * Begins a transaction within the context of the current entity manager.  If
	 * an entity manager has not yet been created, one will be constructed automatically.
	 * 
	 * @throws IllegalStateException  thrown if a transaction is already active
	 */
	public synchronized void beginTransaction() {
		getEntityManager().getTransaction().begin();
	}
	
	/**
	 * Commits the active transaction of the current entity manager.
	 * 
	 * @throws IllegalStateException  thrown if a transaction is not currently active
	 * @throws DAOException  thrown if an error occurs while committing the transaction
	 */
	public synchronized void commitTransaction() throws DAOException {
		EntityManager em = getEntityManager();
		try {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
				
			} else {
				log.warn("Transaction not active - ignoring call to commit.");
			}
			
		} catch (RollbackException e) {
			log.error("An error occurred while committing the transaction.", e);
			throw new DAOException("An error occurred while committing the transaction.", e);
			
		} finally {
			em.close();
			entityManager = null;
		}
	}
	
	/**
	 * Rolls back the active transaction of the current entity manager.
	 * 
	 * @throws IllegalStateException  thrown if a transaction is not currently active
	 * @throws DAOException  thrown if an error occurs while rolling back the transaction
	 */
	public synchronized void rollbackTransaction() throws DAOException {
		EntityManager em = getEntityManager();
		try {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
				
			} else {
				log.warn("Transaction not active - ignoring call to rollback.");
			}
			
		} catch (PersistenceException e) {
			log.error("An error occurred while rolling back the transaction.", e);
			throw new DAOException("An error occurred while rolling back the transaction.", e);
			
		} finally {
			em.close();
			entityManager = null;
		}
	}
	
	/**
	 * Initializes the underlying <code>EntityManagerFactory</code>.
	 */
	static {
		try {
			initializeJPAServices();
			
		} catch (Throwable t) {
			throw new ExceptionInInitializerError( t );
		}
	}
	
}
