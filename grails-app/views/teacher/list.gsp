<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>${teacher.user.username}</title>
</head>

<body>

<ol class="breadcrumb">
    <li><g:link uri="/">Hjem</g:link></li>
    <li class="active">${teacher.user.username}</li>
</ol>

<twbs:row>
    <twbs:column>
        <h3>Info om ${teacher.user.username}</h3>
    </twbs:column>
</twbs:row>

<twbs:row>
    <twbs:column cols="10">
        <h5>Om underviseren</h5>
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aenean varius varius elit, vel fringilla metus lacinia quis. Nulla sollicitudin facilisis turpis, id fermentum risus ultricies at. Pellentesque id ante tempus nunc placerat malesuada. Sed eu diam ac arcu pretium blandit vel quis nisl. Praesent quis sapien non lorem scelerisque porta nec quis purus. Praesent scelerisque sapien in sapien porttitor lacinia vitae vitae lorem. In id nisi pellentesque nunc volutpat aliquam.</p>
    </twbs:column>
    <twbs:column cols="2">
        <img src="http://lorempixel.com/200/200/people" class="img-rounded img-responsive" />
    </twbs:column>
</twbs:row>

<twbs:row>
    <twbs:column>
        <h5>Aktive fag</h5>
        <g:each in="${courses}" var="course">
            <twbs:row>
                <twbs:column>
                    <g:link mapping="teaching" params="${[teacher: params.teacher, course: course.name]}">
                        <b>${course.name}</b>
                    </g:link>
                    <p>${course.description}</p>
                </twbs:column>
            </twbs:row>
        </g:each>
        <h5>Tidligere fag</h5>
        Ingen.
    </twbs:column>
</twbs:row>

</body>
</html>