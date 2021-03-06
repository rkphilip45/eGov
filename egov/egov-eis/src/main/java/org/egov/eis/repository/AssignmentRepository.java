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
package org.egov.eis.repository;

import java.util.Date;
import java.util.List;

import org.egov.pims.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Vaibhav.K
 */
@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    /* Get all assignments for an employee by emp id */
    @Query(" from Assignment A where A.employee.idPersonalInformation =:idPersonalInformation order by A.fromDate")
    public List<Assignment> getAllAssignmentsByEmpId(@Param("idPersonalInformation") Integer idPersonalInformation);

    /*
     * Get all active assignments for an employee as on sysdate
     */
    @Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.employee.idPersonalInformation =:idPersonalInformation order by A.fromDate")
    public List<Assignment> getAllActiveAssignmentsByEmpId(@Param("idPersonalInformation") Integer idPersonalInformation);

    @Query(" from Assignment A where A.fromDate<=:givenDate and A.toDate>=:givenDate and A.position.id=:posId order by A.fromDate")
    public List<Assignment> getAssignmentsForPosition(@Param("posId") Long posId, @Param("givenDate") Date givenDate);

    @Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.position.id=:posId")
    public Assignment getPrimaryAssignmentForPosition(@Param("posId") Long posId);

    @Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.isPrimary='Y' and A.employee.userMaster.id=:userId ")
    public Assignment getPrimaryAssignmentForUser(@Param("userId") Long userId);

    @Query(" from Assignment A where A.fromDate<=:givenDate and A.toDate>=:givenDate and A.isPrimary='Y' and A.employee.idPersonalInformation=:empId ")
    public Assignment getAssignmentByEmpAndDate(@Param("empId") Integer empId, @Param("givenDate") Date givenDate);
    
    @Query(" from Assignment A where A.fromDate<=current_date and A.toDate>=current_date and A.isPrimary='Y' and A.employee.idPersonalInformation=:empId")
    public Assignment getPrimaryAssignmentForEmployee(@Param("empId")Integer empId);
    

}
