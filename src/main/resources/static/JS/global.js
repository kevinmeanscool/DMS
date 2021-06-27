/*
 * Carbon-Forum
 * https://github.com/lincanbin/Carbon-Forum
 *
 * Copyright 2006-2017 Canbin Lin (lincanbin@hotmail.com)
 * http://www.94cb.com/
 *
 * Licensed under the Apache License, Version 2.0:
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * A high performance open-source forum software written in PHP. 
 */


/*
// 当使用的jQuery CDN爆炸时，切换到当前服务器的jQuery备胎
if (!window.jQuery) {
	console.log('Switch to jQuery Backup.');
	loadScript(WebsitePath + "/static/js/jquery.js",function(){
		loadScript(WebsitePath + "/static/js/global.js",function(){});
	});
}
*/


$(function() {
	//Button go to top
	function setButtonToTop() {
		//Force to refresh under pjax
		$("#go-to-top").css('left', (Math.max(document.body.clientWidth, 960) - 960) / 2 + 690);
		$("#go-to-top").unbind('click');
		$("#go-to-top").click(function() {
			$("html, body").animate({
				"scrollTop": 0
			},
			400);
			return false;
		});

		isNotificationPage = isUrlEndWith('/notifications/list');
		isInboxPage = (new RegExp("/inbox/[0-9]+$")).test(window.document.location.pathname);
	}

	function isUrlEndWith(endStr) {
		var url = window.document.location.pathname;
		var d = url.length - endStr.length;
		return d >= 0 && url.lastIndexOf(endStr) === d;
	}

	function loadNotificationsList() {
		var top = $(this).scrollTop();
		if (top + $(window).height() + 20 >= $(document).height() && top > 20) {
			loadMoreReply(false);
			loadMoreMention(false);
			loadMoreInbox(false);
		}
	}
	
	function loadMessagesList() {
		var top = $(this).scrollTop();
		if (top + $(window).height() + 20 >= $(document).height() && top > 20) {
			loadMoreMessages(false);
		}
	}

	//Initialize position of button
	setButtonToTop();
	var isNotificationPage = isUrlEndWith('/notifications/list');
	var isInboxPage = (new RegExp("/inbox/[0-9]+$")).test(window.document.location.pathname);
	$(window).scroll(function() {
		var top = $(document).scrollTop();
		var g = $("#go-to-top");
		if (top > 500 && g.is(":hidden")) {
			g.fadeIn();
		} else if (top < 500 && g.is(":visible")) {
			g.fadeOut();
		}
		if (isNotificationPage) {
			loadNotificationsList();
		}
		if (isInboxPage) {
			loadMessagesList();
		}
	});
	$(window).resize(function() {
		$("#go-to-top").css('left', (Math.max(document.body.clientWidth, 960) - 960) / 2 + 690);
	});
});