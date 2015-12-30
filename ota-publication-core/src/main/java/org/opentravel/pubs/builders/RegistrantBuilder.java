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
package org.opentravel.pubs.builders;

import java.util.Date;

import org.opentravel.pubs.model.Registrant;

/**
 * Builder used to construct new <code>Registrant</code> instances.
 * 
 * @author S. Livezey
 */
public class RegistrantBuilder {
	
	private String lastName;
	private String firstName;
	private String company;
	private String title;
	private String email;
	private String phone;
	private boolean otaMember = false;
	private Date registrationDate = new Date();
	
	/**
	 * Constructs the new <code>Registrant</code> instance based on the current
	 * state of this builder.
	 * 
	 * @return Registrant
	 */
	public Registrant build() {
		Registrant r = new Registrant();
		
		r.setLastName( lastName );
		r.setFirstName( firstName );
		r.setCompany( company );
		r.setTitle( title );
		r.setEmail( email );
		r.setPhone( phone );
		r.setOtaMember( otaMember );
		r.setRegistrationDate( registrationDate );
		return r;
	}
	
	/**
	 * Assigns the value of the 'lastName' field.
	 *
	 * @param lastName  the field value to assign
	 * @return RegistrantBuilder
	 */
	public RegistrantBuilder setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	
	/**
	 * Assigns the value of the 'firstName' field.
	 *
	 * @param firstName  the field value to assign
	 * @return RegistrantBuilder
	 */
	public RegistrantBuilder setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}
	
	/**
	 * Assigns the value of the 'company' field.
	 *
	 * @param company  the field value to assign
	 * @return RegistrantBuilder
	 */
	public RegistrantBuilder setCompany(String company) {
		this.company = company;
		return this;
	}
	
	/**
	 * Assigns the value of the 'title' field.
	 *
	 * @param title  the field value to assign
	 * @return RegistrantBuilder
	 */
	public RegistrantBuilder setTitle(String title) {
		this.title = title;
		return this;
	}
	
	/**
	 * Assigns the value of the 'email' field.
	 *
	 * @param email  the field value to assign
	 * @return RegistrantBuilder
	 */
	public RegistrantBuilder setEmail(String email) {
		this.email = email;
		return this;
	}
	
	/**
	 * Assigns the value of the 'phone' field.
	 *
	 * @param phone  the field value to assign
	 * @return RegistrantBuilder
	 */
	public RegistrantBuilder setPhone(String phone) {
		this.phone = phone;
		return this;
	}
	
	/**
	 * Assigns the value of the 'otaMember' field.
	 *
	 * @param otaMember  the field value to assign
	 * @return RegistrantBuilder
	 */
	public RegistrantBuilder setOtaMember(boolean otaMember) {
		this.otaMember = otaMember;
		return this;
	}
	
	/**
	 * Assigns the value of the 'registrationDate' field.
	 *
	 * @param registrationDate  the field value to assign
	 * @return RegistrantBuilder
	 */
	public RegistrantBuilder setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
		return this;
	}
	
}
