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
package org.opentravel.pubs.notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.opentravel.pubs.config.PasswordHelper;

/**
 * Builder used to quickly assemble the email configuration application
 * settings.
 */
public class EmailConfigBuilder {
	
	public static final String EMAIL_CONFIG_SETTINGS = "mail.smtp.config";
	
	public static final String USERNAME_PROPERTY = "pubs.mail.user";
	public static final String PASSWORD_PROPERTY = "pubs.mail.password";
	public static final String SENDER_ADDRESS_PROPERTY = "pubs.mail.sender.address";
	public static final String SENDER_NAME_PROPERTY    = "pubs.mail.sender.name";
	public static final String CC_RECIPIENTS_PROPERTY  = "pubs.mail.recipients.cc";
	
	private String smtpHost;
	private int smtpPort = -1;
	private String smtpUser;
	private String smtpPassword;
	private long timeout = 10000L;
	private boolean sslEnable;
	private boolean authEnable;
	private boolean startTlsEnable;
	private String senderAddress;
	private String senderName;
	private String[] ccRecipients;
	
	/**
	 * Constructs the email configuration properties using the current state of
	 * this builder.
	 * 
	 * @return Properties
	 */
	public Properties build() {
		Properties props = new Properties();
		
		if (smtpHost != null) {
			props.setProperty( "mail.smtp.host", smtpHost );
		}
		if (smtpPort > 0) {
			props.setProperty( "mail.smtp.port", smtpPort + "" );
		}
		if (smtpUser != null) {
			props.setProperty( USERNAME_PROPERTY, smtpUser );
		}
		if (smtpPassword != null) {
			props.setProperty( PASSWORD_PROPERTY, smtpPassword );
		}
		if (timeout > 0) {
			props.setProperty( "mail.smtp.timeout", timeout + "" );
		}
		if (sslEnable) {
			props.setProperty( "mail.smtp.ssl.enable", "true" );
		}
		if (authEnable) {
			props.setProperty( "mail.smtp.auth", "true" );
		}
		if (startTlsEnable) {
			props.setProperty( "mail.smtp.starttls.enable", "true" );
		}
		if (senderAddress != null) {
			props.setProperty( SENDER_ADDRESS_PROPERTY, senderAddress );
		}
		if (senderName != null) {
			props.setProperty( SENDER_NAME_PROPERTY, senderName );
		}
		if ((ccRecipients != null) && (ccRecipients.length > 0)) {
			StringBuilder ccList = new StringBuilder();
			boolean first = true;
			
			for (String recipient : ccRecipients) {
				if (!first) ccList.append( "," );
				ccList.append( recipient );
				first = false;
			}
			props.setProperty( CC_RECIPIENTS_PROPERTY, ccList.toString() );
		}
		return props;
	}
	
	/**
	 * Assigns the name of the SMTP host.
	 *
	 * @param smtpHost  the host name to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
		return this;
	}
	
	/**
	 * Assigns the number of the SMTP port.
	 *
	 * @param smtpPort  the port number to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
		return this;
	}
	
	/**
	 * Assigns the login ID of the SMTP user.
	 *
	 * @param smtpUser  the user ID to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
		return this;
	}
	
	/**
	 * Assigns the password of the SMTP user.
	 *
	 * @param clearTextPassword  the clear-text password to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setSmtpPassword(String clearTextPassword) {
		if (clearTextPassword != null) {
			this.smtpPassword = PasswordHelper.encrypt( clearTextPassword );
		} else {
			this.smtpPassword = null;
		}
		return this;
	}
	
	/**
	 * Assigns the duration in milliseconds of the SMTP timeout.
	 *
	 * @param timeout  the timeout duration (in milliseconds) to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setTimeout(long timeout) {
		this.timeout = timeout;
		return this;
	}
	
	/**
	 * Assigns the indicator that determines whether SSL communication with
	 * the SMTP server is enabled.
	 *
	 * @param sslEnable  the indicator value to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setSslEnable(boolean sslEnable) {
		this.sslEnable = sslEnable;
		return this;
	}
	
	/**
	 * Assigns the indicator that determines whether authentication is required
	 * for the SMTP server.
	 *
	 * @param authEnable  the indicator value to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setAuthEnable(boolean authEnable) {
		this.authEnable = authEnable;
		return this;
	}
	
	/**
	 * Assigns the indicator that determines whether TLS communications should
	 * be enabled if supported by the SMTP server.
	 *
	 * @param startTlsEnable  the indicator value to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setStartTlsEnable(boolean startTlsEnable) {
		this.startTlsEnable = startTlsEnable;
		return this;
	}
	
	/**
	 * Assigns the email address of the sender for all outgoing email messages.
	 *
	 * @param senderAddress  the sender's email address to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
		return this;
	}
	
	/**
	 * Assigns the display name of the sender for all outgoing email messages.
	 *
	 * @param senderName  the sender's display name to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setSenderName(String senderName) {
		this.senderName = senderName;
		return this;
	}
	
	/**
	 * Assigns the list of all email addresses that will be courtesy copied on
	 * all email notifications.
	 *
	 * @param ccRecipients  the comma-separated list of CC recipients to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setCcRecipients(String ccRecipients) {
		String[] ccItems = (ccRecipients == null) ? new String[0] : ccRecipients.split(",");
		List<String> ccList = new ArrayList<>();
		
		for (String ccItem : ccItems) {
			if ((ccItem = ccItem.trim()).length() > 0) {
				ccList.add( ccItem );
			}
		}
		return setCcRecipients( ccList.toArray( new String[ ccList.size() ] ) );
	}
	
	/**
	 * Assigns the list of all email addresses that will be courtesy copied on
	 * all email notifications.
	 *
	 * @param ccRecipients  the list of CC recipients to assign
	 * @return EmailConfigBuilder
	 */
	public EmailConfigBuilder setCcRecipients(String[] ccRecipients) {
		this.ccRecipients = ccRecipients;
		return this;
	}
	
}
