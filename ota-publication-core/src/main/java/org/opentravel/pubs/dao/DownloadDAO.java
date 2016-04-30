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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.opentravel.pubs.model.CodeList;
import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationItem;
import org.opentravel.pubs.model.Registrant;

/**
 * DAO that provides download access to the contents of publication archives, release notes,
 * and publication items.
 *
 * @author S. Livezey
 */
public class DownloadDAO extends AbstractDAO {
	
	private static ContentCacheManager cacheManager = ContentCacheManager.getInstance();
	
	/**
	 * Constructor that supplies the factory which created this DAO instance.
	 * 
	 * @param factory  the factory that created this DAO
	 */
	DownloadDAO(DAOFactory factory) {
		super( factory );
	}
	
	/**
	 * Returns an input stream for the contents of the archive file for the given
	 * publication.  This method will also log a download record for the publication
	 * to the specified registrant.
	 * 
	 * @param publication  the publication whose archive is to be downloaded
	 * @param registrant  the registrant who is downloading the publication archive
	 * @return InputStream
	 * @throws DAOException  thrown if a stream to the archive content cannot obtained for any reason
	 */
	public InputStream getArchiveContent(Publication publication, Registrant registrant) throws DAOException {
		InputStream contentStream = cacheManager.getArchiveContent( publication );
		
		if (contentStream != null) {
			long registrantId = registrant.getId();
			boolean alreadyDownloaded = false;
			
			getEntityManager().flush();
			getEntityManager().refresh( publication );
			
			for (Registrant r : publication.getDownloadedBy()) {
				if (r.getId() == registrantId) {
					alreadyDownloaded = true;
					break;
				}
			}
			if (!alreadyDownloaded) {
				registrant.getDownloadedPublications().add( publication );
				DAOFactory.invalidateCollectionCache( Publication.class, "downloadedBy", publication.getId() );
			}
		}
		return contentStream;
	}
	
	/**
	 * Returns an input stream for the release notes of the given publication. 
	 * 
	 * @param publication  the publication whose release notes are to be downloaded
	 * @return InputStream
	 * @throws DAOException  thrown if a stream to the release notes content cannot obtained for any reason
	 */
	public InputStream getReleaseNotesContent(Publication publication) throws DAOException {
		return cacheManager.getReleaseNotesContent( publication );
	}
	
	/**
	 * Returns an input stream for the contents the given publication item.  This method
	 * will also log a download record for the publication item to the specified registrant.
	 * 
	 * @param item  the publication whose archive is to be downloaded
	 * @param registrant  the registrant who is downloading the publication archive
	 * @return InputStream
	 * @throws DAOException  thrown if a stream to the item's cannot obtained for any reason
	 */
	public InputStream getContent(PublicationItem item, Registrant registrant) throws DAOException {
		InputStream contentStream = cacheManager.getContent( item );
		
		if ((registrant != null) && (contentStream != null)) {
			long registrantId = registrant.getId();
			boolean alreadyDownloaded = false;
			
			getEntityManager().flush();
			getEntityManager().refresh( item );
			
			for (Registrant r : item.getDownloadedBy()) {
				if (r.getId() == registrantId) {
					alreadyDownloaded = true;
					break;
				}
			}
			if (!alreadyDownloaded) {
				registrant.getDownloadedPublicationItems().add( item );
				DAOFactory.invalidateCollectionCache( PublicationItem.class, "downloadedBy", item.getId() );
			}
		}
		return contentStream;
	}
	
	/**
	 * Returns an input stream for the contents of the archive file for the given
	 * code list.  This method will also log a download record for the code list
	 * to the specified registrant.
	 * 
	 * @param codeList  the code list whose archive is to be downloaded
	 * @param registrant  the registrant who is downloading the code list archive
	 * @return InputStream
	 * @throws DAOException  thrown if a stream to the archive content cannot obtained for any reason
	 */
	public InputStream getArchiveContent(CodeList codeList, Registrant registrant) throws DAOException {
		InputStream contentStream = cacheManager.getArchiveContent( codeList );
		
		if (contentStream != null) {
			long registrantId = registrant.getId();
			boolean alreadyDownloaded = false;
			
			getEntityManager().flush();
			getEntityManager().refresh( codeList );
			
			for (Registrant r : codeList.getDownloadedBy()) {
				if (r.getId() == registrantId) {
					alreadyDownloaded = true;
					break;
				}
			}
			if (!alreadyDownloaded) {
				registrant.getDownloadedCodeLists().add( codeList );
				DAOFactory.invalidateCollectionCache( CodeList.class, "downloadedBy", codeList.getId() );
			}
		}
		return contentStream;
	}
	
	/**
	 * Returns the download history for the given publication and all of its constituent
	 * publication items.
	 * 
	 * @param publication  the publication for which to return the download history
	 * @param dateRange  the date range for comment submission relative to the current date
	 * @return List<DownloadHistoryItem>
	 */
	public List<DownloadHistoryItem> getDownloadHistory(Publication publication, DateRangeType dateRange) {
		List<DownloadHistoryItem> downloadHistory = new ArrayList<>();
		TypedQuery<Registrant> pubQuery;
		TypedQuery<PublicationItem> itemQuery;
		
		// Start by finding the downloaders for the publication archive
		if ((dateRange == null) || (dateRange == DateRangeType.ALL)) {
			pubQuery = getEntityManager().createNamedQuery( "registrantFindAllPublicationDownloaders", Registrant.class );
		} else {
			pubQuery = getEntityManager().createNamedQuery( "registrantFindAllPublicationDownloadersByDateRange", Registrant.class );
			pubQuery.setParameter( "rDate", dateRange.getRangeStart() );
		}
		pubQuery.setParameter( "publicationId", publication.getId() );
		List<Registrant> pubDownloaders = pubQuery.getResultList();
		
		if (!pubDownloaders.isEmpty()) {
			DownloadHistoryItem downloadItem = new DownloadHistoryItem( publication );
			
			downloadItem.setDownloadedBy( pubDownloaders );
			downloadHistory.add( downloadItem );
		}
		
		// Next, find the list of all constituent publication items that have downloads
		if ((dateRange == null) || (dateRange == DateRangeType.ALL)) {
			itemQuery = getEntityManager().createNamedQuery( "publicationItemFindAllDownloaded", PublicationItem.class );
		} else {
			itemQuery = getEntityManager().createNamedQuery( "publicationItemFindAllDownloadedByDateRange", PublicationItem.class );
			itemQuery.setParameter( "rDate", dateRange.getRangeStart() );
		}
		itemQuery.setParameter( "publicationId", publication.getId() );
		List<PublicationItem> downloadedItems = itemQuery.getResultList();
		
		for (PublicationItem pubItem : downloadedItems) {
			DownloadHistoryItem downloadItem = new DownloadHistoryItem( pubItem );
			
			downloadItem.setDownloadedBy( pubItem.getDownloadedBy() );
			downloadHistory.add( downloadItem );
		}
		return downloadHistory;
	}
	
	/**
	 * Returns the download history for the given code list.
	 * 
	 * @param codeList  the code list for which to return the download history
	 * @param dateRange  the date range for comment submission relative to the current date
	 * @return List<Registrant>
	 */
	public List<Registrant> getDownloaders(CodeList codeList, DateRangeType dateRange) {
		TypedQuery<Registrant> query;
		
		// Start by finding the downloaders for the publication archive
		if ((dateRange == null) || (dateRange == DateRangeType.ALL)) {
			query = getEntityManager().createNamedQuery( "registrantFindAllCodeListDownloaders", Registrant.class );
		} else {
			query = getEntityManager().createNamedQuery( "registrantFindAllCodeListDownloadersByDateRange", Registrant.class );
			query.setParameter( "rDate", dateRange.getRangeStart() );
		}
		query.setParameter( "codeListId", codeList.getId() );
		return query.getResultList();
	}
	
	/**
	 * Deletes all locally-cached files that are associated with the given publication.
	 * 
	 * @param publication  the publication for which to purge cached content
	 * @throws DAOException  thrown if the purge operation cannot be completed for any reason
	 */
	public void purgeCache(Publication publication) throws DAOException {
		cacheManager.purgeCache( publication );
	}
	
	/**
	 * Deletes all locally-cached files that are associated with the given code list.
	 * 
	 * @param codeList  the code list for which to purge cached content
	 * @throws DAOException  thrown if the purge operation cannot be completed for any reason
	 */
	public void purgeCache(CodeList codeList) throws DAOException {
		cacheManager.purgeCache( codeList );
	}
	
	/**
	 * Deletes all cache files from the local file system that are not associated with a
	 * publication in persistent storage.
	 * 
	 * @throws DAOException  thrown if the purge operation cannot be completed for any reason
	 */
	public void purgeOrphanedCacheFiles() throws DAOException {
		cacheManager.purgeOrphanedCacheFiles( getFactory() );
	}
	
}
