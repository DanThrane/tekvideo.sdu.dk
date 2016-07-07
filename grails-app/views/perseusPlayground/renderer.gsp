<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main_fluid">

    <title>Perseus Playground</title>
    <fa:require />
    <asset:stylesheet src="perseus/perseus.css" />
    <asset:stylesheet src="perseus/khan-site.css" />
    <asset:stylesheet src="perseus/khan-exercise.css" />
    <asset:stylesheet src="perseus/editor.css" />
    <asset:stylesheet src="perseus/perseus-admin-package/devices.min.css" />
    <style>
        .katex-html { display: none; }
        #workarea {
            margin-left: 0 !important;
        }
    </style>
</head>
<body>
<twbs:pageHeader><h3>Perseus Renderer</h3></twbs:pageHeader>

<twbs:column>
    <div id="perseus-container"></div>
</twbs:column>

<div class="player-container">
    <twbs:row>
        <twbs:column md="9">
            <div class="embed-responsive embed-responsive-16by9">
                <div class="wrapper" style="z-index: 20"></div>
                <div class="player"></div>
            </div>
        </twbs:column>
        <twbs:column md="3">
            <ul class="video-navigation">
            </ul>
        </twbs:column>
    </twbs:row>
</div>

<script>
    window.rootUrl = "${createLink(absolute:true, uri:'/')}";

    window.question = {
    "question": {
        "content": "# Integration med Perseus demo\n\nArtiklerne som bliver vist understøtter alle de samme funktioner som kommer med Perseus. Det vil sige vi kan indsætte opgaver som vi vil, for eksempel:\n\n[[☃ orderer 1]]\n\n[[☃ matrix 1]]\n\nDet er også muligt at vise interaktive videoer. De bruger samme editor og player som resten af siden gør (minus bugs, det er trodsalt bare en demo). På nuværende tidspunkt vil det få videoen til at starte i bunden af siden, dette er udelukkende for at teste det virker.\n\n[[☃ interactivevideo 1]]\n\nDet er blevet implementeret som et native \"widget\". Så vi kan bruge det i siden som hvilket som helst andet widget. Så vi kan indsætte et nyt widget efter det:\n\n[[☃ measurer 1]]\n\nEller vi kan indsætte endnu en video:\n\n[[☃ interactivevideo 2]]\n\n",
        "images": {},
        "widgets": {
            "orderer 1": {
                "type": "orderer",
                "alignment": "default",
                "static": false,
                "graded": true,
                "options": {
                    "options": [
                        {
                            "content": "$x$"
                        },
                        {
                            "content": "$y$"
                        }
                    ],
                    "correctOptions": [
                        {
                            "content": "$x$"
                        }
                    ],
                    "otherOptions": [
                        {
                            "content": "$y$"
                        }
                    ],
                    "height": "normal",
                    "layout": "horizontal"
                },
                "version": {
                    "major": 0,
                    "minor": 0
                }
            },
            "matrix 1": {
                "type": "matrix",
                "alignment": "default",
                "static": false,
                "graded": true,
                "options": {
                    "static": false,
                    "matrixBoardSize": [
                        3,
                        3
                    ],
                    "answers": [
                        [
                            1,
                            2,
                            3
                        ],
                        [
                            4,
                            5,
                            6
                        ],
                        [
                            7,
                            8,
                            9
                        ]
                    ],
                    "prefix": "1",
                    "suffix": "2",
                    "cursorPosition": [
                        0,
                        0
                    ]
                },
                "version": {
                    "major": 0,
                    "minor": 0
                }
            },
            "interactivevideo 1": {
                "type": "interactivevideo",
                "alignment": "block",
                "static": false,
                "graded": true,
                "options": {
                    "static": false,
                    "json": "{\"timelineJson\":\"[{\\\"title\\\":\\\"Test\\\",\\\"timecode\\\":0,\\\"questions\\\":[{\\\"title\\\":\\\"Test spørgsmål\\\",\\\"timecode\\\":0,\\\"fields\\\":[{\\\"name\\\":\\\"field-0\\\",\\\"topoffset\\\":83.32344213649851,\\\"leftoffset\\\":83.80316930775646,\\\"answer\\\":{\\\"type\\\":\\\"expression\\\",\\\"value\\\":\\\"1\\\"}}]}]},{\\\"title\\\":\\\"Test 2\\\",\\\"timecode\\\":69,\\\"questions\\\":[]},{\\\"title\\\":\\\"Test 3\\\",\\\"timecode\\\":119,\\\"questions\\\":[]}]\",\"youtubeId\":\"DXUAyRRkI6k\",\"videoType\":true}"
                },
                "version": {
                    "major": 0,
                    "minor": 0
                }
            },
            "measurer 1": {
                "type": "measurer",
                "alignment": "default",
                "static": false,
                "graded": true,
                "options": {
                    "static": false,
                    "box": [
                        480,
                        480
                    ],
                    "image": {},
                    "showProtractor": true,
                    "showRuler": false,
                    "rulerLabel": "",
                    "rulerTicks": 10,
                    "rulerPixels": 40,
                    "rulerLength": 10
                },
                "version": {
                    "major": 1,
                    "minor": 0
                }
            },
            "interactivevideo 2": {
                "type": "interactivevideo",
                "alignment": "block",
                "static": false,
                "graded": true,
                "options": {
                    "static": false,
                    "json": "{\"timelineJson\":\"[{\\\"title\\\":\\\"Test\\\",\\\"timecode\\\":0,\\\"questions\\\":[{\\\"title\\\":\\\"Test spørgsmål\\\",\\\"timecode\\\":0,\\\"fields\\\":[{\\\"name\\\":\\\"field-0\\\",\\\"topoffset\\\":83.32344213649851,\\\"leftoffset\\\":83.80316930775646,\\\"answer\\\":{\\\"type\\\":\\\"expression\\\",\\\"value\\\":\\\"1\\\"}}]}]},{\\\"title\\\":\\\"Test 2\\\",\\\"timecode\\\":69,\\\"questions\\\":[]},{\\\"title\\\":\\\"Test 3\\\",\\\"timecode\\\":119,\\\"questions\\\":[]}]\",\"youtubeId\":\"DXUAyRRkI6k\",\"videoType\":true}"
                },
                "version": {
                    "major": 0,
                    "minor": 0
                }
            }
        }
    },
    "answerArea": {
        "calculator": false,
        "chi2Table": false,
        "periodicTable": false,
        "tTable": false,
        "zTable": false
    },
    "itemDataVersion": {
        "major": 0,
        "minor": 1
    },
    "hints": [
        {
            "replace": false,
            "content": "Vi har også hints.",
            "images": {},
            "widgets": {}
        },
        {
            "replace": false,
            "content": "Som også understøtter alle de andre ting:\n\nheader 1 | header 2 | header 3\n- | - | -\ndata 1 | data 2 | data 3\ndata 4 | data 5 | data 6\ndata 7 | data 8 | data 9",
            "images": {},
            "widgets": {}
        }
    ]
};
</script>

<asset:javascript src="perseus/lib/babel-polyfills.min.js" />
<asset:javascript src="perseus/lib/underscore.js" />
<asset:javascript src="perseus/lib/react-with-addons.js" />
<asset:javascript src="perseus/lib/mathjax/2.1/MathJax.js?config=KAthJax-f3c5d145ec6d4e408f74f28e1aad49db&amp;delayStartupUntil=configured" />
<asset:javascript src="perseus/lib/katex/katex.js" />
<asset:javascript src="perseus/lib/mathquill/mathquill-basic.js" />
<asset:javascript src="perseus/lib/kas.js" />
<asset:javascript src="perseus/lib/i18n.js" />
<asset:javascript src="perseus/lib/jquery.qtip.js" />

<asset:javascript src="perseus/test-renderer.js" />

<script>
    var InteractiveVideoBridge = {};
    InteractiveVideoBridge.player = null;
    InteractiveVideoBridge.beginPlayback = function(data) {
        if (InteractiveVideoBridge.player !== null) InteractiveVideoBridge.player.destroy();
        InteractiveVideoBridge.player = new InteractiveVideoPlayer($(".player-container"));
        InteractiveVideoBridge.player.autoplay = false;

        var timeline = JSON.parse(data.timelineJson);
        InteractiveVideoBridge.player.startPlayer(data.youtubeId, data.videoType, timeline);
    };
</script>

</body>
</html>
