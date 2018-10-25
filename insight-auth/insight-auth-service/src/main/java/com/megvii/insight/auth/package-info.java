package com.megvii.insight.auth;

// Service root

// 核心子包：auth.service  auth.service.impl

// 围绕核心子包，创建辅助兄弟包，如:
//          component        组件包
//          common           通用包
//          configuration    配置包
//          model            数据模型包
//          model.po         数据持久化对象包
//          model.repository 数据持久化接口包
//          vo               值对象包
//          util             工具包包
//          ...

// 辅助兄弟包创建原则：服务核心，若公共抽象功能则上提