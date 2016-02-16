/**
 * Copyright (C) 2014 OpenTravel Alliance (info@opentravel.org)
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

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.opentravel.pubs.config.PasswordHelper;
import org.opentravel.pubs.dao.ApplicationSettingsDAO;
import org.opentravel.pubs.dao.DAOFactoryManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manager that handles the formatting and sending of notification emails.
 */
public class NotificationManager {
	
    public static final String TEMPLATE_LOCATION = "/org/opentravel/pubs/templates/";
    public static final String COMMENT_TEMPLATE  = "comment-email.vm";
    
    private static final Logger log = LoggerFactory.getLogger( NotificationManager.class );
    private static final int MAX_RETRIES = 5;
    
    private static NotificationManager instance = new NotificationManager();
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static VelocityEngine velocityEngine;
    
	/**
	 * Private constructor.
	 */
	private NotificationManager() {}
	
	/**
	 * Returns the <code>NotificationManager</code> singleton instance.
	 * 
	 * @return NotificationManager
	 */
	public static NotificationManager getInstance() {
		return instance;
	}
	
	/**
	 * Sends an email notification to one or more recipients using the template and content
	 * information provided.  This method returns a <code>Future</code> that can be used to
	 * synchronize with the sending of the email notification which occurs in a background
	 * thread.  The return type of the future indicates the success or failure of the email
	 * transmission.
	 * 
	 * @param messageTemplate  the name of the email template to use for generating the email message body
	 * @param contentMap  the map containing the key/value pairs required for processing the email message template
	 * @param recipients  the list of recipients to whom the email will be addressed
	 * @return Future<Boolean>
	 */
	public Future<Boolean> sendNotification(final String subject, final String messageTemplate, final Map<String,Object> contentMap,
			final InternetAddress... recipients) {
		ApplicationSettingsDAO settingsDAO = DAOFactoryManager.getFactory().newApplicationSettingsDAO();
		final Properties emailConfig = settingsDAO.getSettings( EmailConfigBuilder.EMAIL_CONFIG_SETTINGS );
		Future<Boolean> future = null;
		
		if (emailConfig != null) {
			Callable<Boolean> notificationTask = new Callable<Boolean>() {
				public Boolean call() {
					boolean successInd = false;
					int retryCount = 0;
					
					while (!successInd && (retryCount < MAX_RETRIES)) {
						try {
							Session mailSession = Session.getInstance( getSmtpConfig( emailConfig ) );
							String userId = emailConfig.getProperty( EmailConfigBuilder.USERNAME_PROPERTY );
							String encryptedPassword = emailConfig.getProperty( EmailConfigBuilder.PASSWORD_PROPERTY );
							InternetAddress fromAddress = new InternetAddress(
									emailConfig.getProperty( EmailConfigBuilder.SENDER_ADDRESS_PROPERTY ), 
									emailConfig.getProperty( EmailConfigBuilder.SENDER_NAME_PROPERTY ) );
							String ccList = emailConfig.getProperty( EmailConfigBuilder.CC_RECIPIENTS_PROPERTY );
							Message message = new MimeMessage( mailSession );
							
							for (InternetAddress recipient : recipients) {
								message.addRecipient( RecipientType.TO, recipient );
							}
							if ((ccList != null) && (ccList.length() > 0)) {
								for (String ccAddress : ccList.split(",")) {
									message.addRecipient( RecipientType.CC, new InternetAddress( ccAddress ) );
								}
							}
							message.setFrom( fromAddress );
							message.setSubject( subject );
							message.setContent( generateMessageBody( messageTemplate, contentMap ), "text/html" );
							
							if ((userId != null) && (encryptedPassword != null)) {
								Transport.send( message, userId, PasswordHelper.decrypt( encryptedPassword ) );
							} else {
								Transport.send( message );
							}
							successInd = true;
							log.info( "Sent email notification - " + subject );
							
						} catch (Throwable e) {
							log.error( "Error sending email notification.", e.getMessage() );
							retryCount++;
						}
					}
					return successInd;
				}
			};
			future = executorService.submit( notificationTask );
		}
		return future;
	}
	
	/**
	 * Generates the body of the outbound notification email using the template name and
	 * content information provided.
	 * 
	 * @param messageTemplate  the name of the email template to use for generating the email message body
	 * @param contentMap  the map containing the key/value pairs required for processing the email message template
	 * @return String
	 */
	private String generateMessageBody(String messageTemplate, Map<String, Object> contentMap) {
		Template template = velocityEngine.getTemplate( TEMPLATE_LOCATION + messageTemplate, "UTF-8" );
		VelocityContext context = new VelocityContext();
		StringWriter writer = new StringWriter();
		
		if (contentMap != null) {
			for (Map.Entry<String, Object> property : contentMap.entrySet()) {
				context.put( property.getKey(), property.getValue() );
			}
		}
		template.merge( context, writer );
		return writer.toString();
	}
	
	/**
	 * Returns the filtered set of properties that only apply to the SMTP session configuration.
	 * 
	 * @param emailConfig  the email configuration settings for the application
	 * @return Properties
	 */
	private Properties getSmtpConfig(Properties emailConfig) {
		Properties smtpConfig = new Properties();
		
		for (String key : emailConfig.stringPropertyNames()) {
			if (key.startsWith("mail.smtp")) {
				smtpConfig.put( key, emailConfig.get( key ) );
			}
		}
		return smtpConfig;
	}
	
	/**
	 * Initializes the Velocity template processing engine.
	 */
	static {
		try {
			VelocityEngine ve = new VelocityEngine();
			
			ve.setProperty( RuntimeConstants.RESOURCE_LOADER, "classpath" );
			ve.setProperty( "classpath.resource.loader.class", ClasspathResourceLoader.class.getName() );
			velocityEngine = ve;
			
		} catch (Throwable t) {
			throw new ExceptionInInitializerError( t );
		}
	}
	
}
