<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=gb2312">
<TITLE>页面没有找到</TITLE>
</HEAD>
<BODY background=http://image2.sina.com.cn/home/images/tz-001.gif>
	<script type="text/javascript">
		function isIFrameSelf() {
			try {
				if (window.top == window) {
					return false;
				} else {
					return true;
				}
			} catch (e) {
				return true;
			}
		}
		function toHome() {
			if (!isIFrameSelf()) {
				window.location.href = "index";
			}
		}
		window.setTimeout("toHome()", 5000);
	//-->
	</script>

	<table border=0 cellpadding=0 cellspacing=0>
		<tr>
			<td height=134></td>
		</tr>
	</table>
	<table width=544 height=157 border=0 cellpadding=0 cellspacing=0
		align=center>
		<tr valign=middle align=middle>
			<td background=http://image2.sina.com.cn/home/images/tz-002.gif>
				<table border=0 cellpadding=0 cellspacing=0>
					<tr>
						<td style="padding-left: 80px; padding-top: 10px"><strong>页面没有找到
								5秒钟之后将会带您进入导航页!</strong></td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<br>
</BODY>
</HTML>