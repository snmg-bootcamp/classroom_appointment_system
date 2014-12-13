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
			}
			else
			{
				try {
					$link = new PDO($dsn, $user, $password);
				} catch(PDOException $e) {
					printf("DatabaseError: %s", $e->getMessage());
				}
				//$test = $link->query("SELECT * FROM `classroom` WHERE `name` = `$classroom`")->fetch();
				//echo $test;
				$arr = array();
				$sql = "SELECT name, add_time, del_time FROM classroom WHERE name='$classroom'"; 
				$sth = $link->prepare($sql);	// avoid sql inj
				$sth->execute();
				$row = $sth->fetch(PDO::FETCH_ASSOC);
				//print_r($row);
				//echo $row['add_time'];
				$tmp = array();
				array_push($tmp, $row['add_time']);
				array_push($tmp, $row['del_time']);
				$arr[$row['name']] = $tmp;
				$response = $arr;
				$status = 200;
			}
		}
	}
	else {
		try {
			$link = new PDO($dsn, $user, $password);
		} catch(PDOException $e) {
			printf("DatabaseError: %s", $e->getMessage());
		}
		/*
		for($i = 0; $i < 13; $i++) {
			$test = $link->query("SELECT * FROM `classroom` WHERE `name`=`$ClassList[$i]`");
			echo $test->fetchColumn();
			echo $test."<br>";
		}
		*/
		$arr = array();
		$sql = 'SELECT name, add_time, del_time FROM classroom ORDER BY name';
		foreach($link->query($sql) as $row) {
			//print $row['name']."\t";
			//print $row['add_time']."\t";
			$tmp = array();
			array_push($tmp, $row['add_time']);
			array_push($tmp, $row['del_time']);
			$arr[$row['name']] = $tmp;
		}
		$response = $arr;
		$status = 200;
	}
	$response_arr = array("status_code" => $status,
						  "response"		=> $response
						 );
	echo json_encode($response_arr, JSON_UNESCAPED_UNICODE);
	$link = NULL;	
?>