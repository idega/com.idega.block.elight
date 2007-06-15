package com.idega.block.elight.presentation;

import java.util.List;

import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.ui.AbstractChooser;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.2 $
 *
 * Last modified: $Date: 2007/06/15 13:20:26 $ by $Author: civilis $
 *
 */
public class ElightPluginsUsedChooser extends AbstractChooser {
		
	private List<String> values;
	
	public ElightPluginsUsedChooser(String instanceId, String method) {
		super(false);
		addForm(false);
		setInstanceId(instanceId);
		setMethod(method);
	}
	
	public PresentationObject getChooser(IWContext iwc, IWBundle bundle) {
		ElightPluginsUsedBlock plugins_used = new ElightPluginsUsedBlock();
		plugins_used.setValues(values);
		
		return plugins_used;
	}
	
	public Class getChooserWindowClass() {
		return ElightPluginsUsedBlock.class;
	}
	
	public void setValues(List<String> values) {
		this.values = values;
	}
	
	@Override
	protected String getChooserHelperVarName() {
		return "elight_chooser_helper";
	}
}