<?php
session_start(); 	
if(!isset($_SESSION['user']))
{
	header("Location: login.php");
	die();
}
else
{
	echo '<?xml version="1.0" encoding="utf-8"?>';
	echo '<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"';
	echo '"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">';
	echo '<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">';
	echo '<head>';
	echo '	<title>Create a new complaint</title>';
	echo '</head>';
	echo '<body>';
	echo '<button onclick="window.location=\'home.php\'">Back</button><br/><br/>';
	echo '<form action="dataprovider\leave_complaint.php" method="post" name="complaintform">';
	echo 'Category:</br>';
	echo '<input type="number" name="category"></input></br>';
	echo 'Sub-Category:</br>';
	echo '<input type="number" name="sub_category"></input></br>';
	echo 'Subject:</br>';
	echo '<input type="text" name="subject"></input></br>';
	echo 'Message:</br>';
	echo '<textarea rows="4" cols="50" name="message"></textarea></br>';
	echo '<input type="submit" value="Submit Complaint"></input></br>';
	echo '</body>';
	echo '</html>';

	
}
?>