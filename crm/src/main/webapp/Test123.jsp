<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";%>

<html>
<head>
    <base href="<%=basePath%>">
</head>
<body>

</body>
</html>

//获取当前系统时间
createTime= DateTimeUtil.getSysTime();
//获取当前session中的用户
createBy=((User)request.getSession().getAttribute("user")).getName();

$(".time").datetimepicker({
    minView: "month",
    language:  'zh-CN',
    format: 'yyyy-mm-dd',
    autoclose: true,
    todayBtn: true,
    pickerPosition: "bottom-left"
});