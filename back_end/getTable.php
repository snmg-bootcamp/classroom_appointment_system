<?php
	include('config.php');
	include('func.php');

	$status 		= "400";	// default status code (error)
	$last_modified  = NULL;
	$response		= NULL;

	//check the user whether send auth token and request data (json format)
	if(isset($_POST['data'])) {
		$data       	   =  json_decode($_POST['data']);
		
		if(isset($data -> {'client_ver'}) && 
		   isset($data -> {'classroom'}) && 
		   isset($data -> {'appointment-date'}) && 
		   isset($data -> {'sessionid'}) &&
		   isset($data -> {'last-modified'})
		)
		{
			$client_ver 	   =  $data -> {'client_ver'};
			$class             =  $data -> {'classroom'};
			$appointment_date  =  $data -> {'appointment-date'};
			$token			   =  $data -> {'sessionid'};
			$last_modified	   =  $data -> {'last-modified'};

			if($client_ver != $version) {
				$status = 401;	// wrong client version, client need to be updated.
			}
			else {
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
					$status = 200;	// success
					$response = filter(getUrlContent($url, $token));
				}	
			}
		}
	}
	$response_arr = array("status_code"    => $status, 
						  "last_modified"  => $last_modified,
						  "response"	   => $response
					);
	echo json_encode($response_arr, JSON_UNESCAPED_UNICODE);
?>
