if(Elight == null) var Elight = {};
if(ElightResult == null) var ElightResult = function() {};

Elight.SEARCH_BUTTON_ID 			= 'elightSearchButton';
Elight.INPUT_ID 					= 'elightInput';
Elight.INPUT_TEXT_ID				= 'elightInputText';
Elight.OUTPUT_ID 					= 'elightOutput';
Elight.RESULTS_ID 					= 'elightResults';
Elight.RESULTS_AND_POINTER_ID 		= 'elightResultsAndPointer';
Elight.SEARCH_INPUT_ID 				= 'elightSearchInput';
Elight.SLIDED_OUT					= 1;
Elight.SLIDED_IN					= 2;
Elight.WORD_WRAP_TITLE				= 30;
Elight.WORD_WRAP_URL				= 45;
Elight.CROP_END_TITLE				= 30;
Elight.CROP_END_URL					= 40;
Elight.MOUSE_OVER 					= false;
Elight.RESET_DISPLAYED				= false;
Elight.RESPONSE_PRIORITY_SENT 		= 0;
Elight.RESPONSE_PRIORITY_RETRIEVED	= 0;
Elight.DELAY 						= 1;
Elight.horizontal_slide 			= null;
Elight.results_slide 				= null;
Elight.CACHE 						= new Cache();
Elight.SEARCH_FOR_EXP 				= 'Elight.searchfor()';
Elight.input_blurred = true;

/* <search_results> */
Elight.getSearchResults = function(data, response_priority, searched_for) {

	if(response_priority < Elight.RESPONSE_PRIORITY_RETRIEVED)
		return;
		
	Elight.RESPONSE_PRIORITY_RETRIEVED = response_priority;
	
	var container = $(Elight.RESULTS_ID);
	jQuery(container).empty();
	
	if(!data || data == null || data.length == 0) {
		Elight.addMessage("Sorry. No data was retrieved.", container);
		Elight.setWorking(false);
		return;
	}
	
	if(data.length == 1 && data[0].length == 1  && data[0][0].message != null) {
		Elight.addMessage(data[0][0].message, container);
		Elight.setWorking(false);
		return;
	}
	
	Elight.addSearchResults(data, container);
	Elight.CACHE.setItem(searched_for, data, 	{
													expirationAbsolute:		null,   
													expirationSliding:		60,   
													priority: 				CachePriority.Normal,  
													callback: 				null
     											}
	);  
}

Elight.addSearchResults = function(data, container) {

	if(data.length > 1)
		Elight.one_type = false;
	else
		Elight.one_type = true;
	
	for(var index=0; index<data.length; index++) {
	
		Elight.addResultEntry(data[index], container);
	}
	
	Elight.setWorking(false);
}

Elight.addResultEntry = function(entries, container) {

	if(entries.length == 0)
		return;
	
	if(!Elight.one_type) {
	
		type_container = document.createElement("DIV");
		type_container.setAttribute("class", "elightTypeContainer");
		var label_container = document.createElement("DIV");
		label_container.setAttribute("class", "elightLabelContainer");
		var label = document.createTextNode(entries[0].typeLabel);
		
		label_container.appendChild(label);
		type_container.appendChild(label_container);
		container.appendChild(type_container);
		
		container = type_container;
	}

	for(var index=0; index<entries.length; index++) {
	
		var entry = entries[index];
		var result = new ElightResult();
		result.addTitle(entry.title);
		result.addContents(entry.contents);
		result.addUrl(entry.url);
		result.addImgSrc(entry.iconUri);
		container.appendChild(result.getResultDOM());
	}
}

Elight.addMessage = function(message, container) {
	var messsage_dom = document.createElement("DIV");
	messsage_dom.setAttribute("class", "elightMessage");
	messsage_dom.appendChild(document.createTextNode(message));
	container.appendChild(messsage_dom);
}
/* </search_results> */
/* <events> */

// 		<keyboard>
Elight.searchFieldKeyup = function(e) {

	if($(Elight.SEARCH_INPUT_ID).value == "" && Elight.RESET_DISPLAYED) {
	
		Elight.RESET_DISPLAYED = false;
		jQuery("#elightInputText .reset").hide();
	
	} else if($(Elight.SEARCH_INPUT_ID).value != "" && !Elight.RESET_DISPLAYED) {

		Elight.RESET_DISPLAYED = true;
		jQuery("#elightInputText .reset").show();
	}

	if(Elight.SEARCHING)
		return;
		
	Elight.SEARCHING = true;
	window.setTimeout(Elight.SEARCH_FOR_EXP, Elight.DELAY);
	Elight.DELAY = 1000;
}
// 		</keyboard>

//		<mouse>
Elight.mouseover = function() {

	Elight.MOUSE_OVER = true;
}

Elight.mouseout = function() {
	Elight.MOUSE_OVER = false;
}
//		</mouse>
//		<reactions>
Elight.setWorking = function(working) {

	if(working) {
		jQuery("#elightInputText .reset").removeClass("resetnotworking");
		jQuery("#elightInputText .reset").toggleClass("resetworking");
	} else {
		jQuery("#elightInputText .reset").removeClass("resetworking");
		jQuery("#elightInputText .reset").toggleClass("resetnotworking");
	}

	if(!$(Elight.SEARCH_BUTTON_ID))
		return;

	if(working)
		$(Elight.SEARCH_BUTTON_ID).src = elight_working_uri;
	else
		$(Elight.SEARCH_BUTTON_ID).src = elight_search_uri;
}

Elight.searchfor = function() {

		Elight.setWorking(true);
		var search_for = $(Elight.SEARCH_INPUT_ID).value;
		
		if(search_for != null && search_for != "") {
		
			var cdata = Elight.CACHE.getItem(search_for);
			if(cdata != null) {
				jQuery($(Elight.RESULTS_ID)).empty();
				Elight.addSearchResults(cdata, $(Elight.RESULTS_ID));
			} else {
			
				ElightSearchResults.search(search_for, elight_pu_param, 
			
					{
						callback: function(result) {
							Elight.getSearchResults(result, Elight.RESPONSE_PRIORITY_SENT++, search_for);
						}
					}
				);
			}
		} else {
			Elight.setWorking(false);
		}
		
		if(Elight.results_slide.state == Elight.SLIDED_OUT) {
			Elight.results_slide.state = Elight.SLIDED_IN;
			Elight.results_slidein();
		}
		
		if(search_for != $(Elight.SEARCH_INPUT_ID).value)
			Elight.searchfor();
		else
			Elight.SEARCHING = false;
}
//		</reactions>

/* </events> */
/* <fx> */
Elight.resultsSlideout = function() {

	$(Elight.RESULTS_ID).style.overflow = 'hidden';
	Elight.results_slide.slideOut();
}

Elight.results_slidein = function() {

	Elight.results_slide.slideIn();
	$(Elight.RESULTS_ID).style.overflow = 'auto';
}

Elight.slideout = function() {

		if(Elight.horizontal_slide.state == Elight.SLIDED_IN && elight_hidden) {
			
			Elight.horizontal_slide.state = Elight.SLIDED_OUT;
			Elight.horizontal_slide.slideOut();
		}
		
		if(Elight.results_slide.state == Elight.SLIDED_IN) {
		
			jQuery($(Elight.RESULTS_ID)).empty();

			Elight.results_slide.state = Elight.SLIDED_OUT;		
			Elight.resultsSlideout();

		}
}
/* </fx> */

/* <elight_result> */
ElightResult.prototype.addTitle = function(title) {

	this.title = title;

	if(this.title && this.title != null)
		this.title = this.title.cropEnd(Elight.CROP_END_TITLE, "...");
}

ElightResult.prototype.addContents = function(contents) {
	this.contents = contents;
}

ElightResult.prototype.addUrl = function(url) {

	this.url = url;
}

ElightResult.prototype.addImgSrc = function(img_src) {

	this.img_src = img_src;
}

ElightResult.prototype.getGenericResultObject = function() {
	
	if(ElightResult.genericResultObject && ElightResult.genericResultObject != null) {

		return ElightResult.genericResultObject;
	}
	
	ElightResult.genericResultObject = {};
	
	ElightResult.genericResultObject.resultContainerDIV = document.createElement('DIV');
	ElightResult.genericResultObject.resultContainerDIV.setAttribute("class", "elightResultContainer");
	
	ElightResult.genericResultObject.resultSubContainerDIV = document.createElement('DIV');
	ElightResult.genericResultObject.resultSubContainerDIV.setAttribute("class", "elightResultSubContainer");
	
	ElightResult.genericResultObject.resultHeaderDIV = document.createElement('DIV');
	ElightResult.genericResultObject.resultHeaderDIV.setAttribute("class", "res_header");
	
	var corner = document.createElement('DIV');
	corner.setAttribute("class", "res_corner");
	ElightResult.genericResultObject.resultHeaderDIV.appendChild(corner);
	corner = corner.cloneNode(false);
	corner.setAttribute("class", "res_bar");
	ElightResult.genericResultObject.resultHeaderDIV.appendChild(corner);
	
	ElightResult.genericResultObject.resultFooterDIV = ElightResult.genericResultObject.resultHeaderDIV.cloneNode(true);
	ElightResult.genericResultObject.resultFooterDIV.setAttribute("class", "res_footer");
	
	ElightResult.genericResultObject.resultDIV = document.createElement('DIV');
	ElightResult.genericResultObject.resultDIV.setAttribute("class", "elightResult");
	
	ElightResult.genericResultObject.titleDIV = document.createElement('DIV');
	ElightResult.genericResultObject.titleDIV.setAttribute("class", "elightTitle");
	
	ElightResult.genericResultObject.titleIMG = document.createElement('IMG');
	ElightResult.genericResultObject.titleIMG.setAttribute("class", "elightTitleImg");
	ElightResult.genericResultObject.titleIMG.setAttribute("src", elight_site_img_uri);
	
	ElightResult.genericResultObject.contentsDIV = document.createElement('DIV');
	ElightResult.genericResultObject.contentsDIV.setAttribute("class", "elightContents");
	
	ElightResult.genericResultObject.urlDIV = document.createElement('DIV');
	ElightResult.genericResultObject.urlDIV.setAttribute("class", "elightUrl");
	
	return ElightResult.genericResultObject;
}

ElightResult.prototype.getResultDOM = function() {
	
	var gen_res_obj = this.getGenericResultObject();
	var result_container_dom = gen_res_obj.resultContainerDIV.cloneNode(false);
	var result_sub_container_dom = gen_res_obj.resultSubContainerDIV.cloneNode(false);
	var result_dom = gen_res_obj.resultDIV.cloneNode(false);
	var title_dom = gen_res_obj.titleDIV.cloneNode(false);
	var title_img_dom = gen_res_obj.titleIMG.cloneNode(false);
	var contents_dom = gen_res_obj.contentsDIV.cloneNode(false);
	var url_dom = gen_res_obj.urlDIV.cloneNode(false);
	var header_dom = gen_res_obj.resultHeaderDIV.cloneNode(true);
	var footer_dom = gen_res_obj.resultFooterDIV.cloneNode(true);
	
	title_dom.appendChild(title_img_dom);
	result_dom.appendChild(title_dom);
	result_dom.appendChild(contents_dom);
	result_dom.appendChild(url_dom);
	result_sub_container_dom.appendChild(header_dom);
	result_sub_container_dom.appendChild(result_dom);
	result_sub_container_dom.appendChild(footer_dom);
	result_container_dom.appendChild(result_sub_container_dom);
	
	if(this.url && this.url != null) {
	
		var link = document.createElement('a');
		link.setAttribute("href", this.url);
		link.appendChild(result_container_dom);
		result_container_dom = link;
	}
	
	if(this.img_src && this.img_src != null) {
		title_img_dom.setAttribute("src", this.img_src);
	}
	
	if(this.title && this.title != null)
		title_dom.appendChild(document.createTextNode(this.title));
		
	if(this.contents && this.contents != null)
		insertNodesToContainer(this.contents, contents_dom);
		
	if(this.url && this.url != null)
		url_dom.appendChild(document.createTextNode(this.url.cropEnd(Elight.CROP_END_URL, "...")));
	
	return result_container_dom;
}
/* </elight_result> */

window.addEvent('domready', function() {

	if(elight_hidden) {
		Elight.horizontal_slide = new Fx.Slide('elightInputText', {mode: 'horizontal', duration: 300}).hide();
		Elight.horizontal_slide.state = Elight.SLIDED_OUT;
	} else {
		Elight.horizontal_slide = new Fx.Slide('elightInputText', {mode: 'horizontal', duration: 300});
		Elight.horizontal_slide.state = Elight.SLIDED_IN;
	}

	if($(Elight.SEARCH_BUTTON_ID))
		$(Elight.SEARCH_BUTTON_ID).addEvent('click', function(e) {
			e = new Event(e);
	
			if(Elight.horizontal_slide.state == Elight.SLIDED_OUT) {
			
				$(Elight.SEARCH_INPUT_ID).focus();
				Elight.horizontal_slide.state = Elight.SLIDED_IN;
				Elight.horizontal_slide.slideIn();
				
			} else {
				Elight.slideout();
			}
	
			e.stop();
		});
	
	Elight.results_slide = new Fx.Slide(Elight.RESULTS_AND_POINTER_ID, {mode: 'vertical'}).hide();
	
	Elight.SEARCHING = false;

	Elight.results_slide.state = Elight.SLIDED_OUT;
//	Elight.results_slide.state = Elight.SLIDED_IN;
	$(Elight.SEARCH_INPUT_ID).addEvent('keyup', Elight.searchFieldKeyup);
	
//		<positioning>
	var elight_top_position = getAbsoluteTop(Elight.INPUT_ID);
	var elight_left_position = getAbsoluteLeft(Elight.INPUT_ID);
	
	var elight_input_div = $(Elight.INPUT_ID).getElementsByTagName('div')[0];
	var elight_output_div = $(Elight.OUTPUT_ID).getElementsByTagName('div')[0];

	elight_input_div.style.top = (elight_top_position + 8)+"px";
	elight_input_div.style.left = (elight_left_position + 40)+"px";
	
	elight_output_div.style.top = (elight_top_position + 8+25)+"px";
	elight_output_div.style.left = (elight_left_position + 40)+"px";
//		</positioning>
	
	Elight.inputFieldStylements();
	
	if($(Elight.SEARCH_BUTTON_ID)) {
		$(Elight.SEARCH_BUTTON_ID).addEvent('mouseover', Elight.mouseover);
		$(Elight.SEARCH_BUTTON_ID).addEvent('mouseout', Elight.mouseout);
	}
	
	$(Elight.INPUT_TEXT_ID).addEvent('mouseover', Elight.mouseover);
	$(Elight.INPUT_TEXT_ID).addEvent('mouseout', Elight.mouseout);
	$(Elight.RESULTS_ID).addEvent('mouseover', Elight.mouseover);
	$(Elight.RESULTS_ID).addEvent('mouseout', Elight.mouseout);
	$(Elight.SEARCH_INPUT_ID).addEvent('blur', Elight.inputFieldOnBlur);
	$(Elight.SEARCH_INPUT_ID).addEvent('click', Elight.inputFieldOnClick);
	
	jQuery("#elightInputText .reset").bind("click", function(){
	
		$(Elight.SEARCH_INPUT_ID).value = "";
		$(Elight.SEARCH_INPUT_ID).focus();
		Elight.slideout();
		Elight.RESET_DISPLAYED = false;
		jQuery("#elightInputText .reset").hide();
	});
});

Elight.inputFieldOnBlur = function() {
	
	if($(Elight.SEARCH_INPUT_ID).value != "")
		return;
		
	$(Elight.SEARCH_INPUT_ID).value = elight_input_field_initial_value;
	jQuery($(Elight.INPUT_TEXT_ID)).toggleClass("blurred");
	Elight.input_blurred = true;
}

Elight.inputFieldOnClick = function() {

	if(Elight.input_blurred && $(Elight.SEARCH_INPUT_ID).value == elight_input_field_initial_value) {
	
		$(Elight.SEARCH_INPUT_ID).value = "";
		jQuery($(Elight.INPUT_TEXT_ID)).removeClass("blurred");
		Elight.input_blurred = false;
	}
}

Elight.inputFieldStylements = function() {

	$(Elight.SEARCH_INPUT_ID).value = elight_input_field_initial_value;
	
	if(isSafariBrowser()) {
		$(Elight.SEARCH_INPUT_ID).type = 'search';
		return;
	}

	jQuery("#elightInputText .left").append(document.createElement("img"));
	jQuery("#elightInputText .right").append(document.createElement("img"));
	
	jQuery("#elightInputText .left img").attr("src", elight_input_left_img_uri);
	jQuery("#elightInputText .right img").attr("src", elight_input_right_img_uri);
}

window.addEvent('click', function() {

	if(!Elight.MOUSE_OVER)
		Elight.slideout();
});