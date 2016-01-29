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

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.opentravel.pubs.builders.PublicationBuilder;
import org.opentravel.pubs.dao.AdminDAO;
import org.opentravel.pubs.dao.CommentDAO;
import org.opentravel.pubs.dao.DAOFactoryManager;
import org.opentravel.pubs.dao.DateRangeType;
import org.opentravel.pubs.dao.DownloadDAO;
import org.opentravel.pubs.dao.DownloadHistoryItem;
import org.opentravel.pubs.dao.PublicationDAO;
import org.opentravel.pubs.dao.RegistrantDAO;
import org.opentravel.pubs.model.AdminCredentials;
import org.opentravel.pubs.model.Comment;
import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationState;
import org.opentravel.pubs.model.PublicationType;
import org.opentravel.pubs.model.Registrant;
import org.opentravel.pubs.util.StringUtils;
import org.opentravel.pubs.util.TypeChecker;
import org.opentravel.pubs.util.ValueFormatter;
import org.opentravel.pubs.validation.ModelValidator;
import org.opentravel.pubs.validation.ValidationException;
import org.opentravel.pubs.validation.ValidationResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller that handles interactions with the administration pages of the OpenTravel
 * specification site.
 *
 * @author S. Livezey
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
	
    private static final Logger log = LoggerFactory.getLogger( AdminController.class );
    
    @RequestMapping({ "/index.html", "/index.htm" })
    public String adminHomePage(HttpSession session, Model model) {
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
    	return applyCommonValues( model, "adminMain" );
    }

    @RequestMapping({ "/UploadSpecification.html", "/UploadSpecification.htm" })
    public String uploadSpecificationPage(HttpSession session, Model model) {
    	try {
    		model.addAttribute( "publicationStates", Arrays.asList( PublicationState.values() ) );
    		
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, "uploadSpecification" );
    }
    
    @RequestMapping({ "/DoUploadSpecification.html", "/DoUploadSpecification.htm" })
    public String doUploadSpecificationPage(HttpSession session, Model model, RedirectAttributes redirectAttrs,
            @RequestParam(value = "processUpload", required = false) boolean processUpload,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "specType", required = false) String specType,
            @RequestParam(value = "pubState", required = false) String pubState,
    		@RequestParam(value = "archiveFile", required = false) MultipartFile archiveFile) {
    	String targetPage = "uploadSpecification";
    	try {
    		boolean success = false;
    		
    		if (processUpload) {
        		PublicationType publicationType = resolvePublicationType( specType );
        		PublicationState publicationState = (pubState == null) ? null : PublicationState.valueOf( pubState );
    			Publication publication = new PublicationBuilder()
    					.setName( StringUtils.trimString( name ) )
    					.setType( publicationType )
    					.setState( publicationState )
    					.build();
    			
    			try {
            		if (!archiveFile.isEmpty()) {
        				DAOFactoryManager.getFactory().newPublicationDAO()
        						.publishSpecification( publication, archiveFile.getInputStream() );
        				
        				model.asMap().clear();
        				redirectAttrs.addAttribute( "publication", publication.getName() );
        				redirectAttrs.addAttribute( "specType", publication.getType() );
            			targetPage = "redirect:/admin/ViewSpecification.html";
            			
            		} else {
            			ValidationResults vResults = ModelValidator.validate( publication );
            			
            			if (vResults.hasViolations()) {
            				throw new ValidationException( vResults );
            			}
            		}
    				
    			} catch (ValidationException e) {
    	    		addValidationErrors( e, model );
    				
    			} catch (Throwable t) {
    				log.error("An error occurred while publishing the spec: ", t);
    				setErrorMessage( t.getMessage(), model );
    			}
    		}
    		
    		if (!success) {
        		model.addAttribute( "name", name );
        		model.addAttribute( "specType", specType );
        		model.addAttribute( "pubState", pubState );
        		model.addAttribute( "publicationStates", Arrays.asList( PublicationState.values() ) );
    		}
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }

    @RequestMapping({ "/ViewSpecification.html", "/ViewSpecification.htm" })
    public String viewSpecificationPage(HttpSession session, Model model,
    		 @RequestParam(value = "publication", required = false) String name,
    		 @RequestParam(value = "specType", required = false) String specType) {
    	String targetPage = "viewSpecification";
    	try {
    		Publication publication = null;
    		
    		if ((name != null) && (specType != null)) {
    			PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
        		PublicationType publicationType = resolvePublicationType( specType );
    			
    			publication = pDao.getPublication( name, publicationType );
    			model.addAttribute( "publication", publication );
    			model.addAttribute( "formatter", ValueFormatter.getInstance() );
    		}
    		
    		if (publication == null) {
    			targetPage = "redirect:/admin/index.html";
    		}
    		
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/UpdateSpecification.html", "/UpdateSpecification.htm" })
    public String updateSpecificationPage(HttpSession session, Model model,
            @RequestParam(value = "publicationId", required = false) Long publicationId) {
    	String targetPage = "updateSpecification";
    	try {
			PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
			Publication publication = pDao.getPublication( publicationId );
			
    		model.addAttribute( "publication", publication );
    		model.addAttribute( "name", publication.getName() );
    		model.addAttribute( "specType", publication.getType() );
    		model.addAttribute( "pubState", publication.getState() );
    		model.addAttribute( "publicationStates", Arrays.asList( PublicationState.values() ) );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }

    @RequestMapping({ "/DoUpdateSpecification.html", "/DoUpdateSpecification.htm" })
    public String doUpdateSpecificationPage(HttpSession session, Model model, RedirectAttributes redirectAttrs,
            @RequestParam(value = "processUpdate", required = false) boolean processUpdate,
            @RequestParam(value = "publicationId", required = false) Long publicationId,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "specType", required = false) String specType,
            @RequestParam(value = "pubState", required = false) String pubState,
    		@RequestParam(value = "archiveFile", required = false) MultipartFile archiveFile) {
    	String targetPage = "updateSpecification";
    	try {
			PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
			Publication publication = pDao.getPublication( publicationId );
    		boolean success = false;
    		
    		if (processUpdate) {
        		PublicationType publicationType = resolvePublicationType( specType );
        		PublicationState publicationState = (pubState == null) ? null : PublicationState.valueOf( pubState );
    			
    			publication.setName( StringUtils.trimString( name ) );
    			publication.setType( publicationType );
    			publication.setState( publicationState );
    			
    			try {
        			ValidationResults vResults = ModelValidator.validate( publication );
        			
        			// Before we try to update the contents of the spefication, validate the
        			// publication object to see if there are any errors.
        			if (vResults.hasViolations()) {
        				throw new ValidationException( vResults );
        			}
        			
            		if (!archiveFile.isEmpty()) {
        				pDao.updateSpecification( publication, archiveFile.getInputStream() );
            		}
    				model.asMap().clear();
    				redirectAttrs.addAttribute( "publication", publication.getName() );
    				redirectAttrs.addAttribute( "specType", publication.getType() );
        			targetPage = "redirect:/admin/ViewSpecification.html";
    				success = true;
    				
    			} catch (ValidationException e) {
    	    		addValidationErrors( e, model );
    				
    			} catch (Throwable t) {
    				log.error("An error occurred while updating the spec: ", t);
    				setErrorMessage( t.getMessage(), model );
    			}
    		}
    		
    		if (!success) {
        		model.addAttribute( "publication", publication );
        		model.addAttribute( "name", name );
        		model.addAttribute( "specType", specType );
        		model.addAttribute( "pubState", pubState );
        		model.addAttribute( "publicationStates", Arrays.asList( PublicationState.values() ) );
    		}
    		
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }
    
    @RequestMapping({ "/DeleteSpecification.html", "/DeleteSpecification.htm" })
    public String deleteSpecificationPage(HttpSession session, Model model,
            @RequestParam(value = "confirmDelete", required = false) boolean confirmDelete,
            @RequestParam(value = "publicationId", required = false) Long publicationId) {
    	String targetPage = "deleteSpecification";
    	try {
			PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
			Publication publication = pDao.getPublication( publicationId );
			
			if (confirmDelete) {
				pDao.deleteSpecification( publication );
    			targetPage = "redirect:/admin/index.html";
				model.asMap().clear();
				
			} else {
	    		model.addAttribute( "publication", publication );
			}
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }

    @RequestMapping({ "/SpecificationComments.html", "/SpecificationComments.htm" })
    public String specificationCommentsPage(HttpSession session, Model model,
            @RequestParam(value = "publication", required = false) String name,
            @RequestParam(value = "specType", required = false) String specType,
            @RequestParam(value = "dateRange", required = false) DateRangeType dateRange) {
    	try {
			PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
			CommentDAO cDao = DAOFactoryManager.getFactory().newCommentDAO();
    		PublicationType publicationType = resolvePublicationType( specType );
			Publication publication = pDao.getPublication( name, publicationType );
			List<Comment> commentList;
			
    		if (dateRange == null) {
    			dateRange = DateRangeType.LAST_WEEK;
    		}
    		commentList = cDao.findComments( publication, dateRange );
    		
    		model.addAttribute( "publication", publication );
    		model.addAttribute( "commentList", commentList );
    		model.addAttribute( "dateRange", dateRange );
    		model.addAttribute( "dateRanges", Arrays.asList( DateRangeType.values() ) );
			model.addAttribute( "formatter", ValueFormatter.getInstance() );
			model.addAttribute( "typeChecker", TypeChecker.getInstance() );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, "specificationComments" );
    }

    @RequestMapping({ "/SpecificationDownloads.html", "/SpecificationDownloads.htm" })
    public String specificationDownloadsPage(HttpSession session, Model model,
            @RequestParam(value = "publication", required = false) String name,
            @RequestParam(value = "specType", required = false) String specType,
            @RequestParam(value = "dateRange", required = false) DateRangeType dateRange) {
    	try {
			PublicationDAO pDao = DAOFactoryManager.getFactory().newPublicationDAO();
			DownloadDAO dDao = DAOFactoryManager.getFactory().newDownloadDAO();
    		PublicationType publicationType = resolvePublicationType( specType );
			Publication publication = pDao.getPublication( name, publicationType );
			List<DownloadHistoryItem> downloadHistory;
			
    		if (dateRange == null) {
    			dateRange = DateRangeType.LAST_WEEK;
    		}
    		downloadHistory = dDao.getDownloadHistory( publication, dateRange );
    		
    		model.addAttribute( "publication", publication );
    		model.addAttribute( "downloadHistory", downloadHistory );
    		model.addAttribute( "dateRange", dateRange );
    		model.addAttribute( "publicationStates", Arrays.asList( PublicationState.values() ) );
    		model.addAttribute( "dateRanges", Arrays.asList( DateRangeType.values() ) );
			model.addAttribute( "formatter", ValueFormatter.getInstance() );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, "specificationDownloads" );
    }

    @RequestMapping({ "/ViewRegistrants.html", "/ViewRegistrants.htm" })
    public String viewRegistrantsPage(HttpSession session, Model model,
            @RequestParam(value = "dateRange", required = false) DateRangeType dateRange) {
    	try {
        	RegistrantDAO rDao = DAOFactoryManager.getFactory().newRegistrantDAO();
        	List<Registrant> registrantList;
        	
    		if (dateRange == null) {
    			dateRange = DateRangeType.LAST_WEEK;
    		}
    		registrantList = rDao.findRegistrants( dateRange );
    		
    		model.addAttribute( "registrantList", registrantList );
    		model.addAttribute( "dateRange", dateRange );
    		model.addAttribute( "dateRanges", Arrays.asList( DateRangeType.values() ) );
			model.addAttribute( "formatter", ValueFormatter.getInstance() );
			
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, "viewRegistrants" );
    }

    @RequestMapping({ "/RegistrantDetails.html", "/RegistrantDetails.htm" })
    public String registrantViewPage(HttpSession session, Model model,
            @RequestParam(value = "rid", required = false) Long registrantId) {
    	try {
        	RegistrantDAO rDao = DAOFactoryManager.getFactory().newRegistrantDAO();
        	Registrant registrant = rDao.getRegistrant( registrantId );
        	
    		model.addAttribute( "registrant", registrant );
			model.addAttribute( "formatter", ValueFormatter.getInstance() );
			model.addAttribute( "typeChecker", TypeChecker.getInstance() );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, "registrantView" );
    }

    @RequestMapping({ "/RegistrantDownloads.html", "/RegistrantDownloads.htm" })
    public String registrantDownloadsPage(HttpSession session, Model model,
            @RequestParam(value = "rid", required = false) Long registrantId) {
    	try {
        	RegistrantDAO rDao = DAOFactoryManager.getFactory().newRegistrantDAO();
        	Registrant registrant = rDao.getRegistrant( registrantId );
        	
    		model.addAttribute( "registrant", registrant );
			model.addAttribute( "formatter", ValueFormatter.getInstance() );
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, "registrantDownloads" );
    }

    @RequestMapping({ "/ChangeAdminCredentials.html", "/ChangeAdminCredentials.htm" })
    public String changeCredentialsPage(HttpSession session, Model model,
            @RequestParam(value = "processUpdate", required = false) boolean processUpdate,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "password", required = false) String password) {
    	String targetPage = "changeCredentials";
    	try {
    		AdminDAO aDao = DAOFactoryManager.getFactory().newAdminDAO();
    		boolean success = false;
    		
        	if (processUpdate) {
        		try {
            		AdminCredentials credentials = new AdminCredentials();
            		
            		credentials.setUserId( StringUtils.trimString( userId ) );
            		credentials.setPassword( StringUtils.trimString( password ) );
            		aDao.updateAdminCredentials( credentials );
            		
            		success = true;
            		model.asMap().clear();
            		targetPage = "redirect:/admin/index.html";
        			
        		} catch (ValidationException e) {
            		addValidationErrors( e, model );
        		}
        		
        	} else if (userId == null) {
        		userId = aDao.getAdminCredentials().getUserId();
        	}
        	
        	if (!success) {
        		model.addAttribute( "userId", userId );
        		model.addAttribute( "password", password );
        	}
        	
    	} catch (Throwable t) {
    		log.error("Error during publication controller processing.", t);
            setErrorMessage( DEFAULT_ERROR_MESSAGE, model );
    	}
    	return applyCommonValues( model, targetPage );
    }

	/**
	 * @see org.opentravel.pubs.controllers.BaseController#applyCommonValues(org.springframework.ui.Model, java.lang.String)
	 */
	@Override
	protected String applyCommonValues(Model model, String targetPage) {
		super.applyCommonValues(model, targetPage);
        return (targetPage == null) ? "adminMain" : targetPage;
	}
    
}
