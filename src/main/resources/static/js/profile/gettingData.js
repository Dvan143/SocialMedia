fetch('/socialmedia/api/getMyData') // birthday email isVerifiedEmail
    .then(resp => resp.json())
    .then(resp => {
        const birthdaySpan = document.getElementById('birthday')
        const emailSpan = document.getElementById('email')
        const verifySpan = document.getElementById('verifyContainer')

        const birthday = resp.birthday
        const email = resp.email
        const verified = resp.isEmailVerified

        emailSpan.innerText = email

        if (birthday=='None') {
            birthdaySpan.innerText = 'Not entered'
        } else {
            birthdaySpan.innerText = birthday;
        }

            if (verified == 'true') {
                    verifySpan.innerHTML = `<div>Email is verified</div>`
            } else {
                    verifySpan.innerHTML = `
                <div>Email is unverified</div>
                <button onclick="sendMail()">Verify</button>
                `
            }
    })

function sendMail(){
        const verifySpan = document.getElementById('verifyContainer')
        void fetch('/socialmedia/api/verifyMyEmail')
        verifySpan.innerHTML=`
                <div>Email is unverified</div>
                <div>Mail has been sent</div>
                <input type="text" id="secretCode" name="secretCode" placeholder="Code from mail" minlength="6" maxlength="6" required>
                <button onclick="checkCode()">Send</button>
                `
}

function checkCode(){
        const verifySpan = document.getElementById('verifyContainer')
        const code = document.getElementById('secretCode').value
        fetch('/socialmedia/api/checkEmailCode?code='+code)
            .then(resp => resp.text())
            .then(resp => {
                    if(resp == 'false'){
                            verifySpan.innerHTML=`
                            <div>Email is unverified</div>
                            <div>Wrong code</div>
                            <input type="text" id="secretCode" name="secretCode" placeholder="Code from mail" minlength="6" maxlength="6" required>
                            <button onclick="checkCode()">Send</button>
                            `
                    } else if(resp == 'true') {
                        verifySpan.innerHTML=`
                            <div>Email is verified</div>
                            `
                    }
            })
}