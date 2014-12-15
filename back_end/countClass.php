<?php
	// return the times of all classrooms appointment

	include("func.php");
	include("config.php");
	
	$response = NULL;
	$status   = 400;
	$link	  = NULL;

	if(isset($_POST['data'])) 
	{
		$data = json_decode($_POST['data']);
		if(isset($data -> {'client_ver'}) && isset($data -> {'classroom'}))
		{
			$client_ver = $data -> {'client_ver'};
			$classroom = $data -> {'classroom'};
			if($client_ver != $version)
			{
				$status = 401;
				$response = "wrong version";
			}
			else
			{
				try {
					$link = new PDO($dsn, $user, $password);
				} catch(PDOException $e) {
					printf("DatabaseError: %s", $e->getMessage());
				}

				$arr = array();
				
				// select name, add_time, del_time of $classroom
				try {
					$sql = "SELECT name, add_time, del_time FROM classroom WHERE name='$classroom'"; 
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

		// select name, add_time, del_time of all classroom
		try {
			$sql = 'SELECT name, add_time, del_time FROM classroom ORDER BY name';
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
	$link = NULL;	// close connection of db	
?>
