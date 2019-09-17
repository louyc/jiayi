package com.lifelight.web.controller;

import java.util.List;

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
import com.lifelight.api.entity.Keywords;
import com.lifelight.api.vo.ServiceInfoVO;
import com.lifelight.api.vo.WeixinConfigureVO;
import com.lifelight.common.result.Result;
import com.lifelight.common.result.ResultCode;
import com.lifelight.common.result.StatusCodes;
import com.lifelight.common.tools.RedisTool;
import com.lifelight.dubbo.service.KeywordsService;
import com.lifelight.dubbo.service.WeixinConfigureService;

@Controller
@RequestMapping("/keywords")
public class KeywordsController {

	private static final Logger logger = LoggerFactory.getLogger(KeywordsController.class);

	@Reference
	private KeywordsService keywordsService;
	@Autowired
	private RedisTool redisTool;

	@SuppressWarnings("rawtypes")
	@RequestMapping("/createKeywords")
	@ResponseBody
	public Result createKeywords(HttpServletRequest request,@RequestParam("name") String name) {
		//20160627  查询微信公众号信息
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return keywordsService.createKeywords(name,Integer.parseInt(weixinId));
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/deleteKeywords")
	@ResponseBody
	public Result deleteKeywords(@RequestParam("id") Integer id) {
		return keywordsService.deleteKeywords(id);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/updateKeywords")
	@ResponseBody
	public Result updateKeywords(HttpServletRequest request,@RequestBody Keywords keywords) {
		//20160627  查询微信公众号信息
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		keywords.setPlatformId(Integer.parseInt(weixinId));
		return keywordsService.updateKeywords(keywords);
	}

	/**
	 * 分页 样例
	 * pageSize 分页大小  currentsPage 当前页数
	 * 
	 * selectKeywordsListPage  （分页方法必须以 *****ListPage  为准）
	 * 实体类需继承 PageInfo
	 * 
	 * @param name
	 * @param pageSize
	 * @param currentsPage
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllKeywords")
	@ResponseBody
	public Result getAllKeywords(HttpServletRequest request,
			@RequestParam("name") String name, @RequestParam(value = "pageSize") String pageSize,
			@RequestParam("currentsPage") String currentsPage) {

		int currentPage = Integer.parseInt(currentsPage);

		if (currentPage <= 0){
			currentPage = 1;
		}

		Keywords keywords = new Keywords();

		keywords.setShowCount(Integer.parseInt(pageSize));
		keywords.setCurrentPage(currentPage);

		keywords.setName(name);
		//20160627  查询微信公众号信息
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		keywords.setPlatformId(Integer.parseInt(weixinId));
		return keywordsService.selectKeywordsListPage(keywords);
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping("/getAllKeywordsForFlow")
	@ResponseBody
	public Result getAllKeywordsForFlow(HttpServletRequest request) {
		//20160627  查询微信公众号信息
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return keywordsService.getAllKeywordsForFlow(Integer.parseInt(weixinId));
	}
	/**
	 * 标签查询 根据类型
	 * @param keywords
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/getKeywordsByType")
	@ResponseBody
	public Result getKeywordsByType(HttpServletRequest request,@RequestBody Keywords keywords) {
		//20160627  查询微信公众号信息
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		keywords.setPlatformId(Integer.parseInt(weixinId));
		return keywordsService.getKeywordsByType(keywords);
	}

	/**
	 * getRecommends:根据标签获取推荐. <br/> 
	 * @param keywords
	 * @return 
	 * Result
	 * @author hua
	 */
	@RequestMapping("/recommend")
	@ResponseBody
	public Result getRecommends(HttpServletRequest request,@RequestParam("keywords") String keywords) {
		//20160627  查询微信公众号信息
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		return keywordsService.getRecommends(keywords,Integer.parseInt(weixinId));
	}


	/**
	 * 根据关键字查询所有商品服务
	 * @param currentPage
	 * @param showCount
	 * @param keywordsId
	 * @param keywordsName
	 * @param type
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/findServiceByKeywords")
	@ResponseBody
	public Result findServiceByKeywords(HttpServletRequest request,
			@RequestParam(value="currentPage", required = false) String currentPage,
			@RequestParam(value="showCount", required = false) String showCount,
			@RequestParam(value="keywordsId", required = false) String keywordsId,
			@RequestParam(value="keywordsName", required = false) String keywordsName) {

		logger.info("根据关键字查询，参数[ currentPage="+currentPage+" showCount="+showCount+" keywordsId="+keywordsId+" keywordsName="+keywordsName+"]");

		Result result = new Result<>(false);

		//查询服务
		ServiceInfoVO serviceVO = new ServiceInfoVO();
		if(!StringUtil.isEmpty(currentPage)){
			serviceVO.setCurrentPage(Integer.parseInt(currentPage));
		}
		if(!StringUtil.isEmpty(showCount)){
			serviceVO.setShowCount(Integer.parseInt(showCount));
		}
		if(!StringUtil.isEmpty(keywordsId)){
			serviceVO.setKeywordsIdList(keywordsId);
		}

		if(!StringUtil.isEmpty(keywordsName)){
			//判断关键字是否为多个的情况
			int i = keywordsName.indexOf(";");
			if(i >= 0){
				String[] names = keywordsName.split(";");
				List list = java.util.Arrays.asList(names);
				serviceVO.setKeywordsNameList(list);
			}else{
				serviceVO.setKeywordsName(keywordsName);
			}
		}
		//20160627  查询微信公众号信息
		logger.info("域名：：：："+request.getServerName());
		String domainName = request.getServerName();
		String weixinId = redisTool.get(domainName);
		if(StringUtil.isEmpty(weixinId)) {
			logger.info("获取微信配置信息错误****************");
			return new Result(StatusCodes.OK, false, new ResultCode("FAILD", "获取微信配置表id失败！"));
		}
		serviceVO.setPlatformId(Integer.parseInt(weixinId));
		result = keywordsService.getRelationVoByKeywords(serviceVO);

		return result;
	}
}