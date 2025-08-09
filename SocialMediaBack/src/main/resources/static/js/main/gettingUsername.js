fetch('/socialmedia/api/getMyUsername')
    .then(resp => resp.text())
        .then(username => {
            const usernameSpan = document.getElementById('username')
            usernameSpan.innerText=username;
        })