<?php

$response = array();
if(isset($_POST['sid']))
{
	session_id($_POST['sid']});
	session_start();
	if(isset($_SESSION['user']))
	{
		if(isset($_POST['cid']))
		{
			require_once '../db_config.php';
			$db = new PDO("mysql:dbname=test_cms;host=localhost", DB_USER, DB_PASSWORD);
			$statement = $db->prepare("SELECT * FROM complaints WHERE complaint_id =:cid");
			$statement->bindValue(":cid",$_POST['cid'],PDO::PARAM_INT);
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