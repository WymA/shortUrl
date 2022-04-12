<!DOCTYPE html>
<!--suppress ALL -->
<html>
	<head>
		<meta charset="utf-8" />
		<title>shortUrl</title>
		<link rel="stylesheet" href="css/index.css">
        <script src="https://cdn.bootcss.com/jquery/2.2.3/jquery.min.js"></script>
	</head>
	<body>
		<div class="container">
			<h2>生成短网址/短链接</h2>

				<p>请输入长网址</p>
				<input id="longUrl" type="url" required value="" />
				
				<p>自定义（可不填）</p>
				<input id="customUrl" type="url" value="" />

				<button type="button" class="submit" onclick="submit()">提交</button>

			<div id="result" style="display: none;" >

			</div>
		</div>
		
	</body>
	<script type="text/javascript">
		$(function () {

        })

        function submit() {
            var longUrl = $("#longUrl").val();
            var customUrl = $("#customUrl").val();
			if (longUrl == undefined || longUrl == ""){
		    	alert("请输入要转换的地址");
		    	return false;
			}
            $.ajax({
                url:"/short/get",
                data:{orgurl:longUrl,customCode:customUrl},
                type:"post",
                success: function (result) {
					var shortUrl = result.data.url_short;
					//alert(shortUrl);
					var flage = result.data.result;
					if (flage){
                        var val = "生成结果：<a href=javascript:void(0); target=_blank onclick=\"openUrl('"+shortUrl+"')\" >"+shortUrl+"</a>";
                        $("#result").html(val);
                        $("#result").show();
					}else {
					    alert(result.data);
					}
                }
            });
        }

		function openUrl(url){
            if (url.indexOf("http") != 0) {
                window.open("http://" + url);
            } else {
                window.open(url);
            }
		}


	</script>
</html>
