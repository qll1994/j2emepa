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
	<div id="graph">
		<form id="graphe_form">
			<p>
				<label for="dayGraph">Day : </label>
				<select name="dayGraph" id="dayGraph">
					<option value="1" selected>1</option>
					<option value="2">2</option>
					<option value="3">3</option>
					<option value="4">4</option>
					<option value="5">5</option>
					<option value="6">6</option>
					<option value="7">7</option>
					<option value="8">8</option>
					<option value="9">9</option>
					<option value="10">10</option>
					<option value="11">11</option>
					<option value="12">12</option>
					<option value="13">13</option>
					<option value="14">14</option>
					<option value="15">15</option>
					<option value="16">16</option>
					<option value="17">17</option>
					<option value="18">18</option>
					<option value="19">19</option>
					<option value="20">20</option>
					<option value="21">21</option>
					<option value="22">22</option>
					<option value="23">23</option>
					<option value="24">24</option>
					<option value="25">25</option>
					<option value="26">26</option>
					<option value="27">27</option>
					<option value="28">28</option>
					<option value="29">29</option>
					<option value="30">30</option>
					<option value="31">31</option>
				</select>
			</p>
			<p>
				<label for="monthGraph">Month : </label>
				<select name="monthGraph" id="monthGraph">
					<option value="1" selected>January</option>
					<option value="2">February</option>
					<option value="3">March</option>
					<option value="4">April</option>
					<option value="5">May</option>
					<option value="6">June</option>
					<option value="7">July</option>
					<option value="8">August</option>
					<option value="9">September</option>
					<option value="10">October</option>
					<option value="11">November</option>
					<option value="12">December</option>
				</select>
			</p>
			<p>
				<label for="yearGraph">Year : </label>
				<input type="number" name="yearGraph" id="yearGraph" min=2015 required="true"/>					
			</p>
			<input class="button" type="button" id="drawGraph" value="Draw !"/>
 		</form> 
		<canvas id="graphContent"  width="600" height="300"></canvas>
	</div>
	<script type="text/javascript" src="./script/monitorScript.js"></script>
</body>
</html>