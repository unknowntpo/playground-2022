<script>

    function getUrlParameter(name) {
        name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
        var regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
        var results = regex.exec(location.search);
        return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
    };

    var json = null;
    $(document).ready(function () {

        //已受理的，不用顯示 那個判決影音
        //在已受理這區，這不用show
        //控制案件公告位置
        var fid = getUrlParameter('fid');
        if (fid == '52') {
            $('#section_video').remove();
        }

        // 這頁不用show 宣示判決影音
        //http://localhost:5678/docdata.aspx?fid=5304&id=310013
        if (fid == '5478') {
            //$('#frame_declare_judgment_video').remove();
            $('#section_video').remove();
        }
        // 判決頁 fid=38 不show 案由下的案件公告 

        if (fid == '38') {
            $('#sectionNews3').remove();
        }
         //實體裁定頁 不show 案由下的案件公告
        if (fid == '39') {
            $('#sectionNews3').remove();
        }
        // 公告書狀之案件列表(已結案) 不show 案由下的案件公告
        if (fid == '5304') {
            $('#sectionNews3').remove();
        }

        // 程序裁定案件 不show 案由下的案件公告
        if (fid == '5511' || fid == '5502' || fid == '5501' || fid == '5498' || fid == '5499') {
            $('#sectionNews3').remove();
        }

     


        //bitty

        try {
            var jsonString = $('#jsonLabel').val();
            json = JSON.parse(jsonString);
        } catch (e) {
        }


        //bitty

        if (json.pub_news == '') {
            json.pub_news = '[]';
        }

        if (json.oral_debate_video == '') {
            json.oral_debate_video = '[]';
        }

        if (json.briefing_session_video == '') {
            json.briefing_session_video = '[]';
        }
        if (json.declare_judgment_video == '') {
            json.declare_judgment_video = '[]';
        }


        try {
            var openerWindowName = window.opener.name;
            // alert("Window name from opener:", openerWindowName);
        }
        catch (e) {
            initNews();

            initVideos();

            reBuildSections();
        }


        $('.page-slider-for').slick({
            slidesToShow: 1,
            slidesToScroll: 1,
            arrows: false,
            fade: true,
            asNavFor: '.page-slider-nav'
        });
        $('.page-slider-nav').slick({
            slidesToShow: 3,
            slidesToScroll: 1,
            asNavFor: '.page-slider-for',
            dots: false,
            centerMode: true,
            focusOnSelect: true,
            responsive: [{
                breakpoint: 601,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 1,
                    asNavFor: '.page-slider-for',
                    dots: false,
                    centerMode: true,
                    focusOnSelect: true
                }
            },
            {
                breakpoint: 454,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 1,
                    asNavFor: '.page-slider-for',
                    dots: false,
                    centerMode: false,
                    focusOnSelect: true
                }
            },
            {
                breakpoint: 361,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 1,
                    asNavFor: '.page-slider-for',
                    dots: false,
                    centerMode: false,
                    focusOnSelect: true
                }
            }
            ]
        });

        doSetSession('309577', '');





    });




    //bitty
    function initVideos() {


        json.declare_judgment_video = JSON.parse(json.declare_judgment_video)

        html = '';
        for (var i in json.declare_judgment_video) {
            var x = json.declare_judgment_video[i];

            html += `<li>
       <a title="${x.doc_title}" href="${x.doc_video_url}" target="_blank">
                  <i class="fas fa-link"></i>
                                    <p>${x.doc_title}</p>
                                        </a></li>`;

        }

        $('#frame_declare_judgment_video').html(html);

        if (html == '') {
            $('#section_video').remove();
        }

    }


    function initNews() {

        //抓案件公告2
        var html = '';

        try {
            var pub_news = JSON.parse(json.pub_news);

            for (var i in pub_news) {
                var x = pub_news[i];
                html += `<li><a title="${x.doc_title}" href="${x.doc_url}" target="_blank">
		<i class="fas fa-link"></i>
		<p>${x.doc_title}</p>
		</a></li>`;
            }

            $('#frameNews2').html(html);
            //alert('33333333333333333333');
        }
        catch (e) {
        }

        if (html == '') {
            //alert('44444444444444');
            $('#sectionNews2').remove();
        }
        //案件公告1 結束

        //抓案件公告3
        var html = '';

        try {
            var pub_news = JSON.parse(json.pub_news);

            for (var i in pub_news) {
                var x = pub_news[i];
                html += `<li><a title="${x.doc_title}" href="${x.doc_url}" target="_blank">
		<i class="fas fa-link"></i>
		<p>${x.doc_title}</p>
		</a></li>`;
            }

            $('#frameNews3').html(html);
        }
        catch (e) {
        }

        if (html == '') {
            $('#sectionNews3').remove();
        }
        //案件公告3 結束

        //抓案件公告4
        var html = '';

        try {
            var pub_news = JSON.parse(json.pub_news);

            for (var i in pub_news) {
                var x = pub_news[i];
                html += `<li><a title="${x.doc_title}" href="${x.doc_url}" target="_blank">
		<i class="fas fa-link"></i>
		<p>${x.doc_title}</p>
		</a></li>`;
            }

            $('#frameNews4').html(html);
        }
        catch (e) {
        }

        if (html == '') {
            $('#sectionNews4').remove();
        }
        //案件公告4 結束

        html = '';
        try {
            var oral_debate_video = JSON.parse(json.oral_debate_video);

            for (var i in oral_debate_video) {
                var x = oral_debate_video[i];
                html += `<li><a title="${x.doc_title}" href="${x.doc_video_url}" target="_blank">
		<i class="fas fa-link"></i>
		<p>${x.doc_title}</p>
		</a></li>`;
            }


            $('#frame_oral_debate_video').html(html);
        }
        catch (e) {
        }
        if (html == '') {
            $('#section_oral_debate_video').remove();
        }

        html = '';

        try {
            var briefing_session_video = JSON.parse(json.briefing_session_video);

            for (var i in briefing_session_video) {
                var x = briefing_session_video[i];
                html += `<li><a title="${x.doc_title}" href="${x.doc_video_url}" target="_blank">
		<i class="fas fa-link"></i>
		<p>${x.doc_title}</p>
		</a></li>`;
            }
            $('#frame_briefing_session_video').html(html);
        }
        catch (e) {
        }

        if (html == '') {
            $('#section_briefing_session_video').remove();
        }

        var html_oral_debate = '';
        var html_briefing_session = '';

        for (var i in json.atts) {
            var x = json.atts[i];

            // 1 是言詞辦論 2 是說明會
            if (x.doc_att_category == 1 && x.doc_att_group == 'meetAtt2') {

                html_oral_debate += `<li><a title="${x.doc_att_title}" href="/download/download.aspx?id=${x.doc_att_id}" target="_blank">
		<i class="fas fa-link"></i>
		<p>${x.doc_att_title}</p>
		</a></li>`;
            }

            if (x.doc_att_category == 2 && x.doc_att_group == 'meetAtt2') {

                html_briefing_session += `<li><a title="${x.doc_att_title}" href="/download/download.aspx?id=${x.doc_att_id}" target="_blank">
		<i class="fas fa-link"></i>
		<p>${x.doc_att_title}</p>
		</a></li>`;
            }

        }

        for (var i in json.attsVerity) {
            var x = json.attsVerity[i];

            // 1 是言詞辦論 2 是說明會
            if (x.doc_att_category == 1 && x.doc_att_group == 'meetAtt2') {

                html_oral_debate += `<li><a title="${x.doc_att_title}" href="/download/download.aspx?id=${x.doc_att_id}" target="_blank">
        <i class="fas fa-link"></i>
        <p>${x.doc_att_title}</p>
        </a></li>`;
            }

            if (x.doc_att_category == 2 && x.doc_att_group == 'meetAtt2') {

                html_briefing_session += `<li><a title="${x.doc_att_title}" href="/download/download.aspx?id=${x.doc_att_id}" target="_blank">
        <i class="fas fa-link"></i>
        <p>${x.doc_att_title}</p>
        </a></li>`;
            }

        }

        //console.log(html_briefing_session)

        //var url = document.location;
        //console.log(url);
        //alert(url)

        $('#frame_oral_debate').html(html_oral_debate);
        if (html_oral_debate == '') {
            $('#section_oral_debate').remove();
        }

        $('#frame_briefing_session').html(html_briefing_session);
        if (html_briefing_session == '') {
            $('#section_briefing_session').remove();
        }

    }

    function moveTo(id) {
        var offset = -65
        var target = $('#' + id);
        $('html, body').animate({
            scrollTop: target.offset().top + offset
        }, 1000);
    }

    function reBuildSections() {
        var li_cont = '';
        for (i = 0; i < $('div .title').length; i++) {
            if ($('div .title').eq(i).attr('data-bind') != undefined) {

                /*
                li_cont += '<li><a href="javascript:void(0);" data-type="sideindex" data-bind="' +
                    $('div .title').eq(i).attr('data-bind') + '">' + $('div .title').eq(i).html() + '</a></li>';

                    */

                var dataBind = $('div .title').eq(i).attr('data-bind');
                /*
                li_cont += '<li><a href="#' + dataBind + '" data-type="sideindex" data-bind="' +
                    dataBind + '">' + $('div .title').eq(i).html() + '</a></li>';
                    */

                var ddd = $('div .title').eq(i).attr('id');
                li_cont += '<li><a onclick="moveTo(\'' + ddd + '\')"  >' + $('div .title').eq(i).html() + '</a></li>';


                //  <li><a href="" #section20_2"" id=""lawFastLink20_2"" data-type=""sideindex"" data-bind=""t20_2"">併案</a></li >


                //console.log($('div .title').eq(i).attr('data-bind') + "=>" + $('div .title').eq(i).html());
            }
        }
        $('ul[class="list side-nav"]').html(li_cont);
    }


    function doSetSession(id, str) {
        $.ajax({
            type: "POST",
            url: "/Ajax/judsearch_history.aspx",
            data: { "id": encodeURIComponent(id), "str": encodeURIComponent(str) },
            dataType: "json",
            success: function (data) {
            }
        });
    }

</script>
    

