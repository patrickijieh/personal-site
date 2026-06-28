async function get_last_updated_date() {
    let date;
    try {
        date = await fetch("/last-updated", {
            method: "GET"
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error();
                }
                return response.json();
            })
            .then(data => { return data.last_updated; });
    } catch (err) {
        console.error(err);
        return;
    }

    document.getElementById("last-updated").innerHTML = `Last updated on: ${date}`;
}
