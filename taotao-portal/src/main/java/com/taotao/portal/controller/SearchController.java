package com.taotao.portal.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.taotao.portal.pojo.SearchResult;
import com.taotao.portal.service.SearchService;

/**
 * 实现商品查询
 * @author Homeuser
 *
 */

@Controller
public class SearchController {
	
	@Autowired
	private SearchService searchService;
	
	@RequestMapping("/search")
	public String search(@RequestParam("q")String keyword, @RequestParam(defaultValue="1")Integer page, @RequestParam(defaultValue="60")Integer rows,Model model) throws UnsupportedEncodingException
	{
		//get乱码处理
		byte[] ptext = keyword.getBytes("ISO-8859-1"); 
		keyword = new String(ptext,Charset.forName("UTF-8")); 
		SearchResult searchResult = searchService.search(keyword, page, rows);
		
		//参数传递给页面
		model.addAttribute("query",keyword);
		model.addAttribute("totalPages",searchResult.getPageCount());
		model.addAttribute("itemList",searchResult.getItemList());
		model.addAttribute("page",searchResult.getCurPage());
		
		//返回逻辑视图
		return "search";
	}
	
	
	
}
