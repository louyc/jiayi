package com.lifelight.dubbo.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.QrcodeRel;
import com.lifelight.api.vo.ApiUserInfoVO;
import com.lifelight.api.vo.OrderInfoVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.dubbo.dao.QrcodeRelMapper;
import com.lifelight.dubbo.service.QrcodeRelService;

@Service
public class QrcodeRelServiceImpl implements QrcodeRelService {

	@Autowired
	private QrcodeRelMapper qrcodeRelMapper;
	
	@Override
	public Result<QrcodeRel> addQrcodeRel(QrcodeRel qrcodeRel){
		Result<QrcodeRel> result = new Result<>(StatusCodes.OK, true);
		//验证openID是否存在
		if(StringUtil.isEmpty(qrcodeRel.getOpenId())){
			result.setResultCode(new ResultCode("fell", "用户openId 不存在"));
			return result;
		}
		//验证openid是否重复
		Integer count = qrcodeRelMapper.selectOpenIdCount(qrcodeRel);
		if(count > 0){
			result.setResultCode(new ResultCode("fell", "用户openId重复"));
			return result;
		}
		qrcodeRel.setCreateTime(new Date());
		int num = qrcodeRelMapper.insertSelective(qrcodeRel);
		
		if(num >= 1){
			result.setData(qrcodeRel);
			result.setResultCode(new ResultCode("success", "关系添加成功！"));
		}else{
			result.setResultCode(new ResultCode("fell", "关系添加失败！"));
		}
		
		return result;
	}

	@Override
	public Result<QrcodeRel> firstLoginUser(ApiUserInfoVO user) {
		Result<QrcodeRel> result = new Result<>(StatusCodes.OK, true);
		//验证用户数据
		if(user == null || StringUtil.isEmpty(user.getOpenId())){
			result.setResultCode(new ResultCode("fell", "用户不存在，或用户openId不存在！"));
			return result;
		}
		// 根据openid 查询关系表数据
		QrcodeRel record = new QrcodeRel();
		record.setOpenId(user.getOpenId());
		record.setPlatformId(user.getPlatformId());
		QrcodeRel updateRel = qrcodeRelMapper.selectQrcodeRelByOpenId(record);
		//验证用户数据
		if(updateRel == null){
			result.setResultCode(new ResultCode("fell", "未查询到推广数据！"));
			return result;
		}
		//关联用户数据
		updateRel.setOpenId(null);                 //清空openId
		updateRel.setUserId(user.getManagerId());  //用户id
		updateRel.setLoginTime(new Date());        //首次登陆时间
		updateRel.setType("1");     			   //修改状态  类型：0，初始  1，已注册 2，已购买
		updateRel.setUpdateTime(new Date());
		// 更新数据
		Integer num = qrcodeRelMapper.updateByPrimaryKey(updateRel);
		
		if(num >= 1){
			result.setData(updateRel);
			result.setResultCode(new ResultCode("success", "关系添加成功！"));
		}else{
			result.setResultCode(new ResultCode("fell", "关系添加失败！"));
		}
		
		return result;
	}

	@Override
	public Result<QrcodeRel> firstPayOrderUser(OrderInfoVO order) {
		Result<QrcodeRel> result = new Result<>(StatusCodes.OK, true);
		//验证订单数据
		if(order == null || StringUtil.isEmpty(order.getUserId())){
			result.setResultCode(new ResultCode("fell", "用户不存在，或用户openId不存在！"));
			return result;
		}
		//根据用户ID查询推广数据
		QrcodeRel record = new QrcodeRel();
		record.setUserId(order.getUserId());
		record.setPlatformId(order.getPlatformId());
		QrcodeRel updateRel = qrcodeRelMapper.selectQrcodeRelByUserId(record);
		if(updateRel != null && "1".equals(updateRel.getType())){
			//更新订单数据
			updateRel.setOrderId(order.getId());              //订单ID
			updateRel.setOrderCode(order.getOrderCode());     //订单编号
			updateRel.setOrderName(order.getRelationName());  //商品或服务名称
			updateRel.setOrderPrice(order.getPrice());        //商品单价
			updateRel.setOrderCount(order.getOrderCount());   //订单数量
 			updateRel.setPayTime(new Date());    //首次支付时间
			updateRel.setType("2");     		 //修改状态  类型：0，初始  1，已注册 2，已购买
			updateRel.setUpdateTime(new Date());
			// 更新数据
			Integer num = qrcodeRelMapper.updateByPrimaryKeySelective(updateRel);
			
			if(num >= 1){
				result.setData(updateRel);
				result.setResultCode(new ResultCode("success", "关系修改成功！"));
			}else{
				result.setResultCode(new ResultCode("fell", "关系修改失败！"));
			}
		}else{
			result.setResultCode(new ResultCode("fell", "关系修改失败！ 订单不存在或已经进行购买"));
		}
		
		return result;
	}
	
}
