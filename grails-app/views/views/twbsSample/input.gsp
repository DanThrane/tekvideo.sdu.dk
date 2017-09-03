<%@ page import="dk.danthrane.twbs.InputSize; dk.danthrane.twbs.Icon; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.danthrane.twbs.NavBarPlacement; dk.danthrane.twbs.ContextualColor; dk.danthrane.twbs.Validation" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="twbsmain"/>
    <title>TWBS Demo - Input</title>
</head>

<body>
<twbs:row>
    <twbs:column cols="6">
        <twbs:input name="foo">
            <g:content key="addon-left">
                <twbs:inputGroupAddon><twbs:icon icon="${Icon.USD}" /></twbs:inputGroupAddon>
            </g:content>

            <g:content key="addon-right">
                <twbs:inputGroupAddon>.00</twbs:inputGroupAddon>
            </g:content>

            This is the help block
        </twbs:input>
        <twbs:input name="normal" />

        <twbs:input name="segmented" labelText="Segmented Buttons" >
            <g:content key="addon-right">
                <twbs:inputGroupButton>
                    <twbs:button>Action</twbs:button>
                    <twbs:button><twbs:icon icon="${Icon.OK_CIRCLE}" /></twbs:button>
                </twbs:inputGroupButton>
            </g:content>
            <g:content key="addon-left">
                <twbs:inputGroupAddon>
                    <twbs:checkbox name="checkbox" showLabel="false"/>
                </twbs:inputGroupAddon>
            </g:content>
        </twbs:input>

        <twbs:input name="segmented2" labelText="Segmented Buttons" >
            <g:content key="addon-left">
                <twbs:inputGroupAddon>
                    <twbs:radio name="radio" showLabel="false"/>
                </twbs:inputGroupAddon>
            </g:content>
            <g:content key="addon-right">
                <twbs:inputGroupAddon>
                    <twbs:radio name="radio" showLabel="false"/>
                </twbs:inputGroupAddon>
            </g:content>
        </twbs:input>

        <twbs:radio name="radio" />

        <h3>Inline form</h3>

        <twbs:form action="/test" class="my-form" inline="true">
            <twbs:input name="input2" />
        </twbs:form>

        <h3>Normal form</h3>

        <twbs:form action="/test" class="my-form">
            <twbs:input name="input3" />
        </twbs:form>

        <h3>Horizontal forms</h3>

        <twbs:form action="/test" class="my-form" horizontal="true">
            <twbs:input name="input1" />
        </twbs:form>

        <twbs:form horizontal="true">
            <twbs:input name="input4" showLabel="true" />
            <twbs:input name="input4" showLabel="false" size="${InputSize.LARGE}">
                <g:content key="addon-right">
                    <twbs:inputGroupAddon>
                        <twbs:radio name="radio" showLabel="false"/>
                    </twbs:inputGroupAddon>
                </g:content>
                <g:content key="addon-left">
                    <twbs:inputGroupAddon>
                        <twbs:radio name="radio" showLabel="false"/>
                    </twbs:inputGroupAddon>
                </g:content>
                I don't have a label
            </twbs:input>
            <twbs:input name="input4" showLabel="true" size="${InputSize.SMALL}">
                <g:content key="addon-right">
                    <twbs:inputGroupAddon>
                        <twbs:radio name="radio" showLabel="false"/>
                    </twbs:inputGroupAddon>
                </g:content>
                <g:content key="addon-left">
                    <twbs:inputGroupAddon>
                        <twbs:radio name="radio" showLabel="false"/>
                    </twbs:inputGroupAddon>
                </g:content>
                I do have a label
            </twbs:input>
            <twbs:select name="test-select" list="${[1,2,3]}" size="${InputSize.LARGE}">
                This is help text
            </twbs:select>
            <twbs:select name="test-select" list="${[1,2,3]}" showLabel="false">
                This is help text
            </twbs:select>
            <twbs:textArea name="foo2" validation="${Validation.SUCCESS}" size="${InputSize.LARGE}" rows="4">
                This is also help
            </twbs:textArea>
            <twbs:textArea name="foo3" showLabel="false" rows="4">
                This is help
            </twbs:textArea>
            <twbs:checkbox name="input" />
            <twbs:checkbox name="input" showLabel="false" />
            <twbs:radio name="input" />
            <twbs:radio name="input" showLabel="false" />
        </twbs:form>

    </twbs:column>
</twbs:row>
</body>
</html>