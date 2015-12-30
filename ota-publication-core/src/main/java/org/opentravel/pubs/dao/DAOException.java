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

/**
 * Exception thrown when an error occurrs during DAO processing.
 * 
 * @author S. Livezey
 */
public class DAOException extends Exception {
	
	private static final long serialVersionUID = -5851709057378908824L;

	/**
     * Default constructor.
     */
    public DAOException() {
    }

    /**
     * Constructs an exception with the specified message.
     * 
     * @param message  the exception message
     */
    public DAOException(String message) {
        super(message);
    }

    /**
     * Constructs an exception with the specified underlying cause.
     * 
     * @param cause  the underlying exception that caused this one
     */
    public DAOException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an exception with the specified message and underlying cause.
     * 
     * @param message  the exception message
     * @param cause  the underlying exception that caused this one
     */
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

}
