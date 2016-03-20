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

import java.util.Properties;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.opentravel.pubs.config.PasswordHelper;
import org.opentravel.pubs.notification.EmailConfigBuilder;
import org.opentravel.pubs.util.StringUtils;
import org.opentravel.pubs.validation.EmailAddress;
import org.opentravel.pubs.validation.EmailAddressList;
import org.opentravel.pubs.validation.IntegerString;
import org.opentravel.pubs.validation.LongIntegerString;

/**
 * Form used for the editing of email configuration settings.
 */
public class EmailSettingsForm extends AbstractForm {
	
	private boolean enableNotification;
	
	@NotNull
	@Pattern(
		regexp = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$",
		message = "{org.opentravel.pubs.constraints.HostName.message}" )
	private String smtpHost;
	
	@NotNull
	@IntegerString
	private String smtpPort;
	
	private String smtpUser;
	
	private String smtpPassword;
	
	@LongIntegerString
	private String timeout;
	
	private boolean sslEnable;
	
	private boolean authEnable;
	
	private boolean startTlsEnable;
	
	@NotNull
	@EmailAddress
	private String senderAddress;
	
	@NotNull
	private String senderName;
	
	@EmailAddressList
	private String ccRecipients;
	
	/**
	 * Constructor that initializes the form bean using the email application setting properties.
	 * 
	 * @param emailConfig  the email configuration from the application settings
	 */
	public void initialize(Properties emailConfig) {
		if (emailConfig != null) {
			this.smtpHost       = emailConfig.getProperty( "mail.smtp.host" );
			this.smtpPort       = emailConfig.getProperty( "mail.smtp.port" );
			this.smtpUser       = emailConfig.getProperty( EmailConfigBuilder.USERNAME_PROPERTY );
			this.smtpPassword   = PasswordHelper.decrypt( emailConfig.getProperty( EmailConfigBuilder.PASSWORD_PROPERTY ) );
			this.timeout        = emailConfig.getProperty( "mail.smtp.timeout" );
			this.sslEnable      = StringUtils.parseBooleanValue( emailConfig.getProperty( "mail.smtp.ssl.enable" ) );
			this.authEnable     = StringUtils.parseBooleanValue( emailConfig.getProperty( "mail.smtp.auth" ) );
			this.startTlsEnable = StringUtils.parseBooleanValue( emailConfig.getProperty( "mail.smtp.starttls.enable" ) );
			this.senderAddress  = emailConfig.getProperty( EmailConfigBuilder.SENDER_ADDRESS_PROPERTY );
			this.senderName     = emailConfig.getProperty( EmailConfigBuilder.SENDER_NAME_PROPERTY );
			this.ccRecipients   = emailConfig.getProperty( EmailConfigBuilder.CC_RECIPIENTS_PROPERTY );
		}
		this.enableNotification = (emailConfig != null);
	}
	
	/**
	 * Returns the value of the 'enableNotification' field.
	 *
	 * @return boolean
	 */
	public boolean isEnableNotification() {
		return enableNotification;
	}

	/**
	 * Assigns the value of the 'enableNotification' field.
	 *
	 * @param enableNotification  the field value to assign
	 */
	public void setEnableNotification(boolean enableNotification) {
		this.enableNotification = enableNotification;
	}

	/**
	 * Returns the value of the 'smtpHost' field.
	 *
	 * @return String
	 */
	public String getSmtpHost() {
		return smtpHost;
	}
	
	/**
	 * Assigns the value of the 'smtpHost' field.
	 *
	 * @param smtpHost  the field value to assign
	 */
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	
	/**
	 * Returns the value of the 'smtpPort' field.
	 *
	 * @return String
	 */
	public String getSmtpPort() {
		return smtpPort;
	}
	
	/**
	 * Assigns the value of the 'smtpPort' field.
	 *
	 * @param smtpPort  the field value to assign
	 */
	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}
	
	/**
	 * Returns the value of the 'smtpUser' field.
	 *
	 * @return String
	 */
	public String getSmtpUser() {
		return smtpUser;
	}
	
	/**
	 * Assigns the value of the 'smtpUser' field.
	 *
	 * @param smtpUser  the field value to assign
	 */
	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}
	
	/**
	 * Returns the value of the 'smtpPassword' field.
	 *
	 * @return String
	 */
	public String getSmtpPassword() {
		return smtpPassword;
	}
	
	/**
	 * Assigns the value of the 'smtpPassword' field.
	 *
	 * @param smtpPassword  the field value to assign
	 */
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}
	
	/**
	 * Returns the value of the 'timeout' field.
	 *
	 * @return String
	 */
	public String getTimeout() {
		return timeout;
	}
	
	/**
	 * Assigns the value of the 'timeout' field.
	 *
	 * @param timeout  the field value to assign
	 */
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * Returns the value of the 'sslEnable' field.
	 *
	 * @return boolean
	 */
	public boolean isSslEnable() {
		return sslEnable;
	}
	
	/**
	 * Assigns the value of the 'sslEnable' field.
	 *
	 * @param sslEnable  the field value to assign
	 */
	public void setSslEnable(boolean sslEnable) {
		this.sslEnable = sslEnable;
	}
	
	/**
	 * Returns the value of the 'authEnable' field.
	 *
	 * @return boolean
	 */
	public boolean isAuthEnable() {
		return authEnable;
	}
	
	/**
	 * Assigns the value of the 'authEnable' field.
	 *
	 * @param authEnable  the field value to assign
	 */
	public void setAuthEnable(boolean authEnable) {
		this.authEnable = authEnable;
	}
	
	/**
	 * Returns the value of the 'startTlsEnable' field.
	 *
	 * @return boolean
	 */
	public boolean isStartTlsEnable() {
		return startTlsEnable;
	}
	
	/**
	 * Assigns the value of the 'startTlsEnable' field.
	 *
	 * @param startTlsEnable  the field value to assign
	 */
	public void setStartTlsEnable(boolean startTlsEnable) {
		this.startTlsEnable = startTlsEnable;
	}
	
	/**
	 * Returns the value of the 'senderAddress' field.
	 *
	 * @return String
	 */
	public String getSenderAddress() {
		return senderAddress;
	}
	
	/**
	 * Assigns the value of the 'senderAddress' field.
	 *
	 * @param senderAddress  the field value to assign
	 */
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}
	
	/**
	 * Returns the value of the 'senderName' field.
	 *
	 * @return String
	 */
	public String getSenderName() {
		return senderName;
	}
	
	/**
	 * Assigns the value of the 'senderName' field.
	 *
	 * @param senderName  the field value to assign
	 */
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	
	/**
	 * Returns the value of the 'ccRecipients' field.
	 *
	 * @return String
	 */
	public String getCcRecipients() {
		return ccRecipients;
	}
	
	/**
	 * Assigns the value of the 'ccRecipients' field.
	 *
	 * @param ccRecipients  the field value to assign
	 */
	public void setCcRecipients(String ccRecipients) {
		this.ccRecipients = ccRecipients;
	}
	
}
