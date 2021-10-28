<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
	<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		$(function (){
			if(window.top!=window){
				window.top.location=window.location;
			}

			$("#loginAct").focus();
			$("#bt_dl").click(function (){
				login();
			})
			$(window).keydown(function (event){
				if (event.keyCode === 13){
					login();
				}
			})
		})
		//自定义的普通方法要定义在$(function(){})的外边
		var login=function (){
			//首先验证用户名、密码不能为空
			var login_act=$("#loginAct")
			var login_pwd=$("#loginPwd")
			var loginAct=login_act.val();
			var loginPwd=login_pwd.val();
			if($.trim(loginAct)!=="" && $.trim(loginPwd)!==""){
				$.ajax({
					url:"settings/user/login.do",
					type:"post",
					data:{
						loginAct:loginAct,loginPwd:loginPwd
					},
					dataType:"json",
					success:function (result_data) {
						/*
							result_data对该参数进行分析,这是一个json数据交换格式

							result_data={
								"success":true/false,
								"msg":"错误信息提示"
							}
						*/
						if (result_data.success){//
							//登录成功	请求转发,进入主页面		workbench/index.jsp
							window.location.href="workbench/index.jsp"
						}else {
							//登录失败	ajax异步刷新提示登录失败信息
							$("#msg").html(result_data.msg);
						}
					}
				})
				login_act.val("")
				login_pwd.val("")
			}else {
				$("#msg").html("用户名密码不能为空")
			}
		}
	</script>
</head>
<body>
<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
	<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
</div>
<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
	<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2017&nbsp;动力节点</span></div>
</div>

<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
	<div style="position: absolute; top: 0px; right: 60px;">
		<div class="page-header">
			<h1>登录</h1>
		</div>
		<form action="workbench/index.jsp" class="form-horizontal" role="form">
			<div class="form-group form-group-lg">
				<div style="width: 350px;">
					<input class="form-control" type="text" placeholder="用户名" id="loginAct">
				</div>
				<div style="width: 350px; position: relative;top: 20px;">
					<input class="form-control" type="password" placeholder="密码" id="loginPwd">
				</div>
				<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">

					<span id="msg" style="color: red"></span>

				</div>
				<!--
					按钮在from表单中，默认type=submit提交表单。
					因此点击按钮就会提交表单，但是按钮触发的行为应该由我们程序员根据需求来指定。（编写js代码实现）
				-->
				<button type="button" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;" id="bt_dl">登录</button>
			</div>
		</form>
	</div>
</div>
</body>
</html>
