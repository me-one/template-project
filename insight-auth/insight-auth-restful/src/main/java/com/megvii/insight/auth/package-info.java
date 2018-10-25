package com.megvii.insight.auth;

// Restful root

// 任务包：auth.job，后台任务包，可单独拉出来，创建后台运行项目，若有任务调度系统可作为任务后台子系统存在

// 核心子包：auth.rest.controller

// 围绕核心子包，创建辅助兄弟包，如：
//          component        组件包
//          configuration    配置包
//          common           通用包
//          filter           过滤器包
//          interceptor      拦截器包
//          ...
// 辅助兄弟包创建原则：服务核心，若公共抽象功能则上提