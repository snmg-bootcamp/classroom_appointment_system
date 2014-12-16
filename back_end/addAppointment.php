<?php

	include('config.php');
	include('func.php');

	$status 		= "400";	// default status code (error)
	$response		= NULL;

	//check the user whether send auth token and request data (json format)
	if(isset($_POST['data'])) {
		
		$data = json_decode(($_POST['data']));
		
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
		   isset($data -> {'sessionid'})
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
			$token			   =  $data -> {'sessionid'};

			if($client_ver != $version) {
				$status = 401;	// wrong client version, client need to be updated.
				$response = "wrong version";
			}
			else {
				$url = 'http://classroom.csie.ncu.edu.tw/appointment_form';
				
				//get form_build_id
				preg_match('/name=\"form_build_id\" value=\"(.*)\"/', getUrlContent($url, $token), $match);
				$form_build_id = $match[1];

				//get form_token
				preg_match('/name=\"form_token\" value=\"(.*)\"/', getUrlContent($url, $token), $match);
				$form_token = $match[1];

				//get form_id
				preg_match('/name=\"form_id\" value=\"(.*)\"/', getUrlContent($url, $token), $match);
				$form_id = $match[1];

				$postdata = "name=$name&phone=$phone&teacher=$teacher&classroom=$classroom&date[month]=$month&date[day]=$day&date[year]=$year&start_period=$start_period&end_period=$end_period&form_build_id=$form_build_id&form_token=$form_token&form_id=$form_id";
				
				$content = addAppointment($url, $postdata, $token);
				
				//check the appointment is successful or failed by matching the h1 content
				if(preg_match('/\<h1 class=\"with-tabs\"\>(.*)\<\/h1\>/', $content, $match)) {

					if($match[1] == "我的預約") {
						
						// create a connection of db
						try {
							$link = new PDO($dsn, $user, $password); 
						} catch(PDOException $e) {
							printf("DatabaseError: %s", $e->getMessage());
						}

						// update the time of `classroom` add_appointment
						try {
							$sql = "UPDATE `classroom` SET `add_time`=`add_time`+1 WHERE ( `id` = '$classroom' )";
							$str = $link->prepare($sql);
							$str->execute();
						} catch(PDOException $e) {
							printf("DatabaseError: %s", $e->getMessage());
						}

						// update the time of `user` add_appointment
						try {
							$sql = "SELECT add_time FROM user WHERE name = '$name'";
							$str = $link->prepare($sql);
							$str->execute();
							$row = $str->fetch(PDO::FETCH_ASSOC);

							// if the name doesn't exist, then insert a new user
							if($row == NULL)
							{
								$sql = "INSERT INTO user (name, add_time, del_time) VALUES ('$name', 1, 0)";
								$str = $link->prepare($sql);
								$str->execute();
							}
							else 
							{
								$sql = "UPDATE `user` SET `add_time`=`add_time`+1 WHERE (`name` = '$name')";
								$str = $link->prepare($sql);
								$str->execute();
							}
						} catch(PDOException $e) {
							printf("DatabaseError: %s", $e->getMessage());
						}

						$link = NULL;	// close connection of db
						$status = 200;	// success
						$response = "successful";
					}
					else {
						// the condition of appointing failed
						// match the error message
						if(preg_match('錯誤訊息</h2>\s(.*)</div>', $content, $err_match))
						{
							$response = $err_match[1];
						}
						else if(preg_match('<li>姓名 欄位必填。</li>\s*<li>電話 欄位必填。</li>', $content, $err_match))
						{
							$response = "姓名、電話欄位必填";
						}
						else
						{
							$response = "can't appoint";
						}
						$status = 402;
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
						  "response"	   => $response
					);
	echo json_encode($response_arr, JSON_UNESCAPED_UNICODE);
?>
