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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.opentravel.pubs.model.FileContent;
import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationGroup;
import org.opentravel.pubs.model.PublicationItem;
import org.opentravel.pubs.model.PublicationItemType;
import org.opentravel.pubs.model.PublicationState;
import org.opentravel.pubs.model.PublicationType;
import org.opentravel.pubs.validation.ModelValidator;
import org.opentravel.pubs.validation.ValidationException;
import org.opentravel.pubs.validation.ValidationResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO that provides operations related to the creation, retrieval, and management of
 * <code>Publication</code> entities.
 * 
 * @author S. Livezey
 */
public class PublicationDAO extends AbstractDAO {
	
    private static final Logger log = LoggerFactory.getLogger( PublicationDAO.class );
    
	/**
	 * Constructor that supplies the factory which created this DAO instance.
	 * 
	 * @param factory  the factory that created this DAO
	 */
    PublicationDAO(DAOFactory factory) {
		super( factory );
	}
	
    /**
     * Returns the publication with the specified ID.
     * 
     * @param publicationId  the ID of the publication to retrieve
     * @return Publication
	 * @throws DAOException  thrown if an error occurs while retrieving the publication
     */
    public Publication getPublication(long publicationId) throws DAOException {
		return getEntityManager().find( Publication.class, publicationId );
    }
    
	/**
	 * Returns the publication with the the given name and type values.  If no such
	 * publication exists, this method will return null.
	 * 
	 * @param name  the name of the publication to retrieve
	 * @param type  the type of publication to retrieve
	 * @return Publication
	 * @throws DAOException  thrown if an error occurs while retrieving the publication
	 */
	public Publication getPublication(String name, PublicationType type) throws DAOException {
		TypedQuery<Publication> query = getEntityManager().createNamedQuery(
				"publicationFindByNameType", Publication.class );
		
		query.setParameter( "pType", type );
		query.setParameter( "pName", name );
		
		List<Publication> queryResults = query.getResultList();
		return queryResults.isEmpty() ? null : queryResults.get( 0 );
	}
	
	/**
	 * Returns the publication of the given type with the latest publication date value.
	 * 
	 * @param type  the type of publication to retrieve
	 * @param allowedStates  the list of allowed states for the latest publication that is returned
	 * @return Publication
	 * @throws DAOException  thrown if an error occurs while retrieving the publication
	 */
	public Publication getLatestPublication(PublicationType type, PublicationState... allowedStates)
			throws DAOException {
		TypedQuery<Publication> query;
		
		if ((allowedStates == null) || (allowedStates.length == 0)) {
			query = getEntityManager().createNamedQuery(
					"publicationLatestByType", Publication.class );
		} else {
			query = getEntityManager().createNamedQuery(
					"publicationLatestByTypeStateFilter", Publication.class );
			query.setParameter( "pStates", Arrays.asList( allowedStates ) );
		}
		query.setParameter( "pType", type );
		
		List<Publication> queryResults = query.getResultList();
		return queryResults.isEmpty() ? null : queryResults.get( 0 );
	}
	
	/**
	 * Returns a list of all published specifications of the indicated type.
	 * 
	 * @param type  the type of publications to retrieve
	 * @return List<Publication>
	 * @throws DAOException  thrown if an error occurs while retrieving the publications
	 */
	public List<Publication> getAllPublications(PublicationType type) throws DAOException {
		TypedQuery<Publication> query = getEntityManager().createNamedQuery(
				"publicationFindAll", Publication.class );
		
		query.setParameter( "pType", type );
		return query.getResultList();
	}
	
    /**
     * Returns the publication with the specified ID.
     * 
     * @param itemId  the ID of the publication item to retrieve
     * @return PublicationItem
	 * @throws DAOException  thrown if an error occurs while retrieving the publication item
     */
    public PublicationItem getPublicationItem(long itemId) throws DAOException {
		return getEntityManager().find( PublicationItem.class, itemId );
    }
    
	/**
	 * Returns the list of all publication items of the specified type that belong to the
	 * publication.
	 * 
	 * @param publication  the publication for which to return member items
	 * @param type  the type of items to retrieve for the publication
	 * @return List<PublicationItem>
	 * @throws DAOException  thrown if an error occurs while retrieving the publication items
	 */
	public List<PublicationItem> getPublicationItems(Publication publication, PublicationItemType type) throws DAOException {
		List<PublicationItem> itemList = new ArrayList<>();
		
		if (publication.getPublicationGroups() != null) {
			for (PublicationGroup group : publication.getPublicationGroups()) {
				if (group.getMemberType() != type) {
					continue;
				}
				for (PublicationItem item : group.getPublicationItems()) {
					if (item.getType() == type) {
						itemList.add( item );
					}
				}
			}
		}
		return itemList;
	}
	
	/**
	 * Returns the publication item with the given publication owner and the specified
	 * item filename.
	 * 
	 * @param publication  the owning publication of the item to return
	 * @param itemFilename  the filename of the publication item to return
	 * @return PublicationItem
	 * @throws DAOException  thrown if an error occurs while retrieving the publication item
	 */
	public PublicationItem findPublicationItem(Publication publication, String itemFilename) throws DAOException {
		TypedQuery<PublicationItem> query = getEntityManager().createNamedQuery(
				"publicationItemFindByFilename", PublicationItem.class );
		
		query.setParameter( "publicationId", publication.getId() );
		query.setParameter( "itemFilename", itemFilename );
		
		List<PublicationItem> queryResults = query.getResultList();
		return queryResults.isEmpty() ? null : queryResults.get( 0 );
	}
	
	/**
	 * Publishes a specification using the information provided.  In addition to creating the
	 * <code>Publication</code> record itself, this method also creates all of the associated
	 * <code>FileContent</code> and <code>PublicationItem</code> records.
	 * 
	 * <p>The return value of this method is the ID of the new specification that was created.
	 * 
	 * @param publication  the publication object to be published as a specification
	 * @param archiveContent  stream that provides access to the file content of the specification archive
	 * @return long
	 * @throws ValidationException  thrown if one or more validation errors were detected for the publication
	 * @throws DAOException  thrown if an error occurs while publishing the specification
	 */
	public long publishSpecification(Publication publication, InputStream archiveContent)
			throws ValidationException, DAOException {
		String pubName = publication.getName();
		PublicationType pubType = publication.getType();
		
		if (publication.getId() >= 0) {
			throw new DAOException("The publication has already been published - use 'updateSpecification()' instead.");
			
		} else {
			ValidationResults vResults = ModelValidator.validate( publication );
			
			if (vResults.hasViolations()) {
				throw new ValidationException( vResults );
			}
			
			// Check for an existing publication with the same name/type
			if (getPublication( pubName, pubType ) != null) {
				throw new DAOException("An " + pubType.getDisplayValue() +
						" publication with the name '" + pubName + "' already exists.");
			}
		}
		return processSpecificationArchive( publication, archiveContent );
	}
	
	/**
	 * Updates the contents of the specification.  Note that this will update the contents of any
	 * existing publication items, remove any deleted items, and add any newly discovered items.
	 * 
	 * @param publication  the publication to be updated
	 * @param archiveContent   stream that provides access to the updated file content of the specification archive
	 * @throws DAOException  thrown if an error occurs while updating the specification
	 * @throws ValidationException  thrown if one or more validation errors were detected for the publication
	 */
	public void updateSpecification(Publication publication, InputStream archiveContent)
			throws ValidationException, DAOException {
		if (publication.getId() < 0) {
			throw new DAOException("Unable to update - the publication has not yet been saved to persistent storage.");
			
		} else {
			ValidationResults vResults = ModelValidator.validate( publication );
			
			if (vResults.hasViolations()) {
				throw new ValidationException( vResults );
			}
		}
		processSpecificationArchive( publication, archiveContent );
		getFactory().newDownloadDAO().purgeCache( publication );
	}
	
	/**
	 * Performs an initial load or update of the given publication archive depending
	 * upon its current state.
	 * 
	 * <p>The return value of this method is the ID of the specification that was created
	 * or updated.
	 * 
	 * @param publication  the publication object to be published as a specification
	 * @param archiveContent  stream that provides access to the file content of the specification archive
	 * @return long
	 * @throws DAOException  thrown if an error occurs while publishing the specification
	 */
	private long processSpecificationArchive(Publication publication, InputStream archiveContent) throws DAOException {
		try {
			EntityManager em = getEntityManager();
			SpecificationCollator collator = new SpecificationCollator( publication );
			String archiveFilename = getArchiveFilename( publication );
			FileContent archiveFile = publication.getArchiveContent();
			
			log.info("Saving File Content: " + archiveFilename);
			if (archiveFile != null) {
				persistFileContent( archiveFile, archiveContent );
			} else {
				archiveFile = persistFileContent( archiveContent );
				publication.setArchiveContent( archiveFile );
			}
			publication.setArchiveFilename( archiveFilename );
			em.flush();
			
			// First Pass - Create the publication items for the new specification
			try (InputStream archiveStream = new InflaterInputStream( new ByteArrayInputStream( archiveFile.getFileBytes() ) )) {
				ZipInputStream zipStream = new ZipInputStream( archiveStream );
				ZipEntry zipEntry;
				
				while ((zipEntry = zipStream.getNextEntry()) != null) {
					if (zipEntry.isDirectory()) {
						continue;
						
					} else if (collator.isReleaseNotes( zipEntry )) {
						FileContent releaseNotesFile = publication.getReleaseNotesContent();
						
						if (releaseNotesFile != null) {
							persistFileContent( releaseNotesFile, zipStream );
						} else {
							publication.setReleaseNotesContent( persistFileContent( zipStream ) );
						}
						publication.setReleaseNotesFilename( collator.getFilename( zipEntry ) );
						
					} else {
						PublicationItem item = collator.addItem( zipEntry );
						
						if (item != null) {
							log.info("Saving File Content: " + item.getItemFilename());
							FileContent itemFile = item.getItemContent();
							
							if (itemFile != null) {
								persistFileContent( itemFile, zipStream );
							} else {
								item.setItemContent( persistFileContent( zipStream ) );
							}
						}
					}
				}
				
			} catch (IOException e) {
				log.error("Error reading from publication archive.", e);
				throw new DAOException("Error reading from publication archive.", e);
			}
			
			// Second Pass - Create publication items for any nested archives that fall under
			//   the "special case" of a standalone nested archive that is the only member of
			//   its publication group.
			try (InputStream archiveStream = new InflaterInputStream( new ByteArrayInputStream( archiveFile.getFileBytes() ) )) {
				ZipInputStream zipStream = new ZipInputStream( archiveStream );
				ZipEntry zipEntry;
				
				while ((zipEntry = zipStream.getNextEntry()) != null) {
					if (zipEntry.isDirectory()) {
						continue;
						
					} else if (collator.isSpecialCaseArchive( zipEntry )) {
						ZipInputStream nestedZipStream = new ZipInputStream( zipStream );
						ZipEntry nestedEntry;
						
						while ((nestedEntry = nestedZipStream.getNextEntry()) != null) {
							if (nestedEntry.isDirectory()) {
								continue;
								
							} else {
								PublicationItem item = collator.addArchiveItem( zipEntry, nestedEntry );
								
								if (item != null) {
									log.info("Saving File Content: " + item.getItemFilename());
									item.setItemContent( persistFileContent( nestedZipStream ) );
								}
							}
						}
					}
				}
				
			} catch (IOException e) {
				log.error("Error reading from publication archive.", e);
				throw new DAOException("Error reading from publication archive.", e);
			}
			
			// Finish by saving all of the non-file persistent records for the specification
			// and updating any items that have been removed
			if (publication.getId() < 0) {
				em.persist( publication );
			}
			
			for (PublicationItem deletedItem : collator.getDeletedItems()) {
				deletedItem.setRemoved( true );
			}
			
			for (PublicationGroup group : collator.getGroups()) {
				boolean emptyGroup = true;
				
				if (group.getId() < 0) {
					em.persist( group );
				}
				
				for (PublicationItem item : group.getPublicationItems()) {
					if (item.getId() < 0) {
						em.persist( item );
					}
					emptyGroup &= item.isRemoved();
				}
				group.setRemoved( emptyGroup );
			}
			em.flush();
			DAOFactory.invalidateCollectionCache();
			
			return publication.getId();
			
		} catch (IOException e) {
			log.error("An error occurred while publishing the specification.", e);
			throw new DAOException("An error occurred while publishing the specification.", e);
		}
	}
	
	/**
	 * Deletes the given specification from persistent storage.  Note that this will also perform
	 * a cascade delete of all groups, items, file content, and comments submitted for the specification.
	 * 
	 * @param publication  the specification to be deleted from persistent storage
	 * @throws DAOException  thrown if an error occurs during the operation
	 */
	public void deleteSpecification(Publication publication) throws DAOException {
		EntityManager em = getEntityManager();
		Query publicationDeleteDownloads = em.createNamedQuery( "publicationDeleteDownloads" );
		Query publicationItemDeleteDownloads = em.createNamedQuery( "publicationItemDeleteDownloads" );
		List<Long> itemIds = new ArrayList<>();
		
		for (PublicationGroup group : publication.getPublicationGroups()) {
			for (PublicationItem item : group.getPublicationItems()) {
				itemIds.add( item.getId() );
			}
		}
		
		publicationDeleteDownloads.setParameter( "publicationId", publication.getId() );
		publicationItemDeleteDownloads.setParameter( "itemIds", itemIds );
		publicationDeleteDownloads.executeUpdate();
		publicationItemDeleteDownloads.executeUpdate();
		
		getFactory().newDownloadDAO().purgeCache( publication );
		em.remove( publication );
	}
	
	/**
	 * Returns a standardized archive filename for the given publication.
	 * 
	 * @param publication  the publication for which to return a filename
	 * @return String
	 */
	private String getArchiveFilename(Publication publication) {
		return publication.getName() + "_" +
				publication.getType().getDisplayId().charAt( 0 ) +
				"0_Publication.zip";
	}
	
}
