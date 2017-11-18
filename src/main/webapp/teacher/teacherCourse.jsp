<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%--教师任务 --%>
<script type="text/javascript">
		var userId = '${sessionScope.user.userId }';
		$(function(){
			$("#teacher_teacherCourse_datagrid").datagrid({
				url : '${pageContext.request.contextPath}/Course_getCourseByUserId?type=teacher&userId='+userId,
				fitColumns:true,
				fit:true,
				pagination:true,
				border:true,
				idField:'id',
				sortName:'addTime',
				sortOrder:'desc',
				pageList:[14,20,50],
				showHeader:true,
				singleSelect:true,
				columns : [[{
					//课程id
					field : 'courseId',
					align:'left',
					width:60,
					hidden:true
				},{
					field : 'courseName',
					title : '实验课程',
					width : 240,
					align:'left'
				},{
					field : 'id',
					title : '教学班编号',
					width:60,
					align:'left'
				},{
					field : 'remark',
					title : '教学班备注（班级代码 专业等）',
					width : 160,
					align :  'left',
					formatter: function(value,row){
						var text;
						if(value){
							text = '修改';
						}else{
							text = '添加';
							value = '';
						}
						return '<a href="#" style="text-decoration:none;color:#8080C0;" onclick="teacher_teacherCourse_remark(\''+row.id+'\',\''+value+'\',\''+text+'\')">'+ text + '</a>&nbsp&nbsp&nbsp' +value ;
					}
				},{
					field : 'courseNumber',
					title : '课程编号',
					width : 80,
					align:'left'
				},{
					field : 'term',
					title : '学期',
					width : 100,
					align:'left',
					formatter:function(value,row,index){
						return '<span>'+value+'</span>';
					}
				},{
					field : 'addTime',
					title : '发布时间',
					width : 120,
					align:'left',
					sortable:true
				},{
					field : 'teacherCourseCl',
					title : '实验处理',
					align:'left',
					width : 120,
					formatter:function(value,row,index){
						return '<span style="cursor:pointer;color:#8080C0;" onclick="teacher_teacherCourse_addTask(\''+row.id+'\',\''+row.courseId+'\');">发布任务</span>'+
						'<span style="cursor:pointer;color:#B22222;margin-left:14px;" onclick="teacher_teacherCourse_deleteCourse(\''+row.id+'\',\''+row.courseId+'\');">删除课程</span>';
					}
				}]],
				view: detailview,
	        	detailFormatter:function(index,row){
	            	return '<div style="padding:2px;"><table id="teacher_teacherCourse-' + index + '"></table></div>';
	        	},
	       		onExpandRow: function(index,row){
	       			var courseName = row.courseName;
					var teacherCourseId = row.id;
					$('#teacher_teacherCourse-'+index).datagrid({
	                	url:'${pageContext.request.contextPath}/Task_getTaskByTeacherCourseId?teacherCourseId='+row.id,
	                	fitColumns:true,
	                	singleSelect:true,
	                	loadMsg:'',
	                	height:'auto',
	                	columns:[[
							{field:'id',width:60,align:'left',hidden:true},      
	                    	{field:'taskName',title:'任务名称',width:110,align:'left'
	                    	},
	                    	{field:'fileNameF',
	                    		title:'实验任务附件',width:100,align:'left',
	                        	formatter:function(value,row,index){

	                        		if (value === undefined) {
	                        			value = ' ';
	                        		}
	            					return '<span style="cursor:pointer;color:#8080C0;" onclick="teacher_teacherCourse_downloadTask(\''+row.fileNameF+'\',\''+row.url+'\');">'+value+'</span>';
	            				}},
	                    	{field:'addTime',title:'发布时间',width:50,align:'left'},
	                    	{field:'url',width:60,align:'left',hidden:true},
	                    	{field:'workDir',width:50,align:'left',hidden:true},
	                    	{field:'getWork',title:'学生作业',width:130,align:'center',
	                    		formatter:function(value,row,index){
	                    			var text = '';
	                    			if(row.isClosed == 0){
	                    				text = '关闭任务上传';
	                    			}else{
	                    				text = '开启任务上传'
	                    			}
	            					return '<span style="cursor:pointer;color:#8080C0;" onclick="teacher_teacherCourse_getWork(\''+row.id+'\',\''+row.taskName+'\',\''+courseName+'\',\'作业\',\'studentWork\');">查看作业</span>\
	            					 	&nbsp;&nbsp;&nbsp;&nbsp;<span style="cursor:pointer;color:#8080C0;" id="toggleWork" onclick="teacher_teacherCourse_toggleWork(\''+text+'\',\''+row.id+'\')">'+text+'</span>\
										 &nbsp;&nbsp;&nbsp;&nbsp;<span style="cursor:pointer;color:#8080C0;" id="toggleWork" onclick="teacher_teacherCourse_notWork(\''+row.id+'\',\''+teacherCourseId+'\')">未提交作业名单</span>\
	            					';
	            				}
	                    	},
	                    	{field:'taskCl',title:'任务处理',width:60,align:'left',
	                    		formatter:function(value,row,index){
	            					return '<span style="cursor:pointer;color:#8080C0;" onclick="teacher_teacherCourse_updateTask(\''+row.id+'\',\''+row.taskName+'\',\''+row.url+'\',\''+row.workDir+'\');">修改任务</span>'+
	            					'<span style="cursor:pointer;color:#B22222;margin-left:14px;" onclick="teacher_teacherCourse_deleteTask(\''+row.id+'\',\''+row.url+'\',\''+row.workDir+'\');">删除任务</span>';;
	            				}
	            			}
	                	]],
	               	onResize:function(){
	                    $('#teacher_teacherCourse_datagrid').datagrid('fixDetailRowHeight',index);
	                },
	                onLoadSuccess:function(){
	                    setTimeout(function(){
	                        $('#teacher_teacherCourse_datagrid').datagrid('fixDetailRowHeight',index);
	                   	},0);
	                }
	           	});
	            $('#teacher_teacherCourse_datagrid').datagrid('fixDetailRowHeight',index);
	       	},
			toolbar:'#teacher_teacherCourse_toolbar'
		});
	});
	
	//教师查看自己实验任务下的学生作业(添加tabs)studentWork
	function teacher_teacherCourse_getWork(id,taskName,name,title,url) {
		var t1 = name+'('+taskName+')下学生实验作业';
		//var t1 = name + title;
		var url1 = '${pageContext.request.contextPath}/teacher/'+url+'.jsp'+'?taskId='+id+'&taskName='+taskName;
 		var t = $('#teacher_teacherCourse_tabs');
 		if (t.tabs('exists', t1)) {
 			t.tabs('select', t1);
 		} else {
			t.tabs('add',{
				title:t1,
				href:url1,
				closable : true
			});
		}
	}

	//开启/关闭作业上传
	function teacher_teacherCourse_toggleWork(text,id){
		var isClosed;
		var toggleText;
		if(text == '关闭任务上传'){
			isClosed = 1;
			toggleText = '开启任务上传';
		}else{
			isClosed = 0;
			toggleText = '关闭任务上传';
		}
		var e = $(event.target);
		$.ajax({
			url: '${pageContext.request.contextPath}/teacher/Task_updateTaskUploadById',
			type: 'POST',
			data:{
				id: id,
				isClosed: isClosed
			},
			success: function(data){
				e.html(toggleText);
				e.attr('onclick','teacher_teacherCourse_toggleWork(\''+toggleText+'\',\''+id+'\')');
			}
		})
		
	}

	//查看未提交作业名单
	function teacher_teacherCourse_notWork(id,teacherCourseId){
		// $.ajax({
		// 	url:'${pageContext.request.contextPath}/teacher/Work_getUnfinishedStudentByTaskId?teacherCourseId='+teacherCourseId+'&taskId='+id, 
		// 	type: 'get',
		// 	dataType: 'json',
		// 	success: function(data){
		// 		console.log(data);
		// 	}
		// })
		var url='${pageContext.request.contextPath}/teacher/unfinishedStudent.jsp?taskId='+id+'&teacherCourseId='+teacherCourseId;
		$("<div/>").dialog({
			title: '未提交作业名单',  
			width: 480,  
		 	height: 320,  
			closed: false,  
			cache: false,  
			content: '<iframe src="' + encodeURI(url) + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>',
		    modal: true,
			onClose:function(){
				$(this).dialog('destroy');
			}
		});
	}
		
	//教师删除自己添加的实验课程(删除课程，任务，实验指导书，学生作业)
	function teacher_teacherCourse_deleteCourse(id,courseId){
		//教师实验课程id
		$.messager.confirm('请确认','删除该实验课程，会删除和该课程相关的作业及实验任务附件，你确定吗?',function(data){
			if(data){
				$.ajax({
					url:'${pageContext.request.contextPath}/teacher/Course_deleteCourseByTeacherCourseId',
					type:'post',
					data:{
						teacherCourseId:id,
						courseId:courseId,
						userId:userId
					},
					dataType:'json',
					success:function(d){
						if(d){
							$('#teacher_teacherCourse_datagrid').datagrid('load');
							$.messager.show({
								title:'提示',
								msg:'删除成功!',
							});
						}else{
							$.messager.show({
								title:'提示',
								msg:'对不起,删除失败!'
							});
						}
					}
				});
			}else{
				$.messager.show({
					title:'提示',
					msg:'已取消删除操作!'
				});
			}
			$('#teacher_teacherCourse_datagrid').datagrid('unselectAll');
		});
	}
	
	//教师删除任务(删除任务，删除指导书，删除学生作业)
	function teacher_teacherCourse_deleteTask(id,url,workDir){
		$.messager.confirm('请确认','删除将会删除所有学生作业，无法撤销<br /><p style="color:red;font-size:24px;font-weight:bolder;text-align:center;">请慎重！</p><p style="text-align:center">你确定吗?</p>',function(data){
			if(data){
				$.ajax({
					url:'${pageContext.request.contextPath}/teacher/Task_deleteTaskById',
					type:'post',
					data:{
						id:id,
						url:url,
						workDir:workDir
					},
					dataType:'json',
					success:function(d){
						if(d){
							$('#teacher_teacherCourse_datagrid').datagrid('load');
							$.messager.show({
								title:'提示',
								msg:'恭喜你,删除成功!'
							});
						}else{
							$.messager.show({
								title:'提示',
								msg:'对不起,删除失败!'
							});
						}
					}
				});
			}else{
				$.messager.show({
					title:'提示',
					msg:'删除已被取消!'
				});
			}
			$('#teacher_teacherCourse_datagrid').datagrid('unselectAll');
		});
	}
	
	//教师修改实验任务（即修改实验指导书）
	function teacher_teacherCourse_updateTask(id,taskName,url,workDir){
		$.messager.confirm('请确认!','你确定要修改实验任务附件吗?',function(data){
			if(data){
				var url1 = '${pageContext.request.contextPath}/teacher/uploadUpdateTask.jsp?taskId='+id+'&taskName='+taskName+'&url='+url+'&workDir='+workDir;
				$("<div/>").dialog({
				    title: '重传实验任务附件',  
				    width: 480,  
				    height: 320,  
				    closed: false,  
				    cache: false,  
				    content: '<iframe src="' + encodeURI(url1) + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>',
				    modal: true,
				    onClose:function(){
				    	$(this).dialog('destroy');
				    }
				});
			}			
		});
	}
	
	//教师下载自己的实验指导书
	function teacher_teacherCourse_downloadTask(name,url){
		$.messager.confirm('请确认','你确定要下载选择的资料吗?',function(data){
			if(data){
				//ajax实现文件下载
				var form = $("<form>");  //==>jQuery创建隐藏表单,实现ajax下载
				form.attr('style','display:none');  
				form.attr('target','');  
				form.attr('method','post');  
				form.attr('action','${pageContext.request.contextPath}/download/Download_downloadFile');  
				$('body').append(form);  
				form.append("<input type='hidden' name='url' value='"+url+"'>");
				form.append("<input type='hidden' name='fileName' value='"+name+"'>");
				form.submit();  
				form.remove();
			}
		});
	}
	//教师发布实验任务
	function teacher_teacherCourse_addTask(teacherCourseId,courseId){
		var url = '${pageContext.request.contextPath}/teacher/uploadTask.jsp?teacherCourseId='+teacherCourseId+'&courseId='+courseId;
		$("<div/>").dialog({
		    title: '上传实验任务附件',  
		    width: 480,  
		    height: 320,  
		    closed: false,  
		    cache: false,  
		    content: '<iframe src="' + url + '" frameborder="0" style="border:0;width:100%;height:100%;"></iframe>',
		    modal: true ,
		    onClose:function(){
		    	$(this).dialog('destroy');
		    } 
		});
	}

	//教师添加/修改教学班备注
	function teacher_teacherCourse_remark(id,remark,text){
		$("<div id='setRemark' />").dialog({
		    title: text+'教学班备注',  
		    width: 280,  
		    height: 120, 
		    closed: false,  
		    cache: false,  
		    content: '<p style="margin-top:20px"><lable>教学班备注：</lable><input type="text" id="remark" value="'+remark+'" /></p><button onclick="teacher_teacherCourse_updateRemark(\''+id+'\')" >确认</button>',
		    modal: true ,
		    onClose:function(){
		    	$(this).dialog('destroy');
		    } 
		});
	}
	//确认添加/修改班级备注
	function teacher_teacherCourse_updateRemark(id){
		$.ajax({
			url: '${pageContext.request.contextPath}/teacher/Course_updateCourseById',
			type: 'post',
			data: {
				id: id,
				remark: $('#remark').val()
			},
			success: function(data){
				$.messager.show({
					title:'温馨提示',
					msg:'恭喜你,设置成功!'
				});
				$('#teacher_teacherCourse_datagrid').datagrid('load');
				$("#setRemark").dialog('close');
			}
		})
	}
</script>

<div id="teacher_teacherCourse_tabs" class="easyui-tabs" data-options="fit:true,border:false" style="height:480px;">
	<div title="我的任务" data-options="tools:[{
        iconCls:'icon-reload',
        handler:function(){
           $('#teacher_teacherCourse_datagrid').datagrid('load',{});
           $('#teacher_teacherCourse_datagrid').datagrid('unselectAll');
        }
    }]"  style="padding:2px;">
		<div id="teacher_teacherCourse_datagrid"></div>
	</div>
	
	<div title="实验课程" data-options="href:'${pageContext.request.contextPath}/teacher/course.jsp',tools:[{
        iconCls:'icon-mini-refresh',
        handler:function(){
           $('#teacher_course_datagrid').datagrid('load',{});
           $('#teacher_course_datagrid').datagrid('unselectAll');
        }
    }]" style="padding:2px;"></div>
    
    <div title="所有任务" data-options="href:'${pageContext.request.contextPath}/teacher/allTeacherCourse.jsp',tools:[{
        iconCls:'icon-mini-refresh',
        handler:function(){
           $('#teacher_allTeacherCourse_datagrid').datagrid('load',{});
           $('#teacher_allTeacherCourse_datagrid').datagrid('unselectAll');
        }
    }]" style="padding:2px;"></div>
</div>

