<head><meta charset="utf-8"></head>
<?php
	include('config.php');
	include('func.php');

	//check the user whether send auth token 
	if(isset($_POST['token'])) {

		//default class
		$class = "A203";

		//login and set cookie
		/*
		$url = 'http://classroom.csie.ncu.edu.tw/appointment_rule?destination=appointment_rule';
		$postdata = "name=$User&pass=$Pass&form_id=user_login_block";
		$resource = setUrlCookie($url, $postdata, $User);
		*/

		if(isset($_GET['class']))
			$class = $_GET['class'];

		$flag = 0;
		for($i = 0; $i < 13; $i++) {
			if($ClassList[$i] === $class) {
				$flag = 1;
				break;
			}
		}
		if($flag) {
			$url = 'http://classroom.csie.ncu.edu.tw/appointment_schedule/'.$class;
			//echo getUrlContent($resource, $url);
			echo json_encode(filter(getUrlContent($url, $_POST['token'])), JSON_UNESCAPED_UNICODE);
		}
		else {
			echo "查無此教室資料";
		}
	}
?>
