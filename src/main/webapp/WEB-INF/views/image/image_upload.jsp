<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Costagram</title>
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
	rel="stylesheet">
<link rel="shortcut icon" href="/images/favicon.ico">
<link rel="stylesheet" href="/css/styles.css">
<link rel="stylesheet" href="/css/image-upload.css">
</head>
<body>

	<%@ include file="../include/nav.jsp" %>

	<div class="container">
		<div class="box">
			<div class="bigbox">
				<div>
					<img src="/images/loginLogo.png" alt="x" class="image">
				</div>
				<br />

				<div class="text">
					<hr class="hr" />
					envoyer la photo
					<hr class="hr" />
				</div>
				<!-- 섬네일은 아래 주소 참고 -->
				<!-- https://medium.com/@asadise/create-thumbnail-for-an-image-in-spring-framework-49776c873ea1 -->
				<form action="/image/uploadProc" method="post" enctype="multipart/form-data">
					<table class="table">
						<tr>
							<td><input id="input_img" type="file" name="file" placeholder="fichier photo"></td>
						</tr>
						<tr>
							<td>
								<div class="img_wrap">
									<img id="img" />
								</div>
							</td>
						</tr>
						<tr>
							<td><input type="text" name="caption" placeholder="présentation des images"></td>
						</tr>
						<tr>
							<td><input type="text" name="location" placeholder="emplacement"></td>
						</tr>
						<tr>
							<td><input type="text" name="tags" placeholder="#tag"></td>
						</tr>
					</table>
					<input type="submit" value="télécharger">
				</form>

			</div>
			<div class="smallbox">
				<div class=text>
					<a href="javascript:window.history.back()" class="under1">retourner</a>
				</div>
			</div>
		</div>
	</div>

	<%@ include file="../include/footer.jsp" %>
	<script>
		var sel_file;
		$(document).ready(function() {
			$('#input_img').on("change", handleImgFileSelect);
		});
		function handleImgFileSelect(e) {
			var files = e.target.files;
			var filesArr = Array.prototype.slice.call(files);
			filesArr.forEach(function(f) {
				if (!f.type.match("image.*")) {
					alert("L'extension ne peut être qu'une extension d'image.");
					return;
				}
				sel_file = f;
				var reader = new FileReader();
				reader.onload = function(e) {
					$('#img').attr('src', e.target.result);
				}
				reader.readAsDataURL(f);
			});
		}
	</script>
</body>
</html>