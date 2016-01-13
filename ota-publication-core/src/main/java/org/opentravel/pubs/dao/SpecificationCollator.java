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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;

import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationGroup;
import org.opentravel.pubs.model.PublicationItem;
import org.opentravel.pubs.model.PublicationItemType;
import org.opentravel.pubs.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class that separates and organizes the various publication items into groups
 * during the creation of a specification data set.
 * 
 * @author S. Livezey
 */
class SpecificationCollator {
	
	private static final String XML_SCHEMAS_GROUP_NAME   = "XML Schemas";
	private static final String WSDL_GROUP_NAME          = "WSDL";
	private static final String JSON_SCHEMAS_GROUP_NAME  = "JSON Schemas";
	private static final String GEN_ARTIFACTS_GROUP_NAME = "General Artifacts";
	
    private static final Logger log = LoggerFactory.getLogger( SpecificationCollator.class );
    
	private Publication publication;
	private PublicationGroup xmlSchemasGroup = new PublicationGroup();
	private PublicationGroup wsdlGroup = new PublicationGroup();
	private PublicationGroup jsonSchemasGroup = new PublicationGroup();
	private PublicationGroup generalArtifactsGroup = new PublicationGroup();
	private Map<String,PublicationGroup> otherGroups = new HashMap<>();
	private Map<String,List<String>> groupEntries = new HashMap<>();
	
	private Map<String,PublicationItem> existingPublicationItems = new HashMap<>();
	private Set<String> processedItemFilenames = new HashSet<>();
	
	/**
	 * Constructor that specifies the publication with which all groups will be
	 * associated.
	 * 
	 * @param publication  the publication with which all groups will be associated
	 */
	public SpecificationCollator(Publication publication) {
		init( publication );
	}
	
	/**
	 * Returns a sorted list of <code>PublicationGroup</code> objects.
	 * 
	 * @return List<PublicationGroup>
	 */
	public List<PublicationGroup> getGroups() {
		List<PublicationGroup> groupList = new ArrayList<>();
		List<PublicationGroup> miscGroups = new ArrayList<>();
		PublicationGroup otherGroup = null;
		
		// Sort the 'otherGroups' in an appropriate order
		for (PublicationGroup group : otherGroups.values()) {
			if (group.getName().equalsIgnoreCase("Other")) {
				otherGroup = group;
				
			} else {
				miscGroups.add( group );
			}
		}
		Collections.sort( miscGroups, new GroupComparator() );
		
		// Add all non-empty groups to the final list
		addNonEmptyGroup( xmlSchemasGroup, groupList );
		addNonEmptyGroup( wsdlGroup, groupList );
		addNonEmptyGroup( jsonSchemasGroup, groupList );
		addNonEmptyGroup( generalArtifactsGroup, groupList );
		
		for (PublicationGroup miscGroup : miscGroups) {
			addNonEmptyGroup( miscGroup, groupList );
		}
		addNonEmptyGroup( otherGroup, groupList );
		
		// Assign the 'sortOrder' value for each group in the list
		for (int i = 0; i < groupList.size(); i++) {
			groupList.get( i ).setSortOrder( i );
		}
		return groupList;
	}
	
	/**
	 * Adds the given group to the group list if it is not empty.  Otherwise, the group
	 * is marked as 'removed'.
	 * 
	 * @param group  the group to be added
	 * @param groupList  the list that will recive the non-empty group
	 */
	private void addNonEmptyGroup(PublicationGroup group, List<PublicationGroup> groupList) {
		if (group != null) {
			if (!group.getPublicationItems().isEmpty()) {
				groupList.add( group );
				
			} else {
				group.setRemoved( true );
				group.setSortOrder( 99 );
			}
		}
	}
	
	/**
	 * Returns the list of all publication items that were members of the original
	 * publication, but were not processed by this collator during the load.
	 * 
	 * @return List<PublicationItem>
	 */
	public List<PublicationItem> getDeletedItems() {
		List<PublicationItem> deletedItems = new ArrayList<>();
		
		for (String filename : existingPublicationItems.keySet()) {
			if (!processedItemFilenames.contains( filename )) {
				deletedItems.add( existingPublicationItems.get( filename ) );
			}
		}
		return deletedItems;
	}
	
	/**
	 * Returns true if the given <code>ZipEntry</code> contains the release notes
	 * for the publication.
	 * 
	 * @param entry  the zip entry to analyze
	 * @return boolean
	 */
	public boolean isReleaseNotes(ZipEntry entry) {
		String groupName = getGroupName( entry );
		String filename = getFilename( entry );
		
		return (groupName == null) && isReleaseNotes( filename );
	}
	
	/**
	 * If the entry is a valid publication item, it is added to the appropriate group and
	 * a new <code>PublicationItem</code> is returned.  If the entry is not a valid publication
	 * item, this method will return null.
	 * 
	 * @param entry  the zip entry that specifies the group publication item name
	 * @return PublicationItem
	 */
	public PublicationItem addItem(ZipEntry entry) {
		return addArchiveItem( entry, entry ); // not an archive, but the algorithm works the same
	}
	
	/**
	 * Returns true if the given entry represents a special case for archive entries.  A
	 * special case exists if the entry is a zip archive that is the only file contained
	 * within its publication group.
	 * 
	 * @param entry  the zip entry to analyze
	 * @return boolean
	 */
	public boolean isSpecialCaseArchive(ZipEntry entry) {
		String groupName = getGroupName( entry );
		List<String> entryFilenames = groupEntries.get( groupName );
		boolean specialCase = false;
		
		if ((entryFilenames != null) && (entryFilenames.size() == 1)) {
			specialCase = isArchiveArtifact( entryFilenames.get( 0 ) );
		}
		return specialCase;
	}
	
	/**
	 * Adds the given special-case archive entry to the collection of groups.
	 * 
	 * @param publicationEntry  the publication entry that specifies the name of the group
	 * @param archiveEntry  the archive entry that specifies the name of the publication item to add
	 * @return PublicationItem
	 */
	public PublicationItem addArchiveItem(ZipEntry publicationEntry, ZipEntry archiveEntry) {
		String groupName = getGroupName( publicationEntry );
		String filename = getFilename( archiveEntry );
		PublicationItem pubItem = existingPublicationItems.get( filename );
		
		// Construct a new publication item as long as it is not an archive (zip) entry
		if (processedItemFilenames.contains( filename )) {
			log.warn("The archive has already published a file with the name '" + filename + "' (skipping)");
			
		} else if (!isArchiveArtifact( filename ) && !isExcludedDocument( filename )) {
			PublicationGroup group = null;
			
			if (pubItem == null) {
				pubItem = new PublicationItem();
				pubItem.setItemFilename( filename );
			}
			pubItem.setFileSize( archiveEntry.getSize() );
			pubItem.setCreateDate( new Date( archiveEntry.getTime() ) );
			
			if (isXmlSchema( filename )) {
				group = xmlSchemasGroup;
				pubItem.setType( PublicationItemType.XML_SCHEMA );
				
			} else if (isWsdlDocument( filename )) {
				group = wsdlGroup;
				pubItem.setType( PublicationItemType.WSDL );
				
			} else if (isJsonSchema( filename )) {
				group = jsonSchemasGroup;
				pubItem.setType( PublicationItemType.JSON_SCHEMA );
				
			} else if (groupName == null) {
				group = generalArtifactsGroup;
				pubItem.setType( PublicationItemType.ARTIFACT );
				
			} else {
				group = otherGroups.get( groupName );
				
				if (group == null) {
					group = newGroup( publication, groupName, PublicationItemType.ARTIFACT );
					otherGroups.put( groupName, group );
				}
				pubItem.setType( PublicationItemType.ARTIFACT );
			}
			
			if (!group.getPublicationItems().contains( pubItem )) {
				PublicationGroup existingOwner = pubItem.getOwner();
				
				if ((existingOwner != null) && (existingOwner != group)) { // item was moved to a new group
					pubItem.getOwner().getPublicationItems().remove( pubItem );
				}
				group.getPublicationItems().add( pubItem );
				pubItem.setOwner( group );
			}
			processedItemFilenames.add( filename );
		}
		
		// Add the archive entry to the 'groupEntries' (non-special case only)
		if ((publicationEntry == archiveEntry) && (groupName != null)) {
			List<String> entryFilenames = groupEntries.get( groupName );
			
			if (entryFilenames == null) {
				entryFilenames = new ArrayList<>();
				groupEntries.put( groupName, entryFilenames );
			}
			entryFilenames.add( filename );
		}
		return pubItem;
	}
	
	/**
	 * Returns the name of the publication item group or null if the entry does not specify
	 * a group name.
	 * 
	 * @param archiveEntry  the zip archive entry for which to return a group name
	 * @return String
	 */
	private String getGroupName(ZipEntry archiveEntry) {
		String[] nameParts = archiveEntry.getName().split( "/" );
		String groupName = null;
		
		if (nameParts.length > 1) {
			groupName = nameParts[0];
			
			if (groupName.toLowerCase().startsWith("opentravel_")) {
				groupName = groupName.substring( 11 );
			}
			groupName = StringUtils.toDisplayString( groupName );
		}
		return groupName;
	}
	
	/**
	 * Returns only the filename from the given zip entry path name.
	 * 
	 * @param archiveEntry  the zip archive entry for which to return a filename
	 * @return String
	 */
	public String getFilename(ZipEntry archiveEntry) {
		String itemPath = archiveEntry.getName();
		int slashIdx = itemPath.lastIndexOf( '/' );
		String itemFilename;
		
		if (slashIdx >= itemPath.length()) {
			itemFilename = "";
		} else {
			itemFilename = (slashIdx < 0) ? itemPath : itemPath.substring( slashIdx + 1 );
		}
		return itemFilename;
	}
	
	/**
	 * Returns true if the given filename represents an XML schema.
	 * 
	 * @param filename  the filename to analyze
	 * @return boolean
	 */
	private boolean isXmlSchema(String filename) {
		return filename.toLowerCase().endsWith( ".xsd" );
	}
	
	/**
	 * Returns true if the given filename represents WSDL document.
	 * 
	 * @param filename  the filename to analyze
	 * @return boolean
	 */
	private boolean isWsdlDocument(String filename) {
		return filename.toLowerCase().endsWith( ".wsdl" );
	}
	
	/**
	 * Returns true if the given filename represents an artifact that should be
	 * excluded from the archive.
	 * 
	 * @param filename  the filename to analyze
	 * @return boolean
	 */
	private boolean isExcludedDocument(String filename) {
		String lcFilename = filename.toLowerCase();
		return lcFilename.startsWith(".") || lcFilename.endsWith( ".xml" );
	}
	
	/**
	 * Returns true if the given filename represents a JSON schema.
	 * 
	 * @param filename  the filename to analyze
	 * @return boolean
	 */
	private boolean isJsonSchema(String filename) {
		return filename.toLowerCase().endsWith( ".schema.json" );
	}
	
	/**
	 * Returns true if the given filename represents the publication release notes.
	 * 
	 * @param filename  the filename to analyze
	 * @return boolean
	 */
	private boolean isReleaseNotes(String filename) {
		String lcFilename = filename.toLowerCase();
		
		return lcFilename.endsWith( ".txt" ) && (lcFilename.indexOf("readme") >= 0);
	}
	
	/**
	 * Returns true if the given filename represents a non-schema artifact.
	 * 
	 * @param filename  the filename to analyze
	 * @return boolean
	 */
	private boolean isArchiveArtifact(String filename) {
		return filename.toLowerCase().endsWith( ".zip" );
	}
	
	/**
	 * Initializes this collator using the information provided.
	 * 
	 * @param publication
	 */
	private void init(Publication publication) {
		this.publication = publication;
		
		if (publication.getId() < 0L) { // new publication instance
			this.xmlSchemasGroup = newGroup( publication, XML_SCHEMAS_GROUP_NAME, PublicationItemType.XML_SCHEMA );
			this.wsdlGroup = newGroup( publication, WSDL_GROUP_NAME, PublicationItemType.WSDL );
			this.jsonSchemasGroup = newGroup( publication, JSON_SCHEMAS_GROUP_NAME, PublicationItemType.JSON_SCHEMA );
			this.generalArtifactsGroup = newGroup( publication, GEN_ARTIFACTS_GROUP_NAME, PublicationItemType.ARTIFACT );
			
		} else { // update of an existing publication
			for (PublicationGroup existingGroup : publication.getPublicationGroups()) {
				if (!existingGroup.isRemoved()) {
					for (PublicationItem existingItem : existingGroup.getPublicationItems()) {
						if (!existingItem.isRemoved()) {
							existingPublicationItems.put( existingItem.getItemFilename(), existingItem );
						}
					}
					this.otherGroups.put( existingGroup.getName(), existingGroup );
				}
			}
			
			if (this.otherGroups.containsKey( XML_SCHEMAS_GROUP_NAME )) {
				this.xmlSchemasGroup = this.otherGroups.remove( XML_SCHEMAS_GROUP_NAME );
			} else {
				this.xmlSchemasGroup = newGroup( publication, XML_SCHEMAS_GROUP_NAME, PublicationItemType.XML_SCHEMA );
			}
			
			if (this.otherGroups.containsKey( WSDL_GROUP_NAME )) {
				this.wsdlGroup = this.otherGroups.remove( WSDL_GROUP_NAME );
			} else {
				this.wsdlGroup = newGroup( publication, WSDL_GROUP_NAME, PublicationItemType.WSDL );
			}
			
			if (this.otherGroups.containsKey( JSON_SCHEMAS_GROUP_NAME )) {
				this.jsonSchemasGroup = this.otherGroups.remove( JSON_SCHEMAS_GROUP_NAME );
			} else {
				this.jsonSchemasGroup = newGroup( publication, JSON_SCHEMAS_GROUP_NAME, PublicationItemType.JSON_SCHEMA );
			}
			
			if (this.otherGroups.containsKey( GEN_ARTIFACTS_GROUP_NAME )) {
				this.generalArtifactsGroup = this.otherGroups.remove( GEN_ARTIFACTS_GROUP_NAME );
			} else {
				this.generalArtifactsGroup = newGroup( publication, GEN_ARTIFACTS_GROUP_NAME, PublicationItemType.ARTIFACT );
			}
		}
	}
	
	/**
	 * Creates a new <code>PublicationGroup</code> using the information provided.
	 * 
	 * @param p  the owner of the new group
	 * @param name  the name of the new group
	 * @param memberType  the member type for the new group
	 * @return PublicationGroup
	 */
	private PublicationGroup newGroup(Publication p, String name, PublicationItemType memberType) {
		PublicationGroup newGroup = new PublicationGroup();
		
		newGroup.setName( name );
		newGroup.setOwner( publication );
		newGroup.setMemberType( memberType );
		newGroup.setPublicationItems( new ArrayList<PublicationItem>() );
		return newGroup;
	}
	
	/**
	 * Comparator for sorting lists of <code>PublicationGroup</code> objects.
	 */
	private static class GroupComparator implements Comparator<PublicationGroup> {
		
		/**
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(PublicationGroup g1, PublicationGroup g2) {
			int result;
			
			if (g1 == null) {
				result = (g2 == null) ? 0 : -1;
			} else if (g2 == null) {
				result = 1;
			} else {
				String groupName1 = g1.getName();
				String groupName2 = g2.getName();
				
				if (groupName1 == null) {
					result = (groupName2 == null) ? 0 : -1;
				} else if (groupName2 == null) {
					result = 1;
				} else {
					result = groupName1.compareTo( groupName2 );
				}
			}
			return result;
		}
		
	}
	
}
