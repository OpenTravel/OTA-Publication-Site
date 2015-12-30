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
import java.util.List;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.opentravel.pubs.model.FileContent;
import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationGroup;
import org.opentravel.pubs.model.PublicationItem;
import org.opentravel.pubs.model.PublicationItemType;
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
    
    private static final String QUERY_BY_NAME_TYPE          = "SELECT p FROM Publication p WHERE p.type = :pType AND p.name = :pName";
    private static final String QUERY_LATEST_BY_TYPE        = "SELECT p FROM Publication p WHERE p.type = :pType AND p.publicationDate = (SELECT MAX(p2.publicationDate) FROM Publication p2 WHERE p2.type = :pType)";
    private static final String QUERY_ALL_PUBLICATIONS      = "SELECT p FROM Publication p WHERE p.type = :pType ORDER BY p.publicationDate DESC";
    private static final String QUERY_DELETE_PUB_DOWNLOADS  = "DELETE FROM PUBLICATION_DOWNLOAD WHERE PUBLICATION_ID = :publicationId";
    private static final String QUERY_DELETE_ITEM_DOWNLOADS = "DELETE FROM PUBLICATION_ITEM_DOWNLOAD WHERE PUBLICATION_ITEM_ID IN :itemIds";
    private static final String QUERY_ITEM_FIND_BY_FILENAME =
    		"SELECT i FROM PublicationItem i, PublicationGroup g, Publication p "
    		+ "WHERE i.itemFilename = :itemFilename AND p.id = :publicationId AND g.owner = p.id AND i.owner = g.id";
    
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
	@SuppressWarnings("unchecked")
	public Publication getPublication(String name, PublicationType type) throws DAOException {
		Query query = getEntityManager().createQuery( QUERY_BY_NAME_TYPE );
		
		query.setParameter( "pType", type );
		query.setParameter( "pName", name );
		
		List<Publication> queryResults = (List<Publication>) query.getResultList();
		return queryResults.isEmpty() ? null : queryResults.get( 0 );
	}
	
	/**
	 * Returns the publication of the given type with the latest publication date value.
	 * 
	 * @param type  the type of publication to retrieve
	 * @return Publication
	 * @throws DAOException  thrown if an error occurs while retrieving the publication
	 */
	@SuppressWarnings("unchecked")
	public Publication getLatestPublication(PublicationType type) throws DAOException {
		Query query = getEntityManager().createQuery( QUERY_LATEST_BY_TYPE );
		
		query.setParameter( "pType", type );
		
		List<Publication> queryResults = (List<Publication>) query.getResultList();
		return queryResults.isEmpty() ? null : queryResults.get( 0 );
	}
	
	/**
	 * Returns a list of all published specifications of the indicated type.
	 * 
	 * @param type  the type of publications to retrieve
	 * @return List<Publication>
	 * @throws DAOException  thrown if an error occurs while retrieving the publications
	 */
	@SuppressWarnings("unchecked")
	public List<Publication> getAllPublications(PublicationType type) throws DAOException {
		Query query = getEntityManager().createQuery( QUERY_ALL_PUBLICATIONS );
		
		query.setParameter( "pType", type );
		return (List<Publication>) query.getResultList();
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
	@SuppressWarnings("unchecked")
	public PublicationItem findPublicationItem(Publication publication, String itemFilename) throws DAOException {
		Query query = getEntityManager().createQuery( QUERY_ITEM_FIND_BY_FILENAME );
		
		query.setParameter( "publicationId", publication.getId() );
		query.setParameter( "itemFilename", itemFilename );
		
		List<PublicationItem> queryResults = (List<PublicationItem>) query.getResultList();
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
		try {
			String pubName = publication.getName();
			PublicationType pubType = publication.getType();
			
			if ((pubName == null) || (pubType == null)) {
				throw new DAOException("The publication name and type are required.");
				
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
			
			// Save the archive file content
			String archiveFilename = getArchiveFilename( publication );
			log.info("Saving File Content: " + archiveFilename);
			EntityManager em = getEntityManager();
			SpecificationCollator collator = new SpecificationCollator( publication );
			FileContent archiveFile = persistFileContent( archiveContent );
			
			publication.setArchiveFilename( archiveFilename );
			publication.setArchiveContent( archiveFile );
			em.flush();
			
			// First Pass - Create the publication items for the new specification
			try (InputStream archiveStream = new InflaterInputStream( new ByteArrayInputStream( archiveFile.getFileBytes() ) )) {
				ZipInputStream zipStream = new ZipInputStream( archiveStream );
				ZipEntry zipEntry;
				
				while ((zipEntry = zipStream.getNextEntry()) != null) {
					if (zipEntry.isDirectory()) {
						continue;
						
					} else if (collator.isReleaseNotes( zipEntry )) {
						FileContent releaseNotesFile = persistFileContent( zipStream );
						
						publication.setReleaseNotesFilename( collator.getFilename( zipEntry ) );
						publication.setReleaseNotesContent( releaseNotesFile );
						
					} else {
						PublicationItem item = collator.addItem( zipEntry );
						
						if (item != null) {
							log.info("Saving File Content: " + item.getItemFilename());
							item.setItemContent( persistFileContent( zipStream ) );
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
			log.info("Saving Publication: " + publication.getName());
			em.persist( publication );
			
			for (PublicationGroup group : collator.getGroups()) {
				log.info("Saving Group: " + group.getName());
				em.persist( group );
				
				for (PublicationItem item : group.getPublicationItems()) {
					log.info("Saving Item: " + item.getItemFilename());
					em.persist( item );
				}
			}
			em.flush();
			return publication.getId();
			
		} catch (IOException e) {
			log.error("An error occurred while publishing the specification.", e);
			throw new DAOException("An error occurred while publishing the specification.", e);
		}
	}
	
	/**
	 * Updates the contents of the specification.  Note that this will update the contents of any
	 * existing publication items, remove any deleted items, and add any newly discovered items.
	 * 
	 * @param publication  the publication to be updated
	 * @param archiveContent   stream that provides access to the updated file content of the specification archive
	 * @throws DAOException  thrown if an error occurs while updating the specification
	 */
	public void updateSpecification(Publication publication, InputStream archiveContent) throws DAOException {
		// TODO: Implement the 'updateSpecification()' operation
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
		Query deletePublicationDownloads = em.createNativeQuery( QUERY_DELETE_PUB_DOWNLOADS );
		Query deleteItemDownloads = em.createNativeQuery( QUERY_DELETE_ITEM_DOWNLOADS );
		List<Long> itemIds = new ArrayList<>();
		
		for (PublicationGroup group : publication.getPublicationGroups()) {
			for (PublicationItem item : group.getPublicationItems()) {
				itemIds.add( item.getId() );
			}
		}
		
		deletePublicationDownloads.setParameter( "publicationId", publication.getId() );
		deleteItemDownloads.setParameter( "itemIds", itemIds );
		deletePublicationDownloads.executeUpdate();
		deleteItemDownloads.executeUpdate();
		
		getFactory().newDownloadDAO().purgeCache( publication );
		getEntityManager().remove( publication );
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
