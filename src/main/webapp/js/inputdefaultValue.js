$(document).ready(function(){
	setInterval(inputPress,200);
});
$(".default-value").focus(function(){
	$(this).prev("span").css("color", "#cccccc");
	if($(this).val()!=''){
		$(this).prev("span").hide();
	}
	
});
function inputPress(){
	$(".default-value").each(function(){
		if($(this).val()!=''){
			$(this).prev("span").hide();
		}
	});
	
}
$(".default-value").blur(function(){
	if($(this).val()==''){
		$(this).prev("span").show();
		$(this).prev("span").css("color", "#404040");
	}
});
$(".default-value").prev("span").click(function(){
	$(this).next("input").focus();
});