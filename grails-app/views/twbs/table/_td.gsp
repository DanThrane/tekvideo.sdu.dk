<%@ page import="dk.danthrane.TagLibUtils" %>
<td class="${context.baseName} ${clazz}" ${raw(TagLibUtils.expandAttributes(attrs))}>${raw(body())}</td>