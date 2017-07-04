<%@ page import="dk.danthrane.TagLibUtils" %>
<tr class="${context.baseName} ${clazz}" ${raw(TagLibUtils.expandAttributes(attrs))}>${raw(body())}</tr>