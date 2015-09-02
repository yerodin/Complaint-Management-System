<?php
$response = array();
if(isset($_POST['sid']))
{
	session_id($_POST['sid']);
	session_start();
	if(isset($_SESSION['user']))
	{
		
		if(isset($_POST['cid']) &&  isset($_POST['message']) && isset($_POST['user']))
		{
			require_once '../db_config.php';
			$db = new PDO("mysql:dbname=test_cms;host=localhost", DB_USER, DB_PASSWORD);
			$date = new DateTime("now", new DateTimeZone('America/Jamaica') );
			$dt = $date->format('Y-m-d H:i:s');	
			
			$stmt = $db->prepare('SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = "test_cms" AND   TABLE_NAME   = "replies"');
			$stmt->execute();
			$rows = $stmt->fetchAll(PDO::FETCH_ASSOC);
			$rid = $rows[0]['AUTO_INCREMENT'];
			
			$stmt = $db->prepare('INSERT INTO replies(complaint_id,user,message,tstamp) values(:id,:user,:mess,:ts); insert into reply_version(id_changed) values(:rid );');
			$stmt->bindValue(":id",$_POST['cid'],PDO::PARAM_INT);
			$stmt->bindValue(":user",$_POST['user'],PDO::PARAM_STR);
			$stmt->bindValue(":mess",$_POST['message'],PDO::PARAM_STR);
			$stmt->bindValue(":ts",$dt,PDO::PARAM_STR);
			$stmt->bindValue(":rid",$rid,PDO::PARAM_INT);
			$stmt->execute();
			if($stmt->rowCount() > 0)
			{
				$response['success'] = 1;	
				$response['flag']=0;	
			}
			else
			{
				$response['success'] = 0;
				$response['flag']=1;
				$response['data']= "no data";
			}
			
		}
		else{
			$response['success'] = 0;
				$response['flag']=0;
				$response['data']= "cid or message or user";
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