<html>

<head>
	<title>Ny bruger</title>
	<meta name="layout" content="main"/>
</head>

<body>
<div class="form-group">
	<twbs:container>
		<twbs:row>
			<twbs:column cols="8" offset="2">
				<h1>Ny bruger</h1>
				<g:form action='register' name='registerForm'>

					<twbs:input name="username" bean="${command}" labelText="Brugernavn" />
					<twbs:input name="email" bean="${command}" labelText="Email" type="email" />
					<twbs:input name="password" type="password" labelText="Password" bean="${command}">
						Kodeordet skal indeholde bogstaver, tal, og special symboler
					</twbs:input>
					<twbs:input name="password2" type="password" labelText="Password (igen)" bean="${command}" />

					<div class="form-buttons">
						<s2ui:submitButton class="btn btn-primary" elementId='create' form='registerForm' messageCode='Ny bruger'/>
					</div>
				</g:form>
			</twbs:column>
		</twbs:row>
	</twbs:container>
</div>

<script>
	$(document).ready(function() {
		$('#username').focus();
	});
</script>

</body>
</html>