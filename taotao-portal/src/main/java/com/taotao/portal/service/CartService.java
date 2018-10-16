package com.taotao.portal.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.pojo.CartItem;

public interface CartService {

	TaotaoResult addCart(Long itemId,  Integer num, HttpServletRequest request, HttpServletResponse response);
	TaotaoResult updateCartItem(long itemId, Integer num, HttpServletRequest request, HttpServletResponse response);
	List<CartItem> getCartItems(HttpServletRequest request);
	TaotaoResult deleteCartItem(long itemId, HttpServletRequest request, HttpServletResponse response);

}
