<head><meta charset="utf-8"></head>
<?php
	//get one class appointment list

	include('config.php');
	include('func.php');

	//default class
	$class = 'A203';

	//login and set cookie
	$url = 'http://classroom.csie.ncu.edu.tw/appointment_rule?destination=appointment_rule';
	$postdata = "name=$User&pass=$Pass&form_id=user_login_block";
	$resource = setUrlCookie($url, $postdata);

	if(isset($_GET['class']))
		$class = $_GET['class'];

	$url = 'http://classroom.csie.ncu.edu.tw/appointment_schedule/list/'.$class;
	preg_match_all('/\<td\>([^<]*)\<\/td\>/', getUrlContent($resource, $url), $match);
	//print_r($match);
	echo json_encode($match[1], JSON_UNESCAPED_UNICODE);
?>
