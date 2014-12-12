<?php
	//get all class appointment table

	include('config.php');
	include('func.php');
	
	$status         = "400";    // default status code (error)
	$last_modified  = NULL;
	$last_modified  = NULL;

 	//check the user whether send auth token and request data (json format)
	if(isset($_POST['data'])) {
		$data = json_decode($_POST['data']);

		if(isset($data -> {'client_ver'}) &&
		   isset($data -> {'appointment-date'}) &&
		   isset($data -> {'sessionid'}) &&
		   isset($data -> {'last-modified'})
		)
		{
			$client_ver        =  $data -> {'client_ver'};
			$appointment_date  =  $data -> {'appointment-date'};
			$token             =  $data -> {'sessionid'};
			$last_modified     =  $data -> {'last-modified'};

			if($client_ver != $version) {
				$status = 401;  // wrong client version, client need to be updated.
	        	$response = "wrong version";
			}
	        else {
				$total = array();
				for($i = 0; $i < 13; $i++) {
					$url = 'http://classroom.csie.ncu.edu.tw/appointment_schedule/'.$ClassList[$i];
					$total[$ClassList[$i]] = filter(getUrlContent($url, $token));
				}
				/*
				print_r($total);
				echo "<br><br>";
				*/
				$response = $total;
				$status = 200;
			}
		}
	}
	$response_arr = array("status_code"    => $status,
	                      "last_modified"  => $last_modified,
	                      "response"       => $response
	                     );
	echo json_encode($response_arr, JSON_UNESCAPED_UNICODE);
?>
