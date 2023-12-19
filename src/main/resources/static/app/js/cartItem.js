(function($) {
	var storedCart = JSON.parse(sessionStorage.getItem("cart")) || [];
	var csrfToken = $("meta[name='_csrf']").attr("content");
	var csrfHeader = $("meta[name='_csrf_header']").attr("content");

	if (storedCart) {
		sendCartToBackend();
	}
	function sendCartToBackend() {
		$.ajax({
			url: "/api/v1/updateCart",
			type: "POST",
			contentType: "application/json",
			data: JSON.stringify(storedCart),
			dataType: 'json',
			beforeSend: function(xhr) {
				xhr.setRequestHeader(csrfHeader, csrfToken);
			},
			success: function(data) {
				console.log(data);

			}
		});
	}


})(jQuery);
