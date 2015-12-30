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
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;

import org.opentravel.pubs.model.Publication;
import org.opentravel.pubs.model.PublicationGroup;
import org.opentravel.pubs.model.PublicationItem;
import org.opentravel.pubs.model.PublicationItemType;
import org.opentravel.pubs.util.StringUtils;

/**
 * Helper class that separates and organizes the various publication items into groups
 * during the creation of a specification data set.
 * 
 * @author S. Livezey
 */
class SpecificationCollator {
	
	private Publication publication;
	private PublicationGroup xmlSchemasGroup = new PublicationGroup();
	private PublicationGroup jsonSchemasGroup = new PublicationGroup();
	private PublicationGroup generalArtifactsGroup = new PublicationGroup();
	private Map<String,PublicationGroup> otherGroups = new HashMap<>();
	private Map<String,List<String>> groupEntries = new HashMap<>();
	
	/**
	 * Constructor that specifies the publication with which all groups will be
	 * associated.
	 * 
	 * @param publication  the publication with which all groups will be associated
	 */
	public SpecificationCollator(Publication publication) {
		this.publication = publication;
		
		this.xmlSchemasGroup.setOwner( publication );
		this.xmlSchemasGroup.setName( "XML Schemas" );
		this.xmlSchemasGroup.setMemberType( PublicationItemType.XML_SCHEMA );
		this.xmlSchemasGroup.setPublicationItems( new ArrayList<PublicationItem>() );
		
		this.jsonSchemasGroup.setOwner( publication );
		this.jsonSchemasGroup.setName( "JSON Schemas" );
		this.jsonSchemasGroup.setMemberType( PublicationItemType.JSON_SCHEMA );
		this.jsonSchemasGroup.setPublicationItems( new ArrayList<PublicationItem>() );
		
		this.generalArtifactsGroup.setOwner( publication );
		this.generalArtifactsGroup.setName( "General Artifacts" );
		this.generalArtifactsGroup.setMemberType( PublicationItemType.ARTIFACT );
		this.generalArtifactsGroup.setPublicationItems( new ArrayList<PublicationItem>() );
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
		if (!xmlSchemasGroup.getPublicationItems().isEmpty()) {
			groupList.add( xmlSchemasGroup );
		}
		if (!jsonSchemasGroup.getPublicationItems().isEmpty()) {
			groupList.add( jsonSchemasGroup );
		}
		if (!generalArtifactsGroup.getPublicationItems().isEmpty()) {
			groupList.add( generalArtifactsGroup );
		}
		for (PublicationGroup miscGroup : miscGroups) {
			groupList.add( miscGroup );
		}
		if (otherGroup != null) {
			groupList.add( otherGroup );
		}
		
		// Assign the 'sortOrder' value for each group in the list
		for (int i = 0; i < groupList.size(); i++) {
			groupList.get( i ).setSortOrder( i );
		}
		return groupList;
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
		PublicationItem pubItem = null;
		
		// Construct a new publication item as long as it is not an archive (zip) entry
		if (!isArchiveArtifact( filename ) && !isXmlDocument( filename )) {
			PublicationGroup group = null;
			
			pubItem = new PublicationItem();
			pubItem.setItemFilename( filename );
			pubItem.setFileSize( archiveEntry.getSize() );
			pubItem.setCreateDate( new Date( archiveEntry.getTime() ) );
			
			if (isXmlSchema( filename )) {
				group = xmlSchemasGroup;
				pubItem.setType( PublicationItemType.XML_SCHEMA );
				
			} else if (isJsonSchema( filename )) {
				group = jsonSchemasGroup;
				pubItem.setType( PublicationItemType.JSON_SCHEMA );
				
			} else if (groupName == null) {
				group = generalArtifactsGroup;
				pubItem.setType( PublicationItemType.ARTIFACT );
				
			} else {
				group = otherGroups.get( groupName );
				
				if (group == null) {
					group = new PublicationGroup();
					group.setName( groupName );
					group.setOwner( publication );
					group.setMemberType( PublicationItemType.ARTIFACT );
					group.setPublicationItems( new ArrayList<PublicationItem>() );
					otherGroups.put( groupName, group );
				}
				pubItem.setType( PublicationItemType.ARTIFACT );
			}
			group.getPublicationItems().add( pubItem );
			pubItem.setOwner( group );
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
	 * Returns true if the given filename represents an XML document.
	 * 
	 * @param filename  the filename to analyze
	 * @return boolean
	 */
	private boolean isXmlDocument(String filename) {
		return filename.toLowerCase().endsWith( ".xml" );
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
