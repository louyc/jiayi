package com.lifelight.web.controller;

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
import com.lifelight.api.entity.GoodsInfo;
import com.lifelight.api.vo.GoodsInfoVO;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.PaginatedResult;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.GoodsInfoService;
import com.lifelight.dubbo.service.WeixinConfigureService;

@Controller
@RequestMapping("/goodsInfo")
public class GoodsInfoController {

	private static final Logger logger = LoggerFactory.getLogger(GoodsInfoController.class);

	@Reference
	private GoodsInfoService goodsInfoService;
	@Autowired
	private RedisTool redisTool;
	@RequestMapping("/getGoodsInfoArray")
	@ResponseBody
	public Result getGoodsInfoArray(HttpServletRequest request,@RequestBody GoodsInfo goodsInfo){
		// log
		logger.info("GoodsInfoController.getGoodsInfoArray start, select");
		//20180627  获取微信对应配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		goodsInfo.setPlatformId(Integer.parseInt(weixinId));
		return goodsInfoService.getGoodsInfoArray(goodsInfo);
	}

	@RequestMapping("/addGoodsInfo")
	@ResponseBody
	public Result addGoodsInfo(HttpServletRequest request,@RequestBody GoodsInfo goodsInfo){
		// log
		logger.info("GoodsInfoController.addGoodsInfo start,goodsInfo = {}",goodsInfo.toString());
		//20180627  获取微信对应配置表id
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		goodsInfo.setPlatformId(Integer.parseInt(weixinId));
		return goodsInfoService.addGoodsInfo(goodsInfo);
	}

	@RequestMapping("/updateGoodsInfo")
	@ResponseBody
	public Result updateGoodsInfo(HttpServletRequest request,@RequestBody GoodsInfo goodsInfo){
		// log
		logger.info("GoodsInfoController.updateGoodsInfo start,goodsInfo = {}",goodsInfo.toString());
		return goodsInfoService.updateGoodsInfo(goodsInfo);
	}

	@RequestMapping("/deleteGoodsInfo")
	@ResponseBody
	public Result deleteGoodsInfo(@RequestParam("id") Integer id){
		// log
		logger.info("GoodsInfoController.deleteGoodsInfo start,id = {}",id);
		return goodsInfoService.deleteGoodsInfo(id);
	}

	@RequestMapping("/recoverDeviceType")
	@ResponseBody
	public Result recoverDeviceType(@RequestParam("id") Integer id){
		// log
		logger.info("GoodsInfoController.recoverDeviceType start,id = {}",id);
		return goodsInfoService.recoverDeviceType(id);
	}

	@RequestMapping("/getGoodsInfoById")
	@ResponseBody
	public Result getGoodsInfoById(@RequestParam("id") Integer id){
		// log
		logger.info("GoodsInfoController.getGoodsInfoById start,id = {}",id);
		return goodsInfoService.getGoodsInfoById(id);
	}

}
