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
package org.egov.infra.config.security.authentication;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.egov.infra.admin.master.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SecureUser implements UserDetails {
    private static final long serialVersionUID = -8756608845278722035L;
    private final User user;
    private final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    public SecureUser(User user) {
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        } else {
            this.user = user;
            user.getRoles().forEach((role) -> {
                this.authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
        }
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public boolean isAccountNonExpired() {
        return this.user.getPwdExpiryDate().isAfterNow();
    }

    public boolean isAccountNonLocked() {
        return this.user.isActive();
    }

    public boolean isCredentialsNonExpired() {
        return this.user.getPwdExpiryDate().isAfterNow();
    }

    public boolean isEnabled() {
        return this.user.isActive();
    }

    public String getPassword() {
        return this.user.getPassword();
    }

    public String getUsername() {
        return this.user.getUsername();
    }
    
    public Long getUserId() {
        return this.user.getId();
    }
}
