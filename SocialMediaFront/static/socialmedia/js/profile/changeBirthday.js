function changeBirthday() {
    const newBirthday = document.getElementById('newBirthdayDate').value;
    fetch('/socialmedia/api/setMyBirthday?birthday='+newBirthday)
        .then(resp => {
            if(resp.status===403) window.location='/socialmedia/login'
        })
    setTimeout(() => {
        location.reload()
    }, 1000)
}