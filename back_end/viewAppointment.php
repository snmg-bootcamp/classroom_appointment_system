<head><meta charset="utf-8"></head>
<?php
	//get one class appointment list

	include('config.php');
	include('func.php');

	$status         = "400";    // default status code (error)
	$last_modified  = NULL;
	$response       = NULL;

	//check the user whether send auth token and request data (json format)
	if(isset($_POST['data'])) {
		$data = json_decode($_POST['data']);

		if(isset($data -> {'client_ver'}) &&
		   isset($data -> {'sessionid'}) &&
		   isset($data -> {'last-modified'})
		)
		{
			$client_ver        =  $data -> {'client_ver'};
			$token             =  $data -> {'sessionid'};
			$last_modified     =  $data -> {'last-modified'};

			if($client_ver != $version) {
				$status = 401;  // wrong client version, client need to be updated.
			}
			else {
				$url = 'http://classroom.csie.ncu.edu.tw/my_list';
				//echo getUrlContent($url, $token);
				$content = getUrlContent($url, $token);
				preg_match_all('/\<td\>([^<]*)\<\/td\>/', $content, $match);
				//print_r($match);
				//echo json_encode($match[1], JSON_UNESCAPED_UNICODE);
				$preg = $match[1];

				preg_match_all('/\<a href=\"my_list\/edit\/([^<]*)\"\>/', $content, $match);
				$preg2 = $match[1];

				$result = array();
				for($i = 0; $i < count($preg) / 6; $i++)
				{
					$tmp = array();
					for($j = 0; $j < 6; $j++)
					{
						array_push($tmp, $preg[$i * 6 + $j]);
					}
					array_push($tmp, $preg2[$i]);
					//array_push($result, $tmp);
					$result[$i] = $tmp;
				}
				$response = $result;
				//print_r($response);
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