/**
 * eGov suite of products aim to improve the internal efficiency,transparency, 
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation 
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or 
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this 
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It 
	   is required that all modified versions of this material be marked in 
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program 
	   with regards to rights under trademark law for use of the trade names 
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.infstr;

import java.io.Serializable;

public class ValidationError implements Serializable {

	private static final long serialVersionUID = 1L;
	private final String key;
	private final String message;
	private String[] args = null;

	public ValidationError(final String key, final String message) {
		this.key = key;
		this.message = message;
	}

	/**
	 * Gets a message based on a key using the supplied args, as defined in {@link java.text.MessageFormat}, or, if the message is not found, a supplied default value is returned.
	 * @param key the resource bundle key that is to be searched for
	 * @param message the default value which will be returned if no message is found
	 * @param args an array args to be used in a {@link java.text.MessageFormat} message
	 * @return the message as found in the resource bundle, or defaultValue if none is found
	 */
	public ValidationError(final String key, final String message, final String... args) {
		this.key = key;
		this.message = message;
		this.args = args;
	}

	public String getKey() {
		return this.key;
	}

	public String getMessage() {
		return this.message;
	}

	public String[] getArgs() {
		return this.args;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.key == null) ? 0 : this.key.hashCode());
		result = prime * result + ((this.message == null) ? 0 : this.message.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ValidationError other = (ValidationError) obj;
		if (this.key == null) {
			if (other.key != null) {
				return false;
			}
		} else if (!this.key.equals(other.key)) {
			return false;
		}
		if (this.message == null) {
			if (other.message != null) {
				return false;
			}
		} else if (!this.message.equals(other.message)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Key=" + this.key + ",Message=" + this.message;
	}

}
