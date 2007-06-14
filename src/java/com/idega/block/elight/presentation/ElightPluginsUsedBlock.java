package com.idega.block.elight.presentation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.idega.core.search.business.SearchPlugin;
import com.idega.core.search.business.SearchPluginManager;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Label;
import com.idega.presentation.ui.util.AbstractChooserBlock;

/**
 * 
 * @author <a href="civilis@idega.com">Vytautas Čivilis</a>
 * @version $Revision: 1.1 $
 *
 * Last modified: $Date: 2007/06/14 18:56:23 $ by $Author: civilis $
 *
 */
public class ElightPluginsUsedBlock extends AbstractChooserBlock {

	private List<String> values;
	
	public void main(IWContext iwc) {
		Layer checkboxes = new Layer();
		
		Collection<SearchPlugin> plugins = SearchPluginManager.getInstance().getAllSearchPluginsInitialized(iwc.getIWMainApplication());
		
		if(values == null)
			values = new ArrayList<String>();
		
		for (SearchPlugin plugin : plugins) {
			
			try {
				CheckBox ch_box = new CheckBox("elightPluginUsedCheckbox");
				ch_box.setId(plugin.getSearchIdentifier());
				ch_box.setOnChange(getOnChangeCheckbox());
				
				if(values.contains(plugin.getSearchIdentifier()))
					ch_box.setChecked(true);
				else
					ch_box.setChecked(false);
				
				Layer checkbox_and_label = new Layer();
				checkbox_and_label.add(ch_box);
				checkbox_and_label.add(new Label(plugin.getSearchName(), ch_box));
				checkboxes.add(checkbox_and_label);
			} catch (Exception e) {
//				TODO: log
				e.printStackTrace();
			}
		}
		
		add(checkboxes);
		addClientResources();
	}
	
	protected void addClientResources() {
		add(
			new StringBuilder("<script type=\"text/javascript\">")
			.append("function elightChoosePlugin(checkbox) {")
			.append("if(!checkbox || checkbox == null)")
			.append("return;")
			.append("if(checkbox.checked) {")
			.append("addAdvancedProperty(checkbox.id, checkbox.id);")
			.append("} else {")
			.append("removeAdvancedProperty(checkbox.id);")
			.append("}} console.log('shit man');")
			.append("var elight_checkboxes = document.getElementsByName('elightPluginUsedCheckbox');")
			.append("if(elight_checkboxes &amp;&amp; elight_checkboxes != null)")
			.append("for(var index=0; index &lt; elight_checkboxes.length; index++)")
			.append("if(elight_checkboxes[index].checked)")
			.append("addAdvancedProperty(elight_checkboxes[index].id, elight_checkboxes[index].id);")
			.append("</script>")
			.toString()
		);
	}
	
	private String getOnChangeCheckbox() {
		
		return "elightChoosePlugin(this);";
	}
	
	@Override
	public boolean getChooserAttributes() {
		return false;
	}
	
	public void setValues(List<String> values) {
		this.values = values;
	}
}
