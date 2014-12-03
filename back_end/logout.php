<?php
	//logout and delete cookie

	include("func.php");

	$status   = 400;
	$response = "error";

	if(isset($_POST['data'])) 
	{
		$data = json_encode($_POST['data']);
		if(isset($data -> {'token'}))
		{
			$token = $data -> {'token'};
			$url = 'http://classroom.csie.ncu.edu.tw/user/logout';
			//$postdata = "name=$username&pass=$password&form_id=user_login_block";
			getUrlContent($url, $token);
			$status   = 200;
			$response = "succeeful";
		}
	}
	 $response_arr = array("status_code"    => $status,
	                       "response"       => $response
	   				 );
	 echo json_encode($response_arr, JSON_UNESCAPED_UNICODE);
?>
