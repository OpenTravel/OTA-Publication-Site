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

package org.opentravel.pubs.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Performs formatting of model values for display on the UI.
 * 
 * @author S. Livezey
 */
public class ValueFormatter {
	
	private static DateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
	private static NumberFormat integerFormat = new DecimalFormat( "#,##0" );
	private static NumberFormat doubleFormat = new DecimalFormat( "#,##0.#" );
	
	private static ValueFormatter instance = new ValueFormatter();
	
	/**
	 * Private constructor.
	 */
	private ValueFormatter() {}
	
	/**
	 * Returns the default formatter instance.
	 * 
	 * @return ValueFormatter
	 */
	public static ValueFormatter getInstance() {
		return instance;
	}
	
	/**
	 * Returns a formatted string representation of the given date.
	 * 
	 * @param date  the date value to format
	 * @return String
	 */
	public String formatDate(Date date) {
		return (date == null) ? "" : dateFormat.format( date );
	}
	
	/**
	 * Returns a formatted string representation of the given integer value.
	 * 
	 * @param value  the integer value to format
	 * @return String
	 */
	public String formatNumber(Long value) {
		return (value == null) ? "" : integerFormat.format( value );
	}
	
	/**
	 * Returns a formatted string representation of the given double value.
	 * 
	 * @param value  the double value to format
	 * @return String
	 */
	public String formatNumber(Double value) {
		return (value == null) ? "" : doubleFormat.format( value );
	}
	
	/**
	 * Returns a formatted string representation of the given integer value.
	 * 
	 * @param value  the integer value to format
	 * @return String
	 */
	public String formatFileSize(Long fileSize) {
		String result = "";
		
		if (fileSize != null) {
			double size = fileSize.doubleValue() / 1024.0;
			String suffix = "K";
			
			if (size > 1024) {
				size = size / 1024.0;
				suffix = "M";
			}
			result = formatNumber( size) + suffix;
		}
		return result;
	}
	
}
