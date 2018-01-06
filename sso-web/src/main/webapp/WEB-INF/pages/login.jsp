<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>用户登录</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<link href="<%=basePath%>css/login.css" rel="stylesheet" type="text/css">
<script src="<%=basePath%>js/jquery-1.7.2.js"></script>
<script src="<%=basePath%>js/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/jquery.tips.js"></script>
<script src="<%=basePath%>js/rsa/jsbn.js"></script>
<script src="<%=basePath%>js/rsa/prng4.js"></script>
<script src="<%=basePath%>js/rsa/rng.js"></script>
<script src="<%=basePath%>js/rsa/rsa.js"></script>
<script src="<%=basePath%>js/rsa/base64.js"></script>
<script>
	$(document).ready(function() {
		//记住账号选取及反选
		$("#autoLogin").click(function() {
			if ($(this).prop('value') == 0) {
				$(this).prop("value", 1);
				$(this).prop('checked', true);
			} else {
				$(this).prop("value", 0);
				$(this).prop('checked', false);
			}
		});

		document.onkeydown = function(e) {
			var ev = document.all ? window.event : e;
			if (ev.keyCode == 13) {
				login();
			}
		}

	});


	function login() {
		var username = $("#username").val().trim();
		var password = $("#password").val().trim();
		var autoLogin = $("#autoLogin").val();
		if (username == '') {
			$("#username").tips({
				side : 1,
				msg : "请输入用户名",
				bg : '#FF5080',
				time : 15
			});
			$("#password").val('');
			$("#password").focus();
			return false;
		} else if (password == '') {
			$("#password").tips({
				side : 1,
				msg : "请输入密码",
				bg : '#FF5080',
				time : 15
			});
			$("#password").focus();
			return false;
		}
		var modulus = "${modulus}";
		var exponent = "${exponent}";

		var rsaKey = new RSAKey();
		rsaKey.setPublic(b64tohex(modulus), b64tohex(exponent));
		password = hex2b64(rsaKey.encrypt(password));
		$("#password").val(password);

		$("#loginForm").submit();
	}
</script>
</head>
<body>
	<div class="container login_box">
		<div class="col-sm-5 col-xs-12">
			<div class="login-and-register-container">
				<div class="login-box">
					<div class="page-header"
						style=" margin-top: 10px; margin-bottom: 5px;">
						<a href="<%=basePath%>"> <img
							src="<%=basePath%>images/bg/st-logo.png" alt="服务贸易"></a>
					</div>
					<p class="left_box">
						<img src="<%=basePath%>images/bg/login-top-bg.jpg" height="240"
							width="450">
					</p>
					<form name="loginForm" id="loginForm" action="login.do"
						method="post" novalidate>
						<input name="service" type="hidden" value="${service }"></input>
						<text>${errormsg }</text>
						<div class="form-group">
							<label class="sr-only" for="account">帐号</label> <i
								class="yhu-ico"></i> <input class="form-control placeholder"
								id="username" name="username" value="" placeholder="会员名/邮箱/手机号"
								type="text">
						</div>
						<div class="form-group">
							<label class="sr-only" for="password">密码</label> <i
								class="paw-ico"></i> <input class="form-control placeholder"
								id="password" name="password" placeholder="请填写登录密码"
								type="password">
						</div>
						<div class="form-group clearfix">
							<div class="checkbox pull-left">
								<label> <input id="autoLogin" name="autoLogin" value="1"
									type="checkbox" checked="checked"> 记住我
								</label>
							</div>
						</div>
						<div class="form-group text-center">
							<button type="button" onclick="login()"
								class="btn btn-primary btn-block">登录</button>
							<a href="register" class="mt_10">注册</a> <a
								href="<%=basePath%>lostPwd/lostPhone"
								style="padding-top:10px;float: left;">忘记密码？</a>
						</div>
					</form>
					<div class="otherLogin">
						<div class="f-14">第三方登录</div>
						<div class="mt-20">
							<a href="<%=basePath%>qq_login"> <img
								src="<%=basePath%>images/bg/btn-login-qq.png">QQ
							</a>
						</div>
						<div class="mt-20">
							<a href="javascript:wx_login();void(0)"> <img
								src="<%=basePath%>images/bg/btn-login-wx.png">微信
							</a>
						</div>
						<div class="mt-20">
							<a href="javascript:wb_login();void(0)">
						</div>
					</div>
					<!-- login-box end -->
				</div>
				<script type="text/javascript">
					function change() {
						$("#code").val("");
					}
				</script>
			</div>
		</div>
</body>
</html>
