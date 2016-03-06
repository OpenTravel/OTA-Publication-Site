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

package org.opentravel.pubs.controllers;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.opentravel.pubs.builders.ArtifactCommentBuilder;
import org.opentravel.pubs.builders.SchemaCommentBuilder;
import org.opentravel.pubs.dao.CommentDAO;
import org.opentravel.pubs.dao.DAOException;
import org.opentravel.pubs.dao.DAOFactoryManager;
import org.opentravel.pubs.dao.DownloadDAO;
import org.opentravel.pubs.dao.PublicationDAO;
import org.opentravel.pubs.dao.RegistrantDAO;
import org.opentravel.pubs.forms.ArtifactCommentForm;
import org.opentravel.pubs.forms.RegistrantForm;
import org.opentravel.pubs.forms.SchemaCommentForm;
import org.opentravel.pubs.forms.ViewSpecificationForm;
import org.opentravel.pubs.model.ArtifactComment;
import org.opentravel.pubs.model.CommentType;
import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationGroup;
import org.opentravel.pubs.model.PublicationItem;
import org.opentravel.pubs.model.PublicationItemType;
import org.opentravel.pubs.model.PublicationType;
import org.opentravel.pubs.model.Registrant;
import org.opentravel.pubs.model.SchemaComment;
import org.opentravel.pubs.util.StringUtils;
import org.opentravel.pubs.util.ValueFormatter;
import org.opentravel.pubs.validation.ModelValidator;
import org.opentravel.pubs.validation.ValidationException;
import org.opentravel.pubs.validation.ValidationResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller that handles interactions with the publication pages of the OpenTravel
 * specification site.
 * 
 * @author S. Livezey
 */
@Controller
@RequestMapping("/specifications")
public class PublicationController extends BaseController {
	
    private static final Logger log = LoggerFactory.getLogger( PublicationController.class );
    
    @RequestMapping({ "/Specifications.html", "/Specifications.htm", "/Specifications10.html", "/Specifications10.htm" })
    public String specifications10Page(Model model, HttpSession session,
            @RequestParam(value = "spec", required = false) String spec,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("specificationForm") ViewSpecificationForm specificationForm) {
    	String targetPage = "specification10Main";
    	try {
    		RegistrantForm registrantForm = specificationForm.getRegistrantForm();
        	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
        	Publication publication = null;
        	
        	if (spec != null) {
        		publication = pDao.getPublication( spec, PublicationType.OTA_1_0 );
        	}
    		if (publication == null) {
    			publication = pDao.getLatestPublication( PublicationType.OTA_1_0 );
    		}
    		
        	model.addAttribute( "publication", publication );
        	model.addAttribute( "registrationPage", "Specifications10.html" );
        	
        	if (newSession) session.removeAttribute( "registrantId" );
        	handleRegistrantInfo( registrantForm, model, session );
        	
        	// If we processed the form successfully, clear our model so that its
        	// attributes will not show up as URL parameters on redirect
        	if (registrantForm.isProcessForm() && (model.asMap().get( "registrant" ) != null)) {
        		targetPage = "redirect:/specifications/Specifications10.html";
        		model.asMap().clear();
        	}
        	specificationForm.setProcessForm( true );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/Specifications20.html", "/Specifications20.htm" })
    public String specifications20Page(Model model, HttpSession session,
            @RequestParam(value = "spec", required = false) String spec,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("specificationForm") ViewSpecificationForm specificationForm) {
    	String targetPage = "specification20Main";
    	try {
    		RegistrantForm registrantForm = specificationForm.getRegistrantForm();
        	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
        	Publication publication = null;
        	
        	if (spec != null) {
        		publication = pDao.getPublication( spec, PublicationType.OTA_2_0 );
        	}
    		if (publication == null) {
    			publication = pDao.getLatestPublication( PublicationType.OTA_2_0 );
    		}
    		
        	model.addAttribute( "publication", publication );
        	model.addAttribute( "registrationPage", "Specifications20.html" );
        	
        	if (newSession) session.removeAttribute( "registrantId" );
        	handleRegistrantInfo( registrantForm, model, session );
        	
        	// If we processed the form successfully, clear our model so that its
        	// attributes will not show up as URL parameters on redirect
        	if (registrantForm.isProcessForm() && (model.asMap().get( "registrant" ) != null)) {
        		targetPage = "redirect:/specifications/Specifications20.html";
        		model.asMap().clear();
        	}
        	specificationForm.setProcessForm( true );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/ReleaseNotes.html", "/ReleaseNotes.htm" })
    public String releaseNotesPage(Model model, HttpSession session,
            @RequestParam(value = "spec", required = false) String spec,
    		@RequestParam(value = "specType", required = false) String specType) {
		String targetPage = "specificationReleaseNotes";
    	try {
        	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
    		PublicationType pubType = resolvePublicationType( specType );
        	Publication publication = pDao.getPublication( spec, pubType );
    		Registrant registrant = getCurrentRegistrant( session );
    		
        	model.addAttribute( "publication", publication );
    		
    		if (registrant == null) {
    			targetPage = "specificationMain";
    		}
    		model.addAttribute( "releaseNotesText", getReleaseNotesText( publication ));
    		
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/Comment10Spec.html", "/Comment10Spec.htm" })
    public String comment10SpecPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") SchemaCommentForm commentForm) {
    	String targetPage = "specificationComment10";
		boolean commentSuccess = false;
    	try {
        	if (newSession) session.removeAttribute( "registrantId" );
    		Registrant registrant = getCurrentRegistrant( session );
    		boolean processRegistrant = commentForm.isProcessForm() && (registrant == null);
    		RegistrantForm registrantForm = commentForm.getRegistrantForm();
    		
    		registrantForm.setProcessForm( processRegistrant );
        	handleRegistrantInfo( registrantForm, model, session );
    		registrant = (Registrant) model.asMap().get( "registrant" );
        	
    		// If the registrant was created successfully, commit it and start a new
    		// transaction for the comment
    		if (processRegistrant && (registrant != null)) {
    			DAOFactoryManager.getFactory().commitTransaction();
    			DAOFactoryManager.getFactory().beginTransaction();
    			registrant = getCurrentRegistrant( session );
    		}
    		
        	if (commentForm.isProcessForm()) {
        		commentSuccess = addSchemaComment( commentForm, registrant, model, session );
        	}
    		
        	if (commentSuccess) {
        		targetPage = "redirect:/specifications/CommentThankYou.html";
        		redirectAttrs.addAttribute( "submitCommentsUrl", "/specifications/Comment10Spec.html" );
        		model.asMap().clear();
        		
        	} else {
            	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
            	Publication publication = pDao.getLatestPublication( PublicationType.OTA_1_0 );
            	List<PublicationItem> publicationItems = new ArrayList<>();
            	
            	publicationItems.addAll( pDao.getPublicationItems( publication, PublicationItemType.WSDL ) );
            	publicationItems.addAll( pDao.getPublicationItems( publication, PublicationItemType.XML_SCHEMA ) );
            	publicationItems.addAll( pDao.getPublicationItems( publication, PublicationItemType.JSON_SCHEMA ) );
            	
        		model.addAttribute( "publication", publication );
        		model.addAttribute( "publicationItems", publicationItems );
        		model.addAttribute( "commentTypes", Arrays.asList( CommentType.values() ) );
        	}
    		model.addAttribute( "submitCommentsUrl", "/specifications/Comment10Spec.html" );
        	commentForm.setProcessForm( true );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/Comment20Spec.html", "/Comment20Spec.htm" })
    public String comment20SpecPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") SchemaCommentForm commentForm) {
    	String targetPage = "specificationComment20";
		boolean commentSuccess = false;
    	try {
        	if (newSession) session.removeAttribute( "registrantId" );
    		Registrant registrant = getCurrentRegistrant( session );
    		boolean processRegistrant = commentForm.isProcessForm() && (registrant == null);
    		RegistrantForm registrantForm = commentForm.getRegistrantForm();
    		
    		registrantForm.setProcessForm( processRegistrant );
        	handleRegistrantInfo( registrantForm, model, session );
    		registrant = (Registrant) model.asMap().get( "registrant" );
        	
    		// If the registrant was created successfully, commit it and start a new
    		// transaction for the comment
    		if (processRegistrant && (registrant != null)) {
    			DAOFactoryManager.getFactory().commitTransaction();
    			DAOFactoryManager.getFactory().beginTransaction();
    			registrant = getCurrentRegistrant( session );
    		}
        	
        	if (commentForm.isProcessForm()) {
        		commentSuccess = addSchemaComment( commentForm, registrant, model, session );
        	}
    		
        	if (commentSuccess) {
        		targetPage = "redirect:/specifications/CommentThankYou.html";
        		redirectAttrs.addAttribute( "submitCommentsUrl", "/specifications/Comment20Spec.html" );
        		model.asMap().clear();
        		
        	} else {
            	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
            	Publication publication = pDao.getLatestPublication( PublicationType.OTA_2_0 );
            	List<PublicationItem> publicationItems = new ArrayList<>();
            	
            	publicationItems.addAll( pDao.getPublicationItems( publication, PublicationItemType.WSDL ) );
            	publicationItems.addAll( pDao.getPublicationItems( publication, PublicationItemType.XML_SCHEMA ) );
            	publicationItems.addAll( pDao.getPublicationItems( publication, PublicationItemType.JSON_SCHEMA ) );
            	
        		model.addAttribute( "publication", publication );
        		model.addAttribute( "publicationItems", publicationItems );
        		model.addAttribute( "commentTypes", Arrays.asList( CommentType.values() ) );
        	}
    		model.addAttribute( "submitCommentsUrl", "/specifications/Comment20Spec.html" );
        	commentForm.setProcessForm( true );
    		
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/Comment10Artifact.html", "/Comment10Artifact.html" })
    public String comment10ArtifactPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") ArtifactCommentForm commentForm) {
    	String targetPage = "artifactComment10";
		boolean commentSuccess = false;
    	try {
        	if (newSession) session.removeAttribute( "registrantId" );
    		Registrant registrant = getCurrentRegistrant( session );
    		boolean processRegistrant = commentForm.isProcessForm() && (registrant == null);
    		RegistrantForm registrantForm = commentForm.getRegistrantForm();
    		
    		registrantForm.setProcessForm( processRegistrant );
        	handleRegistrantInfo( registrantForm, model, session );
    		registrant = (Registrant) model.asMap().get( "registrant" );
        	
    		// If the registrant was created successfully, commit it and start a new
    		// transaction for the comment
    		if (processRegistrant && (registrant != null)) {
    			DAOFactoryManager.getFactory().commitTransaction();
    			DAOFactoryManager.getFactory().beginTransaction();
    			registrant = getCurrentRegistrant( session );
    		}
    		
        	if (commentForm.isProcessForm()) {
        		commentSuccess = addArtifactComment( commentForm, registrant, model, session );
        	}
    		
        	if (commentSuccess) {
        		targetPage = "redirect:/specifications/CommentThankYou.html";
        		redirectAttrs.addAttribute( "submitCommentsUrl", "/specifications/Comment10Artifact.html" );
        		model.asMap().clear();
        		
        	} else {
            	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
            	Publication publication = pDao.getLatestPublication( PublicationType.OTA_1_0 );
            	
        		model.addAttribute( "publication", publication );
        		model.addAttribute( "publicationItems",
        				pDao.getPublicationItems( publication, PublicationItemType.ARTIFACT ) );
        		model.addAttribute( "commentTypes", Arrays.asList( CommentType.values() ) );
        	}
    		model.addAttribute( "submitCommentsUrl", "/specifications/Comment10Artifact.html" );
        	commentForm.setProcessForm( true );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/Comment20Artifact.html", "/Comment20Artifact.htm" })
    public String comment20ArtifactPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") ArtifactCommentForm commentForm) {
    	String targetPage = "artifactComment20";
		boolean commentSuccess = false;
    	try {
        	if (newSession) session.removeAttribute( "registrantId" );
    		Registrant registrant = getCurrentRegistrant( session );
    		boolean processRegistrant = commentForm.isProcessForm() && (registrant == null);
    		RegistrantForm registrantForm = commentForm.getRegistrantForm();
    		
    		registrantForm.setProcessForm( processRegistrant );
        	handleRegistrantInfo( registrantForm, model, session );
    		registrant = (Registrant) model.asMap().get( "registrant" );
        	
    		// If the registrant was created successfully, commit it and start a new
    		// transaction for the comment
    		if (processRegistrant && (registrant != null)) {
    			DAOFactoryManager.getFactory().commitTransaction();
    			DAOFactoryManager.getFactory().beginTransaction();
    			registrant = getCurrentRegistrant( session );
    		}
    		
        	if (commentForm.isProcessForm()) {
        		commentSuccess = addArtifactComment( commentForm, registrant, model, session );
        	}
    		
        	if (commentSuccess) {
        		targetPage = "redirect:/specifications/CommentThankYou.html";
        		redirectAttrs.addAttribute( "submitCommentsUrl", "/specifications/Comment20Artifact.html" );
        		model.asMap().clear();
        		
        	} else {
            	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
            	Publication publication = pDao.getLatestPublication( PublicationType.OTA_2_0 );
            	
        		model.addAttribute( "publication", publication );
        		model.addAttribute( "publicationItems",
        				pDao.getPublicationItems( publication, PublicationItemType.ARTIFACT ) );
        		model.addAttribute( "commentTypes", Arrays.asList( CommentType.values() ) );
        	}
    		model.addAttribute( "submitCommentsUrl", "/specifications/Comment20Artifact.html" );
        	commentForm.setProcessForm( true );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/CommentThankYou.html", "/CommentThankYou.htm" })
    public String commentThankYouPage(Model model,
    		@RequestParam(value = "submitCommentsUrl", required = false) String submitCommentsUrl) {
    	model.addAttribute( "submitCommentsUrl", submitCommentsUrl );
    	return applyCommonValues( model, "commentThankYou" );
    }
    
    @RequestMapping({ "/ModelViewer.html", "/ModelViewer.htm" })
    public String modelViewerPage(Model model) {
    	try {
        	PublicationDAO dao = DAOFactoryManager.getFactory().newPublicationDAO();
        	Publication publication = dao.getLatestPublication( PublicationType.OTA_1_0 );
        	
        	model.addAttribute( "publication", publication );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, "modelViewer" );
    }
    
    @RequestMapping({ "/OnlinePublications.html", "/OnlinePublications.htm" })
    public String onlineArtifactsPage(Model model) {
    	try {
        	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
    		List<Publication> publications10 = pDao.getAllPublications( PublicationType.OTA_1_0 );
    		List<Publication> publications20 = pDao.getAllPublications( PublicationType.OTA_2_0 );
    		
        	model.addAttribute( "publications10", publications10 );
        	model.addAttribute( "publications20", publications20 );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, "onlinePublications" );
    }
    
    @RequestMapping({ "/OnlinePublicationDetails.html", "/OnlinePublicationDetails.htm" })
    public String onlineArtifactDetailsPage(Model model, HttpSession session,
            @RequestParam(value = "spec", required = false) String spec,
    		@RequestParam(value = "specType", required = false) String specType,
    		@RequestParam(value = "group", required = false) Long selectedGroupId) {
    	try {
        	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
    		PublicationType pubType = resolvePublicationType( specType );
        	Publication publication = pDao.getPublication( spec, pubType );
        	List<PublicationGroup> groupList = publication.getPublicationGroups();
        	List<PublicationItem> itemList;
    		PublicationGroup selectedGroup =
    				((groupList == null) || groupList.isEmpty()) ? null : groupList.get( 0 );
    		
    		if (selectedGroupId != null) {
    			for (PublicationGroup group : groupList) {
    				if (group.getId() == selectedGroupId) {
    					selectedGroup = group;
    					break;
    				}
    			}
    		}
   			itemList = (selectedGroup == null) ? null : selectedGroup.getPublicationItems();
    		
        	model.addAttribute( "publication", publication );
        	model.addAttribute( "selectedGroup", selectedGroup.getId() );
        	model.addAttribute( "groupList", groupList );
        	model.addAttribute( "itemList", itemList );
        	model.addAttribute( "formatter", ValueFormatter.getInstance() );
    		
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, "onlinePublicationDetails" );
    }
    
    @RequestMapping({ "/PastSpecs.html", "/PastSpecs.htm" })
    public String pastSpecsPage(Model model) {
    	try {
        	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
    		List<Publication> publications10 = pDao.getAllPublications( PublicationType.OTA_1_0 );
    		List<Publication> publications20 = pDao.getAllPublications( PublicationType.OTA_2_0 );
    		
    		// Remove the first element of each list since it is the current specification
    		if (!publications10.isEmpty()) {
    			publications10.remove( 0 );
    		}
    		if (!publications20.isEmpty()) {
    			publications20.remove( 0 );
    		}
        	model.addAttribute( "publications10", publications10 );
        	model.addAttribute( "publications20", publications20 );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, "pastSpecifications" );
    }
    
    @RequestMapping({ "/FAQ.html", "/FAQ.htm" })
    public String faqPage(Model model) {
    	return applyCommonValues( model, "specificationFAQ" );
    }
    
    @RequestMapping({ "/DownloadRegister.html", "/DownloadRegister.htm" })
    public String downloadRegisterPage(Model model, HttpSession session,
    		@RequestParam(value = "pubName", required = true) String pubName,
    		@RequestParam(value = "pubType", required = true) String type,
    		@RequestParam(value = "filename", required = true) String filename,
    		@ModelAttribute("specificationForm") ViewSpecificationForm specificationForm) {
    	String targetPage = "downloadRegister";
    	try {
    		RegistrantForm registrantForm = specificationForm.getRegistrantForm();
        	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
    		PublicationType pubType = resolvePublicationType( type );
        	Publication publication = pDao.getPublication( pubName, pubType );
        	
        	handleRegistrantInfo( registrantForm, model, session );
    		
        	// If we processed the form successfully, clear our model so that its
        	// attributes will not show up as URL parameters on redirect
        	if (registrantForm.isProcessForm() && (model.asMap().get( "registrant" ) != null)) {
        		targetPage = "redirect:/content/specifications/downloads/" +
        				pubName + "/" + type + "/" + filename;
        		model.asMap().clear();
        		
        	} else {
        		model.addAttribute( "publication", publication );
        		model.addAttribute( "pubName", pubName );
        		model.addAttribute( "pubType", pubType );
        		model.addAttribute( "filename", filename );
        	}
        	specificationForm.setProcessForm( true );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/downloads/{pubName}/{pubType}/{filename:.+}" })
    public String downloadContent(Model model, HttpSession session, HttpServletRequest request,
    		HttpServletResponse response, RedirectAttributes redirectAttrs,
    		@PathVariable("pubName") String pubName, @PathVariable("pubType") String type,
    		@PathVariable("filename") String filename) {
    	String targetPath = null;
    	try {
    		Registrant registrant = getCurrentRegistrant( session );
    		InputStream contentStream = null;
    		
    		if (registrant == null) {
    			model.asMap().clear();
    			redirectAttrs.addAttribute( "pubName", pubName );
    			redirectAttrs.addAttribute( "pubType", type );
    			redirectAttrs.addAttribute( "filename", filename );
    			targetPath = "redirect:/specifications/DownloadRegister.html";
    			
    		} else {
    			if ((pubName != null) && (type != null) && (filename != null)) {
                	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
                	DownloadDAO dDao = DAOFactoryManager.getFactory().newDownloadDAO();
            		PublicationType pubType = resolvePublicationType( type );
                	Publication publication = pDao.getPublication( pubName, pubType );
        			
                	if (publication != null) {
                    	if (filename.equals( publication.getArchiveFilename() )) {
                    		contentStream = dDao.getArchiveContent( publication, registrant );
                    		
                    	} else {
                    		PublicationItem item = pDao.findPublicationItem( publication, filename );
                    		
                    		if (item != null) {
                        		contentStream = dDao.getContent( item, registrant );
                    		}
                    	}
                	}
        		}
    			
        		if (contentStream != null) {
        			try (OutputStream responseOut = response.getOutputStream()) {
        				byte[] buffer = new byte[BUFFER_SIZE];
        				int bytesRead;
        				
        				while ((bytesRead = contentStream.read( buffer, 0, buffer.length)) >= 0) {
        					responseOut.write( buffer, 0, bytesRead );
        				}
        				
        			} finally {
        				try {
        					contentStream.close();
        				} catch (Throwable t) {}
        			}
        			
        		} else {
        			model.asMap().clear();
        			redirectAttrs.addAttribute( "filename", request.getRequestURL() );
        			targetPath = "redirect:/specifications/DownloadNotFound.html";
        		}
    		}
    		
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPath );
    }
    
    @RequestMapping({ "/DownloadNotFound.html", "/DownloadNotFound.htm" })
    public String downloadNotFoundPage(Model model,
    		@RequestParam(value = "filename") String filename) {
    	model.addAttribute( "filename", filename );
    	return applyCommonValues( model, "downloadNotFound" );
    }
    
    /**
     * Returns the current registrant or null if the user has not yet registered.
     * 
     * @param session  the HTTP session
     * @return Registrant
     */
    private Registrant getCurrentRegistrant(HttpSession session) {
		Long registrantId = (Long) session.getAttribute( "registrantId" );
    	Registrant registrant = null;
    	
		if (registrantId != null) {
			registrant = DAOFactoryManager.getFactory().newRegistrantDAO().getRegistrant( registrantId );
		}
		return registrant;
    }
    
    /**
     * Creates, retrieves, or updates the current/new registrant.  If no registration has
     * been created or a validation error occurs while attempting to create one, this method
     * will return null.
     * 
     * @param registrantForm  form containing all of registrant values submitted by the user
     * @param model  the UI model for the current request
     * @param session  the HTTP session
     */
    private void handleRegistrantInfo(RegistrantForm registrantForm, Model model,
    		HttpSession session) {
		RegistrantDAO rDao = DAOFactoryManager.getFactory().newRegistrantDAO();
		Long registrantId = (Long) session.getAttribute( "registrantId" );
    	Registrant registrant = null;
    	
		if ((registrantId != null) && (registrantId >= 0)) {
    		registrant = rDao.getRegistrant( registrantId );
    		if (registrant == null) registrantId = null;
		}
    	if (registrantForm.isProcessForm()) {
    		if (registrant == null) {
    			registrant = new Registrant();
    			registrant.setRegistrationDate( new Date() );
    		}
			registrant.setLastName( StringUtils.trimString( registrantForm.getLastName() ) );
			registrant.setFirstName( StringUtils.trimString( registrantForm.getFirstName() ) );
			registrant.setTitle( StringUtils.trimString( registrantForm.getTitle() ) );
			registrant.setCompany( StringUtils.trimString( registrantForm.getCompany() ) );
			registrant.setPhone( StringUtils.trimString( registrantForm.getPhone() ) );
			registrant.setEmail( StringUtils.trimString( registrantForm.getEmail() ) );
			registrant.setOtaMember( registrantForm.getOtaMember() );
			
			if ((registrantId == null) || (registrantId < 0)) { // persist for the first time
				try {
					registrantId = rDao.saveRegistrant( registrant );
					session.setAttribute( "registrantId", registrantId );
					
				} catch (ValidationException e) {
		    		addValidationErrors( e, model );
					registrant = null;
				}
			}
    	}
    	model.addAttribute( "registrant", registrant );
    }
    
    /**
     * Creates and saves a new schema comment using the information provided.
     * 
     * @param commentForm  the form used to supply the field values for the submitted comment
     * @param registrant  the web site registrant who submitted the comment
     * @param model  the UI model for the current request
     * @param session  the HTTP session
     */
    private boolean addSchemaComment(SchemaCommentForm commentForm, Registrant registrant,
    		Model model, HttpSession session) {
    	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
    	CommentDAO cDao = DAOFactoryManager.getFactory().newCommentDAO();
    	boolean success = false;
    	try {
    		PublicationItem item = (commentForm.getItemId() == null) ?
    				null : pDao.getPublicationItem( commentForm.getItemId() );
    		
    		if ((commentForm.getItemId() != null) && (item == null)) {
       			throw new DAOException("Unable to resolve the publication item to which this comment applies.");
    		}
    		
    		SchemaComment comment = new SchemaCommentBuilder()
    			.setPublicationItem( item )
    			.setPublicationState( (item == null) ? null : item.getOwner().getOwner().getState() )
    			.setCommentType( commentForm.getCommentType() )
    			.setCommentText( StringUtils.trimString( commentForm.getCommentText() ) )
    			.setSuggestedChange( StringUtils.trimString( commentForm.getSuggestedChange() ) )
    			.setCommentXPath( StringUtils.trimString( commentForm.getCommentXPath() ) )
    			.setModifyXPath( StringUtils.trimString( commentForm.getModifyXPath() ) )
    			.setNewAnnotations( commentForm.getNewAnnotations() )
    			.setSubmittedBy( registrant )
    			.build();
    		
    		if ((comment.getPublicationItem() != null) && (comment.getSubmittedBy() != null)) {
        		cDao.saveComment( comment );
    			success = true;
    			
    		} else {
    			ValidationResults vResults = ModelValidator.validate( comment );
    			
    			if (vResults.hasViolations()) {
    				throw new ValidationException( vResults );
    			}
    		}
    		
    	} catch (ValidationException e) {
    		addValidationErrors( e, model );
    		
    	} catch (Throwable t) {
    		log.error("Error saving comment (please contact the site administrator).", t);
    		setErrorMessage( "Error saving comment (please contact the site administrator).", model);
    	}
    	return success;
    }
    
    /**
     * Creates and saves a new schema comment using the information provided.
     * 
     * @param commentForm  the form used to supply the field values for the submitted comment
     * @param registrant  the web site registrant who submitted the comment
     * @param model  the UI model for the current request
     * @param session  the HTTP session
     */
    private boolean addArtifactComment(ArtifactCommentForm commentForm, Registrant registrant,
    		Model model, HttpSession session) {
    	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
    	CommentDAO cDao = DAOFactoryManager.getFactory().newCommentDAO();
    	boolean success = false;
    	try {
    		PublicationItem item = (commentForm.getItemId() == null) ?
    				null : pDao.getPublicationItem( commentForm.getItemId() );
    		
    		if ((commentForm.getItemId() != null) && (item == null)) {
    			throw new DAOException("Unable to resolve the publication item to which this comment applies.");
    		}
    		ArtifactComment comment = new ArtifactCommentBuilder()
    			.setPublicationItem( item )
    			.setPublicationState( (item == null) ? null : item.getOwner().getOwner().getState() )
    			.setCommentType( commentForm.getCommentType() )
    			.setCommentText( StringUtils.trimString( commentForm.getCommentText() ) )
    			.setSuggestedChange( StringUtils.trimString( commentForm.getSuggestedChange() ) )
    			.setPageNumbers( StringUtils.trimString( commentForm.getPageNumbers() ) )
    			.setLineNumbers( StringUtils.trimString( commentForm.getLineNumbers() ) )
    			.setSubmittedBy( registrant )
    			.build();
    		
    		if ((comment.getPublicationItem() != null) && (comment.getSubmittedBy() != null)) {
        		cDao.saveComment( comment );
    			success = true;
    			
    		} else {
    			ValidationResults vResults = ModelValidator.validate( comment );
    			
    			if (vResults.hasViolations()) {
    				throw new ValidationException( vResults );
    			}
    		}
    		
    	} catch (ValidationException e) {
    		addValidationErrors( e, model );
    		
    	} catch (Throwable t) {
    		log.error("Error saving comment (please contact the site administrator).", t);
    		setErrorMessage( "Error saving comment (please contact the site administrator).", model);
    	}
    	return success;
    }
    
    /**
     * Returns the text of the release notes, or an empty string if they cannot be
     * retrieved.
     * 
     * @param publication  the publication for which to return release notes
     */
    private String getReleaseNotesText(Publication publication) {
		DownloadDAO dao = DAOFactoryManager.getFactory().newDownloadDAO();
    	String releaseNotes;
    	
    	try (Reader reader = new InputStreamReader( dao.getReleaseNotesContent( publication ) )	) {
    		StringBuilder out = new StringBuilder();
        	char[] buffer = new char[4096];
        	int bytesRead;
        	
    		while ((bytesRead = reader.read( buffer, 0, buffer.length )) >= 0) {
    			out.append( buffer, 0, bytesRead );
    		}
    		releaseNotes = out.toString().trim().replaceAll("\n", "<br>");
    		
    	} catch (Throwable t) {
    		releaseNotes = "";
    	}
    	return releaseNotes;
    }
    
}
