<?php

class DBConnector{
	public $db;
	function __construct(){
		$db = $this->connect();
	}
	
	function __destruct(){
	$this->close();
	}
	
	function connect(){
	require_once __DIR__ . '/db_config.php';
	$db = new PDO("mysql:dbname=test_cms;host=localhost", DB_USER, DB_PASSWORD);
	return $db;
	}
	
	function close(){
	mysql_close();
	}
	
}

?>