<?php

	include('config.php');
	include('func.php');

	$status 		= "400";	// default status code (error)
	$last_modified  = NULL;
	$response		= NULL;

	//check the user whether send auth token and request data (json format)
	if(isset($_POST['data'])) {
		//echo $_POST['data'];
		$data = json_decode(($_POST['data']));
		//print_r($data);
		if(isset($data -> {'client_ver'}) && 
		   isset($data -> {'name'}) && 
		   isset($data -> {'phone'}) && 
		   isset($data -> {'teacher'}) &&
		   isset($data -> {'classroom'}) &&
		   isset($data -> {'month'}) &&
		   isset($data -> {'day'}) &&
		   isset($data -> {'year'}) &&
		   isset($data -> {'start_period'}) &&
		   isset($data -> {'end_period'}) &&
           isset($data -> {'note'}) &&
           isset($data -> {'appointment_number'}) &&
           isset($data -> {'sessionid'}) &&
		   isset($data -> {'last-modified'})
		)
		{
			$client_ver 	   =  $data -> {'client_ver'};
			$name			   =  $data -> {'name'};
			$phone			   =  $data -> {'phone'};
			$teacher		   =  $data -> {'teacher'};
			$classroom         =  $data -> {'classroom'};
			$month			   =  $data -> {'month'};
			$day			   =  $data -> {'day'};
			$year			   =  $data -> {'year'};
			$start_period	   =  $data -> {'start_period'};
			$end_period		   =  $data -> {'end_period'};
            $note			   =  $data -> {'note'};
            $appointment_number=  $data -> {'appointment_number'};
			$token			   =  $data -> {'sessionid'};
			$last_modified	   =  $data -> {'last-modified'};

			if($client_ver != $version) {
				$status = 401;	// wrong client version, client need to be updated.
				$response = "wrong version";
			}
			else {
				$url = 'http://classroom.csie.ncu.edu.tw/appointment_form/'.$appointment_number;
				//echo getUrlContent($resource, $url);
				
				preg_match('/name=\"form_build_id\" value=\"(.*)\"/', getUrlContent($url, $token), $match);
				$form_build_id = $match[1];

				preg_match('/name=\"form_token\" value=\"(.*)\"/', getUrlContent($url, $token), $match);
				$form_token = $match[1];

				preg_match('/name=\"form_id\" value=\"(.*)\"/', getUrlContent($url, $token), $match);
				$form_id = $match[1];

				$postdata = "name=$name&phone=$phone&teacher=$teacher&classroom=$classroom&date[month]=$month&date[day]=$day&date[year]=$year&start_period=$start_period&end_period=$end_period&form_build_id=$form_build_id&form_token=$form_token&form_id=$form_id";
				
				$content = addAppointment($url, $postdata, $token);
				
				//check the appointment is successful or failed by matching the h1 content
				if(preg_match('/\<h1 class=\"with-tabs\"\>(.*)\<\/h1\>/', $content, $match)) {

					if($match[1] == "我的預約") {
						$status = 200;	// success
						$response = "successful";
					}
					else {
						$status = 402;
						$response = "can't appoint";
					}
				}
				else {
					$status = 400;
					$response = "match fail";
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
