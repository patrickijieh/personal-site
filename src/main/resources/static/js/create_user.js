async function submit_form() {
    let user = document.getElementById("username").value;
    let pass = document.getElementById("password").value;
    let verify_pass = document.getElementById("verify_password").value;

    if (pass !== verify_pass) {
        return;
    }

    let res = await fetch("/admin/users", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "username": user,
            "password": pass
        })
    })
    .then(response => response.json())
    .then(data => {return data;});

    console.log(res);
}
