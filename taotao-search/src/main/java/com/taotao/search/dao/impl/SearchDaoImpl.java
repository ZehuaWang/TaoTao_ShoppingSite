package com.taotao.search.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.taotao.search.dao.SearchDao;
import com.taotao.search.pojo.SearchItem;
import com.taotao.search.pojo.SearchResult;

/**
 * solr查询dao
 * @author Homeuser
 *
 */

@Repository
public class SearchDaoImpl implements SearchDao {

	@Autowired
	private SolrServer solrServer;
	
	@Override
	public SearchResult search(SolrQuery query) throws Exception {
		
		
		//执行查询
		QueryResponse response = solrServer.query(query);
		
		//取出查询结果列表
		SolrDocumentList solorDocumentList = response.getResults();
		
		List<SearchItem> itemList = new ArrayList<>();
		
		for(SolrDocument solrDocument : solorDocumentList) {
			
			//创建一个SearchItem对象
			SearchItem item = new SearchItem();
			item.setCategory_name((String) solrDocument.get("item_category_name"));
			item.setId((String) solrDocument.get("id"));
			item.setImage((String) solrDocument.get("item_image"));
			System.out.println((String) solrDocument.get("item_image"));
			item.setPrice((Long) solrDocument.get("item_price"));
			item.setSell_point((String) solrDocument.get("item_sell_point"));
			
			//取出高亮显示
			Map<String, Map<String,List<String>>> highlighting = response.getHighlighting();
			
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			
			String itemTitle = "";
			
			if(list != null && list.size() > 0) {
				itemTitle = list.get(0);
			}
			
			else {
				List titleList = (List) solrDocument.get("title");
				itemTitle = (String) titleList.get(0);
			}
			
			item.setTitle(itemTitle);
			
			//添加到列表
			itemList.add(item);
		}
		
		SearchResult result = new SearchResult();
		
		result.setItemList(itemList);
		
		//查询结果总数量
		result.setRecordCount(solorDocumentList.getNumFound());
		
		return result;
	}
}
