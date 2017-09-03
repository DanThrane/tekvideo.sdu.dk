<%@ page import="dk.danthrane.twbs.NavStyle; dk.danthrane.twbs.NavBarPlacement; dk.danthrane.twbs.ContextualColor; dk.danthrane.twbs.Validation" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="twbsmain"/>
    <title>TWBS Demo - Navbar</title>
</head>

<body>

<twbs:row>
    <twbs:pageHeader>
        <h1>Navigation bars</h1>
    </twbs:pageHeader>
    <twbs:navbar inverse="false">
        <g:content key="navbar-brand">Brand ${System.currentTimeMillis()}</g:content>
        <twbs:navbarPullRight>
            <twbs:navbarLinks>
                <twbs:navDropdownToggle>
                    My Dropdown
                    <twbs:dropdownMenu>
                        <twbs:dropdownHeader>Header!</twbs:dropdownHeader>
                        <twbs:dropdownItem disabled="true">Item 1</twbs:dropdownItem>
                        <twbs:dropdownItem>Item 2</twbs:dropdownItem>
                        <twbs:dropdownDivider />
                        <twbs:dropdownItem>Item 3</twbs:dropdownItem>
                    </twbs:dropdownMenu>
                </twbs:navDropdownToggle>
            </twbs:navbarLinks>
        </twbs:navbarPullRight>
    </twbs:navbar>

    <twbs:navbar inverse="true">
        <g:content key="navbar-brand">Brand ${System.currentTimeMillis()}</g:content>
    </twbs:navbar>

    <twbs:navbar>
        <g:content key="navbar-brand">Brand 22</g:content>
        <twbs:navbarPullRight>
            <twbs:navbarForm>
                <twbs:input name="Test" showLabel="false"/>
                <twbs:button>Search</twbs:button>
            </twbs:navbarForm>
        </twbs:navbarPullRight>
    </twbs:navbar>

    <twbs:navbar>
        <g:content key="navbar-brand">Brand 30</g:content>
        <twbs:navbarText>This is some text</twbs:navbarText>
        <twbs:navbarPullRight>
            <twbs:navbarText>Even more text, to the right!</twbs:navbarText>
            <twbs:navbarText>Item 2</twbs:navbarText>
            <twbs:navbarText>Item 3</twbs:navbarText>
        </twbs:navbarPullRight>

        <twbs:navbarText>
            I should be <twbs:navbarNonNavLink action="table" class="test">left</twbs:navbarNonNavLink>
        </twbs:navbarText>

        <twbs:navbarPullRight>
            <twbs:navbarLinks>
                <twbs:button>Test button</twbs:button>
                <twbs:navbarLink action="table" active="true">Link 1</twbs:navbarLink>
                <twbs:navbarLink action="table">Link 2</twbs:navbarLink>
                <twbs:navbarLink action="table">Link 3</twbs:navbarLink>
            </twbs:navbarLinks>
        </twbs:navbarPullRight>
    </twbs:navbar>

    <g:each in="${NavStyle.values()}" var="style">
        <h2>${style}</h2>
        <h4>Normal</h4>
        <twbs:nav style="${style}">
            <twbs:navItem active="true">Item 1</twbs:navItem>
            <twbs:navItem disabled="true">Item 2</twbs:navItem>
            <twbs:navItem>Item 3</twbs:navItem>
            <twbs:navDropdownToggle>
                Dropdown toggle
                <twbs:dropdownMenu>
                    <twbs:dropdownItem>Hello!</twbs:dropdownItem>
                    <twbs:dropdownItem>Hello 2!</twbs:dropdownItem>
                    <twbs:dropdownItem>Hello 3!</twbs:dropdownItem>
                </twbs:dropdownMenu>
            </twbs:navDropdownToggle>
        </twbs:nav>
        <h4>Justified</h4>
        <twbs:nav style="${style}" justified="true">
            <twbs:navItem active="true">Item 1</twbs:navItem>
            <twbs:navItem disabled="true">Item 2</twbs:navItem>
            <twbs:navItem>Item 3</twbs:navItem>
            <twbs:navDropdownToggle>
                Dropdown toggle
                <twbs:dropdownMenu>
                    <twbs:dropdownItem>Hello!</twbs:dropdownItem>
                    <twbs:dropdownItem>Hello 2!</twbs:dropdownItem>
                    <twbs:dropdownItem>Hello 3!</twbs:dropdownItem>
                </twbs:dropdownMenu>
            </twbs:navDropdownToggle>
        </twbs:nav>
        <h4>Stacked</h4>
        <twbs:nav style="${style}" stacked="true">
            <twbs:navItem active="true">Item 1</twbs:navItem>
            <twbs:navItem disabled="true">Item 2</twbs:navItem>
            <twbs:navItem>Item 3</twbs:navItem>
            <twbs:navDropdownToggle>
                Dropdown toggle
                <twbs:dropdownMenu>
                    <twbs:dropdownItem>Hello!</twbs:dropdownItem>
                    <twbs:dropdownItem>Hello 2!</twbs:dropdownItem>
                    <twbs:dropdownItem>Hello 3!</twbs:dropdownItem>
                </twbs:dropdownMenu>
            </twbs:navDropdownToggle>
        </twbs:nav>
    </g:each>

</twbs:row>

</body>
</html>