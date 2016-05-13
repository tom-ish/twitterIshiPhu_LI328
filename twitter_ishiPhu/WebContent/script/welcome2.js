/* voir function main() du TD7 plus bas
function main() {
	environnement = new Object();
	environnement.users = [];
	environnement.actif = new User(1, "bidule", true);
	var json_txt = envoiCommentaire();
	RechercheCommentaire.traiteReponseJSON(json_txt);
}
*/

function main(){
	console.log("main() execute");
	logoMenuVisible = false;
	isWelcomePage = true;
	environnement = new Object();
	environnement.users = [];
	environnement.commentaires = [];
	showHideLogZone();
	// Recuperer le login de User et l'ajouter dans l'environnement
	$("#logForm").submit(function(){
		console.log("login click...");
		connexion(loginForm);
	});
	
	$("#logOut_zone").click(function(){
		console.log("disconnect...");
		disconnect();
	});

	$("#logo").click(function(){
		if(isWelcomePage == false)
			showHideLogoMenu();
	});
	
	$("#searchButton").click(function(){
		search();
	});
	
	$("#signUpMenu").click(function(){
		createUser(form_iscription);
	})
	
}

function createUser(formulaire){
	login = formulaire.username.value;
	pwd = formulaire.password.value;
	pwd2 = formulaire.password2.value;
	mail = formulaire.email.value;
	firstName = formulaire.firstName.value;
	lastName = formulaire.lastName.value;
	birthDate = formulaire.birthDate.value;
	//profilePicture = formulaire.profilePicture.value;
	
	var ok = verif_form(login,pwd);
	if(pwd != pwd2) ok = !ok;
	
	if(ok){
		signUp(login,pwd,pwd2,mail,firstName,lastName,birthDate,undefined);
		console.log("signUp OKEEY");
	}
}	
	
function signUp(login, pwd, pwd2, mail, firstname, lastname, birthdate, profilePicture){

	console.log(login);
	console.log(pwd);
	console.log(pwd2);
	console.log(firstname);
	console.log(lastname);
	console.log(birthdate);
	console.log(profilePicture);
	
	if(firstname == undefined) firstname = null;
	if(lastname == undefined) lastname = null;
	if(birthdate == undefined) birthdate == null;
	if(profilePicture == undefined) profilePicture == null;
	
	
	$.ajax ({
		type: "POST",
		url: "createUser",
		contentType: false, // obligatoire pour de l'upload
        processData: false, // obligatoire pour de l'upload
		data: "username=" + login + "&password=" + pwd + "&password2=" + pwd2 + "&email=" + mail + "&firstName=" + firstname + "&lastName=" + lastname + "&birthDate=" + birthdate,
		dataType: "json",
		success: function(json){
			if(json.createNewUser == "success!"){
				console.log("signUp success!");
			}
			else{
				console.log("signUp failed!");
			}
		},
		error: function(jqXHR , textStatus , errorThrown ){
			console.log(textStatus);
			console.log(jqXHR);
			alert("Erreur Ajax: SignUp is not working.\n" + textStatus + " " + errorThrown);
		}
	});
}
/*
function main(id,login,session_key){
	environnement = new Object();
	environnement.users = [];
	showHideLogZone();
	if((id != undefined) && (login != undefined) && (session_key != undefined)){
		environnement.key = session_key;
		environnement.actif = new User(id, login);
		gererDivConnexion(session_key);
	}
	
	search();
	
	$("#logOut_zone").click(function(){
		console.log("disconnect...");
		disconnect();
	});

	$("#logo").click(function(){
		if(isWelcomePage == false)
			showHideLogoMenu();
	});
}
*/
function connexion(formulaire) {
	login = formulaire.username.value;
	pwd = formulaire.password.value;
	var ok = verif_form(login,pwd);
	if(ok){
		connect(login,pwd);
		console.log("connect OKEEY");
	}
}

function verif_form(login,pwd){
	console.log("verif_form execute");
	if(login.length == 0){
		// alert("login obligatoire"); // ouvre une fenetre
		func_error("Login is missing!");
		return false;
	}
	if(pwd.length == 0){
		// alert("Password obligatoire");
		func_error("Password is missing!")
		return false;
	}
	if(pwd.length < 6){
		// alert("Password is too small");
		func_error("Password is too small!")
		return false;
	}
	return true;
}

function connect(login,pwd){
	$.ajax ({
		type: "POST",
		url: "login",
		data: "username=" + login + "&password=" + pwd,
		dataType: "json",
		success: function(rep){
					if(rep.login == "success!"){	
						console.log(rep);
						environnement.key = rep.session_key;
						console.log(environnement.key);
						alert("AJAX function connect: " + rep);

						console.log("login de: " + login + ", session_key =" + environnement.key);
						environnement.actif = new User(environnement.key, login);
						console.log("Utilisateur actif: ")
						console.log(environnement.actif);
						gererDivConnexion(environnement.key);	
						console.log("login de " + login + " done...");
						isWelcomePage = false;
						
						showAllTweet(environnement.key);
						
						/*	JSP
						 *  window.location.href = "main.jsp?id=" + rep.id + "&login=" + rep.login + "&session_key=" + rep.key;
						 */
					}
					else {
						func_error("Problème connexion BD: " + rep);
					}
				},
		error: function(jqXHR , textStatus , errorThrown ){
					console.log(textStatus);
					alert(jqXHR + " " + textStatus + " " + errorThrown);
				}
	});
}

function showAllTweet(key){
	$.ajax ({
		type: "GET",
		url: "search",
		data: "session_key=" + key + "&query=&friendID=0",
		dataType: "text",
		success: function(rep){
			console.log("rep:");
			console.log(rep);
			var json_results = JSON.parse(rep, RechercheCommentaire.revival);
			if(json_results != undefined){

				$("#infoLog").append("<br/><h2 id = \"title\">Recently posted Tweet<br/></h2>");
				$("#infoLog").append("<div id = \"tweetZone\"></div>");

				/*
				console.log("parsing OK...");
				var tmp_html = "";
				
				$.each(json_results.resultats, function(){
					$.each(this, function(key,value){
						console.log(key + '=' + value);
						tmp_html += value;
					});
					
					console.log(tmp_html);
					
					$.each(tmp_html, function(k,v){
						if(k == "tweet") console.log(v);
					});
				});*/
				console.log("parsing OK...");

				console.log(environnement.commentaires);
				for(var i = 0; i < environnement.commentaires.length; i++){
					console.log(environnement.commentaires[i].texte);
					$("#tweetZone").prepend("<br/>" +
							"<span class = \"author\">Posted by " + environnement.commentaires[i].auteur + "<br/></span>" +
							"<div class = \"tweet\">" + environnement.commentaires[i].texte + "<br/><span class = \"date\">" + environnement.commentaires[i].date + "</span></div>");
				
				}
				
					console.log(json_results);
					
				
				
				$("#title").css("font-size","1.5em");
				$("#title").css("color","grey");
				
				tweetZoneCSS();
				/*
				console.log(rep.tweet);
				console.log("rep.tweet.length " + rep.tweet.length);
				console.log("rep.tweet[0].length " + rep.tweet[0].length);*/
			}
			else {
				func_error("Problème connexion BD: " + rep);
			}
		},
		error: function(jqXHR , textStatus , errorThrown ){
					console.log(textStatus);
					alert(jqXHR + " " + textStatus + " " + errorThrown);
			}
	});
}

function setPostZone(key){
	
	$("#infoLog").append("<br/><div id=\"postZone\"></div>")
	$("#postZone").append("<form id=\"postTweet\" action = \"javascript:(function(){})()\" method = \"get\"></form>");
	$("#postTweet").append("<textarea id = \"tweetPost\" type=\"text\" name = \"tweetQuery\" rows=\"6\" cols=\"20\" placeholder = \"Post your tweet\" onblur = \"this.style.backgroundColor =\'#FFFFFF\';\" on focus=\"this.style.backgroundColor = \'#EAFFFF\';\"></textarea>");
	$("#postTweet").append("<br/><br/><button id = \"postButton\" class = \"needed\">Post</button>");
	
	$("#tweetPost").css("width","50%");
	$("#tweetPost").css("height","20%");
	
	
	$("#postButton").click(function(){
		tweet = $("#tweetPost").val();
		console.log("tweet vaut "+tweet);
		$.ajax ({
			type: "GET",
			url: "tweet",
			data: "session_key=" + key + "&tweet=" + tweet,
			dataType: "json",
			success: function(rep){
						if(rep.postTweet == "success!"){
							console.log(rep.date);
							$("#tweetZone").prepend("<br/>" +
									"<span class = \"author\">Posted by " + rep.author.login + "<br/></span>" +
									"<div class = \"tweet\">" + rep.tweet + "<br/><span class = \"date\">" + rep.date + "</span></div>");
							tweetZoneCSS();
							console.log(rep);
						}
						else {
							func_error("Problème connexion BD: " + rep);
						}
					},
			error: function(jqXHR , textStatus , errorThrown ){
						console.log(textStatus);
						alert(jqXHR + " " + textStatus + " " + errorThrown);
					}
		});
	});
}

function tweetZoneCSS(){
	$(".tweet").css("color", "black");
	$(".tweet").css("font-size", "1.2em");
	$(".tweet").css("margin-left", "5%");
	$(".tweet").css("margin-right", "5%");
	$(".tweet").css("padding", "2%");
	
	$(".tweet").css("border","thin");
	$(".tweet").css("border-style","groove");
	$(".tweet").css("border-color","#ffffff");

	$(".author").css("border","thin");
	$(".author").css("border-style","groove");
	$(".author").css("border-color","#ffffff");
	$(".author").css("padding-left","5%");
	$(".author").css("padding-right","5%");
	$(".author").css("text-align","center");
	$(".author").css("margin-left", "5%");
	$(".author").css("margin-right", "5%");
	
	$(".date").css("font-size","0.65em");
}

function showHideFriendsMenu(){
	console.log("showHideFriendsMenu");
	$("#friendsButton").after("<span id = \"friendsList\">Friends List</span>");

	$("#friendsList").css("top","40px");
	$("#friendsList").css("left","1px");
	
	$("#friendsList").css("border");
	$("#friendsList").css("border","thin");
	$("#friendsList").css("border-style","groove");
	$("#friendsList").css("border-color","#ffffff");
	$("#friendsList").css("display","none");

	if(!friendsListIsVisible){
		$("#friendsList").show();
		friendsListIsVisible = true;
	}
	else{
		$("#friendsList").hide();
		friendsListIsVisible = false;
	}
	console.log($("#friendsButton"));
}

/* User constructor */
function User(session_key /*id*/, login, contact){
	console.log("User constructor");
	this.sessionKey = session_key; // this.id = id;
	this.login = login;
	this.contact = false;
	if(contact != undefined){
		this.contact = contact;
	}
	if(environnement == undefined){
		environnement = new Object();
	}
	if(environnement.users == undefined){
		environnement.users = [];
	}
	environnement.users[this.id] = this; /* insertion */

	console.log("User created");
}

User.prototype.modifStatus = function(){
	this.contact =! this.contact;
}


/* ---------------------- TD 7: Javascript - jQuery ------------------------------- */
function func_error(msg){
	var msg_box = "<div id = \"msg_err_connexion\">" + msg + "<\div>"
	var old_msg = $("#msg_err_connexion");
	if(old_msg.length == 0){
		$("#log_zone").prepend(msg_box);
	}
	else{
		old_msg.replaceWith(msg_box);
	}
	$("#msg_err_connexion").css(
	{
		"padding-top": "0px",
		"padding-bottom": "0px",
		"color": "red",
		"word-wrap": "break-word"
	});
}

// Suppose que la variable environnement existe (cree dans main)
function gererDivConnexion(session_key){
	var user = environnement.actif;
	if((user != undefined) && (user.login != "")){ // il y a un user connecte
		setTimelineCSS(user);
		setPostZone(environnement.key);
		//$("#disconnect").css("visibility", "visible");
	}
	// si on click sur deconnexion
	else {
		clickOnLogOut();
		/*
		$("#conenrlog").html(
			"<a href = \"connexion.html\" id = \"connexion_link\">Connexion</a>" +
			"<a href = \"enregistrement.html\" id = \"enegistrement\">Enregistrement</a>");
		$("#disconnect").css("visibility", "hidden");*/
	}
}

function setTimelineCSS(user){
	hideWelcome_txt();
	$("#timeline").html("<div id = \"infoLog\"></div>");
	$("#infoLog").append("<br/><h2 id = \"welcome\">Welcome " + user.login + "!<br/></h2>");
	
	$("#timeline").css("top", "0");
	$("#timeline").css("height", "100%");
	$("#timeline").css("background-color", "#D4DCE6");
	$("#timeline").css("border", "thin");
	$("#timeline").css("border-color", "#ffffff");
	$("#timeline").css("border-style", "solid");
	$("#timeline").css("box-shadow", "-1px 2px 5px, -1px 2px 5px inset");
	$("#timeline").css("opacity", "1");
	
	$("#infoLog").css("position", "absolute");
	$("#infoLog").css("width", "60%");
	$("#infoLog").css("margin-bottom", "30px");
	$("#infoLog").css("margin-top", "40px");
	$("#infoLog").css("padding-bottom", "20px");
	
	$("#infoLog").css("color", "090404");
	$("#infoLog").css("font-family", "\"Helvetica Neue\",Helvetica,Arial,sans-serif");
	$("#infoLog").css("text-align","center");
	$("#infoLog").css("border","thin");
	$("#infoLog").css("border-style","groove");
	$("#infoLog").css("border-color","#ffffff");
	$("#infoLog").css("opacity", "1");
	$("#infoLog").css("background-color", "#D4DCE6");
	
	$("#welcome").css("top", "100px");
	$("#welcome").css("right", "100px");
	$("#welcome").css("font-size", "2.5em");
	$("#welcome").css("color", "#090404");

	console.log($("#timeline").html());
}


function disconnect(){
	console.log("Deconnexion");
	var user = environnement.actif;
	if(environnement.key != undefined){
		$.ajax ({
			type: "GET",
			url: "logout",
			data: "session_key=" + user.sessionKey,
			dataType: "text",
			success: function(rep){
						if((rep.error == undefined) || (rep.error == 0)){					
							message = JSON.parse(rep);
							if(message.logout == "success!"){
								environnement.actif = undefined;
								gererDivConnexion(environnement.key);
								logoMenuVisible = false;
								isWelcomePage = true;
								alert("AJAX function disconnect: " + rep);
								hideLogoMenu();
							}
							else
								alert("AJAX problem disconnect: " + rep);
						}
						else {
							alert("Problem disconnect" + rep);
						}
					},
			error: function(jqXHR , textStatus , errorThrown ){
				console.log(textStatus);
				alert(jqXHR + " " + textStatus + " " + errorThrown);
			}
		});
	}
}

function ajoutsup_contact(id){
	var user = environnement.users[id];
	var url = "addFriend";
	if(user.contact)
		url = "removeFriend";
	
	$.ajax({
		type: "GET",
		url: url,
		data: "session_key=" + environnement.key + "&friendID=" + id,
		dataType: "json",
		success: function(response){
			traiteRespAjoutSupContact(response,id);
		},
		error: function(jqXHR , textStatus , errorThrown ){
			console.log(textStatus);
			alert(jqXHR + " " + textStatus + " " + errorThrown);
		}
	})
	
	// apres = ajouter appel serveur & erreur(s) specifique(s)
}

function traiteRespAjoutSupContact(response, id){
	if((response.erreur == undefined) || (reponse.erreur == 0)){
		if((environnement != undefined) && (environnement.users != users != undefined) && (environnement[id] != undefined)){
			var user = environnement.users[id];
			user.modifStatus();
			var comments = environnement.Recherche.resultats;
			for(var i in comments){
				if(comments[i].auteur_id == id){
					var com = comments[i];
					var id_com = com.id;
					$("#commentaire_" + id_com).replaceWith(com.getHtml);
				}
			}
		}
		else alert();
	}
}

/* ---------------------- TD 7: Javascript - jQuery ------------------------------- */

/* Comment constructor */
function Comment(id, auteur, texte, date, score) {
	console.log("new Comment constructor");
	this.id = id;
	this.auteur = auteur;
	this.texte = texte;
	this.date = date;
	this.score = score;
	console.log("new Comment done...");
}

Comment.prototype.getHtml = function(){
	var s = "<div id = \"Commentaire_" + this.id + "\"class = \"comments\">\n";
	s+= "<div class = \"text_comment\">" + this.texte + "</div>\n";
	s+= "<div class = \"infos_comment\">\n";
	s+= "<span>Poste par " + this.auteur.login + "le " + this.date.toDateString() + "</span>\n";
	// Tester si environnement defini et auteur != mec connecte:
	if((environnement != undefined) && (environnement.actif != undefined) && (this.auteur.id != environnement.actif.id)) {
		if(this.auteur.contact){ // User amis
			s+= "<img class = \"img_" + this.auteur.id + " src = \"Image/img-name.jpg\" title = \"retirer des contacts\" alt = \"retirer des contacts\" onclick = \"Javascript:ajoutsup_contact(" + this.auteur.id + ")\"/>\n";
		}
		else {
			s+= "<img class = \"img_" + this.auteur.id + " src = \"Image/img-name.jpg\" title = \"ajouter des contacts\" alt = \"ajouter des contacts\" onclick = \"Javascript:ajoutsup_contact(" + this.auteur.id + ")\"/>\n";
		}
	}
	s+= "</div>\n</div>";
	return (s);
}

/* Affichage Commentaire */
function search(){
	/*
	 * a = test? 1:2
	 * 	<=>
	 * if(test) a = 1;
	 * else a = 2;
	 */
	var friends = (($("#box_friends").get(0).checked)? 1: 0);
	var query = $("#query").val();
	if(environnement.key == undefined) environnement.key = null;

	console.log("Query is : " + query);
	console.log("session_key = " + environnement.key);
	
	$.ajax ({
		type: "GET",
		url: "search",
		data: "session_key=" + environnement.key + "&query=" + query + "&friendID=" + friends,
		dataType: "text",
		success: RechercheCommentaire.traiteReponseJSON,
		error: function(jqXHR , textStatus , errorThrown ){
			console.log(textStatus);
			alert("Erreur Ajax: Search is not working.\n" + textStatus + " " + errorThrown);
		}
	});
}

RechercheCommentaire.traiteReponseJSON = function(json_txt) {
	var old_users = environnement.users;	// ???
	environnement.users = [];	// ???
	console.log("TraiteReponseJSON...");
	json_results = JSON.parse(json_txt, RechercheCommentaire.revival);
	console.log("json_txt = " + json_txt);
	if(json_results == undefined) console.log("json_results undefined");
	else console.log(json_results);
	console.log("json_results.contact_only = " + json_results.contact_only);
	console.log("json_results.results = " + json_results.results);
	if(json_results.erreur == undefined){
		/*if(json_results.resultsSize > 0){
			var commentHtml = "";
			$.each(json_results, function( key value){
				var comment = new Comment(value._id, value.author_login, value.tweet, value.date, 0);
				commentHtml += comment.getHtml() + "\n";
			});	
		}
		
		
		// Insertion HTML contenant les reponses JSON
		var s = "<div id = \"Commentaire_" + this.id + "\"class = \"comments\">\n";
		s+= "<div class = \"text_comment\">" + this.texte + "</div>\n";
		s+= "<div class = \"infos_comment\">\n";
		s+= "<span>Poste par " + this.auteur.login + "le " + this.date.toDateString() + "</span>\n";
		// Tester si environnement defini et auteur != mec connecte:
		if((environnement != undefined) && (environnement.actif != undefined) && (this.auteur.id != environnement.actif.id)) {
			if(this.auteur.contact){ // User amis
				s+= "<img class = \"img_" + this.auteur.id + " src = \"Image/img-name.jpg\" title = \"retirer des contacts\" alt = \"retirer des contacts\" onclick = \"Javascript:ajoutsup_contact(" + this.auteur.id + ")\"/>\n";
			}
			else {
				s+= "<img class = \"img_" + this.auteur.id + " src = \"Image/img-name.jpg\" title = \"ajouter des contacts\" alt = \"ajouter des contacts\" onclick = \"Javascript:ajoutsup_contact(" + this.auteur.id + ")\"/>\n";
			}
		}
		s+= "</div>\n</div>";
		*/
		console.log(json_results.getHtml());
		alert(json_results.getHtml());
	}
	else {
		environnement.users = old_users;	// ???
		alert(json_results.erreur);
	}
}

RechercheCommentaire.traiteResponseJSON = function(json_txt){
	var old_users = environnement.users;	// ???
	environnement.users = [];	// ???
	console.log("ici");
	console.log("json_txt.results = " + json_txt.results);
	var obj = JSON.parse(json_txt, RechercheCommentaire.revival);
	console.log(obj);
	if(obj.erreur == undefined){
		console.log("-------");
		console.log(obj.getHtml());
		$("#main").html(obj.getHtml());
	}
	else	// alert probleme serveur ou BD
		environnement.users = old_users;	// ???
}


function isNumber(s){
	return ! isNaN (s-0);
}


RechercheCommentaire.revival = function (key, value){
	if(key.length == 0){
		console.log("length == 0");
		var r;
		if((value.erreur == undefined) || (value.erreur == 0)){
			console.log("RechercheCommentaire(...) ...");
			console.log(value.results);
			console.log(value.query);
			console.log(value.contact_only);
			console.log(value.author);
			console.log(value.author.login);
			console.log(value.date);
			console.log("key: " + key + " | key.length : " + key.length);
			r = new RechercheCommentaire(environnement.commentaires, value.query, value.contact_only, value.author.login, value.date);
			console.log("RechercheCommentaire(...) done");
			console.log(r);
		}
		else {
			r = new Object();
			r.erreur = value.erreur;
		}
		return (r);
	}
	else if(isNumber(key)){
		console.log("number");
		console.log(value);
		console.log("key: " + key + " | key.length : " + key.length);
		var line = JSON.parse(value, function(k, v){
			var u
			if(k == "author"){
				console.log("creating new User...");
				u = new User(v.session_key, v.login, 0);
				console.log("new User created");
				console.log(u);
				return u;
			}
			else if(k.length == 0){
				var c = new Comment(key, v.author.login, v.tweet, v.date, 0);
				environnement.commentaires[environnement.commentaires.length] = c;
				console.log(c);
				return c;
			}
			else{
				console.log("k = ");
				console.log(k);
				console.log("v = ");
				console.log(v);
				return v;
			}
		});
		return value;
	}
	else if(key == "author"){
		var u;
		if((environnement != undefined) && (environnement.users != undefined) && (environnement.users[value.session_key] != undefined)){
			console.log("environnement.user != undefined");
			console.log("----- " + value.session_key);
			console.log(value);
			console.log("key: " + key + " | key.length : " + key.length);
			u = environnement.users[value.session_key];
		}
		else {
			console.log("new User");
			console.log("----- " + value.login);
			console.log("key: " + key + " | key.length : " + key.length);
			u = new User(value.session_key, value.login, value.contact);
		}
		return u;
	}
	else if(value.author instanceof User){
		console.log("author");
		console.log(value);
		console.log("key: " + key + " | key.length : " + key.length);
		return value;
	}
	else if(key == "date"){
		console.log("key == date");
		console.log("----- " + value);
		console.log("key: " + key + " | key.length : " + key.length);
		var d = new Date(value);
		return (d);
	}
	else{
		console.log("else...");
		console.log("----- " + value);
		console.log("key: " + key + " | key.length : " + key.length);
		return (value);
	}

}


/* Affichage Commentaire */

/* Constructor et getHtml pour RechercheCommentaire */
function RechercheCommentaire(resultats, recherche, contacts_only, auteur, date) {
	console.log("RechercheCommentaire start...");
	this.resultats = resultats;
	this.auteur = auteur;
	this.recherche = " ";
	if(recherche != undefined) {
		this.recherche = recherche;
	}
	this.date = date;
	if(date == undefined) {
		this.date = new Date();
	}
	this.contacts_only = contacts_only;
	if(contacts_only == undefined) {
		this.contacts_only = false;
	}
	environnement.recherche = this;
	console.log("RechercheCommentaire ok");
}

RechercheCommentaire.prototype.getHtml = function(){
	var s = "<div id = \"comments\">\n";
	for(var i = 0; i < this.resultats.length; i++) {
		s+= this.resultats[i].getHtml() + "\n";
	}
	s+= "</div>";
	return (s);
}

function envoiCommentaire(){
	var u1 = new User(1,"bidule", true);
	var u2 = new User(2,"machin", false);
	var u3 = new User(3,"truc");
	var c1 = new Comment(23, u2, "blabla", new Date(), 45);
	var c2 = new Comment(5,  u1, "pouet",  new Date(), 27);
	var c3 = new Comment(7,  u3, "hello",  new Date(), 18);
	var tab = [c1, c2, c3];
	console.log(tab);
	var rec = new RechercheCommentaire(tab, "qqc", false, u1);
	console.log(rec);
	return (JSON.stringify(rec));
}

function showHideLogoMenu(){
	if(logoMenuVisible == true){
		hideLogoMenu();
		hideLogOutZone();
		logoMenuVisible = false;
	}
	else {
		showLogoMenu();
		showLogOutZone();
		logoMenuVisible = true;
	}
}
function getWidthWindow(){
	if (document.body)
		larg = (document.body.clientWidth);
	else
		larg = (window.innerWidth);
	return larg;
}
function getHeightWindow(){
	if(document.body)
		haut = (document.body.clientHeight);
	else
		haut = (window.innerHeight);
	return haut;
}
function adpaterALaTailleDeLaFenetre (){
	var largeur = getWidthWindow();
	var hauteur = getHeightWindow();
	console.log("hauteur:" + hauteur);
	console.log("largeur:" + largeur);
	jQuery('.background_img').css('width',largeur+'px');
	jQuery('.background_img').css('height',hauteur+'px');
	jQuery('body').css('width',largeur+'px');
	jQuery('body').css('height',hauteur+'px');
	jQuery('.background_img').css('position','fixed');
}
function addEvent(element, type, listener){
  if(element.addEventListener){
    element.addEventListener(type, listener, false);
  }else if(element.attachEvent){
    element.attachEvent("on"+type, listener);
	}
}
 
// On exécute la fonction une première fois au chargement de la page
addEvent(window, "load", adpaterALaTailleDeLaFenetre);
// Puis à chaque fois que la fenêtre est redimensionnée
addEvent(window, "resize", adpaterALaTailleDeLaFenetre);


function showHideLogZone(){
	// On attend que la page soit chargée 
	jQuery(document).ready(function()
			{
		// On cache la zone de texte
		jQuery('#log_zone').hide();
		// toggle() lorsque le log_zoneButton est cliqué
		jQuery('#log_zoneButton').click(function(){
			jQuery('#log_zone').toggle(200);
			return false;
		});
	});
}


function showHideSignUpZone(){
	jQuery('#enregistrement_form').hide();
	var isVisible = false;
// On attend que la page soit chargée 
	jQuery(document).ready(function(){
		// On cache la zone de texte
		// toggle() lorsque le lien avec l'ID #toggler est cliqué
		jQuery('#signUpZone_button').click(function(){
			if(isVisible){
				jQuery('#enregistrement_form').slideUp();
				isVisible = false;
				return false;
			}
			else{
				jQuery('#enregistrement_form').slideDown();
				isVisible = true;
				return false;
			}
		});
	});
}

function hideWelcome_txt(){
	jQuery('#welcome_txt').fadeOut();
	hideLogZone();
	hideSignUpZone();
	showTimeline(3000);
}
function clickOnLogOut(){
	hideTimeline(1500);
	hideLogOutZone();
	showLogZone();
	setSignUpZone();
	setTimeout(showWelcome_txt(), 1500);
}
function showWelcome_txt(){
	jQuery('#welcome_txt').fadeIn();
}
function showLogoMenu(){
	jQuery('#menuLogo').fadeIn(500);
	showLogOutZone();
	logoMenuVisible = true;
}
function hideLogoMenu(){
	jQuery('#menuLogo').fadeOut(500);
	jQuery('#menuLogo').hide();
	hideLogOutZone();
	logoMenuVisible = false;
}
function showLogOutZone(){
		jQuery('#logOut_zone').fadeIn(500);
}
function hideLogOutZone(){
		jQuery('#logOut_zone').fadeOut(500);
		jQuery('#logOut_zone').hide();
}
function hideLogZone(){
		jQuery('#log_zone').hide();
		jQuery('#log_zoneButton').animate({width: 'toggle'}, 1500);
}
function showLogZone(){
		jQuery('#log_zoneButton').animate({width: 'toggle'}, 1500);
		jQuery('#log_zoneButton').show();
		jQuery('#log_zone').hide();
}

function hideSignUpZone(){
		jQuery('#enregistrement_form').hide();
		jQuery('#signUpZone_button').fadeOut();
}
function setSignUpZone(){
		jQuery('#signUpZone_button').fadeIn();
		jQuery('#signUpZone_button').show();
		jQuery('#enregistrement_form').hide();
}

function showTimeline(tps){
	console.log("showTimeline starts");
	jQuery('#contents').css('opacity','1');
	jQuery('#timeline').slideDown(tps);
	jQuery('#timeline').css('display','inline-block');
	jQuery('#timeline').css('width','80%');
	jQuery('#timeline').css('margin-left','10%');
	jQuery('#timeline').css('margin-right','10%');
	jQuery('#timeline').css('z-index','-1');
	jQuery('#timeline').css('opacity', "1");
	
	jQuery('#header').prepend("<button id = \"friendsButton\" class = \"needed\">Friends</button>");

	$("#friendsButton").click(function(){
		friendsListIsVisible = false;
		showHideFriendsMenu();
	});
	console.log("showTimeline ends");
}
function hideTimeline(tps){
	jQuery('#friendsButton').remove();
	jQuery('#timeline').slideUp(tps);
	jQuery('#contents').css('opacity','0.88');
}
