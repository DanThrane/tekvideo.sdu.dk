<%@ page import="dk.sdu.tekvideo.FaIcon; dk.danthrane.twbs.ButtonStyle" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Importer skriftlige opgaver fra JSON</title>
    <meta name="layout" content="main_fluid"/>
</head>

<body>
<twbs:row>
    <twbs:column>
        <twbs:pageHeader>
            <h3>Importer skriftlige opgaver fra JSON <small>Tilføjes til: ${subject.name}</small></h3>
        </twbs:pageHeader>
    </twbs:column>
</twbs:row>

<twbs:row>
    <twbs:column md="6">
        <twbs:form action="${createLink(action: "postImportedWrittenExercises", controller: "courseManagement", id: subject.id)}" method="post">
            <twbs:textArea name="json" placeholder="Indsæt JSON her" labelText="Opgaver i JSON format" rows="15" />
            <twbs:button style="${ButtonStyle.PRIMARY}" type="submit">
                <fa:icon icon="${FaIcon.UPLOAD}"/>
                Upload
            </twbs:button>
        </twbs:form>
    </twbs:column>
    <twbs:column md="5" offset-md="1">
        <p>Systemet modtager en JSON encoded array af opgaver. Det er muligt at eksporterer dette fra editoren.</p>

        <p><b>Eksempel input:</b></p>

        <pre>[
    {
        "description": "Beskrivelse til opgave 1",
        "exercises": [
            {
                "document": "",
                "identifier": null,
                "name": "Ny opgave",
                "widgets": {}
            }
        ],
        "name": "Opgave 1",
        "thumbnailUrl": "http://placehold.it/300x300"
    },
    {
        "description": "Beskrivelse til opgave 2",
        /* ... */
    }
]</pre>
    </twbs:column>
</twbs:row>
</body>
</html>