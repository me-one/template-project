package com.megvii.insight.auth.sdk.vo;

// VO 可称之为值对象（Value Object）或视图（View Object）对象，也可替代（DTO）为数据传输对象

// pojo（Plain Ordinary Java Object）简单的Java对象，也即是通常所说的javabean

// 根据职责划分，一个pojo可有不同称谓

// 负责本地接口调用传值，视为值对象
// 负责RPC间网络传值，视为数据传输对象
// 负责给前端展现层传值，视为视图对象

// 简化以上职责划分，统一称之为值对象，VO可以完成以上所有职责


// 当前包，可存放PO的一个完整VO映射
// 当前包，若类较多，可按子模块划分多个一级子包，这时子包如同当前包存放子模块相关完整VO映射
// 当前子包，若类较多，可按入参出参再划分两个二级子包，分别存放 xxxInquiry.java，xxxResult.java