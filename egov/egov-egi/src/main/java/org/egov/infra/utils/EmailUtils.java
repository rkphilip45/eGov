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
package org.egov.infra.utils;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EmailUtils {
	
	private static Logger LOG = Logger.getLogger(EmailUtils.class);
	
	public static final String MAILSENDER = "mailSender";
	 
	@Autowired
    private JavaMailSenderImpl mailSender;
    
    @Autowired
	private SimpleMailMessage mailMessage;
    
    @Autowired
    @Qualifier("egovErpProperties")
    private Properties environment;
    
	public boolean sendMail(final String toEmail, final String mailBody,
			final String subject) {
		boolean isSent = false;

		try {
			this.mailSender.setPort(Integer.valueOf(environment.getProperty("mail.port")));
			this.mailSender.setHost(environment.getProperty("mail.host"));
			this.mailSender.setProtocol(environment.getProperty("mail.protocol"));

			this.mailSender.setUsername(environment.getProperty("mail.sender.username"));
			this.mailSender.setPassword(environment.getProperty("mail.sender.password"));

			final Properties mailProperties = new Properties();
			mailProperties.setProperty("mail.smtps.auth", environment.getProperty("mail.smtps.auth"));
			mailProperties.setProperty("mail.smtps.starttls.enable", environment.getProperty("mail.smtps.starttls.enable"));
			mailProperties.setProperty("mail.smtps.debug", environment.getProperty("mail.smtps.debug"));
			this.mailSender.setJavaMailProperties(mailProperties);
			this.mailMessage.setTo(toEmail);
			this.mailMessage.setSubject(subject);
			this.mailMessage.setText(mailBody);
			this.mailSender.send(this.mailMessage);
			isSent = true;
		} catch (final Exception e) {
			LOG.error("Error occurred while trying to send mail", e);
		}
		return isSent;
	} 	
}
