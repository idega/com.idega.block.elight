if(Elight == null) var Elight = {};
if(ElightResult == null) var ElightResult = function() {};

/* ------ elight ------ */

Elight.SEARCH_BUTTON_ID 			= 'elightSearchButton';
Elight.INPUT_ID 					= 'elightInput';
Elight.INPUT_TEXT_ID				= 'elightInputText';
Elight.OUTPUT_ID 					= 'elightOutput';
Elight.RESULTS_ID 					= 'elightResults';
Elight.SEARCH_INPUT_ID 				= 'elightSearchInput';
Elight.SLIDED_OUT					= 1;
Elight.SLIDED_IN					= 2;
Elight.WORD_WRAP_TITLE				= 30;
Elight.WORD_WRAP_URL				= 50;
Elight.CROP_END_TITLE				= 30;
Elight.CROP_END_URL					= 45;
Elight.elightSearchInput_WIDTH1		= '150px';
Elight.elightSearchInput_WIDTH2		= '290px';

Elight.MOUSE_OVER 					= false;

Elight.getSearchResults = function(data) {

	var container = $(Elight.RESULTS_ID);
	
	removeChildren(container);
	
	if(!data || data == null || data.length == 0) {
		Elight.addMessage("Sorry. No data was retrieved.", container);
		$(Elight.SEARCH_BUTTON_ID).src = elight_search_uri;
		return;
	}
	
	if(data.length == 1 && data[0].length == 1  && data[0][0].message != null) {
		Elight.addMessage(data[0][0].message, container);
		$(Elight.SEARCH_BUTTON_ID).src = elight_search_uri;
		return;
	}
	
	if(data.length > 1)
		Elight.one_type = false;
	else
		Elight.one_type = true;
	
	for(var index=0; index<data.length; index++) {
		
		Elight.addResultEntry(data[index], container);
	}
	
	$(Elight.SEARCH_BUTTON_ID).src = elight_search_uri;
}


Elight.addResultEntry = function(entries, container) {

	if(entries.length == 0)
		return;
	
	if(!Elight.one_type) {
	
		// create type container
		
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
		container.appendChild(result.getResultDOM());
	}
}

Elight.addMessage = function(message, container) {
	var messsage_dom = document.createElement("DIV");
	messsage_dom.setAttribute("class", "elightMessage");
	messsage_dom.appendChild(document.createTextNode(message));
	container.appendChild(messsage_dom);
}

Elight.mouseover = function() {

	Elight.MOUSE_OVER = true;
}

Elight.mouseout = function() {
	Elight.MOUSE_OVER = false;
}

Elight.horizontal_slide 	= null;
Elight.results_slide 		= null;

Elight.results_slideout = function() {

	$(Elight.RESULTS_ID).style.overflow = 'hidden';
	Elight.results_slide.slideOut();
}

Elight.results_slidein = function() {

	removeChildren($(Elight.RESULTS_ID));
	Elight.results_slide.slideIn();
	$(Elight.RESULTS_ID).style.overflow = 'auto';
}

Elight.slideout = function() {

		if(Elight.horizontal_slide.state == Elight.SLIDED_IN) {
			
			Elight.horizontal_slide.state = Elight.SLIDED_OUT;
			Elight.horizontal_slide.slideOut();
		}
		
		if(Elight.results_slide.state == Elight.SLIDED_IN) {

			Elight.results_slide.state = Elight.SLIDED_OUT;		
			Elight.results_slideout();

//			$(Elight.SEARCH_INPUT_ID).style.width = Elight.elightSearchInput_WIDTH1;
		}
}

/* ------ elight ---(END)--- */

/* ------ elight result ------ */
ElightResult.prototype.addTitle = function(title) {

	this.title = title;

	if(this.title && this.title != null)
		//this.title = this.title.wordWrap(Elight.WORD_WRAP_TITLE, " ", true);
		this.title = this.title.cropEnd(Elight.CROP_END_TITLE, "...");
}

ElightResult.prototype.addContents = function(contents) {
	this.contents = contents;
}

ElightResult.prototype.addUrl = function(url) {

	this.url = url;
}

ElightResult.prototype.getGenericResultObject = function() {
	
	if(ElightResult.genericResultObject && ElightResult.genericResultObject != null) {

		return ElightResult.genericResultObject;
	}
	
	ElightResult.genericResultObject = {};
	ElightResult.genericResultObject.resultDIV = document.createElement('DIV');
	ElightResult.genericResultObject.resultDIV.setAttribute("class", "elightResult");
	
	ElightResult.genericResultObject.titleDIV = document.createElement('DIV');
	ElightResult.genericResultObject.titleDIV.setAttribute("class", "elightTitle");
	
	ElightResult.genericResultObject.titleIMG = document.createElement('IMG');
	ElightResult.genericResultObject.titleIMG.setAttribute("class", "elightTitleImg");
	ElightResult.genericResultObject.titleIMG.setAttribute("src", elight_site_img_uri);
	
	ElightResult.genericResultObject.titleDIV.appendChild(ElightResult.genericResultObject.titleIMG);
	
	ElightResult.genericResultObject.contentsDIV = document.createElement('DIV');
	ElightResult.genericResultObject.contentsDIV.setAttribute("class", "elightContents");
	
	ElightResult.genericResultObject.urlDIV = document.createElement('DIV');
	ElightResult.genericResultObject.urlDIV.setAttribute("class", "elightUrl");
	
	return ElightResult.genericResultObject;
}

ElightResult.prototype.getResultDOM = function() {
	
	var gen_res_obj = this.getGenericResultObject();
	var result_dom = gen_res_obj.resultDIV.cloneNode(false);
	var title_dom = gen_res_obj.titleDIV.cloneNode(true);
	var contents_dom = gen_res_obj.contentsDIV.cloneNode(false);
	var url_dom = gen_res_obj.urlDIV.cloneNode(false);
	
	result_dom.appendChild(title_dom);
	result_dom.appendChild(contents_dom);
	result_dom.appendChild(url_dom);
	
	if(this.url && this.url != null) {
	
		var link = document.createElement('a');
		link.setAttribute("href", this.url);
		link.appendChild(result_dom);
		result_dom = link;
	}
	
	if(this.title && this.title != null)
		title_dom.appendChild(document.createTextNode(this.title));
		
	if(this.contents && this.contents != null)
		insertNodesToContainer(this.contents, contents_dom);
		
	if(this.url && this.url != null)
		url_dom.appendChild(document.createTextNode(this.url.cropEnd(Elight.CROP_END_URL, "...")));
	
	return result_dom;
}

/* ------ elight result --(END)---- */

window.addEvent('domready', function() {
	
	if(elight_hidden) {
		Elight.horizontal_slide = new Fx.Slide('elightInputText', {mode: 'horizontal', duration: 300}).hide();
		Elight.horizontal_slide.state = Elight.SLIDED_OUT;
	} else {
		Elight.horizontal_slide = new Fx.Slide('elightInputText', {mode: 'horizontal', duration: 300});
		Elight.horizontal_slide.state = Elight.SLIDED_IN;
	}

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
	
	Elight.results_slide = new Fx.Slide(Elight.RESULTS_ID, {mode: 'vertical'}).hide();
	Elight.results_slide.state = Elight.SLIDED_OUT;
//	Elight.results_slide.state = Elight.SLIDED_IN;

	$(Elight.SEARCH_INPUT_ID).addEvent('keypress', function(e) {
		
		if(!isEnterEvent(e))
			return;
			
		$(Elight.SEARCH_BUTTON_ID).src = elight_working_uri;
		
		ElightSearchResults.search($(Elight.SEARCH_INPUT_ID).value, elight_pu_param, Elight.getSearchResults);
			
		e = new Event(e);
		
		Elight.results_slide.state = Elight.SLIDED_IN;
		Elight.results_slidein();
		
//		$(Elight.SEARCH_INPUT_ID).style.width = Elight.elightSearchInput_WIDTH2;

		e.stop();
	});
	
/*	var elight_scrolly = new Scroller(Elight.RESULTS_ID, {area: 80, velocity: 0.7});
	
	$(Elight.RESULTS_ID).addEvent('mouseover', elight_scrolly.start.bind(elight_scrolly));
	$(Elight.RESULTS_ID).addEvent('mouseout', elight_scrolly.stop.bind(elight_scrolly));
*/
	
//	positioning
	var elight_top_position = getAbsoluteTop(Elight.SEARCH_BUTTON_ID);
	var elight_left_position = getAbsoluteLeft(Elight.SEARCH_BUTTON_ID);
	
	var elight_input_div = $(Elight.INPUT_ID).getElementsByTagName('div')[0];
	var elight_output_div = $(Elight.OUTPUT_ID).getElementsByTagName('div')[0];

	elight_input_div.style.top = (elight_top_position + 8)+"px";
	elight_input_div.style.left = (elight_left_position + 40)+"px";
	
	elight_output_div.style.top = (elight_top_position + 8+25)+"px";
	elight_output_div.style.left = (elight_left_position + 40)+"px";
	
	if(isSafariBrowser()) {
	
		$(Elight.SEARCH_INPUT_ID).type = 'search';
		$(Elight.SEARCH_INPUT_ID).style.color = 'black';
		$(Elight.SEARCH_INPUT_ID).style.backgroundColor = 'white';
	}
	
	$(Elight.SEARCH_BUTTON_ID).addEvent('mouseover', Elight.mouseover);
	$(Elight.SEARCH_BUTTON_ID).addEvent('mouseout', Elight.mouseout);
	$(Elight.INPUT_TEXT_ID).addEvent('mouseover', Elight.mouseover);
	$(Elight.INPUT_TEXT_ID).addEvent('mouseout', Elight.mouseout);
	$(Elight.RESULTS_ID).addEvent('mouseover', Elight.mouseover);
	$(Elight.RESULTS_ID).addEvent('mouseout', Elight.mouseout);	
});

window.addEvent('click', function() {

	if(!Elight.MOUSE_OVER) {
	
		Elight.slideout();
	}
});