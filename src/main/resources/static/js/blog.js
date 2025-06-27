const options = {
    root: null,
    rootMargin: "0px",
    threshold: 1.0,
};

const handleIntersect = (entries) => {
    entries.forEach((entry) => {
        if (entry.isIntersecting) {
            console.log("Element is visible!");
            if (document.getElementById("last-post"))
                get_new_blog_page();
        }
    });
}

async function get_new_blog_page() {

    let last_post = document.getElementById("last-post");
    
    let val = last_post.getAttribute("value").split("/");
    let id = val[0];
    let datetime = val[1];
    let res = await fetch("/blog/posts/newpage", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            "id": id,
            "createdAt" : datetime
        })
    })
    .then(response => {
        if (!response.ok) {
            return;
        }
        return response.json();
    })
    .then(data => {return data;});

    console.log(res);

    if (!res) {
        cleanup();
        return;
    }

    append_posts(res.posts);
}

function cleanup() {
    document.getElementById("target").remove;
}

function setup_blog() {
    get_posts();

    const target = document.getElementById("target");
    if (!target) 
        console.log("no target was found!");

    let observer = new IntersectionObserver(handleIntersect, options);

    observer.observe(target);
}

function create_post_elements(postList) {
    let post_elements = [];
    for (let i = 0; i < postList.length; i++) {
        let post = postList[i];
        let element = document.createElement("div");
        let title = document.createElement("h2");
        let createdAt = document.createElement("p");
        let createdBy = document.createElement("p");
        title.innerText = post.title;
        createdBy.innerText = post.createdBy;
        createdAt.innerText = new Date(post.createdAt).toLocaleString();
        element.setAttribute("value", post.id+"/"+post.createdAt);
        element.setAttribute("class", "post-link");
        element.appendChild(title);;
        element.appendChild(createdBy);
        element.appendChild(createdAt);
        if (i == postList.length-1) {
            let old_last_post = document.getElementById("last-post");
            if (old_last_post) {
                old_last_post.removeAttribute("id");
            }
            element.setAttribute("id", "last-post");
        }
        post_elements.push(element);
    }
    return post_elements;
}

function append_posts(postsList) {
    let elements = create_post_elements(postsList);
    let post_section = document.getElementById("posts");
    elements.forEach(element => {
        post_section.appendChild(element);
    });

}

async function get_posts() {
    let res = await fetch("/blog/posts", {
        method: "GET"
    })
    .then(response => {
        if (!response.ok) {
            return;
        }
        return response.json();
    })
    .then(data => {return data;});

    console.log(res);

    if (!res) {
        console.log("error");
        cleanup();
        return;
    }
    append_posts(res.posts);
}

window.onload = setup_blog;
