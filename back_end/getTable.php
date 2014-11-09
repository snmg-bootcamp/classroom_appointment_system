<head><meta charset="utf-8"></head>
<?php

	include('config.php');
	include('func.php');
		
	//default class
	$class = "A203";

	//login and set cookie
	$url = 'http://classroom.csie.ncu.edu.tw/appointment_rule?destination=appointment_rule';
	$postdata = "name=$User&pass=$Pass&form_id=user_login_block";
	$resource = setUrlCookie($url, $postdata);

	if(isset($_GET['class']))
		$class = $_GET['class'];

	$flag = 0;
	for($i = 0; $i < 13; $i++) {
		if($ClassList[$i] == $class) {
			$flag = 1;
			break;
		}
	}
	if($flag) {
		$url = 'http://classroom.csie.ncu.edu.tw/appointment_schedule/'.$class;
		echo "<br><br><br>";
		//echo getUrlContent($resource, $url);
		echo json_encode(filter(getUrlContent($resource, $url)), JSON_UNESCAPED_UNICODE);
	}
	else {
		echo "查無此教室資料";
	}
?>
