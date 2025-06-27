async function submit_post() {
    let title = document.getElementById("title").value;
    let post_body = document.getElementById("post_body").value;
    let res = await fetch("/admin/posts", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "postTitle": title,
            "postBody": post_body
        })
    })
    .then(response => response.json())
    .then(data => {return data;});

    console.log(res);
}
