package com.taotao.portal.service;

import org.springframework.stereotype.Service;

import com.taotao.portal.pojo.OrderInfo;

/**
 * 订单处理service
 * @author Homeuser
 *
 */

public interface OrderService {

	String createOrder(OrderInfo orderInfo);
	
}
