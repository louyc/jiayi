webpackJsonp([43],{10:function(t,e){},11:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"vux-toast"},[i("div",{directives:[{name:"show",rawName:"v-show",value:t.isShowMask&&t.show,expression:"isShowMask && show"}],staticClass:"weui-mask_transparent"}),t._v(" "),i("transition",{attrs:{name:t.currentTransition}},[i("div",{directives:[{name:"show",rawName:"v-show",value:t.show,expression:"show"}],staticClass:"weui-toast",class:t.toastClass,style:{width:t.width}},[i("i",{directives:[{name:"show",rawName:"v-show",value:"text"!==t.type,expression:"type !== 'text'"}],staticClass:"weui-icon-success-no-circle weui-icon_toast"}),t._v(" "),t.text?i("p",{staticClass:"weui-toast__content",style:t.style,domProps:{innerHTML:t._s(t.text)}}):i("p",{staticClass:"weui-toast__content",style:t.style},[t._t("default")],2)])])],1)},staticRenderFns:[]}},12:function(t,e,i){var n=i(0)(i(14),i(17),null,null,null);t.exports=n.exports},1297:function(t,e){},1298:function(t,e){},13:function(t,e,i){function n(t){i(16)}var s=i(0)(i(15),i(18),n,null,null);t.exports=s.exports},14:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=["-moz-box-","-webkit-box-",""];e.default={name:"flexbox-item",props:{span:[Number,String],order:[Number,String]},beforeMount:function(){this.bodyWidth=document.documentElement.offsetWidth},methods:{buildWidth:function(t){return"number"==typeof t?t<1?t:t/12:"string"==typeof t?t.replace("px","")/this.bodyWidth:void 0}},computed:{style:function(){var t={},e="horizontal"===this.$parent.orient?"marginLeft":"marginTop";if(1*this.$parent.gutter!=0&&(t[e]=this.$parent.gutter+"px"),this.span)for(var i=0;i<n.length;i++)t[n[i]+"flex"]="0 0 "+100*this.buildWidth(this.span)+"%";return void 0!==this.order&&(t.order=this.order),t}},data:function(){return{bodyWidth:0}}}},1410:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{attrs:{id:"app"}},[i("div",{staticClass:"content"},[i("div",{staticClass:"bg"},[i("div",{staticClass:"header"},[t.info.imageUrl?i("img",{attrs:{src:t.info.imageUrl}}):i("img",{attrs:{src:"/m/static/header333x.png"}})]),t._v(" "),i("div",{staticClass:"certification"}),t._v(" "),i("div",{staticClass:"info"},[i("p",{staticClass:"detail"},[i("span",{staticClass:"detailName"},[t._v(t._s(t.info.name))]),t._v(" "),i("span",{staticClass:"detailLevel",staticStyle:{"font-size":"0.5rem"}},[t._v(t._s(t.info.title))])]),t._v(" "),i("p",{staticClass:"skills"},[t._v(t._s(t.info.company))])])]),t._v(" "),i("div",{staticClass:"introduce"},[t._m(0),t._v(" "),i("div",{staticClass:"main"},[i("div",{staticClass:"content"},[t._v("\n                    "+t._s(t.info.desc)+"\n                ")])])]),t._v(" "),i("div",{staticStyle:{width:"100%",height:"10px","background-color":"#F5F5F9"}}),t._v(" "),i("div",{staticClass:"serviceList"},[i("div",{staticClass:"header",staticStyle:{height:"1.3rem"}},[i("label",{staticClass:"headerTitle",staticStyle:{"font-size":"0.73rem"}},[t._v("ta的服务")]),t._v(" "),i("div",{staticClass:"application",on:{click:t.applyToService}},[t._v("申请服务")])]),t._v(" "),i("div",{staticClass:"main",staticStyle:{margin:"0 0.6rem"}},[i("flexbox",{attrs:{orient:"vertical"}},t._l(t.serviceArr,function(e,n){return i("flexbox-item",{key:n,staticStyle:{"margin-top":"0"}},[i("div",{staticClass:"itemBox",on:{click:function(i){t.goTo(e.id)}}},[i("div",{staticClass:"itemBoxTitle"},[i("div",{staticClass:"itemBoxTitleDiv"},[i("span",{staticClass:"itemBoxName"},[t._v(t._s(e.name))])]),t._v(" "),i("div",{staticStyle:{display:"inline-block",width:"23%","vertical-align":"top","text-align":"right","line-height":"1rem"}},[i("span",{staticClass:"itemContentPrice"},[t._v("￥"+t._s(e.price))])])]),t._v(" "),i("div",{staticClass:"itemBoxAuthor"},[t._v("\n                                来源："+t._s(e.createName)+"\n                            ")]),t._v(" "),i("div",{staticClass:"itemContent"},[i("div",[t._v("\n                                    简介："+t._s(e.description)+"\n                                ")])])])])}))],1)]),t._v(" "),i("toast",{attrs:{text:t.toastMsg,type:"text"},model:{value:t.showToast,callback:function(e){t.showToast=e},expression:"showToast"}})],1)])},staticRenderFns:[function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",{staticClass:"header"},[i("label",{staticClass:"headerTitle"},[t._v("专家介绍")])])}]}},1424:function(t,e,i){function n(t){i(1297),i(1298)}var s=i(0)(i(659),i(1410),n,"data-v-fad141b2",null);t.exports=s.exports},15:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0}),e.default={name:"flexbox",props:{gutter:{type:Number,default:8},orient:{type:String,default:"horizontal"},justify:String,align:String,wrap:String,direction:String},computed:{styles:function(){return{"justify-content":this.justify,"-webkit-justify-content":this.justify,"align-items":this.align,"-webkit-align-items":this.align,"flex-wrap":this.wrap,"-webkit-flex-wrap":this.wrap,"flex-direction":this.direction,"-webkit-flex-direction":this.direction}}}}},1522:function(t,e,i){i(2),t.exports=i(580)},159:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=i(31);e.default={name:"x-button",props:{type:{default:"default"},disabled:Boolean,mini:Boolean,plain:Boolean,text:String,actionType:String,showLoading:Boolean,link:[String,Object],gradients:{type:Array,validator:function(t){return 2===t.length}}},methods:{onClick:function(){!this.disabled&&(0,n.go)(this.link,this.$router)}},computed:{noBorder:function(){return Array.isArray(this.gradients)},buttonStyle:function(){if(this.gradients)return{background:"linear-gradient(90deg, "+this.gradients[0]+", "+this.gradients[1]+")",color:"#FFFFFF"}},classes:function(){return[{"weui-btn_disabled":!this.plain&&this.disabled,"weui-btn_plain-disabled":this.plain&&this.disabled,"weui-btn_mini":this.mini,"vux-x-button-no-border":this.noBorder},this.plain?"":"weui-btn_"+this.type,this.plain?"weui-btn_plain-"+this.type:"",this.showLoading?"weui-btn_loading":""]}}}},16:function(t,e){},169:function(t,e){},17:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement;return(t._self._c||e)("div",{staticClass:"vux-flexbox-item",style:t.style},[t._t("default")],2)},staticRenderFns:[]}},175:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("button",{staticClass:"weui-btn",class:t.classes,style:t.buttonStyle,attrs:{disabled:t.disabled,type:t.actionType},on:{click:t.onClick}},[t.showLoading?i("i",{staticClass:"weui-loading"}):t._e(),t._v(" "),t._t("default",[t._v(t._s(t.text))])],2)},staticRenderFns:[]}},18:function(t,e){t.exports={render:function(){var t=this,e=t.$createElement;return(t._self._c||e)("div",{staticClass:"vux-flexbox",class:{"vux-flex-col":"vertical"===t.orient,"vux-flex-row":"horizontal"===t.orient},style:t.styles},[t._t("default")],2)},staticRenderFns:[]}},184:function(t,e,i){function n(t){i(169)}var s=i(0)(i(159),i(175),n,null,null);t.exports=s.exports},4:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(t){return typeof t}:function(t){return t&&"function"==typeof Symbol&&t.constructor===Symbol&&t!==Symbol.prototype?"symbol":typeof t};e.default={setCookie:function(t,e,i){i||(i=30);var n=new Date;n.setMinutes(n.getMinutes()+i),document.cookie=t+"="+escape(e)+";path=/;expires ="+n.toGMTString()},getCookie:function(t){var e=document.cookie.split(";"),i={};if("token"==t)for(var n=0;n<e.length;n++){var s=e[n].indexOf("="),o=[e[n].substr(0,s),e[n].substring(s+2,e[n].length-1)];o[0]&&o[1]&&(i[o[0].replace(/^\s+/,"")]=o[1].replace(/^\s+/,""))}else for(var n=0;n<e.length;n++){var o=e[n].split("=");o[0]&&o[1]&&(i[o[0].replace(/^\s+/,"")]=o[1].replace(/^\s+/,""))}return t?unescape(i[t])||"":i},removeCookie:function(t){this.setCookie(t,1,-1)},simpleDateFormat:function(t,e){var i=new Date(t),n=function(t){return(t<10?"0":"")+t};return e.replace(/yyyy|MM|dd|HH|mm|ss/g,function(t){switch(t){case"yyyy":return n(i.getFullYear());case"MM":return n(i.getMonth()+1);case"mm":return n(i.getMinutes());case"dd":return n(i.getDate());case"HH":return n(i.getHours());case"ss":return n(i.getSeconds())}})},audioSet:function(){var t=new Date;return(t.getHours()>9?t.getHours():"0"+t.getHours())+":"+(t.getMinutes()>9?t.getMinutes():"0"+t.getMinutes())+":"+(t.getSeconds()>9?t.getSeconds():"0"+t.getSeconds())},getObjLength:function(t){var e=0;for(var i in t)e++;return e},getRequestParam:function(t){for(var e=location.search.replace(/^\?/,"").split("&"),i={},n=0;n<e.length;n++){var s=e[n].split("=");i[s[0]]=unescape(decodeURI(s[1]))}return t?i[t]||"":i},delURLQuery:function(t){var e=window.location,i=e.origin+e.pathname+"?",n=e.search.substr(1);if(n.indexOf(t)>-1){for(var s={},o=n.split("&"),r=0;r<o.length;r++)o[r]=o[r].split("="),s[o[r][0]]=o[r][1];delete s[t];return i+JSON.stringify(s).replace(/[\"\{\}]/g,"").replace(/\:/g,"=").replace(/\,/g,"&")}},next:function(t){var e=(new Date).getTime();t+=t.indexOf("?")>=0?"&_="+e:"?_="+e,window.location.href=t},mul:function(t,e){var i=0,n=0;return t=""+t,e=""+e,~t.indexOf(".")&&(i=t.split(".")[1].length),~e.indexOf(".")&&(n=e.split(".")[1].length),t=1*t.replace(".",""),e=1*e.replace(".",""),t*e/Math.pow(10,i+n)},wxConfig:function(t,e,i){t.post("/api/WeiXin/jssdkconfig.action",{url:e.url}).then(function(t){t=t.data;var n={debug:!1,appId:t.appId,timestamp:t.timeStamp,nonceStr:t.nonceStr,signature:t.signature,jsApiList:e.jsList};t?(wx.config(n),i(t)):console.error("调用返回信息错误",t)},function(t){console.error("调用返回信息错误==",t)})},isObj:function(t){return t&&"object"==(void 0===t?"undefined":n(t))&&"[object object]"==Object.prototype.toString.call(t).toLowerCase()},isArray:function(t){return t&&"object"==(void 0===t?"undefined":n(t))&&t.constructor==Array},deepCopy:function(t){if(!t||"object"!=(void 0===t?"undefined":n(t)))return t;var e=Array.isArray(t)?[]:{},i=Object.keys(t);s.push(t);var o=!0,r=!1,a=void 0;try{for(var u,l=i[Symbol.iterator]();!(o=(u=l.next()).done);o=!0){var c=u.value,d=t[c];if(s.indexOf(d)>-1)throw Error("检测到属性循环引用");e[c]=this.deepCopy(d)}}catch(t){r=!0,a=t}finally{try{!o&&l.return&&l.return()}finally{if(r)throw a}}return s.pop(),e},getPercent:function(t,e){return t=parseFloat(t),e=parseFloat(e),isNaN(t)||isNaN(e)?"-":e<=0?"0%":Math.round(t/e*1e4)/100+"%"},getArrIndex:function(t,e){for(var i=0;i<e.length;i++)if(e[i]==t)return i}};var s=[]},5:function(t,e,i){function n(t){i(10)}var s=i(0)(i(9),i(11),n,null,null);t.exports=s.exports},580:function(t,e,i){"use strict";function n(t){return t&&t.__esModule?t:{default:t}}var s=i(21),o=n(s),r=i(1424),a=n(r),u=i(6),l=n(u);o.default.prototype.$http=l.default,o.default.config.productionTip=!1,i(7).attach(document.body),new o.default({el:"#app",template:"<doctorIndex/>",components:{doctorIndex:a.default}})},6:function(t,e,i){"use strict";function n(t){return t&&t.__esModule?t:{default:t}}function s(t){return!t||200!==t.status&&304!==t.status&&400!==t.status?{status:-404,msg:"网络异常"}:t}function o(t){return console.log(t),-404===t.status&&alert(t.msg),t}Object.defineProperty(e,"__esModule",{value:!0});var r=i(22),a=n(r),u=i(23),l=n(u);i(2),a.default.interceptors.request.use(function(t){return t},function(t){return Promise.reject(t)}),a.default.interceptors.response.use(function(t){return t},function(t){return Promise.resolve(t.response)}),e.default={post:function(t,e){return(0,a.default)({method:"POST",url:t,data:l.default.stringify(e),timeout:1e4,withCredentials:!0,headers:{"X-Requested-With":"XMLHttpRequest","Content-Type":"application/x-www-form-urlencoded; charset=UTF-8"}}).then(function(t){return s(t)}).then(function(t){return o(t)})},get:function(t,e){return(0,a.default)({method:"GET",url:t,params:e,timeout:1e4,withCredentials:!0,headers:{"X-Requested-With":"XMLHttpRequest"}}).then(function(t){return s(t)}).then(function(t){return o(t)})},axios:function(t){return(0,a.default)(t).then(function(t){return s(t)}).then(function(t){return o(t)})},axiosPost:function(t,e,i){return a.default.post(t,e,i).then(function(t){return s(t)}).then(function(t){return o(t)})}}},659:function(t,e,i){"use strict";function n(t){return t&&t.__esModule?t:{default:t}}Object.defineProperty(e,"__esModule",{value:!0});var s=i(13),o=n(s),r=i(12),a=n(r),u=i(184),l=n(u),c=i(5),d=n(c),f=i(4),p=n(f);e.default={name:"doctorIndex",components:{Flexbox:o.default,FlexboxItem:a.default,XButton:l.default,Toast:d.default},data:function(){return{id:"",toastMsg:"",showToast:!1,info:{},serviceArr:[]}},mounted:function(){this.loading(),this.loadService(),this.wxConfig()},methods:{loading:function(){var t=this,e=p.default.getRequestParam("md");if(!e)return!1;this.id=e,this.$http.post("/backstage/queryOne?managerId="+e,{}).then(function(e){var i=e.data;i.successful&&200==i.status?i.data&&(t.info=i.data):(t.showToast=!0,t.toastMsg="查询失败",console.log(i.resultCode))},function(e){console.log(e),t.showToast=!0,t.toastMsg="查询失败"})},loadService:function(){var t=this,e=p.default.getRequestParam("md");if(!e)return!1;this.id=e,this.$http.post("/service/query?id="+e+"&status=2&pageNumber=1&pageSize=1000&serviceAttribute=1",{}).then(function(e){var i=e.data;i.successful&&200==i.status?i.items&&(t.serviceArr=i.items):(t.showToast=!0,t.toastMsg="查询失败",console.log(i.resultCode))},function(e){console.log(e),t.showToast=!0,t.toastMsg="查询失败"})},goTo:function(t){p.default.next("/m/serviceIndex.html?serviceId="+t)},wxConfig:function(){var t=this,e={jsList:["onMenuShareTimeline","onMenuShareAppMessage"],url:location.href.split("#")[0]},i="",n=localStorage.getItem("md")||p.default.getCookie("user_id");i=n?"&sourceUserId="+n:"",p.default.wxConfig(this.$http,e,function(e){wx.ready(function(){var n={title:t.info.name,desc:t.info.description,link:e.link+"m/sharing.html?type=2&id="+t.id+i,imgUrl:e.link+"m/static/img/20170817165354.jpg"};console.log("shareData==",n),wx.onMenuShareAppMessage(n),wx.onMenuShareTimeline(n)})})},applyToService:function(){var t=this,e=localStorage.getItem("md")||p.default.getCookie("user_id"),i=p.default.getRequestParam("md");if(e)return this.$http.axios({method:"POST",url:"/api/contracte/queryRel",data:{managerId:e,doctorId:i}}).then(function(e){var i=e.data;return i.successful&&200==i.status?i.data&&i.data.length>0?(t.showToast=!0,t.toastMsg="您已申请过,请勿重复申请",!1):(console.log(111),void p.default.next("/m/applyService.html?doctorId="+t.id)):(t.showToast=!0,t.toastMsg=i.resultCode.message,console.error(i),!1)},function(t){return console.error(t),!1}),!1;console.log(333),p.default.next("/m/applyService.html?doctorId="+this.id)}}}},9:function(t,e,i){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=i(24),s=function(t){return t&&t.__esModule?t:{default:t}}(n);e.default={name:"toast",mixins:[s.default],props:{value:Boolean,time:{type:Number,default:2e3},type:{type:String,default:"success"},transition:String,width:{type:String,default:"7.6em"},isShowMask:{type:Boolean,default:!1},text:String,position:String},data:function(){return{show:!1}},created:function(){this.value&&(this.show=!0)},computed:{currentTransition:function(){return this.transition?this.transition:"top"===this.position?"vux-slide-from-top":"bottom"===this.position?"vux-slide-from-bottom":"vux-fade"},toastClass:function(){return{"weui-toast_forbidden":"warn"===this.type,"weui-toast_cancel":"cancel"===this.type,"weui-toast_success":"success"===this.type,"weui-toast_text":"text"===this.type,"vux-toast-top":"top"===this.position,"vux-toast-bottom":"bottom"===this.position,"vux-toast-middle":"middle"===this.position}},style:function(){if("text"===this.type&&"auto"===this.width)return{padding:"10px"}}},watch:{show:function(t){var e=this;t&&(this.$emit("input",!0),this.$emit("on-show"),this.fixSafariOverflowScrolling("auto"),clearTimeout(this.timeout),this.timeout=setTimeout(function(){e.show=!1,e.$emit("input",!1),e.$emit("on-hide"),e.fixSafariOverflowScrolling("touch")},this.time))},value:function(t){this.show=t}}}}},[1522]);
//# sourceMappingURL=doctorIndex.0aaf63097d02bf91349e.js.map