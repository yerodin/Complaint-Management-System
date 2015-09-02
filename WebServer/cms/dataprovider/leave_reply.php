<?php
session_start();
require_once __DIR__ . '/db_config.php';
$db = new PDO("mysql:dbname=test_cms;host=localhost", DB_USER, DB_PASSWORD);
if(!isset($_SESSION['user']))
{
	echo 'Please login.';
	header( "refresh:3;url=../login.php" );
	die();
}
else if(isset($_SESSION['cid']) &&  isset($_POST['message']))
{
	$date = new DateTime("now", new DateTimeZone('America/Jamaica') );
	$dt = $date->format('Y-m-d H:i:s');	
	
	$stmt = $db->prepare('SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = "test_cms" AND   TABLE_NAME   = "replies"');
	$stmt->execute();
	$rows = $stmt->fetchAll(PDO::FETCH_ASSOC);
	$rid = $rows[0]['AUTO_INCREMENT'];
	
	$stmt = $db->prepare('INSERT INTO replies(complaint_id,user,message,tstamp) values(:id,:user,:mess,:ts); insert into reply_version(id_changed) values(:rid );');
	$stmt->bindValue(":id",$_SESSION['cid'],PDO::PARAM_INT);
	$stmt->bindValue(":user",$_SESSION['user'],PDO::PARAM_STR);
	$stmt->bindValue(":mess",$_POST['message'],PDO::PARAM_STR);
	$stmt->bindValue(":ts",$dt,PDO::PARAM_STR);
	$stmt->bindValue(":rid",$rid,PDO::PARAM_INT);
	$stmt->execute();
	if($stmt->rowCount() > 0)
	{
		echo 'Successfully submitted reply!. Redirecting you...';
		header( 'refresh:3;url=../view_complaint.php?id='.$_SESSION['cid'] );
		die();
	}
	else
	{
		echo "error {$stmt->errorCode()}";
		header( "refresh:3;url=../home.php" );
		die();
	}
}
else
{
	echo 'error, try again. Redirecting you...';
	header( "refresh:3;url=../home.php" );
	die();
}
	

?>