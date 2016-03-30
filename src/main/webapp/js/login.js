var second = 0;
var intervalid = 0;
var rex = /^1[3-8]+\d{9}$/;
var sms = function() {
	if (second > 0) {
		return;
	}
	second = 60;
	var p = $("#myModal input[name='phone']").val();
	if (p && p != '' && p != null && rex.test(p)) {
		intervalid = self.setInterval("clock()", 1000);
	} else {
		second = 0;
		alert('请输入正确的手机号');
	}
};

var clock = function() {
	console.log(second);
	second--;
	$('#tips').html('<font color="red">' + second + '</font>秒后可重新获取');
	if (second == 0) {
		$('#tips').html('');
		window.clearInterval(intervalid);
	}
};
