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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.opentravel.pubs.config.ConfigSettingsFactory;
import org.opentravel.pubs.model.ArtifactComment;
import org.opentravel.pubs.model.Comment;
import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationState;
import org.opentravel.pubs.model.Registrant;
import org.opentravel.pubs.model.SchemaComment;
import org.opentravel.pubs.notification.NotificationManager;
import org.opentravel.pubs.validation.ModelValidator;
import org.opentravel.pubs.validation.ValidationException;
import org.opentravel.pubs.validation.ValidationResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DAO that provides operations related to the creation, retrieval, and management of
 * <code>Comment</code> entities.
 * 
 * @author S. Livezey
 */
public class CommentDAO extends AbstractDAO {
	
	private static final int INITIAL_COMMENT_NUMBER = 1000;
	private static final Map<Class<?>,String[]> emailTemplateMappings;
	
    private static final Logger log = LoggerFactory.getLogger( CommentDAO.class );

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
	 * @param dateRange  the date range for comment submission relative to the current date
	 * @return List<Comment>
	 */
	public List<Comment> findComments(Publication publication, DateRangeType dateRange) {
		TypedQuery<Comment> query;
		
		if ((dateRange == null) || (dateRange == DateRangeType.ALL)) {
			query = getEntityManager().createNamedQuery( "commentFindByPublication", Comment.class );
		} else {
			query = getEntityManager().createNamedQuery( "commentFindByPublicationDateRange", Comment.class );
			query.setParameter( "rDate", dateRange.getRangeStart() );
		}
		query.setParameter( "publicationId", publication.getId() );
		
		return query.getResultList();
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
		checkSubmitterStatus( comment );
		
		getEntityManager().persist( comment );
		sendEmailNotification( comment );
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
		Query query = em.createNamedQuery( "commentCounterNextVal" );
		boolean success = false;
		int nextValue;
		
		try {
			em.getTransaction().begin();
			Integer queryResult = (Integer) query.getSingleResult();
			
			nextValue = queryResult;
			query = em.createNamedQuery( "commentCounterUpdate" );
			query.setParameter( "nextValue", nextValue + 1 );
			query.executeUpdate();
			success = true;
			
		} catch (NoResultException e) {
			nextValue = INITIAL_COMMENT_NUMBER;
			query = em.createNamedQuery( "commentCounterCreate" );
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
	
	/**
	 * Verifies that the submitter is an OpenTravel member if the publication is
	 * currently under member review.
	 * 
	 * @param comment  the comment instance to validate
	 * @throws ValidationException  thrown if the submitter is a non-member when the document is under member review
	 */
	private void checkSubmitterStatus(Comment comment) throws ValidationException {
		boolean submitterIsMember = comment.getSubmittedBy().getOtaMember();
		PublicationState pubState = comment.getPublicationItem().getOwner().getOwner().getState();
		
		if (!submitterIsMember && (pubState == PublicationState.MEMBER_REVIEW)) {
			throw new ValidationException(
					"Sorry, the specification is currently open for comment only to OpenTravel members."
					+ "  Please return when the specification is open for public review.", null);
		}
	}
	
	/**
	 * Sends an email notification to the submitter and all OpenTravel managers when a new comment
	 * is submitted by a web site registrant.
	 * 
	 * @param comment  the comment for which to send an email notification
	 */
	private void sendEmailNotification(Comment comment) {
		if (comment != null) {
			try {
				String[] emailTemplates = emailTemplateMappings.get( comment.getClass() );
				Registrant registrant = comment.getSubmittedBy();
				InternetAddress registrantEmail = new InternetAddress( registrant.getEmail(),
						registrant.getFirstName() + " " + registrant.getLastName() );
				Map<String,Object> contentMap = new HashMap<>();
				
				contentMap.put( "comment", comment );
				contentMap.put( "environmentId", ConfigSettingsFactory.getConfig().getEnvironmentId() );
				
				NotificationManager.getInstance().sendNotification(
						emailTemplates[0], emailTemplates[1], contentMap, registrantEmail );
				
			} catch (UnsupportedEncodingException e) {
				log.error("Error sending notification email for comment submission.", e);
			}
		}
	}
	
	/**
	 * Initializes the email template mapping assignments.
	 */
	static {
		try {
			Map<Class<?>,String[]> etm = new HashMap<>();
			
			etm.put( SchemaComment.class, new String[] {
					NotificationManager.SCHEMA_COMMENT_SUBJECT_TEMPLATE,
					NotificationManager.SCHEMA_COMMENT_MESSAGE_TEMPLATE } );
			etm.put( ArtifactComment.class, new String[] {
					NotificationManager.ARTIFACT_COMMENT_SUBJECT_TEMPLATE,
					NotificationManager.ARTIFACT_COMMENT_MESSAGE_TEMPLATE } );
			emailTemplateMappings = etm;
			
		} catch (Throwable t) {
			throw new ExceptionInInitializerError( t );
		}
	}
	
}
