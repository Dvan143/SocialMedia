fetch('/socialmedia/api/getMyUsername')
    .then(resp => {
        if(resp.status===403) window.location='/socialmedia/login'
        return resp.text();
    })
            .then(username => {
            const usernameSpan = document.getElementById('username')
            usernameSpan.innerText=username;
        })