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
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.opentravel.pubs.model.Comment;
import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationState;
import org.opentravel.pubs.validation.ModelValidator;
import org.opentravel.pubs.validation.ValidationException;
import org.opentravel.pubs.validation.ValidationResults;

/**
 * DAO that provides operations related to the creation, retrieval, and management of
 * <code>Comment</code> entities.
 * 
 * @author S. Livezey
 */
public class CommentDAO extends AbstractDAO {
	
	private static final int INITIAL_COMMENT_NUMBER = 1000;
	
    private static final String QUERY_COUNTER_NEXTVAL = "SELECT next_val FROM comment_counter";
    private static final String QUERY_CREATE_COUNTER = "INSERT INTO comment_counter VALUES ( :nextValue )";
    private static final String QUERY_UPDATE_COUNTER = "UPDATE comment_counter SET next_val = :nextValue";
    private static final String QUERY_FIND_ALL =
    		"SELECT c FROM Comment c, Publication p, PublicationGroup g, PublicationItem i "
    		+ "WHERE c.publicationState = :state AND c.publicationItem = i AND i.owner = g AND g.owner = p AND p.id = :publicationId "
    		+ "ORDER BY c.commentNumber ASC";
    private static final String QUERY_FIND_BY_DATE_RANGE =
    		"SELECT c FROM Comment c, Publication p, PublicationGroup g, PublicationItem i, Registrant r "
    		+ "WHERE c.publicationState = :state AND r.registrationDate >= :rDate AND c.publicationItem = i AND c.submittedBy = r AND i.owner = g AND g.owner = p AND p.id = :publicationId "
    		+ "ORDER BY c.commentNumber ASC";
	
	/**
	 * Constructor that supplies the factory which created this DAO instance.
	 * 
	 * @param factory  the factory that created this DAO
	 */
	CommentDAO(DAOFactory factory) {
		super( factory );
	}
	
	/**
	 * Retrieves the comment with the specified ID.
	 * 
	 * @param commentId  the ID of the comment to retrieve
	 * @return Comment
	 */
	public Comment getComment(long commentId) {
		return getEntityManager().find( Comment.class, commentId );
	}
	
	/**
	 * Retrieves the comment of the indicated type with the specified ID.
	 * 
	 * @param commentId  the ID of the comment to retrieve
	 * @param type  the type of the comment to return
	 * @return Comment
	 */
	public <T extends Comment> T getComment(long commentId, Class<T> type) {
		return getEntityManager().find( type, commentId );
	}
	
	/**
	 * Returns the list of publication comments that meet the criteria provided.
	 * 
	 * @param publication  the publication for which the comments were submitted
	 * @param state  the state of the publication at the time the comments were submitted
	 * @param dateRange  the date range for comment submission relative to the current date
	 * @return List<Comment>
	 */
	@SuppressWarnings("unchecked")
	public List<Comment> findComments(Publication publication, PublicationState state, DateRangeType dateRange) {
		Query query;
		
		if ((dateRange == null) || (dateRange == DateRangeType.ALL)) {
			query = getEntityManager().createQuery( QUERY_FIND_ALL );
		} else {
			query = getEntityManager().createQuery( QUERY_FIND_BY_DATE_RANGE );
			query.setParameter( "rDate", dateRange.getRangeStart() );
		}
		query.setParameter( "publicationId", publication.getId() );
		query.setParameter( "state", state );
		
		return (List<Comment>) query.getResultList();
	}
	
	/**
	 * Saves the given comment to persistent storage.  This method returns the ID
	 * assigned to the new comment record.
	 * 
	 * @param comment  the comment instance to save
	 * @return long
	 * @throws ValidationException  thrown if one or more validation errors were detected for the publication
	 */
	public long saveComment(Comment comment) throws ValidationException {
		ValidationResults vResults;
		
		comment.setCommentNumber( getNextCommentNumber() );
		vResults = ModelValidator.validate( comment );
		
		if (vResults.hasViolations()) {
			throw new ValidationException( vResults );
		}
		getEntityManager().persist( comment );
		return comment.getId();
	}
	
	/**
	 * Deletes the given comment from persistent storage.
	 * 
	 * @param comment  the comment instance to delete
	 */
	public void deleteComment(Comment comment) {
		getEntityManager().remove( comment );
	}
	
	/**
	 * Returns the next available comment number.  To avoid concurrency issues, this operation
	 * is performed outside the normal transaction context of the parent <code>DAOFactory</code>.
	 * 
	 * @return int
	 */
	private synchronized int getNextCommentNumber() {
		EntityManager em = getFactory().getStandaloneEntityManager();
		Query query = em.createNativeQuery( QUERY_COUNTER_NEXTVAL );
		boolean success = false;
		int nextValue;
		
		try {
			em.getTransaction().begin();
			Integer queryResult = (Integer) query.getSingleResult();
			
			nextValue = queryResult;
			query = em.createNativeQuery( QUERY_UPDATE_COUNTER );
			query.setParameter( "nextValue", nextValue + 1 );
			query.executeUpdate();
			success = true;
			
		} catch (NoResultException e) {
			nextValue = INITIAL_COMMENT_NUMBER;
			query = em.createNativeQuery( QUERY_CREATE_COUNTER );
			query.setParameter( "nextValue", nextValue + 1 );
			query.executeUpdate();
			success = true;
			
		} finally {
			if (success) {
				em.getTransaction().commit();
			} else if ((em != null) && em.getTransaction().isActive()){
				em.getTransaction().rollback();
			}
			em.close();
		}
		return nextValue;
	}
	
}
