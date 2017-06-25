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

package org.opentravel.pubs.forms;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

import org.opentravel.pubs.util.StringUtils;

/**
 * Form used to request access to the OTM repository.
 */
public class RepositoryAccessForm extends AbstractForm {
	
	@NotNull private String firstName;
	@NotNull private String lastName;
    private String company;
    private String companyContact;
    private String phone;
	@NotNull private String email;
    private String address;
    private Boolean otaMember;
    private Boolean wantModelDevelopmentInfo;
    private Boolean wantWorkgroupInfo;
    @AssertTrue private Boolean acceptTerms;
    
	/**
	 * Returns the value of the 'firstName' field.
	 *
	 * @return String
	 */
	public String getFirstName() {
		return firstName;
	}
	
	/**
	 * Assigns the value of the 'firstName' field.
	 *
	 * @param firstName  the field value to assign
	 */
	public void setFirstName(String firstName) {
		this.firstName = StringUtils.trimString( firstName );
	}
	
	/**
	 * Returns the value of the 'lastName' field.
	 *
	 * @return String
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Assigns the value of the 'lastName' field.
	 *
	 * @param lastName  the field value to assign
	 */
	public void setLastName(String lastName) {
		this.lastName = StringUtils.trimString( lastName );
	}
	
	/**
	 * Returns the value of the 'company' field.
	 *
	 * @return String
	 */
	public String getCompany() {
		return company;
	}
	
	/**
	 * Assigns the value of the 'company' field.
	 *
	 * @param company  the field value to assign
	 */
	public void setCompany(String company) {
		this.company = StringUtils.trimString( company );
	}
	
	/**
	 * Returns the value of the 'companyContact' field.
	 *
	 * @return String
	 */
	public String getCompanyContact() {
		return companyContact;
	}
	
	/**
	 * Assigns the value of the 'companyContact' field.
	 *
	 * @param companyContact  the field value to assign
	 */
	public void setCompanyContact(String companyContact) {
		this.companyContact = StringUtils.trimString( companyContact );
	}
	
	/**
	 * Returns the value of the 'phone' field.
	 *
	 * @return String
	 */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * Assigns the value of the 'phone' field.
	 *
	 * @param phone  the field value to assign
	 */
	public void setPhone(String phone) {
		this.phone = StringUtils.trimString( phone );
	}
	
	/**
	 * Returns the value of the 'email' field.
	 *
	 * @return String
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Assigns the value of the 'email' field.
	 *
	 * @param email  the field value to assign
	 */
	public void setEmail(String email) {
		this.email = StringUtils.trimString( email );
	}
	
	/**
	 * Returns the value of the 'address' field.
	 *
	 * @return String
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Assigns the value of the 'address' field.
	 *
	 * @param address  the field value to assign
	 */
	public void setAddress(String address) {
		this.address = StringUtils.trimString( address );
	}
	
	/**
	 * Returns the value of the 'otaMember' field.
	 *
	 * @return Boolean
	 */
	public Boolean getOtaMember() {
		return otaMember;
	}
	
	/**
	 * Assigns the value of the 'otaMember' field.
	 *
	 * @param otaMember  the field value to assign
	 */
	public void setOtaMember(Boolean otaMember) {
		this.otaMember = otaMember;
	}
	
	/**
	 * Returns the value of the 'wantModelDevelopmentInfo' field.
	 *
	 * @return Boolean
	 */
	public Boolean getWantModelDevelopmentInfo() {
		return wantModelDevelopmentInfo;
	}
	
	/**
	 * Assigns the value of the 'wantModelDevelopmentInfo' field.
	 *
	 * @param wantModelDevelopmentInfo  the field value to assign
	 */
	public void setWantModelDevelopmentInfo(Boolean wantModelDevelopmentInfo) {
		this.wantModelDevelopmentInfo = wantModelDevelopmentInfo;
	}
	
	/**
	 * Returns the value of the 'wantWorkgroupInfo' field.
	 *
	 * @return Boolean
	 */
	public Boolean getWantWorkgroupInfo() {
		return wantWorkgroupInfo;
	}
	
	/**
	 * Assigns the value of the 'wantWorkgroupInfo' field.
	 *
	 * @param wantWorkgroupInfo  the field value to assign
	 */
	public void setWantWorkgroupInfo(Boolean wantWorkgroupInfo) {
		this.wantWorkgroupInfo = wantWorkgroupInfo;
	}
	
	/**
	 * Returns the value of the 'acceptTerms' field.
	 *
	 * @return Boolean
	 */
	public Boolean getAcceptTerms() {
		return acceptTerms;
	}
	
	/**
	 * Assigns the value of the 'acceptTerms' field.
	 *
	 * @param acceptTerms  the field value to assign
	 */
	public void setAcceptTerms(Boolean acceptTerms) {
		this.acceptTerms = acceptTerms;
	}
    
}
