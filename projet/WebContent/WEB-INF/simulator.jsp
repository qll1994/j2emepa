<html>
	<head>
		<title>Breakdowns Simulator</title>
		 <link rel="stylesheet" href="./style/style.css" />
	</head>
	<body>
		<h1>Breakdowns Simulator</h1>
		<div id="reloadlink">
			<a href="${pageContext.request.contextPath}/Simulator">Reload</a>
		</div>
		<div id="pannesaisie">
			<h2>Custom breakdown</h2>
			<form target="noone">
				<p>
					<label for="machine">Targeted machine : </label><input class="data" title="16 hexadecimal characters. Ex : AF109AFF023CDE56" id="machine" type="text" name="machine" pattern="[a-fA-F0-9]{16}" required="true"/>
				</p>
				<p>
					<label for="typepanne">Type of breakdown : </label>
					<select name="typepanne" id="typepanne">
						<option value="reseau" selected>Network</option>
						<option value="crashdisque">Disk crash</option>
						<option value="memoire">Memory problem</option>
					</select>
				</p>
				<input class="button" type="button" onclick="generate()" value="Generate"/>
	 		</form> 
		</div>
		<div id="panneauto">
			<h2>Random breakdown</h2>
			<form target="noone">
				<input class="button" type="button" onclick="randomGenerate()" value="Generate random breakdown"/>
			</form>
		</div>
		<div id="pannes">
			<h2>Many breakdowns at once</h2>
			<form target="noone">
				<p>
					<label for="nombre">Number of breakdowns : </label><input class="data" type="number" name="nombre" id="nombre" min=2 required="true"/>
				</p>
				<input class="button" type="button" onclick="manyGenerate()" value="Generate breakdowns"/>
			</form>
		</div>
		<div id="pannesOnTime">
			<h2>Many breakdowns over time</h2>
			<form target="noone">
				<p>
					<label for="nombre">Number of breakdowns : </label><input class="data" type="number" id="nbOnTime" name="nombreDuree" min=2 required="true"/>
				</p>
				<p>
					<label for="duree">Time (seconde) : </label><input class="data" type="number" id="dureeOnTime" name="duree" min=20 required="true"/>
				</p>
				<span id="buttonGeneration">
					<input class="button" id="buttonOverTime" type="button" onclick="generateOverTime()" value="Generate breakdowns"/>
					<input class="button" id="buttonStop" type="button" onclick="stopGeneration()" value="Stop generation"/>
				</span>				
			</form>
		</div>
		<div id="info">
			<b>Information :</b>
			<p id="infoContent">None</p>
		</div>
	<script type="text/javascript" src="./script/simulatorScript.js"></script>
	</body>
</html>