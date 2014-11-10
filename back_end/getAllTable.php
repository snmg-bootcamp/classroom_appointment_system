<head><meta charset="utf-8"></head>
<?php
	//get all class appointment table

	include('config.php');
	include('func.php');
		
	//login and set cookie
	$url = 'http://classroom.csie.ncu.edu.tw/appointment_rule?destination=appointment_rule';
	$postdata = "name=$User&pass=$Pass&form_id=user_login_block";
	$resource = setUrlCookie($url, $postdata);
	$total = array();
	for($i = 0; $i < 13; $i++) {
		$url = 'http://classroom.csie.ncu.edu.tw/appointment_schedule/'.$ClassList[$i];
		$total[$ClassList[$i]] = filter(getUrlContent($resource, $url));
	}
	/*
	print_r($total);
	echo "<br><br>";
	*/
	$total = json_encode($total, JSON_UNESCAPED_UNICODE);
	echo $total;
?>
