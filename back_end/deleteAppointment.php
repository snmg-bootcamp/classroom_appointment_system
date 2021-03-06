<?php
	include('config.php');
	include('func.php');

	$status 		= "400";	// default status code (error)
	$last_modified  = NULL;
	$response		= "wrong";

	//check the user whether send auth token and request data (json format)
	if(isset($_POST['data'])) {
		
		$data = json_decode($_POST['data']);
		
		if(isset($data -> {'client_ver'}) && 
		   isset($data -> {'delete-number'}) && 
		   isset($data -> {'sessionid'}) &&
		   isset($data -> {'last-modified'})
		)
		{
			$client_ver 	   =  $data -> {'client_ver'};
			$number            =  $data -> {'delete-number'};
			$token			   =  $data -> {'sessionid'};
			$last_modified	   =  $data -> {'last-modified'};

			if($client_ver != $version) {
				$status = 401;	// wrong client version, client need to be updated.
				$response = "wrong version";
			}
			else {
					//find user's name
					$url = "http://classroom.csie.ncu.edu.tw/appointment_form/".$number;
					$content = getUrlContent($url, $token);
					
					//get name from form
					if(preg_match('/name=\"name\" value=\"(.*)\" size/', $content, $match))
						$name = $match[1];

					//get classroom from form
					if(preg_match('/value=\"(.*)\" selected=\"selected\"/', $content, $match))
						$classroom = $match[1];
									
					if(isset($classroom) && isset($name)) //check the name & classroom is true or wrong
					{
						// add a connection of db
						try {
							$link = new PDO($dsn, $user, $password);
						} catch(PDOException $e) {
							printf("DatabaseError: %s", $e->getMessage());
						}
						
						// update `user` time of delete_appointment
						try {
							$sql = "SELECT del_time FROM user WHERE name = '$name'";
							$str = $link->prepare($sql);
							$str->execute();
							$row = $str->fetch(PDO::FETCH_ASSOC);

							// if the del_time doesn't exist, then add a new user
							if($row == NULL)
							{
								$sql = "INSERT INTO user (name, add_time, del_time) VALUES ('$name', 0, 1)";
								$str = $link->prepare($sql);
								$str->execute();
							}
							else
							{
								$sql = "UPDATE `user` SET `del_time`=`del_time`+1 WHERE ( `name` = '$name' )";
								$str = $link->prepare($sql);
								$str->execute();
							}
						} catch (PDOException $e) {
							printf("DatabaseError: %s", $e->getMessage());
						}
						
						// update `classroom` time of delete_appointment
						try {
							$sql = "UPDATE `classroom` SET `del_time`=`del_time`+1 WHERE ( `name`='$classroom' )";
							$str = $link->prepare($sql);
							$str->execute(); 
						} catch(PDOException $e) {
							printf("DatabaseError: %s", $e->getMessage());
						}

						$url = 'http://classroom.csie.ncu.edu.tw/my_list/delete/'.$number;
						$status = 200;	// success
						$response = "successful";
						getUrlContent($url, $token);
						$link = NULL;	// close the connection of db
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
