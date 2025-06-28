async function submit_form() {
    let user = document.getElementById("username").value;
    let pass = document.getElementById("password").value;
    let verify_pass = document.getElementById("verify_password").value;

    if (pass !== verify_pass) {
        return;
    }

    let res, err, message;

    try {
        res = await fetch("/admin/users", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "username": user,
                "password": pass
            })
        })
        .then(response => {
                if (!response.ok)
                    throw new Error();

                return response.json();
            })
        .then(data => {return data;});
        message = "success!";
    } catch (error) {
        err = error;
        message = "error";
    }
    console.log(res);

    form_message(message, err);
}

function form_message(message, err) {
    const validation = document.getElementById("validation");
    let color = "green";
    if (err) {
        color = "red";
    }

    validation.innerText = message;
    validation.setAttribute("style", `color: ${color};`);
}
