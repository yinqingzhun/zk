<!DOCTYPE html>
<html lang="en"  >
<head>
    <meta charset="utf-8">
    <title>demo</title>
    <link rel="stylesheet" th:href="@{/layui/css/layui.css}"/>
    <link rel="stylesheet" th:href="@{/css/print/normalize.min.css}">
    <style>
        body {
            padding: 100px;
        }

        .unit {
            position: absolute;
            font-size: 12px;
            color: #333;
            border: 1px solid #ccc;
            font-family: '宋体';

        }

        .box {
            width: 60px;
            height: 60px;
            border: 1px red dashed;
        }

        .selected {
            border: 1px solid red;
            background-color: #D6DFF7;
        }

        h3 {
            text-align: center;
            padding-bottom: 50px
        }

        form input[type=text] {
            width: 100px;
        }

        .layui-form-item .layui-input-inline {
            width: auto;
        }

        .layui-form-label {
            width: auto;
            display: none;
        }

        .select-box {
            position: absolute;
            width: 0px;
            height: 0px;
            font-size: 0px;
            margin: 0px;
            padding: 0px;
            border: 1px dashed #0099FF;
            background-color: #C3D5ED;
            z-index: 1000;
            filter: alpha(opacity:60);
            opacity: 0.6;
        }

    </style>
</head>
<body >
<#assign ages = {"Joe":23, "Fred":25} + {"Joe":30, "Julia":18}>
- Joe is ${ages.Joe}
- Fred is ${ages.Fred}
- Julia is ${ages.Julia}
<#macro greet name>
  <font size="+2">Hello ${name}!</font>
</#macro>
<@greet name="jack"></@greet>
<div id="foo">
    <form id="font" class="layui-form" action="#">
        <div class="layui-form-item">
            <div class="layui-inline">
                <label class="layui-form-label">颜色：</label>
                <div class="layui-input-inline">
                    <input type="text" name="color" lay-verify="required|colorHexValue" placeholder="请输入色值，如：#FFCC00"
                           autocomplete="off" style="width:100px;border:1px solid black;display:inline-block;"
                           class="layui-input jscolor {hash:true}">
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">字号：</label>
                    <div class="layui-input-inline">
                        <input type="hidden" name="size" lay-verify="required" autocomplete="off" class="layui-input">
                        <button class="layui-btn layui-btn-primary" name="size" data-type="large">字号 +</button>
                        <button class="layui-btn layui-btn-primary" name="size" data-type="small">字号 -</button>
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">字体：</label>
                    <div class="layui-input-inline">
                        <select name="family" lay-filter="family">
                            <option value="宋体">宋体</option>
                            <option value="黑体">黑体</option>
                            <option value="微软雅黑">微软雅黑</option>
                            <option value="隶书">隶书</option>
                            <option value="楷书">楷书</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-form-item">

        </div>
        <div class="layui-form-item">

        </div>
    </form>
</div>
<div id="container" style="position:relative;border:1px slategray solid;height: 330px;">
    <div class="unit">file1</div>
    <div class="unit" style="left:110px">file2</div>
    <div class="unit" style="left:220px">file3</div>
    <div class="unit" style="top:110px;">file4</div>
    <div class="unit" style="left:110px;top:110px;">file5</div>
    <div class="unit" style="left:220px;top:110px;">file6</div>

</div>
<div id="qrcode"></div>
<script th:src="@{/layui/layui.js}"></script>
<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
<script src="https://cdn.bootcss.com/jquery-tools/1.2.7/jquery.tools.min.js"></script>
<script th:src="@{/js/jscolor.js}"></script>
<script th:src="@{/js/drag.js}"></script>
<script th:src="@{/js/vendor/html5-3.6-respond-1.1.0.min.js}"></script>
<script th:src="@{/js/jquery.print.js}"></script>
<script th:src="@{/js/jquery.qrcode.min.js}"></script>
<script type="text/javascript">

    function setForm(ele) {
        var form = document.getElementById('form');
        var e = $(ele);
        form.color.value = e.css('color');
        form.family = e.css('font-family');
        form.size = e.css('font-size');
    }

    function sort() {
        var a = [];
        for (i = 0; i < arguments.length; i++)
            a = a.concat(arguments[i]);

        a.sort(function (a, b) {
            return a - b;
        });
        return a;
    }

    function between(x1, x2, v) {
        var x = sort(x1, x2);
        x1 = x[0];
        x2 = x[1];
        return v >= x1 && v <= x2;
    }

    function dragable(ele) {
        var drag = $(ele).myDrag({
            randomPosition: true,
            direction: 'all',
            handler: false,
            randomPosition: false
        });
        return drag;
    }

    function getOnlySelectedElement() {
        var selectedList = $('.selected');
        if (selectedList && selectedList.length == 1)
            return selectedList.eq(0);
        return null;
    }

    function generateQrCode(ele, text, width, height) {
        jQuery(ele).qrcode({
            render: "table", //也可以替换为table,canvas
            width: width,
            height: height,
            // foreground: "#C00",
            // background: "#333",
            text: text
        });
    }

    (function () {


        $('#foo').prepend('<a class="print-preview">Print this page</a>');
        $('a.print-preview').on('click', function () {

            var html = $('#container').get(0).outerHTML,
                out = '',
                len = 3;
            for (var i = 0; i < len; i++) {
                out += html;
                if (i < len - 1) {
                    out += '<div style="page-break-after:always;"></div>';
                }
            }

            $.print(out);
        });


        function cross(x1, x2, y1, y2) {
            if (Math.abs(x1 - x2) > Math.abs(y1 - y2))
                return between(x1, x2, y1) || between(x1, x2, y2);
            return between(y1, y2, x1) || between(y1, y2, x2);
        }

        function select(e) {

            if ($(e.target).is('.unit'))
                return;

            var parentContainer = $('#container');
            var fileNodes = $('#container>*'),
                boxList = [];
            for (var i = 0; i < fileNodes.length; i++) {
                if (fileNodes[i].className.indexOf("unit") != -1) {
                    fileNodes[i].className = fileNodes[i].className.replace('selected', '');
                    boxList.push(fileNodes[i]);
                }
            }
            var isSelect = true;
            var evt = window.event || arguments[0];
            var startX = (evt.x || evt.clientX);
            var startY = (evt.y || evt.clientY);
            var rectangleDiv = document.createElement("div");
            rectangleDiv.className = "select-box";
            rectangleDiv.id = "selectDiv";
            document.body.appendChild(rectangleDiv);
            rectangleDiv.style.left = startX + "px";
            rectangleDiv.style.top = startY + "px";
            var _x, _y;
            clearEventBubble(evt);

            function calSelect() {
                evt = window.event || arguments[0];
                if (isSelect) {
                    if (rectangleDiv.style.display == "none") {
                        rectangleDiv.style.display = "";
                    }
                    _x = (evt.x || evt.clientX);
                    _y = (evt.y || evt.clientY);
                    rectangleDiv.style.left = Math.min(_x, startX) + "px";
                    rectangleDiv.style.top = Math.min(_y, startY) + "px";
                    rectangleDiv.style.width = Math.abs(_x - startX) + "px";
                    rectangleDiv.style.height = Math.abs(_y - startY) + "px";
                    // ---------------- 关键算法 ---------------------
                    var rectangleLeft = rectangleDiv.offsetLeft,
                        rectangleTop = rectangleDiv.offsetTop,
                        rectangleWidth = rectangleDiv.offsetWidth,
                        rectangleHeight = rectangleDiv.offsetHeight,
                        rectangleRight = rectangleLeft + rectangleWidth,
                        rectangleBottom = rectangleTop + rectangleHeight;

                    var containerLeft = parentContainer.get(0).offsetLeft,
                        containerTop = parentContainer.get(0).offsetTop,
                        containerWidth = parentContainer.get(0).offsetWidth,
                        containerHeight = parentContainer.get(0).offsetHeight;

                    // $('#s1').text(rectangleLeft + ',' + rectangleTop + ',' + rectangleRight + ',' + rectangleBottom);
                    // $('#s2').text((boxList[0].offsetLeft + rectangleLeft) + ',' + (boxList[0].offsetTop + rectangleTop));

                    if (cross(containerLeft, containerLeft + containerWidth, rectangleLeft, rectangleRight) && cross(containerTop, containerTop + containerHeight, rectangleTop, rectangleBottom))
                        for (var i = 0; i < boxList.length; i++) {
                            var boxLeft = boxList[i].offsetLeft + containerLeft;
                            var boxTop = boxList[i].offsetTop + containerTop;

                            var boxRight = boxList[i].offsetWidth + boxLeft;
                            var boxBottom = boxList[i].offsetHeight + boxTop;
                            //if (boxRight > rectangleLeft && boxBottom > rectangleTop && boxLeft < rectangleLeft + rectangleWidth && boxTop < rectangleTop + rectangleHeight)
                            if (cross(boxLeft, boxRight, rectangleLeft, rectangleRight) && cross(boxTop, boxBottom, rectangleTop, rectangleBottom)) {
                                if (boxList[i].className.indexOf("selected") == -1) {
                                    boxList[i].className = boxList[i].className + " selected";
                                }
                            } else {
                                if (boxList[i].className.indexOf("selected") != -1) {
                                    boxList[i].className = boxList[i].className.replace('selected', '');
                                }
                            }
                        }
                }
                clearEventBubble(evt);
            }

            function stopSelect() {
                isSelect = false;
                if (rectangleDiv) {
                    document.body.removeChild(rectangleDiv);
                }
                boxList = null, _x = null, _y = null, rectangleDiv = null, startX = null, startY = null, evt = null;
            }

            $(document).on('mousemove', calSelect);

            $(document).on('mouseup', function (e) {
                calSelect(e);
                stopSelect(e);
            })


        }


        $('#container > *').each(function (index, ele) {
            dragable(ele);
        })

        $('#container').mousedown(select);

        $('button[name=align]').click(function (e) {
            align = e.target.dataset.align;
            var selectedBoxList = $('.selected');
            if (selectedBoxList.length == 0) return;
            var minLeft = sort(selectedBoxList.map(function (i, e) {
                return e.offsetLeft
            }).toArray())[0];

            var maxRight = sort(selectedBoxList.map(function (i, e) {
                return e.offsetLeft + e.offsetWidth
            }).toArray())[selectedBoxList.length - 1];

            if (align == 'left') {
                selectedBoxList.css({left: minLeft})
            } else if (align == 'right') {
                selectedBoxList.each(function (i, e) {
                    $(e).css('left', maxRight - e.offsetWidth);
                })

            } else if (align == 'middle') {
                var m = (minLeft + maxRight) / 2;

                selectedBoxList.each(function (i, e) {
                    $(e).css('left', m - e.offsetWidth / 2);
                })

            }
        });

        $('button[name=newImage]').click(function (e) {

            var box = document.createElement('div');
            box.className = 'unit';

            var img = document.createElement('img');
            img.src = '';


            $('#container').append(box.outerHTML);

        });

        $('button[name=newText]').click(function (e) {

            var box = document.createElement('div');
            box.className = 'unit';
            box.innerText = '新节点';
            $('#container').append(box.outerHTML);
            dragable($('#container>*:last'));

        });

        $('button[name=newBox]').click(function (e) {

            var box = document.createElement('div');
            box.className = 'unit box';
            generateQrCode(box,'test',100,100);
            $('#container').append(box.outerHTML);
            dragable($('#container>*:last'));
        });

        $('input[name=color]').change(function (e) {
            var selected = getOnlySelectedElement();
            if (selected) {
                selected.css('color', e.target.value);
            }
        });


    })();


    function ajdustSize(op) {
        var selected = getOnlySelectedElement();
        if (selected) {
            var oldSize = selected.css('font-size').replace('px', '');
            if (op == 'large') {
                oldSize++;
            } else if (op == 'small') {
                oldSize--;
            }
            selected.css('font-size', oldSize + 'px');
        }

    }

    layui.use(['form', 'layedit', 'laydate'], function () {
        var form = layui.form, layer = layui.layer;
        form.on('select(family)', function (data) {
            var selected = getOnlySelectedElement();
            if (selected) {
                selected.css('font-family', data.value);
            }
        });
        layui.$('.layui-form .layui-btn').on('click', function (e) {
            var type = $(this).data('type');
            if (type == 'large' || type == 'small') {
                ajdustSize(type);
            }

            e.preventDefault();
        });
    });

    function clearEventBubble(evt) {
        if (evt.stopPropagation) evt.stopPropagation(); else evt.cancelBubble = true;
        if (evt.preventDefault) evt.preventDefault(); else evt.returnValue = false;
    }


</script>
</body>
</html>