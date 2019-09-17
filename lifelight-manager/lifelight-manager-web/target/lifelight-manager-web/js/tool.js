(function(window, document) {
	window.utils = {
		/**
		 * 写入cookie expireTime 单位是分
		 */
		setCookie : function(name, value, expireTime) {
			if(!expireTime){
				expireTime = 30;
			}
			var time = new Date();
			time.setMinutes(time.getMinutes() + expireTime);
			document.cookie = name + '=' + escape(value) + ';path=/;expires ='
					+ time.toGMTString();
		},
		/**
		 * 获取cookie 不传name就是返回所有
		 * 
		 * @param name
		 * @returns
		 */
		getCookie : function(name) {
			var arr = document.cookie.split(';');
			var paramObj = {};
			if(name == 'token'){
				for (var key = 0; key < arr.length; key++) {
					var index = arr[key].indexOf('=');
					var param = [arr[key].substr(0,index),arr[key].substring(index+1,arr[key].length)];
					if (param[0] && param[1]) {
						paramObj[param[0].replace(/^\s+/, "")] = param[1].replace(
								/^\s+/, "");
					}
				}
			}else{
				for (var key = 0; key < arr.length; key++) {
					var param = arr[key].split('=');
					if (param[0] && param[1]) {
						paramObj[param[0].replace(/^\s+/, "")] = param[1].replace(
								/^\s+/, "");
					}
				}
			}
			if (name) {
				if(unescape(paramObj[name]) == "undefined"){
					if (self.frameElement && self.frameElement.tagName == "IFRAME"){
						parent.location.href="/index.html"
					}else{
						window.location.href="/index.html"
					}
				}
				return unescape(paramObj[name]) || '';
			}
			return paramObj;
		},
		/**
		 * 删除cookie
		 * 
		 * @param name
		 * @returns
		 */
		removeCookie : function(name) {
			this.setCookie(name, 1, -1);
		},
		/**
		 * 判断是否对象
		 * 
		 * @param obj
		 * @returns
		 */
		isEmptyObject : function(obj) {
			for ( var key in obj) {
				return false
			}
			return true;
		},
		/**
		 * 年-月-日 时:分:秒
		 */
		dateFormat : function(timer) {
			var month = timer.getMonth() + 1;
			month = month > 9 ? month : '0' + month;
			var day = timer.getDate();
			day = day > 9 ? day : '0' + day;
			var hourse = timer.getHours();
			hourse = hourse > 9 ? hourse : '0' + hourse;
			var minute = timer.getMinutes();
			minute = minute > 9 ? minute : '0' + minute;
			var second = timer.getSeconds();
			second = second > 9 ? second : '0' + second;
			return timer.getFullYear() + '-' + month + '-' + day + ' ' + hourse
					+ ':' + minute + ':' + second;
		},
		/**
		 * 获取时间年-月-日
		 */
		dateFormat2 : function(timer) {
			var month = timer.getMonth() + 1;
			month = month > 9 ? month : '0' + month;
			var day = timer.getDate();
			day = day > 9 ? day : '0' + day;
			return timer.getFullYear() + '-' + month + '-' + day;
		},
		
		/**
		 * 自定义时间 time 时间 format 想要的格式
		 */
		simpleDateFormat : function(time, format) {
			var t = new Date(time);
			var tf = function(i) {
				return (i < 10 ? '0' : '') + i
			};
			return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a) {
				switch (a) {
					case 'yyyy':
						return tf(t.getFullYear()); 
						break;
					case 'MM':
						return tf(t.getMonth() + 1);
						break;
					case 'mm':
						return tf(t.getMinutes());
						break;
					case 'dd':
						return tf(t.getDate());
						break;
					case 'HH':
						return tf(t.getHours());
						break;
					case 'ss':
						return tf(t.getSeconds());
						break;
				}
			})
		},

		/**
		 * 当前时间 時分秒
		 */
		audioSet : function() {
			var mydate = new Date();
			var h = mydate.getHours() > 9 ? mydate.getHours() : "0"
					+ mydate.getHours();
			var m = mydate.getMinutes() > 9 ? mydate.getMinutes() : "0"
					+ mydate.getMinutes();
			var s = mydate.getSeconds() > 9 ? mydate.getSeconds() : "0"
					+ mydate.getSeconds();
			return h + ':' + m + ':' + s;
		},

		/**
		 * 对比两个对象是否相等
		 */
		isObjectValueEqual : function(x, y) {
			if (x === y) {
				return true;
			}
			if (!(x instanceof Object) || !(y instanceof Object)) {
				return false;
			}
			if (x.constructor !== y.constructor) {
				return false;
			}
			for ( var p in x) {
				if (x.hasOwnProperty(p)) {
					if (!y.hasOwnProperty(p)) {
						return false;
					}
					if (x[p] === y[p]) {
						continue;
					}
					if (typeof (x[p]) !== "object") {
						return false;
					}
					if (!Object.equals(x[p], y[p])) {
						return false;
					}
				}
			}
			for (p in y) {
				if (y.hasOwnProperty(p) && !x.hasOwnProperty(p)) {
					return false;
				}
			}
			return true;
		},
		/**
		 * 判断是否对象
		 * 
		 * @param object
		 * @returns
		 */
		isObj : function(object) {
			return object
					&& typeof (object) == 'object'
					&& Object.prototype.toString.call(object).toLowerCase() == "[object object]";
		},
		/**
		 * 判断是否数组
		 * 
		 * @param object
		 * @returns
		 */
		isArray : function(object) {
			return object && typeof (object) == 'object'
					&& object.constructor == Array;
		},
		/**
		 * 获取对象长度
		 * 
		 * @param object
		 * @returns
		 */
		getObjLength : function(object) {
			var count = 0;
			for ( var i in object)
				count++;
			return count;
		},
		/**
		 * 判断两个对象是否相等
		 */
		compare : function(objA, objB) {
			if (!this.isObj(objA) || !this.isObj(objB))
				return false; // 判断类型是否正确
			if (this.getObjLength(objA) != this.getObjLength(objB))
				return false; // 判断长度是否一致
			return this.compareObj(objA, objB, true); // 默认为true
		},
		compareObj : function(objA, objB, flag) {
			for ( var key in objA) {
				if (!flag) // 跳出整个循环
					break;
				if (!objB.hasOwnProperty(key)) {
					flag = false;
					break;
				}
				if (!this.isArray(objA[key])) { // 子级不是数组时,比较属性值
					if (objB[key] != objA[key]) {
						flag = false;
						break;
					}
				} else {
					if (!this.isArray(objB[key])) {
						flag = false;
						break;
					}
					var oA = objA[key], oB = objB[key];
					if (oA.length != oB.length) {
						flag = false;
						break;
					}
					for ( var k in oA) {
						if (!flag) // 这里跳出循环是为了不让递归继续
							break;
						flag = this.compareObj(oA[k], oB[k], flag);
					}
				}
			}
			return flag;
		},
		/**
		 * 获取url中‘？’后的参数
		 * 
		 * @param key
		 * @returns
		 */
		getRequestParam : function(item) {
			var url = location.search.replace(/^\?/,'').split('&');
		    var paramsObj = {};
		    for(var key=0;key<url.length;key++){
		    	var param =url[key].split('=');
		        paramsObj[param[0]]=unescape(decodeURI(param[1]));
		    }
		    if(item){
		        return paramsObj[item] || '';
		    }
		    return paramsObj;
		},
		/**
		 * 删除url中的参数
		 * 
		 * @param name
		 * @returns 删除后的url
		 */
		delURLQuery : function(name) {
			var loca = window.location;
			var baseUrl = loca.origin + loca.pathname + "?";
			var query = loca.search.substr(1);
			if (query.indexOf(name) > -1) {
				var obj = {}
				var arr = query.split("&");
				for (var i = 0; i < arr.length; i++) {
					arr[i] = arr[i].split("=");
					obj[arr[i][0]] = arr[i][1];
				}
				delete obj[name];
				var url = baseUrl
						+ JSON.stringify(obj).replace(/[\"\{\}]/g, "").replace(
								/\:/g, "=").replace(/\,/g, "&");
				return url
			}
		},
		/**
		 * 根据身份证获取年纪
		 * 
		 * @param identityCard 身份证号
		 * @returns 年龄
		 */
		getAge : function (identityCard) {
		    var len = (identityCard + "").length;
		    if (len == 0) {
		        return 0;
		    } else {
		        if ((len != 15) && (len != 18))//身份证号码只能为15位或18位其它不合法
		        {
		            return 0;
		        }
		    }
		    var strBirthday = "";
		    if (len == 18)//处理18位的身份证号码从号码中得到生日和性别代码
		    {
		        strBirthday = identityCard.substr(6, 4) + "/" + identityCard.substr(10, 2) + "/" + identityCard.substr(12, 2);
		    }
		    if (len == 15) {
		        strBirthday = "19" + identityCard.substr(6, 2) + "/" + identityCard.substr(8, 2) + "/" + identityCard.substr(10, 2);
		    }
		    //时间字符串里，必须是“/”
		    var birthDate = new Date(strBirthday);
		    var nowDateTime = new Date();
		    var age = nowDateTime.getFullYear() - birthDate.getFullYear();
		    //再考虑月、天的因素;.getMonth()获取的是从0开始的，这里进行比较，不需要加1
		    if (nowDateTime.getMonth() < birthDate.getMonth() || (nowDateTime.getMonth() == birthDate.getMonth() && nowDateTime.getDate() < birthDate.getDate())) {
		        age--;
		    }
		    return age;
		},
		/*
		生成guid
		*/
		guid : function () {
		    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
		        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
		        return v.toString(16);
		    });
		},
		/*根据字典ID获取家医字典*/
		getJYDictionary : function(itemType){
			if(itemType == "16"){
				return this.getServicePack();
			}else{
				for(var i = 0; i < this.JYDictionary.length;i++){
					if(this.JYDictionary[i].itemType == itemType){
						return this.JYDictionary[i].item
					}
				}
			}
		},
		/*加载服务包*/
		getServicePack : function(){
			var _this =this;
			var servicePack = [];
			$.ajax({
	            url:'/package/getAllServicePackage?pageSize=1000&currentsPage=1&packageName=',
	            type:'GET',
	            async: false,
	            success : function(data){
	                if(data.status === 200 && data.successful){
	                    var list = data.items,html = '',num = 0;
	                    var servicePackArr = [];
	                    while(list && (list.length > num)){
	                        var item = list[num];
	                        var servicePackObj = {'itemId': item.id,'itemName': item.packageName}
	                        servicePackArr.push(servicePackObj)
	                        num++;
	                    }
	                    var obj = {
                    		'itemType': '16',
                    		'itemDesc': '服务包',
                    		'item':servicePackArr
                    	}
	                    _this.JYDictionary.push(obj)
	                    servicePack = servicePackArr
	                }else{
	                    layer.msg(data.resultCode.message);
	                }
	            },
	            error:function(error){
	                layer.closeAll();
	                console.error(error)
	                layer.msg('查询服务包失败!');
	            }
	        });
			return servicePack;
		},
		/*家医字典*/
		JYDictionary : 
			[
				{
					'itemType': '0',
					'itemDesc': '是否',
					'item':[{'itemId': '1','itemName': '是'},{'itemId': '2','itemName': '否'}]
				},
				{
					'itemType': '1',
					'itemDesc': '重点人群',
					'item':[{'itemId': '1','itemName': '老年人'},{'itemId': '2','itemName': '高血压'},{'itemId': '3','itemName': '糖尿病'},{'itemId': '4','itemName': '重性精神病'},{'itemId': '5','itemName': '孕产妇'},{'itemId': '6','itemName': '儿童'}]
				},
				{
					'itemType': '2',
					'itemDesc': '家庭关系',
					'item':[{'itemId': '1','itemName': '本人或户主'},{'itemId': '2','itemName': '配偶'},{'itemId': '3','itemName': '子'},{'itemId': '4','itemName': '女'},{'itemId': '5','itemName': '孙子、孙女或外孙子、外孙女'},{'itemId': '6','itemName': '父母'},{'itemId': '7','itemName': '祖父母或外祖父母'},{'itemId': '8','itemName': '兄、弟、姐、妹'},{'itemId': '9','itemName': '其他'}]
				},
				{
					'itemType': '3',
					'itemDesc': '居住情况',
					'item':[{'itemId': '1','itemName': '常驻（户籍）'},{'itemId': '2','itemName': '常驻（非户籍）'},{'itemId': '3','itemName': '住外户籍（户籍）'},{'itemId': '4','itemName': '长外出（不在本地）'},{'itemId': '5','itemName': '不详'}]
				},
				{
					'itemType': '4',
					'itemDesc': '居住类型',
					'item':[{'itemId': '1','itemName': '流动'},{'itemId': '2','itemName': '常住'}]
				},
				{
					'itemType': '5',
					'itemDesc': '户口类型',
					'item':[{'itemId': '1','itemName': '乡村'},{'itemId': '2','itemName': '城镇'}]
				},
				{
					'itemType': '6',
					'itemDesc': '血型',
					'item':[{'itemId': '1','itemName': 'A'},{'itemId': '2','itemName': 'B'},{'itemId': '3','itemName': 'AB'},{'itemId': '4','itemName': 'O'},{'itemId': '5','itemName': '不详'},{'itemId': '6','itemName': '未检查'}]
				},
				{
					'itemType': '7',
					'itemDesc': 'RH',
					'item':[{'itemId': '1','itemName': '否'},{'itemId': '2','itemName': '是'},{'itemId': '2','itemName': '不详'}]
				},
				{
					'itemType': '8',
					'itemDesc': '文化程度',
					'item':[{'itemId': '1','itemName': '文盲或半文盲'},{'itemId': '2','itemName': '小学'},{'itemId': '3','itemName': '初中'},{'itemId': '4','itemName': '高中'},{'itemId': '5','itemName': '大学'},{'itemId': '6','itemName': '不详'}]
				},
				{
					'itemType': '9',
					'itemDesc': '职业',
					'item':[{'itemId': '1','itemName': '国家机关、党群组织、企业、事业单位负责人'},{'itemId': '2','itemName': '专业技术人员'},{'itemId': '3','itemName': '办事人员和有关人员'},{'itemId': '4','itemName': '商业、服务业人员'},{'itemId': '5','itemName': '农、林、牧、渔、水利业生产人员'},{'itemId': '6','itemName': '生产、运输设备操作员及有关人员'},{'itemId': '7','itemName': '军人'},{'itemId': '8','itemName': '不便分类的其他从业人员'},{'itemId': '9','itemName': '无职业'}]
				},
				{
					'itemType': '10',
					'itemDesc': '婚姻状态',
					'item':[{'itemId': '1','itemName': '未婚'},{'itemId': '2','itemName': '已婚'},{'itemId': '3','itemName': '离婚'},{'itemId': '4','itemName': '丧偶'},{'itemId': '5','itemName': '未说明婚姻状态'}]
				},
				{
					'itemType': '11',
					'itemDesc': '证件类型',
					'item':[{'itemId': '1','itemName': '身份证'},{'itemId': '2','itemName': '军官证'},{'itemId': '3','itemName': '其他证件'},{'itemId': '4','itemName': '护照'},{'itemId': '5','itemName': '户口本'},{'itemId': '6','itemName': '无'}]
				},
				{
					'itemType': '12',
					'itemDesc': '处置安排',
					'item':[{'itemId': '1','itemName': '持续管理'},{'itemId': '2','itemName': '居家治疗'},{'itemId': '3','itemName': '请管理团队会诊'},{'itemId': '4','itemName': '转诊门诊治疗'},{'itemId': '5','itemName': '转诊住院治疗'}]
				},
				{
					'itemType': '13',
					'itemDesc': '服务方式',
					'item':[{'itemId': '1','itemName': '主动服务'},{'itemId': '2','itemName': '上门服务'},{'itemId': '3','itemName': '预约服务'},{'itemId': '4','itemName': '电话随访'},{'itemId': '5','itemName': '其他'}]
				},
				{
					'itemType': '14',
					'itemDesc': '医保类型',
					'item':[{'itemId': '1','itemName': '新农合'},{'itemId': '2','itemName': '职工医保'},{'itemId': '3','itemName': '居民医保'},{'itemId': '4','itemName': '其他或无'}]
				},
				{
					'itemType': '15',
					'itemDesc': '签约人群',
					'item':[{'itemId': '1','itemName': '普通居民'},{'itemId': '2','itemName': '老年人'},{'itemId': '3','itemName': '高血压'},{'itemId': '4','itemName': '糖尿病'},{'itemId': '5','itemName': '孕产妇'},{'itemId': '6','itemName': '0-6岁儿童'},{'itemId': '7','itemName': '脑卒中'},{'itemId': '8','itemName': '计划生育特殊家庭对象'},{'itemId': '9','itemName': '精神病'},{'itemId': '9','itemName': '慢性呼吸道疾病'}]
				}
			]
		
	}
})(window, document);
