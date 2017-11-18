<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/javaScript/jQueryEasyUI/themes/metro/easyui.css" type="text/css"></link>
<style>
    body{
        margin: 0;
    }
    #unfinished{
        width: 100%;
        text-align: center;
        font-size: 12px;
    }
</style>


<script type="text/javascript"	src="${pageContext.request.contextPath}/javaScript/jquery-1.8.0.min.js"></script>
<SCRIPT type="text/javascript" src="${pageContext.request.contextPath}/javaScript/jQueryEasyUI/jquery.easyui.min.js"></SCRIPT>
<script type="text/javascript" src="${pageContext.request.contextPath}/javaScript/jQueryEasyUI/datagrid-detailview.js"></script>
<script>
    $(function(){
        var taskId = '<%=request.getParameter("taskId")%>';
        var teacherCourseId = '<%=request.getParameter("teacherCourseId")%>';
        $.ajax({
            url:'${pageContext.request.contextPath}/teacher/Work_getUnfinishedStudentByTaskId?teacherCourseId='+teacherCourseId+'&taskId='+taskId, 
            type: 'get',
            dataType: 'json',
            success: function(data){
                var headStr = '<thead class="datagrid-header"><tr><td>学生姓名</td><td>学生学号</td></tr></thead>';
                var str = '';
                for(var tableItem in data){
                    str += '<tr class="datagrid-row"><td class="datagrid-cell">'+data[tableItem].userName+'</td><td>'+data[tableItem].userId+'</td></tr>';
                }
                str = headStr+str;
                console.log(str);
                $('#unfinished').html(str);
            }
        })
    })
</script>
<table id="unfinished" class="datagrid-btable" border="0" cellspacing="0" cellpadding="0"></table>