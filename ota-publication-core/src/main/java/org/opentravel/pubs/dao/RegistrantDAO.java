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

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.opentravel.pubs.model.Registrant;
import org.opentravel.pubs.validation.ModelValidator;
import org.opentravel.pubs.validation.ValidationException;
import org.opentravel.pubs.validation.ValidationResults;

/**
 * DAO that provides operations related to the creation, retrieval, and management of
 * <code>Registrant</code> entities.
 * 
 * @author S. Livezey
 */
public class RegistrantDAO extends AbstractDAO {
	
    private static final String QUERY_FIND_ALL = "SELECT r FROM Registrant r ORDER BY r.registrationDate ASC";
    private static final String QUERY_FIND_BY_DATE_RANGE = "SELECT r FROM Registrant r WHERE r.registrationDate >= :rDate ORDER BY r.registrationDate ASC";
    private static final String QUERY_DELETE_PUB_DOWNLOADS  = "DELETE FROM PUBLICATION_DOWNLOAD WHERE REGISTRANT_ID = :registrantId";
    private static final String QUERY_DELETE_ITEM_DOWNLOADS = "DELETE FROM PUBLICATION_ITEM_DOWNLOAD WHERE REGISTRANT_ID = :registrantId";
    
	/**
	 * Constructor that supplies the factory which created this DAO instance.
	 * 
	 * @param factory  the factory that created this DAO
	 */
    RegistrantDAO(DAOFactory factory) {
		super( factory );
	}
	
	/**
	 * Returns the registrant with the specified ID.
	 * 
	 * @param registrantId  the ID of the registrant to retrieve
	 * @return Registrant
	 */
	public Registrant getRegistrant(long registrantId) {
		return getEntityManager().find( Registrant.class, registrantId );
	}
	
	/**
	 * Returns the list of all registrants that were created in the specified
	 * date range.
	 * 
	 * @param rangeType  the relative date range for the query
	 * @return List<Registrant>
	 */
	@SuppressWarnings("unchecked")
	public List<Registrant> findRegistrants(DateRangeType dateRange) {
		Query query;
		
		if ((dateRange == null) || (dateRange == DateRangeType.ALL)) {
			query = getEntityManager().createQuery( QUERY_FIND_ALL );
		} else {
			query = getEntityManager().createQuery( QUERY_FIND_BY_DATE_RANGE );
			query.setParameter( "rDate", dateRange.getRangeStart() );
		}
		return (List<Registrant>) query.getResultList();
	}
	
	/**
	 * Saves the given registrant to persistent storage.  This method returns the ID
	 * assigned to the new registrant record.
	 * 
	 * @param registrant  the registrant instance to save
	 * @return long
	 * @throws ValidationException  thrown if one or more validation errors were detected for the publication
	 */
	public long saveRegistrant(Registrant registrant) throws ValidationException {
		ValidationResults vResults = ModelValidator.validate( registrant );
		
		if (vResults.hasViolations()) {
			throw new ValidationException( vResults );
		}
		getEntityManager().persist( registrant );
		return registrant.getId();
	}
	
	/**
	 * Deletes the given registrant from persistent storage.
	 * 
	 * @param registrant  the registrant instance to delete
	 */
	public void deleteRegistrant(Registrant registrant) {
		EntityManager em = getEntityManager();
		Query deletePublicationDownloads = em.createNativeQuery( QUERY_DELETE_PUB_DOWNLOADS );
		Query deleteItemDownloads = em.createNativeQuery( QUERY_DELETE_ITEM_DOWNLOADS );
		
		deletePublicationDownloads.setParameter( "registrantId", registrant.getId() );
		deleteItemDownloads.setParameter( "registrantId", registrant.getId() );
		deletePublicationDownloads.executeUpdate();
		deleteItemDownloads.executeUpdate();
		getEntityManager().remove( registrant );
	}
	
}
