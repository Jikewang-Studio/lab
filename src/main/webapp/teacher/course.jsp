<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%--课程页面，教师用于发布实验任务 --%>
<script type="text/javascript">
	$(function(){
		$("#teacher_course_datagrid").datagrid({
			url : '${pageContext.request.contextPath}/Course_getCourseByType?type=admin',
			fitColumns:true,
			fit:true,
			border:true,
			pagination:true,
			idField:'id',
			sortName:'addTime',
			sortOrder:'desc',
			pageList:[14,20,50],
			showHeader:true,
			singleSelect:true,
			frozenColumns:[[{
				field : 'id',
				hidden:true
			},{
				field : 'courseName',
				title : '课程名',
				width : 320,
				align:'left'
			}]],
			columns : [[{
					field : 'courseNumber',
					title : '课程编号',
					width : 80,
					align:'left'
				},{
				field : 'term',
				title : '学期',
				width : 80,
				align:'left',
				formatter:function(value,row,index){
						return '<span>'+value+'</span>';
					}
			},{
				field : 'addTime',
				title : '发布时间',
				width : 100,
				align:'left',
				sortable:true
			},{
				field : 'addCourse',
				title : '添加实验课程',
				width : 100,
				align:'left',
				formatter:function(value,row,index){
					return '<span style="cursor:pointer;color:#8080C0;" onclick="teacher_course_addCourse(\''+row.id+'\');">添加课程</span>';
				}
			}]],
			toolbar:'#teacher_course_toolbar'
		});
	});

	function teacher_course_addCourseAgain(title){
		$('#addCourseAgain').dialog({
			title: title,  
		    width: 240,  
		    height: 176,  
		    href: '${pageContext.request.contextPath}/teacher/addCourseAgain.jsp',
		    modal: true,
		    buttons:[{
		    	text: '提交',
		    	handler: function(){
		    		console.log(href);
		    	}
		    }]
		})
	}
	
	//教师添加课程
	function teacher_course_addCourse(courseId){
		var userId = '${sessionScope.user.userId}';
		$.messager.confirm('请确认','你确定要添加实验课程吗?',function(data){
			if(data){
				// $.ajax({
				// 	url: '${pageContext.request.contextPath}/teacher/Course_getCourseNum?courseId='+ courseId +'&userId='+userId,
				// 	type: 'GET',
				// 	dataType: 'json',
				// 	success: function(d){
				// 		console.log(d);
				// 		//如果是第一次添加
				// 		if(d == 0){
							$.ajax({
								url:'${pageContext.request.contextPath}/teacher/Course_addCourseByCourseId',
								type:'post',
								data:{
									courseId:courseId,
									userId:userId
								},
								dataType:'json',
								success:function(d){
									if(d){
										$.messager.show({
											title:'温馨提示',
											msg:'添加成功!</br><p style="color:red">请在"我的任务栏"中查看！</p>',
											showType:'show',
											style:{
												left: ($(window).width()-238)/2,
												top:($(window).height()-200)/2
			  								}
										});
										$('#teacher_teacherCourse_datagrid').datagrid('load');
									}else{
										$.messager.show({
											title:'温馨提示',
											msg:'对不起,课程已添加!',
											showType:'show',
											style:{
												left: ($(window).width()-238)/2,
												top:($(window).height()-200)/2
			  								}
										});
									}
								}
							});
						//}
						//如果是第二次添加
						/*else if(d == 1){
							//教室第二次添加课程弹出对话框
							$('#addCourseAgain').dialog({
								title: '第二次添加',  
								width: 280,  
								height: 185,  
								href: '${pageContext.request.contextPath}/teacher/addCourseAgain.jsp',
								modal: true,
								buttons:[{
									text: '提交',
									handler: function(){
									    var classNumber1 = $("input[id='classNumber1']").val();
									    var classNumber2 = $("input[id='classNumber2']").val();
									    var classNumber = classNumber1+':'+classNumber2;
									    console.log(classNumber);
									    $.ajax({
									    	url: '${pageContext.request.contextPath}/teacher/Course_addCourseByCourseId',
									    	type: 'POST',
									    	data: {
									    		courseId: courseId,
									    		userId: userId,
									    		classNumber: classNumber
									    	},
									    	dataType: 'json',
									    	success: function(d){
									    		if(d){
													$.messager.show({
														title:'温馨提示',
														msg:'恭喜你,添加成功!'
													});
													$('#teacher_teacherCourse_datagrid').datagrid('load');
													$("#addCourseAgain").dialog('close');
												}else{
													$.messager.show({
														title:'温馨提示',
														msg:'对不起,课程不能添加超过两次!'
													});
												}
									    	}
									    })
									}
								}]
							})
						}
					}*/
				//})
			}else{
				$.messager.show({
					title:'温馨提示',
					msg:'添加已取消!'
				});
			}
			$('#teacher_course_datagrid').datagrid('unselectAll');
		});
	}
	
	//查询
	function teacher_course_search(value,name){
		if(name == 'courseName'){
			courseName = value;
			term = '';
		}else{
			courseName = '';
			term = value;
		}
		
		$('#teacher_course_datagrid').datagrid('load',{
			term:term,
			courseName:courseName
		});
	}
</script>

<table id="teacher_course_datagrid"></table>


<div id="teacher_course_toolbar">
	<input  class="easyui-searchbox" style="width:300px"  data-options="searcher:teacher_course_search,prompt:'请输入搜索信息',menu:'#teacher_course_search'"></input>  
	<div id="teacher_course_search" style="width:120px"> 
	 	<div data-options="name:'courseName',iconCls:'icon-ok'">课程名</div>   
	    <div data-options="name:'term',iconCls:'icon-ok'">学期</div>  
	</div>  
</div>

<div id="addCourseAgain"></div>