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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.InflaterInputStream;

import org.opentravel.pubs.config.ConfigSettingsFactory;
import org.opentravel.pubs.lock.LockException;
import org.opentravel.pubs.lock.LockManager;
import org.opentravel.pubs.lock.LockableResource;
import org.opentravel.pubs.model.CodeList;
import org.opentravel.pubs.model.FileContent;
import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationGroup;
import org.opentravel.pubs.model.PublicationItem;
import org.opentravel.pubs.model.PublicationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the local file cache for downloadable content.
 * 
 * @author S. Livezey
 */
public class ContentCacheManager {
	
	private static Map<String,ContentCacheManager> cacheManagerRegistry = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger( ContentCacheManager.class );
	
	private LockManager lockManager = new LockManager();
	private File cacheRoot;
	
	/**
	 * Returns the default instance of the cache manager.
	 * 
	 * @return ContentCacheManager
	 */
	public static ContentCacheManager getInstance() {
		String cacheLocation = ConfigSettingsFactory.getConfig().getContentCacheLocation();
		
		if (cacheLocation == null) {
			cacheLocation = System.getProperty( "java.io.tmpdir" ) + "/.fileCache";
		}
		return getInstance( new File( cacheLocation ) );
	}
	
	/**
	 * Returns a cache manager for a local file cache at the specified folder
	 * location.
	 * 
	 * @param cacheRoot  the root folder location of the cache manager's local file cache
	 * @return ContentCacheManager
	 */
	public synchronized static ContentCacheManager getInstance(File cacheRoot) {
		String cachePath = cacheRoot.getAbsolutePath();
		ContentCacheManager manager = cacheManagerRegistry.get( cachePath );
		
		if (manager == null) {
			manager = new ContentCacheManager( cacheRoot );
			cacheManagerRegistry.put( cachePath, manager );
		}
		return manager;
	}
	
	/**
	 * Constructor that specifies the root folder location of the local file cache.
	 * 
	 * @param cacheRoot  the root folder location of the local file cache
	 */
	private ContentCacheManager(File cacheRoot) {
		this.cacheRoot = cacheRoot;
		
		if (!cacheRoot.exists()) {
			cacheRoot.mkdirs();
		}
	}
	
	/**
	 * Returns an input stream for the archive content of the given publication.
	 * 
	 * @param publication  the publication for which to return the archive content
	 * @return InputStream
	 * @throws DAOException  thrown if a stream to the archive content cannot obtained for any reason
	 */
	public InputStream getArchiveContent(final Publication publication) throws DAOException {
		LockableFile lockable = new LockableFile( getCacheFile( publication.getId(), "a") );
		FileContentAdapter contentAdapter = new FileContentAdapter() {
			public FileContent getFileContent() {
				return publication.getArchiveContent();
			}
		};
		
		return getContentStream( contentAdapter, lockable );
	}
	
	/**
	 * Returns an input stream for the release notes content of the given publication.
	 * 
	 * @param publication  the publication for which to return the release notes content
	 * @return InputStream
	 * @throws DAOException  thrown if a stream to the release notes content cannot obtained for any reason
	 */
	public InputStream getReleaseNotesContent(final Publication publication) throws DAOException {
		LockableFile lockable = new LockableFile( getCacheFile( publication.getId(), "r") );
		FileContentAdapter contentAdapter = new FileContentAdapter() {
			public FileContent getFileContent() {
				return publication.getReleaseNotesContent();
			}
		};
		
		return getContentStream( contentAdapter, lockable );
	}
	
	/**
	 * Returns an input stream for the file content of the given publication item.
	 * 
	 * @param item  the publication item for which to return content
	 * @return InputStream
	 * @throws DAOException  thrown if a stream to the item's cannot obtained for any reason
	 */
	public InputStream getContent(final PublicationItem item) throws DAOException {
		LockableFile lockable = new LockableFile( getCacheFile( item.getId(), "i") );
		FileContentAdapter contentAdapter = new FileContentAdapter() {
			public FileContent getFileContent() {
				return item.getItemContent();
			}
		};
		
		return getContentStream( contentAdapter, lockable );
	}
	
	/**
	 * Returns an input stream for the archive content of the given code list.
	 * 
	 * @param codeList  the code list for which to return the archive content
	 * @return InputStream
	 * @throws DAOException  thrown if a stream to the archive content cannot obtained for any reason
	 */
	public InputStream getArchiveContent(final CodeList codeList) throws DAOException {
		LockableFile lockable = new LockableFile( getCacheFile( codeList.getId(), "c") );
		FileContentAdapter contentAdapter = new FileContentAdapter() {
			public FileContent getFileContent() {
				return codeList.getArchiveContent();
			}
		};
		
		return getContentStream( contentAdapter, lockable );
	}
	
	/**
	 * Returns an input stream for the file associated with the given adapter instance.  If the
	 * file does not yet exist in the local file cache, it will be cached automatically during
	 * this method's processing.
	 * 
	 * @param contentAdapter  adapter that provides the file content instance associated with a persistent entity
	 * @param lockable  the locable instance that should not be modified or deleted until the content download is complete
	 * @return InputStream
	 * @throws DAOException  thrown if a stream to the file content cannot obtained for any reason
	 */
	private InputStream getContentStream(FileContentAdapter contentAdapter, LockableFile lockable) throws DAOException {
		File cacheFile = lockable.getResource();
		InputStream contentStream;
		boolean cachedFileExists;
		boolean ignoreCache = false;
		
		// Start by obtaining the proper type of lock
		synchronized (lockable.getIdentity()) {
			if (cacheFile.exists()) { // file exists - obtain a read lock
				try {
					lockManager.acquireReadLock( lockable );
					
				} catch (LockException e) {
					log.warn("Timed out while attempting to obtain read lock for file: " + lockable.getIdentity());
					ignoreCache = true;
				}
				cachedFileExists = true;
				
			} else { // file does not exist - obtain a write lock
				try {
					lockManager.acquireWriteLock( lockable );
					
				} catch (LockException e) {
					log.warn("Timed out while attempting to obtain write lock for file: " + lockable.getIdentity());
					ignoreCache = true;
				}
				cachedFileExists = false;
			}
		}
		
		if (ignoreCache) {
			// If we failed to obtain a lock for some reason, we can still return an input
			// stream by obtaining it directly from the database.
			contentStream = null;
			
		} else if (cachedFileExists) {
			try {
				contentStream = new LockReleaseFileInputStream( cacheFile, lockable );
				
			} catch (FileNotFoundException e) {
				// If the file no longer exists for some reason, we will obtain the stream
				// content directly from the database.
				contentStream = null;
			}
			
		} else { // file not cached yet, so we need to create it
			FileContent contentRecord = contentAdapter.getFileContent();
			byte[] contentBytes = (contentRecord == null) ? null : contentRecord.getFileBytes();
			
			if (contentBytes == null) {
				throw new DAOException("No file content exists for the requested resource.");
			}
			cacheFile.getParentFile().mkdirs();
			
			try {
				try (OutputStream out = new FileOutputStream( cacheFile )) {
					InputStream in = new InflaterInputStream( new ByteArrayInputStream( contentBytes ) );
					byte[] buffer = new byte[AbstractDAO.BUFFER_SIZE];
					int bytesRead;
					
					while ((bytesRead = in.read( buffer, 0, buffer.length )) >= 0) {
						out.write( buffer, 0, bytesRead );
					}
					out.flush();
				}
				
				lockManager.downgradeWriteLock( lockable );
				contentStream = new LockReleaseFileInputStream( cacheFile, lockable );
				
			} catch (IOException | LockException e) {
				// If the cache file cannot be created for any reason, we will obtain the stream
				// content directly from the database.
				contentStream = null;
			}
		}
		
		// If we could not gain access to a cached file for any reason, we will obtain the stream
		// content directly from the database.
		if (contentStream == null) {
			FileContent contentRecord = contentAdapter.getFileContent();
			byte[] contentBytes = (contentRecord == null) ? null : contentRecord.getFileBytes();
			
			if (contentBytes == null) {
				throw new DAOException("No file content exists for the requested resource.");
			}
			contentStream = new InflaterInputStream( new ByteArrayInputStream( contentBytes ) );
		}
		return contentStream;
	}
	
	/**
	 * Deletes all locally-cached files that are associated with the given publication.
	 * 
	 * @param publication  the publication for which to purge cached content
	 * @throws DAOException  thrown if the purge operation cannot be completed for any reason
	 */
	public void purgeCache(Publication publication) throws DAOException {
		for (PublicationGroup group : publication.getPublicationGroups()) {
			for (PublicationItem item : group.getPublicationItems()) {
				deleteCacheFile( getCacheFile( item.getId(), "i" ) );
			}
		}
		deleteCacheFile( getCacheFile( publication.getId(), "a" ) ); // archive file
		deleteCacheFile( getCacheFile( publication.getId(), "r" ) ); // release notes
	}
	
	/**
	 * Deletes all cache files from the local file system that are not associated with a
	 * publication in persistent storage.
	 * 
	 * @param dao  the publication DAO that should be used to obtain data from persistent storage
	 * @throws DAOException  thrown if the purge operation cannot be completed for any reason
	 */
	public void purgeOrphanedCacheFiles(DAOFactory factory) throws DAOException {
		PublicationDAO publicationDAO = factory.newPublicationDAO();
		CodeListDAO codeListDAO = factory.newCodeListDAO();
		Set<String> validFilenames = new HashSet<>();
		
		// Start by building a set of all valid cache filenames
		for (PublicationType pubType : PublicationType.values()) {
			for (Publication publication : publicationDAO.getAllPublications( pubType )) {
				for (PublicationGroup group : publication.getPublicationGroups()) {
					for (PublicationItem item : group.getPublicationItems()) {
						validFilenames.add( getCacheFile( item.getId(), "i" ).getName() );
					}
				}
				validFilenames.add( getCacheFile( publication.getId(), "a" ).getName() );
				validFilenames.add( getCacheFile( publication.getId(), "r" ).getName() );
			}
		}
		
		for (CodeList codeList : codeListDAO.getAllCodeLists()) {
			validFilenames.add( getCacheFile( codeList.getId(), "c" ).getName() );
		}
		
		// Purge all files from the cache that are not among the valid filenames
		purgeOrphanedFiles( cacheRoot, validFilenames );
	}
	
	/**
	 * Deletes all locally-cached files that are associated with the given code list.
	 * 
	 * @param codeList  the code list for which to purge cached content
	 * @throws DAOException  thrown if the purge operation cannot be completed for any reason
	 */
	public void purgeCache(CodeList codeList) throws DAOException {
		deleteCacheFile( getCacheFile( codeList.getId(), "c" ) );
	}
	
	/**
	 * Recursively scans the specified cache folder.  Any files that are not among
	 * the list of valid filenames are deleted.  Since the deleted files are not associated
	 * with any existing persistent resources, no locks are obtained prior to deletion.
	 * 
	 * @param cacheFolder  the cache folder to be purged
	 * @param validFilenames  the set of valid filenames that should not be purged
	 */
	private void purgeOrphanedFiles(File cacheFolder, Set<String> validFilenames) {
		if ((cacheFolder != null) && cacheFolder.isDirectory()) {
			for (File folderItem : cacheFolder.listFiles()) {
				if (folderItem.isFile()) {
					if (!validFilenames.contains( folderItem.getName() )) {
						folderItem.delete();
					}
				} else if (folderItem.isDirectory()) {
					purgeOrphanedFiles( folderItem, validFilenames );
				}
			}
		}
	}
	
	/**
	 * If the given cache file exists on the local file system, it will be deleted
	 * as soon as a write lock can be obtained.  If the attempt to obtain a lock fails,
	 * the file will be skipped.
	 * 
	 * @param cacheFile  the cache file to delete
	 */
	private void deleteCacheFile(File cacheFile) {
		LockableFile lockable = new LockableFile( cacheFile );
		boolean lockAcquired = false;
		
		try {
			if (cacheFile.exists()) {
				lockManager.acquireWriteLock( lockable );
				lockAcquired = true;
				cacheFile.delete();
			}
			
		} catch (LockException e) {
			log.warn("Unable to delete cached file because a write lock could not be obtained.");
			
		} finally {
			try {
				if (lockAcquired) {
					lockManager.releaseWriteLock( lockable );
				}
			} catch (LockException e) {}
		}
	}
	
	/**
	 * Returns a cache file location using the database ID and prefix provided.  The
	 * file that is returned may or may not yet exist on the file system.
	 * 
	 * @param id  the database ID for which to create a cache file
	 * @param prefix  the prefix to be prepended to the cache filename
	 * @return File
	 */
	private File getCacheFile(long id, String prefix) {
		String fileId = id + "";
		String cacheFolder;
		int len;
		
		if (id < 1000) { // pad with 0's if three digits or less
			fileId = "000" + fileId;
			fileId = fileId.substring( fileId.length() - 4 );
		}
		len = fileId.length();
		cacheFolder = "/" + fileId.charAt( len - 1 ) + "/" +
				fileId.charAt( len - 2 ) + "/" + fileId.charAt( len - 3 ) + "/";
		return new File( cacheRoot, cacheFolder + prefix + fileId + ".dat" );
	}
	
	/**
	 * Used to normalized the acquisition of <code>FileContent</code> from a variety of
	 * persistent object types.
	 */
	private interface FileContentAdapter {
		
		/**
		 * Returns the <code>FileContent</code> instance for a persistent resource.
		 * 
		 * @return FileContent
		 */
		public FileContent getFileContent();
		
	}
	
	/**
	 * <code>LockableResource</code> for files that allows read/write locks
	 * to be established.
	 *
	 * @author S. Livezey
	 */
	private static class LockableFile extends LockableResource<File> {
		
		/**
		 * Constructor that provides the file for which locks can be obtained.
		 * 
		 * @param resource  the file resource for which locks can be obtained
		 */
		public LockableFile(File resource) {
			super(resource);
		}

		/**
		 * @see org.opentravel.pubs.lock.LockableResource#buildIdentity(java.lang.Object)
		 */
		@Override
		protected String buildIdentity(File resource) {
			return resource.getName();
		}

	}
	
	/**
	 * Input stream that releases the file's read lock when the stream is closed.
	 * 
	 * <p>Note that the read lock must be established prior to creating the stream.
	 *
	 * @author S. Livezey
	 */
	private class LockReleaseFileInputStream extends FileInputStream {
		
		private LockableFile lockable;
		/**
		 * Constructor that specifies the file and lockable resource for which a lock
		 * has already been obtained.
		 * 
		 * @param file  the file from which to stream content
		 * @param lockable  the lockable resource for the file object
		 * @throws FileNotFoundException
		 */
		public LockReleaseFileInputStream(File file, LockableFile lockable)
				throws FileNotFoundException {
			super( file );
			this.lockable = lockable;
		}
		
		/**
		 * Releases the lock on the file.
		 * 
		 * @throws IOException  thrown 
		 */
		private synchronized void releaseLock() throws IOException {
			if (lockable != null) {
				try {
					lockManager.releaseReadLock( lockable );
					
				} catch (LockException e) {
					throw new IOException("Unable to release read lock on file: " + lockable.getIdentity());
					
				} finally {
					lockable = null;
				}
			}
		}
		/**
		 * @see java.io.FileInputStream#close()
		 */
		@Override
		public void close() throws IOException {
			super.close();
			releaseLock();
		}
		
		/**
		 * @see java.io.FileInputStream#finalize()
		 */
		@Override
		protected void finalize() throws IOException {
			super.finalize();
			releaseLock(); // Release the lock if the stream was not properly closed
		}
		
	}
	
}
