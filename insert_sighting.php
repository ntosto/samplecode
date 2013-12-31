<?php

/*
 * Following code will create a sighting record and send a notification email
 */

	// array for JSON response
	$response = array();
    
	// include db connect class and connect to the database
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();

	// get the posted parameters
    $SightTime = $_POST['SightTime'];
    $SightLong = $_POST['SightLong'];
    $SightLat = $_POST['SightLat'];
    $Maturity = $_POST['Maturity'];
    $SightComment = $_POST['SightComment'];
    $ReporterName = $_POST['ReporterName'];
    $ReporterPhone = $_POST['ReporterPhone'];
	// construct the Google Maps URL with the passed GPS coordinates
	$MapURL = "http://maps.google.com/maps/api/staticmap?center=$SightLong,$SightLat&zoom=13&size=400x400&markers=icon:http://str.pthosted.com/turtlemarker.png%7C$SightLong,$SightLat&sensor=false";
	
	// mysql insert a new row
	$sql = "INSERT INTO `SeaTurtleReport`.`Sightings` (`SightTime`, `SightLong`, `SightLat`, `Maturity`, `Comment`, `ReporterName`, `ReporterPhone`, `MapURL`) VALUES ('$SightTime', '$SightLong', '$SightLat', '$Maturity', '$SightComment','$ReporterName','$ReporterPhone','$MapURL')";

    $result = mysql_query($sql);

	/* debug
	echo "The SQL:";
	echo $sql;
	echo "<p>";
	*/
	
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Sighting successfully created.";

        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred. $result";
        
        // echoing JSON response
        echo json_encode($response);
    }
	
	// Construct and send notification email
	// TO DO: put mail parameters in external file or database for easier maintenance
	$headers = "From: BSTPReporter@pthosted.com \n";
	$headers .= 'MIME-Version: 1.0' . "\n"; 
	$headers .= 'Content-type: text/html; charset=iso-8859-1' . "\r\n";
	$to = "ntosto@gmail.com";
	$subject = "New Turtle Report!";
	$Map = "<img src='$MapURL' />";
		 
	$body = "<b>Turtle sighting reported:</b><br>";
	$body .= "$SightTime<br>";
	$body .= "Sighting description:<br>";
	$body .= "$SightComment<br>";
	$body .= "Reported by: $ReporterName<br>";
	$body .= "Phone: $ReporterPhone<br>";
	$body .= "Maturity: $Maturity<br>";
	$body .= "GPS coordinates: $SightLong, $SightLat<br>";
	$body .= "$Map";
	
	// send the email
	if (mail($to, $subject, $body, $headers)) {
	   echo("<p>Email successfully sent!</p>");
	} else {
		echo("<p>Email delivery failed…</p>");
	}

?>