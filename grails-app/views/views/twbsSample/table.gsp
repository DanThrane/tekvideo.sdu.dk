<%@ page import="dk.danthrane.twbs.ContextualColor; dk.danthrane.twbs.Validation" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="twbsmain"/>
    <title>TWBS Demo - Tables</title>
</head>

<body>
<twbs:row>
    <twbs:column>
        <h1>Table Demo</h1>

        <h2>Ordinary Table</h2>
        <twbs:table>
            <thead>
            <twbs:tr>
                <twbs:th context="${ContextualColor.SUCCESS}">Test</twbs:th>
                <twbs:th>Aother header</twbs:th>
                <twbs:th>Lorem</twbs:th>
                <twbs:th>Dolar</twbs:th>
                <twbs:th>Sit amet</twbs:th>
            </twbs:tr>
            </thead>
            <tbody>
            <g:each in="${0..(ContextualColor.values().size() - 1)}" var="i">
                <twbs:tr context="${ContextualColor.values()[i]}">
                    <twbs:td>Data 1!</twbs:td>
                    <twbs:td>Data 2!</twbs:td>
                    <twbs:td>Data 3!</twbs:td>
                    <twbs:td>Data 4!</twbs:td>
                    <twbs:td>Data 5!</twbs:td>
                </twbs:tr>
            </g:each>

            </tbody>
        </twbs:table>

        <h2>Bordered Table</h2>
        <twbs:table bordered="true">
            <thead>
            <twbs:tr>
                <twbs:th context="${ContextualColor.SUCCESS}">Test</twbs:th>
                <twbs:th>Aother header</twbs:th>
                <twbs:th>Lorem</twbs:th>
                <twbs:th>Dolar</twbs:th>
                <twbs:th>Sit amet</twbs:th>
            </twbs:tr>
            </thead>
            <tbody>
            <g:each in="${0..(ContextualColor.values().size() - 1)}" var="i">
                <twbs:tr context="${ContextualColor.values()[i]}">
                    <twbs:td>Data 1!</twbs:td>
                    <twbs:td>Data 2!</twbs:td>
                    <twbs:td>Data 3!</twbs:td>
                    <twbs:td>Data 4!</twbs:td>
                    <twbs:td>Data 5!</twbs:td>
                </twbs:tr>
            </g:each>

            </tbody>
        </twbs:table>

        <h2>Condensed Table</h2>
        <twbs:table condensed="true">
            <thead>
            <twbs:tr>
                <twbs:th context="${ContextualColor.SUCCESS}">Test</twbs:th>
                <twbs:th>Aother header</twbs:th>
                <twbs:th>Lorem</twbs:th>
                <twbs:th>Dolar</twbs:th>
                <twbs:th>Sit amet</twbs:th>
            </twbs:tr>
            </thead>
            <tbody>
            <g:each in="${0..(ContextualColor.values().size() - 1)}" var="i">
                <twbs:tr context="${ContextualColor.values()[i]}">
                    <twbs:td>Data 1!</twbs:td>
                    <twbs:td>Data 2!</twbs:td>
                    <twbs:td>Data 3!</twbs:td>
                    <twbs:td>Data 4!</twbs:td>
                    <twbs:td>Data 5!</twbs:td>
                </twbs:tr>
            </g:each>

            </tbody>
        </twbs:table>

        <h2>Hover Table</h2>
        <twbs:table hover="true">
            <thead>
            <twbs:tr>
                <twbs:th context="${ContextualColor.SUCCESS}">Test</twbs:th>
                <twbs:th>Aother header</twbs:th>
                <twbs:th>Lorem</twbs:th>
                <twbs:th>Dolar</twbs:th>
                <twbs:th>Sit amet</twbs:th>
            </twbs:tr>
            </thead>
            <tbody>
            <g:each in="${0..(ContextualColor.values().size() - 1)}" var="i">
                <twbs:tr context="${ContextualColor.values()[i]}">
                    <twbs:td>Data 1!</twbs:td>
                    <twbs:td>Data 2!</twbs:td>
                    <twbs:td>Data 3!</twbs:td>
                    <twbs:td>Data 4!</twbs:td>
                    <twbs:td>Data 5!</twbs:td>
                </twbs:tr>
            </g:each>

            </tbody>
        </twbs:table>

        <h2>Responsive Table</h2>
        <twbs:table responsive="true">
            <thead>
            <twbs:tr>
                <twbs:th context="${ContextualColor.SUCCESS}">Test</twbs:th>
                <twbs:th>Aother header</twbs:th>
                <twbs:th>Lorem</twbs:th>
                <twbs:th>Dolar</twbs:th>
                <twbs:th>Sit amet</twbs:th>
            </twbs:tr>
            </thead>
            <tbody>
            <g:each in="${0..(ContextualColor.values().size() - 1)}" var="i">
                <twbs:tr context="${ContextualColor.values()[i]}">
                    <twbs:td>Data 1!</twbs:td>
                    <twbs:td>Data 2!</twbs:td>
                    <twbs:td>Data 3!</twbs:td>
                    <twbs:td>Data 4!</twbs:td>
                    <twbs:td>Data 5!</twbs:td>
                </twbs:tr>
            </g:each>

            </tbody>
        </twbs:table>

        <h2>Striped Table</h2>
        <twbs:table striped="true">
            <thead>
            <twbs:tr>
                <twbs:th context="${ContextualColor.SUCCESS}">Test</twbs:th>
                <twbs:th>Aother header</twbs:th>
                <twbs:th>Lorem</twbs:th>
                <twbs:th>Dolar</twbs:th>
                <twbs:th>Sit amet</twbs:th>
            </twbs:tr>
            </thead>
            <tbody>
            <g:each in="${0..(ContextualColor.values().size() - 1)}" var="i">
                <twbs:tr context="${ContextualColor.values()[i]}">
                    <twbs:td>Data 1!</twbs:td>
                    <twbs:td>Data 2!</twbs:td>
                    <twbs:td>Data 3!</twbs:td>
                    <twbs:td>Data 4!</twbs:td>
                    <twbs:td>Data 5!</twbs:td>
                </twbs:tr>
            </g:each>
            <g:each in="${0..5}" var="i">
                <twbs:tr>
                    <twbs:td>Data 1!</twbs:td>
                    <twbs:td>Data 2!</twbs:td>
                    <twbs:td>Data 3!</twbs:td>
                    <twbs:td>Data 4!</twbs:td>
                    <twbs:td>Data 5!</twbs:td>
                </twbs:tr>
            </g:each>
            </tbody>
        </twbs:table>

        <h2>Responsive Hover Table</h2>
        <twbs:table responsive="true" hover="true">
            <thead>
            <twbs:tr>
                <twbs:th context="${ContextualColor.SUCCESS}">Test</twbs:th>
                <twbs:th>Aother header</twbs:th>
                <twbs:th>Lorem</twbs:th>
                <twbs:th>Dolar</twbs:th>
                <twbs:th>Sit amet</twbs:th>
            </twbs:tr>
            </thead>
            <tbody>
            <g:each in="${0..(ContextualColor.values().size() - 1)}" var="i">
                <twbs:tr context="${ContextualColor.values()[i]}">
                    <twbs:td>Data 1!</twbs:td>
                    <twbs:td>Data 2!</twbs:td>
                    <twbs:td>Data 3!</twbs:td>
                    <twbs:td>Data 4!</twbs:td>
                    <twbs:td>Data 5!</twbs:td>
                </twbs:tr>
            </g:each>

            </tbody>
        </twbs:table>
    </twbs:column>
</twbs:row>
</body>
</html>