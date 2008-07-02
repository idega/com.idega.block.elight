package com.idega.block.elight.presentation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.custom.htmlTag.HtmlTag;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import com.idega.block.web2.business.Web2Business;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.presentation.IWContext;
import com.idega.util.CoreConstants;
import com.idega.util.CoreUtil;
import com.idega.util.expression.ELUtil;
import com.idega.webface.WFDivision;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.22 $
 *
 * Last modified: $Date: 2008/07/02 19:23:30 $ by $Author: civilis $
 *
 */
public class ELight extends IWBaseComponent {
	
	private static final String elight_id 								= "elight";
	private static final String elight_search_button_id 				= "elightSearchButton";
	private static final String elight_search_input_id	 				= "elightInputText";
	private static final String elight_input_text_id	 				= "elightInputTextContainer";
	private static final String elight_results_id 						= "elightResults";
	private static final String elight_pointer_id 						= "elightPointer";
	private static final String elight_results_and_pointer_id 			= "elightResultsAndPointer";
	
	private static final String elight_input_class 						= "elinput";
	private static final String elight_output_class 					= "eloutput";
	private static final String elight_search_input_class 				= "g-prettysearch";
	private static final String elight_input_text_class 				= "search-wrapper";
	private static final String elight_elright_class 					= "elright";
	private static final String elight_elleft_class 					= "elleft";
	private static final String elight_as_header_class 					= "as_header";
	private static final String elight_as_left_corner_class 			= "as_corner_left";
	private static final String elight_as_right_corner_class 			= "as_corner_right";
	private static final String elight_as_bar_class 					= "as_bar";
	private static final String elight_as_footer_class 					= "as_footer";
	
	private static final String span_tag				 				= "span";
	
	private static final String ELIGHT_SEARCH_BUTTON_IMG 				= "images/elightSearchButton.png";
	private static final String ELIGHT_WORKING_IMG 						= "images/elightWorking.gif";
	private static final String ELIGHT_SITE_IMG 						= "images/Site16.png";
	
	private static final String ELIGHT_JS_SRC 							= "javascript/elight.js";
	private static final String ELIGHT_SEARCH_RESULTS_JS_SRC 			= "/dwr/interface/ElightSearchResults.js";
	private static final String CACHE_JS_SRC 							= "javascript/MonsurCache.js";
	
	private static final String ELIGHT_CSS_SRC 							= "style/elight.css";
	
	public static final String IW_BUNDLE_IDENTIFIER 					= "com.idega.block.elight";
	private List<String> plugins_used;
	private boolean hidden = false;
	
	public ELight() {
		
		super();
		setRendererType(null);
	}
	
	@SuppressWarnings("cast")
	@Override
	protected void initializeComponent(FacesContext context) {
		
		Application application = context.getApplication();
		
		WFDivision elight_div = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		elight_div.setId(elight_id);
		
		WFDivision input_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		input_division.setStyleClass(elight_input_class);
		
		WFDivision output_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		output_division.setStyleClass(elight_output_class);
		
		HtmlTag left_span = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		left_span.setValue(span_tag);
		left_span.setStyleClass(elight_elleft_class);
		
		WFDivision input_text_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		input_text_division.setId(elight_input_text_id);
		input_text_division.setStyleClass(elight_input_text_class+" blurred");
		
		HtmlTag right_span = (HtmlTag)application.createComponent(HtmlTag.COMPONENT_TYPE);
		right_span.setValue(span_tag);
		right_span.setStyleClass(elight_elright_class);
		
		WFDivision reset_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		reset_division.setStyleClass("elreset resetnotworking");
		
		WFDivision pointer_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		pointer_division.setId(elight_pointer_id);
		
		WFDivision results_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		results_division.setId(elight_results_id);
		
		WFDivision results_and_pointer_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		results_and_pointer_division.setId(elight_results_and_pointer_id);
		
		WFDivision header_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		header_division.setStyleClass(elight_as_header_class);
		
		WFDivision header_left_corner_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		header_left_corner_division.setStyleClass(elight_as_left_corner_class);
		WFDivision footer_left_corner_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		footer_left_corner_division.setStyleClass(elight_as_left_corner_class);
		WFDivision header_right_corner_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		header_right_corner_division.setStyleClass(elight_as_right_corner_class);
		WFDivision footer_right_corner_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		footer_right_corner_division.setStyleClass(elight_as_right_corner_class);
		
		WFDivision bar_division1 = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		bar_division1.setStyleClass(elight_as_bar_class);
		WFDivision bar_division2 = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		bar_division2.setStyleClass(elight_as_bar_class);
		
		WFDivision footer_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		footer_division.setStyleClass(elight_as_footer_class);
		
		header_division.add(header_left_corner_division);
		header_division.add(bar_division1);
		header_division.add(header_right_corner_division);
		footer_division.add(footer_left_corner_division);
		footer_division.add(bar_division2);
		footer_division.add(footer_right_corner_division);
		
		if(hidden) {
			HtmlGraphicImage search_button = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
			search_button.setId(elight_search_button_id);
			search_button.setValue(IWMainApplication.getIWMainApplication(context).getBundle(IW_BUNDLE_IDENTIFIER).getImageURI(ELIGHT_SEARCH_BUTTON_IMG));
			
			input_division.add(search_button);
		}
		
		HtmlInputText search_input = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		search_input.setId(elight_search_input_id);
		search_input.setStyleClass(elight_search_input_class);
		search_input.setOnkeypress("return !isEnterEvent(event);");
		search_input.setAutocomplete("off");
		search_input.setAccesskey("s");
//		TODO: check if accessd. then expand (slidein), if hidden

		input_text_division.add(left_span);
		input_text_division.add(search_input);
		input_text_division.add(right_span);
		input_text_division.add(reset_division);
		
		input_division.add(input_text_division);
		
		results_and_pointer_division.add(pointer_division);
		results_and_pointer_division.add(header_division);
		results_and_pointer_division.add(results_division);
		results_and_pointer_division.add(footer_division);
		output_division.add(results_and_pointer_division);
		
		elight_div.add(input_division);
		elight_div.add(output_division);
		 
		Map<Object, UIComponent> facets = getFacets();
		facets.put(elight_id, elight_div);
		
		addClientResources(context);
	}
	
	@Override
	public boolean getRendersChildren() {
		return true;
	}
	
	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		
		super.encodeChildren(context);
		
		UIComponent elight_div = getFacet(elight_id);
		
		if(elight_div != null) {
			
			elight_div.setRendered(true);
			renderChild(context, elight_div);
		}
	}
	
	protected void addClientResources(FacesContext context) {
		
		IWMainApplication iwma = IWMainApplication.getIWMainApplication(context);
		Web2Business web2 = ELUtil.getInstance().getBean(Web2Business.class);
		
		try {
			AddResource resource = AddResourceFactory.getInstance(context);
			resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, CoreUtil.getCoreBundle().getVirtualPathWithFileNameString(CACHE_JS_SRC));
			resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, web2.getBundleURIToMootoolsLib());
			resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, web2.getBundleURIToJQueryLib());
			resource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, "jQuery.noConflict();");
			
			IWBundle bundle = iwma.getBundle(IW_BUNDLE_IDENTIFIER);
			resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, bundle.getVirtualPathWithFileNameString(ELIGHT_JS_SRC));
			resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, ELIGHT_SEARCH_RESULTS_JS_SRC);
			resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, CoreConstants.DWR_ENGINE_SCRIPT);
			
			resource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, 
					new StringBuilder("var elight_working_uri = '")
					.append(bundle.getImageURI(ELIGHT_WORKING_IMG))
					.append("';\n")
					.append("var elight_search_uri = '")
					.append(bundle.getImageURI(ELIGHT_SEARCH_BUTTON_IMG))
					.append("';\n")
					.append("var elight_pu_param = new Array(")
					.append(constructPluginsUsedArrayValues(plugins_used))
					.append(");\n")
					.append("var elight_hidden = ")
					.append(hidden ? "true" : "false")
					.append(";\n")
					.append("var elight_input_field_initial_value = '")
					.append(bundle.getResourceBundle(IWContext.getIWContext(context)).getLocalizedString("input_field.default", "Search"))
					.append("';\n")
					.append("var elight_site_img_uri = '")
					.append(bundle.getImageURI(ELIGHT_SITE_IMG))
					.append("';")
					.toString()
			);
			resource.addStyleSheet(context, AddResource.HEADER_BEGIN, bundle.getVirtualPathWithFileNameString(ELIGHT_CSS_SRC));
			
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setPluginsUsed(List<String> plugins_used) {
		this.plugins_used = plugins_used;
	}
	
	private String constructPluginsUsedArrayValues(List<String> plugins_used) {
	
		if(plugins_used == null || plugins_used.isEmpty())
			return "";
		
		StringBuilder constructed = new StringBuilder();
		
		for (Iterator<String> iter = plugins_used.iterator(); iter.hasNext();) {
			
			constructed.append("'").append(iter.next()).append("'");
			
			if(iter.hasNext())
				constructed.append(", ");
		}
		
		return constructed.toString();
	}
	
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}
}