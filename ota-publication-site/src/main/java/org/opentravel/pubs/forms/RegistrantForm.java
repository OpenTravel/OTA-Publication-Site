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

/**
 * Form used for the creation of new web site registrants.
 */
public class RegistrantForm extends AbstractForm {
	
    private String firstName;
    private String lastName;
    private String title;
    private String company;
    private String phone;
    private String email;
    private Boolean otaMember;
    
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
		this.firstName = firstName;
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
		this.lastName = lastName;
	}
	
	/**
	 * Returns the value of the 'title' field.
	 *
	 * @return String
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Assigns the value of the 'title' field.
	 *
	 * @param title  the field value to assign
	 */
	public void setTitle(String title) {
		this.title = title;
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
		this.company = company;
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
		this.phone = phone;
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
		this.email = email;
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
    
}
