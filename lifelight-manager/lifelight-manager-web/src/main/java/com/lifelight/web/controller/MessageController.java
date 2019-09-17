package com.lifelight.web.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.foxinmy.weixin4j.util.StringUtil;
import com.lifelight.api.entity.Message;
import com.lifelight.api.entity.OrderInfo;
import com.lifelight.api.vo.ServiceInfoVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.BackstageUserInfoService;
import com.lifelight.dubbo.service.MessageService;
import com.lifelight.dubbo.service.OrderInfoService;
import com.lifelight.dubbo.service.ServiceService;

@Controller
@RequestMapping("/message")
public class MessageController {

	private static final Logger logger = LoggerFactory.getLogger(DeviceDataController.class);

	@Reference
	private MessageService messageService;
	@Autowired
	private RedisTool redisTool;
	@RequestMapping("/create")
	@ResponseBody
	public Result createKeywords(HttpServletRequest request,@RequestBody Message message) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		message.setPlatformId(Integer.parseInt(weixinId));
		return messageService.createMessage(message);
	}

	@RequestMapping("/delete")
	@ResponseBody
	public Result deleteKeywords(@RequestParam("id") String id) {
		return messageService.deleteMessage(id);
	}

	@RequestMapping("/update")
	@ResponseBody
	public Result updateKeywords(@RequestBody Message message) {
		return messageService.updateMessage(message);
	}

	/**
	 * 发送医生消息
	 * @return
	 */
	@RequestMapping("/sendDoctorMessage")
	@ResponseBody
	public Result sendDoctorMessage(HttpServletRequest request,@RequestParam("doctorId") String doctorId , 
			@RequestParam("toUserId") String toUserId , @RequestParam("context") String context){
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return messageService.sendDoctorMessage(doctorId , toUserId , context,Integer.parseInt(weixinId));
	}

	@RequestMapping("/sendDoctorMessageList")
	@ResponseBody
	public Result sendDoctorMessageList(HttpServletRequest request,@RequestParam("doctorId") String doctorId , 
			@RequestParam("toUserIds") List<String> toUserIds , @RequestParam("context") String content ){
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return messageService.sendDoctorMessageList(doctorId,toUserIds,content,Integer.parseInt(weixinId));
	}

	/**
	 * 分页 样例 pageSize 分页大小 currentsPage 当前页数
	 * 
	 * selectMessageListPage
	 * 
	 * @param message
	 * @return
	 */
	@RequestMapping("/getPageMessages")
	@ResponseBody
	public Result getPageMessages(HttpServletRequest request,@RequestBody Message message) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		message.setPlatformId(Integer.parseInt(weixinId));
		return messageService.selectMessageListPage(message);
	}
	/**
	 * 查询当前医生接受到的用户消息
	 * 
	 * selectMessageCount
	 * 
	 * @param message
	 * @return
	 */
	@RequestMapping("/selectMessageCount")
	@ResponseBody
	public Result selectMessageCount(HttpServletRequest request,@RequestBody Message message) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		message.setPlatformId(Integer.parseInt(weixinId));
		return messageService.selectMessageCount(message);
	}

	@Reference
	private BackstageUserInfoService backstageUserInfoService;  //用户

	@Reference
	private OrderInfoService orderInfoService;   //订单

	@Reference
	private ServiceService serviceService;   //服务

	/**
	 * 代办事项处理
	 * @return
	 */
	@RequestMapping("/scheduleCountMessages")
	@ResponseBody
	public Result scheduleCountMessages(HttpServletRequest request){

		Result<Object> result = new Result<>(StatusCodes.OK, true);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		// 统计当天订单数量
		OrderInfo orderQuery = new OrderInfo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		orderQuery.setStartDate(sdf.format(new Date()));
		orderQuery.setEndDate(sdf.format(new Date()));
		orderQuery.setPlatformId(Integer.parseInt(weixinId));
		Result orderRE = orderInfoService.countTotalAmount(orderQuery);
		if(orderRE != null && orderRE.getData() != null){
			returnMap.put("orderCount", orderRE.getData());
		}else{
			returnMap.put("orderCount", 0);
		}

		// 查询待审批用户数量
		Map<String, Object> userQuery = new HashMap<String, Object>();
		userQuery.put("userStatus", 2);  //待审批
		userQuery.put("inUse", 1);       //可用
		//分页参数（必填）
		userQuery.put("pageSize", 2);
		userQuery.put("pageNumber", 1);
		userQuery.put("beginSize", 0);
		userQuery.put("weixinId", Integer.parseInt(weixinId));
		Result userRE = backstageUserInfoService.getAllBackstageUserInfo(userQuery);
		if(userRE.getData() != null ){
			returnMap.put("userCount", ((Map<String, Object>) userRE.getData()).get("infoCount"));
		}else{
			returnMap.put("userCount", 0);
		}

		// 查询待审批服务数量
		ServiceInfoVO serviceVO = new ServiceInfoVO();
		//待审核
		List<Integer> listStatus = new ArrayList<Integer>();
		listStatus.add(1);
		serviceVO.setStatusList(listStatus);  
		serviceVO.setInUse(1);         //可用
		serviceVO.setCurrentPage(-1);  //无分页
		serviceVO.setPlatformId(Integer.parseInt(weixinId));
		PaginatedResult<ServiceInfoVO> serviceER = (PaginatedResult<ServiceInfoVO>) serviceService.getServiceInfoListPage(serviceVO);
		if(serviceER != null){
			returnMap.put("serviceCount", serviceER.getTotalItemsCount());
		}else{
			returnMap.put("serviceCount", 0);
		}

		result.setData(returnMap);

		return result;
	}

	/**
	 * 设置所有状态为已读
	 */
	@RequestMapping("/readOverMessage")
	@ResponseBody
	public Result readOverMessage(HttpServletRequest request,@RequestParam(value="messageId", required = false) String messageId ,
			@RequestParam(value="userId", required = true) String userId ,
			@RequestParam(value="messageFromId", required = false) String messageFromId , 
			@RequestParam(value="type", required = true) Integer type ) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		Message message = new Message();
		message.setId(messageId);
		message.setMessageFrom(messageFromId);   // 消息来源
		message.setMessageTo(userId);            // 用户
		message.setType(type);                   // 消息类型 ： 2,站内信; 3,医生消息
		message.setPlatformId(Integer.parseInt(weixinId));
		return messageService.readOverMessage(message);
	}

	/**
	 * 查询未读消息数量统计
	 * 
	 * 
	 */
	@RequestMapping("/countUnReadMessageList")
	@ResponseBody
	public Result countUnReadMessageList(
			HttpServletRequest request,
			@RequestParam(value="showCount", required = true) Integer showCount ,
			@RequestParam(value="currentPage", required = true) Integer currentPage ,
			@RequestParam(value="userId", required = true) String userId ,
			@RequestParam(value="doctorId", required = false) String doctorId ) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		Message message = new Message();
		message.setShowCount(showCount);
		message.setCurrentPage(currentPage);
		message.setMessageFrom(doctorId);   // 消息来源
		message.setMessageTo(userId);       // 用户
		message.setType(3);                 // 消息类型 ： 2,站内信; 3,医生消息
		message.setPlatformId(Integer.parseInt(weixinId));
		return messageService.countUnReadMessageList(message);
	}

	/**
	 * 查询
	 */
	@RequestMapping("/countAllUnReadMessage")
	@ResponseBody
	public Result countAllUnReadMessage(HttpServletRequest request,
			@RequestParam(value="userId", required = true) String userId) {
		//查询当前域名微信配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		Message message = new Message();
		message.setMessageTo(userId);
		message.setPlatformId(Integer.parseInt(weixinId));
		return messageService.countAllUnReadMessage(message);
	}

}