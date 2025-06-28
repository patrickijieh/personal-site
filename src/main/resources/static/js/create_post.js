async function submit_post() {
    let title = document.getElementById("title").value;
    let post_body = document.getElementById("post_body").value;
    let res, err, message;

    try {
        res = await fetch("/admin/posts", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                "postBody": post_body,
                "postTitle": title
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
