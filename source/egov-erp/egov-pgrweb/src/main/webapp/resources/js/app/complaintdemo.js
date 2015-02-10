jQuery(document).ready(function($)
{
	// Instantiate the Bloodhound suggestion engine
	var complaintype = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: '/pgr/complaint/complaintsTypes?q=%QUERY',
			filter: function (data) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(data, function (ct) {
					return {
						value: ct.name
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	complaintype.initialize();
	
	// Instantiate the Typeahead UI
	$('.typeahead').typeahead({
		  hint: true,
		  highlight: true,
		  minLength: 3
		}, {
		displayKey: 'value',
		source: complaintype.ttAdapter()
	});
	
	// Instantiate the Bloodhound suggestion engine
	var complaintlocation = new Bloodhound({
		datumTokenizer: function (datum) {
			return Bloodhound.tokenizers.whitespace(datum.value);
		},
		queryTokenizer: Bloodhound.tokenizers.whitespace,
		remote: {
			url: 'http://google.com',
			filter: function (complaintlocation) {
				// Map the remote source JSON array to a JavaScript object array
				return $.map(complaintlocation.results, function (cl) {
					return {
						value: cl.original_title
					};
				});
			}
		}
	});
	
	// Initialize the Bloodhound suggestion engine
	complaintlocation.initialize();
	
	// Instantiate the Typeahead UI
	$('#clocation').typeahead(null, {
		displayKey: 'value',
		source: complaintlocation.ttAdapter()
	});
	
	
	$(":input").inputmask();
	
	$("#f-name").on("input", function(){
		var regexp = /[^a-zA-Z]/g;
		if($(this).val().match(regexp)){
			$(this).val( $(this).val().replace(regexp,'') );
		}
	});
	/*complaint through*/
	$('input:radio[name="compthr"]').click(function(e) {
		if($('#pform').is(':checked'))
		{
			$('#recenter, #regnoblock').show();
		}else
		{
			$('#recenter, #regnoblock').hide();
		}
	});
	
	$('#doc').bind('input propertychange', function() {
		var remchar = parseInt(400 - ($('#doc').val().length));
		$('#rcc').html('Remaining Characters : '+remchar);
		
	});
	
	$('#triggerFile1').on('click', function(e){
        e.preventDefault();
        $("#file1").trigger('click');
    });
	
	
	$('#triggerFile2').on('click', function(e){
        e.preventDefault();
        $("#file2").trigger('click');
    });
	
	$('#triggerFile3').on('click', function(e){
        e.preventDefault();
        $("#file3").trigger('click');
    });
	
	$("select#rec_centerselect").prop('selectedIndex', 2);
	$('#reg_no').val('CT156YT6C89');
	$('#f-name').val('Manu Srivastava');
	$('#mob-no').val('8076453213');
	$('#email').val('manu@egovernments.org');
	$('#com_type').val('Repairs to the SWD');
	$('#comptitle').val('Waterlogged in our areas');
	$('#doc').val('Road is waterlogged. kids are floating boats in it. People are washing cars in it. Please fix it soon as the mosquito colonies are going to expload soon!');
	$('#clocation').val('VP Hall Compound Road, Kannappar Thidal, Poongavanapuram, Chennai, Tamil Nadu 600003, India');
	$('#lm').val('Near phoenix mall');
	
});