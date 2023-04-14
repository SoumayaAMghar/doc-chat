function follow(check, userId, i){
	let url = "/follow/"+userId;
	if(check){
		fetch(url,{
			method: "POST"
	    }).then(function(res){
			return res.text();							
		}).then(function(res){
			if(res === "ok"){
				let follow_item_el = document.querySelector("#follow_item_"+i);
				follow_item_el.innerHTML = `<button onClick="follow(false, ${userId}, ${i})" class="following_btn">suivi(e)</button>`;
			}
		});
	}else{
		fetch(url,{
			method: "DELETE"
	    }).then(function(res){
			return res.text();							
		}).then(function(res){
			if(res === "ok"){
				let follow_item_el = document.querySelector("#follow_item_"+i);
				follow_item_el.innerHTML = `<button onClick="follow(true, ${userId}, ${i})" class="follow_btn">suivre</button>`;
			}
		});
	}
}