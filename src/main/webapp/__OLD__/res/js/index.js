 $(document).ready(function(){
     $("#loginLink").click(function(){
         $('#loginModal').modal('show');
     });

    $("#signupLink").click(function(){
        $('#signupModal').modal('show');
    });


    //EDIT FIELDS

	$.fn.inlineEdit = function(replaceWith, connectWith) {

	    $(this).hover(function() {
	        $(this).addClass('hover');
	    }, function() {
	        $(this).removeClass('hover');
	    });

	    $(this).click(function() {

	        var elem = $(this);

	        elem.hide();
	        elem.after(replaceWith);
	        replaceWith.focus();

	        replaceWith.blur(function() {

	            if ($(this).val() != "") {
	                connectWith.val($(this).val()).change();
	                elem.text($(this).val());
	            }

	            $(this).remove();
	            elem.show();
	        });
	    });
	};

	var replaceWith = $('<input name="temp" type="text"  />'),
    connectWith = $('input[name="hiddenField"]');

	$('.changable').inlineEdit(replaceWith, connectWith);

	//END EDIIT

	//ADD NUMBER
	 $('.multi-number-wrapper').each(function() {
	    var $wrapper = $('.multi-numbers', this);
	    $(".add-number", $(this)).click(function(e) {
	    	if ($('.multi-number', $wrapper).length < 5)
	        	$('.multi-number:first-child', $wrapper).clone(true).appendTo($wrapper).find('p').val('').focus();
	    });
	    $('.multi-number .remove-number', $wrapper).click(function() {
	        if ($('.multi-number', $wrapper).length > 1)
	            $(this).parent('.multi-number').remove();
	    });
	});
    //END NUMBER

});


