<?php
	include('config.php');
	include('func.php');

	$status 		= "400";	// default status code (error)
	$last_modified  = NULL;
	$response		= NULL;

	//check the user whether send auth token and request data (json format)
	if(isset($_POST['data'])) {
		$data = json_decode($_POST['data']);
		
		if(isset($data -> {'client_ver'}) && 
		   isset($data -> {'delete-number'}) && 
		   isset($data -> {'classroom'}) && 
		   isset($data -> {'sessionid'}) &&
		   isset($data -> {'last-modified'})
		)
		{
			$client_ver 	   =  $data -> {'client_ver'};
			$number            =  $data -> {'delete-number'};
			$classroom		   =  $data -> {'classroom'};
			$token			   =  $data -> {'sessionid'};
			$last_modified	   =  $data -> {'last-modified'};

			if($client_ver != $version) {
				$status = 401;	// wrong client version, client need to be updated.
			}
			else {
					try {
						$link = new PDO($dsn, $user, $password);
					} catch(PDOException $e) {
						printf("DatabaseError: %s", $e->getMessage());
					}

					$sql = "UPDATE `classroom` SET `del_time`=`del_time`+1 WHERE ( `id`='$classroom' )";
					$str = $link->prepare($sql);
					$str->execute(); 
					$url = 'http://classroom.csie.ncu.edu.tw/my_list/delete/'.$number;
					//echo getUrlContent($resource, $url);
					$status = 200;	// success
					getUrlContent($url, $token);
					//$response = filter(getUrlContent($url, $token));
			}
		}
	}
	$response_arr = array("status_code"    => $status, 
						  "last_modified"  => $last_modified,
						  "response"	   => $response
					);
	echo json_encode($response_arr, JSON_UNESCAPED_UNICODE);
?>
