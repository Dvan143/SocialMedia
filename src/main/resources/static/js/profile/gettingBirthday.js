fetch('/socialmedia/api/getMyBirthday')
    .then(resp => resp.text())
    .then(birthday => {
        const birthdaySpan = document.getElementById('birthday')
        if(!birthday){
            birthdaySpan.innerText='Not entered'
        } else{
            birthdaySpan.innerText=birthday;
        }
    })