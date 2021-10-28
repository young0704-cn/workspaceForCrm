<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />


<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>


	<script type="text/javascript">

	$(function(){

		$(".activityTime").datetimepicker({
			minView: "month",
			language:  'zh-CN',
			format: 'yyyy-mm-dd',
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		$("#addbtn").click(function (){

			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});

			//从后端获取资源，获取到的用户信息加入到下拉列表
			$.ajax({
				url:"activity/getUserList.do",
				data:{

				},
				type:"get",
				dataType:"json",
				success:function (data) {
					var html = "";
					$.each(data,function (i,n){
						html+="<option value='"+n.id+"'>"+n.name+"</option>"
					})
					$("#create-Owner").html(html);

					var id="${user.id}"
					$("#create-Owner").val(id);
				}
			})

			$("#createActivityModal").modal("show");
		})
		
		$("#save").click(function (){
			var owner=$("#create-Owner").val();
			var name=$("#create-marketActivityName").val();;
			var startDate=$("#create-startTime").val();
			var endDate=$("#create-endTime").val();
			var cost=$("#create-cost").val();
			var description=$("#create-describe").val();
			$.ajax({
				url:"activity/save.do",
				data: {
					"owner":$.trim(owner),"name":$.trim(name),"startDate":$.trim(startDate), "endDate":$.trim(endDate),
					"cost":$.trim(cost), "description":$.trim(description)
				},
				dataType: "json",
				type: "post",
				success:function (result_data) {
					//result_data值可能是		{"success:true/false"}
					if (result_data.success){
						//添加成功	1、刷新市场活动信息列表，ajax异步请求

						/*
							jquery中没有提供重置form表单的方法，但原生js中拥有。
							因此我们将jquery对象转为js对象(dom对象)
							jquery--->dom
								jquery对象[0]	或者		$("#activity-addform").get(0)
							dom--->jquery
								$(dom)
						*/
						$("#activity-addform")[0].reset();
						//2、关闭添加操作的模态窗口
						$("#createActivityModal").modal("hide")
					}else{
						alert(result_data.msg);
					}
					pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

				}
			})
		})

		//全选框绑定事件，触发全选操作
		$("#qx").click(function () {
			$("input[name=xz]").prop("checked",this.checked)
		})
		//$().on(触发事件,选择器,回调函数)	全选框根据复选框情况变更				$("input[name=xz]").click(function () {}语法不能监听动态生成的元素
		$("#activity-tbody").on("click",$("input[name=xz]"),function () {
			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length)
		})

		$("#getbtn").click(function (){
			/*
				只有点击查询按钮，才能确定查询条件
					做法，将搜索文本域中的value值先存入隐藏域。
			*/
			$("#hidden-endTime").val($.trim($("#endTime").val()));
			$("#hidden-startTime").val($.trim($("#startTime").val()));
			$("#hidden-owner").val($.trim($("#getActOwner").val()));
			$("#hidden-name").val($.trim($("#getActName").val()));
			pageList(1,3);
		})

		$("#deletebtn").click(function () {
			if (confirm("删除选中的信息？")){
				var $xz=$("input[name=xz]:checked")
				var parm="";
				if ($xz.length!=0){
					//可能选择了一条或多条信息
					for (i=0;i<$xz.length;i++){
						parm+="ids="+$($xz[i]).val();
						if (i<$xz.length-1){
							parm+="&";
						}
					}

					$.ajax({
						url:"activity/delete.do",
						data:parm,
						dataType:"json",
						type:"post",
						success:function (result_data) {
							/*
                                {"success":true/false,"msg":String s}
                            */
							if (result_data.success){
								alert("删除成功")
								pageList(1,3)
							}else {
								alert(result_data.msg)
							}
							pageList(1,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

						}
					})
				}else {
					alert("请选择要删除的信息")
				}
			}
		})

		$("#editbtn").click(function () {
			$xz=$("input[name=xz]:checked")
			if ($xz.length!=1){
				alert("请选择一条需要更改的信息")
			}else {
				$.ajax({
					url:"activity/edit.do",
					data:{
						"id":$xz.val()
					},
					dataType:"json",
					type:"get",
					success:function(result_data){
						/*
							{uList,activity}
						*/
						var html="<option></option>"
						$.each(result_data.uList,function (i,n) {
							html+="<option value='"+n.id+"'>"+n.name+"</option>"
						})

						$("#edit-id").val(result_data.activity.id)
						$("#edit-name").val(result_data.activity.name)
						$("#edit-startDate").val(result_data.activity.startDate)
						$("#edit-endDate").val(result_data.activity.endDate)
						$("#edit-description").val(result_data.activity.description)
						$("#edit-cost").val(result_data.activity.cost)
						$("#edit-owner").html(html)

						$("#editActivityModal").modal("show")
					}
				})
			}
		})

		$("#updatebtn").click(function () {
			if (confirm("确定更新？")){
				$.ajax({
					url:"activity/update.do",
					dataType:"json",
					type:"post",
					data:{
						id:$("#edit-id").val(),
						owner:$("#edit-owner").val(),
						name:$.trim($("#edit-name").val()),
						startDate: $("#edit-startDate").val(),
						endDate: $("#edit-endDate").val(),
						cost:$.trim($("#edit-cost").val()),
						description: $.trim($("#edit-description").val())
					},
					success:function (result_data) {
						/*
                            {success:true/flase,msg}
                        */
						if (result_data.success){
							$("#editActivityModal").modal("hide");
						}else {
							alert(result_data.msg)
						}
						pageList($("#activityPage").bs_pagination('getOption', 'currentPage')
								,$("#activityPage").bs_pagination('getOption', 'rowsPerPage'));

					}
				})
			}
		})

		pageList(1,3);
	});
			/*分页查询功能实现分析
                分页查询必要的两个参数 pageNo页码	pageSize每页的记录条数
                pageList方法，利用ajax请求后端，从后端获取最新的列表数据，响应刷新到前端页面。
            */
	function pageList(pageNo, pageSize) {
		//每次查询前，将隐藏域中保存的信息取出，重新赋予到搜索文本域
		$("#endTime").val($.trim($("#hidden-endTime").val()));
		$("#startTime").val($.trim($("#hidden-startTime").val()));
		$("#getActOwner").val($.trim($("#hidden-owner").val()));
		$("#getActName").val($.trim($("#hidden-name").val()));
		$.ajax({
			url:"activity/pageList.do",
			data:{
				"pageNo":pageNo,"pageSize":pageSize,"endTime":$.trim($("#endTime").val()),"startTime":$.trim($("#startTime").val()),
				"owner":$.trim($("#getActOwner").val()),"name":$.trim($("#getActName").val())
			},
			dataType:"json",
			type:"get",
			success:function (result_data) {
				/*
                result_data分析
                    {"dataList":[{},{},{}],"total":50}

                前端传给后端
                    map
                        Map<String,Object> map=new HashMap<>();
                        map.put("skipCount",skipCount);
                        map.put("pageSize",pageSize);
                        map.put("act",act);
                后端传给前端
                    map,vo

                    分页查询需要重复使用，因此建议使用vo更方便
                        ViewObject<T>
                        private int total;
                        private List<T> dataList
                */
				html="";
				//每个n就是一条市场活动对象
				$.each(result_data.dataList,function (i,n){
					html+='<tr class="active">';
					html+='<td><input type="checkbox" name="xz" value="'+n.id+'"/></td>'
					html+='<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'activity/detail.do?id='+n.id+'\';">'+n.name+'</a></td>'
					html+='<td>'+n.owner+'</td>'
					html+='<td>'+n.startDate+'</td>'
					html+='<td>'+n.endDate+'</td></tr>'
				})
				$("#activity-tbody").html(html);

				//计算总页数
				var totalPages=result_data.total%pageSize==0?result_data.total/pageSize:parseInt(result_data.total/pageSize)+1
				//数据处理完后，以分页的形式展现信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: result_data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,
					//点击分页组件触发
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});
			}
		})
	}
</script>
</head>
<body>
	<input type="hidden" id="hidden-endTime"/>
	<input type="hidden" id="hidden-startTime"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-name"/>
	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="activity-addform" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-Owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-Owner">

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="save">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-owner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-owner" >

								</select>
							</div>
                            <label for="edit-name" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
								<input type="hidden" id="edit-id"/>
                                <input type="text" class="form-control" id="edit-name">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control activityTime" id="edit-startDate" readonly/>
							</div>
							<label for="edit-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control activityTime" id="edit-endDate" readonly/>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updatebtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="getActName">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="getActOwner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control activityTime" type="text" id="startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control activityTime" type="text" id="endTime">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="getbtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="addbtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editbtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deletebtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activity-tbody">

					</tbody>
<%--						<tr class="active">--%>
<%--							<td><input type="checkbox" /></td>--%>
<%--							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--							<td>2020-10-10</td>--%>
<%--							<td>2020-10-20</td>--%>
<%--						</tr>--%>
<%--                        <tr class="active">--%>
<%--                            <td><input type="checkbox" /></td>--%>
<%--                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>--%>
<%--                            <td>zhangsan</td>--%>
<%--                            <td>2020-10-10</td>--%>
<%--                            <td>2020-10-20</td>--%>
<%--                        </tr>--%>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">
				<div id="activityPage"></div>
			</div>
		</div>
	</div>
</body>
</html>