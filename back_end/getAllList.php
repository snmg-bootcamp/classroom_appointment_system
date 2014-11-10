<head><meta charset="utf-8"></head>
<?php
	//get all classes appointment list

	include('config.php');
	include('func.php');
		
	//login and set cookie
	$url = 'http://classroom.csie.ncu.edu.tw/appointment_rule?destination=appointment_rule';
	$postdata = "name=$User&pass=$Pass&form_id=user_login_block";
	$resource = setUrlCookie($url, $postdata);
	$total = array();
	for($i = 0; $i < 13; $i++) {
		$url = 'http://classroom.csie.ncu.edu.tw/appointment_schedule/list/'.$ClassList[$i];
		preg_match_all('/\<td\>([^<]*)\<\/td\>/', getUrlContent($resource, $url), $match);
		//print_r($match);
		$total[$ClassList[$i]] = $match[1];
	}
	$total = json_encode($total, JSON_UNESCAPED_UNICODE);
	echo $total;
?>
