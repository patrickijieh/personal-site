async function submit_form() {
    let user = document.getElementById("username").value;
    let pass = document.getElementById("password").value;
    let res = await fetch("/login", {
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

    window.location = "/"+res.location;
}
