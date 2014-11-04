<head><meta charset="utf-8"></head>
<?php
function setUrlCookie($url, $postdata)
{
    //$cookie_jar = tempnam('./','cookie'); // Create file with unique file name (cookie*)
    $cookie_jar = './cookie.txt';

    $resource = curl_init();
    curl_setopt($resource, CURLOPT_URL, $url);
    curl_setopt($resource, CURLOPT_POST, 1);
    curl_setopt($resource, CURLOPT_POSTFIELDS, $postdata);
    curl_setopt($resource, CURLOPT_COOKIEJAR, $cookie_jar);
	curl_setopt($resource, CURLOPT_COOKIEFILE, $cookie_jar);
    curl_setopt($resource, CURLOPT_RETURNTRANSFER, 1);
    curl_exec($resource);

    return $resource;
}

function getUrlContent($res, $url)
{
    curl_setopt($res, CURLOPT_URL, $url);
    curl_setopt($res, CURLOPT_RETURNTRANSFER, 1);
    $content = curl_exec($res);
	curl_close($res);
    return $content;
}

/*
function filter($str)
{
	$tmp = "";
	$chk = 0;
	for($i=0;$i<strlen($str);$i++)
	{
		if($chk == 0){
			if($str[$i] == '<' && $str[$i+1] == 't' && $str[$i+2] == 'a' && $str[$i+3] == 'b' && $str[$i+4] == 'l' && $str[$i+5] =='e')
			{
				$chk = 1;
				$tmp = $tmp.$str[$i];
			}
		} else {
			$tmp =  $tmp.$str[$i];
		}
	}
	return $tmp;
}
*/

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
			if($str[$i]=='>')
				$flag = 4;
		}
		else if($flag == 4){
			if(substr($str, $i, 4) == "</td"){
				array_push($arr[(int)($cnt/8)+1], $tmp);	
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

//login and set coolie
$url = 'http://classroom.csie.ncu.edu.tw/appointment_rule?destination=appointment_rule';
$postdata = 'name=102502042&pass=***********&form_id=user_login_block';
$resource = setUrlCookie($url, $postdata);

//the target of getting information
$url = 'http://classroom.csie.ncu.edu.tw/appointment_schedule/A209';

//echo getUrlContent($resource, $url);
//echo strip_tags(getUrlContent($resource, $url)); // filter html tags
//print_r(filter2(getUrlContent($resource, $url)));
//echo getUrlContent($resource,$url);

echo "<br><br><br>";

//echo getUrlContent($resource, $url);

echo json_encode(filter(getUrlContent($resource, $url)), JSON_UNESCAPED_UNICODE);

/*
//寫檔
$file = fopen("out.html",'w') or die("g_g");
fwrite($file, filter2(getUrlContent($resource, $url)));
fclose($file);
*/

?>
