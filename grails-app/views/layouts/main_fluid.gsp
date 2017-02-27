<%@ page import="grails.plugin.springsecurity.SpringSecurityUtils; grails.util.Holders; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>TekVideo | <g:layoutTitle default="Title"/></title>
    <g:layoutHead/>
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <fa:require/>
    <link rel="shortcut icon" href="${asset.assetPath(src: "favicon.ico")}"/>
</head>

<body>

<twbs:navbar>
    <g:content key="navbar-brand">
        <asset:image src="sdu_branch.png" class="navbar-logo"/>
        Video
    </g:content>
    <twbs:navbarLinks>
        <twbs:navbarLink action="list" controller="course">Kurser</twbs:navbarLink>

        <sec:ifAllGranted roles="ROLE_TEACHER">
            <twbs:navbarLink controller="courseManagement">Kursusadministration</twbs:navbarLink>
            <twbs:navbarLink controller="accountManagement" action="manage">Kontoadministration</twbs:navbarLink>
            <twbs:navbarLink controller="stats">Statistikker</twbs:navbarLink>
        </sec:ifAllGranted>
    </twbs:navbarLinks>
    <twbs:navbarPullRight>
        <twbs:navbarLinks>
            <sec:ifNotLoggedIn>
                <twbs:navDropdownToggle>
                    Log ind

                    <twbs:dropdownMenu>
                        <li class="inline-login">
                            <twbs:row>
                                <twbs:column>
                                    <i>Ved hjælp af:</i> <br/>
                                    <twbs:linkButton controller="sso" action="index" style="${ButtonStyle.DEFAULT}"
                                                     block="true">
                                        <asset:image src="sdu_branch.png" class="sdu-logo-button"/> SDU SSO
                                    </twbs:linkButton>
                                </twbs:column>
                            </twbs:row>
                            <twbs:row>
                                <twbs:column>
                                    <i>Eller med en TekVideo bruger</i>
                                    %{--
                                        Is it pretty to lookup configuration in a view? Definitely not.
                                        Is it better than the alternative? Yes.
                                    --}%
                                    <twbs:form action="${createLink(uri: SpringSecurityUtils.securityConfig.apf.filterProcessesUrl)}"
                                               method="POST" id="loginForm" name="loginForm" autocomplete="off">
                                        <twbs:input name="j_username" labelText="Brugernavn" />
                                        <twbs:input name="j_password" type="password" labelText="Kodeord" />
                                        <twbs:checkbox name="rememberMeParameter" id="remember_me" labelText="Forbliv logget ind" />

                                        <twbs:button type="submit" style="${ButtonStyle.PRIMARY}" block="true">
                                            Log ind
                                        </twbs:button>

                                        <twbs:row>
                                            <twbs:column md="6">
                                                <twbs:linkButton controller="register" action="forgotPassword" style="${ButtonStyle.LINK}" block="true">
                                                    Glemt kodeord?
                                                </twbs:linkButton>
                                            </twbs:column>
                                            <twbs:column md="6">
                                                <twbs:linkButton elementId="register" controller="register" style="${ButtonStyle.LINK}" block="true">
                                                    Ny bruger?
                                                </twbs:linkButton>
                                            </twbs:column>
                                        </twbs:row>
                                    </twbs:form>
                                </twbs:column>
                            </twbs:row>
                        </li>
                    </twbs:dropdownMenu>
                </twbs:navDropdownToggle>
            </sec:ifNotLoggedIn>
            <sec:ifLoggedIn>
                <twbs:navDropdownToggle>
                    <sdu:username/>
                    <avatar:gravatar email="${sdu.userEmail()}" cssClass="img-rounded"
                                     defaultGravatarUrl="http://www.gravatar.com/avatar/?d=identicon"/>
                    <twbs:dropdownMenu>
                        <twbs:dropdownItem controller="AccountManagement" action="index">
                            <fa:icon icon="${FaIcon.WRENCH}"/>
                            Konto indstillinger
                        </twbs:dropdownItem>
                        <twbs:dropdownDivider/>
                        <twbs:dropdownItem method="POST" controller="logout" action="">Log ud</twbs:dropdownItem>
                    </twbs:dropdownMenu>
                </twbs:navDropdownToggle>
            </sec:ifLoggedIn>
        </twbs:navbarLinks>
    </twbs:navbarPullRight>
</twbs:navbar>

<twbs:container fluid="true">
    <g:if test="${flash.error}">
        <twbs:alert type="danger">${flash.error}</twbs:alert>
    </g:if>
    <g:if test="${flash.warning}">
        <twbs:alert type="warning">${flash.warning}</twbs:alert>
    </g:if>
    <g:if test="${flash.success}">
        <twbs:alert type="success">${flash.success}</twbs:alert>
    </g:if>
    <g:if test="${flash.message}">
        <twbs:alert type="info">${flash.message}</twbs:alert>
    </g:if>
    <sec:ifNotLoggedIn>
        <g:link controller="sso" action="index">
            <twbs:alert type="info" dismissible="false">
                Klik her for at logge ind via SDU SSO.
            </twbs:alert>
        </g:link>
    </sec:ifNotLoggedIn>
</twbs:container>

<div class="flex-grid">
    <div class="maincontent flex-col">
        <g:layoutBody/>
    </div>

    <g:ifContentAvailable key="sidebar-left">
        <div class="flex-col left">
            <g:selectContent key="sidebar-left"/>
            <g:content key="sidebar-left"/>
        </div>
    </g:ifContentAvailable>

    <g:ifContentAvailable key="sidebar-right">
        <div class="flex-col right">
            <g:selectContent key="sidebar-right"/>
            <g:content key="sidebar-right"/>
        </div>
    </g:ifContentAvailable>
</div>

<g:ifContentAvailable key="content-below-the-fold">
    <div class="flex-grid">
        <div class="maincontent flex-col">
            <g:selectContent key="content-below-the-fold"/>
        </div>

        <g:ifContentAvailable key="sidebar-left">
            <g:ifContentNotAvailable key="sidebar-left-below-the-fold">
                <div class="left flex-col"></div>
            </g:ifContentNotAvailable>
        </g:ifContentAvailable>
        <g:ifContentAvailable key="sidebar-left-below-the-fold">
            <div class="left flex-col">
                <g:selectContent key="sidebar-left-below-the-fold"/>
            </div>
        </g:ifContentAvailable>

        <g:ifContentAvailable key="sidebar-right">
            <g:ifContentNotAvailable key="sidebar-right-below-the-fold">
                <div class="right flex-col"></div>
            </g:ifContentNotAvailable>
        </g:ifContentAvailable>
        <g:ifContentAvailable key="sidebar-right-below-the-fold">
            <div class="right flex-col">
                <g:selectContent key="sidebar-right-below-the-fold"/>
            </div>
        </g:ifContentAvailable>
    </div>
</g:ifContentAvailable>

<twbs:container id="footer">
    <twbs:row>
        <twbs:column cols="3">
            <ul class="list-unstyled">
                <li>
                    <a href="https://github.com/DanThrane/tekvideo.sdu.dk">
                        <fa:icon icon="${FaIcon.GITHUB}"/>
                        GitHub
                    </a>
                </li>
                <li>
                    <g:link controller="about">
                        <fa:icon icon="${FaIcon.USERS}"/>
                        Om TekVideo
                    </g:link>
                </li>
            </ul>
        </twbs:column>
        <twbs:column cols="3">
        </twbs:column>
        <twbs:column cols="6">
            <div class="pull-right">
                <asset:image src="sdu_logo.png"/>
            </div>
        </twbs:column>
    </twbs:row>
</twbs:container>

<g:ifContentAvailable key="layout-script">
    <g:selectContent key="layout-script"/>
</g:ifContentAvailable>

<script>
    $(function () {
        if (!localStorage.getItem("id")) {
            localStorage.setItem("id", generateId());
        }

        events.configure("${createLink(controller: "event", action: "register")}");
        events.start();


        function getRandomInt(min, max) {
            min = Math.ceil(min);
            max = Math.floor(max);
            return Math.floor(Math.random() * (max - min)) + min;
        }

        function generateId(){
            var adjectives = ["afraid","ancient","angry","average","bad","big","bitter","black","blue","brave","breezy","bright","brown","calm","chatty","chilly","clever","cold","cowardly","cuddly","curly","curvy","dangerous","dry","dull","empty","evil","fast","fat","fluffy","foolish","fresh","friendly","funny","fuzzy","gentle","giant","good","great","green","grumpy","happy","hard","heavy","helpless","honest","horrible","hot","hungry","itchy","jolly","kind","lazy","light","little","loud","lovely","lucky","massive","mean","mighty","modern","moody","nasty","neat","nervous","new","nice","odd","old","orange","ordinary","perfect","pink","plastic","polite","popular","pretty","proud","purple","quick","quiet","rare","red","rotten","rude","selfish","serious","shaggy","sharp","short","shy","silent","silly","slimy","slippery","smart","smooth","soft","sour","spicy","splendid","spotty","stale","strange","strong","stupid","sweet","swift","tall","tame","tasty","tender","terrible","thin","tidy","tiny","tough","tricky","ugly","unlucky","warm","weak","wet","white","wicked","wise","witty","wonderful","yellow","young"];
            var animals = ["ape","baboon","badger","bat","bear","bird","bobcat","bulldog","bullfrog","cat","catfish","cheetah","chicken","chipmunk","cobra","cougar","cow","crab","deer","dingo","dodo","dog","dolphin","donkey","dragon","dragonfly","duck","eagle","earwig","eel","elephant","emu","falcon","fireant","firefox","fish","fly","fox","frog","gecko","goat","goose","grasshopper","horse","hound","husky","impala","insect","jellyfish","kangaroo","ladybug","liger","lion","lionfish","lizard","mayfly","mole","monkey","moose","moth","mouse","mule","newt","octopus","otter","owl","panda","panther","parrot","penguin","pig","puma","pug","quail","rabbit","rat","rattlesnake","robin","seahorse","sheep","shrimp","skunk","sloth","snail","snake","squid","starfish","stingray","swan","termite","tiger","treefrog","turkey","turtle","vampirebat","walrus","warthog","wasp","wolverine","wombat","yak","zebra"];

            var adjective = adjectives[getRandomInt(0, adjectives.length)];
            var animal = animals[getRandomInt(0, animals.length)];
            var number = getRandomInt(0, 1000);

            return adjective + "-" + animal + "-" + number;
        }
    });
</script>

</body>
</html>
