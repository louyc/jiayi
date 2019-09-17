package com.lifelight.dubbo.service;

import com.lifelight.api.vo.UserShareServiceVO;
import com.lifelight.common.result.Result;

public interface ShareServiceService {

	Result addShareServices(UserShareServiceVO service);

	Result removeShareServices(UserShareServiceVO service);

	Result getShareServices(UserShareServiceVO shareService);
}