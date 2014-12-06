<?php
	function setUrlCookie($url, $postdata, $username)
	{
		// check the user cookie whether exists
		// if not, create a new cookie file
		// else use the same filename 
		/* 可能有bug */
		//$status     = 400;
		//$response   = NULL;
		$chk_cookie = glob('./cookie/'.$username.'*');
		if($chk_cookie != null) {
			preg_match("/[^(.\/cookie\/)].*/", $chk_cookie[0], $match);
			$cookie_jar = "/usr/local/www/apache22/data/curl_test/cookie/".$match[0];
			//echo $cookie_jar."<br>";
		}
		else {
			$cookie_jar = tempnam('./cookie/',$username); // Create file with unique file name (cookie*)
			//echo $cookie_jar."<br>";
		}
		//$cookie_jar = "./cookie/".$username."cookie.txt";

		$resource = curl_init();
		curl_setopt($resource, CURLOPT_URL, $url);
		curl_setopt($resource, CURLOPT_POST, 1);
		curl_setopt($resource, CURLOPT_POSTFIELDS, $postdata);
		curl_setopt($resource, CURLOPT_COOKIEJAR, $cookie_jar);
		curl_setopt($resource, CURLOPT_COOKIEFILE, $cookie_jar);
		curl_setopt($resource, CURLOPT_RETURNTRANSFER, 1);
		curl_exec($resource);

		// echo cookie filename to front-end, it will be used to auth user identity.
		preg_match("/[^(\/usr\/local\/www\/apache22\/data\/curl_test\/cookie\/)].*/",$cookie_jar, $match);
		
		//if(filesize('cookie/'.$match[0]) > 0) {
		//clearstatcache();
		//echo filesize($cookie_jar);
		/*
		if(filesize($cookie_jar) > 0)
	 	{
			$response = $match[0];
			$status   = 200;
		}
		else {
			$response = "error";
		}

		
		$response_arr = array("status_code"    => $status,
		                      "response"       => $response
		                 );
		echo json_encode($response_arr, JSON_UNESCAPED_UNICODE);
		*/
		return $match[0];
	}

	/*
	function getUrlContent($res, $url)
	{
		curl_setopt($res, CURLOPT_URL, $url);
		curl_setopt($res, CURLOPT_RETURNTRANSFER, 1);
		$content = curl_exec($res);
		//curl_close($res);
		return $content;
	}
	*/

	function getUrlContent($url, $token)
	{
		$cookie_jar = "./cookie/".$token;
		$res = curl_init();
		curl_setopt($res, CURLOPT_URL, $url);
		curl_setopt($res, CURLOPT_RETURNTRANSFER, 1);
		curl_setopt($res, CURLOPT_COOKIEFILE, $cookie_jar);
		$content = curl_exec($res);
		return $content;
	}

	function addAppointment($url, $postdata, $token)
	{
		$cookie_jar = "./cookie/".$token;
		$resource = curl_init();
		curl_setopt($resource, CURLOPT_URL, $url);
		curl_setopt($resource, CURLOPT_POST, 1);
		curl_setopt($resource, CURLOPT_POSTFIELDS, $postdata);
		curl_setopt($resource, CURLOPT_COOKIEFILE, $cookie_jar);
		curl_setopt($resource, CURLOPT_REFERER, 'http://classroom.csie.ncu.edu.tw/appointment_form');
		curl_setopt($resource, CURLOPT_RETURNTRANSFER, 1);
		$content = curl_exec($resource);
		return $content;
	}

	function separate($str)
	{
		$arr = array();
		$flag = 0;
		for($i = 0; $i < strlen($str)-3; ++$i)
		{
			if(substr($str, $i, 5) == "<br/>")
			{
				//echo "fuck";
				$arrData1 = substr($str, 0, $i);
				$arrData2 = substr($str, $i + 5, strlen($str) - $i - 4);
				if($arrData2 == null)continue;
				array_push($arr, $arrData1);
				array_push($arr, $arrData2);
				$flag = 1;
				break;
			}
		}
		return ($flag ? $arr : $str);
	}

	function filter($str)
	{
		$arr = array(
				"0" => array(),
				"1" => array(),
				"2" => array(),
				"3" => array(),
				"4" => array(),
				"5" => array(),
				"6" => array(),
				"7" => array(),
				"8" => array(),
				"9" => array(),
				"10" => array(),
				"11" => array(),
				"12" => array(),
				"13" => array(),
				"14" => array()
				);
		$cnt = 0;
		$tmp = "";
		$flag = 0;
		for($i = 0; $i < strlen($str)-3; $i++)
		{	
			if($flag == 0){
				if(substr($str, $i, 30) == "<table class=\"sticky-enabled\">")
					$flag = 1;
			}
			else if($flag == 1){
				if(substr($str, $i, 4) == "<th>"){
					$flag = 2; 
					$i += 3;
				}
				else if(substr($str, $i, 3) == "<td"){
					$flag = 3;
				}
			}
			else if($flag == 2){
				if(substr($str, $i, 5) == "</th>"){
					array_push($arr[0], $tmp);
					$flag = 1; 
					$tmp = "";
				}
				else if($str[$i] != '/'){
					$tmp = $tmp.$str[$i];
				}
				else{
					$tmp = $tmp.".";
				}
			}
			else if($flag == 3){
				if($str[$i] == '>')
					$flag = 4;
			}
			else if($flag == 4){
				if(substr($str, $i, 4) == "</td"){
					$tmp = separate($tmp);
					array_push($arr[(int)($cnt / 8) + 1], $tmp);	
					$tmp = "";
					$flag = 1;
					$cnt ++;
				}
				else{
					$tmp = $tmp.$str[$i];
				}

			}
		}
		return $arr;
	}

	function getOneDay($str, $week)
	{
		$weekdata = filter($str);
		$result = array();
		for($i = 0; $i <= 14; $i++)
		{
			array_push($result, $weekdata[$i][$week]);
		}
		return $result;
	}

	function CaculateWeekDay($y, $m, $d)
	{
		if($m == 1 || $m == 2)
		{
			$m += 12;
			$y--;
		}
		
		$week = ($d + 2 * $m + 3 * ($m + 1) / 5 + $y + $y / 4 - $y / 100 + $y / 400) % 7;

		return $week + 1;
	}

?>
