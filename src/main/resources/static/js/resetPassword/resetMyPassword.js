function sendMail(){
    const email = document.getElementById('email-textbox').value;
    const divForm = document.getElementById('email-form')
    fetch('/socialmedia/api/resetMyPassword?email='+email)
        .then(resp => resp.text())
        .then((resp)=>{
            if(resp=='true'){
                divForm.innerHTML=`
            <div>Message has been sent</div>
            `
            } else if(resp=='false') {
                divForm.innerHTML=`
            <div>Email is not registered</div>
            `
            } else{
                divForm.innerHTML=`
            <div>Something went wrong.</div>
            `
            }
        })
}