function changeBirthday() {
    const newBirthday = document.getElementById('newBirthdayDate').value;
    fetch('/socialmedia/api/setMyBirthday?birthday='+newBirthday)
    setTimeout(() => {
        location.reload()
    }, 1000)
}