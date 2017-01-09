<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
<title>Breakdowns Monitor</title>
<link rel="stylesheet" href="./style/style.css" />
</head>
<body>
	<h1>Breakdowns Monitor</h1>
	<div id="beforeUpdate">
		Next update in : <span id="beforeUpdateContent"></span>
	</div>
	<h3>Breakdowns for :</h3>
	<table id="generalMonitor" border="1">
		<tr>
			<td>the last minute : </td>
			<td><span class="number" id="minute"></span></td>
			<td><input class="buttonNumber" type="button" value="details" name="minute" /></td>
			<td><input class="buttonNumber" type="button" value="details per type" name="minutetype" /></td>
		</tr>
		<tr>
			<td>the last hour : </td>
			<td><span class="number" id="hour"></span></td>
			<td><input class="buttonNumber" type="button" value="details" name="hour" /></td>
			<td><input class="buttonNumber" type="button" value="details per type" name="hourtype" /></td>
		</tr>
		<tr>
			<td>the last day : </td>
			<td><span class="number" id="day"></span></td>
			<td><input class="buttonNumber" type="button" value="details" name="day" /></td>
			<td><input class="buttonNumber" type="button" value="details per type" name="daytype" /></td>
		</tr>
		<tr>
			<td>the last month : </td>
			<td><span class="number" id="month"></span></td>
			<td><input class="buttonNumber" type="button" value="details" name="month" /></td>
			<td><input class="buttonNumber" type="button" value="details per type" name="monthtype" /></td>
		</tr>
		<tr>
			<td>ever : </td>
			<td><span class="number" id="ever"></span></td>
			<td><input class="buttonNumber" type="button" value="details" name="ever" /></td>
			<td><input class="buttonNumber" type="button" value="details per type" name="evertype" /></td>
		</tr>
	</table>
	<input type="button" id="reload" value="reload"/>
	<div id="info">
		<h3>Information :</h3>
		<p id="infoContent">None</p>
	</div>
	<div id="content"></div>	
	<script type="text/javascript" src="./script/monitorScript.js"></script>
</body>
</html>