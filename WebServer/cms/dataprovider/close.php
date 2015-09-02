<?php
session_start();
require_once __DIR__ . '/db_config.php';
$db = new PDO("mysql:dbname=test_cms;host=localhost", DB_USER, DB_PASSWORD);
if(!isset($_SESSION['user']))
{
	echo 'Please login.';
	header( "refresh:5;url=../login.php" );
	die();
}
else if(isset($_SESSION['cid']))
{
	$stat = "CLOSED";
	$stmt = $db->prepare('UPDATE complaints set stat=:stat where complaint_id=:id');
	$stmt->bindValue(':stat', $stat, PDO::PARAM_STR);
	$stmt->bindValue(':id', $_SESSION['cid'], PDO::PARAM_INT);
	$stmt->execute();
	if($stmt->rowCount() > 0)
	{
		echo 'Successfully closed complaint!. Redirecting you...';
		header( "refresh:5;url=../home.php" );
		die();
	}
	else
	{
		echo "error {$stmt->errorCode()}";
		header( "refresh:5;url=../home.php" );
		die();
	}
}
else{
	echo 'error, try again. Redirecting you...';
	header( "refresh:5;url=../home.php" );
	die();
}