package com.idega.block.elight.presentation;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.myfaces.component.html.ext.HtmlGraphicImage;
import org.apache.myfaces.component.html.ext.HtmlInputText;
import org.apache.myfaces.renderkit.html.util.AddResource;
import org.apache.myfaces.renderkit.html.util.AddResourceFactory;

import com.idega.block.web2.business.Web2Business;
import com.idega.business.SpringBeanLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWBaseComponent;
import com.idega.util.CoreUtil;
import com.idega.webface.WFDivision;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.8 $
 *
 * Last modified: $Date: 2007/06/22 16:24:38 $ by $Author: civilis $
 *
 */
public class ELight extends IWBaseComponent {
	
	private static final String elight_id = "elight";
	private static final String elight_input_id = "elightInput";
	private static final String elight_output_id = "elightOutput";
	private static final String elight_search_button_id = "elightSearchButton";
	private static final String elight_search_input_id = "elightSearchInput";
	private static final String elight_input_text_id = "elightInputText";
	private static final String elight_results_id = "elightResults";
	private static final String elight_pointer_id = "elightPointer";
	private static final String elight_results_and_pointer_id = "elightResultsAndPointer";
	
	private static final String ELIGHT_SEARCH_BUTTON_SRC = "images/elightSearchButton.png";
	private static final String ELIGHT_WORKING_SRC = "images/elightWorking.gif";
	private static final String ELIGHT_SITE_ICON = "images/Site16.png";
	
	private static final String ELIGHT_JS_SRC = "javascript/elight.js";
	private static final String ELIGHT__SEARCH_RESULTS_JS_SRC = "/dwr/interface/ElightSearchResults.js";
	private static final String DWR_ENGINE_SRC = "/dwr/engine.js";
	private static final String CACHE_JS_SRC = "javascript/MonsurCache.js";
	
	private static final String ELIGHT_CSS_SRC = "style/elight.css";
	
	public static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.elight";
	private List<String> plugins_used;
	private boolean hidden = true;
	
	public ELight() {
		
		super();
		setRendererType(null);
	}
	
	@Override
	protected void initializeComponent(FacesContext context) {
		
		Application application = context.getApplication();
		
		WFDivision elight_div = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		elight_div.setId(elight_id);
		
		WFDivision input_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		input_division.setId(elight_input_id);
		
		WFDivision output_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		output_division.setId(elight_output_id);
		
		WFDivision input_text_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		input_text_division.setId(elight_input_text_id);
		
		WFDivision pointer_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		pointer_division.setId(elight_pointer_id);
		
		WFDivision results_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		results_division.setId(elight_results_id);
		
		WFDivision results_and_pointer_division = (WFDivision)application.createComponent(WFDivision.COMPONENT_TYPE);
		results_and_pointer_division.setId(elight_results_and_pointer_id);
		
		HtmlGraphicImage search_button = (HtmlGraphicImage) application.createComponent(HtmlGraphicImage.COMPONENT_TYPE);
		search_button.setId(elight_search_button_id);
		search_button.setValue(IWMainApplication.getIWMainApplication(context).getBundle(IW_BUNDLE_IDENTIFIER).getImageURI(ELIGHT_SEARCH_BUTTON_SRC));
		
		HtmlInputText search_input = (HtmlInputText) application.createComponent(HtmlInputText.COMPONENT_TYPE);
		search_input.setId(elight_search_input_id);
		search_input.setAutocomplete("off");
		search_input.setAccesskey("s");
//		TODO: check if accessd. then expand (slidein)

		input_division.add(search_button);
		input_text_division.add(search_input);
		input_division.add(input_text_division);
		
		results_and_pointer_division.add(pointer_division);
		results_and_pointer_division.add(results_division);
		output_division.add(results_and_pointer_division);
		
		elight_div.add(input_division);
		 elight_div.add(output_division);
		
		getFacets().put(elight_id, elight_div);
		
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
	
	protected Web2Business getWeb2Service(IWApplicationContext iwc) {
		
		return (Web2Business) SpringBeanLookup.getInstance().getSpringBean(iwc, Web2Business.class);
	}
	
	protected void addClientResources(FacesContext context) {
		
		IWMainApplication iwma = IWMainApplication.getIWMainApplication(context);
		Web2Business web2_business = getWeb2Service(iwma.getIWApplicationContext());
		
		if (web2_business != null) {
			
			try {
				AddResource resource = AddResourceFactory.getInstance(context);
				resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, CoreUtil.getCoreBundle().getVirtualPathWithFileNameString(CACHE_JS_SRC));
				resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, web2_business.getBundleURIToMootoolsLib());
				
				IWBundle bundle = iwma.getBundle(IW_BUNDLE_IDENTIFIER);
				resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, bundle.getVirtualPathWithFileNameString(ELIGHT_JS_SRC));
				resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, ELIGHT__SEARCH_RESULTS_JS_SRC);
				resource.addJavaScriptAtPosition(context, AddResource.HEADER_BEGIN, DWR_ENGINE_SRC);
				
				resource.addInlineScriptAtPosition(context, AddResource.HEADER_BEGIN, 
						new StringBuilder("var elight_working_uri = '")
						.append(bundle.getImageURI(ELIGHT_WORKING_SRC))
						.append("';\n")
						.append("var elight_search_uri = '")
						.append(bundle.getImageURI(ELIGHT_SEARCH_BUTTON_SRC))
						.append("';\n")
						.append("var elight_pu_param = new Array(")
						.append(constructPluginsUsedArrayValues(plugins_used))
						.append(");\n")
						.append("var elight_hidden = ")
						.append(hidden ? "true" : "false")
						.append(";\n")
						.append("var elight_site_img_uri = '")
						.append(bundle.getImageURI(ELIGHT_SITE_ICON))
						.append("';")
						.toString()
				);
				resource.addStyleSheet(context, AddResource.HEADER_BEGIN, bundle.getVirtualPathWithFileNameString(ELIGHT_CSS_SRC));
				
			} catch (RemoteException e) {
				e.printStackTrace();
//				TODO: log
			}
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