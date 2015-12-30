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

import java.util.Date;

/**
 * Enumeration used to specify a range of dates relative to the current day.
 * 
 * @author S. Livezey
 */
public enum DateRangeType {
	
	LAST_WEEK( 86400000L * 7, "Last Week" ),
	
	LAST_30_DAYS( 86400000L * 30, "Last 30 Days" ),
	
	LAST_60_DAYS( 86400000L * 60, "Last 60 Days" ),
	
	LAST_90_DAYS( 86400000L * 90, "Last 90 Days" ),
	
	ALL( -1, "Show All" );
	
	private long adjustmentMillis;
	private String displayValue;
	
	/**
	 * Constructor that specifies the number of milliseconds in the date range
	 * window.
	 * 
	 * @param adjustmentMillis  the number of milliseconds in the date range window
	 * @param displayValue  the display value for the enumeration
	 */
	private DateRangeType(long adjustmentMillis, String displayValue) {
		this.adjustmentMillis = adjustmentMillis;
		this.displayValue = displayValue;
	}
	
	/**
	 * Returns a date representation of the starting time for the date range window
	 * relative to the current date/time.
	 * 
	 * @return Date
	 */
	public Date getRangeStart() {
		return new Date( System.currentTimeMillis() - adjustmentMillis );
	}
	
	/**
	 * Returns the display value for the enumeration.
	 * 
	 * @return String
	 */
	public String getDisplayValue() {
		return displayValue;
	}
	
}
