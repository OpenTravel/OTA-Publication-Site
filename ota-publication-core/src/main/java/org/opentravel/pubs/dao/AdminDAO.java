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

import java.security.MessageDigest;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.opentravel.pubs.model.AdminCredentials;
import org.opentravel.pubs.util.StringUtils;
import org.opentravel.pubs.validation.ModelValidator;
import org.opentravel.pubs.validation.ValidationException;
import org.opentravel.pubs.validation.ValidationResults;

/**
 * DAO that provides access to administrative functions for the application.
 * 
 * @author S. Livezey
 */
public class AdminDAO extends AbstractDAO {
	
	private static final String QUERY_CREDENTIALS        = "SELECT username FROM users";
    private static final String QUERY_UPDATE_CREDENTIALS = "UPDATE users SET username = :userId, password = :password";
    private static final String QUERY_ADD_CREDENTIALS    = "INSERT INTO users VALUES (:userId, :password)";
	
	/**
	 * Constructor that supplies the factory which created this DAO instance.
	 * 
	 * @param factory  the factory that created this DAO
	 */
	AdminDAO(DAOFactory factory) {
		super( factory );
	}
	
	/**
	 * Returns the administrator login credentials.  The password field is omitted from
	 * the credentials object returned by this method.
	 * 
	 * @return AdminCredentials
	 * @throws DAOException  thrown if the credentials cannot be retrieved
	 */
	public AdminCredentials getAdminCredentials() throws DAOException {
		AdminCredentials credentials = new AdminCredentials();
		try {
			EntityManager em = getEntityManager();
			Query query = em.createNativeQuery( QUERY_CREDENTIALS );
			String userId = (String) query.getSingleResult();
			
			credentials.setUserId( userId );
			
		} catch (NoResultException e) {
			// No error - leave admin login blank
		}
		return credentials;
	}
	
	/**
	 * Saves the updated administrator credentials to persistent storage.
	 * 
	 * @param credentials  the credentials to update
	 * @throws ValidationException  thrown if errors are detected in the updated credentials
	 * @throws DAOException  thrown if an error occurrs during the update
	 */
	public void updateAdminCredentials(AdminCredentials credentials) throws ValidationException, DAOException {
		ValidationResults vResults = ModelValidator.validate( credentials );
		
		if (vResults.hasViolations()) {
			throw new ValidationException( vResults );
		}
		String digestedPassword = digestPassword( credentials.getPassword(), "SHA1", "UTF-8" );
		EntityManager em = getEntityManager();
		Query query = em.createNativeQuery( QUERY_UPDATE_CREDENTIALS );
		
		query.setParameter( "userId", credentials.getUserId() );
		query.setParameter( "password", digestedPassword );
		int rowsUpdated = query.executeUpdate();
		
		if (rowsUpdated == 0) {
			query = em.createNativeQuery( QUERY_ADD_CREDENTIALS );
			
			query.setParameter( "userId", credentials.getUserId() );
			query.setParameter( "password", credentials.getPassword() );
			query.executeUpdate();
		}
	}
	
    /**
     * Digest password using the algorithm specified and
     * convert the result to a corresponding hex string.
     * If exception, the plain credentials string is returned
     *
     * @param credentials  password or other credentials to use in authenticating this username
     * @param algorithm  algorithm used to do the digest
     * @param encoding  character encoding of the string to digest
     */
	private String digestPassword(String credentials, String algorithm, String encoding) {
		try {
			MessageDigest md = (MessageDigest) MessageDigest.getInstance(algorithm).clone();
			
			if (encoding == null) {
				md.update(credentials.getBytes());
			} else {
				md.update(credentials.getBytes(encoding));
			}
			
			return (StringUtils.toHexString(md.digest()));
			
		} catch (Exception ex) {
			return credentials;
		}
		
	}

}
