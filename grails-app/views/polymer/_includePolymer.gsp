<script>
    // setup Polymer options
    window.Polymer = {lazyRegister: true, dom: 'shadow'};

    // load webcomponents polyfills
    (function() {
        if ('registerElement' in document
                && 'import' in document.createElement('link')
                && 'content' in document.createElement('template')) {
            // browser has web components
        } else {
            // polyfill web components
            var e = document.createElement('script');
            e.src = '/app/bower_components/webcomponentsjs/webcomponents-lite.min.js';
            document.head.appendChild(e);
        }
    })();

</script>