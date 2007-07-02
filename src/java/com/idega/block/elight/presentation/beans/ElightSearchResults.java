package com.idega.block.elight.presentation.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.idega.core.search.business.Search;
import com.idega.core.search.business.SearchPlugin;
import com.idega.core.search.business.SearchPluginManager;
import com.idega.core.search.business.SearchQuery;
import com.idega.core.search.business.SearchResult;
import com.idega.core.search.data.SimpleSearchQuery;
import com.idega.core.search.presentation.Searcher;
import com.idega.data.StringInputStream;
import com.idega.presentation.IWContext;

/**
 * application scope
 * 
 * @author <a href="mailto:civilis@idega.com">Vytautas Čivilis</a>
 * @version 1.0
 * 
 */
public class ElightSearchResults implements Serializable {
	
	private static final long serialVersionUID = -6155590432961913762L;
	
	private String searchParameterName = Searcher.DEFAULT_SEARCH_PARAMETER_NAME;
	
	private DocumentBuilderFactory factory;
	private static final String contents_xml_part1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><content>";
	private static final String contents_xml_part2 = "</content>";
	
	
	public Collection<List<ElightSearchResult>> search(String query, List<String> plugins_used) {
		
		IWContext iwc = IWContext.getIWContext(FacesContext.getCurrentInstance());
		
		Collection<SearchPlugin> plugins = SearchPluginManager.getInstance().getAllSearchPluginsInitialized(iwc.getIWMainApplication());
		
		if (plugins == null || plugins.isEmpty()) {
			
//			TODO: return than nothing found
			return getMessageToTheUser("None plugins found");
		}
		
//		doing just simple search right? at least for now
		Map query_map = new HashMap();
		
//		if(!query.startsWith("*") && !query.startsWith("?"))
//			query = "*"+query;
		
		if(!query.endsWith("*") && !query.endsWith("?"))
			query = query+"*";
			
		query_map.put(getSimpleSearchParameterName(), query);
		SearchQuery search_query = new SimpleSearchQuery(query_map);
		
		Map<String, List<ElightSearchResult>> typed_search_results = new HashMap<String, List<ElightSearchResult>>();
		
		DocumentBuilder doc_builder = getDocumentBuilder();
		
		if(doc_builder == null) {
//			TODO: log
			return getMessageToTheUser("Sorry, internal error occured. Please, report to administrators.");
		}
			
		for (SearchPlugin plugin : plugins) {
			
			if(plugins_used != null && !plugins_used.isEmpty() && !plugins_used.contains(plugin.getSearchIdentifier()))
				continue;
			
			try {
				
				plugin = configureSearchPlugin(plugin);
				
				//doing just simple search right? at least for now
				if(!plugin.getSupportsSimpleSearch())
					continue;

				Search search = plugin.createSearch(search_query);
				
				Collection<SearchResult> results = search.getSearchResults();
				
				if(results == null || results.isEmpty()) {
//					TODO: maybe add plugin type still and say no results or smth
					continue;
				}
				
				String search_name = plugin.getSearchName();
				
				for (SearchResult result : results) {
					
					if(plugin.getSearchIdentifier().equals("ContentSearch")) {
						
//						TODO: this is of course temporary, modify content search plugin so it looks at user rights 
						
						if(result.getSearchResultURI() != null && 
							(
							!result.getSearchResultURI().startsWith("/content/files/public/") && 
							!result.getSearchResultURI().startsWith("/content/files/users/") &&
							!result.getSearchResultURI().startsWith("/content/files/groups/")
							) 
						)
							continue;
					}
					
					ElightSearchResult elight_result = new ElightSearchResult();
					
					elight_result.setTitle(result.getSearchResultName());
					elight_result.setUrl(result.getSearchResultURI());
					elight_result.setIconUri(plugin.getResultImgByResultURI(result.getSearchResultURI()));
					
					try {
						
						elight_result.setContents(doc_builder.parse(new StringInputStream(
								new StringBuilder(contents_xml_part1)
								.append(result.getSearchResultAbstract())
								.append(contents_xml_part2)
								.toString()
						)));
						
					} catch (Exception e) {
						// TODO: log
						e.printStackTrace();
						continue;
					}
					
					elight_result.setExtraInfo(result.getSearchResultExtraInformation());
					elight_result.setTypeLabel(search_name);
					
					putResultOnType(typed_search_results, plugin.getSearchIdentifier(), elight_result);
					
	/*				Map extraParameters = result.getSearchResultAttributes();
					// todo group by type
					String type = result.getSearchResultType();
	*/
//					Collection rowElements = plugin.getExtraRowElements(result, iwrb);
				}
				
			} catch (Exception e) {
				// TODO: log
				e.printStackTrace();
				continue;
			}
		}
		
		if(typed_search_results.values().isEmpty())
			return getMessageToTheUser("No results found");
		
		return typed_search_results.values();
	}
	
	private void putResultOnType(Map<String, List<ElightSearchResult>> typed_search_results, String type, ElightSearchResult elight_result) {
		
		List<ElightSearchResult> res_list = typed_search_results.get(type);
		
		if(res_list == null) {
			res_list = new ArrayList<ElightSearchResult>();
			typed_search_results.put(type, res_list);
		}
		
		res_list.add(elight_result);
	}
	
	private synchronized DocumentBuilder getDocumentBuilder() {
		
		try {
			if(factory == null) {
				
				factory = DocumentBuilderFactory.newInstance();
				factory.setNamespaceAware(false);
				factory.setValidating(false);
			}
			return factory.newDocumentBuilder();
			
		} catch (Exception e) {
			// TODO: log
			e.printStackTrace();
			return null;
		}
	}
	
	private List<List<ElightSearchResult>> getMessageToTheUser(String message) {
		
		List<List<ElightSearchResult>> res_map = new ArrayList<List<ElightSearchResult>>();
		List<ElightSearchResult> results_error = new ArrayList<ElightSearchResult>();
		ElightSearchResult res_error = new ElightSearchResult();
		res_error.setMessage(message);
		results_error.add(res_error);
		res_map.add(results_error);
		return res_map;
	}
	
	/**
	 * Allows subclasses to cast the search plugin to its true class and manipulate it.
	 * Remember this is a global plugin so clone it if you don't want to mess with other searches.
	 * @param searchPlugin
	 */
	protected SearchPlugin configureSearchPlugin(SearchPlugin searchPlugin) {
		return searchPlugin;
	}
	
	public String getSimpleSearchParameterName() {
		return searchParameterName;
	}
}