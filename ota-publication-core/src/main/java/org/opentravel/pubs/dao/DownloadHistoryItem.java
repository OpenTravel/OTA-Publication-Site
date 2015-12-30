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

import java.util.List;

import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationItem;
import org.opentravel.pubs.model.Registrant;

/**
 * Encapsulates the download history for a single <code>Publication</code> or
 * <code>PublicationItem</code>.
 * 
 * @author S. Livezey
 */
public class DownloadHistoryItem {
	
	private Publication publication;
	private PublicationItem publicationItem;
	private List<Registrant> downloadedBy;
	
	/**
	 * Constructor that specifies the publication for which downloads occurred.
	 * 
	 * @param publication  the publication for which downloads occurred
	 */
	public DownloadHistoryItem(Publication publication) {
		this.publication = publication;
	}
	
	/**
	 * Constructor that specifies the publication item for which downloads occurred.
	 * 
	 * @param publicationItem  the publication item for which downloads occurred
	 */
	public DownloadHistoryItem(PublicationItem publicationItem) {
		this.publicationItem = publicationItem;
	}

	/**
	 * Returns the value of the 'downloadedBy' field.
	 *
	 * @return List<Registrant>
	 */
	public List<Registrant> getDownloadedBy() {
		return downloadedBy;
	}

	/**
	 * Assigns the value of the 'downloadedBy' field.
	 *
	 * @param downloadedBy  the field value to assign
	 */
	public void setDownloadedBy(List<Registrant> downloadedBy) {
		this.downloadedBy = downloadedBy;
	}

	/**
	 * Returns the value of the 'publication' field.
	 *
	 * @return Publication
	 */
	public Publication getPublication() {
		return publication;
	}

	/**
	 * Returns the value of the 'publicationItem' field.
	 *
	 * @return PublicationItem
	 */
	public PublicationItem getPublicationItem() {
		return publicationItem;
	}
	
}
