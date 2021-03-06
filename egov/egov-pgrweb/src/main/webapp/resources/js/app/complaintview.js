/*#-------------------------------------------------------------------------------
# eGov suite of products aim to improve the internal efficiency,transparency, 
#    accountability and the service delivery of the government  organizations.
# 
#     Copyright (C) <2015>  eGovernments Foundation
# 
#     The updated version of eGov suite of products as by eGovernments Foundation 
#     is available at http://www.egovernments.org
# 
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     any later version.
# 
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
# 
#     You should have received a copy of the GNU General Public License
#     along with this program. If not, see http://www.gnu.org/licenses/ or 
#     http://www.gnu.org/licenses/gpl.html .
# 
#     In addition to the terms of the GPL license to be adhered to in using this
#     program, the following additional terms are to be complied with:
# 
# 	1) All versions of this program, verbatim or modified must carry this 
# 	   Legal Notice.
# 
# 	2) Any misrepresentation of the origin of the material is prohibited. It 
# 	   is required that all modified versions of this material be marked in 
# 	   reasonable ways as different from the original version.
# 
# 	3) This license does not grant any rights to any user of the program 
# 	   with regards to rights under trademark law for use of the trade names 
# 	   or trademarks of eGovernments Foundation.
# 
#   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
#-------------------------------------------------------------------------------*/
$(document).ready(function()
{
	$('#toggleactions').click(function(){
		if($(this).html()== "More..")
		{
			$(this).html('Less..');
			$('.show-action-more').show();
		}else
		{
			$(this).html('More..');
			$('.show-action-more').hide();
		}
		
	});
	
	$("#btn_submit").click(function(){
		alert("hello"+$("#btn_submit").name)
		if($("#btn_submit").name=='Close')
			{
			return true;
			}
		if($('#inc_messge').val() == '')
		{
			$('#inc_messge').addClass('error');
		}else
		{
			document.forms[0].submit();
			//alert("Submitted Successfully");
			$('#inc_messge').removeClass('error');
		}
		
	});
	
	$('.slide-history-menu').click(function(){
		$('.history-slide').slideToggle();
		if($('#toggle-his-icon').hasClass('entypo-down-open'))
		{
			$('#toggle-his-icon').removeClass('entypo-down-open').addClass('entypo-up-open');
			//$('#see-more-link').hide();
			}else{
			$('#toggle-his-icon').removeClass('entypo-up-open').addClass('entypo-down-open');
			//$('#see-more-link').show();
		}
	});
	
	$('#ct-sel-jurisd').change(function(){
		console.log("came jursidiction"+$('#ct-sel-jurisd').val());
		$.ajax({
			url: "/pgr/ajax-getWards",
			type: "GET",
			data: {
				id : $('#ct-sel-jurisd').val()
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#location').empty();
				
				$('#location').append($("<option value=''>Select</option>"));
				$.each(response, function(index, value) {
					
				     $('#location').append($('<option>').text(value.name).attr('value', value.id));
				});
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});
	
	$('#approvalDepartment').change(function(){
		//alert("came approvalDepartment");
		$.ajax({
			url: "/pgr/ajax-approvalDesignations",     
			type: "GET",
			data: {
				approvalDepartment : $('#approvalDepartment').val()   
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#approvalDesignation').empty();
				//alert(document.getElementById('approvalDesignation'));
				$('#approvalDesignation').append($("<option value=''>Select</option>"));
				$.each(response, function(index, value) {
					$('#approvalDesignation').append($('<option>').text(value.designationName).attr('value', value.designationId));
				});
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});
	
	$('#approvalDesignation').change(function(){
		$.ajax({
			url: "/pgr/ajax-approvalPositions",     
			type: "GET",
			data: {
				approvalDesignation : $('#approvalDesignation').val(),
				approvalDepartment : $('#approvalDepartment').val()    
			},
			dataType: "json",
			success: function (response) {
				console.log("success"+response);
				$('#approvalPosition').empty();
				$('#approvalPosition').append($("<option value=''>Select</option>"));
				$.each(response, function(index, value) {
					$('#approvalPosition').append($('<option>').text(value.name).attr('value', value.id));  
				});
				
			}, 
			error: function (response) {
				console.log("failed");
			}
		});
	});
	
});
