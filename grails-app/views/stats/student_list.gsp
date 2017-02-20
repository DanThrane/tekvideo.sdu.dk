<%--suppress HtmlUnknownTag --%>
<g:applyLayout name="stats_layout">
    <html xmlns="">
    <head>
        <title>Studerende i ${node.name}</title>
    </head>

    <body>
    <twbs:row>
        <twbs:column>
            <h3>Stats</h3>
            <ul>
                <li>Antal studerende: ${students.size()}</li>
            </ul>
        </twbs:column>
    </twbs:row>

    <twbs:row>
        <twbs:column>
            <h3>Administrative opgaver</h3>
            <ul>
                <li><a href="#">Masse-inviter</a></li>
                <li><a href="#">Eksportér bruger data</a></li>
            </ul>
        </twbs:column>
    </twbs:row>

    <twbs:row>
        <twbs:column>
            <twbs:table responsive="true" striped="true" hover="true">
                <thead>
                <twbs:tr>
                    <twbs:th>Bruger type</twbs:th>
                    <twbs:th>Brugernavn</twbs:th>
                    <twbs:th>Muligheder</twbs:th>
                </twbs:tr>
                </thead>
                <tbody>
                <g:each in="${students}" var="student">
                    <twbs:tr>
                        <twbs:td>${student.isCas ? "SDU" : "TekVideo"}</twbs:td>
                        <twbs:td>
                            <g:if test="${student.realName != null}">
                                <span title="Fulde navn">${student.realName}</span>
                                <span title="Brugernavn">(${student.username})</span>
                            </g:if>
                            <g:else>
                                <span title="Brugernavn">${student.username}</span>
                            </g:else>
                        </twbs:td>
                        <twbs:td>???</twbs:td>
                    </twbs:tr>
                </g:each>
                </tbody>
            </twbs:table>
        </twbs:column>
    </twbs:row>

    </body>
    </html>
</g:applyLayout>
