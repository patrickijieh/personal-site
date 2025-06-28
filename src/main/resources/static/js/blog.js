let observer = null;

const options = {
    root: null,
    rootMargin: "0px",
    threshold: 1.0,
};

const handleIntersect = (entries) => {
    entries.forEach((entry) => {
        if (entry.isIntersecting) {
            if (document.getElementById("last-post"))
                get_new_blog_page();
        }
    });
}

async function get_new_blog_page() {

    const last_post = document.getElementById("last-post");
    
    const val = last_post.getAttribute("value").split("/");
    const id = val[0];
    const datetime = val[1];
    let res;
    try {
        res = await fetch("/blog/posts/newpage", {
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
                throw new Error();
            }
            return response.json();
        })
        .then(data => {return data;});
    } catch (err) {
        cleanup();
        return;
    }

    append_posts(res.posts);
}

function cleanup() {
    const target = document.getElementById("target");
    if (observer) {
        observer.disconnect();
    }
    const paragraph = document.createElement("p");
    paragraph.innerHTML = "I was wrong. You're not greedy...<br /><br />\
        ...<br /><br />\
        ...<br /><br />\
        you're BATS*** INSANE!";

    target.appendChild(paragraph);
    paragraph.setAttribute("style", "text-align: center;");
    target.setAttribute("style", "display: grid; justify-items: center;");
}

function setup_blog() {
    get_posts();

    const target = document.getElementById("target");
    if (!target) {
        console.log("no target was found!");
        return;
    }

    observer = new IntersectionObserver(handleIntersect, options);
    observer.observe(target);
}

function create_post_elements(postList) {
    const post_elements = [];
    for (let i = 0; i < postList.length; i++) {
        const post = postList[i];
        const root_element = document.createElement("div");
        const anchor = document.createElement("a");
        const title = document.createElement("h2");
        const createdAt = document.createElement("p");
        const createdBy = document.createElement("p");

        title.innerText = post.title;
        createdBy.innerText = `authored by: ${post.createdBy}`;
        createdAt.innerText = new Date(post.createdAt).toLocaleString();
        root_element.setAttribute("value", `${post.id}/${post.createdAt}`);
        anchor.setAttribute("class", "post-link");
        anchor.setAttribute("href", `/blog/${post.id}`);

        anchor.appendChild(title);;
        anchor.appendChild(createdBy);
        anchor.appendChild(createdAt);
        root_element.appendChild(anchor);

        // add last-post tag to last element
        if (i == postList.length-1) {
            const old_last_post = document.getElementById("last-post");
            if (old_last_post) {
                old_last_post.removeAttribute("id");
            }
            root_element.setAttribute("id", "last-post");
        }
        post_elements.push(root_element);
    }
    return post_elements;
}

function append_posts(postsList) {
    const elements = create_post_elements(postsList);
    const post_section = document.getElementById("posts");
    elements.forEach(element => {
        post_section.appendChild(element);
    });
}

async function get_posts() {
    let res;
    try {
        res = await fetch("/blog/posts", {
            method: "GET"
        })
        .then(response => {
            if (!response.ok) {
                throw new Error();
            }
            return response.json();
        })
        .then(data => {return data;});
    } catch (err) {
        cleanup();
        console.error(err);
        return;
    }
    append_posts(res.posts);
}

window.onload = setup_blog;
