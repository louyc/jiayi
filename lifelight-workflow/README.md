#流程编辑器


## 流程 添加

###### URL
```
/procedure/create
```
###### API Type
```
POST
```
###### params
```javascript
{
"managerId": "1", // 创建人id
"title": "4", // 流程标题
"status": 1, // 状态
"content": null, // 流程内容
"createTime": "2018-07-19 16:07:55", //创建时间
"updateTime": "2018-07-19 16:07:55" // 更新时间
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "添加成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```
## 流程 修改

###### URL
```
/procedure/update
```
###### API Type
```
POST
```
###### params
```javascript
{
"managerId": "1", // 创建人id
"title": "4", // 流程标题
"status": 1, // 状态
"content": null, // 流程内容
"createTime": "2018-07-19 16:07:55", //创建时间
"updateTime": "2018-07-19 16:07:55" // 更新时间
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "修改成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```


## 流程 删除

###### URL
```
/procedure/delete?id=eNQ4XrfO
```
###### API Type
```
POST
```
###### params
```
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "删除成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```

## 流程 分享

###### URL
```
/procedure/share?id=eNQ4XrfO&?users=abc,bcd,cde
```
###### API Type
```
POST
```
###### params
```
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "分享成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```


## 分页查询流程

###### URL
```
/procedure/getPageProcedures
```
###### API Type
```
POST
```
###### params
```javascript
{
"showCount": 10, // 分页大小
"currentPage": 1, // 当前页码
"id": "eNQ4XrfO", //消息id
"managerId": "1", // 创建人id
"title": "4", // 流程标题
"status": 1, // 状态
"content": null, // 流程内容
"createTime": "2018-07-19 16:07:55", //创建时间
"updateTime": "2018-07-19 16:07:55" // 更新时间
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "查询成功！"
    },
    "token": null,
    "data": null,
    "items": [
        {
            "showCount": 10,
            "totalPage": 0,
            "totalResult": 0,
            "currentPage": 1,
            "currentResult": 0,
            "sortField": null,
            "order": null,
            "id": "eNQ4XrfO",
            "managerId": "4",
            "title": "测试标题2",
            "status": 1,
            "createTime": "2018-07-19 16:07:55",
            "updateTime": "2018-07-19 16:07:55",
            "inUse": 1,
            "content": null
        },
        {
            "showCount": 10,
            "totalPage": 0,
            "totalResult": 0,
            "currentPage": 1,
            "currentResult": 0,
            "sortField": null,
            "order": null,
            "id": "1",
            "managerId": "4",
            "title": "测试标题1",
            "status": 1,
            "createTime": "2018-07-19 15:09:52",
            "updateTime": "2018-07-19 15:09:55",
            "inUse": 1,
            "content": "{\"class\":\"go.GraphLinksModel\",\"modelData\":{\"pid\":4415,\"mData\":{\"text\":\"\",\"category\":\"\",\"val\":\"\"},\"title\":\"测试所有类型\",\"uid\":83,\"object_id\":\"4\",\"varlist\":[\"身体特征(单位：a)\",\"体重(单位：kg)\"],\"phrVarlist\":[{\"key\":\"10\",\"name\":\"身体特征(单位：a)\",\"resultNumber\":1,\"suffix\":\"\"},{\"key\":\"12\",\"name\":\"体重(单位：kg)\",\"resultNumber\":1,\"suffix\":\"\"}]},\"nodeDataArray\":[{\"key\":1,\"text\":\"测试所有类型\",\"无标题_\":\"无标题_1531820196\",\"loc\":\"-74.5 56.99999999999994\",\"category\":\"Loading\"},{\"key\":-2,\"loc\":\"-51.5 444\",\"category\":\"Recycle\"},{\"text\":\"单选问题\",\"category\":\"qbox\",\"loc\":\"-71.5 137.2539925508721\",\"key\":-3},{\"text\":\"单选项1\",\"category\":\"opt\",\"loc\":\"167.5 64.25399255087211\",\"key\":-4,\"itemEmbedData\":[{\"text\":\"身体特征(单位：a)\",\"cond\":\"add\",\"val\":1,\"string\":1},{\"text\":\"体重(单位：kg)\",\"cond\":\"add\",\"val\":1,\"string\":1}]},{\"text\":\"单选项2\",\"category\":\"opt\",\"loc\":\"-71.5 252.8064827852471\",\"key\":-5},{\"text\":\"多选问题\",\"category\":\"mbox\",\"loc\":\"-71.5 345.2539925508721\",\"key\":-6,\"itemEmbedData\":[{\"text\":\"设置多选的执行条件\",\"cond\":\"sum\",\"val\":\"0\"}]},{\"text\":\"判断\",\"category\":\"ifbox\",\"loc\":\"139.5 143.2539925508721\",\"key\":-7,\"itemEmbedData\":[{\"text\":\"条件1\",\"category\":\"\",\"cond\":\"gte\",\"val\":\"1\",\"string\":\"是\"},{\"text\":\"缺省\",\"category\":\"\",\"cond\":\"lt\",\"val\":\"1\",\"string\":\"否\"}]},{\"text\":\"是\",\"category\":\"opt\",\"loc\":\"185.5 212.2539925508721\",\"key\":-8},{\"text\":\"否\",\"category\":\"opt\",\"loc\":\"172.5 272.2539925508721\",\"key\":-9},{\"text\":\"输入\",\"category\":\"usrinp\",\"loc\":\"336.5 212.2539925508721\",\"key\":-10,\"itemEmbedData\":[]},{\"text\":\"逻辑\",\"category\":\"logic\",\"loc\":\"494.5 204.2539925508721\",\"key\":-11,\"itemEmbedData\":[]},{\"text\":\"消息\",\"category\":\"msgset\",\"loc\":\"523.5 273.2539925508721\",\"key\":-12,\"itemEmbedData\":[{\"text\":\"消息设置1\",\"category\":\"\",\"cond\":\"\",\"string\":\"消息内容ABC\"}]},{\"text\":\"多选项a\",\"category\":\"opt\",\"loc\":\"111.5 338.2539925508721\",\"key\":-13,\"itemEmbedData\":[{\"text\":\"身体特征(单位：a)\",\"cond\":\"add\",\"val\":1,\"string\":1}]},{\"text\":\"多选项b\",\"category\":\"opt\",\"loc\":\"110.5 375.8064827852471\",\"key\":-14},{\"text\":\"消息推送\",\"category\":\"subscribe\",\"loc\":\"348.5 275.2539925508721\",\"key\":-15,\"itemEmbedData\":[{\"text\":\"推送的消息\"}]},{\"text\":\"日历\",\"category\":\"calsel\",\"loc\":\"293.5 338.2539925508721\",\"key\":-16,\"itemEmbedData\":[{\"text\":\"日历选择\",\"category\":\"\",\"cond\":\"\"}]},{\"text\":\"量表\",\"category\":\"exerst\",\"loc\":\"475.5 338.2539925508721\",\"key\":-17,\"itemEmbedData\":[{\"text\":\"需要执行的量表\",\"code\":\"\"}]},{\"text\":\"多选执行\",\"category\":\"mboxst\",\"loc\":\"292.5 375.8064827852471\",\"key\":-18,\"itemEmbedData\":[]},{\"text\":\"a\",\"category\":\"opt\",\"loc\":\"474.5 375.8064827852471\",\"key\":-19},{\"text\":\"b\",\"category\":\"opt\",\"loc\":\"474.5 405.8064827852471\",\"key\":-20},{\"text\":\"多选结束\",\"category\":\"mboxed\",\"loc\":\"632.5 384.8064827852471\",\"key\":-21,\"itemEmbedData\":[]}],\"linkDataArray\":[{\"from\":1,\"to\":-3},{\"from\":-3,\"to\":-4},{\"from\":-3,\"to\":-5},{\"from\":-5,\"to\":-6},{\"from\":-4,\"to\":-7},{\"from\":-7,\"to\":-8},{\"from\":-7,\"to\":-9},{\"from\":-8,\"to\":-10},{\"from\":-10,\"to\":-11},{\"from\":-6,\"to\":-13},{\"from\":-6,\"to\":-14},{\"from\":-9,\"to\":-15},{\"from\":-15,\"to\":-12},{\"from\":-13,\"to\":-16},{\"from\":-16,\"to\":-17},{\"from\":-14,\"to\":-18},{\"from\":-18,\"to\":-19},{\"from\":-18,\"to\":-20},{\"from\":-19,\"to\":-21},{\"from\":-20,\"to\":-21}]}"
        }
    ],
    "pageSize": 10,
    "pageNumber": 1,
    "pagesCount": 1,
    "totalItemsCount": 2,
    "successful": true
}
```

## 查询当前用户流程列表

###### URL
```
/procedure/getOne
```
###### API Type
```
POST
```
###### params
```
?id=eNQ4XrfO
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "查询成功！"
    },
    "token": null,
    "data": {
        "showCount": 10,
        "totalPage": 0,
        "totalResult": 0,
        "currentPage": 1,
        "currentResult": 0,
        "sortField": null,
        "order": null,
        "id": "1",
        "managerId": "4",
        "title": "测试标题1",
        "status": 1,
        "createTime": "2018-07-19 15:09:52",
        "updateTime": "2018-07-19 15:09:55",
        "inUse": 1,
        "content": "{\"class\":\"go.GraphLinksModel\",\"modelData\":{\"pid\":4415,\"mData\":{\"text\":\"\",\"category\":\"\",\"val\":\"\"},\"title\":\"测试所有类型\",\"uid\":83,\"object_id\":\"4\",\"varlist\":[\"身体特征(单位：a)\",\"体重(单位：kg)\"],\"phrVarlist\":[{\"key\":\"10\",\"name\":\"身体特征(单位：a)\",\"resultNumber\":1,\"suffix\":\"\"},{\"key\":\"12\",\"name\":\"体重(单位：kg)\",\"resultNumber\":1,\"suffix\":\"\"}]},\"nodeDataArray\":[{\"key\":1,\"text\":\"测试所有类型\",\"无标题_\":\"无标题_1531820196\",\"loc\":\"-74.5 56.99999999999994\",\"category\":\"Loading\"},{\"key\":-2,\"loc\":\"-51.5 444\",\"category\":\"Recycle\"},{\"text\":\"单选问题\",\"category\":\"qbox\",\"loc\":\"-71.5 137.2539925508721\",\"key\":-3},{\"text\":\"单选项1\",\"category\":\"opt\",\"loc\":\"167.5 64.25399255087211\",\"key\":-4,\"itemEmbedData\":[{\"text\":\"身体特征(单位：a)\",\"cond\":\"add\",\"val\":1,\"string\":1},{\"text\":\"体重(单位：kg)\",\"cond\":\"add\",\"val\":1,\"string\":1}]},{\"text\":\"单选项2\",\"category\":\"opt\",\"loc\":\"-71.5 252.8064827852471\",\"key\":-5},{\"text\":\"多选问题\",\"category\":\"mbox\",\"loc\":\"-71.5 345.2539925508721\",\"key\":-6,\"itemEmbedData\":[{\"text\":\"设置多选的执行条件\",\"cond\":\"sum\",\"val\":\"0\"}]},{\"text\":\"判断\",\"category\":\"ifbox\",\"loc\":\"139.5 143.2539925508721\",\"key\":-7,\"itemEmbedData\":[{\"text\":\"条件1\",\"category\":\"\",\"cond\":\"gte\",\"val\":\"1\",\"string\":\"是\"},{\"text\":\"缺省\",\"category\":\"\",\"cond\":\"lt\",\"val\":\"1\",\"string\":\"否\"}]},{\"text\":\"是\",\"category\":\"opt\",\"loc\":\"185.5 212.2539925508721\",\"key\":-8},{\"text\":\"否\",\"category\":\"opt\",\"loc\":\"172.5 272.2539925508721\",\"key\":-9},{\"text\":\"输入\",\"category\":\"usrinp\",\"loc\":\"336.5 212.2539925508721\",\"key\":-10,\"itemEmbedData\":[]},{\"text\":\"逻辑\",\"category\":\"logic\",\"loc\":\"494.5 204.2539925508721\",\"key\":-11,\"itemEmbedData\":[]},{\"text\":\"消息\",\"category\":\"msgset\",\"loc\":\"523.5 273.2539925508721\",\"key\":-12,\"itemEmbedData\":[{\"text\":\"消息设置1\",\"category\":\"\",\"cond\":\"\",\"string\":\"消息内容ABC\"}]},{\"text\":\"多选项a\",\"category\":\"opt\",\"loc\":\"111.5 338.2539925508721\",\"key\":-13,\"itemEmbedData\":[{\"text\":\"身体特征(单位：a)\",\"cond\":\"add\",\"val\":1,\"string\":1}]},{\"text\":\"多选项b\",\"category\":\"opt\",\"loc\":\"110.5 375.8064827852471\",\"key\":-14},{\"text\":\"消息推送\",\"category\":\"subscribe\",\"loc\":\"348.5 275.2539925508721\",\"key\":-15,\"itemEmbedData\":[{\"text\":\"推送的消息\"}]},{\"text\":\"日历\",\"category\":\"calsel\",\"loc\":\"293.5 338.2539925508721\",\"key\":-16,\"itemEmbedData\":[{\"text\":\"日历选择\",\"category\":\"\",\"cond\":\"\"}]},{\"text\":\"量表\",\"category\":\"exerst\",\"loc\":\"475.5 338.2539925508721\",\"key\":-17,\"itemEmbedData\":[{\"text\":\"需要执行的量表\",\"code\":\"\"}]},{\"text\":\"多选执行\",\"category\":\"mboxst\",\"loc\":\"292.5 375.8064827852471\",\"key\":-18,\"itemEmbedData\":[]},{\"text\":\"a\",\"category\":\"opt\",\"loc\":\"474.5 375.8064827852471\",\"key\":-19},{\"text\":\"b\",\"category\":\"opt\",\"loc\":\"474.5 405.8064827852471\",\"key\":-20},{\"text\":\"多选结束\",\"category\":\"mboxed\",\"loc\":\"632.5 384.8064827852471\",\"key\":-21,\"itemEmbedData\":[]}],\"linkDataArray\":[{\"from\":1,\"to\":-3},{\"from\":-3,\"to\":-4},{\"from\":-3,\"to\":-5},{\"from\":-5,\"to\":-6},{\"from\":-4,\"to\":-7},{\"from\":-7,\"to\":-8},{\"from\":-7,\"to\":-9},{\"from\":-8,\"to\":-10},{\"from\":-10,\"to\":-11},{\"from\":-6,\"to\":-13},{\"from\":-6,\"to\":-14},{\"from\":-9,\"to\":-15},{\"from\":-15,\"to\":-12},{\"from\":-13,\"to\":-16},{\"from\":-16,\"to\":-17},{\"from\":-14,\"to\":-18},{\"from\":-18,\"to\":-19},{\"from\":-18,\"to\":-20},{\"from\":-19,\"to\":-21},{\"from\":-20,\"to\":-21}]}"
    },
    "successful": true
}
```

## 查询流程会话的下一步

###### URL
```
/procedure/getNextStep
```
###### API Type
```
POST
```
###### params
```
?id=eNQ4XrfO&key=1&procedureId=mdwoq&data=111
用户填写data非必填；流程ID procedureId，节点key,会话ID id必填
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "查询成功！"
    },
    "token": null,
    "data": "[{\"loc\":\"369.5 1\",\"propertyType\":\"3\",\"stepData\":[{\"name\":\"空腹血糖\",\"childData\":{\"contant\":\"12\",\"expression\":\"12\"},\"id\":\"115\"}],\"text\":\"选项\",\"key\":-3}]",
    "successful": true
}
```

## 流程模块 添加

###### URL
```
/procedure/module/create
```
###### API Type
```
POST
```
###### params
```javascript
{
"managerId": "1", // 创建人id
"name": "4", // 流程标题
"content": null, // 流程内容
"createTime": "2018-07-19 16:07:55", //创建时间
"updateTime": "2018-07-19 16:07:55" // 更新时间
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "添加成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```
## 流程模块 修改

###### URL
```
/procedure/module/update
```
###### API Type
```
POST
```
###### params
```javascript
{
"managerId": "1", // 创建人id
"name": "4", // 流程标题
"content": null, // 流程内容
"createTime": "2018-07-19 16:07:55", //创建时间
"updateTime": "2018-07-19 16:07:55" // 更新时间
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "修改成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```


## 流程模块 删除

###### URL
```
/procedure/module/delete?id=eNQ4XrfO
```
###### API Type
```
POST
```
###### params
```
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "删除成功！"
    },
    "token": null,
    "data": null,
    "successful": true
}
```

## 流程模块 根据id查询

###### URL
```
/procedure/module/getOne?id=eNQ4XrfO
```
###### API Type
```
POST
```
###### params
```
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "查询成功！"
    },
    "token": null,
    "data": {
        "showCount": 10,
        "totalPage": 0,
        "totalResult": 0,
        "currentPage": 1,
        "currentResult": 0,
        "sortField": null,
        "order": null,
        "id": "1",
        "managerId": "4",
        "name": "血压诊断模块",
        "createTime": "2018-11-01 11:07:01",
        "updateTime": "2018-11-01 11:07:03",
        "inUse": 1,
        "content": "{}"
    },
    "successful": true
}
```

## 分页查询流程模块

###### URL
```
/procedure/module/getPageModules
```
###### API Type
```
POST
```
###### params
```javascript
{
"showCount": 10, // 分页大小
"currentPage": 1, // 当前页码
"id": "eNQ4XrfO", //消息id
"managerId": "1", // 创建人id
"name": "4", // 流程模块标题
"content": null, // 流程内容
"createTime": "2018-07-19 16:07:55", //创建时间
"updateTime": "2018-07-19 16:07:55" // 更新时间
}
```
###### return
```javascript
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "查询成功！"
    },
    "token": null,
    "data": null,
    "items": [
        {
            "showCount": 10,
            "totalPage": 0,
            "totalResult": 0,
            "currentPage": 1,
            "currentResult": 0,
            "sortField": null,
            "order": null,
            "id": "FRHELXVE",
            "managerId": "4",
            "name": "血氧诊断模块",
            "createTime": "2018-11-01 11:22:01",
            "updateTime": "2018-11-01 11:22:57",
            "inUse": null,
            "content": "{json12}"
        },
        {
            "showCount": 10,
            "totalPage": 0,
            "totalResult": 0,
            "currentPage": 1,
            "currentResult": 0,
            "sortField": null,
            "order": null,
            "id": "72AzGQqr",
            "managerId": "4",
            "name": "血压诊断模块",
            "createTime": "2018-11-01 11:20:49",
            "updateTime": "2018-11-01 11:20:49",
            "inUse": 1,
            "content": "{json}"
        }
    ],
    "pageSize": 10,
    "pageNumber": 1,
    "pagesCount": 1,
    "totalItemsCount": 2,
    "successful": true
}
```