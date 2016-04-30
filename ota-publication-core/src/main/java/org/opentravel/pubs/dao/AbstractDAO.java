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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.ZipInputStream;

import javax.persistence.EntityManager;

import org.opentravel.pubs.model.FileContent;

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
	
	/**
	 * Creates and persists a new <code>FileContent</code> object.  This method
	 * operates within the scope of the current entity manager's transaction and
	 * does not commit or rollback after the file content has been persisted.
	 * 
	 * @param contentStream  the input stream from which the file content should be obtained
	 * @return FileContent
	 * @throws IOException  thrown if an error occurs while attempting to read from the given input stream
	 */
	protected FileContent persistFileContent(InputStream contentStream) throws IOException {
		return persistFileContent( null, contentStream );
	}
	
	/**
	 * Creates or updates a <code>FileContent</code> object with the data provided from the
	 * given input stream.  This method operates within the scope of the current entity manager's
	 * transaction and does not commit or rollback after the file content has been persisted.
	 * 
	 * @param existingContent  the existing content record (if null, a new one will be created and persisted)
	 * @param contentStream  the input stream from which the file content should be obtained
	 * @return FileContent
	 * @throws IOException  thrown if an error occurs while attempting to read from the given input stream
	 */
	protected FileContent persistFileContent(FileContent existingContent, InputStream contentStream) throws IOException {
		try {
			ByteArrayOutputStream contentBytes = new ByteArrayOutputStream();
			FileContent fc;
			
			// Compress the content before storing it in the database BLOB
			try (DeflaterOutputStream zipOut = new DeflaterOutputStream( contentBytes )) {
				byte[] buffer = new byte[ BUFFER_SIZE ];
				int bytesRead;
				
				while ((bytesRead = contentStream.read( buffer, 0, BUFFER_SIZE )) >= 0) {
					zipOut.write( buffer, 0, bytesRead );
				}
				zipOut.flush();
			}
			
			// Create and persist the file entity
			fc = (existingContent == null) ? new FileContent() : existingContent;
			fc.setFileBytes( contentBytes.toByteArray() );
			
			if (existingContent == null) {
				getEntityManager().persist( fc );
			}
			return fc;
			
		} finally {
			// Close the input stream unless it is a zip stream since we may be
			// reading subsequent file content entries from the same stream.
			if (!(contentStream instanceof ZipInputStream)) {
				try {
					contentStream.close();
				} catch (IOException e) {}
			}
		}
	}
	
}
