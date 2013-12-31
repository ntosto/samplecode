<!--    program to show the latest turtle report entries and enable downloading of the Android app   -->
<html>
<body 
background="http://str.pthosted.com/old_moon_@2X.jpg"
background-repeat= repeat-x
background-repeat= repeat-y>

<h2>Barbados Sea Turtle Report</h2>
<div>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<IMG SRC="./bstplogo.png" ALT="some text" WIDTH=120 HEIGHT=120>
<div>
<br>
<p1><a href="./SeaTurtleReport.apk">Click here (from an Android device) to download the app</a></p1>
<div>
<br>
<h3>Screenshots</h3>
<IMG SRC="./WelcomeSS.png" ALT="some text" WIDTH=300 HEIGHT=575> <IMG SRC="./DataFormSS.png" ALT="some text" WIDTH=120 HEIGHT=120> 
<h3> Activity Database View (last 20 entries)</>
</body>
</html>
<?php
	// display the database records

	// include db connect class and connect to the database
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();

	// get the last set of sightings
	// use MAX(SightingNum) - 20 to get only the last 20 records
	$query = 'SELECT * FROM `Sightings` WHERE `SightingNum` > (SELECT MAX(`SightingNum`) - 20 FROM `Sightings`)';
	$result = mysql_query($query);

	// build the data table
	echo "<table border='1'>
	<tr>
	<th>Sighting Time</th>
	<th>Reporter Name</th>
	<th>Reporter Phone</th>
	<th>Comments</th>
	<th>Longitude</th>
	<th>Latitude</th>
	<th>MapURL</th>
	</tr>";

	// populate the rows
	while($row = mysql_fetch_array($result))
	{
		echo "<tr>";
		//echo "<td>" . $row['SightingNum'] . "</td>";
		echo "<td>" . $row['SightTime'] . "&nbsp" . "</td>";
		echo "<td>" . $row['ReporterName'] . "&nbsp" . "</td>";
		echo "<td>" . $row['ReporterPhone'] . "&nbsp" . "</td>";
		echo "<td>" . $row['Comment'] . "&nbsp" . "</td>";
		echo "<td>" . $row['SightLong'] . "&nbsp" . "</td>";
		echo "<td>" . $row['SightLat'] . "&nbsp" . "</td>";
		echo "<td>" . "<a href=" . $row['MapURL'] . ">Map Link</a>" . "</td>";
		echo "</tr>";
	}
	echo "</table>";			
			
?>
