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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.opentravel.pubs.builders.ArtifactCommentBuilder;
import org.opentravel.pubs.builders.SchemaCommentBuilder;
import org.opentravel.pubs.dao.ApplicationSettingsDAO;
import org.opentravel.pubs.dao.CodeListDAO;
import org.opentravel.pubs.dao.CommentDAO;
import org.opentravel.pubs.dao.DAOException;
import org.opentravel.pubs.dao.DAOFactoryManager;
import org.opentravel.pubs.dao.DownloadDAO;
import org.opentravel.pubs.dao.PublicationDAO;
import org.opentravel.pubs.dao.RegistrantDAO;
import org.opentravel.pubs.forms.ArtifactCommentForm;
import org.opentravel.pubs.forms.RegistrantForm;
import org.opentravel.pubs.forms.RepositoryAccessForm;
import org.opentravel.pubs.forms.SchemaCommentForm;
import org.opentravel.pubs.forms.ViewSpecificationForm;
import org.opentravel.pubs.model.ArtifactComment;
import org.opentravel.pubs.model.CodeList;
import org.opentravel.pubs.model.CommentType;
import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationGroup;
import org.opentravel.pubs.model.PublicationItem;
import org.opentravel.pubs.model.PublicationItemType;
import org.opentravel.pubs.model.PublicationState;
import org.opentravel.pubs.model.PublicationType;
import org.opentravel.pubs.model.Registrant;
import org.opentravel.pubs.model.SchemaComment;
import org.opentravel.pubs.notification.EmailConfigBuilder;
import org.opentravel.pubs.notification.NotificationManager;
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
    public String specifications10PublicPage(Model model, HttpSession session,
            @RequestParam(value = "spec", required = false) String spec,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("specificationForm") ViewSpecificationForm specificationForm) {
    	
    	return doSpecificationPage( model, session, spec, newSession, specificationForm,
    			PublicationType.OTA_1_0, false, "specification10Main", "Specifications10.html" );
    }
    
    @RequestMapping({ "/Specifications20.html", "/Specifications20.htm" })
    public String specifications20PublicPage(Model model, HttpSession session,
            @RequestParam(value = "spec", required = false) String spec,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("specificationForm") ViewSpecificationForm specificationForm) {
    	
    	return doSpecificationPage( model, session, spec, newSession, specificationForm,
    			PublicationType.OTA_2_0, false, "specification20Main", "Specifications20.html" );
    }
    
    @RequestMapping({ "/members/Specifications.html", "/members/Specifications.htm",
    				  "/members/Specifications10.html", "/members/Specifications10.htm" })
    public String specifications10MemberPage(Model model, HttpSession session,
            @RequestParam(value = "spec", required = false) String spec,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("specificationForm") ViewSpecificationForm specificationForm) {
    	
    	return doSpecificationPage( model, session, spec, newSession, specificationForm,
    			PublicationType.OTA_1_0, true, "specification10MainMembers", "members/Specifications10.html" );
    }
    
    @RequestMapping({ "/members/Specifications20.html", "/members/Specifications20.htm" })
    public String specifications20MemberPage(Model model, HttpSession session,
            @RequestParam(value = "spec", required = false) String spec,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("specificationForm") ViewSpecificationForm specificationForm) {
    	
    	return doSpecificationPage( model, session, spec, newSession, specificationForm,
    			PublicationType.OTA_2_0, true, "specification20MainMembers", "members/Specifications20.html" );
    }
    
    @RequestMapping({ "/ReleaseNotes.html", "/ReleaseNotes.htm" })
    public String releaseNotesPublicPage(Model model, HttpSession session,
            @RequestParam(value = "spec", required = false) String spec,
    		@RequestParam(value = "specType", required = false) String specType) {
    	
    	return doReleaseNotesPage( model, session, spec, specType,
    			"specificationReleaseNotes", false );
    }
    
    @RequestMapping({ "/members/ReleaseNotes.html", "/members/ReleaseNotes.htm" })
    public String releaseNotesMemberPage(Model model, HttpSession session,
            @RequestParam(value = "spec", required = false) String spec,
    		@RequestParam(value = "specType", required = false) String specType) {
    	
    	return doReleaseNotesPage( model, session, spec, specType,
    			"specificationReleaseNotesMembers", true );
    }
    
    @RequestMapping({ "/CodeLists.html", "/CodeLists.htm" })
    public String codeListPublicPage(Model model, HttpSession session,
            @RequestParam(value = "releaseDate", required = false) String releaseDateStr,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("specificationForm") ViewSpecificationForm specificationForm) {
    	String targetPage = "codeListMain";
    	
    	try {
    		RegistrantForm registrantForm = specificationForm.getRegistrantForm();
        	CodeListDAO cDao = DAOFactoryManager.getFactory().newCodeListDAO();
        	CodeList codeList = null;
        	
        	if ((releaseDateStr != null) && (releaseDateStr.trim().length() > 0)) {
        		try {
            		codeList = cDao.getCodeList( CodeList.labelFormat.parse( releaseDateStr.trim() ) );
        			
        		} catch (ParseException e) {
        			setErrorMessage( "Invalid release date requested: \"" + releaseDateStr + "\"", model );
        		}
        	}
    		if (codeList == null) {
    			codeList = cDao.getLatestCodeList();
    		}
    		
        	model.addAttribute( "codeList", codeList );
        	model.addAttribute( "registrationPage", "CodeLists.html" );
        	
        	if (newSession) session.removeAttribute( "registrantId" );
        	handleRegistrantInfo( registrantForm, model, session );
        	
        	// If we processed the form successfully, clear our model so that its
        	// attributes will not show up as URL parameters on redirect
        	if (registrantForm.isProcessForm() && (model.asMap().get( "registrant" ) != null)) {
        		targetPage = "redirect:/specifications/CodeLists.html";
        		model.asMap().clear();
        	}
        	specificationForm.setProcessForm( true );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/Comment10Spec.html", "/Comment10Spec.htm" })
    public String comment10SpecPublicPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") SchemaCommentForm commentForm) {
    	
    	return doCommentSpecPage( model, session, redirectAttrs, newSession, commentForm,
    			PublicationType.OTA_1_0, false, "specificationComment10",
    			"/specifications/Comment10Spec.html" );
    }
    
    @RequestMapping({ "/Comment20Spec.html", "/Comment20Spec.htm" })
    public String comment20SpecPublicPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") SchemaCommentForm commentForm) {
    	
    	return doCommentSpecPage( model, session, redirectAttrs, newSession, commentForm,
    			PublicationType.OTA_2_0, false, "specificationComment20",
    			"/specifications/Comment20Spec.html" );
    }
    
    @RequestMapping({ "/members/Comment10Spec.html", "/members/Comment10Spec.htm" })
    public String comment10SpecMemberPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") SchemaCommentForm commentForm) {
    	
    	return doCommentSpecPage( model, session, redirectAttrs, newSession, commentForm,
    			PublicationType.OTA_1_0, true, "specificationComment10Members",
    			"/specifications/members/Comment10Spec.html" );
    }
    
    @RequestMapping({ "/members/Comment20Spec.html", "/members/Comment20Spec.htm" })
    public String comment20SpecMemberPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") SchemaCommentForm commentForm) {
    	
    	return doCommentSpecPage( model, session, redirectAttrs, newSession, commentForm,
    			PublicationType.OTA_2_0, true, "specificationComment20Members",
    			"/specifications/members/Comment20Spec.html" );
    }
    
    @RequestMapping({ "/Comment10Artifact.html", "/Comment10Artifact.html" })
    public String comment10ArtifactPublicPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") ArtifactCommentForm commentForm) {
    	
    	return doCommentArtifactPage( model, session, redirectAttrs, newSession, commentForm,
    			PublicationType.OTA_1_0, false, "artifactComment10",
    			"/specifications/Comment10Artifact.html" );
    }
    
    @RequestMapping({ "/Comment20Artifact.html", "/Comment20Artifact.htm" })
    public String comment20ArtifactPublicPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") ArtifactCommentForm commentForm) {
    	
    	return doCommentArtifactPage( model, session, redirectAttrs, newSession, commentForm,
    			PublicationType.OTA_2_0, false, "artifactComment20",
    			"/specifications/Comment20Artifact.html" );
    }
    
    @RequestMapping({ "/members/Comment10Artifact.html", "/members/Comment10Artifact.html" })
    public String comment10ArtifactMemberPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") ArtifactCommentForm commentForm) {
    	
    	return doCommentArtifactPage( model, session, redirectAttrs, newSession, commentForm,
    			PublicationType.OTA_1_0, true, "artifactComment10Members",
    			"/specifications/members/Comment10Artifact.html" );
    }
    
    @RequestMapping({ "/members/Comment20Artifact.html", "/members/Comment20Artifact.htm" })
    public String comment20ArtifactMemberPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
            @RequestParam(value = "newSession", required = false) boolean newSession,
            @ModelAttribute("commentForm") ArtifactCommentForm commentForm) {
    	
    	return doCommentArtifactPage( model, session, redirectAttrs, newSession, commentForm,
    			PublicationType.OTA_2_0, true, "artifactComment20Members",
    			"/specifications/members/Comment20Artifact.html" );
    }
    
    @RequestMapping({ "/CommentThankYou.html", "/CommentThankYou.htm" })
    public String commentThankYouPublicPage(Model model,
    		@RequestParam(value = "submitCommentsUrl", required = false) String submitCommentsUrl) {
    	model.addAttribute( "submitCommentsUrl", submitCommentsUrl );
    	return applyCommonValues( model, "commentThankYou" );
    }
    
    @RequestMapping({ "/members/CommentThankYou.html", "/members/CommentThankYou.htm" })
    public String commentThankYouMemberPage(Model model,
    		@RequestParam(value = "submitCommentsUrl", required = false) String submitCommentsUrl) {
    	model.addAttribute( "submitCommentsUrl", submitCommentsUrl );
    	return applyCommonValues( model, "commentThankYouMembers" );
    }
    
    @RequestMapping({ "/OTMRepositoryAccess.html", "/OTMRepositoryAccess.htm" })
    public String otmRepositoryAccessPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
    		@ModelAttribute("repositoryForm") RepositoryAccessForm repositoryForm) {
    	String targetPage = "otmRepositoryAccess";
    	
    	if (repositoryForm.isProcessForm()) {
    		ValidationResults vResults = ModelValidator.validate( repositoryForm );
    		
    		if (vResults.hasViolations()) {
    			addValidationErrors( vResults, model );
    			
    		} else {
    			try {
        			ApplicationSettingsDAO settingsDAO = DAOFactoryManager.getFactory().newApplicationSettingsDAO();
        			final Properties emailConfig = settingsDAO.getSettings( EmailConfigBuilder.EMAIL_CONFIG_SETTINGS );
    				InternetAddress recipient = new InternetAddress(
    						emailConfig.getProperty( EmailConfigBuilder.SENDER_ADDRESS_PROPERTY ), 
    						emailConfig.getProperty( EmailConfigBuilder.SENDER_NAME_PROPERTY ) );
        			Map<String,Object> contentMap = new HashMap<>();
        			
        			contentMap.put( "accessForm", repositoryForm );
        			NotificationManager.getInstance().sendNotification(
        					NotificationManager.OTM_REPOSITORY_SUBJECT_TEMPLATE,
        					NotificationManager.OTM_REPOSITORY_MESSAGE_TEMPLATE, contentMap, recipient );
        			targetPage = "redirect:/specifications/OTMRepositoryThankYou.html";
    				
    			} catch (Exception e) {
    				log.error("Error while attempting to send email notification.", e);
    	            setErrorMessage( "An error occurred while processing your request, please try again later.", model );
    			}
    		}
    		
    	} else {
    		repositoryForm.setProcessForm( true );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/OTMRepositoryThankYou.html", "/OTMRepositoryThankYou.htm" })
    public String otmRepositoryThankYouPage(Model model) {
    	return applyCommonValues( model, "otmRepositoryThankYou" );
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
    		
    		// This page is only available in the public area, so purge all member-review
    		// specifications from the list
    		purgeMemberReviewSpecifications( publications10 );
    		purgeMemberReviewSpecifications( publications20 );
    		
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
        	CodeListDAO cDao = DAOFactoryManager.getFactory().newCodeListDAO();
    		List<Publication> publications10 = pDao.getAllPublications( PublicationType.OTA_1_0 );
    		List<Publication> publications20 = pDao.getAllPublications( PublicationType.OTA_2_0 );
    		List<CodeList> codeLists = cDao.getAllCodeLists();
    		
    		// This page is only available in the public area, so purge all member-review
    		// specifications from the list
    		purgeMemberReviewSpecifications( publications10 );
    		purgeMemberReviewSpecifications( publications20 );
    		
    		// Remove the first element of each list since it is the current specification
    		if (!publications10.isEmpty()) {
    			publications10.remove( 0 );
    		}
    		if (!publications20.isEmpty()) {
    			publications20.remove( 0 );
    		}
    		if (!codeLists.isEmpty()) {
    			codeLists.remove( 0 );
    		}
        	model.addAttribute( "publications10", publications10 );
        	model.addAttribute( "publications20", publications20 );
        	model.addAttribute( "codeLists", codeLists );
        	
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
    public String downloadPublicationRegisterPage(Model model, HttpSession session,
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
    public String downloadPublication(Model model, HttpSession session, HttpServletRequest request,
    		HttpServletResponse response, RedirectAttributes redirectAttrs,
    		@PathVariable("pubName") String pubName, @PathVariable("pubType") String type,
    		@PathVariable("filename") String filename) {
    	String targetPath = null;
    	try {
    		Registrant registrant = getCurrentRegistrant( session );
    		
    		if (registrant == null) {
    			model.asMap().clear();
    			redirectAttrs.addAttribute( "pubName", pubName );
    			redirectAttrs.addAttribute( "pubType", type );
    			redirectAttrs.addAttribute( "filename", filename );
    			targetPath = "redirect:/specifications/DownloadRegister.html";
    			
    		} else {
    			targetPath = doPublicationDownload( model, session, registrant, request, response,
    					redirectAttrs, pubName, type, filename );
    		}
    		
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPath );
    }
    
    @RequestMapping({ "/downloads/noregister/{pubName}/{pubType}/{filename:.+}" })
    public String downloadPublicationNoRegistration(Model model, HttpSession session,
    		HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttrs,
    		@PathVariable("pubName") String pubName, @PathVariable("pubType") String type,
    		@PathVariable("filename") String filename) {
    	String targetPath = null;
		try {
    		Registrant registrant = getCurrentRegistrant( session );
    		
    		// There is no requirement to register for this download link.  If the user HAS already
    		// registered, however, we will associate this download with their registration identity.
			targetPath = doPublicationDownload( model, session, registrant, request, response,
					redirectAttrs, pubName, type, filename );
			
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
		}
    	return applyCommonValues( model, targetPath );
    }
    
    @RequestMapping({ "/cl/DownloadRegister.html", "/cl/DownloadRegister.htm" })
    public String downloadCodeListRegisterPage(Model model, HttpSession session,
    		@RequestParam(value = "releaseDate", required = true) String releaseDateStr,
    		@RequestParam(value = "filename", required = true) String filename,
    		@ModelAttribute("specificationForm") ViewSpecificationForm specificationForm) {
    	String targetPage = "downloadCodeListRegister";
    	try {
    		RegistrantForm registrantForm = specificationForm.getRegistrantForm();
        	CodeListDAO cDao = DAOFactoryManager.getFactory().newCodeListDAO();
    		try {
            	CodeList codeList = cDao.getCodeList( CodeList.labelFormat.parse( releaseDateStr ) );
            	
            	handleRegistrantInfo( registrantForm, model, session );
        		
            	// If we processed the form successfully, clear our model so that its
            	// attributes will not show up as URL parameters on redirect
            	if (registrantForm.isProcessForm() && (model.asMap().get( "registrant" ) != null)) {
            		targetPage = "redirect:/content/specifications/downloads/cl/" +
            				releaseDateStr + "/" + filename;
            		model.asMap().clear();
            		
            	} else {
            		model.addAttribute( "codeList", codeList );
            		model.addAttribute( "releaseDate", releaseDateStr );
            		model.addAttribute( "filename", filename );
            	}
            	specificationForm.setProcessForm( true );
            	
    		} catch (ParseException e) {
    			setErrorMessage( "Invalid release date requested: \"" + releaseDateStr + "\"", model );
    		}
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/downloads/cl/{releaseDate}/{filename:.+}" })
    public String downloadCodeList(Model model, HttpSession session, HttpServletRequest request,
    		HttpServletResponse response, RedirectAttributes redirectAttrs,
    		@PathVariable("releaseDate") String releaseDateStr, @PathVariable("filename") String filename) {
    	String targetPath = null;
    	try {
    		Registrant registrant = getCurrentRegistrant( session );
    		
    		if (registrant == null) {
    			model.asMap().clear();
    			redirectAttrs.addAttribute( "releaseDate", releaseDateStr );
    			redirectAttrs.addAttribute( "filename", filename );
    			targetPath = "redirect:/specifications/cl/DownloadRegister.html";
    			
    		} else {
    			targetPath = doCodeListDownload( model, session, registrant, request, response,
    					redirectAttrs, releaseDateStr, filename );
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
     * Performs a download of the publication or publication item content specified by the URL
     * parameters.
     * 
     * @param model  the UI model for the current request
     * @param session  the HTTP session
     * @param registrant  the web site registrant who is performing the download
     * @param request  the HTTP request for the content download
     * @param response  the HTTP response to which output should be directed
     * @param redirectAttrs  request attributes that must be available in case of a page redirect
     * @param pubName  the name of the publication from which content is being downloaded
     * @param type  the type of the publication
     * @param filename  the name of the content item that is being downloaded
     * @return String
     * @throws DAOException  thrown if an error occurs while accessing the content from persistent storage
     * @throws IOException  thrown if the content cannot be streamed to the HTTP response
     */
    protected String doPublicationDownload(Model model, HttpSession session, Registrant registrant,
    		HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttrs,
    		String pubName, String type, String filename) throws DAOException, IOException {
		InputStream contentStream = null;
    	String targetPath = null;
		
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
		return targetPath;
    }
    
    /**
     * Performs a download of the publication or publication item content specified by the URL
     * parameters.
     * 
     * @param model  the UI model for the current request
     * @param session  the HTTP session
     * @param registrant  the web site registrant who is performing the download
     * @param request  the HTTP request for the content download
     * @param response  the HTTP response to which output should be directed
     * @param redirectAttrs  request attributes that must be available in case of a page redirect
     * @param releaseDateStr  the release date of the code list archive being downloaded
     * @param filename  the name of the content item that is being downloaded
     * @return String
     * @throws DAOException  thrown if an error occurs while accessing the content from persistent storage
     * @throws IOException  thrown if the content cannot be streamed to the HTTP response
     */
    protected String doCodeListDownload(Model model, HttpSession session, Registrant registrant,
    		HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttrs,
    		String releaseDateStr, String filename) throws DAOException, IOException {
		InputStream contentStream = null;
    	String targetPath = null;
		
		if ((releaseDateStr != null) && (releaseDateStr.trim().length() > 0)) {
        	CodeListDAO cDao = DAOFactoryManager.getFactory().newCodeListDAO();
        	DownloadDAO dDao = DAOFactoryManager.getFactory().newDownloadDAO();
        	
        	try {
            	CodeList codeList = cDao.getCodeList( CodeList.labelFormat.parse( releaseDateStr ) );
    			
            	if (codeList != null) {
                	if (filename.equals( codeList.getArchiveFilename() )) {
                		contentStream = dDao.getArchiveContent( codeList, registrant );
                	}
            	}
            	
        	} catch (ParseException e) {
        		log.warn("Unreadable release date specified for code list download: \"" + releaseDateStr + "\"" );
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
		return targetPath;
    }
    
    /**
     * Hanldes the processing for all of the view-specification page targets.
     * 
     * @param model  the UI model for the current request
     * @param session  the HTTP session
     * @param spec  indicates the name of the specification to view
     * @param newSession  flag indicating that the user has requested to re-register in their session
     * @param specificationForm  the form used to supply content when a new user registers
     * @param pubType  the type of the publication to view (1.0/2.0)
     * @param isMembersOnlyPage  flag indicating whether the page being viewed is a members-only page
     * @param targetPage  the MVC name of the target page
     * @param altPageLocation  the name of the alternate page location in case of registration or redirect
     * @return String
     */
    private String doSpecificationPage(Model model, HttpSession session, String spec,
    		boolean newSession, ViewSpecificationForm specificationForm, PublicationType pubType,
    		boolean isMembersOnlyPage, String targetPage, String altPageLocation) {
    	try {
    		RegistrantForm registrantForm = specificationForm.getRegistrantForm();
        	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
        	Publication publication = null;
        	
        	if (spec != null) {
        		publication = pDao.getPublication( spec, pubType );
        	}
    		if (publication == null) {
    			publication = pDao.getLatestPublication(
    					pubType, getAllowedStates( isMembersOnlyPage ) );
    		}
    		
        	model.addAttribute( "publication", publication );
        	model.addAttribute( "registrationPage", altPageLocation );
        	model.addAttribute( "releaseNotesUrl", isMembersOnlyPage ?
        			"/specifications/members/ReleaseNotes.html" : "/specifications/ReleaseNotes.html" );
        	setupPublicationCheck( model, isMembersOnlyPage, pubType );
        	
        	if (newSession) session.removeAttribute( "registrantId" );
        	handleRegistrantInfo( registrantForm, model, session );
        	
        	// If we processed the form successfully, clear our model so that its
        	// attributes will not show up as URL parameters on redirect
        	if (registrantForm.isProcessForm() && (model.asMap().get( "registrant" ) != null)) {
        		targetPage = "redirect:/specifications/" + altPageLocation;
        		model.asMap().clear();
        	}
        	specificationForm.setProcessForm( true );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    /**
     * Hanldes the processing for all of the release notes page targets.
     * 
     * @param model  the UI model for the current request
     * @param session  the HTTP session
     * @param spec  indicates the name of the specification to view
     * @param specType  the type of the publication to view (1.0/2.0)
     * @param targetPage  the MVC name of the target page
     * @param isMembersOnlyPage  flag indicating whether the page being viewed is a members-only page
     * @return String
     */
    private String doReleaseNotesPage(Model model, HttpSession session, String spec, String specType,
    		String targetPage, boolean isMembersOnlyPage) {
    	try {
        	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
    		PublicationType pubType = resolvePublicationType( specType );
        	Publication publication = pDao.getPublication( spec, pubType );
    		Registrant registrant = getCurrentRegistrant( session );
    		
        	model.addAttribute( "publication", publication );
    		
    		if (registrant == null) {
    			if (pubType == PublicationType.OTA_1_0) {
        			targetPage = "specification10Main";
    			} else {
        			targetPage = "specification20Main";
    			}
    			if (isMembersOnlyPage) {
    				targetPage += "Members";
    			}
    		}
    		model.addAttribute( "releaseNotesText", getReleaseNotesText( publication ));
        	setupPublicationCheck( model, isMembersOnlyPage, pubType );
    		
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    /**
     * Hanldes the processing for all of the schema comment page targets.
     * 
     * @param model  the UI model for the current request
     * @param session  the HTTP session
     * @param redirectAttrs  request attributes that must be available in case of a page redirect
     * @param newSession  flag indicating that the user has requested to re-register in their session
     * @param commentForm  the form used to supply the field values for the submitted comment
     * @param pubType  the type of the publication to view (1.0/2.0)
     * @param isMembersOnlyPage  flag indicating whether the page being viewed is a members-only page
     * @param targetPage  the MVC name of the target page
     * @param submitCommentsUrl  the URL location of the comment submission page
     * @return String
     */
    private String doCommentSpecPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
    		boolean newSession, SchemaCommentForm commentForm, PublicationType pubType,
    		boolean isMembersOnlyPage, String targetPage, String submitCommentsUrl) {
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
        		if (isMembersOnlyPage) {
            		targetPage = "redirect:/specifications/members/CommentThankYou.html";
        		} else {
            		targetPage = "redirect:/specifications/CommentThankYou.html";
        		}
        		redirectAttrs.addAttribute( "submitCommentsUrl", submitCommentsUrl );
        		model.asMap().clear();
        		
        	} else {
            	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
            	Publication publication = pDao.getLatestPublication(
            			pubType, getAllowedStates( isMembersOnlyPage ) );
            	List<PublicationItem> publicationItems = new ArrayList<>();
            	
            	if (publication != null) {
                	publicationItems.addAll( pDao.getPublicationItems( publication, PublicationItemType.WSDL ) );
                	publicationItems.addAll( pDao.getPublicationItems( publication, PublicationItemType.XML_SCHEMA ) );
                	publicationItems.addAll( pDao.getPublicationItems( publication, PublicationItemType.JSON_SCHEMA ) );
            	}
            	
        		model.addAttribute( "publication", publication );
        		model.addAttribute( "publicationItems", publicationItems );
        		model.addAttribute( "commentTypes", Arrays.asList( CommentType.values() ) );
            	setupPublicationCheck( model, isMembersOnlyPage, pubType );
        	}
    		model.addAttribute( "submitCommentsUrl", submitCommentsUrl );
        	commentForm.setProcessForm( true );
    		
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    /**
     * Hanldes the processing for all of the artifact comment page targets.
     * 
     * @param model  the UI model for the current request
     * @param session  the HTTP session
     * @param redirectAttrs  request attributes that must be available in case of a page redirect
     * @param newSession  flag indicating that the user has requested to re-register in their session
     * @param commentForm  the form used to supply the field values for the submitted comment
     * @param pubType  the type of the publication to view (1.0/2.0)
     * @param isMembersOnlyPage  flag indicating whether the page being viewed is a members-only page
     * @param targetPage  the MVC name of the target page
     * @param submitCommentsUrl  the URL location of the comment submission page
     * @return String
     */
    private String doCommentArtifactPage(Model model, HttpSession session, RedirectAttributes redirectAttrs,
    		boolean newSession, ArtifactCommentForm commentForm, PublicationType pubType,
    		boolean isMembersOnlyPage, String targetPage, String submitCommentsUrl) {
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
        		if (isMembersOnlyPage) {
            		targetPage = "redirect:/specifications/members/CommentThankYou.html";
        		} else {
            		targetPage = "redirect:/specifications/CommentThankYou.html";
        		}
        		redirectAttrs.addAttribute( "submitCommentsUrl", submitCommentsUrl );
        		model.asMap().clear();
        		
        	} else {
            	PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
            	Publication publication = pDao.getLatestPublication(
            			pubType, getAllowedStates( isMembersOnlyPage ) );
            	List<PublicationItem> publicationItems = new ArrayList<>();
            	
            	if (publication != null) {
            		publicationItems.addAll( pDao.getPublicationItems( publication, PublicationItemType.ARTIFACT ) );
            	}
        		model.addAttribute( "publication", publication );
        		model.addAttribute( "publicationItems", publicationItems );
        		model.addAttribute( "commentTypes", Arrays.asList( CommentType.values() ) );
            	setupPublicationCheck( model, isMembersOnlyPage, pubType );
        	}
    		model.addAttribute( "submitCommentsUrl", submitCommentsUrl );
        	commentForm.setProcessForm( true );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    /**
     * Returns an array of allowed publication states depending upon whether the desired
     * viewing should be publicly available or a members-only.
     * 
     * @param isMembersOnly  flag indicating whether the desired viewing is members-only
     * @return PublicationState[]
     */
    private PublicationState[] getAllowedStates(boolean isMembersOnly) {
    	PublicationState[] allowedStates;
    	
    	if (isMembersOnly) {
    		allowedStates = new PublicationState[] { PublicationState.MEMBER_REVIEW };
    		
    	} else {
    		allowedStates = new PublicationState[] {
    				PublicationState.PUBLIC_REVIEW, PublicationState.RELEASED };
    	}
    	return allowedStates;
    }
    
    /**
     * Adds the required model attributes for the 'publicationCheck.jsp' page verification.
     * 
     * @param model  the UI model for the current request
     * @param isMembersOnlyPage  flag indicating whether the page being viewed is a members-only page
     * @param expectedPubType  the type of publication that expected to be available 
     */
    private void setupPublicationCheck(Model model, boolean isMembersOnly, PublicationType expectedPubType) {
    	model.addAttribute( "isMembersOnly", isMembersOnly );
    	model.addAttribute( "expectedPubType", expectedPubType );
    }
    
    /**
     * Removes all publications assigned to the <code>MEMBER_REVIEW</code> state from
     * the given list.
     * 
     * @param specList  the list of publications to process
     */
    private void purgeMemberReviewSpecifications(List<Publication> specList) {
    	Iterator<Publication> iterator = specList.iterator();
    	
    	while (iterator.hasNext()) {
    		Publication p = iterator.next();
    		
    		if (p.getState() == PublicationState.MEMBER_REVIEW) {
    			iterator.remove();
    		}
    	}
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
