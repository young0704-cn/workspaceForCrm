<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%String basePath = request.getScheme() + "://" + request.getServerName() + ":" + 	request.getServerPort() + request.getContextPath() + "/";%>
<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>">
    <script type="text/javascript" src="jquery/echarts.min.js"></script>
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script>
       $(function () {
           $.ajax({
               url:"tran/tranPieChart.do",
               dataType:"json",
               type:"get",
               success:function (result_data) {
                   // 基于准备好的dom，初始化echarts实例
                   var myChart = echarts.init(document.getElementById('main'));

                   // 指定图表的配置项和数据
                   var option = {
                       title: {
                           text: '交易状态统计图',
                           left: 'center'
                       },
                       tooltip: {
                           trigger: 'item'
                       },
                       legend: {
                           top: '5%',
                           left: 'center'
                       },
                       series: [
                           {
                               name: 'Access From',
                               type: 'pie',
                               radius: ['40%', '70%'],
                               avoidLabelOverlap: false,
                               itemStyle: {
                                   borderRadius: 10,
                                   borderColor: '#fff',
                                   borderWidth: 2
                               },
                               label: {
                                   show: false,
                                   position: 'center'
                               },
                               emphasis: {
                                   label: {
                                       show: true,
                                       fontSize: '40',
                                       fontWeight: 'bold'
                                   }
                               },
                               labelLine: {
                                   show: false
                               },
                               data: result_data
                           }
                       ]
                   };

                   // 使用刚指定的配置项和数据显示图表。
                   option && myChart.setOption(option);
               }
           })
       })
    </script>
</head>
<body>
    <div id="main" style="width: 600px;height:400px;"></div>
</body>
</html>