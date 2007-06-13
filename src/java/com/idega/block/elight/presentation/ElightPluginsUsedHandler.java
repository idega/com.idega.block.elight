package com.idega.block.elight.presentation;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.presentation.ICPropertyHandler;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.ui.CheckBoxGroup;
import com.idega.presentation.ui.DropdownMenu;

public class ElightPluginsUsedHandler implements ICPropertyHandler {

	public List getDefaultHandlerTypes() {
		return null;
	}

	public PresentationObject getHandlerObject(String name, String stringValue, IWContext iwc, boolean oldGenerationHandler, String instanceId, String method) {
		CheckBoxGroup menu = new CheckBoxGroup(name);
		
		menu.addOption("xx", "smth");
		menu.addOption("xx1", "smth2");
		menu.addOption("xx2", "smth3");
		
		IWMainApplication iwma = IWMainApplication.getDefaultIWMainApplication();
		
//		try {
//			VideoServices videoServices = (VideoServices) IBOLookup.getServiceInstance(iwma.getIWApplicationContext(), VideoServices.class);
//			Map services = videoServices.getVideoServices();
//			Iterator iterator = services.keySet().iterator();
//			while(iterator.hasNext()) {
//				String sourceId = (String) iterator.next();
//				VideoService source = videoServices.getVideoService(sourceId);
//				menu.addMenuElement(source.getId(), source.getName());
//			}
//		} catch (IBOLookupException ile) {
//			throw new IBORuntimeException(ile);
//		} catch (RemoteException re) {
//			//TODO
//		}
		//menu.setSelectedElement(stringValue);
//		Table table = new Table();
//		table.add(menu);
		return menu;
	}

	public void onUpdate(String[] values, IWContext iwc) {}

}
