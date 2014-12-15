<?php
	// receive the username & password from front-end to login and save the cookie

	include("func.php");
	
	$response = NULL;
	$status   = 400;

	if(isset($_POST['data'])) 
	{
		$data = json_decode($_POST['data']);
		if(isset($data -> {'username'}) && isset($data -> {'password'}))
		{
			$username = $data -> {'username'};
			$password = $data -> {'password'};
			$url = 'http://classroom.csie.ncu.edu.tw/appointment_rule?destination=appointment_rule';
			$postdata = "name=$username&pass=$password&form_id=user_login_block";
			$result = setUrlCookie($url, $postdata, $username);
			
			// clear the cache
			clearstatcache();

			if(filesize('cookie/'.$result))
			{
				$response = $result;
				$status   = 200;
			}
			else
			{
				$response = "error";
			}
		}
	}
	$response_arr = array("status_code" => $status,
						  "response"		=> $response
						 );
	echo json_encode($response_arr, JSON_UNESCAPED_UNICODE);
?>
