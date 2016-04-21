<%@ page import="dk.sdu.tekvideo.DashboardPeriod; dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<div id="students" class="card-item">
    <twbs:column sm="12">
        <twbs:table condensed="true" responsive="true">
            <thead>
            <th>Navn</th>
            <th>Antal unikke videoer</th>
            <th>Antal svar</th>
            <th>Detaljer</th>
            </thead>
            <tbody>
            </tbody>
        </twbs:table>
        <div id="students-no-data" class="hide">Fandt ingen studerende i dette emne</div>
    </twbs:column>

    <script type="text/template" id="student-row-template">
    <tr>
        <td>{0}</td>
        <td>{1}</td>
        <td>
            {2}
            ({3} <span class="text-success"><fa:icon icon="${FaIcon.CHECK}"/></span>
            /{4} <span class="text-danger"><fa:icon icon="${FaIcon.TIMES}"/></span>)
        </td>

        <td><twbs:button style="${ButtonStyle.INFO}" class="student-more-info">&raquo;</twbs:button></td>
    </tr>
    </script>
</div>