<%@ page import="dk.danthrane.twbs.Validation; dk.danthrane.TagLibUtils; dk.danthrane.twbs.FormTagLib" %>

<div class="form-group ${validationClass} ${sizeClass} ${clazz}">
    <g:render template="/twbs/form/defaultLabel" model="${pageScope.variables}" />

    <g:render template="/twbs/form/openHorizontalWrapper" model="${pageScope.variables}" />

    <g:if test="${hasAddons}">
        <div class="input-group">
            <g:ifContentAvailable key="addon-left">
                <g:selectContent key="addon-left" />
            </g:ifContentAvailable>

            <input name="${name}" type="${type}" class="form-control ${inputSizeClass}" id="${id}" type="${type}" value="${value}"
                ${raw(placeholder)} ${disabled} ${raw(TagLibUtils.expandAttributes(attrs))}>

            <g:ifContentAvailable key="addon-right">
                <g:selectContent key="addon-right" />
            </g:ifContentAvailable>
        </div>
    </g:if>
    <g:else>
        <input name="${name}" type="${type}" class="form-control ${inputSizeClass}" id="${id}" type="${type}" value="${value}"
            ${raw(placeholder)} ${disabled} ${raw(TagLibUtils.expandAttributes(attrs))}>
    </g:else>

    <g:render template="/twbs/form/closeHorizontalWrapper" model="${pageScope.variables}" />

    <g:if test="${validation != Validation.DEFAULT}">
        <twbs:icon icon="${validation.icon}" class="form-control-feedback" />
        <span class="sr-only">(${validation.name().toLowerCase()})</span>
    </g:if>

    <g:render template="/twbs/form/defaultHelp" model="${pageScope.variables}" />
</div>