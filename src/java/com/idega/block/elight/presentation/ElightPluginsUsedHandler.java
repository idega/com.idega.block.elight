package com.idega.block.elight.presentation;

import java.util.List;

import com.idega.core.builder.business.ICBuilderConstants;
import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.util.StringUtil;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/06/14 18:56:23 $ by $Author: civilis $
 *
 */
public class ElightPluginsUsedHandler implements ICPropertyHandler {

	public List getDefaultHandlerTypes() {
		return null;
	}

	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc, boolean oldGenerationHandler, String instanceId, String method) {
		
		ElightPluginsUsedChooser chooser = new ElightPluginsUsedChooser(instanceId, method);
		chooser.setValues(StringUtil.getValuesFromString(stringValue, ICBuilderConstants.BUILDER_MODULE_PROPERTY_VALUES_SEPARATOR));
		
		return chooser;
	}
	
	public void onUpdate(String[] values, IWContext iwc) {}
}