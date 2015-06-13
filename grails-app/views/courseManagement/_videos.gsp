<%@ page import="dk.sdu.tekvideo.FaIcon" %>
<twbs:table hover="true">
    <thead>
    <tr>
        <th>Navn</th>
        <th>YouTube</th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${videos}" var="video">
        <tr>
            <td>${video.name}</td>
            <td>
                <a href="https://youtube.com/watch?v=${video.youtubeId}">
                    <fa:icon icon="${FaIcon.YOUTUBE_PLAY}" /> YouTube
                </a>
            </td>
        </tr>
    </g:each>
    </tbody>
</twbs:table>