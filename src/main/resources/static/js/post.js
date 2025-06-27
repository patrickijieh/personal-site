async function get_post() {
    const postId = window.location.pathname.split("/")[2];
    console.log(postId);
    let res = await fetch("/blog/posts/" + postId, {
        method: "GET"
    })
    .then(response => response.json())
    .then(data => {return data;});

    console.log(res);
}

window.onload = get_post;
