---
title: vben5接口文档
description: es, js
hide:
  - navigation
---

## 用户

- 用户登录：api/auth/login

``` json
{
  "code": 0,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6MCwicGFzc3dvcmQiOiIxMjM0NTYiLCJyZWFsTmFtZSI6IlZiZW4iLCJyb2xlcyI6WyJzdXBlciJdLCJ1c2VybmFtZSI6InZiZW4iLCJpYXQiOjE3Mzc0NzA0NjAsImV4cCI6MTczODA3NTI2MH0.RHWXHgHD4_8135f0nlCgASA5dXJNHyGvT1X0cFLmbp0"
  },
  "error": null,
  "message": "ok"
}
```

- 用户信息：api/user/info

``` json
{
  "code": 0,
  "data": {
    "id": 0,
    "desc":"",
    "avatar":"",
    "userId":"",
    "username":"",
    "homePath":"",
    "token":"",
    "realName": "Vben",
    "roles": [
      "super"
    ],
    "username": "vben"
  },
  "error": null,
  "message": "ok"
}
```

- token刷新：api/auth/refresh

``` json
{
  "code": 0,
  "data": {
    "data": "newToken",
    "status": 0
  },
  "error": null,
  "message": "ok"
}
```

- 授权码：api/auth/codes

``` json
{
  "code": 0,
  "data": [
    "AC_100100",
    "AC_100110",
    "AC_100120",
    "AC_100010"
  ],
  "error": null,
  "message": "ok"
}
```

## 菜单

- 获取菜单：api/menu/all

``` json
{
  "code": 0,
  "data": [
    {
        // 这里固定写死 BasicLayout，不可更改
        "component": "BasicLayout",
        "meta": {
        "order": -1,
        "title": "page.dashboard.title",
        },
        "name": "Dashboard",
        "path": "/",
        "redirect": "/analytics",
        "children": [
            {
                "name": "Analytics",
                "path": "/analytics",
                // 这里为页面的路径，需要去掉 views/ 和 .vue
                "component": "/dashboard/analytics/index",
                "meta": {
                "affixTab": true,
                "title": "page.dashboard.analytics",
                },
            },
            {
                "name": "Workspace",
                "path": "/workspace",
                "component": "/dashboard/workspace/index",
                "meta": {
                "title": "page.dashboard.workspace",
                },
            },
        ]
    }
  ],
  "error": null,
  "message": "ok"
}
```
