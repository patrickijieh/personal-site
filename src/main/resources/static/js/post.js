async function get_post() {
    const postId = window.location.pathname.split("/")[2];

    let res = await fetch("/blog/posts/" + postId, {
        method: "GET"
    })
    .then(response => response.json())
    .then(data => {return data;});

    return res;
}

async function display_post() {
    const post = await get_post();
    if (!post) {
        return;
    }

    const title = post.title;
    const author = post.createdBy;
    const date = new Date(post.createdAt);
    const body = post.postBody;

    const title_elem  = document.getElementById("title");
    const author_elem = document.getElementById("author");
    const date_elem = document.getElementById("date");
    const body_elem = document.getElementById("body");

    title_elem.innerText = title;
    author_elem.innerText = `by: ${author}`;
    date_elem.innerText = `created on: ${date.toLocaleDateString()} at ${date.toLocaleTimeString()}`;

    const paragraph = document.createElement("p");
    const span = document.createElement("span");
    span.innerText = body;

    body_elem.appendChild(paragraph);
    paragraph.appendChild(span);
}

window.onload = display_post;
