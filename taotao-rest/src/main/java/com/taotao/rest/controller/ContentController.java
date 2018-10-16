package com.taotao.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.pojo.TbContent;
import com.taotao.rest.service.ContentService;

/**
 * 内容管理Controller
 * 
 * @author Homeuser
 *
 */
@Controller
@RequestMapping("/content")
public class ContentController {

	@Autowired
	private ContentService contentService;

	@RequestMapping("/{cid}")
	@ResponseBody
	public TaotaoResult getContentList(@PathVariable Long cid) {
		try {
			System.out.println("here");
			List<TbContent> list = contentService.getContentList(cid);
			System.out.println(list.toString());
			return TaotaoResult.ok(list);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

	@RequestMapping("/sync/content/{cid}")
	@ResponseBody
	public TaotaoResult syncContent(@PathVariable Long cid) {
		try {
			TaotaoResult result = contentService.syncContent(cid);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}
	}

}