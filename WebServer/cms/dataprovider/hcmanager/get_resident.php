<?php
$response = array();
if(isset($_POST['sid']))
{
	session_id($_POST['sid']);
	session_start();
	if(isset($_SESSION['user']))
	{
		if(isset($_POST['uid']))
		{
			require_once '../db_config.php';
			$db = new PDO("mysql:dbname=test_sms;host=localhost", DB_USER, DB_PASSWORD);
			$statement = $db->prepare("select first_name,last_name,block_alias,number from test_stdns as stdns inner join blocks as blck on blck.block_id = stdns.block inner join rooms as room on room.room_id = stdns.room where stdns.id_number =:uid");
			$statement->bindValue(":uid",$_POST['uid'],PDO::PARAM_INT);
			$statement->execute();
			$rows = $statement->fetchAll(PDO::FETCH_ASSOC);
			$response['data']= array();
			if(count($rows) > 0)
			{
				$response['success'] = 1;	
				$response['flag']=0;
				array_push($response['data'],$rows[0]);	
			}
			else
			{
				$response['success'] = 1;
				$response['flag']=1;
				$response['data']= "no data";
			}
		}else{
			$response['success'] = 0;
			$response['flag']=0;
			$response['data'] = "uid";
	}
	}else{
		$response['success'] = 0;
		$response['flag']=2;
		$response['data'] = "not logged in";
	}
}
else{
	$response['success'] = 0;
	$response['flag']=0;
	$response['data'] = "sid";
}
	echo json_encode($response);
	die();
?>