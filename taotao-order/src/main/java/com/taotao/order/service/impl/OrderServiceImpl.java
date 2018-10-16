package com.taotao.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbOrderItemMapper;
import com.taotao.mapper.TbOrderMapper;
import com.taotao.mapper.TbOrderShippingMapper;
import com.taotao.order.component.JedisClient;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbOrderItem;
import com.taotao.pojo.TbOrderShipping;

/**
 * 生成订单服务
 * 
 * @author Homeuser
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;

	@Autowired
	private TbOrderItemMapper orderItemMapper;

	@Autowired
	private TbOrderShippingMapper orderShippingMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS_ORDER_GEN_KEY}")
	private String REDIS_ORDER_GEN_KEY;

	@Value("${ORDER_ID_BEGIN}")
	private String ORDER_ID_BEGIN;

	@Value("${REDIS_ORDER_DETAIL_GEN_KEY}")
	private String REDIS_ORDER_DETAIL_GEN_KEY;

	@Override
	public TaotaoResult createOrder(OrderInfo orderInfo) {

		// 生成订单号 使用redis
		// 取出订单号
		String id = jedisClient.get(REDIS_ORDER_GEN_KEY);

		if (StringUtils.isEmpty(id)) {
			// 如果订单号生成的key不存在 设置初始值
			jedisClient.set(REDIS_ORDER_GEN_KEY, ORDER_ID_BEGIN);
		}

		Long orderId = jedisClient.incr(REDIS_ORDER_GEN_KEY);

		// 补全字段ORDER_ID_BEGIN
		orderInfo.setOrderId(orderId.toString());
		orderInfo.setStatus(1);

		Date date = new Date();

		orderInfo.setCreateTime(date);
		orderInfo.setUpdateTime(date);

		// 插入订单
		orderMapper.insert(orderInfo);

		// 插入订单明细

		// 补全字段
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();

		for (TbOrderItem orderItem : orderItems) {
			// 生成订单明细id 使用redis
			Long detailId = jedisClient.incr(REDIS_ORDER_DETAIL_GEN_KEY);
			orderItem.setId(detailId.toString());
			orderItem.setOrderId(orderId.toString());

			// 插入数据
			orderItemMapper.insert(orderItem);
		}

		//插入物流表
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		
		orderShipping.setOrderId(orderId.toString());
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		
		orderShippingMapper.insert(orderShipping);
		
		return TaotaoResult.ok(orderId);
	}
}