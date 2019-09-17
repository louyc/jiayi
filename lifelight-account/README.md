#账户系统


## 用户登录接口

###### URL
```
/manager/login/submit
```
###### API Type
```
POST
```
###### params
```
?userName=13240130405&password=123456
```
###### return
```
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "登录成功！"
    },
    "token": "eyJoZWFkIjp7InR5cGUiOiJKV1QiLCJhbGciOiJzZW1pb2UifSwibG9hZCI6eyJkYXRhIjoiNCIsImlzcyI6Ind3dy5zZW1pb2UuYWNjb3VudCIsImV4cCI6MTUwMTA1MjI5NDE5MSwiaWF0IjoxNTAxMDQ5Mjk0MTkxfX0=.48afcb9155313b4345c82e7ad2d01290",
    "data": "4",
    "successful": true
}

{
    "status": 200,
    "resultCode": {
        "code": "PWD_ERROR",
        "message": "密码错误！"
    },
    "token": null,
    "data": null,
    "successful": false
}

{
    "status": 200,
    "resultCode": {
        "code": "ACCOUNT_NULL",
        "message": "账号不存在！"
    },
    "token": null,
    "data": null,
    "successful": false
}

```

## 根据token查询用户接口

###### URL
```
/manager/current
```
###### API Type
```
POST
```
###### params
```
header 中添加 X-Auth-Token
```
###### return
```
{
    "status": 200,
    "resultCode": {
        "code": "SUCCESS",
        "message": "查询成功！"
    },
    "token": null,
    "data": {
        "id": 4,
        "mobile": "13240130405",
        "email": "123@456.com",
        "idCode": "12345600",
        "userName": "abc",
        "realName": "汪华",
        "address": "北京",
        "isConfirm": 1,
        "createTime": "2017-05-23 16:25:46",
        "updateTime": "2017-05-25 16:46:54"
    },
    "successful": true
}

{
    "status": 200,
    "resultCode": {
        "code": "ACCOUNT_NULL",
        "message": "账号不存在！"
    },
    "token": null,
    "data": null,
    "successful": false
}
```