<!DOCTYPE html>
<html>
<head>
    <fa:require />
    <asset:stylesheet src="perseus/perseus.css" />
    <asset:stylesheet src="perseus/khan-site.css" />
    <asset:stylesheet src="perseus/khan-exercise.css" />
    <asset:stylesheet src="perseus/editor.css" />
    <asset:stylesheet src="perseus/perseus-admin-package/devices.min.css" />
</head>
<body>
Woooo
<div id="content-container">
</div>
<!-- put an empty div here so the margin on the perseus editor has something
    to "push against" (without the div, the padding goes off the page, and the
    add hint button ends up touching the bottom of the page). -->
<div class="clear"></div>

<asset:javascript src="perseus/lib/babel-polyfills.min.js" />
<asset:javascript src="perseus/lib/jquery.js" />
<asset:javascript src="perseus/lib/underscore.js" />
<asset:javascript src="perseus/lib/react-with-addons.js" />
<asset:javascript src="perseus/lib/mathjax/2.1/MathJax.js?config=KAthJax-f3c5d145ec6d4e408f74f28e1aad49db&amp;delayStartupUntil=configured" />
<asset:javascript src="perseus/lib/katex/katex.js" />
<asset:javascript src="perseus/lib/mathquill/mathquill-basic.js" />
<asset:javascript src="perseus/lib/kas.js" />
<asset:javascript src="perseus/lib/i18n.js" />
<asset:javascript src="perseus/lib/jquery.qtip.js" />

<asset:javascript src="perseus/frame-perseus.js" />
</body>
</html>