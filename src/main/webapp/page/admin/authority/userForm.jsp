<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑用户菜单</title>
<jsp:include page="../../common/commonjs.jsp"></jsp:include>
<script type="text/javascript">
	//验证电话和手机号码
  $.extend($.fn.validatebox.defaults.rules, {
	  phone: {
	    validator: function(value){
	    var rex=/^1[3-8]+\d{9}$/;
	    var rex2=/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
	    if(rex.test(value)||rex2.test(value))
	    {
	      return true;
	    }else
	    {
	       return false;
	    }
	    },
	    message: '请输入正确电话或手机格式'
	  }
	});
   var isAdd ;
   var roleCombo;
   var tempId;
   //初始化父级节点
   $(function(){
	   //初始化角色列表
		roleCombo = $("#roleCom").combogrid({
			panelWidth: 300,
			multiple: true,
			idField: 'roleId',
			textField: 'roleName',
			url: sy.contextPath + '/getAllRole.do',
			method: 'get',
			columns: [[
				{field:'ck',checkbox:true},
				{field:'roleName',title:'角色名称',width:100},
				{field:'description',title:'描述',width:250}
			]],
			fitColumns: true
		});
	 //初始化添加或者更新
	 
		if("${editType}"==1){
			   
			   isAdd = false;
			   $("#enable").combobox("setValue","${admin.enable}");
			   $("#enable_alert").combobox("setValue","${admin.enable_alert}");
			   //设置角色信息
			  $.post(sy.contextPath+"getRoleByIdUser.do?id=${id}", {}, function(result) {
				  $("#roleCom").combogrid("setValues",result);
			  }, 'json');
			   
		}else{
			   isAdd = true;
		}
	  
   });
   
   /**
	   * 设置提交按钮(添加或者修改权限的按钮)
	   */
	   var submitNow = function($dialog, $grid, $pjq) {
		   parent.sy.progressBar('open');// 关闭上传进度条
			var url ;
			if(isAdd){
				url = sy.contextPath+"addUser.do";
			}else{
				url = sy.contextPath+"editUser.do";
			}
			$.post(url, sy.serializeObject($('form')), function(r) {
				parent.sy.progressBar('close');// 关闭上传进度条
				if (r.flag) {
					$pjq.messager.alert('提示', r.msg, 'info');
					$grid.datagrid('load');
					$dialog.dialog('destroy');
				} else {
					$pjq.messager.alert('提示', r.msg, 'error');
				}
			}, 'json');
		};
	/**
	*设置提交方法
	*/
   var submitForm = function($dialog, $grid, $pjq) {
		if ($('form').form('validate')) {

			submitNow($dialog, $grid, $pjq);

		}
	};
</script>
<style>
  table tr {
    height:35px;
  }
  .main{
       margin-left: 66px;
  }
</style>
</head>
<body>
<div class="main">
   <form method="post">
			<table>
				<tr>
					<td>账号：<input name="admin.userId" class="easyui-textbox"
						value="${admin.userId}" data-options="required:true"
						style="width: 280px;" />
						<s:if test="editType == 1">
							<input name="admin.zabbix_id" type="hidden" value="${admin.zabbix_id}" />
						</s:if>
					</td>

				</tr>
				<tr>
					<td>姓名：<input name="admin.realName" class="easyui-textbox"
						value="${admin.realName}" data-options="required:true"
						style="width: 280px;" />
					</td>
				</tr>
				<tr>
					<td>邮箱：<input name="admin.email"
						class="easyui-textbox" value="${admin.email}"
						data-options="required:true,validType:'email'"
						style="width: 280px;">
					</td>
				</tr>
				<tr>
					<td>电话：<input class="easyui-textbox" name="admin.phone"
						value="${admin.phone}" data-options="validType:'phone'"
						style="width: 280px;">
					</td>
				</tr>
				<tr>
					<td>禁用：<select id="enable" name="admin.enable"
						class="easyui-combobox" data-options="required:true"
						style="width: 280px;">
							<option value="1">未冻结</option>
							<option value="0">冻结</option>
					</select>
				</tr>
				<tr>
					<td>角色：<input id="roleCom" name="admin.roles"
						class="easyui-combotree" style="width: 280px;" /></td>
				</tr>
				<tr>
					<td>接收告警：<select id="enable_alert" name="admin.enable_alert"
						class="easyui-combobox" data-options="required:true"
						style="width: 250px;">
							<option value="1" selected="selected">接收</option>
							<option value="0">不接收</option>
					</select>
				</tr>
				<tr>
					<td><div>描述：</div>
						<div style="margin-top: -16px;margin-left: 50px;width: 100%;">
							<textarea id="" rows=6 name="admin.description"
								style="width: 275px;">
								<s:if test="admin.description !=null">${admin.description}</s:if>
								<s:else>默认密码为123456a?</s:else> </textarea>
						</div></td>
				</tr>
			</table>
		</form>
    </div>
</body>
</html>