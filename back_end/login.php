<?php
	// receive the username & password from front-end to login and save the cookie

	include("func.php");
	
	if(isset($_POST['username']) && isset($_POST['password']))
	{
		$username = $_POST['username'];
		$password = $_POST['password'];
		$url = 'http://classroom.csie.ncu.edu.tw/appointment_rule?destination=appointment_rule';
		$postdata = "name=$username&pass=$password&form_id=user_login_block";
		$resource = setUrlCookie($url, $postdata, $username);
	}
?>
