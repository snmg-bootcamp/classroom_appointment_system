<?php
	//logout and delete cookie

	include("func.php");

	$status   = 400;
	$response = "error";

	if(isset($_POST['data'])) 
	{
		$data = json_decode($_POST['data']);
		if(isset($data -> {'sessionid'}))
		{
			$token = $data -> {'sessionid'};
			$check = true;

			// check whether the input token is valid or not.
			for($i = 0; $i < strlen($token); $i++)
			{
				if(!(($token[$i] >= 'a' && $token[$i] <= 'z') || 
				     ($token[$i] >= 'A' && $token[$i] <= 'Z') || 
					 ($token[$i] >= '0' && $token[$i] <= '9'))
				)
				{
					$check = false;
					break;
				}
			}

			if($check)
			{
				//$url = 'http://classroom.csie.ncu.edu.tw/user/logout';
				//getUrlContent($url, $token);
				unlink('cookie/'.$token);
				$status   = 200;
				$response = "succeeful";
			}
		}
	}
	 $response_arr = array("status_code"    => $status,
	                       "response"       => $response
	   				 );
	 echo json_encode($response_arr, JSON_UNESCAPED_UNICODE);
?>
