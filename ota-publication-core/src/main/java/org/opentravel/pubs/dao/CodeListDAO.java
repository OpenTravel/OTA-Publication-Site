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

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.opentravel.pubs.model.CodeList;
import org.opentravel.pubs.model.FileContent;
import org.opentravel.pubs.validation.ModelValidator;
import org.opentravel.pubs.validation.ValidationException;
import org.opentravel.pubs.validation.ValidationResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO that provides operations related to the creation, retrieval, and management of
 * <code>CodeList</code> entities.
 *
 * @author S. Livezey
 */
public class CodeListDAO extends AbstractDAO {
	
	public static final DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
	
    private static final Logger log = LoggerFactory.getLogger( CodeListDAO.class );
    
	/**
	 * Constructor that supplies the factory which created this DAO instance.
	 * 
	 * @param factory  the factory that created this DAO
	 */
    CodeListDAO(DAOFactory factory) {
		super( factory );
	}
	
    /**
     * Returns the code list with the specified ID.
     * 
     * @param codeListId  the ID of the code list to retrieve
     * @return CodeList
	 * @throws DAOException  thrown if an error occurs while retrieving the code list
     */
    public CodeList getCodeList(long codeListId) throws DAOException {
		return getEntityManager().find( CodeList.class, codeListId );
    }
    
    /**
     * Returns the code list that was released on the specified date or null if
     * no code list was released on that date.
     * 
     * @param releaseDate  the release date of the code list
     * @return CodeList
	 * @throws DAOException  thrown if an error occurs while retrieving the code list
     */
    public CodeList getCodeList(Date releaseDate) throws DAOException {
		TypedQuery<CodeList> query = getEntityManager().createNamedQuery(
				"codeListFindByReleaseDate", CodeList.class );
		
		query.setParameter( "releaseDate", releaseDate );
		
		List<CodeList> queryResults = query.getResultList();
		return queryResults.isEmpty() ? null : queryResults.get( 0 );
    }
    
    /**
     * Returns the code list with the latest release date.
     * 
     * @return CodeList
	 * @throws DAOException  thrown if an error occurs while retrieving the code list
     */
    public CodeList getLatestCodeList() throws DAOException {
		TypedQuery<CodeList> query = getEntityManager().createNamedQuery(
				"codeListLatestByReleaseDate", CodeList.class );
		List<CodeList> queryResults = query.getResultList();
		
		return queryResults.isEmpty() ? null : queryResults.get( 0 );
    }
    
	/**
	 * Returns a list of all published code lists in order from most recent to oldest.
	 * 
	 * @return List<CodeList>
	 * @throws DAOException  thrown if an error occurs while retrieving the code lists
	 */
	public List<CodeList> getAllCodeLists() throws DAOException {
		TypedQuery<CodeList> query = getEntityManager().createNamedQuery(
				"codeListFindAll", CodeList.class );
		
		return query.getResultList();
	}
	
	/**
	 * Publishes a code list using the information provided.  In addition to creating the
	 * <code>CodeList</code> record itself, this method also creates the associated
	 * <code>FileContent</code> record.
	 * 
	 * <p>The return value of this method is the ID of the new code list that was created.
	 * 
	 * @param codeList  the codeList object to be published
	 * @param archiveContent  stream that provides access to the file content of the code list archive
	 * @return long
	 * @throws ValidationException  thrown if one or more validation errors were detected for the code list
	 * @throws DAOException  thrown if an error occurs while publishing the code list
	 */
	public long publishCodeList(CodeList codeList, InputStream archiveContent)
			throws ValidationException, DAOException {
		// Veryify that the code list can be published (error checking)
		if (codeList.getId() >= 0) {
			throw new DAOException("The code list has already been published - use 'updateCodeList()' instead.");
			
		} else {
			ValidationResults vResults = ModelValidator.validate( codeList );
			
			if (vResults.hasViolations()) {
				throw new ValidationException( vResults );
			}
			
			// Check for an existing publication with the same name/type
			if (getCodeList( codeList.getReleaseDate() ) != null) {
				throw new DAOException("A code list has already been published for the date "
						+ dateFormat.format( codeList.getReleaseDate() ) );
			}
		}
		return processCodeListArchive( codeList, archiveContent );
	}
	
	/**
	 * Updates the contents of the code list.
	 * 
	 * @param codeList  the code list to be updated
	 * @param archiveContent   stream that provides access to the updated file content of the code list archive
	 * @throws DAOException  thrown if an error occurs while updating the code list
	 * @throws ValidationException  thrown if one or more validation errors were detected for the code list
	 */
	public void updateCodeList(CodeList codeList, InputStream archiveContent)
			throws ValidationException, DAOException {
		if (codeList.getId() < 0) {
			throw new DAOException("Unable to update - the code list has not yet been saved to persistent storage.");
			
		} else {
			ValidationResults vResults = ModelValidator.validate( codeList );
			
			if (vResults.hasViolations()) {
				throw new ValidationException( vResults );
			}
		}
		processCodeListArchive( codeList, archiveContent );
	}
	
	/**
	 * Deletes the given code list from persistent storage.  Note that this will also perform
	 * a cascade delete of the file content for the code list archive.
	 * 
	 * @param codeList  the code list to be deleted from persistent storage
	 * @throws DAOException  thrown if an error occurs during the operation
	 */
	public void deleteCodeList(CodeList codeList) throws DAOException {
		EntityManager em = getEntityManager();
		Query codeListDeleteDownloads = em.createNamedQuery( "codeListDeleteDownloads" );
		
		codeListDeleteDownloads.setParameter( "codeListId", codeList.getId() );
		codeListDeleteDownloads.executeUpdate();
		
		getFactory().newDownloadDAO().purgeCache( codeList );
		em.remove( codeList );
	}
	
	/**
	 * Performs an initial load or update of the given code list archive depending
	 * upon its current state.
	 * 
	 * <p>The return value of this method is the ID of the code list that was created
	 * or updated.
	 * 
	 * @param publication  the publication object to be published as a specification
	 * @param archiveContent  stream that provides access to the file content of the specification archive
	 * @throws DAOException  thrown if an error occurs while publishing the specification
	 */
	private long processCodeListArchive(CodeList codeList, InputStream archiveContent) throws DAOException {
		try {
			EntityManager em = getEntityManager();
			String archiveFilename = getArchiveFilename( codeList );
			FileContent archiveFile = codeList.getArchiveContent();
			
			log.info("Saving File Content: " + archiveFilename);
			if (archiveFile != null) {
				persistFileContent( archiveFile, archiveContent );
			} else {
				archiveFile = persistFileContent( archiveContent );
				codeList.setArchiveContent( archiveFile );
			}
			codeList.setArchiveFilename( archiveFilename );
			
			if (codeList.getId() < 0) {
				em.persist( codeList );
			}
			
			em.flush();
			return codeList.getId();
			
		} catch (IOException e) {
			log.error("An error occurred while publishing the code list.", e);
			throw new DAOException("An error occurred while publishing the code list.", e);
		}
	}
	
	/**
	 * Returns a standardized archive filename for the given code list.
	 * 
	 * @param codeList  the code list for which to return a filename
	 * @return String
	 */
	private String getArchiveFilename(CodeList codeList) {
		return "CodeList_" + dateFormat.format( codeList.getReleaseDate() ) + ".zip";
	}
	
}
