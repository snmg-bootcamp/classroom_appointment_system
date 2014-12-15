<?php
	// return the times of user's appointment

	include("func.php");
	include("config.php");
	
	$response = NULL;
	$status   = 400;
	$link	  = NULL;

	if(isset($_POST['data'])) 
	{
		$data = json_decode($_POST['data']);
		if(isset($data -> {'client_ver'}) && isset($data -> {'name'}))
		{
			$client_ver = $data -> {'client_ver'};
			$name = $data -> {'name'};
			if($client_ver != $version)
			{
				$status = 401;
				$response = "wrong version";
			}
			else
			{
				// create a new connection of db
				try {
					$link = new PDO($dsn, $user, $password);
				} catch(PDOException $e) {
					printf("DatabaseError: %s", $e->getMessage());
				}

				$arr = array();
				
				// select name, add_time, del_time of a user '$user'
				try {
					$sql = "SELECT name, add_time, del_time FROM user WHERE name='$name'"; 
					$sth = $link->prepare($sql);	// avoid sql inj
					$sth->execute();
					$row = $sth->fetch(PDO::FETCH_ASSOC);
					$tmp = array();
					array_push($tmp, $row['add_time']);
					array_push($tmp, $row['del_time']);
					$arr[$row['name']] = $tmp;
				} catch(PDOException $e) {
					printf("DatabaseError: %s", $e->getMessage());
				}
				$response = $arr;
				$status = 200;
			}
		}
	}
	else {

		// add a new connection of db
		try {
			$link = new PDO($dsn, $user, $password);
		} catch(PDOException $e) {
			printf("DatabaseError: %s", $e->getMessage());
		}

		$arr = array();
		
		// select name, add_time, del_time of all user
		try {
			$sql = 'SELECT name, add_time, del_time FROM user ORDER BY add_time';
			foreach($link->query($sql) as $row) {
				$tmp = array();
				array_push($tmp, $row['add_time']);
				array_push($tmp, $row['del_time']);
				$arr[$row['name']] = $tmp;
			}
		} catch(PDOException $e) {
			printf("DatabaseError: %s", $e->getMessage());
		}
		$response = $arr;
		$status = 200;
	}

	$response_arr = array("status_code" => $status,
						  "response"		=> $response
						 );
	echo json_encode($response_arr, JSON_UNESCAPED_UNICODE);
	$link = NULL;	//close connection of db
?>
